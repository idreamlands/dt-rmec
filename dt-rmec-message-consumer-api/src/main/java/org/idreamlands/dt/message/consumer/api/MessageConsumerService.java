package org.idreamlands.dt.message.consumer.api;

import org.idreamlands.dt.message.entity.Message;

public interface MessageConsumerService {
	
	default boolean handle(Message message) {
		return true;
	}
}
