/*
 *
 *	Copyright (c) 2013 Ravello Systems Ltd.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * 	you may not use this file except in compliance with the License.
 * 	You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 * 
 */

/**
 * 
 * @author Alex Nickolaevsky
 * */

package com.ravello.plugins.common;

import com.ravello.auto.mgmt.rest.ApplicationClient;
import com.ravello.auto.mgmt.rest.BlueprintsClient;
import com.ravello.auto.mgmt.rest.RestClient;

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
		ApplicationRestService restService = new PluginApplicationRestService(
				client);
		ApplicationService service = new PluginApplicationService();
		service.setRestClient(restService);
		return service;
	}

	public final BlueprintService blueprint() {
		BlueprintsClient client = new BlueprintsClient(restClient);
		BlueprintsRestService restService = new PluginBlueprintsRestService(
				client);
		BlueprintService service = new PluginBlueprintService();
		service.setRestClient(restService);
		return service;
	}

}
