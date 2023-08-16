package com.sdnc.common.log;

import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.noear.snack.ONode;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.sdnc.common.exception.SystemException;
import com.sdnc.common.exception.TokenException;

import log.tiny.TinyLog;
import log.util.CommUtil;

/**
 *
 * 业务操作日志切面类
 *
 */
@Aspect
@Component
@Profile("dev")
public final class BusLogAop implements Ordered {

    private final TinyLog log = TinyLog.getInstance();

    /**
     * 定义BusLogAop的切入点为标记@BusLog注解的方法
     */
    @Pointcut("execution(* com.sdnc..interfaces.controller..*(..)) || execution(* com.sdnc..application.service..*(..))")
    public void pointcut() {
    }

    /**
     * 业务操作环绕通知
     *
     * @param proceedingJoinPoint
     * @retur
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        // 执行目标方法
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
            // 目标方法执行完成后，获取目标类、目标方法上的业务日志注解上的功能名称和功能描述
            Map<String, Object> map = new LinkedHashMap<>();
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            map.put("serviceName", signature.getDeclaringTypeName());
            map.put("methodName", signature.getName());
            map.put("args", proceedingJoinPoint.getArgs());
            map.put("result", result);
            log.info(ONode.stringify(map));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            // 目标方法执行完成后，获取目标类、目标方法上的业务日志注解上的功能名称和功能描述
            Map<String, Object> map = new LinkedHashMap<>();
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            map.put("declaringTypeName", signature.getDeclaringTypeName());
            map.put("name", signature.getName());
            map.put("args", proceedingJoinPoint.getArgs());
            map.put("throwable", CommUtil.getExpStack(throwable));
            // 把参数报文写入到日志中
            log.error(ONode.stringify(map));

            if (throwable instanceof SystemException) {
                throw new SystemException(throwable.getMessage());
            } else if (throwable instanceof TokenException) {
                throw new TokenException();
            } else {
                throw new RuntimeException(throwable);
            }
        }
        // 目标方法执行完成后，获取目标类、目标方法上的业务日志注解上的功能名称和功能描述
        // Object target = proceedingJoinPoint.getTarget();
        // Object[] args = proceedingJoinPoint.getArgs();
        // MethodSignature signature = (MethodSignature)
        // proceedingJoinPoint.getSignature();
        // BusLog anno1 = target.getClass().getAnnotation(BusLog.class);
        // BusLog anno2 = signature.getMethod().getAnnotation(BusLog.class);
        // BusLogBean busLogBean = new BusLogBean();
        // String logName = anno1.name();
        // String logDescrip = anno2.descrip();
        // busLogBean.setBusName(logName).setBusDescrip(logDescrip)
        // .setOperPerson("").setOperTime(new Date());
        // 把参数报文写入到日志中
        // log.info(ONode.stringify(args));
        // log.info(ONode.stringify(busLogBean));
        // 保存业务操作日志信息
        // this.busLogDao.insert(busLogBean);

        return result;
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
