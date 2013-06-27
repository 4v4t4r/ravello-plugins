package com.ravello.plugins.common.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ravello.auto.rest.client.common.types.RestResponse;
import com.ravello.management.common.dtos.application.ApplicationDto;
import com.ravello.management.common.dtos.application.ApplicationPropertiesDto;
import com.ravello.management.common.dtos.vm.GuestStateDto;
import com.ravello.management.common.dtos.vm.VmDto;
import com.ravello.management.common.dtos.vm.VmPropertiesDto;
import com.ravello.management.common.dtos.vm.VmRuntimeInformation;
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean isPublishing(long appId) throws ApplicationPublishException,
			ApplicationWrongStateException {
		RestResponse<ApplicationDto> response = restService
				.getApplicationInstance(appId).to(RestResponse.class);
		ApplicationDto dto = response.getDto();
		List<VmDto> vms = dto.getVms();
		Set<Boolean> vmStates = new HashSet<Boolean>();
		for (VmDto vmDto : vms)
			vmStates.add(isVmStarted(vmDto));
		return vmStates.contains(false);
	}

	private boolean isVmStarted(VmDto vmDto)
			throws ApplicationPublishException, ApplicationWrongStateException {
		VmPropertiesDto vmProperties = vmDto.getVmProperties();
		VmRuntimeInformation runtimeInformation = vmProperties
				.getRuntimeInformation();
		GuestStateDto state = runtimeInformation.getState();
		switch (state) {
		case ERROR:
			throw new ApplicationPublishException(String.format("VM %s ERROR!",
					vmProperties.getName()));
		case STOPPED:
			throw new ApplicationWrongStateException(String.format(
					"VM %s STOPED!", vmProperties.getName()));
		case TERMINATED:
			throw new ApplicationWrongStateException(String.format(
					"VM %s TERMINATED!", vmProperties.getName()));
		case STARTED:
			return true;
		default:
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Application findApplication(String appName)
			throws ApplicationNotFoundException {
		RestResponse<List<ApplicationPropertiesDto>> response = restService
				.getApplicationsList().to(RestResponse.class);
		List<ApplicationPropertiesDto> propertiesList = response.getDto();
		for (ApplicationPropertiesDto properties : propertiesList) {
			if (properties.getName().trim().equalsIgnoreCase(appName))
				return findApplication(properties.getId());
		}
		throw new ApplicationNotFoundException(appName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Application findApplication(long appId) {
		RestResponse<ApplicationDto> response = restService
				.getApplicationInstance(appId).to(RestResponse.class);
		final ApplicationPropertiesDto properties = response.getDto()
				.getApplicationProperties();
		final List<VmDto> vms = response.getDto().getVms();
		return new Application() {
			@Override
			public Map<String, String> getVmsDNS() {
				Map<String, String> map = new HashMap<String, String>();
				for (VmDto vmDto : vms) {
					VmPropertiesDto vmProperties = vmDto.getVmProperties();
					VmRuntimeInformation runtimeInformation = vmProperties
							.getRuntimeInformation();
					runtimeInformation.getExternalFqdn();
					map.put(vmProperties.getName(),
							runtimeInformation.getExternalFqdn());
				}
				return map;
			}

			@Override
			public String getName() {
				return properties.getName();
			}

			@Override
			public long getId() {
				return properties.getId();
			}
		};
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
