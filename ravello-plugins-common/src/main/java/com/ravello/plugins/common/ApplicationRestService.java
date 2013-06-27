package com.ravello.plugins.common;


public interface ApplicationRestService {

	void publish(long appId, String preferredCloud, String preferredZone);

	RestResult getApplicationInstance(long appId);

	RestResult getApplicationsList();


}
