package com.ravello.management.plugin.application;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.management.plugin.exceptions.ApplicationCreateException;
import com.ravello.management.plugin.exceptions.ApplicationPublishException;
import com.ravello.management.toolbox.Application;
import com.ravello.management.toolbox.ApplicationService;
import com.ravello.management.toolbox.BlueprintService;
import com.ravello.management.toolbox.RavelloBuilder;

@Mojo(name = "app-create-publish", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class CreatePublishMojo extends ApplicationMojo {

	@Parameter(property = "blueprintId", required = true)
	private Long blueprintId;

	@Parameter(property = "applicationName", required = true)
	private String applicationName;

	public void execute() throws MojoExecutionException, MojoFailureException {
		RavelloBuilder ravelloBuilder = RavelloBuilder
				.get(new CredentialsImpl());
		BlueprintService blueprintService = ravelloBuilder.blueprint();
		ApplicationService applicationService = ravelloBuilder.application();
		try {
			Application application = blueprintService.createApplication(
					blueprintId, applicationName);
			applicationService.publish(application.getId(), preferredCloud,
					preferredCloud);
		} catch (ApplicationCreateException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (ApplicationPublishException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
