package com.sdnc.common.auth;

/**
 *
 * 用户对象上下文, 用于处理当前操作的用户信息
 *
 */
public final class AccessContext {

	private static final ThreadLocal<AccessUser> LOCAL = new ThreadLocal<>();

	public static void setAccessUser(AccessUser user) {
		LOCAL.set(user);
	}

	public static AccessUser getAccessUser() {
		return LOCAL.get();
	}

	public static void remove() {
		LOCAL.remove();
	}

}
