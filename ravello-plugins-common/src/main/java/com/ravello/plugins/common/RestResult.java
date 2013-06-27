package com.ravello.plugins.common;

public interface RestResult {

	<T> T  to(Class<T> type);
}
