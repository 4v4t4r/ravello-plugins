package com.ravello.plugins.maven.impl;

import static com.ravello.plugins.common.Utils.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import com.ravello.plugins.exceptions.ApplicationPropertiesException;
import com.ravello.plugins.maven.MavenHelper;
import com.ravello.plugins.maven.PluginConfigurationHelper;
import com.ravello.plugins.maven.PluginHelper;

public class MavenHelperImpl implements MavenHelper {

	private List<MavenProject> reactorProjects;
	private MavenProject project;

	public MavenHelperImpl(MavenProject project,
			List<MavenProject> reactorProjects) {
		this.project = project;
		this.reactorProjects = reactorProjects;
	}

	@Override
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
					"DNS mapping name cannot be empty");
		return Arrays.asList(dnsNames.split(";"));
	}

	private String findDNSValue(Map<String, String> dnsProperties,
			String dnsName) throws ApplicationPropertiesException {
		if (!dnsProperties.containsKey(dnsName))
			throw new ApplicationPropertiesException(String.format(
					"DNS name %s not found in application DNS file", dnsName));
		return dnsProperties.get(dnsName);
	}

	@Override
	public void updateProperties(Map<String, String> propertiesMap) {
		Set<String> keys = propertiesMap.keySet();
		for (String key : keys) {
			project.getProperties().setProperty(key, propertiesMap.get(key));
		}
	}

	@Override
	public List<PluginHelper> findAllPlugins() {
		List<PluginHelper> mvnPlugins = new ArrayList<PluginHelper>();
		for (MavenProject mavenProject : reactorProjects) {
			List<Plugin> buildPlugins = mavenProject.getBuildPlugins();
			for (Plugin plugin : buildPlugins) {
				mvnPlugins.add(new PluginHelperImpl(plugin));
			}
		}
		return mvnPlugins;
	}

	@Override
	public void updatePluginsConfiguration(Map<String, String> propertiesMap) {
		for (PluginHelper mvnPlugin : findAllPlugins()) {
			PluginConfigurationHelper configuration = mvnPlugin
					.getConfiguration();
			doUpdate(propertiesMap, configuration);
		}
	}

	private void doUpdate(Map<String, String> propertiesMap,
			PluginConfigurationHelper configuration) {
		Set<String> keySet = propertiesMap.keySet();
		for (String key : keySet) {
			String value = propertiesMap.get(key);
			configuration.updateValue(key, value);
		}
	}

}
