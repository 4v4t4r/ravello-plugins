package com.ravello.plugins.maven.impl;

import org.apache.maven.model.Plugin;

import com.ravello.plugins.maven.PluginConfigurationHelper;
import com.ravello.plugins.maven.PluginHelper;

public class PluginHelperImpl implements PluginHelper {

	private Plugin plugin;

	public PluginHelperImpl(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public PluginConfigurationHelper getConfiguration() {
		Object configuration = plugin.getConfiguration();
		return new PluginConfigurationImpl(configuration);
	}

}
