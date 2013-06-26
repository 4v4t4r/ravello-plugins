package com.ravello.plugins.exceptions;

public class ApplicationWrongStateException extends RavelloPluginException {

	public ApplicationWrongStateException(Throwable e) {
		super(e);
	}

	public ApplicationWrongStateException(String message) {
		super(message);
	}

	public ApplicationWrongStateException(String message, Exception e) {
		super(message, e);
	}

}
