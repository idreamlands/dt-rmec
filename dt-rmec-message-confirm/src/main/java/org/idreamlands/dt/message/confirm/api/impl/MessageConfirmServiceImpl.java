package org.idreamlands.dt.message.confirm.api.impl;

import java.util.List;

import org.idreamlands.dt.message.api.MessageService;
import org.idreamlands.dt.message.confirm.BeanFactory;
import org.idreamlands.dt.message.confirm.api.MessageConfirmService;
import org.idreamlands.dt.message.confirm.api.SenderConfirmService;
import org.idreamlands.dt.message.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageConfirmServiceImpl implements MessageConfirmService {

	@Autowired
	private MessageService messageService;

	@Autowired
	private SenderConfirmService senderConfirmService;

	@Override
	public void handle(List<Message> messages) {
		
		for (Message message : messages) {
			try {
				if (senderConfirmService.test(message)) {
					messageService.confirmAndSend(message.getMessageId());
				} else {
					messageService.deleteByMessageId(message.getMessageId());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
