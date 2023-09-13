package com.sdnc.common.config;

import com.sdnc.common.constant.HeaderConstant;
import com.sdnc.common.redis.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * 幂等性判断
 *
 */
@Component
@RequiredArgsConstructor
public final class IdempotentInterceptor implements HandlerInterceptor {

	private final RedisCache<Boolean> cache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String token = request.getHeader(HeaderConstant.ACCESS_TOKEN);
		StringBuilder keys = new StringBuilder(50);
		keys.append(token).append(request.getRequestURI());
		if (cache.hasKey(keys.toString())) {
			response.setStatus(HttpStatus.LOCKED.value());
			return false;
		} else {
			cache.putValue(keys.toString(), true, 5, TimeUnit.MINUTES);
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String token = request.getHeader(HeaderConstant.ACCESS_TOKEN);
		StringBuilder keys = new StringBuilder(50);
		keys.append(token).append(request.getRequestURI());
		if (cache.hasKey(keys.toString())) {
			// 3秒后异步方式删除Key
			cache.asyncDel(keys.toString());
		}
	}

}
