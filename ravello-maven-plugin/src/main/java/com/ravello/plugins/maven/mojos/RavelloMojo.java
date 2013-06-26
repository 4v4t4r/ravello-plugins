package com.ravello.plugins.maven.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

public abstract class RavelloMojo extends AbstractMojo {

	@Component
	protected MavenProject project;

	@Component
	protected MavenProjectHelper projectHelper;

	protected String getTarget() {
		return project.getBuild().getDirectory();
	}

}
