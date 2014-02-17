package com.examples.cache;

import java.util.Map;

import com.utils.cache.Cache;

public class UeaacCacheTest {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {
		Long userId = 2046L;
		Long projectId = 2040L;
		int type = 1;
		int count = 1000;
		if (args != null && args.length > 0) {
			userId = Long.decode(args[0]);
			projectId = Long.decode(args[1]);
			type = Integer.decode(args[2]);
			count = Integer.decode(args[3]);
		} else {
			System.out.println("Parameters: userId projectId type count");
			System.exit(0);
		}
		Cache.init();
		testPerf(userId, projectId, type, count);
		Cache.stop();
	}

	private static void testPerf(Long userId, Long projectId, int type,
			int count) {
		long start = System.currentTimeMillis();
		int totalGetCount = 0;
		System.out.println("Running count:" + count + ". Now processed...");
		Object r = null;
		for (int i = 1; i <= count; i++) {
			r = getUeaacCachedObject(userId, projectId, type);
			totalGetCount++;
			if ((i + 100) % 100 == 0) {
				System.out.println(i);
			}
		}
		long end = System.currentTimeMillis();
		long spent = end - start;

		System.out.println("Object got:" + r);
		System.out.println("UserId:" + userId);
		System.out.println("ProjectId:" + projectId);
		System.out.println("Type:" + type);
		System.out.println("Count:" + count);

		System.out.println("Time:" + spent);
		System.out.println("Time per operation:" + (double) spent / (count));
	}

	private static void show(Map r) {
		System.out.println("Object ID: " + r.get("id"));
	}

	private static Object getUeaacCachedObject(Long userId, Long projectId,
			int type) {
		Object object = Cache.get(getKey(userId, projectId, type));
		if (object == null) {
			System.out.println("Oops, not found in cache");
		} else {
		}
		return object;
	}

	private static String getKey(Long userId, Long projectId, int type) {
		return "com.sunrise.ueaac.impl.UserServiceImpl_getPrivilegesByRealmProject_["
				+ userId + ",_" + type + ",_42,_" + projectId + "]";
	}

}
