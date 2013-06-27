package com.ravello.plugins.common.impl;

import com.ravello.auto.mgmt.rest.ApplicationClient;
import com.ravello.plugins.common.ApplicationRestService;
import com.ravello.plugins.common.RestResult;

public class ApplicationRestServiceImpl implements ApplicationRestService {

	private ApplicationClient client;

	public ApplicationRestServiceImpl(ApplicationClient client) {
		this.client = client;
	}

	@Override
	public void publish(long appId, String preferredCloud, String preferredZone) {
		client.publish(appId, preferredCloud, preferredZone);
	}

	@Override
	public RestResult getApplicationInstance(final long appId) {
		return new RestResult() {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T to(Class<T> type) {
				return (T) client.getApplicationInstance(appId);
			}
		};
	}

	@Override
	public RestResult getApplicationsList() {
		return new RestResult() {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T to(Class<T> type) {
				return (T) client.getApplicationsList();
			}
		};
	}

}
