package com.ravello.plugins.common;

import com.ravello.auto.mgmt.rest.ApplicationClient;
import com.ravello.auto.mgmt.rest.BlueprintsClient;
import com.ravello.auto.mgmt.rest.RestClient;
import com.ravello.plugins.common.impl.ApplicationServiceImpl;
import com.ravello.plugins.common.impl.BlueprintServiceImpl;

public final class RavelloFabric {

	private static RestClient restClient;

	private RavelloFabric() {
	}

	public static final RavelloFabric get(Credentials credentials) {
		restClient = getRestClient(credentials);
		return new RavelloFabric();
	}

	private static RestClient getRestClient(Credentials credentials) {
		if (restClient == null) {
			restClient = new RestClient(credentials.getUrl());
			restClient.login(credentials.getUser(), credentials.getPassword());
		}
		return restClient;
	}

	public final ApplicationService application() {
		ApplicationClient client = new ApplicationClient(restClient);
		ApplicationService service = new ApplicationServiceImpl();
		service.setRestClient(client);
		return service;
	}

	public final BlueprintService blueprint() {
		BlueprintsClient client = new BlueprintsClient(restClient);
		BlueprintService service = new BlueprintServiceImpl();
		service.setRestClient(client);
		return service;
	}

}
