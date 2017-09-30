package org.idreamlands.dt.message.repository;

import org.idreamlands.dt.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, String>, JpaSpecificationExecutor<Message> {

	public Message findOneByMessageId(String messageId);
	
	@Modifying
	@Query("delete from Message where messageId = ?1")
	public void deleteByMessageId(String messageId);
	
}
