package com.ravello.plugins.maven.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.plugins.common.RavelloFabric;
import com.ravello.plugins.exceptions.RavelloPluginException;

@Mojo(name = "app-create", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class CreateMojo extends ApplicationMojo {

	@Parameter(property = "blueprintId", required = true)
	private Long blueprintId;

	@Parameter(property = "applicationName", required = true)
	private String applicationName;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			RavelloFabric.get(new CredentialsImpl()).blueprint()
					.createApplication(blueprintId, applicationName);
		} catch (RavelloPluginException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
}
