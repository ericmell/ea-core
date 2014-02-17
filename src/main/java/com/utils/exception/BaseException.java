package com.utils.exception;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 异常的基础类，各模块自己定义异常实体类， <br>
 * 从这里继承 BaseException--> CacheException -->CacheMemcacheException abstract
 * class BaseException（ 的定义,是不能实例化的）
 * 
 * <br>
 * 通过title 可以方便错误定位,使用方法 ,参考测试案例<br>
 * 
 * <pre>
 * throw new CacheMemcacheException(&quot;参数不能为空&quot;, new NullPointerException());
 * </pre>
 * 
 */
public abstract class BaseException extends RuntimeException {

	static AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());
	/** 错误ID */
	String id;

	public BaseException() {
		super();
		setId();
	}

	public BaseException(String message) {
		super(message);
		setId();
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
		setId();
	}

	void setId() {
		long nid = atomicLong.incrementAndGet();
		id = Long.toString(nid, 26);
	}

	public abstract String getErrorTitle();

	public abstract String getErrorDescription();

	public boolean isSourceAvailable() {
		return false;
	}

	public Integer getLineNumber() {
		return -1;
	}

	public String getSourceFile() {
		return "";
	}

	public String getId() {
		return id;
	}

	public static StackTraceElement getInterestingStrackTraceElement(
			Throwable cause) {
		return null;
	}
}