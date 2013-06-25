package com.ravello.management.plugin.utils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.ravello.management.RavelloMojo;
import com.ravello.management.plugin.exceptions.ApplicationPropertiesException;
import com.ravello.management.plugin.exceptions.ApplicationPropertiesNotFoundException;
import com.ravello.management.toolbox.IOService;
import com.ravello.management.toolbox.impl.IOServiceImpl;
import com.ravello.management.toolbox.mvn.MvnArtifactResolver;
import com.ravello.management.toolbox.mvn.MvnPlugin;
import com.ravello.management.toolbox.mvn.MvnService;
import com.ravello.management.toolbox.mvn.impl.MvnArtifactResolverImpl;
import com.ravello.management.toolbox.mvn.impl.MvnServiceImpl;

@Mojo(name = "inject-properties", defaultPhase = LifecyclePhase.VALIDATE, threadSafe = true)
public class InjectProperties extends RavelloMojo {

	@Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true, required = true)
	protected List<ArtifactRepository> remoteRepositories;

	@Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
	private ArtifactRepository localRepository;

	@Component
	protected ArtifactResolver resolver;

	@Parameter(defaultValue = "${reactorProjects}", readonly = true)
	private List<MavenProject> reactorProjects;

	@Parameter(property = "artifactId", required = true)
	protected String artifactId;

	@Parameter(property = "propertiesFileName", required = true)
	protected String propertiesFileName;

	@Parameter(property = "propertiesMap", required = true)
	protected Map<String, String> propertiesMap;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {
			MvnArtifactResolver mvnArtifactResolver = new MvnArtifactResolverImpl(
					project, resolver, remoteRepositories, localRepository);
			File propertiesZip = mvnArtifactResolver.artifactToFile(artifactId);
			IOService ioService = new IOServiceImpl();
			ioService.unzipFile(propertiesZip, getTarget());
			Properties properties = ioService.readProperties(new File(
					getTarget(), propertiesFileName));
			MvnService mvnService = new MvnServiceImpl(project, reactorProjects);
			List<MvnPlugin> plugins = mvnService.findAllPlugins();
			Map<String, List<String>> maps = mvnService.preparePropertiesMap(
					propertiesMap, properties);
			mvnService.updateConfiguration(maps, properties, plugins);
			mvnService.updateProperties(maps, properties);
		} catch (ApplicationPropertiesException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (ApplicationPropertiesNotFoundException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
