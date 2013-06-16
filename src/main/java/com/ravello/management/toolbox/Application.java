package com.ravello.management.toolbox;

import java.util.Map;

public interface Application {

	long getId();

	String getName();

	Map<String, String> getVmsDNS();

}
