package com.ravello.management.toolbox;

import com.ravello.auto.mgmt.rest.RestClient;
import com.ravello.management.plugin.exceptions.ApplicationNotFoundException;
import com.ravello.management.plugin.exceptions.ApplicationPublishException;
import com.ravello.management.plugin.exceptions.ApplicationWrongStateException;

public interface ApplicationService {

	void publish(long appId, String preferredCloud, String preferredZone)
			throws ApplicationPublishException;

	void setRestClient(RestClient restClient);

	void awaitForPublish(long appId, long timeout)
			throws ApplicationPublishException, ApplicationWrongStateException;

	boolean isPublishing(long appId) throws ApplicationPublishException,
			ApplicationWrongStateException;

	Application findApplication(long appId);

	Application findApplication(String appName)
			throws ApplicationNotFoundException;

}
