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

import java.util.Collections;
import java.util.Map;

public final class Utils {

	public final static <T> Iterable<T> safeIter(Iterable<T> iterable) {
		return iterable == null ? Collections.<T> emptyList() : iterable;
	}

	public final static <K, V> Map<K, V> safeMap(Map<K, V> map) {
		return map == null ? Collections.<K, V> emptyMap() : map;
	}

	public final static Boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public final static Boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public final static Boolean isEmpty(String... strings) {
		for (String string : strings) {
			if (string == null || string.isEmpty())
				return true;
		}
		return false;
	}

	public final static <T> T safeIterNext(Iterable<T> iterable) {
		Iterable<T> it = safeIter(iterable);
		if (it.iterator().hasNext())
			return it.iterator().next();
		return null;
	}
}
