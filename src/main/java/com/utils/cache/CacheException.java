package com.utils.cache;

import com.utils.exception.BaseException;

/**
 * Cache related exception 一般企业范围的自定义模式
 */
public class CacheException extends BaseException {

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getErrorTitle() {
		return "缓存模块-";
	}

	public String getErrorDescription() {
		return getMessage();
	}

	public String getMessage() {
		return getErrorTitle() + super.getMessage();
	}

}
