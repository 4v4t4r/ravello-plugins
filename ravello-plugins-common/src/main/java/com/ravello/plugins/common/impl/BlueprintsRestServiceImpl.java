package com.ravello.plugins.common.impl;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static com.ravello.plugins.common.Utils.safeIterNext;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;

import com.ravello.auto.mgmt.rest.BlueprintsClient;
import com.ravello.auto.rest.client.common.types.RestResponse;
import com.ravello.management.common.dtos.application.ApplicationDto;
import com.ravello.management.common.dtos.application.ApplicationPropertiesDto;
import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.BlueprintsRestService;
import com.ravello.plugins.exceptions.BlueprintNotFoundException;

public class BlueprintsRestServiceImpl implements BlueprintsRestService {

	private BlueprintsClient client;

	public BlueprintsRestServiceImpl(BlueprintsClient client) {
		this.client = client;
	}

	@Override
	public Application findBlueprint(String blueprintName)
			throws BlueprintNotFoundException {
		RestResponse<List<ApplicationPropertiesDto>> response = client
				.getAllVisibleBlueprints();
		List<ApplicationPropertiesDto> propertiesList = response.getDto();
		final ApplicationPropertiesDto propertiesDto = safeIterNext(select(
				propertiesList,
				having(on(ApplicationPropertiesDto.class).getName(),
						Matchers.equalTo(blueprintName.trim()))));
		return new Blueprint(blueprintName, propertiesDto);

	}

	@Override
	public Application createApplication(long blueprintId, final String appName) {
		RestResponse<ApplicationDto> response = client
				.createAppInstanceFromBlueprint(blueprintId, appName);
		ApplicationDto applicationDto = response.getDto();
		ApplicationPropertiesDto propertiesDto = applicationDto
				.getApplicationProperties();
		return new Blueprint(propertiesDto);
	}

	final class Blueprint implements Application {

		private ApplicationPropertiesDto propertiesDto;

		public Blueprint(String blueprintName,
				ApplicationPropertiesDto propertiesDto)
				throws BlueprintNotFoundException {
			if (propertiesDto == null)
				throw new BlueprintNotFoundException(blueprintName);
			this.propertiesDto = propertiesDto;
		}

		public Blueprint(ApplicationPropertiesDto propertiesDto) {
			this.propertiesDto = propertiesDto;
		}

		@Override
		public String getName() {
			return propertiesDto.getName();
		}

		@Override
		public long getId() {
			return propertiesDto.getId();
		}

		@Override
		public Map<String, String> getVmsDNS() {
			return null;
		}

		@Override
		public Set<Boolean> getVmsState() {
			return new HashSet<Boolean>();
		}
	}
}
