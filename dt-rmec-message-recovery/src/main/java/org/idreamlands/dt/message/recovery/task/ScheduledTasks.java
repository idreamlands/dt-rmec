package org.idreamlands.dt.message.recovery.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.idreamlands.dt.MessageProperties;
import org.idreamlands.dt.message.MessageStatusEnum;
import org.idreamlands.dt.message.api.MessageService;
import org.idreamlands.dt.message.dto.MessageCondition;
import org.idreamlands.dt.message.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageProperties messageProperties;

	@Scheduled(fixedRate = 15000)
	public void handleSendingTimeOutMessage() {

		MessageCondition condition = new MessageCondition();
		condition.setStatus(MessageStatusEnum.SENDING.name());
		condition.setCreateTime(new Date(Calendar.getInstance().getTimeInMillis() - messageProperties.getHandleduration() * 1000));
		condition.setAreadlyDead("否");

		handle(messageService.getMessagePaging(2000, 3, condition));

	}

	private void handle(List<Message> messages) {

		Map<Integer, Integer> notifyParam = messageProperties.getTimeinterval();
		for (Message message : messages) {
			try {
				// 判断发送次数
				int maxTimes = messageProperties.getMaxsendtimes();
				// 如果超过最大发送次数直接退出 标记为死亡
				if (maxTimes < message.getMessageSendTimes()) {
					messageService.setToAreadlyDead(message.getMessageId());
					continue;
				}
				// 判断是否达到发送消息的时间间隔条件
				int reSendTimes = message.getMessageSendTimes();
				int times = notifyParam.get(reSendTimes == 0 ? 1 : reSendTimes);
				long needTime = Calendar.getInstance().getTimeInMillis() - times * 60 * 1000;
				long hasTime = message.getEditTime().getTime();
				// 判断是否达到了可以再次发送的时间条件
				if (hasTime > needTime) {
					continue;
				}
				// 重新发送消息
				messageService.reSend(message);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
