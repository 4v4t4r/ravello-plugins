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

package com.ravellosystems.plugins.maven.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

public abstract class RavelloMojo extends AbstractMojo {

	@Component
	protected MavenProject project;

	@Component
	protected MavenProjectHelper projectHelper;

	@Parameter(property = "skip", defaultValue = "false")
	protected String skip;

	@Parameter(property = "serviceUrl", defaultValue = "https://cloud.ravellosystems.com/services")
	protected String serviceUrl;

	protected String getTarget() {
		return project.getBuild().getDirectory();
	}

}
