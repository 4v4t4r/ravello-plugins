/*
 * 
 * Copyright (c) 2013 Ravello Systems Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 * @author Alex Nickolaevsky
 * */

package com.ravellosystems.plugins.maven.inject;

import static com.ravellosystems.plugins.common.Utils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import com.ravellosystems.plugins.exceptions.ApplicationPropertiesException;

public class InjectorHelper {

	private List<MavenProject> reactorProjects;
	private MavenProject project;

	public InjectorHelper(MavenProject project,
			List<MavenProject> reactorProjects) {
		this.project = project;
		this.reactorProjects = reactorProjects;
	}

	public Map<String, String> preparePropertiesMap(
			Map<String, String> propertiesMap, Map<String, String> dnsProperties)
			throws ApplicationPropertiesException {

		if (isEmpty(propertiesMap)) {
			return dnsProperties;
		}

		Map<String, String> maps = new HashMap<String, String>();
		Set<String> keySet = propertiesMap.keySet();
		for (String key : keySet) {
			String propertyMap = propertiesMap.get(key);
			List<String> dnsNames = findDNSNames(propertyMap);
			for (String dnsName : dnsNames) {
				String dnsValue = findDNSValue(dnsProperties, dnsName);
				propertyMap = propertyMap.replace(dnsName, dnsValue);
			}
			maps.put(key, propertyMap);
		}
		return maps;
	}

	private List<String> findDNSNames(String dnsNames)
			throws ApplicationPropertiesException {
		if (isEmpty(dnsNames))
			throw new ApplicationPropertiesException(
					"key mapping name cannot be empty");
		return Arrays.asList(dnsNames.split(";"));
	}

	private String findDNSValue(Map<String, String> dnsProperties,
			String dnsName) throws ApplicationPropertiesException {
		if (!dnsProperties.containsKey(dnsName))
			throw new ApplicationPropertiesException(String.format(
					"key name %s not found in application properties file",
					dnsName));
		return dnsProperties.get(dnsName);
	}

	public void updateProjectProperties(Map<String, String> propertiesMap)
			throws Exception {
		try {
			Set<String> keys = propertiesMap.keySet();
			for (String key : keys) {
				doUpdate(propertiesMap, key);
			}
			for (String key : keys) {
				Map<String, String> replacedProps = findPropertiesForReplace(
						propertiesMap, key);
				Set<String> replacedKeySet = replacedProps.keySet();
				for (String replacedKey : replacedKeySet) {
					doUpdate(replacedProps, replacedKey);
				}
			}
		} catch (Exception e) {
			throw new Exception("Failed to update project properties", e);
		}
	}

	private Map<String, String> findPropertiesForReplace(
			Map<String, String> propertiesMap, String key) {
		Map<String, String> replacedProps = new HashMap<String, String>();
		Set<Object> projectPropertiesKeySet = project.getProperties().keySet();
		for (Object projectPropKey : projectPropertiesKeySet) {
			String projectPropValue = String.valueOf(project.getProperties()
					.get(projectPropKey));
			String wrappedKey = String.format("${%s}", key);
			if (projectPropValue.contains(wrappedKey)) {
				String replaced = projectPropValue.replace(wrappedKey,
						propertiesMap.get(key));
				replacedProps.put(String.valueOf(projectPropKey), replaced);
			}
		}
		return replacedProps;
	}

	private void doUpdate(Map<String, String> propertiesMap, String key) {
		project.getProperties().setProperty(key, propertiesMap.get(key));
		System.setProperty(key, propertiesMap.get(key));
	}

	public List<PluginHelper> findAllPlugins() {
		List<PluginHelper> mvnPlugins = new ArrayList<PluginHelper>();
		for (MavenProject mavenProject : reactorProjects) {
			List<Plugin> buildPlugins = mavenProject.getBuildPlugins();
			for (Plugin plugin : buildPlugins) {
				mvnPlugins.add(new PluginHelper(plugin));
			}
		}
		return mvnPlugins;
	}

	public void updatePluginsConfiguration(Map<String, String> propertiesMap) {
		for (PluginHelper mvnPlugin : findAllPlugins()) {
			PluginConfigurationHelper configuration = mvnPlugin
					.getConfiguration();
			doUpdatePluginsConfiguration(propertiesMap, configuration);
		}
	}

	private void doUpdatePluginsConfiguration(
			Map<String, String> propertiesMap,
			PluginConfigurationHelper configuration) {
		Set<String> keySet = propertiesMap.keySet();
		for (String key : keySet) {
			String value = propertiesMap.get(key);
			configuration.updateValue(key, value);
		}
	}

}
