package com.ravello.management.toolbox.mvn;

import java.io.File;

import com.ravello.management.plugin.exceptions.ApplicationPropertiesNotFoundException;

public interface MvnArtifactResolver {

	File artifactToFile(String artifactId)
			throws ApplicationPropertiesNotFoundException;

}
