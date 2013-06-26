package com.ravello.plugins.maven.impl;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.project.MavenProject;

import com.ravello.plugins.exceptions.ApplicationPropertiesNotFoundException;
import com.ravello.plugins.maven.ArtifactResolverHelper;

public class ArtifactResolverImpl implements ArtifactResolverHelper {

	private MavenProject project;
	private ArtifactResolver resolver;
	private List<ArtifactRepository> remoteRepositories;
	private ArtifactRepository localRepository;

	public ArtifactResolverImpl(MavenProject project,
			ArtifactResolver resolver,
			List<ArtifactRepository> remoteRepositories,
			ArtifactRepository localRepository) {
		this.project = project;
		this.resolver = resolver;
		this.remoteRepositories = remoteRepositories;
		this.localRepository = localRepository;
	}

	@Override
	public File artifactToFile(String artifactId)
			throws ApplicationPropertiesNotFoundException {
		try {
			Set<Artifact> dependencyArtifacts = project
					.getDependencyArtifacts();
			for (Artifact artifact : dependencyArtifacts) {
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
}
