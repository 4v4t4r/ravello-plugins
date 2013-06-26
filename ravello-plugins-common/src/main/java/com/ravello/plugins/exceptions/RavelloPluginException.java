package com.ravello.plugins.exceptions;

public class RavelloPluginException extends Exception {

	public RavelloPluginException(Throwable e) {
		super(e);
	}

	public RavelloPluginException(String message) {
		super(message);
	}

	public RavelloPluginException(String message, Exception e) {
		super(message, e);
	}

}
