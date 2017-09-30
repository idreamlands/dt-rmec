package org.idreamlands.dt.message.confirm.task;

import java.util.Calendar;
import java.util.Date;

import org.idreamlands.dt.message.MessageStatusEnum;
import org.idreamlands.dt.message.api.MessageService;
import org.idreamlands.dt.message.confirm.api.MessageConfirmService;
import org.idreamlands.dt.message.dto.MessageCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	@Autowired
	private MessageConfirmService messageConfirmService;

	@Autowired
	private MessageService messageService;

	@Value("${message.handle.duration}")
	private int duration;
	

	@Scheduled(fixedRate = 15000)
	public void handleWaitingConfirmTimeOutMessages() {
		
		MessageCondition condition = new MessageCondition();
		condition.setStatus(MessageStatusEnum.WAITING_CONFIRM.name());
		condition.setCreateTime(new Date(Calendar.getInstance().getTimeInMillis() - duration * 1000));

		messageConfirmService.handle(messageService.getMessagePaging(2000, 3, condition));

	}

}
