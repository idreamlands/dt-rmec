package org.idreamlands.dt.message.consumer;

import org.idreamlands.dt.message.consumer.api.MessageConsumerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.alibaba.boot.dubbo.EnableDubboAutoConfiguration;

@SpringBootApplication
@EnableBinding
@EnableDubboAutoConfiguration
public class Application {

	private static ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public MessageConsumerService getMessageConsumerService() {
		MessageConsumerService service = null;

		try {
			service = applicationContext.getBean(MessageConsumerService.class);
		} catch (Exception e) {
			// do nothing
		}

		if (service != null) {
			return service;
		}
		return new MessageConsumerService() {
		};
	}
}
