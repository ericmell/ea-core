package com.common.cache;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.cache.Cache;

/**
 * 基本 单元测试
 */
public class CacheSimpleTest extends TestCase {
	private static Logger log = LoggerFactory.getLogger(CacheSimpleTest.class);
	static String cacheKey = "User_101";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPutAndGet() {
		String user = (String) Cache.get(cacheKey);
		if (user == null) {
			user = "张三";
			Cache.set(cacheKey, user, "60mn"); // 放入缓存
		}
		log.info("放入用户：" + user);

		user = (String) Cache.get(cacheKey); // 读取缓存
		log.info("取出用户:" + user);
	}

}
