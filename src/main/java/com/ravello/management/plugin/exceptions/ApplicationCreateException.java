package com.ravello.management.plugin.exceptions;

public class ApplicationCreateException extends Exception {

	public ApplicationCreateException(Throwable e) {
		super(e);
	}

	public ApplicationCreateException(String message) {
		super(message);
	}

	public ApplicationCreateException(String message, Exception e) {
		super(message, e);
	}

}
