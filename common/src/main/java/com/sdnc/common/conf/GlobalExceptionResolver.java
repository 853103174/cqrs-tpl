package com.sdnc.common.conf;

import java.io.InputStreamReader;
import java.util.Map;

import org.noear.snack.ONode;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import log.tiny.TinyLog;
import log.util.CommUtil;

/**
 *
 * 全局异常信息打印
 *
 */
@Order(-1000)
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

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
				result.append(CommUtil.getExpStack(ex));
				log.fatal(result);
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
				result.append(CommUtil.getExpStack(ex));
				log.fatal(result);
			} catch (Exception e) {
			}
		}

		return null;
	}

}
