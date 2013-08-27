package com.ravello.plugins.common;

import java.util.Map;
import java.util.Set;

import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public interface Application {

	public enum STATE {
		STARTED, STOPPED;
	}

	long getId();

	String getName();

	Map<String, String> getVmsDNS(DNSNameTrimmer trimmer) throws ApplicationWrongStateException;

	Set<Boolean> compareVmsState(Application.STATE state)
			throws ApplicationPublishException, ApplicationWrongStateException;

	interface DNSNameTrimmer {
		String trim(String name);
	}

}
