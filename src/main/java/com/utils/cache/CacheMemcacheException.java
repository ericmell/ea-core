package com.utils.cache;

/**
 * Cache related exception 一般企业范围的自定义模式
 */
public class CacheMemcacheException extends CacheException {

	public CacheMemcacheException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getErrorTitle() {
		return "分布式缓存-Memcache-";
	}

	@Override
	public String getErrorDescription() {
		return getMessage();
	}

	@Override
	public String getMessage() {
		return getErrorTitle() + super.getMessage();
	}

}
