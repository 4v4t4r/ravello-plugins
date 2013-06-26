package com.ravello.plugins.common;

import java.util.Map;

public interface Application {

	long getId();

	String getName();

	Map<String, String> getVmsDNS();

}
