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

package com.ravellosystems.plugins.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ravellosystems.api.RavelloApplication;
import com.ravellosystems.api.RavelloApplicationClient;
import com.ravellosystems.api.RavelloRestService;
import com.ravellosystems.plugins.exceptions.ApplicationDeleteException;
import com.ravellosystems.plugins.exceptions.ApplicationNotFoundException;
import com.ravellosystems.plugins.exceptions.ApplicationStartException;
import com.ravellosystems.plugins.exceptions.ApplicationStopException;
import com.ravellosystems.plugins.exceptions.ApplicationWrongStateException;

public class PluginApplicationRestService implements ApplicationRestService {

	private RavelloApplicationClient client;

	public PluginApplicationRestService(RavelloApplicationClient client) {
		this.client = client;
	}

	@Override
	public void delete(long appId) throws ApplicationDeleteException {
		try {
			RavelloRestService.deleteApplication(client, appId);
		} catch (Throwable e) {
			throw new ApplicationDeleteException(e);
		}
	}

	@Override
	public void start(long appId, int autoStop)
			throws ApplicationStartException {
		try {
			RavelloRestService.startApplication(client, appId, autoStop);
		} catch (Throwable e) {
			throw new ApplicationStartException(e);
		}
	}

	@Override
	public void stop(long appId) throws ApplicationStopException {
		try {
			RavelloRestService.stopApplication(client, appId);
		} catch (Throwable e) {
			throw new ApplicationStopException(e);
		}
	}

	@Override
	public Application findApplication(String appName)
			throws ApplicationNotFoundException {
		RavelloApplication ravelloApplication = RavelloRestService
				.findApplication(client, appName);
		if (ravelloApplication == null)
			throw new ApplicationNotFoundException(appName + " not found.");
		return findApplication(ravelloApplication.getId());
	}

	@Override
	public Application findApplication(long appId)
			throws ApplicationNotFoundException {
		RavelloApplication ravelloApplication = RavelloRestService
				.findApplication(client, appId);
		if (ravelloApplication == null)
			throw new ApplicationNotFoundException(appId + " not found.");
		return new ApplicationImpl(ravelloApplication);
	}

	@Override
	public void publishPerformanceOptimized(String preferredCloud,
			String preferredZone, long appId, int autoStop) {
		RavelloRestService.publishPerformanceOptimized(client, preferredCloud,
				preferredZone, appId, autoStop);
	}

	@Override
	public void publishCostOptimized(long appId, int autoStop) {
		RavelloRestService.publishCostOptimized(client, appId, autoStop);
	}

	@Override
	public void publish(long appId, String preferredCloud,
			String preferredZone, int autoStop) {
		RavelloRestService.publish(client, appId, preferredCloud,
				preferredZone, autoStop);
	}

	final class ApplicationImpl implements Application {

		private RavelloApplication ravelloApplication;

		public ApplicationImpl(RavelloApplication ravelloApplication) {
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
		public Map<String, String> getVMsDNS()
				throws ApplicationWrongStateException {
			Map<String, String> vmsDNS = RavelloRestService
					.getVMsDNS(ravelloApplication);
			if (vmsDNS == null)
				throw new ApplicationWrongStateException(
						String.format(
								"Can't find runtime info of %s application. Is started?",
								getName()));
			return vmsDNS;
		}

		@Override
		public void validateVMsState() throws ApplicationWrongStateException {
			try {
				Set<String> errorVMStates = RavelloRestService
						.getErrorVMStates();
				Map<String, String> vmsState = RavelloRestService
						.getVMsState(ravelloApplication);
				for (String error : errorVMStates) {
					if (vmsState.containsValue(error))
						throw new ApplicationWrongStateException(String.format(
								"One of VM application %s got error ",
								getName(), error));
				}
			} catch (Exception e) {
				throw new ApplicationWrongStateException(e);
			}
		}

		@Override
		public Set<Boolean> compareVmsState(Application.STATE state)
				throws ApplicationWrongStateException {
			try {
				Map<String, String> vmsState = RavelloRestService
						.getVMsState(ravelloApplication);
				Set<String> keySet = vmsState.keySet();
				Set<Boolean> vmsStateCompareResults = new HashSet<Boolean>();

				for (String vmName : keySet)
					vmsStateCompareResults.add(vmsState.get(vmName).trim()
							.equalsIgnoreCase(state.name().trim()));

				return vmsStateCompareResults;
			} catch (Exception e) {
				throw new ApplicationWrongStateException(e);
			}
		}

	}

}
