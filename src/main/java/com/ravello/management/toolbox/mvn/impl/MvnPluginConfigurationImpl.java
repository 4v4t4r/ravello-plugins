package com.ravello.management.toolbox.mvn.impl;

import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.ravello.management.toolbox.mvn.MvnPluginConfiguration;

public class MvnPluginConfigurationImpl implements MvnPluginConfiguration {

	private Xpp3Dom configuration;

	public MvnPluginConfigurationImpl(Object configuration) {
		this.configuration = (Xpp3Dom) configuration;
	}

	@Override
	public boolean updateValue(String placeholder, String value) {
		if (!valid(configuration))
			return false;

		List<Xpp3Dom> children = Arrays.asList(configuration.getChildren());
		for (Xpp3Dom xpp3Dom : children) {
			if (valid(xpp3Dom) && found(xpp3Dom, wrap(placeholder))) {
				String updatedValue = replace(xpp3Dom.getValue(),
						wrap(placeholder), value);
				xpp3Dom.setValue(updatedValue);
				return true;
			}
		}
		return false;
	}

	private String replace(String updateableValue, String placeholder,
			String newValue) {
		return updateableValue.replace(placeholder, newValue);
	}

	private String wrap(String placeholder) {
		return String.format("${%s}", placeholder);
	}

	private boolean found(Xpp3Dom xpp3Dom, String placeholder) {
		String value = xpp3Dom.getValue();
		return value != null ? value.trim().contains(placeholder) : false;
	}

	private boolean valid(Object o) {
		return o != null;
	}
}
