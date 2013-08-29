package com.ravello.plugins.common;

import com.ravello.plugins.exceptions.ApplicationNotFoundException;

public interface ApplicationRestService {

	void start(long appId, int autoStop);

	void stop(long appId);

	void delete(long appId);

	void publish(long appId, String preferredCloud, String preferredZone,
			int autoStop);

	void publishCostOptimized(long appId, int autoStop);

	void publishPerformanceOptimized(long appId, int autoStop);

	Application findApplication(String appName)
			throws ApplicationNotFoundException;

	Application findApplication(long appId) throws ApplicationNotFoundException;
}
