package com.ravello.plugins.common;

public interface BlueprintsRestService {

	RestResult getBlueprints();

	RestResult createApplication(long blueprintId, String appName);

}
