package com.sdnc.common.kits;

import org.springframework.util.Assert;

import com.sdnc.common.exception.SystemException;

/**
 *
 * 断言工具类
 *
 */
public final class ValidKit extends Assert {

	/**
	 * 不为空
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new SystemException(message);
		}
	}

}
