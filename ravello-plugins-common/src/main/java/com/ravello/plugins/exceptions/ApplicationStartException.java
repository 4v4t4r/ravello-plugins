package com.ravello.plugins.exceptions;

public class ApplicationStartException extends RavelloPluginException {

	public ApplicationStartException(Throwable e) {
		super(e);
	}

	public ApplicationStartException(String message) {
		super(message);
	}

	public ApplicationStartException(String message, Exception e) {
		super(message, e);
	}

}
