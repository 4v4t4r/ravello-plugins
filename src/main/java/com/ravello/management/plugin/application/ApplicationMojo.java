package com.ravello.management.plugin.application;

import java.io.File;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ravello.management.RavelloMojo;
import com.ravello.management.plugin.exceptions.ApplicationPropertiesException;
import com.ravello.management.toolbox.Application;
import com.ravello.management.toolbox.Credentials;
import com.ravello.management.toolbox.IOService;
import com.ravello.management.toolbox.impl.IOServiceImpl;

@Mojo(name = "", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public abstract class ApplicationMojo extends RavelloMojo {

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

	@Parameter(property = "fileName", required = true)
	protected String fileName;

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
		if (classifier == null || classifier.trim().isEmpty()) {
			classifier = String.valueOf(appId);
		}
		projectHelper.attachArtifact(this.project, "zip", classifier, zip);
	}

	private String getZipFilePath() {
		return getTarget().concat("/").concat(fileName).concat(".zip");
	}

	private String getTarget() {
		return project.getBuild().getDirectory();
	}

	private File getPropFile() {
		new File(getTarget()).mkdirs();
		return new File(getTarget().concat("/").concat(fileName));
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
