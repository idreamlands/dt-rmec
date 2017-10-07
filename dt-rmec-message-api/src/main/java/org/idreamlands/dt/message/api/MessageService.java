package org.idreamlands.dt.message.api;

import java.util.List;

import org.idreamlands.dt.message.MessageException;
import org.idreamlands.dt.message.dto.MessageCondition;
import org.idreamlands.dt.message.entity.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.boot.dubbo.DubboClient;
import com.alibaba.dubbo.config.annotation.Reference;

@FeignClient(path = "/message")
@DubboClient(protocol="dubbo", value = @Reference(timeout = 10000, version = "1.0.0"))
public interface MessageService {

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public int saveWaitingConfirm(@RequestBody Message message) throws MessageException;

	@PutMapping(value = "/confirmAndSend/{messageId}")
	public void confirmAndSend(@PathVariable("messageId") String messageId) throws MessageException;

	@PostMapping("/saveAndSend")
	public int saveAndSend(@RequestBody Message message) throws MessageException;

	@PostMapping("/directSend")
	public void directSend(@RequestBody Message message) throws MessageException;

	@PostMapping("/reSend")
	public void reSend(@RequestBody Message message) throws MessageException;

	@GetMapping("/reSendByMessageId")
	public void reSendByMessageId(String messageId) throws MessageException;

	@RequestMapping(path = "/setToAreadlyDead/{messageId}", method = RequestMethod.PUT)
	public void setToAreadlyDead(@PathVariable("messageId") String messageId) throws MessageException;

	@GetMapping("/{messageId}")
	public Message getByMessageId(@PathVariable("messageId") String messageId) throws MessageException;

	@RequestMapping(path = "/{messageId}", method = RequestMethod.DELETE)
	public void deleteByMessageId(@PathVariable("messageId") String messageId) throws MessageException;

	@GetMapping("/reSendAllDeadMessageByQueueName")
	public void reSendAllDeadMessageByQueueName(@RequestParam("queueName") String queueName, @RequestParam("batchSize") int batchSize)
			throws MessageException;

	@RequestMapping(path = "/getMessagePaging", method = RequestMethod.POST)
	public List<Message> getMessagePaging(@RequestParam("pageSize") int pageSize, @RequestParam("maxPageCount") int maxPageCount,
			@RequestBody MessageCondition condition);

	@GetMapping("/listPage")
	Page<Message> listPage(@RequestParam("pageable") Pageable pageable, @RequestParam("message") MessageCondition message) throws MessageException;

}
