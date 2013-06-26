package com.ravello.plugins.maven;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface MavenHelper {

	List<PluginHelper> findAllPlugins();

	void updateConfiguration(Map<String, List<String>> propertiesMap,
			Properties properties, List<PluginHelper> plugins);

	void updateProperties(Map<String, List<String>> propertiesMap,
			Properties properties);

	Map<String, List<String>> preparePropertiesMap(
			Map<String, String> propertiesMap, Properties properties);

}
