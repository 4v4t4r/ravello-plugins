package com.ravello.plugins.exceptions;

public class BlueprintNotFoundException extends RavelloPluginException {

	public BlueprintNotFoundException(Throwable e) {
		super(e);
	}

	public BlueprintNotFoundException(String message) {
		super(message);
	}

	public BlueprintNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
