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

package com.ravellosystems.plugins.maven.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.ravellosystems.plugins.common.Application;
import com.ravellosystems.plugins.common.ApplicationService;
import com.ravellosystems.plugins.common.RavelloRestFactory;
import com.ravellosystems.plugins.exceptions.RavelloPluginException;

@Mojo(name = "app-delete", defaultPhase = LifecyclePhase.POST_SITE, threadSafe = true, aggregator = true)
public class DeleteMojo extends ApplicationMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			ApplicationService service = RavelloRestFactory.get(
					new CredentialsImpl()).application();
			Application application = service.findApplication(applicationName);
			service.delete(application.getId());
		} catch (RavelloPluginException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
}
