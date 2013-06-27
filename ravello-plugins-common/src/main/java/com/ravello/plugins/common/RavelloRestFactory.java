package com.ravello.plugins.common;

import com.ravello.auto.mgmt.rest.ApplicationClient;
import com.ravello.auto.mgmt.rest.BlueprintsClient;
import com.ravello.auto.mgmt.rest.RestClient;
import com.ravello.plugins.common.impl.ApplicationRestServiceImpl;
import com.ravello.plugins.common.impl.ApplicationServiceImpl;
import com.ravello.plugins.common.impl.BlueprintServiceImpl;
import com.ravello.plugins.common.impl.BlueprintsRestServiceImpl;

public final class RavelloRestFactory {

	private static RestClient restClient;

	private RavelloRestFactory() {
	}

	public static final RavelloRestFactory get(Credentials credentials) {
		restClient = getRestClient(credentials);
		return new RavelloRestFactory();
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
		ApplicationRestService restService = new ApplicationRestServiceImpl(
				client);
		ApplicationService service = new ApplicationServiceImpl();
		service.setRestClient(restService);
		return service;
	}

	public final BlueprintService blueprint() {
		BlueprintsClient client = new BlueprintsClient(restClient);
		BlueprintsRestService restService = new BlueprintsRestServiceImpl(
				client);
		BlueprintService service = new BlueprintServiceImpl();
		service.setRestClient(restService);
		return service;
	}

}
