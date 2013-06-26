package com.ravello.plugins.exceptions;

public class ApplicationPropertiesException extends RavelloPluginException {

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
