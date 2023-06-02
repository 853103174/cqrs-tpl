package com.sdnc.common.auth;

import org.springframework.stereotype.Component;

import com.sdnc.common.constant.TokenConstants;
import com.sdnc.common.redis.RedisCache;

import lombok.RequiredArgsConstructor;

/**
 *
 * Token上下文, 用于获取当前操作的Token信息
 *
 */
@Component
@RequiredArgsConstructor
public class TokenContext {

	private final RedisCache<Long> redisCache;

	/**
	 * 获取当前操作的用户信息
	 */
	public AccessUser getAccessUser(String accessToken) {
		String key = String.format(TokenConstants.APP_ACCESS_TOKEN, accessToken);
		Long userId = redisCache.getValue(key);

		return new AccessUser(userId);
	}

}
