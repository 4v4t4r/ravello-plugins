package com.ravello.plugins.common;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.ravello.management.common.dtos.application.ApplicationPropertiesDto;
import com.ravello.management.common.dtos.vm.VmDto;
import com.ravello.plugins.common.impl.ApplicationServiceImpl;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceImplTest {

	@Mock
	private ApplicationRestService restService;

	// @Mock
	// private RestResponse<ApplicationDto> responseOfApp;
	//
	// @Mock
	// private ApplicationDto applicationDto;

	@Mock
	private VmDto vmDto;

	// @Mock
	// private VmPropertiesDto vmPropertiesDto;
	//
	// @Mock
	// private VmRuntimeInformation vmRuntimeInformation;

	private ApplicationPropertiesDto propsDto;
	private ApplicationService service;
	private ApplicationService serviceSpy;
	private List<VmDto> vms;
	private Application app = new ApplicationImpl();

	@Before
	public void setUp() throws Exception {
		this.propsDto = new ApplicationPropertiesDto();
		this.propsDto.setName("bp");
		this.propsDto.setId(1L);
		this.service = new ApplicationServiceImpl();
		this.service.setRestClient(restService);
		this.serviceSpy = spy(this.service);
		this.serviceSpy.setRestClient(restService);
		this.vms = new ArrayList<VmDto>();
		this.vms.add(vmDto);
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

		@Override
		public Set<Boolean> getVmsState() throws ApplicationPublishException,
				ApplicationWrongStateException {
			return null;
		}
	};

	@Test
	public void testPublish() {
		try {
			this.service.publish(app.getId(), "pc", "pz");
			verify(restService, times(1)).publish(app.getId(), "pc", "pz");
		} catch (ApplicationPublishException e) {
			fail(e.getMessage());
		}
	}

	// @Test
	// public void tesIsPublishingAndStarted() {
	// try {
	// prepareIsPublishingTest();
	// when(vmRuntimeInformation.getState()).thenReturn(
	// GuestStateDto.STARTED);
	// Assert.isTrue(!this.service.isPublishing(app.getId()));
	// } catch (Exception e) {
	// e.printStackTrace();
	// fail(e.getMessage());
	// }
	// }

	// @Test
	// public void tesIsPublishingAndStarting() {
	// try {
	// prepareIsPublishingTest();
	// when(vmRuntimeInformation.getState()).thenReturn(
	// GuestStateDto.STARTING);
	// Assert.isTrue(this.service.isPublishing(app.getId()));
	// } catch (Exception e) {
	// e.printStackTrace();
	// fail(e.getMessage());
	// }
	// }
	//
	// @Test
	// public void tesIsPublishingAndThrowOnFailure() {
	// try {
	// prepareIsPublishingTest();
	// when(vmRuntimeInformation.getState()).thenReturn(
	// GuestStateDto.ERROR);
	// this.service.isPublishing(app.getId());
	// fail("should've thrown an exception!");
	// } catch (Exception e) {
	// Assert.isTrue(e instanceof ApplicationPublishException);
	// }
	// }
	//
	// @Test
	// public void tesIsPublishingAndThrowOnWrongState() {
	// try {
	// prepareIsPublishingTest();
	// when(vmRuntimeInformation.getState()).thenReturn(
	// GuestStateDto.TERMINATED);
	// this.service.isPublishing(app.getId());
	// fail("should've thrown an exception!");
	// } catch (Exception e) {
	// Assert.isTrue(e instanceof ApplicationWrongStateException);
	// }
	// }
	//
	// private void prepareIsPublishingTest() {
	// when(restService.getApplicationInstance(app.getId())).thenReturn(
	// restResult);
	// when(restResult.to(RestResponse.class)).thenReturn(responseOfApp);
	// when(responseOfApp.getDto()).thenReturn(applicationDto);
	// when(applicationDto.getVms()).thenReturn(vms);
	// when(vmDto.getVmProperties()).thenReturn(vmPropertiesDto);
	// when(vmPropertiesDto.getRuntimeInformation()).thenReturn(
	// vmRuntimeInformation);
	// }

	@Test
	public void testPublishShouldThrowApplicationPublishException() {
		try {
			doThrow(new RuntimeException()).when(restService).publish(
					app.getId(), "pc", "pz");
			this.service.publish(app.getId(), "pc", "pz");
			fail("should've thrown an exception!");
		} catch (Exception e) {
			Assert.isTrue(e instanceof ApplicationPublishException);
		}
	}
}
