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

package com.ravellosystems.plugins.common;

import java.io.File;
import java.util.Map;

import com.ravellosystems.plugins.exceptions.ApplicationPropertiesException;

public interface IOService {

	void writeToPropertiesFile(File file, Map<String, String> properties, PropertyKeyTrimmer trimmer)
			throws ApplicationPropertiesException;

	File zipFile(File file, String zip) throws ApplicationPropertiesException;

	void unzipFile(File file, String extractTo) throws ApplicationPropertiesException;

	Map<String, String> readProperties(File file) throws ApplicationPropertiesException;

	interface PropertyKeyTrimmer {
		String trim(String name);
	}

}
