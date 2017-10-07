package org.idreamlands.dt.message.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.idreamlands.dt.message.MessageException;
import org.idreamlands.dt.message.MessageStatusEnum;
import org.idreamlands.dt.message.api.MessageService;
import org.idreamlands.dt.message.dto.MessageCondition;
import org.idreamlands.dt.message.entity.Message;
import org.idreamlands.dt.message.repository.MessageRepository;
import org.idreamlands.dt.message.repository.spec.MessageSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Service;

@RestController
@Service(version = "1.0.0", protocol = { "dubbo", "feign" }, timeout = 10000)
@Transactional(rollbackFor = Exception.class)
@RequestMapping(value = "/message/")
public class JdbcMessageServiceImpl implements MessageService {

	@Autowired
	@Qualifier("sourceChannel")
	private MessageChannel channel;

	@Autowired
	private MessageRepository repo;

	@Value("${spring.dtrmec.message.maxsendtimes}")
	private int maxTimes = 0;

	private void sendMessage(Object body) {
		channel.send(MessageBuilder.withPayload(body).build());
	}

	@Override
	public int saveWaitingConfirm(@RequestBody Message message) throws MessageException {
		if (message == null) {
			throw new MessageException("保存的消息为空");
		}

		if (StringUtils.isEmpty(message.getConsumerQueue())) {
			throw new MessageException("消息的消费队列不能为空 ");
		}

		message.setEditTime(new Date());
		message.setStatus(MessageStatusEnum.WAITING_CONFIRM.name());
		message.setAreadlyDead("否");
		message.setMessageSendTimes(0);
		repo.save(message);
		return 1;
	}

	@Override
	public void confirmAndSend(@PathVariable("messageId") String messageId) throws MessageException {
		final Message message = repo.findOneByMessageId(messageId);
		if (message == null) {
			throw new MessageException("根据消息id查找的消息为空");
		}

		message.setStatus(MessageStatusEnum.SENDING.name());
		message.setEditTime(new Date());
		repo.save(message);

		sendMessage(message);
	}

	@Override
	public int saveAndSend(@RequestBody Message message) throws MessageException {
		if (message == null) {
			throw new MessageException("保存的消息为空");
		}

		if (StringUtils.isEmpty(message.getConsumerQueue())) {
			throw new MessageException("消息的消费队列不能为空 ");
		}

		message.setStatus(MessageStatusEnum.SENDING.name());
		message.setAreadlyDead("否");
		message.setMessageSendTimes(0);
		message.setEditTime(new Date());

		repo.save(message);
		sendMessage(message);

		return 1;
	}

	@Override
	public void directSend(@RequestBody Message message) throws MessageException {
		if (message == null) {
			throw new MessageException("保存的消息为空");
		}

		if (StringUtils.isEmpty(message.getConsumerQueue())) {
			throw new MessageException("消息的消费队列不能为空 ");
		}

		sendMessage(message);
	}

	@Override
	public void reSend(@RequestBody Message message) throws MessageException {
		if (message == null) {
			throw new MessageException("保存的消息为空");
		}

		if (StringUtils.isEmpty(message.getConsumerQueue())) {
			throw new MessageException("消息的消费队列不能为空 ");
		}

		message.addSendTimes();
		message.setEditTime(new Date());
		repo.save(message);

		sendMessage(message);
	}

	@Override
	public void reSendByMessageId(@PathVariable("messageId") String messageId) throws MessageException {
		final Message message = repo.findOneByMessageId(messageId);
		if (message == null) {
			throw new MessageException("根据消息id查找的消息为空");
		}

		if (message.getMessageSendTimes() >= maxTimes) {
			message.setAreadlyDead("是");
		}

		message.setEditTime(new Date());
		message.setMessageSendTimes(message.getMessageSendTimes() + 1);
		repo.save(message);

		sendMessage(message);
	}

	@Override
	public void setToAreadlyDead(@PathVariable("messageId") String messageId) throws MessageException {
		final Message message = repo.findOneByMessageId(messageId);
		if (message == null) {
			throw new MessageException("根据消息id查找的消息为空");
		}

		message.setAreadlyDead("是");
		message.setEditTime(new Date());
		repo.save(message);
	}

	@Override
	public Message getByMessageId(String messageId) throws MessageException {
		return repo.findOneByMessageId(messageId);
	}

	@Override
	public void deleteByMessageId(@PathVariable("messageId") String messageId) throws MessageException {
		repo.deleteByMessageId(messageId);
	}

	@Override
	public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws MessageException {

		int numPerPage = 1000;
		if (batchSize > 0 && batchSize < 100) {
			numPerPage = 100;
		} else if (batchSize >= 100 && batchSize < 5000) {
			numPerPage = batchSize;
		} else if (batchSize >= 5000) {
			numPerPage = 5000;
		} else {
			numPerPage = 1000;
		}

		MessageCondition condition = new MessageCondition();
		condition.setConsumerQueue(queueName);
		condition.setAreadlyDead("是");
		List<Message> messages = getMessagePaging(numPerPage, Integer.MIN_VALUE, condition);

		for (Message message : messages) {
			message.setEditTime(new Date());
			message.setMessageSendTimes(message.getMessageSendTimes() + 1);
			repo.save(message);

			sendMessage(message);
		}
	}

	@Override
	public List<Message> getMessagePaging(@RequestParam("pageSize") int pageSize, @RequestParam("maxPageCount") int maxPageCount,
			@RequestBody MessageCondition condition) {

		List<Message> messages = new ArrayList<Message>();

		Page<Message> pageBean = repo.findAll(new MessageSpec(condition), new PageRequest(0, pageSize, new Sort(Direction.ASC, "createTime")));
		messages.addAll(pageBean.getContent());
		int pageCout = pageBean.getTotalPages() > maxPageCount ? pageBean.getTotalPages() : maxPageCount;

		for (int pageNum = 1; pageNum < pageCout; pageNum++) {
			messages.addAll(
					repo.findAll(new MessageSpec(condition), new PageRequest(pageNum, pageSize, new Sort(Direction.ASC, "createTime"))).getContent());
		}

		return messages;
	}

	@Override
	public Page<Message> listPage(Pageable pageable, MessageCondition condition) throws MessageException {
		Page<Message> m = repo.findAll(new MessageSpec(condition), pageable);

		return m;
	}

}
