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

import java.io.File;

import org.apache.maven.plugins.annotations.Parameter;

import com.ravellosystems.plugins.common.Application;
import com.ravellosystems.plugins.common.ApplicationService;
import com.ravellosystems.plugins.common.Credentials;
import com.ravellosystems.plugins.common.IOService;
import com.ravellosystems.plugins.common.IOService.PropertyKeyTrimmer;
import com.ravellosystems.plugins.common.PluginIOService;
import com.ravellosystems.plugins.common.Utils;
import com.ravellosystems.plugins.exceptions.ApplicationPropertiesException;
import com.ravellosystems.plugins.exceptions.ApplicationPublishException;
import com.ravellosystems.plugins.exceptions.ApplicationWrongStateException;

public abstract class ApplicationMojo extends RavelloMojo {

	@Parameter(property = "applicationName", required = true)
	protected String applicationName;

	@Parameter(property = "blueprintId")
	protected Long blueprintId;

	@Parameter(property = "preferredCloud")
	protected String preferredCloud;

	@Parameter(property = "preferredZone")
	protected String preferredZone;

	@Parameter(property = "publishCostOptimized", defaultValue = "true")
	protected String publishCostOptimized;

	@Parameter(property = "publishPerformanceOptimized", defaultValue = "false")
	protected String publishPerformanceOptimized;

	@Parameter(property = "finalName")
	protected String finalName;

	@Parameter(property = "classifier")
	protected String classifier;

	@Parameter(property = "timeout", defaultValue = "20")
	protected int timeout;

	@Parameter(property = "delay", defaultValue = "0")
	protected String delay;

	@Parameter(property = "autoStop", defaultValue = "0")
	protected int autoStop;

	protected interface Publisher {
		void doPublish(Application application, ApplicationService applicationService)
				throws ApplicationPublishException;
	}

	protected Publisher getPublisher() throws ApplicationPublishException {

		if (Boolean.valueOf(publishPerformanceOptimized)) {
			return new Publisher() {
				@Override
				public void doPublish(Application application, ApplicationService applicationService)
						throws ApplicationPublishException {
					applicationService.publishPerformanceOptimized(application.getId(), autoStop);
				}
			};
		}

		if (!Utils.isEmpty(preferredCloud, preferredZone)) {
			return new Publisher() {
				@Override
				public void doPublish(Application application, ApplicationService applicationService)
						throws ApplicationPublishException {
					applicationService.publish(application.getId(), preferredCloud, preferredZone, autoStop);
				}
			};
		}

		if (Boolean.valueOf(publishCostOptimized)) {
			return new Publisher() {
				@Override
				public void doPublish(Application application, ApplicationService applicationService)
						throws ApplicationPublishException {
					applicationService.publishCostOptimized(application.getId(), autoStop);
				}
			};
		}

		throw new ApplicationPublishException("Publish rules not set. " + "Please select (prefered cloud and zone) "
				+ "or (optimized publish mode).");

	}

	protected File createZip(Application application) throws ApplicationPropertiesException,
			ApplicationWrongStateException {
		File propertiesFile = getPropFile();
		IOService ioService = new PluginIOService();
		ioService.writeToPropertiesFile(propertiesFile, application.getVMsDNS(), new PropertyKeyTrimmer() {
			@Override
			public String trim(String name) {
				return name.trim().toLowerCase().replace(' ', '_');
			}
		});
		return ioService.zipFile(propertiesFile, getZipFilePath());
	}

	protected void delay() {
		try {
			Thread.sleep(Integer.parseInt(delay) * 60000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void attach(File zip, long appId) {
		if (classifier == null || classifier.trim().isEmpty())
			classifier = String.valueOf(appId);
		getLog().info(
				String.format("attach: %s %s %s", project.getGroupId(), project.getArtifactId(), project.getPackaging()));
		projectHelper.attachArtifact(project, "zip", classifier, zip);
	}

	private String getZipFilePath() {
		return getTarget().concat("/").concat(getFinalName()).concat(".zip");
	}

	private String getFinalName() {
		return finalName != null ? finalName : project.getArtifactId();
	}

	private File getPropFile() {
		new File(getTarget()).mkdirs();
		return new File(getTarget().concat("/").concat(getFinalName()));
	}

	protected final class CredentialsImpl implements Credentials {
		@Override
		public String getUser() {
			return userName;
		}

		@Override
		public String getUrl() {
			return serviceUrl;
		}

		@Override
		public String getPassword() {
			return password;
		}
	}
}