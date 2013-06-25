package com.ravello.management.plugin.application;

import java.io.File;

import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.management.RavelloMojo;
import com.ravello.management.plugin.exceptions.ApplicationPropertiesException;
import com.ravello.management.toolbox.Application;
import com.ravello.management.toolbox.Credentials;
import com.ravello.management.toolbox.IOService;
import com.ravello.management.toolbox.impl.IOServiceImpl;

public abstract class ApplicationMojo extends RavelloMojo {

	@Parameter(property = "userName", required = true)
	protected String userName;

	@Parameter(property = "password", required = true)
	protected String password;

	protected final static String serviceUrl = "https://cloud.ravellosystems.com/services";

	@Parameter(property = "blueprintName", required = true)
	protected String blueprintName;

	@Parameter(property = "blueprintId")
	protected Long blueprintId;

	@Parameter(property = "applicationName", required = true)
	protected String applicationName;

	@Parameter(property = "preferredCloud", required = true)
	protected String preferredCloud;

	@Parameter(property = "preferredZone", required = true)
	protected String preferredZone;

	@Parameter(property = "finalName")
	protected String finalName;

	@Parameter(property = "classifier")
	protected String classifier;

	@Parameter(property = "delay", defaultValue = "0")
	protected String delay;

	protected File createZip(Application application)
			throws ApplicationPropertiesException {
		File props = getPropFile();
		IOService ioService = new IOServiceImpl();
		ioService.writeToFile(props, application.getVmsDNS());
		return ioService.zipFile(props, getZipFilePath());
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
				String.format("attach: %s %s %s", project.getGroupId(),
						project.getArtifactId(), project.getPackaging()));
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
