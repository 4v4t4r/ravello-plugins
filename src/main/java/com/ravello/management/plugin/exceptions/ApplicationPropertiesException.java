package com.ravello.management.plugin.exceptions;

public class ApplicationPropertiesException extends Exception {

	public ApplicationPropertiesException(Throwable e) {
		super(e);
	}

	public ApplicationPropertiesException(String message) {
		super(message);
	}

	public ApplicationPropertiesException(String message, Exception e) {
		super(message, e);
	}

}
