package com.ravello.management.plugin.exceptions;

public class ApplicationPublishException extends Exception {

	public ApplicationPublishException(Throwable e) {
		super(e);
	}

	public ApplicationPublishException(String message) {
		super(message);
	}

	public ApplicationPublishException(String message, Exception e) {
		super(message, e);
	}

}
