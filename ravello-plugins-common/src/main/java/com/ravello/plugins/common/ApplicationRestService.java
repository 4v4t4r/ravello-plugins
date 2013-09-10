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

package com.ravello.plugins.common;

import com.ravello.plugins.exceptions.ApplicationDeleteException;
import com.ravello.plugins.exceptions.ApplicationNotFoundException;
import com.ravello.plugins.exceptions.ApplicationStartException;
import com.ravello.plugins.exceptions.ApplicationStopException;

public interface ApplicationRestService {

	void start(long appId, int autoStop) throws ApplicationStartException;

	void stop(long appId) throws ApplicationStopException;

	void delete(long appId) throws ApplicationDeleteException;

	void publish(long appId, String preferredCloud, String preferredZone, int autoStop);

	void publishCostOptimized(long appId, int autoStop);

	void publishPerformanceOptimized(long appId, int autoStop);

	Application findApplication(String appName) throws ApplicationNotFoundException;

	Application findApplication(long appId) throws ApplicationNotFoundException;
}
