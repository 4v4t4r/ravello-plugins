/*
 * 
 * Copyright (c) 2013 Ravello Systems Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 * @author Alex Nickolaevsky
 * */

package com.ravello.plugins.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ravello.auto.mgmt.rest.BlueprintsClient;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;
import com.ravello.plugins.exceptions.BlueprintNotFoundException;
import com.ravello.restapi.RavelloApplication;
import com.ravello.restapi.RavelloRestService;

public class PluginBlueprintsRestService implements BlueprintsRestService {

	private BlueprintsClient client;

	public PluginBlueprintsRestService(BlueprintsClient client) {
		this.client = client;
	}

	@Override
	public Application findBlueprint(String blueprintName) throws BlueprintNotFoundException {
		RavelloApplication ravelloApplication = RavelloRestService.findBlueprint(client, blueprintName);
		if (ravelloApplication == null)
			throw new BlueprintNotFoundException(blueprintName + " Blueprint not found.");
		return new Blueprint(ravelloApplication);
	}

	@Override
	public Application createApplication(long blueprintId, String appName) {
		RavelloApplication ravelloApplication = RavelloRestService.createApplication(client, blueprintId, appName);
		return new Blueprint(ravelloApplication);
	}

	final class Blueprint implements Application {

		private RavelloApplication ravelloApplication;

		public Blueprint(RavelloApplication ravelloApplication) {
			this.ravelloApplication = ravelloApplication;
		}

		@Override
		public String getName() {
			return ravelloApplication.getName();
		}

		@Override
		public long getId() {
			return ravelloApplication.getId();
		}

		@Override
		public Map<String, String> getVMsDNS() {
			return null;
		}

		@Override
		public void validateVMsState() throws ApplicationWrongStateException {

		}

		@Override
		public Set<Boolean> compareVmsState(STATE state) throws ApplicationWrongStateException {
			return new HashSet<Boolean>();
		}

	}
}
