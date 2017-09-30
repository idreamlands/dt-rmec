package org.idreamlands.dt.message.confirm;

import org.idreamlands.dt.message.confirm.api.SenderConfirmService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.boot.dubbo.EnableDubboAutoConfiguration;

@SpringBootApplication
@EnableScheduling
@EnableDubboAutoConfiguration
public class Application {

	private static ApplicationContext applicationContext;

	public static void main(String[] args) throws InterruptedException {
		applicationContext = SpringApplication.run(Application.class, args);
		Thread.sleep(6000);
	}

	@Bean
	public SenderConfirmService getSenderConfirmService() {
		SenderConfirmService service = null;

		try {
			service = applicationContext.getBean(SenderConfirmService.class);
		} catch (Exception e) {
			// do nothing
		}

		if (service != null) {
			return service;
		}
		return new SenderConfirmService() {
		};
	}
	
	public static ApplicationContext getContext() {
		return applicationContext;
	}
}
