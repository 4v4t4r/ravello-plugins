package com.ravello.plugins.exceptions;

public class ApplicationPublishException extends RavelloPluginException {

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
