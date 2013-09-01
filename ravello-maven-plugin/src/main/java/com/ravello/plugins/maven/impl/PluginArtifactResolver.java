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
	public File artifactToFile(String artifactPrefix)
			throws ApplicationPropertiesNotFoundException {
		try {
			List<Artifact> artifacts = pluginDescriptor.getArtifacts();
			for (Artifact artifact : safe(artifacts)) {
				if (artifact.getArtifactId().startsWith(artifactPrefix.trim())) {
					this.resolver.resolve(artifact, this.remoteRepositories,
							this.localRepository);
					return artifact.getFile();
				}
			}
		} catch (Exception e) {
			throw new ApplicationPropertiesNotFoundException(e);
		}

		throw new ApplicationPropertiesNotFoundException(
				"Not found in any repository: " + artifactPrefix);
	}

	private final static <T> Iterable<T> safe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T> emptyList() : iterable;
	}

}
