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

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.ravellosystems.plugins.common.Application;
import com.ravellosystems.plugins.common.RavelloRestFactory;
import com.ravellosystems.plugins.exceptions.RavelloPluginException;

@Mojo(name = "app-attach-props", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, aggregator = true)
public class AttachPropertiesMojo extends ApplicationMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			RavelloRestFactory ravelloBuilder = RavelloRestFactory
					.get(new CredentialsImpl());
			Application application = ravelloBuilder.application()
					.findApplication(applicationName);
			File zip = createZip(application);
			attach(zip, application.getId());
		} catch (RavelloPluginException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
