package com.icarat.amzons3.sample.exception;

public class HandleAmazonClientException extends Exception{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public HandleAmazonClientException(String message2) {
	this.message=message2;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
