package com.sdnc.common.beetlsql;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.beetl.sql.core.ThreadLocalSQLManager;

/**
 * 为service提供一个切面, 根据配置动态切换数据源
 */
//@Aspect
//@Configuration
public class BeetlSqlDatasourceConfig {

	@Around("within(@org.springframework.stereotype.Service *)")
	public Object functionAccessCheck(final ProceedingJoinPoint pjp) throws Throwable {
		String old = ThreadLocalSQLManager.locals.get();
		MethodSignature sig = (MethodSignature) pjp.getSignature();
		Use use = sig.getMethod().getDeclaringClass().getAnnotation(Use.class);
		if (use == null) {
			//设置为默认
			ThreadLocalSQLManager.locals.set(null);
		} else {
			String targetSqlManager = use.value();
			ThreadLocalSQLManager.locals.set(targetSqlManager);
		}

		try {
			Object o = pjp.proceed();
			return o;
		} finally {
			ThreadLocalSQLManager.locals.set(old);
		}
	}

}
