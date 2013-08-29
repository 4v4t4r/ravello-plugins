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
