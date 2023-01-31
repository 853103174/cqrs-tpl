package com.sdnc.common.conf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sdnc.common.constant.HeaderConstant;
import com.sdnc.common.redis.RedisCache;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

/**
 *
 * 幂等性判断
 *
 */
@Component
@AllArgsConstructor
public class IdempotentInterceptor implements HandlerInterceptor {

	private final RedisCache<Boolean> cache;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
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
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String token = request.getHeader(HeaderConstant.ACCESS_TOKEN);
		StringBuilder keys = new StringBuilder(50);
		keys.append(token).append(request.getRequestURI());
		if (cache.hasKey(keys.toString())) {
			// 3秒后异步方式删除Key
			CompletableFuture.runAsync(() -> {
				try {
					Thread.sleep(3000);
					cache.delete(keys.toString());
				} catch (InterruptedException e) {
				}
			});
		}
	}

}
