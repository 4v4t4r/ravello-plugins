package com.ravello.plugins.exceptions;

public class ApplicationCreateException extends RavelloPluginException {

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
