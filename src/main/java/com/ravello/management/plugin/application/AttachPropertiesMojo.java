package com.ravello.management.plugin.application;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.ravello.management.plugin.exceptions.ApplicationNotFoundException;
import com.ravello.management.toolbox.Application;
import com.ravello.management.toolbox.RavelloBuilder;

@Mojo(name = "app-attach-props", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class AttachPropertiesMojo extends ApplicationMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		RavelloBuilder ravelloBuilder = RavelloBuilder
				.get(new CredentialsImpl());
		try {
			Application application = ravelloBuilder.application()
					.findApplication(applicationName);
			File zip = createZip(application);
			attach(zip, application.getId());
		} catch (ApplicationNotFoundException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
