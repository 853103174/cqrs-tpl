package com.sdnc.common.auth;

import com.sdnc.common.constant.HeaderConstant;
import com.sdnc.common.kits.HttpWriterKit;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * 校验用户是否登录过滤器
 */
@Component
@Order(100)
@RequiredArgsConstructor
@EnableConfigurationProperties(PassportUrlProperties.class)
public final class PassportFilter implements Filter {

	private final TokenContext tokenContext;
	private final PassportUrlProperties urlProperties;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		final String accessToken = req.getHeader(HeaderConstant.ACCESS_TOKEN);
		//if (ObjectUtils.isEmpty(accessToken)) {
		//	HttpStatus status = HttpStatus.PRECONDITION_FAILED;
		//	HttpWriterKit.text(response, "登录信息获取失败", status);
		//	return;
		//}
		if (urlProperties.contains(req.getRequestURI())) {
			chain.doFilter(request, response);
		} else {
			final AccessUser accessUser = tokenContext.getAccessUser(accessToken);
			if (Objects.isNull(accessUser)) {
				HttpStatus status = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
				HttpWriterKit.text(response, "请重新登录", status);
				return;
			}
			AccessContext.setAccessUser(accessUser);
			chain.doFilter(request, response);
			AccessContext.remove();
		}
	}

}
