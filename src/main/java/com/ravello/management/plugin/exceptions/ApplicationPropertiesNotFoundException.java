package com.ravello.management.plugin.exceptions;

public class ApplicationPropertiesNotFoundException extends Exception {

	public ApplicationPropertiesNotFoundException(Throwable e) {
		super(e);
	}

	public ApplicationPropertiesNotFoundException(String message) {
		super(message);
	}

	public ApplicationPropertiesNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
