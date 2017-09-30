package org.idreamlands.dt.message.repository.spec;

import org.idreamlands.dt.message.dto.MessageCondition;
import org.idreamlands.dt.message.entity.Message;
import org.idreamlands.dt.repository.spec.support.QueryWraper;
import org.idreamlands.dt.repository.spec.support.SimpleSpecification;

public class MessageSpec extends SimpleSpecification<Message, MessageCondition> {

	public MessageSpec(MessageCondition condition) {
		super(condition);
	}

	@Override
	protected void addCondition(QueryWraper<Message> queryWraper) {
		addLessThanCondition(queryWraper, "createTime");
		addEqualsCondition(queryWraper, "status", "status");
		addEqualsCondition(queryWraper, "areadlyDead");
	}

}
