package com.ravello.plugins.common;

import com.ravello.plugins.exceptions.ApplicationNotFoundException;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public interface ApplicationService {

	void publish(long appId, String preferredCloud, String preferredZone)
			throws ApplicationPublishException;

	void setRestClient(ApplicationRestService restService);

	void awaitForPublish(long appId, long timeout)
			throws ApplicationPublishException, ApplicationWrongStateException;

	boolean isPublishing(long appId) throws ApplicationPublishException,
			ApplicationWrongStateException, ApplicationNotFoundException;

	Application findApplication(long appId) throws ApplicationNotFoundException;

	Application findApplication(String appName)
			throws ApplicationNotFoundException;

}
