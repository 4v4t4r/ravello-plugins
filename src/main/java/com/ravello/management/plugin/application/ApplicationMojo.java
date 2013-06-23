package com.ravello.management.plugin.application;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.ravello.management.RavelloMojo;
import com.ravello.management.plugin.exceptions.ApplicationPropertiesException;
import com.ravello.management.toolbox.Application;
import com.ravello.management.toolbox.Credentials;
import com.ravello.management.toolbox.IOService;
import com.ravello.management.toolbox.impl.IOServiceImpl;

@Mojo(name = "", threadSafe = true, aggregator = true)
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

	@Parameter(property = "groupId")
	protected String groupId;

	@Parameter(property = "artifactId")
	protected String artifactId;

	@Parameter(property = "version")
	protected String version;

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
		MavenProject projectClone = project.clone();
		projectClone.setGroupId(getGroupId());
		projectClone.setArtifactId(getArtifactId());
		projectClone.setVersion(getVersion());
		projectHelper.attachArtifact(projectClone, "zip", classifier, zip);
	}

	private String getVersion() {
		return version != null ? version : project.getVersion();
	}

	private String getArtifactId() {
		return artifactId != null ? artifactId : project.getArtifactId();
	}

	private String getGroupId() {
		return groupId != null ? groupId : project.getGroupId();
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
