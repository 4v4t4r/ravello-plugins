package com.ravello.plugins.maven;

import java.util.List;
import java.util.Map;

import com.ravello.plugins.exceptions.ApplicationPropertiesException;

public interface MavenHelper {

	List<PluginHelper> findAllPlugins();

	void updatePluginsConfiguration(Map<String, String> propertiesMap);

	void updateProperties(Map<String, String> dnsNamesPropertiesMap);

	Map<String, String> preparePropertiesMap(Map<String, String> propertiesMap,
			Map<String, String> dnsProperties)
			throws ApplicationPropertiesException;

}
