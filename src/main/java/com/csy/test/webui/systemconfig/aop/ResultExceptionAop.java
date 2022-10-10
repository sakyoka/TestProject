package com.csy.test.webui.systemconfig.aop;

import java.lang.reflect.Method;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.csy.test.commons.jarmanage.exception.JarManageException;
import com.csy.test.commons.result.ResultBean;
import com.csy.test.webui.systemconfig.annotation.Ignore;
import com.csy.test.webui.systemconfig.exception.CommonException;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：统一处异常、打印日志
 * @author csy
 * @date 2022年1月14日 上午9:48:19
 */
@Component
@Aspect
@Log4j2
public class ResultExceptionAop {
	 
    @Pointcut("execution(public * com.csy.test.webui.*.controller.*.*(..))")
    public void result(){}
    
    @Around(value = "result()")
    public Object around(ProceedingJoinPoint point){    	
		try {
			return point.proceed();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
	    	if (isIgroe(point)){
	    		throw new RuntimeException(e);
	    	}else{
	    		String msg = (e instanceof CommonException ? e.getMessage(): "系统内部处理异常，请联系管理员");
	    		Object data = (e instanceof JarManageException ? ExceptionUtils.getStackTrace(e) : null);
				return ResultBean.builder()
						.code(ResultBean.INTERNAL_ERROR)
						.msg(msg)
						.data(data)
						.build();
	    	}
		}
    }
    
    /**
     * 描述：是否忽略
     * @author csy 
     * @date 2022年1月17日 下午5:27:25
     * @param point
     */
    private boolean isIgroe(ProceedingJoinPoint point){
        Signature signature = point.getSignature();    
        MethodSignature methodSignature = (MethodSignature)signature;    
        Method targetMethod = methodSignature.getMethod();
        return targetMethod.isAnnotationPresent(Ignore.class);
    }
}
