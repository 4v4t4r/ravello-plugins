package com.ravello.plugins.maven.impl;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.descriptor.PluginDescriptor;

import com.ravello.plugins.exceptions.ApplicationPropertiesNotFoundException;
import com.ravello.plugins.maven.ArtifactResolverHelper;

public class PluginArtifactResolver implements ArtifactResolverHelper {

	private PluginDescriptor pluginDescriptor;
	private ArtifactResolver resolver;
	private List<ArtifactRepository> remoteRepositories;
	private ArtifactRepository localRepository;

	public PluginArtifactResolver(PluginDescriptor pluginDescriptor,
			ArtifactResolver resolver,
			List<ArtifactRepository> remoteRepositories,
			ArtifactRepository localRepository) {
		this.pluginDescriptor = pluginDescriptor;
		this.resolver = resolver;
		this.remoteRepositories = remoteRepositories;
		this.localRepository = localRepository;
	}

	@Override
	public File artifactToFile(String artifactId)
			throws ApplicationPropertiesNotFoundException {
		try {
			List<Artifact> artifacts = pluginDescriptor.getArtifacts();
			for (Artifact artifact : safe(artifacts)) {
				if (artifact.getArtifactId().equals(artifactId.trim())) {
					this.resolver.resolve(artifact, this.remoteRepositories,
							this.localRepository);
					return artifact.getFile();
				}
			}
		} catch (Exception e) {
			throw new ApplicationPropertiesNotFoundException(e);
		}

		throw new ApplicationPropertiesNotFoundException(
				"Not found in any repository: " + artifactId);
	}

	// private Artifact toArtifact(ComponentDependency dependency) {
	// DefaultArtifactFactory artifactFactory = new DefaultArtifactFactory();
	// return artifactFactory.createDependencyArtifact(
	// dependency.getGroupId(), dependency.getArtifactId(),
	// VersionRange.createFromVersion(dependency.getVersion()),
	// dependency.getType(), "", "");
	// }

	private final static <T> Iterable<T> safe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T> emptyList() : iterable;
	}

}
