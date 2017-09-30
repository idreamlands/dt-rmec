package org.idreamlands.dt.message.confirm.api;

import java.util.List;

import org.idreamlands.dt.message.entity.Message;

public interface MessageConfirmService {
	
	void handle(List<Message> messages);
}
