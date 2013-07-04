package com.ravello.plugins.exceptions;

public class ApplicationDeleteException extends RavelloPluginException {

	public ApplicationDeleteException(Throwable e) {
		super(e);
	}

	public ApplicationDeleteException(String message) {
		super(message);
	}

	public ApplicationDeleteException(String message, Exception e) {
		super(message, e);
	}

}
