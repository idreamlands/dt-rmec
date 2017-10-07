package org.idreamlands.dt.message.confirm;

import org.idreamlands.dt.MessageProperties;
import org.idreamlands.dt.common.bean.dto.StatusConfirm;
import org.idreamlands.dt.message.confirm.api.SenderConfirmService;
import org.idreamlands.dt.message.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(MessageProperties.class)
@Configuration
public class ConfirmAutoConfiguration {

	@Autowired
	private MessageProperties messageProperties;

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	@ConditionalOnMissingBean
	public SenderConfirmService senderConfirmService() {

		try {
			Object obj = BeanFactory.getBean("senderConfirmServiceDubboClient");
			if (obj != null) {
				return (SenderConfirmService) obj;
			}
		} catch (Exception e) {
			// do nothing
		}

		if (messageProperties.isEnablehttp()) {
			return new SenderConfirmService() {
				@Override
				public boolean test(Message message) {
					try {
						StatusConfirm sc = restTemplate.getForObject(message.getField2(), StatusConfirm.class, message.getField1());
						if ("success".equals(sc.getStatus())) {
							return true;
						}
					} catch (Exception e) {
						return false;
					}
					return false;
				}
			};
		}

		return new SenderConfirmService() {
		};
	}

}
