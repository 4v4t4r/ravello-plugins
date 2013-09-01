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
import com.ravello.management.common.dtos.publish.PublishOptimizationDto;
import com.ravello.management.common.dtos.vm.GuestStateDto;
import com.ravello.management.common.dtos.vm.VmDto;
import com.ravello.management.common.dtos.vm.VmPropertiesDto;
import com.ravello.management.common.dtos.vm.VmRuntimeInformation;
import com.ravello.plugins.exceptions.ApplicationNotFoundException;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public class PluginApplicationRestService implements ApplicationRestService {

	private ApplicationClient client;

	public PluginApplicationRestService(ApplicationClient client) {
		this.client = client;
	}

	@Override
	public void delete(long appId) {
		client.deleteApplicationInstance(appId);
	}

	@Override
	public void start(long appId, int autoStop) {
		setAppExpiration(appId, autoStop);
		ApplicationDto applicationDto = findApplicationDto(appId);
		List<VmDto> vms = applicationDto.getVms();
		for (VmDto vmDto : vms) {
			client.startGuest(appId, vmDto.getVmProperties().getId());
		}
	}

	@Override
	public void stop(long appId) {
		ApplicationDto applicationDto = findApplicationDto(appId);
		List<VmDto> vms = applicationDto.getVms();
		for (VmDto vmDto : vms) {
			client.stopGuest(appId, vmDto.getVmProperties().getId());
		}
	}

	private ApplicationPropertiesDto findApplicationPropertiesDto(String appName)
			throws ApplicationNotFoundException {
		RestResponse<List<ApplicationPropertiesDto>> response = client
				.getApplicationsList();
		List<ApplicationPropertiesDto> propertiesList = response.getDto();
		ApplicationPropertiesDto propertiesDto = safeIterNext(select(
				propertiesList,
				having(on(ApplicationPropertiesDto.class).getName(),
						Matchers.equalTo(appName.trim()))));
		if (propertiesDto == null)
			throw new ApplicationNotFoundException(appName);
		return propertiesDto;
	}

	@Override
	public Application findApplication(String appName)
			throws ApplicationNotFoundException {
		ApplicationPropertiesDto propertiesDto = findApplicationPropertiesDto(appName);
		return findApplication(propertiesDto.getId());
	}

	@Override
	public Application findApplication(long appId) {
		ApplicationDto applicationDto = findApplicationDto(appId);
		return new ApplicationImpl(applicationDto);
	}

	private ApplicationDto findApplicationDto(long appId) {
		RestResponse<ApplicationDto> response = client
				.getApplicationInstance(appId);
		return response.getDto();
	}

	@Override
	public void publishPerformanceOptimized(long appId, int autoStop) {
		setAppExpiration(appId, autoStop);
		client.publish(appId, "", "",
				PublishOptimizationDto.PERFORMANCE_OPTIMIZED);
	}

	@Override
	public void publishCostOptimized(long appId, int autoStop) {
		setAppExpiration(appId, autoStop);
		client.publish(appId, "", "", PublishOptimizationDto.COST_OPTIMIZED);
	}

	@Override
	public void publish(long appId, String preferredCloud,
			String preferredZone, int autoStop) {
		setAppExpiration(appId, autoStop);
		client.publish(appId, preferredCloud, preferredZone);
	}

	private void setAppExpiration(long appId, int autoStop) {
		if (autoStop > 0)
			client.setApplicationExpirationTime(appId, autoStop);
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
		public Map<String, String> getVmsDNS(DNSNameTrimmer trimmer)
				throws ApplicationWrongStateException {
			List<VmDto> vms = applicationDto.getVms();
			Map<String, String> map = new HashMap<String, String>();
			for (VmDto vmDto : vms) {
				VmPropertiesDto vmProperties = vmDto.getVmProperties();
				VmRuntimeInformation runtimeInformation = vmProperties
						.getRuntimeInformation();

				if (runtimeInformation == null) {
					throw new ApplicationWrongStateException(
							String.format(
									"Can't find runtime info of %s application. Is started?",
									getName()));
				}

				runtimeInformation.getExternalFqdn();
				map.put(trimmer.trim(vmProperties.getName()),
						runtimeInformation.getExternalFqdn());
			}
			return map;
		}

		@Override
		public Set<Boolean> compareVmsState(Application.STATE state)
				throws ApplicationPublishException,
				ApplicationWrongStateException {
			List<VmDto> vms = applicationDto.getVms();
			Set<Boolean> vmsStates = new HashSet<Boolean>();
			for (VmDto vmDto : vms)
				vmsStates.add(compareVmState(vmDto, state));
			return vmsStates;
		}

		private boolean compareVmState(VmDto vmDto, STATE state)
				throws ApplicationPublishException,
				ApplicationWrongStateException {
			VmPropertiesDto vmProps = vmDto.getVmProperties();
			VmRuntimeInformation runtimeInformation = vmProps
					.getRuntimeInformation();
			GuestStateDto vmState = runtimeInformation.getState();
			String name = vmProps.getName();

			switch (vmState) {
			case ERROR:
				throw new ApplicationPublishException(String.format(
						"VM %s ERROR!", name));
			case STOPPED:
				throw new ApplicationWrongStateException(String.format(
						"VM %s STOPED!", name));
			case TERMINATED:
				throw new ApplicationWrongStateException(String.format(
						"VM %s TERMINATED!", name));
			default:
				return vmState.name().equals(state.name());
			}

		}

	}

}
