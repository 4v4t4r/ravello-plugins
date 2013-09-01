/*
 *
 *	Copyright (c) 2013 Ravello Systems Ltd.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * 	you may not use this file except in compliance with the License.
 * 	You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 * 
 */

/**
 * 
 * @author Alex Nickolaevsky
 * */

package com.ravello.plugins.maven.impl;

import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.ravello.plugins.maven.PluginConfigurationHelper;

public class PluginConfigurationImpl implements PluginConfigurationHelper {

	private Xpp3Dom configuration;

	public PluginConfigurationImpl(Object configuration) {
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
