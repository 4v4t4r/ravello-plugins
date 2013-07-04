package com.ravello.plugins.common;

import com.ravello.plugins.exceptions.ApplicationNotFoundException;

public interface ApplicationRestService {

	void publish(long appId, String preferredCloud, String preferredZone);

	Application findApplication(String appName)
			throws ApplicationNotFoundException;

	Application findApplication(long appId) throws ApplicationNotFoundException;

	void start(long appId);

	void stop(long appId);

	void delete(long appId);

}
