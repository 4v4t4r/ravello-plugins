package com.ravello.plugins.common.impl;

import com.ravello.auto.mgmt.rest.BlueprintsClient;
import com.ravello.plugins.common.BlueprintsRestService;
import com.ravello.plugins.common.RestResult;

public class BlueprintsRestServiceImpl implements BlueprintsRestService {

	private BlueprintsClient client;

	public BlueprintsRestServiceImpl(BlueprintsClient client) {
		this.client = client;
	}

	@Override
	public RestResult getBlueprints() {
		return new RestResult() {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T to(Class<T> type) {
				return (T) client.getAllVisibleBlueprints();
			}
		};
	}

	@Override
	public RestResult createApplication(final long blueprintId,
			final String appName) {
		return new RestResult() {
			@SuppressWarnings("unchecked")
			@Override
			public <T> T to(Class<T> type) {
				return (T) client.createAppInstanceFromBlueprint(blueprintId,
						appName);
			}
		};
	}

}
