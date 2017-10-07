package org.idreamlands.dt.message.recovery;


import org.idreamlands.dt.MessageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.boot.dubbo.EnableDubboAutoConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(MessageProperties.class)
@EnableScheduling
@EnableDubboAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
