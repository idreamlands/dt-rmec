package org.idreamlands.dt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(MessageProperties.class)
public class MessageAutoConfiguration {

	@Autowired
	private MessageProperties messageProperties;
}
