package org.idreamlands.dt.message;

import java.util.UUID;
import java.util.stream.IntStream;

import org.idreamlands.dt.message.api.MessageService;
import org.idreamlands.dt.message.dto.MessageCondition;
import org.idreamlands.dt.message.entity.Message;
import org.idreamlands.dt.message.repository.MessageRepository;
import org.idreamlands.dt.message.repository.spec.MessageSpec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JdbcMessageServiceImplTest {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageRepository repo;
	
	@Test
	@Rollback(false)
	public void saveWaitingConfirmTest() {
		IntStream.range(0, 100).forEach(i -> {
		String id = UUID.randomUUID().toString().replace("-", "");
		Message message = new Message(id, "test", "dt-rmec-message");
		messageService.saveWaitingConfirm(message);
		});
	}
	@Test
	@Rollback(false)
	public void confirmAndSendTest() {
		MessageCondition c = new MessageCondition();
		c.setStatus("WAITING_CONFIRM");
		Page<Message> page = repo.findAll(new MessageSpec(c), new PageRequest(1, 2));
		for(Message m : page.getContent()) {
			messageService.confirmAndSend(m.getMessageId());
		}
	}

	@Test
	@Rollback(false)
	public void saveAndSendTest() {
		
		IntStream.range(0, 100).forEach(i -> {
			String id = UUID.randomUUID().toString().replace("-", "");
			Message message = new Message(id, "test", "dt-rmec-message");
			messageService.saveAndSend(message);
		});
			
	}
	@Test
	public void directSend() {
		IntStream.range(0, 100).forEach(i -> {
			String id = UUID.randomUUID().toString().replace("-", "");
			Message message = new Message(id, "test", "dt-rmec-message");
			messageService.directSend(message);
		});
	}
//
//	public void reSend(Message message) throws MessageException;
//
	@Test
	@Rollback(false)
	public void reSendByMessageId() {
		MessageCondition c = new MessageCondition();
		c.setStatus("WAITING_CONFIRM");
		Page<Message> page = repo.findAll(new MessageSpec(c), new PageRequest(1, 1));
		final String messageId = page.getContent().get(0).getMessageId();
		IntStream.range(0, 10).forEach(i -> {
			messageService.reSendByMessageId(messageId);
		});
	}
//
//	public void setToAreadlyDead(String messageId) throws MessageException;
//
//	public Message getByMessageId(String messageId) throws MessageException;
//
//	public void deleteByMessageId(String messageId) throws MessageException;
//
//	public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws MessageException;
//
//	public List<Message> getMessagePaging(int pageSize, int maxPageCount, MessageCondition condition, Sort sort);
//
//	Page<Message> listPage(Pageable pageable, MessageCondition message) throws MessageException;
}
