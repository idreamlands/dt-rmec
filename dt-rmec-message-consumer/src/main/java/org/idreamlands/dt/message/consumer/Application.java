package org.idreamlands.dt.message.consumer;

import org.idreamlands.dt.MessageProperties;
import org.idreamlands.dt.common.bean.dto.StatusConfirm;
import org.idreamlands.dt.message.consumer.api.MessageConsumerService;
import org.idreamlands.dt.message.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.alibaba.boot.dubbo.EnableDubboAutoConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(MessageProperties.class)
@EnableBinding
@EnableDubboAutoConfiguration
public class Application {

	private static ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
	}
	
	@Autowired
	private MessageProperties messageProperties;
	
	@Bean
	public MessageConsumerService getMessageConsumerService() {
		
		try {
			Object obj = applicationContext.getBean("messageConsumerServiceDubboClient");
			if (obj != null) {
				return (MessageConsumerService) obj;
			}
		} catch (Exception e) {
			// do nothing
		}
		RestTemplate restTemplate = new RestTemplate();

		if (messageProperties.isEnablehttp()) {
			return new MessageConsumerService() {
				@Override
				public boolean handle(Message message) {
					try {
						StatusConfirm sc = restTemplate.postForObject(message.getField3(), message, StatusConfirm.class);
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

		return new MessageConsumerService() {
		};
	}
}
