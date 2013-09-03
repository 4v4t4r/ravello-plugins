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

package com.ravello.plugins.maven.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.plugins.common.Application;
import com.ravello.plugins.common.ApplicationService;
import com.ravello.plugins.common.BlueprintService;
import com.ravello.plugins.common.RavelloRestFactory;
import com.ravello.plugins.exceptions.RavelloPluginException;

@Mojo(name = "app-create-publish", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class CreatePublishMojo extends ApplicationMojo {

	@Parameter(property = "blueprintName", required = true)
	protected String blueprintName;

	protected RavelloRestFactory ravelloBuilder;
	protected BlueprintService blueprintService;
	protected ApplicationService applicationService;
	protected Application application;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info("ravello: login");
			ravelloBuilder = RavelloRestFactory.get(new CredentialsImpl());
			blueprintService = ravelloBuilder.blueprint();
			getLog().info("ravello: create app");
			application = blueprintService.createApplication(blueprintName,
					applicationName);
			applicationService = ravelloBuilder.application();
			getLog().info("ravello: publish app");
			Publisher publisher = getPublisher();
			publisher.doPublish(application, applicationService);
		} catch (RavelloPluginException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
