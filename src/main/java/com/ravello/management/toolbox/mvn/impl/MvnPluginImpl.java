package com.ravello.management.toolbox.mvn.impl;

import org.apache.maven.model.Plugin;

import com.ravello.management.toolbox.mvn.MvnPlugin;
import com.ravello.management.toolbox.mvn.MvnPluginConfiguration;

public class MvnPluginImpl implements MvnPlugin {

	private Plugin plugin;

	public MvnPluginImpl(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public MvnPluginConfiguration getConfiguration() {
		Object configuration = plugin.getConfiguration();
		return new MvnPluginConfigurationImpl(configuration);
	}

}
