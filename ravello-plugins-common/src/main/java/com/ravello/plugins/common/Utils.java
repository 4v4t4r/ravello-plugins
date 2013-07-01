package com.ravello.plugins.common;

import java.util.Collections;

public final class Utils {

	public final static <T> Iterable<T> safeIter(Iterable<T> iterable) {
		return iterable == null ? Collections.<T> emptyList() : iterable;
	}

	public final static <T> T safeIterNext(Iterable<T> iterable) {
		Iterable<T> it = safeIter(iterable);
		if (it.iterator().hasNext())
			return it.iterator().next();
		return null;
	}
}
