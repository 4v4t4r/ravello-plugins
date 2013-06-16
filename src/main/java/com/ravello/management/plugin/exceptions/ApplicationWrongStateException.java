package com.ravello.management.plugin.exceptions;

public class ApplicationWrongStateException extends Exception {

	public ApplicationWrongStateException(Throwable e) {
		super(e);
	}

	public ApplicationWrongStateException(String message) {
		super(message);
	}

	public ApplicationWrongStateException(String message, Exception e) {
		super(message, e);
	}

}
