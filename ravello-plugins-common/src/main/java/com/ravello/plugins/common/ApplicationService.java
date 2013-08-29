package com.ravello.plugins.common;

import com.ravello.plugins.exceptions.ApplicationDeleteException;
import com.ravello.plugins.exceptions.ApplicationNotFoundException;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationStartException;
import com.ravello.plugins.exceptions.ApplicationStopException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public interface ApplicationService {

	void start(long appId, int autoStop) throws ApplicationStartException;

	void stop(long appId) throws ApplicationStopException;

	void delete(long appId) throws ApplicationDeleteException;

	void publish(long appId, String preferredCloud, String preferredZone,
			int autoStop) throws ApplicationPublishException;

	void publishCostOptimized(long appId, int autoStop)
			throws ApplicationPublishException;

	void publishPerformanceOptimized(long appId, int autoStop)
			throws ApplicationPublishException;

	void setRestClient(ApplicationRestService restService);

	void awaitForApplicationState(long appId, long timeout,
			Application.STATE state) throws ApplicationStartException,
			ApplicationWrongStateException;

	boolean checkState(long appId, Application.STATE state)
			throws ApplicationPublishException, ApplicationWrongStateException,
			ApplicationNotFoundException;

	Application findApplication(long appId) throws ApplicationNotFoundException;

	Application findApplication(String appName)
			throws ApplicationNotFoundException;

}
