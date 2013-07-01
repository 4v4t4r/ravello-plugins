package com.ravello.plugins.common.impl;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static com.ravello.plugins.common.Utils.safeIterNext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;

import com.ravello.auto.mgmt.rest.ApplicationClient;
import com.ravello.auto.rest.client.common.types.RestResponse;
import com.ravello.management.common.dtos.application.ApplicationDto;
import com.ravello.management.common.dtos.application.ApplicationPropertiesDto;
import com.ravello.management.common.dtos.vm.GuestStateDto;
import com.ravello.management.common.dtos.vm.VmDto;
import com.ravello.management.common.dtos.vm.VmPropertiesDto;
import com.ravello.management.common.dtos.vm.VmRuntimeInformation;
import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.ApplicationRestService;
import com.ravello.plugins.exceptions.ApplicationNotFoundException;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public class ApplicationRestServiceImpl implements ApplicationRestService {

	private ApplicationClient client;

	public ApplicationRestServiceImpl(ApplicationClient client) {
		this.client = client;
	}

	@Override
	public Application findApplication(String appName)
			throws ApplicationNotFoundException {
		RestResponse<List<ApplicationPropertiesDto>> response = client
				.getApplicationsList();
		List<ApplicationPropertiesDto> propertiesList = response.getDto();
		final ApplicationPropertiesDto propertiesDto = safeIterNext(select(
				propertiesList,
				having(on(ApplicationPropertiesDto.class).getName(),
						Matchers.equalTo(appName.trim()))));
		if (propertiesDto == null)
			throw new ApplicationNotFoundException(appName);
		return findApplication(propertiesDto.getId());
	}

	@Override
	public Application findApplication(long appId) {
		RestResponse<ApplicationDto> response = client
				.getApplicationInstance(appId);
		ApplicationDto applicationDto = response.getDto();
		return new ApplicationImpl(applicationDto);
	}

	@Override
	public void publish(long appId, String preferredCloud, String preferredZone) {
		client.publish(appId, preferredCloud, preferredZone);
	}

	final class ApplicationImpl implements Application {

		private ApplicationDto applicationDto;

		public ApplicationImpl(ApplicationDto applicationDto) {
			this.applicationDto = applicationDto;
		}

		public ApplicationImpl(String appName, ApplicationDto applicationDto)
				throws ApplicationNotFoundException {
			if (applicationDto == null)
				throw new ApplicationNotFoundException(appName);
			this.applicationDto = applicationDto;
		}

		@Override
		public String getName() {
			return applicationDto.getApplicationProperties().getName();
		}

		@Override
		public long getId() {
			return applicationDto.getApplicationProperties().getId();
		}

		@Override
		public Map<String, String> getVmsDNS() {
			List<VmDto> vms = applicationDto.getVms();
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
		public Set<Boolean> getVmsState() throws ApplicationPublishException,
				ApplicationWrongStateException {
			List<VmDto> vms = applicationDto.getVms();
			Set<Boolean> vmsStates = new HashSet<Boolean>();
			for (VmDto vmDto : vms)
				vmsStates.add(isVmStarted(vmDto));
			return vmsStates;
		}

		private boolean isVmStarted(VmDto vmDto)
				throws ApplicationPublishException,
				ApplicationWrongStateException {
			VmPropertiesDto vmProps = vmDto.getVmProperties();
			VmRuntimeInformation runtimeInformation = vmProps
					.getRuntimeInformation();
			GuestStateDto state = runtimeInformation.getState();

			switch (state) {
			case ERROR:
				throw new ApplicationPublishException(String.format(
						"VM %s ERROR!", vmProps.getName()));
			case STOPPED:
				throw new ApplicationWrongStateException(String.format(
						"VM %s STOPED!", vmProps.getName()));
			case TERMINATED:
				throw new ApplicationWrongStateException(String.format(
						"VM %s TERMINATED!", vmProps.getName()));
			case STARTED:
				return true;
			default:
				return false;
			}
		}
	}

}
