package com.ravello.plugins.common;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.ravello.auto.rest.client.common.types.RestResponse;
import com.ravello.management.common.dtos.application.ApplicationDto;
import com.ravello.management.common.dtos.application.ApplicationPropertiesDto;
import com.ravello.plugins.common.impl.BlueprintServiceImpl;
import com.ravello.plugins.exceptions.ApplicationCreateException;
import com.ravello.plugins.exceptions.RavelloPluginException;

@RunWith(MockitoJUnitRunner.class)
public class BlueprintServiceImplTest {

	@Mock
	private BlueprintsRestService restService;

	@Mock
	private RestResult restResult;

	@Mock
	private RestResponse<List<ApplicationPropertiesDto>> responseOfList;

	@Mock
	private RestResponse<ApplicationDto> responseOfApp;

	@Mock
	private ApplicationDto applicationDto;

	private List<ApplicationPropertiesDto> propertiesList;
	private ApplicationPropertiesDto propsDto;
	private BlueprintService service;
	private BlueprintService serviceSpy;
	private Application app = new ApplicationImpl();

	@Before
	public void setUp() throws Exception {
		this.propertiesList = new ArrayList<ApplicationPropertiesDto>();
		this.propsDto = new ApplicationPropertiesDto();
		this.propsDto.setName("bp");
		this.propsDto.setId(1L);
		this.propertiesList.add(propsDto);
		this.service = new BlueprintServiceImpl();
		this.serviceSpy = spy(service);
		this.serviceSpy.setRestClient(restService);
	}

	@After
	public void tearDown() throws Exception {
	}

	private static final class ApplicationImpl implements Application {
		@Override
		public Map<String, String> getVmsDNS() {
			return null;
		}

		@Override
		public String getName() {
			return "new-app";
		}

		@Override
		public long getId() {
			return 100;
		}
	};

	@Test
	public void testCreateApplicationUsingBlueprintName() {

		try {
			when(restResult.to(RestResponse.class)).thenReturn(responseOfList);
			when(restService.getBlueprints()).thenReturn(restResult);
			when(responseOfList.getDto()).thenReturn(propertiesList);
			doReturn(app).when(serviceSpy).createApplication(propsDto.getId(),
					app.getName());
			Application createdApp = serviceSpy.createApplication(
					propsDto.getName(), app.getName());
			Assert.notNull(createdApp,
					"Failed to create app using blueprint name");
		} catch (RavelloPluginException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateApplicationUsingBlueprintId() {
		try {
			when(restService.createApplication(propsDto.getId(), app.getName()))
					.thenReturn(restResult);
			when(restResult.to(RestResponse.class)).thenReturn(responseOfApp);
			when(responseOfApp.getDto()).thenReturn(applicationDto);
			when(applicationDto.getApplicationProperties())
					.thenReturn(propsDto);
			Assert.notNull(serviceSpy.createApplication(propsDto.getId(),
					app.getName()));
		} catch (ApplicationCreateException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateApplicationShouldThrowIfBlueprintNotFound() {
		try {
			when(restResult.to(RestResponse.class)).thenReturn(responseOfList);
			when(restService.getBlueprints()).thenReturn(restResult);
			when(responseOfList.getDto()).thenReturn(propertiesList);
			serviceSpy.createApplication("not_exists_name", app.getName());
			fail("should've thrown an exception!");
		} catch (RavelloPluginException e) {
			Assert.isTrue(e.getMessage().equals("not_exists_name"));
		}
	}
}
