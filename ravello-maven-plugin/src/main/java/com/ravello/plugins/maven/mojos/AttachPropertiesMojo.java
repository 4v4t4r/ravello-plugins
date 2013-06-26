package com.ravello.plugins.maven.mojos;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.RavelloFabric;
import com.ravello.plugins.exceptions.RavelloPluginException;

@Mojo(name = "app-attach-props", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class AttachPropertiesMojo extends ApplicationMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		RavelloFabric ravelloBuilder = RavelloFabric
				.get(new CredentialsImpl());
		try {
			Application application = ravelloBuilder.application()
					.findApplication(applicationName);
			File zip = createZip(application);
			attach(zip, application.getId());
		} catch (RavelloPluginException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
