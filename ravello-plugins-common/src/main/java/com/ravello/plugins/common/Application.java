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
package com.ravello.plugins.common;

import java.util.Map;
import java.util.Set;

import com.ravello.plugins.exceptions.ApplicationPublishException;
import com.ravello.plugins.exceptions.ApplicationWrongStateException;

public interface Application {

	public enum STATE {
		STARTED, STOPPED;
	}

	long getId();

	String getName();

	Map<String, String> getVmsDNS(DNSNameTrimmer trimmer)
			throws ApplicationWrongStateException;

	Set<Boolean> compareVmsState(Application.STATE state)
			throws ApplicationPublishException, ApplicationWrongStateException;

	interface DNSNameTrimmer {
		String trim(String name);
	}

}
