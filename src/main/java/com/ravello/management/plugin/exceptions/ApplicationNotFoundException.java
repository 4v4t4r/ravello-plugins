package com.ravello.management.plugin.exceptions;

public class ApplicationNotFoundException extends Exception {

	public ApplicationNotFoundException(Throwable e) {
		super(e);
	}

	public ApplicationNotFoundException(String message) {
		super(message);
	}

	public ApplicationNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
