package com.ravello.plugins.common;

import com.ravello.auto.mgmt.rest.RestClient;
import com.ravello.plugins.exceptions.ApplicationNotFoundException;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

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
