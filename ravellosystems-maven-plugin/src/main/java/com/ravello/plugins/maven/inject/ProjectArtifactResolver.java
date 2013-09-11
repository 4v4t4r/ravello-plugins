/*
 *
 *	Copyright (c) 2013 Ravello Systems Ltd.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * 	you may not use this file except in compliance with the License.
 * 	You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 * 
 */

/**
 * 
 * @author Alex Nickolaevsky
 * */

package com.ravello.plugins.maven.inject;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.project.MavenProject;

import com.ravellosystems.plugins.exceptions.ApplicationPropertiesNotFoundException;

public class ProjectArtifactResolver implements ArtifactFinder {

	private MavenProject project;
	private ArtifactResolver resolver;
	private List<ArtifactRepository> remoteRepositories;
	private ArtifactRepository localRepository;

	public ProjectArtifactResolver(MavenProject project,
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
