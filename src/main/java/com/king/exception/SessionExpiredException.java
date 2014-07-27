package com.king.exception;

public class SessionExpiredException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static String MESSAGE = "Session key already expired";
	
	public SessionExpiredException() {
		super(MESSAGE);
	}
	
	public SessionExpiredException(String message){
		super(message);
	}

}
