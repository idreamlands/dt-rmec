package org.idreamlands.dt.message.confirm.api;

import org.idreamlands.dt.message.entity.Message;

public interface SenderConfirmService {
	
	default boolean test(Message message) {
		return true;
	}
}
