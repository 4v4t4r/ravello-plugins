package com.ravello.management.toolbox.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ravello.management.plugin.exceptions.ApplicationPropertiesException;
import com.ravello.management.toolbox.IOService;

public class IOServiceImpl implements IOService {

	@Override
	public void writeToFile(File file, Map<String, String> properties)
			throws ApplicationPropertiesException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file, "UTF-8");
			Set<String> keys = properties.keySet();
			for (String key : keys) {
				writer.println(String.format("%s=%s", key, properties.get(key)));
			}
		} catch (Exception e) {
			throw new ApplicationPropertiesException(e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@Override
	public File zipFile(File file, String zip)
			throws ApplicationPropertiesException {
		byte[] buffer = new byte[1024];
		ZipOutputStream zipOutputStream = null;
		FileInputStream fileInputStream = null;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zip);
			zipOutputStream = new ZipOutputStream(fileOutputStream);
			ZipEntry zipEntry = new ZipEntry(file.getName());
			zipOutputStream.putNextEntry(zipEntry);
			fileInputStream = new FileInputStream(file);

			int len;
			while ((len = fileInputStream.read(buffer)) > 0)
				zipOutputStream.write(buffer, 0, len);

		} catch (Exception e) {
			throw new ApplicationPropertiesException(e);
		} finally {
			try {
				fileInputStream.close();
				zipOutputStream.closeEntry();
				zipOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new File(zip);
	}

}
