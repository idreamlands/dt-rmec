package org.idreamlands.dt.message;

public class MessageException extends RuntimeException {

	private static final long serialVersionUID = 4308634181125808252L;

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Throwable e) {
		super(e);
	}

}
