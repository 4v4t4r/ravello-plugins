package com.ravello.plugins.common;

import com.ravello.auto.mgmt.rest.RestClient;
import com.ravello.plugins.exceptions.ApplicationCreateException;
import com.ravello.plugins.exceptions.BlueprintNotFoundException;

public interface BlueprintService {

	Application createApplication(String blueprintName, String appName)
			throws BlueprintNotFoundException, ApplicationCreateException;

	Application createApplication(long blueprintId, String appName)
			throws ApplicationCreateException;

	void setRestClient(RestClient restClient);

}
