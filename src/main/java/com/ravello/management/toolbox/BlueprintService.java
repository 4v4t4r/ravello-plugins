package com.ravello.management.toolbox;

import com.ravello.auto.mgmt.rest.RestClient;
import com.ravello.management.plugin.exceptions.ApplicationCreateException;
import com.ravello.management.plugin.exceptions.BlueprintNotFoundException;

public interface BlueprintService {

	Application createApplication(String blueprintName, String appName)
			throws BlueprintNotFoundException, ApplicationCreateException;

	Application createApplication(long blueprintId, String appName)
			throws ApplicationCreateException;

	void setRestClient(RestClient restClient);

}
