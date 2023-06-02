package com.sdnc.common.config;

import java.io.InputStreamReader;
import java.util.Map;

import org.noear.snack.ONode;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sdnc.common.exception.SystemException;
import com.sdnc.common.exception.TokenException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import log.tiny.TinyLog;
import log.util.CommUtil;

/**
 *
 * 全局异常信息打印
 *
 */
@Order(-1000)
@Component
public class PrintExceptionResolver implements HandlerExceptionResolver {

	private static final TinyLog log = TinyLog.getInstance();
	private static final String endStr = TinyLog.endStr;

	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object handler,
			Exception ex) {
		Map<String, String[]> parasMap = req.getParameterMap();
		if (parasMap.isEmpty()) {
			try (InputStreamReader isr = new InputStreamReader(req.getInputStream(), "UTF-8");) {
				StringBuilder result = new StringBuilder(4000);
				result.append(endStr);
				result.append(endStr);
				result.append("地址:=============================");
				result.append(endStr);
				result.append(req.getRequestURI());
				result.append(endStr);
				result.append("参数:=============================");
				result.append(endStr);
				char[] buf = new char[1024];
				for (int num; (num = isr.read(buf, 0, buf.length)) != -1;) {
					result.append(buf, 0, num);
				}
				result.append(endStr);
				result.append("异常:=============================");
				result.append(endStr);
				if (ex instanceof BindException be) {
					FieldError error = be.getBindingResult().getFieldErrors().get(0);
					result.append(error.getField()).append("--").append(error.getDefaultMessage());
					log.warn(result);
				} else if (ex instanceof ValidationException ve) {
					result.append(ve.getMessage());
					log.warn(result);
				} else if (ex instanceof TokenException te) {
					result.append("未获取到RefreshToken");
					log.warn(result);
				} else if (ex instanceof SystemException se) {
					result.append(se.getMessage());
					log.fatal(result);
				} else {
					result.append(CommUtil.getExpStack(ex));
					log.fatal(result);
				}
			} catch (Exception e) {
			}
		} else {
			try {
				StringBuilder result = new StringBuilder(4000);
				result.append(endStr);
				result.append(endStr);
				result.append("地址:=============================");
				result.append(endStr);
				result.append(req.getRequestURI());
				result.append(endStr);
				result.append("参数:=============================");
				result.append(endStr);
				result.append(ONode.stringify(parasMap));
				result.append(endStr);
				result.append("异常:=============================");
				result.append(endStr);
				if (ex instanceof BindException be) {
					FieldError error = be.getBindingResult().getFieldErrors().get(0);
					result.append(error.getField()).append("--").append(error.getDefaultMessage());
					log.warn(result);
				} else if (ex instanceof ValidationException ve) {
					result.append(ve.getMessage());
					log.warn(result);
				} else if (ex instanceof TokenException te) {
					result.append("未获取到RefreshToken");
					log.warn(result);
				} else if (ex instanceof SystemException se) {
					result.append(se.getMessage());
					log.fatal(result);
				} else {
					result.append(CommUtil.getExpStack(ex));
					log.fatal(result);
				}
			} catch (Exception e) {
			}
		}

		return null;
	}

}
