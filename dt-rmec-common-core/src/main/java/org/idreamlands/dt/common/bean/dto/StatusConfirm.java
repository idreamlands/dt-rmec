package org.idreamlands.dt.common.bean.dto;

public class StatusConfirm implements java.io.Serializable {

	private static final long serialVersionUID = -4713317819339251608L;
	
	private String status;
	
	private String message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
