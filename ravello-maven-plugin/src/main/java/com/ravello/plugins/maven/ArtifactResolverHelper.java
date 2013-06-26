package com.ravello.plugins.maven;

import java.io.File;

import com.ravello.plugins.exceptions.ApplicationPropertiesNotFoundException;

public interface ArtifactResolverHelper {

	File artifactToFile(String artifactId)
			throws ApplicationPropertiesNotFoundException;

}
