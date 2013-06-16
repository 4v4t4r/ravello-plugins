package com.ravello.management.plugin.application;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.management.plugin.exceptions.ApplicationCreateException;
import com.ravello.management.toolbox.RavelloBuilder;

@Mojo(name = "app-create", threadSafe = true)
public class CreateMojo extends ApplicationMojo {

	@Parameter(property = "blueprintId", required = true)
	private Long blueprintId;

	@Parameter(property = "applicationName", required = true)
	private String applicationName;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			RavelloBuilder.get(new CredentialsImpl()).blueprint()
					.createApplication(blueprintId, applicationName);
		} catch (ApplicationCreateException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
}
