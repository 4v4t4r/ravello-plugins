package com.ravello.plugins.maven.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.ApplicationService;
import com.ravello.plugins.common.RavelloRestFactory;
import com.ravello.plugins.exceptions.RavelloPluginException;

@Mojo(name = "app-start-wait", defaultPhase = LifecyclePhase.VALIDATE, threadSafe = true, aggregator = true)
public class StartWaitMojo extends ApplicationMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			ApplicationService service = RavelloRestFactory.get(
					new CredentialsImpl()).application();
			Application application = service.findApplication(applicationName);
			service.start(application.getId());
			service.awaitForApplicationState(application.getId(), timeout,
					Application.STATE.STARTED);
		} catch (RavelloPluginException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
}
