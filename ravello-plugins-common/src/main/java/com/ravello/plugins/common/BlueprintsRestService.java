package com.ravello.plugins.common;

import com.ravello.plugins.exceptions.BlueprintNotFoundException;

public interface BlueprintsRestService {

	Application findBlueprint(String blueprintName)
			throws BlueprintNotFoundException;

	Application createApplication(long blueprintId, String appName);

}
