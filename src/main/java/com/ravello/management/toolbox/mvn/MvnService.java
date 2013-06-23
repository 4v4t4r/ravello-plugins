package com.ravello.management.toolbox.mvn;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface MvnService {

	List<MvnPlugin> findAllPlugins();

	void updateConfiguration(Map<String, List<String>> propertiesMap,
			Properties properties, List<MvnPlugin> plugins);

	void updateProperties(Map<String, List<String>> propertiesMap,
			Properties properties);

	Map<String, List<String>> preparePropertiesMap(
			Map<String, String> propertiesMap, Properties properties);

}
