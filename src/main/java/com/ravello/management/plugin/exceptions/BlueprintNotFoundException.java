package com.ravello.management.plugin.exceptions;

public class BlueprintNotFoundException extends Exception {

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
