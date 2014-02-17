package com.utils.cache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import com.utils.config.Appconfig;
import com.utils.config.ConfigurationException;

/**
 * Memcached implementation (using http://code.google.com/p/spymemcached/)
 */
public class MemcachedImpl implements CacheImpl {

	private static MemcachedImpl uniqueInstance;

	public static MemcachedImpl getInstance() throws IOException {
		if (uniqueInstance == null) {
			uniqueInstance = new MemcachedImpl();
		}
		return uniqueInstance;
	}

	MemcachedClient client;

	private MemcachedImpl() throws IOException {
		System.setProperty("net.spy.log.LoggerImpl",
				"net.spy.memcached.compat.log.Log4JLogger");
		if (Appconfig.configuration.containsKey("memcached.host")) {
			// client = new MemcachedClient(new BinaryConnectionFactory(),
			// AddrUtil.getAddresses(Play.configuration.getProperty("memcached.host")));
			client = new MemcachedClient(
					AddrUtil.getAddresses(Appconfig.configuration
							.getProperty("memcached.host")));
		} else if (Appconfig.configuration.containsKey("memcached.1.host")) {
			int nb = 1;
			String addresses = "";
			while (Appconfig.configuration.containsKey("memcached." + nb
					+ ".host")) {
				addresses += Appconfig.configuration.get("memcached." + nb
						+ ".host")
						+ " ";
				nb++;
			}
			// client = new MemcachedClient(new BinaryConnectionFactory(),
			// AddrUtil.getAddresses(addresses));
			client = new MemcachedClient(AddrUtil.getAddresses(addresses));
		} else {
			throw new ConfigurationException(
					("Bad configuration for memcached"));
		}
	}

	public void add(String key, Object value, int expiration) {
		client.add(key, expiration, value);
	}

	public Object get(String key) {
		Future<Object> future = client.asyncGet(key);
		try {
			return future.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return null;
	}

	public void clear() {
		client.flush();
	}

	public void delete(String key) {
		client.delete(key);
	}

	public Map<String, Object> get(String[] keys) {
		Future<Map<String, Object>> future = client.asyncGetBulk(keys);
		try {
			return future.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return new HashMap<String, Object>();
	}

	public long incr(String key, int by) {
		return client.incr(key, by);
	}

	public long decr(String key, int by) {
		return client.decr(key, by);
	}

	public void replace(String key, Object value, int expiration) {
		client.replace(key, expiration, value);
	}

	public boolean safeAdd(String key, Object value, int expiration) {
		Future<Boolean> future = client.add(key, expiration, value);
		try {
			return future.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	public boolean safeDelete(String key) {
		Future<Boolean> future = client.delete(key);
		try {
			return future.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	public boolean safeReplace(String key, Object value, int expiration) {
		Future<Boolean> future = client.replace(key, expiration, value);
		try {
			return future.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	public boolean safeSet(String key, Object value, int expiration) {
		Future<Boolean> future = client.set(key, expiration, value);
		try {
			return future.get(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
		}
		return false;
	}

	public void set(String key, Object value, int expiration) {
		client.set(key, expiration, value);
	}

	public void stop() {
		client.shutdown();
	}
}
