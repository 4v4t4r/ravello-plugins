package com.ravello.plugins.exceptions;

public class ApplicationNotFoundException extends RavelloPluginException {

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
