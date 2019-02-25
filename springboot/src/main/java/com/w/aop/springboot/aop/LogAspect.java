package com.w.aop.springboot.aop;

import com.alibaba.fastjson.JSON;
import com.w.aop.springboot.annotation.OperationLogDetail;
import com.w.aop.springboot.model.OperationLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class LogAspect {
    /**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* com.wwj.springboot.service.impl.*.*(..))")'
     */

    @Pointcut("@annotation(com.w.aop.springboot.annotation.OperationLogDetail)")
    public void operationLog() {
    }


    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        long time = System.currentTimeMillis();

        try {
            res = joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            return res;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
      //获取到请求的属性
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            //获取到请求对象
            HttpServletRequest request = attributes.getRequest();
            addOpentionLog(joinPoint,res, time,request);
            return res;
        }
    }

    private void addOpentionLog(JoinPoint joinPoint, Object res, long time,HttpServletRequest request) {
        System.out.println("我是http请求获取的参数:"+request.getParameter("tel"));
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLog operationLog = new OperationLog();
        operationLog.setRunTime(time);
        operationLog.setReturnValue(JSON.toJSONString(res));
        operationLog.setId(UUID.randomUUID().toString());
        operationLog.setArgs(JSON.toJSONString(joinPoint.getArgs()));
        operationLog.setCreateTime(new Date());
        operationLog.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        operationLog.setUserId(("{{currentUserId}}"));
        operationLog.setUserName("{{currentUserName}}");

        OperationLogDetail operationLogDetail = signature.getMethod().getAnnotation(OperationLogDetail.class);
        if (operationLogDetail != null) {
            operationLog.setLevel(operationLogDetail.level());
            operationLog.setMethod(getDetail(((MethodSignature) joinPoint.getSignature()).getParameterNames(),joinPoint.getArgs(),operationLogDetail));
            operationLog.setOperationType(operationLogDetail.operationType().getValue());
            operationLog.setOperationUnit(operationLogDetail.operationUnit().getValue());

            System.out.println("我是注解里面的值:"+operationLogDetail.detail());
        }

        //这里保存日志
        System.out.println("记录日志："+operationLog.toString());

    }

    /**
     * 对当前登录用户和占位符处理
     * @param parameterNames 方法参数名称数组
     * @param args 方法参数数组
     * @param operationLogDetail 注解信息
     * @return 返回处理后的描述
     */

    private String getDetail(String[] parameterNames, Object[] args, OperationLogDetail operationLogDetail) {
        Map<Object,Object> map = new HashMap<>(4);
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i],args[i]);
        }

        String detail = operationLogDetail.detail();

            detail = "'"+"{{currentUserName}}"+"'=>>"+operationLogDetail.detail();

        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object k = entry.getKey();
            Object v = entry.getValue();
            detail = detail.replace("{{" + k + "}}", JSON.toJSONString(v));
        }
        return detail;
    }


    @Before("operationLog()")
    public  void doBeforeAdvice(JoinPoint point) {
        System.out.println("进入方法前");
    }

    /**
     * 处理完请求，返回内容
     * @param ret
     */

    @AfterReturning(returning = "ret",pointcut = "operationLog()")
    public void doAferReturning (Object ret) {
        System.out.println("方法异常时执行..." + ret);
    }
    /**
     * 后置异常通知
     */

    @AfterThrowing("operationLog()")
    public  void  throess(JoinPoint point) {
        System.out.println("方法异常时执行......");
    }

    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     */
    @After("operationLog()")
    public  void  after(JoinPoint point) {
        System.out.println("方法最后执行......");
    }

}
