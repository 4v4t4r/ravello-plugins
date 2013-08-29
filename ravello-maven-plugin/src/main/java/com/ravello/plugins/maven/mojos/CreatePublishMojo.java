package com.ravello.plugins.maven.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.ApplicationService;
import com.ravello.plugins.common.BlueprintService;
import com.ravello.plugins.common.RavelloRestFactory;
import com.ravello.plugins.exceptions.RavelloPluginException;

@Mojo(name = "app-create-publish", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class CreatePublishMojo extends ApplicationMojo {

	@Parameter(property = "applicationName", required = true)
	private String applicationName;

	@Parameter(property = "blueprintName", required = true)
	protected String blueprintName;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			RavelloRestFactory ravelloBuilder = RavelloRestFactory
					.get(new CredentialsImpl());
			Publisher publisher = getPublisher();
			BlueprintService blueprintService = ravelloBuilder.blueprint();
			ApplicationService applicationService = ravelloBuilder
					.application();
			Application application = blueprintService.createApplication(
					blueprintName, applicationName);
			publisher.doPublish(application, applicationService);
		} catch (RavelloPluginException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
