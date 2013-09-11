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

package com.ravellosystems.plugins.common;

import com.ravellosystems.plugins.exceptions.ApplicationCreateException;
import com.ravellosystems.plugins.exceptions.BlueprintNotFoundException;

public class PluginBlueprintService implements BlueprintService {

	private BlueprintsRestService restService;

	@Override
	public Application createApplication(String blueprintName, String appName)
			throws BlueprintNotFoundException, ApplicationCreateException {
		Application blueprint = restService.findBlueprint(blueprintName);
		return createApplication(blueprint.getId(), appName);
	}

	@Override
	public Application createApplication(long blueprintId, String appName)
			throws ApplicationCreateException {
		try {
			return restService.createApplication(blueprintId, appName);
		} catch (Exception e) {
			throw new ApplicationCreateException(e);
		} catch (Throwable t) {
			throw new ApplicationCreateException(t);
		}
	}

	@Override
	public void setRestClient(BlueprintsRestService restService) {
		this.restService = restService;
	}

}
