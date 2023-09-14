package com.sdnc.common.auth;

import com.sdnc.common.kits.StrKit;
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
public final class TokenContext {

	private final RedisCache<String> redisCache;

	/**
	 * 获取当前操作的用户信息
	 */
	public AccessUser getAccessUser(String accessToken) {
		String key = String.format(TokenConstants.APP_ACCESS_TOKEN, accessToken);
		String accessId = redisCache.getValue(key);
		if (StrKit.isBlank(accessId)) {
			return null;
		} else {
			return new AccessUser(Long.valueOf(accessId));
		}
	}

}
