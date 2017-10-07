package org.idreamlands.dt.message.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class Message implements Serializable {

	private static final long serialVersionUID = 1757377457814546156L;

	@Id
	@GeneratedValue(generator = "messageGenerator")      
	@GenericGenerator(name = "messageGenerator", strategy = "uuid")  
	String id;

	private Integer version = 0;

	private String status;

	private String creater;

	@Column(name = "create_time")
	private Date createTime = new Date();

	private String editor;

	private Date editTime;

	private String remark;

	@Column(name = "message_id")
	private String messageId = UUID.randomUUID().toString().replace("-", "");;

	@Column(name = "message_body")
	private String messageBody;

	@Column(name = "message_data_type")
	private String messageDataType;

	@Column(name = "consumer_queue")
	private String consumerQueue;

	@Column(name = "message_send_times")
	private Integer messageSendTimes;

	private String areadlyDead;

	private String field1;

	private String field2;

	private String field3;

	public Message() {
		super();
	}

	public Message(String messageId, String messageBody, String consumerQueue) {
		super();
		this.messageId = messageId;
		this.messageBody = messageBody;
		this.consumerQueue = consumerQueue;
	}

	public void addSendTimes() {
		messageSendTimes++;
	}

}