package com.ravello.plugins.exceptions;

public class ApplicationStopException extends RavelloPluginException {

	public ApplicationStopException(Throwable e) {
		super(e);
	}

	public ApplicationStopException(String message) {
		super(message);
	}

	public ApplicationStopException(String message, Exception e) {
		super(message, e);
	}

}
