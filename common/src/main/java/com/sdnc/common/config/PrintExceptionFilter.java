package com.sdnc.common.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 * 配置请求输出错误信息
 *
 */
@Component
public final class PrintExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if ("POST".equals(req.getMethod())) {
            chain.doFilter(new PrintExceptionRequestWrapper(req), response);
            // 解密请求信息
            // if ("POST".equals(req.getMethod()) &&
            // StrKit.notBlank(req.getHeader(HeaderConstant.AES_SIGN))) {
            // chain.doFilter(new DecodeRequestBodyWrapper(req), response);
            // }
        } else {
            chain.doFilter(request, response);
        }
    }

}
