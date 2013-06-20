package com.ravello.management.toolbox.mvn.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import com.ravello.management.toolbox.mvn.MvnPlugin;
import com.ravello.management.toolbox.mvn.MvnReactors;

public class MvnReactorsImpl implements MvnReactors {

	private List<MavenProject> reactorProjects;

	public MvnReactorsImpl(List<MavenProject> reactorProjects) {
		this.reactorProjects = reactorProjects;
	}

	@Override
	public List<MvnPlugin> findAllPlugins() {

		List<MvnPlugin> mvnPlugins = new ArrayList<MvnPlugin>();
		for (MavenProject mavenProject : reactorProjects) {
			List<Plugin> buildPlugins = mavenProject.getBuildPlugins();
			for (Plugin plugin : buildPlugins) {
				mvnPlugins.add(new MvnPluginImpl(plugin));
			}
		}

		return mvnPlugins;
	}

}
