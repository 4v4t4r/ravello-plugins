package com.ravello.management.plugin.application;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.management.plugin.exceptions.ApplicationCreateException;
import com.ravello.management.plugin.exceptions.ApplicationPublishException;
import com.ravello.management.plugin.exceptions.ApplicationWrongStateException;
import com.ravello.management.plugin.exceptions.BlueprintNotFoundException;
import com.ravello.management.toolbox.Application;
import com.ravello.management.toolbox.ApplicationService;
import com.ravello.management.toolbox.BlueprintService;
import com.ravello.management.toolbox.RavelloBuilder;

@Mojo(name = "app-create-publish-wait", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class CreatePublishWaitMojo extends ApplicationMojo {

	@Parameter(property = "timeout", defaultValue = "20")
	protected int timeout;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info("ravello: login");
			RavelloBuilder ravelloBuilder = RavelloBuilder
					.get(new CredentialsImpl());
			BlueprintService blueprintService = ravelloBuilder.blueprint();
			getLog().info("ravello: create app");
			Application application = blueprintService.createApplication(
					blueprintName, applicationName);
			ApplicationService applicationService = ravelloBuilder
					.application();
			getLog().info("ravello: publish app");
			applicationService.publish(application.getId(), preferredCloud,
					preferredZone);
			getLog().info("ravello: wait for publish");
			applicationService.awaitForPublish(application.getId(), timeout);
			application = applicationService.findApplication(application
					.getId());
			getLog().info("ravello: deploy");
			File zip = createZip(application);
			attach(zip, application.getId());
			delay();
		} catch (BlueprintNotFoundException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (ApplicationCreateException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (ApplicationPublishException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (ApplicationWrongStateException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
