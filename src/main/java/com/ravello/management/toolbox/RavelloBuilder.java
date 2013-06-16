package com.ravello.management.toolbox;

import com.ravello.auto.mgmt.rest.ApplicationClient;
import com.ravello.auto.mgmt.rest.BlueprintsClient;
import com.ravello.auto.mgmt.rest.RestClient;
import com.ravello.management.toolbox.impl.ApplicationServiceImpl;
import com.ravello.management.toolbox.impl.BlueprintServiceImpl;

public final class RavelloBuilder {

	private static RestClient restClient;

	private RavelloBuilder() {
	}

	public static final RavelloBuilder get(Credentials credentials) {
		restClient = getRestClient(credentials);
		return new RavelloBuilder();
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
