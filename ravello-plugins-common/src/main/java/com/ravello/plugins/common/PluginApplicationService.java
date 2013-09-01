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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ravello.plugins.common.Application.STATE;
import com.ravello.plugins.exceptions.ApplicationDeleteException;
import com.ravello.plugins.exceptions.ApplicationNotFoundException;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationStartException;
import com.ravello.plugins.exceptions.ApplicationStopException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public class PluginApplicationService implements ApplicationService {

	private ApplicationRestService restService;
	private Map<Application.STATE, StateTask> stateTasks = new HashMap<Application.STATE, StateTask>();

	public PluginApplicationService() {
		this.stateTasks.put(STATE.STARTED, new StartedTask());
		this.stateTasks.put(STATE.STOPPED, new StoppedTask());
	}

	@Override
	public void publishCostOptimized(long appId, int autoStop)
			throws ApplicationPublishException {
		try {
			restService.publishCostOptimized(appId, autoStop);
		} catch (Exception e) {
			throw new ApplicationPublishException(e);
		}
	}

	@Override
	public void publishPerformanceOptimized(long appId, int autoStop)
			throws ApplicationPublishException {
		try {
			restService.publishPerformanceOptimized(appId, autoStop);
		} catch (Exception e) {
			throw new ApplicationPublishException(e);
		}
	}

	@Override
	public void publish(long appId, String preferredCloud,
			String preferredZone, int autoStop)
			throws ApplicationPublishException {
		try {
			restService.publish(appId, preferredCloud, preferredZone, autoStop);
		} catch (Exception e) {
			throw new ApplicationPublishException(e);
		}
	}

	@Override
	public void delete(long appId) throws ApplicationDeleteException {
		try {
			restService.delete(appId);
		} catch (Exception e) {
			throw new ApplicationDeleteException(e);
		}
	}

	@Override
	public void stop(long appId) throws ApplicationStopException {
		try {
			restService.stop(appId);
		} catch (Exception e) {
			throw new ApplicationStopException(e);
		}
	}

	@Override
	public void start(long appId, int autoStop)
			throws ApplicationStartException {
		try {
			restService.start(appId, autoStop);
		} catch (Exception e) {
			throw new ApplicationStartException(e);
		}
	}

	@Override
	public void awaitForApplicationState(long appId, long timeout, STATE state)
			throws ApplicationStartException, ApplicationWrongStateException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		StateTask stateTask = stateTasks.get(state);
		stateTask.appId = appId;
		Future<Object> future = executor.submit(stateTask);
		try {
			future.get(timeout, TimeUnit.MINUTES);
		} catch (TimeoutException e) {
			throw new ApplicationStartException(e);
		} catch (Exception ex) {
			if (ex.getCause() instanceof ApplicationPublishException)
				throw new ApplicationStartException(ex);
			if (ex.getCause() instanceof ApplicationWrongStateException)
				throw new ApplicationWrongStateException(ex);
			throw new ApplicationStartException(ex);
		} finally {
			executor.shutdownNow();
		}
	}

	@Override
	public boolean checkState(long appId, STATE state)
			throws ApplicationPublishException, ApplicationWrongStateException,
			ApplicationNotFoundException {
		Application application = restService.findApplication(appId);
		return application.compareVmsState(state).contains(false);
	}

	@Override
	public Application findApplication(String appName)
			throws ApplicationNotFoundException {
		return restService.findApplication(appName);
	}

	@Override
	public Application findApplication(long appId)
			throws ApplicationNotFoundException {
		return restService.findApplication(appId);
	}

	private abstract class StateTask implements Callable<Object> {
		long appId;

		abstract STATE getState();

		@Override
		public Object call() throws Exception {
			do {
				Thread.sleep(30000);
			} while (checkState(appId, getState()));
			return "Ready";
		}
	}

	private final class StartedTask extends StateTask {
		@Override
		STATE getState() {
			return STATE.STARTED;
		}
	}

	private final class StoppedTask extends StateTask {
		@Override
		STATE getState() {
			return STATE.STOPPED;
		}
	}

	@Override
	public void setRestClient(ApplicationRestService restService) {
		this.restService = restService;
	}

}
