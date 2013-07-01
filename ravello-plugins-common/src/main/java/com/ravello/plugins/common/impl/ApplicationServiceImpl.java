package com.ravello.plugins.common.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.ApplicationRestService;
import com.ravello.plugins.common.ApplicationService;
import com.ravello.plugins.exceptions.ApplicationNotFoundException;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public class ApplicationServiceImpl implements ApplicationService {

	private ApplicationRestService restService;

	@Override
	public void publish(long appId, String preferredCloud, String preferredZone)
			throws ApplicationPublishException {
		try {
			this.restService.publish(appId, preferredCloud, preferredZone);
		} catch (Exception e) {
			throw new ApplicationPublishException(e);
		}
	}

	@Override
	public void awaitForPublish(long appId, long timeout)
			throws ApplicationPublishException, ApplicationWrongStateException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Object> future = executor.submit(new Task(appId));
		try {
			future.get(timeout, TimeUnit.MINUTES);
		} catch (TimeoutException e) {
			throw new ApplicationPublishException(e);
		} catch (Exception ex) {
			if (ex.getCause() instanceof ApplicationPublishException)
				throw new ApplicationPublishException(ex);
			if (ex.getCause() instanceof ApplicationWrongStateException)
				throw new ApplicationWrongStateException(ex);
			throw new ApplicationPublishException(ex);
		} finally {
			executor.shutdownNow();
		}
	}

	@Override
	public boolean isPublishing(long appId) throws ApplicationPublishException,
			ApplicationWrongStateException, ApplicationNotFoundException {
		Application application = restService.findApplication(appId);
		return application.getVmsState().contains(false);
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

	private final class Task implements Callable<Object> {
		long appId;

		Task(long appId) {
			this.appId = appId;
		}

		@Override
		public Object call() throws Exception {
			do {
				Thread.sleep(30000);
			} while (isPublishing(appId));
			return "Ready";
		}
	}

	@Override
	public void setRestClient(ApplicationRestService restService) {
		this.restService = restService;
	}
}
