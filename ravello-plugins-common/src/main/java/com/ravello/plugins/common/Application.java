package com.ravello.plugins.common;

import java.util.Map;
import java.util.Set;

import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public interface Application {

	long getId();

	String getName();

	Map<String, String> getVmsDNS();

	Set<Boolean> getVmsState() throws ApplicationPublishException,
			ApplicationWrongStateException;

}
