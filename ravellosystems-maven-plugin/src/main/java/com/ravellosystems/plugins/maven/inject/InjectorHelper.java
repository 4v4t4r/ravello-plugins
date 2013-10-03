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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import com.ravellosystems.plugins.exceptions.ApplicationPropertiesException;
import static com.ravellosystems.plugins.common.Utils.isEmpty;

public class InjectorHelper {

	private List<MavenProject> reactorProjects;
	private MavenProject project;

	public InjectorHelper(MavenProject project, List<MavenProject> reactorProjects) {
		this.project = project;
		this.reactorProjects = reactorProjects;
	}

	public Map<String, String> preparePropertiesMap(Map<String, String> propertiesMap, Map<String, String> dnsProperties)
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

	private List<String> findDNSNames(String dnsNames) throws ApplicationPropertiesException {
		if (isEmpty(dnsNames))
			throw new ApplicationPropertiesException("DNS mapping name cannot be empty");
		return Arrays.asList(dnsNames.split(";"));
	}

	private String findDNSValue(Map<String, String> dnsProperties, String dnsName)
			throws ApplicationPropertiesException {
		if (!dnsProperties.containsKey(dnsName))
			throw new ApplicationPropertiesException(String.format("DNS name %s not found in application DNS file",
					dnsName));
		return dnsProperties.get(dnsName);
	}

	public void updateProperties(Map<String, String> propertiesMap) {
		Set<String> keys = propertiesMap.keySet();
		for (String key : keys) {
			project.getProperties().setProperty(key, propertiesMap.get(key));
		}
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
			PluginConfigurationHelper configuration = mvnPlugin.getConfiguration();
			doUpdatePluginsConfiguration(propertiesMap, configuration);
		}
	}

	private void doUpdatePluginsConfiguration(Map<String, String> propertiesMap, PluginConfigurationHelper configuration) {
		Set<String> keySet = propertiesMap.keySet();
		for (String key : keySet) {
			String value = propertiesMap.get(key);
			configuration.updateValue(key, value);
		}
	}

}
