package com.ravello.management;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.project.MavenProject;

public abstract class RavelloMojo extends AbstractMojo {

	@Component
	protected MavenProject project;

	@Component
	protected MavenProjectHelper projectHelper;

	@Parameter(property = "userName", required = true)
	protected String userName;

	@Parameter(property = "password", required = true)
	protected String password;

	protected final static String serviceUrl = "https://babushka-2687003.srv.ravcloud.com/services";

}
