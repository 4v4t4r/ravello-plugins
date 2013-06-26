package com.ravello.plugins.exceptions;

public class ApplicationPropertiesNotFoundException extends RavelloPluginException {

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
