package com.ravello.management.plugin.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

@Mojo(name = "utils-add-property")
public class UtilsMojo extends AbstractMojo {

	@Component
	protected MavenProject mavenProject;

	@Parameter(defaultValue = "${reactorProjects}", readonly = true)
	private List<MavenProject> reactorProjects;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			System.out.println("##########################");
			System.out.println(reactorProjects);
			System.out.println("##########################");

			List<Plugin> buildPlugins = mavenProject.getBuildPlugins();
			System.out.println("11111111111111111");
			for (Plugin plugin : buildPlugins) {
				System.out.println("222222222222222222222");
				Xpp3Dom config = (Xpp3Dom) plugin.getConfiguration();
				System.out.println("3333333333333333333");
				if (config != null) {
					Xpp3Dom[] children = config.getChildren();
					System.out.println("44444444444444444444");
					List<Xpp3Dom> asList = Arrays.asList(children);
					System.out.println("555555555555555555555");
					for (Xpp3Dom xpp3Dom : asList) {
						if (xpp3Dom.getValue().trim()
								.equalsIgnoreCase("${server-deploy-locations}")) {
							System.out.println("666666666666666666666");
							xpp3Dom.setValue("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
							System.out.println("77777777777777777777777");
						}
					}
				}
			}
		} catch (Exception e) {
			getLog().error(e);
		}
	}

}
