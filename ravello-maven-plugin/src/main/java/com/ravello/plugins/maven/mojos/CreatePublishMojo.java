package com.ravello.plugins.maven.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.ApplicationService;
import com.ravello.plugins.common.BlueprintService;
import com.ravello.plugins.common.RavelloFabric;
import com.ravello.plugins.exceptions.RavelloPluginException;

@Mojo(name = "app-create-publish", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class CreatePublishMojo extends ApplicationMojo {

	@Parameter(property = "blueprintId", required = true)
	private Long blueprintId;

	@Parameter(property = "applicationName", required = true)
	private String applicationName;

	public void execute() throws MojoExecutionException, MojoFailureException {
		RavelloFabric ravelloBuilder = RavelloFabric
				.get(new CredentialsImpl());
		BlueprintService blueprintService = ravelloBuilder.blueprint();
		ApplicationService applicationService = ravelloBuilder.application();
		try {
			Application application = blueprintService.createApplication(
					blueprintId, applicationName);
			applicationService.publish(application.getId(), preferredCloud,
					preferredCloud);
		} catch (RavelloPluginException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
