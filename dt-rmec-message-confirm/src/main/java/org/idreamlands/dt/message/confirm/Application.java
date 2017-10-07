package org.idreamlands.dt.message.confirm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.alibaba.boot.dubbo.EnableDubboAutoConfiguration;

@SpringBootApplication
@EnableScheduling
@EnableDubboAutoConfiguration
public class Application {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
