package org.idreamlands.dt.message.dto;

import java.util.Date;

public class MessageCondition implements java.io.Serializable {
	private static final long serialVersionUID = 8241669858127193387L;

	private Date createTime;
	
	private String consumerQueue;

	private String areadlyDead;
	
	private String status;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getConsumerQueue() {
		return consumerQueue;
	}

	public void setConsumerQueue(String consumerQueue) {
		this.consumerQueue = consumerQueue;
	}

	public String getAreadlyDead() {
		return areadlyDead;
	}

	public void setAreadlyDead(String areadlyDead) {
		this.areadlyDead = areadlyDead;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
