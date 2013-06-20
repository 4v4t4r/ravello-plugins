package com.ravello.management.toolbox;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import com.ravello.management.plugin.exceptions.ApplicationPropertiesException;

public interface IOService {

	void writeToFile(File file, Map<String, String> properties)
			throws ApplicationPropertiesException;

	File zipFile(File file, String zip) throws ApplicationPropertiesException;

	void unzipFile(File file, String extractTo)
			throws ApplicationPropertiesException;

	Properties readProperties(File file) throws ApplicationPropertiesException;

}
