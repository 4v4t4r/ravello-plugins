package com.ravello.management.toolbox.mvn.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import com.ravello.management.toolbox.mvn.MvnPlugin;
import com.ravello.management.toolbox.mvn.MvnPluginConfiguration;
import com.ravello.management.toolbox.mvn.MvnService;

public class MvnServiceImpl implements MvnService {

	private List<MavenProject> reactorProjects;
	private MavenProject project;

	public MvnServiceImpl(MavenProject project,
			List<MavenProject> reactorProjects) {
		this.project = project;
		this.reactorProjects = reactorProjects;
	}

	@Override
	public Map<String, List<String>> preparePropertiesMap(
			Map<String, String> propertiesMap, Properties properties) {
		Map<String, List<String>> maps = new HashMap<String, List<String>>();
		if (propertiesMap == null || propertiesMap.isEmpty()) {
			Set<Object> keySet = properties.keySet();
			for (Object key : keySet) {
				maps.put(key.toString(), propsToList(properties, key));
			}
			return maps;
		}
		Set<String> keySet = propertiesMap.keySet();
		for (String key : keySet) {
			String values = propertiesMap.get(key);
			maps.put(key, Arrays.asList(values.split(",")));
		}
		return maps;
	}

	private List<String> propsToList(Properties properties, Object key) {
		return Arrays.asList(new String[] { properties.getProperty(key
				.toString()) });
	}

	@Override
	public void updateProperties(Map<String, List<String>> propertiesMap,
			Properties properties) {
		Set<Object> propertiesKeys = properties.keySet();
		for (Object propertyKey : propertiesKeys) {
			String _propertyKey = propertyKey.toString();
			String value = properties.getProperty(_propertyKey);
			List<String> placeholders = propertiesMap.get(_propertyKey);
			for (String placeholder : safe(placeholders)) {
				project.getProperties().setProperty(placeholder, value);
			}
		}
	}

	@Override
	public List<MvnPlugin> findAllPlugins() {
		List<MvnPlugin> mvnPlugins = new ArrayList<MvnPlugin>();
		for (MavenProject mavenProject : reactorProjects) {
			List<Plugin> buildPlugins = mavenProject.getBuildPlugins();
			for (Plugin plugin : buildPlugins) {
				mvnPlugins.add(new MvnPluginImpl(plugin));
			}
		}
		return mvnPlugins;
	}

	@Override
	public void updateConfiguration(Map<String, List<String>> propertiesMap,
			Properties properties, List<MvnPlugin> plugins) {
		for (MvnPlugin mvnPlugin : plugins) {
			MvnPluginConfiguration configuration = mvnPlugin.getConfiguration();
			prepareUpdate(propertiesMap, properties, configuration);
		}
	}

	private void prepareUpdate(Map<String, List<String>> propertiesMap,
			Properties properties, MvnPluginConfiguration configuration) {
		Set<Object> propertiesKeys = properties.keySet();
		for (Object propertyKey : propertiesKeys) {
			String _propertyKey = propertyKey.toString();
			String value = properties.getProperty(_propertyKey);
			List<String> placeholders = propertiesMap.get(_propertyKey);
			doUpdate(configuration, value, placeholders);
		}
	}

	private void doUpdate(MvnPluginConfiguration configuration, String value,
			List<String> placeholders) {
		for (String placeholder : safe(placeholders)) {
			configuration.updateValue(placeholder, value);
		}
	}

	private final static <T> Iterable<T> safe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T> emptyList() : iterable;
	}

}
