package com.sdnc.common.conf;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.sdnc.common.constant.HeaderConstant;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * 加密请求解码过滤器
 *
 */
@Component
public class DecodeRequestBodyFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if ("POST".equals(req.getMethod()) && !ObjectUtils.isEmpty(req.getHeader(HeaderConstant.AES_SIGN))) {
			chain.doFilter(new DecodeRequestBodyWrapper(req), response);
		} else {
			chain.doFilter(request, response);
		}
	}

}
