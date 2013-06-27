package com.ravello.plugins.common.impl;

import java.util.List;
import java.util.Map;

import com.ravello.auto.rest.client.common.types.RestResponse;
import com.ravello.management.common.dtos.application.ApplicationDto;
import com.ravello.management.common.dtos.application.ApplicationPropertiesDto;
import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.BlueprintService;
import com.ravello.plugins.common.BlueprintsRestService;
import com.ravello.plugins.exceptions.ApplicationCreateException;
import com.ravello.plugins.exceptions.BlueprintNotFoundException;

public class BlueprintServiceImpl implements BlueprintService {

	private BlueprintsRestService restService;

	@SuppressWarnings("unchecked")
	@Override
	public Application createApplication(String blueprintName, String appName)
			throws BlueprintNotFoundException, ApplicationCreateException {
		RestResponse<List<ApplicationPropertiesDto>> response = restService
				.getBlueprints().to(RestResponse.class);
		List<ApplicationPropertiesDto> propertiesList = response.getDto();
		for (ApplicationPropertiesDto properties : propertiesList) {
			if (properties.getName().trim().equalsIgnoreCase(blueprintName))
				return createApplication(properties.getId(), appName);
		}
		throw new BlueprintNotFoundException(blueprintName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Application createApplication(long blueprintId, String appName)
			throws ApplicationCreateException {
		ApplicationPropertiesDto properties = null;
		try {
			RestResponse<ApplicationDto> response = restService
					.createApplication(blueprintId, appName).to(
							RestResponse.class);
			ApplicationDto dto = response.getDto();
			properties = dto.getApplicationProperties();
		} catch (Exception e) {
			throw new ApplicationCreateException(e);
		} catch (Throwable t) {
			throw new ApplicationCreateException(t);
		}
		return new ApplicationImpl(properties);
	}

	final class ApplicationImpl implements Application {

		private ApplicationPropertiesDto properties;

		ApplicationImpl(ApplicationPropertiesDto properties) {
			this.properties = properties;
		}

		@Override
		public String getName() {
			return properties.getName();
		}

		@Override
		public long getId() {
			return properties.getId();
		}

		@Override
		public Map<String, String> getVmsDNS() {
			return null;
		}
	}

	@Override
	public void setRestClient(BlueprintsRestService restService) {
		this.restService = restService;
	}

}
