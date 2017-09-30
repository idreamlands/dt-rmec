package org.idreamlands.message.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.idreamlands.dt.message.MessageStatusEnum;
import org.idreamlands.dt.message.api.MessageService;
import org.idreamlands.dt.message.dto.MessageCondition;
import org.idreamlands.dt.message.entity.Message;
import org.idreamlands.message.admin.JSONUtil;
import org.idreamlands.message.admin.PageParam;
import org.idreamlands.message.admin.dto.Jui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/list")
	public String list(PageParam pageParam, MessageCondition message, Model model) {

		int pageNum = pageParam.getPageNum() == 0 ? 0 : pageParam.getPageNum() - 1;
		
		PageRequest pageable = new PageRequest(pageNum, pageParam.getNumPerPage());

		Page<Message> page = messageService.listPage(pageable, message);
		org.idreamlands.message.admin.Page<Message> p = new org.idreamlands.message.admin.Page<Message>(page.getTotalElements(), page.getTotalPages(),
				pageable.getPageNumber(), pageable.getPageSize(), page.getContent());
		model.addAttribute("page", p);

		model.addAttribute("messageStatus", MessageStatusEnum.toList());
		model.addAttribute("queues", MessageStatusEnum.toList());

		return "message/list";
	}

	@RequestMapping(value = "/sendMessage/{messageId}")
	@ResponseBody
	public String sendMessage(@PathVariable("messageId") String messageId, Model model) {
		messageService.reSendByMessageId(messageId);
		return JSONUtil.toJSONString(new Jui(200, "", "操作成功", ""));
	}

	@RequestMapping(value = "/sendAllMessage")
	@ResponseBody
	public String sendAllMessage(String queueName, Model model) {
		messageService.reSendAllDeadMessageByQueueName(queueName, 2000);

		return JSONUtil.toJSONString(new Jui(200, "", "操作成功", ""));
	}

}
