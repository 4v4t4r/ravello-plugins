package com.ravello.plugins.common;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.ravello.plugins.common.impl.BlueprintServiceImpl;
import com.ravello.plugins.exceptions.ApplicationCreateException;
import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;
import com.ravello.plugins.exceptions.BlueprintNotFoundException;
import com.ravello.plugins.exceptions.RavelloPluginException;

@RunWith(MockitoJUnitRunner.class)
public class BlueprintServiceImplTest {

	@Mock
	private BlueprintsRestService restService;

	private BlueprintService service;
	private BlueprintService serviceSpy;
	private Application app = new ApplicationImpl();
	private Application bp = new Blueprint();

	@Before
	public void setUp() throws Exception {
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

		@Override
		public Set<Boolean> compareVmsState(STATE state)
				throws ApplicationPublishException,
				ApplicationWrongStateException {
			return null;
		}
	};

	private static final class Blueprint implements Application {
		@Override
		public Map<String, String> getVmsDNS() {
			return null;
		}

		@Override
		public String getName() {
			return "bp";
		}

		@Override
		public long getId() {
			return 100;
		}

		@Override
		public Set<Boolean> compareVmsState(STATE state)
				throws ApplicationPublishException,
				ApplicationWrongStateException {
			return null;
		}
	};

	@Test
	public void testCreateApplicationUsingBlueprintName() {

		try {
			when(restService.findBlueprint(bp.getName())).thenReturn(bp);
			doReturn(app).when(serviceSpy).createApplication(bp.getId(),
					app.getName());
			Application createdApp = serviceSpy.createApplication(bp.getName(),
					app.getName());
			Assert.notNull(createdApp,
					"Failed to create app using blueprint name");
		} catch (RavelloPluginException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateApplicationUsingBlueprintId() {
		try {
			when(restService.createApplication(bp.getId(), app.getName()))
					.thenReturn(app);
			Assert.notNull(serviceSpy.createApplication(bp.getId(),
					app.getName()));
			verify(restService, times(1)).createApplication(bp.getId(),
					app.getName());
		} catch (ApplicationCreateException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCreateApplicationShouldThrowIfBlueprintNotFound() {
		try {
			when(restService.findBlueprint("not_exists_name")).thenThrow(
					new BlueprintNotFoundException("not_exists_name"));
			serviceSpy.createApplication("not_exists_name", app.getName());
			fail("should've thrown an exception!");
		} catch (RavelloPluginException e) {
			Assert.isTrue(e.getMessage().equals("not_exists_name"));
		}
	}
}
