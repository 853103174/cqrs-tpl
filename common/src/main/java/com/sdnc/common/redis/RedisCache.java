package com.sdnc.common.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

/**
 *
 * Redis命令封装
 *
 */
@Component
@AllArgsConstructor
public class RedisCache<T> {

	private final RedisTemplate<String, T> redis;

	/**
	 * 缓存基本的对象，Integer、String、实体类等
	 *
	 * @param key
	 *              缓存的键值
	 * @param value
	 *              缓存的值
	 */
	public void putValue(String key, T value) {
		redis.opsForValue().set(key, value);
	}

	/**
	 * 缓存基本的对象，Integer、String、实体类等
	 *
	 * @param key
	 *                 缓存的键值
	 * @param value
	 *                 缓存的值
	 * @param timeout
	 *                 时间
	 * @param timeUnit
	 *                 时间颗粒度
	 */
	public void putValue(final String key, T value, final Integer timeout, final TimeUnit timeUnit) {
		redis.opsForValue().set(key, value, timeout, timeUnit);
	}

	/**
	 * 获得缓存的基本对象
	 *
	 * @param key
	 *            缓存键值
	 * @return 缓存键值对应的数据
	 */
	public T getValue(final String key) {
		ValueOperations<String, T> operation = redis.opsForValue();
		return operation.get(key);
	}

	/**
	 * 缓存List数据
	 *
	 * @param key
	 *               缓存的键值
	 * @param values
	 *               待缓存的List数据
	 * @return 缓存的对象个数
	 */
	public long putList(final String key, final List<T> values) {
		Long count = redis.opsForList().rightPushAll(key, values);
		return count == null ? 0 : count;
	}

	/**
	 * 获得缓存的List对象
	 *
	 * @param key
	 *            缓存的键值
	 * @return 缓存键值对应的数据
	 */
	public List<T> getList(final String key) {
		return redis.opsForList().range(key, 0, -1);
	}

	/**
	 * 缓存Set
	 *
	 * @param key
	 *               缓存键值
	 * @param values
	 *               缓存的数据
	 * @return 缓存的Set对象
	 */
	// public BoundSetOperations<String, T> putSet(final String key, final Set<T>
	// values) {
	// BoundSetOperations<String, T> setOperation = redis.boundSetOps(key);
	// Iterator<T> it = values.iterator();
	// while (it.hasNext()) {
	// setOperation.add(it.next());
	// }
	// return setOperation;
	// }

	/**
	 * 获得缓存的Set
	 *
	 * @param key
	 *            缓存的键值
	 * @return 缓存的Set对象
	 */
	public Set<T> getSet(final String key) {
		return redis.opsForSet().members(key);
	}

	/**
	 * 缓存多个Hash值
	 *
	 * @param key
	 *                缓存的键值
	 * @param entries
	 *                多个Hash值
	 */
	public void putEntries(final String key, final Map<String, T> entries) {
		redis.opsForHash().putAll(key, entries);
	}

	/**
	 * 获得缓存的多个Hash值
	 *
	 * @param key
	 *            缓存的键值
	 * @return 多个Hash值
	 */
	public Map<String, T> getEntries(final String key) {
		HashOperations<String, String, T> opsForHash = redis.opsForHash();
		return opsForHash.entries(key);
	}

	/**
	 * 往Hash中存入数据
	 *
	 * @param key
	 *              缓存的键值
	 * @param hKey
	 *              Hash键
	 * @param value
	 *              值
	 */
	public void putMapValue(final String key, final String hKey, final T value) {
		redis.opsForHash().put(key, hKey, value);
	}

	/**
	 * 获取Hash中的数据
	 *
	 * @param key
	 *             缓存的键值
	 * @param hKey
	 *             Hash键
	 * @return Hash中的对象
	 */
	public T getMapValue(final String key, final String hKey) {
		HashOperations<String, String, T> opsForHash = redis.opsForHash();
		return opsForHash.get(key, hKey);
	}

	/**
	 * 获取多个Hash中的数据
	 *
	 * @param key
	 *              缓存的键值
	 * @param hKeys
	 *              Hash键集合
	 * @return Hash对象集合
	 */
	public List<T> getMultiMapValue(final String key, final Collection<String> hKeys) {
		HashOperations<String, String, T> opsForHash = redis.opsForHash();
		return opsForHash.multiGet(key, hKeys);
	}

	/**
	 * 获得缓存的基本对象列表
	 *
	 * @param pattern
	 *                字符串前缀
	 * @return 对象列表
	 */
	public Collection<String> keys(final String pattern) {
		return redis.keys(pattern);
	}

	/**
	 * 设置有效时间
	 *
	 * @param key
	 *                缓存的键值
	 * @param timeout
	 *                超时时间(秒)
	 * @return true=设置成功；false=设置失败
	 */
	public boolean expire(final String key, final long timeout) {
		return expire(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 设置有效时间
	 *
	 * @param key
	 *                缓存的键值
	 * @param timeout
	 *                超时时间
	 * @param unit
	 *                时间单位
	 * @return true=设置成功；false=设置失败
	 */
	public boolean expire(final String key, final long timeout, final TimeUnit unit) {
		return redis.expire(key, timeout, unit);
	}

	/**
	 * 是否存在给定的键值
	 *
	 * @param key
	 *            缓存的键值
	 */
	public boolean hasKey(final String key) {
		return redis.hasKey(key);
	}

	/**
	 * 将缓存的键值+1
	 *
	 * @param key
	 *            缓存的键值
	 */
	public Long increment(final String key) {
		return redis.boundValueOps(key).increment();
	}

	/**
	 * 删除单个对象
	 *
	 * @param key
	 *            缓存的键值
	 */
	public boolean delete(final String key) {
		return redis.delete(key);
	}

	/**
	 * 删除集合对象
	 *
	 * @param collection
	 *                   多个对象
	 * @return 删除的个数
	 */
	public long delete(final Collection<String> collection) {
		return redis.delete(collection);
	}

}
