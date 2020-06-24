package org.nh.core.log.eventlog.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.nh.core.exception.ParentException;
import org.nh.core.exception.ParentRuntimeException;
import org.nh.core.exception.bean.ExceptionMsg;
import org.nh.core.log.contents.LogAppenderName;
import org.nh.core.log.contents.LogType;
import org.nh.core.log.eventlog.bean.EventLogBean;
import org.nh.core.log.eventlog.contents.EventLogLevel;
import org.nh.core.log.eventlog.contents.EventLogType;
import org.nh.core.util.clazz.ClazzUtil;
import org.nh.core.util.json.JsonUtil;
import org.nh.core.util.map.MapUtil;
import org.nh.core.util.string.RegexUtil;
import org.nh.core.util.system.EnvironmentUtil;
import org.nh.core.util.thread.ThreadLocalUtil;
import org.nh.core.web.contants.ResultRespStatus;
import org.nh.core.web.req.util.IpUtil;
import org.nh.core.web.resp.ResultResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Classname RestLogAspect
 * @Description TODO 事件日志切入操作
 * @Date 2020/4/13 6:43 PM
 * @Created by nihui
 *
 *  标注了 @RestLog 注解的类将会对事件日志进行记录
 */
@Component
@Aspect
public class RestLogAspect {

    protected final transient Logger eventlogger = LoggerFactory.getLogger(LogAppenderName.EVENT);
    protected final transient Logger systemExceptionlogger = LoggerFactory.getLogger(LogAppenderName.EXCEPTION_SYSTEM);

    /**
     * 使用 @Value 从配置文件中获取对应的配置，默认框架为 Spring Cloud
     */
    @Value("${nh.support.log.restlog.component:未知}")
    private String component;
    @Value("${nh.support.log.restlog.subSystem:未知}")
    private String subSystem;
    @Value("${nh.support.log.restlog.framework:未知}")
    private String framework="SpringCloud";

    private String logType = LogType.EVENT;
    private String eventLogType = EventLogType.API_EXECUTE;
    private String hostname = EnvironmentUtil.getHostName();

    private EventLogBean eventLogBean;

    private long eventLogStartExecuteTime;//日志开始执行的时间

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private String REGEX_API_VERSION="v[0-9]{0,1}";


    /**
     * 编写切点方法
     * 切入点就是是否存在 @RestLog 和 @IgnoreRestLog 两个注解
     */
    @Pointcut("!@within(org.nh.core.log.eventlog.aop.IgnoreRestLog) && !@annotation(org.nh.core.log.eventlog.aop.IgnoreRestLog)  && (@annotation(org.nh.core.log.eventlog.aop.RestLog) || @within(org.nh.core.log.eventlog.aop.RestLog))")
    public void restAspect() {
    }


    /**
     * 环形注解，也就是在任意位置都会调用该切面
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("restAspect()")
    public Object doRestAspectAround(ProceedingJoinPoint joinPoint) throws Throwable {
        eventLogBean = new EventLogBean();
        eventLogStartExecuteTime = System.currentTimeMillis();

        Object object = null;

        long apiStartTime = System.currentTimeMillis();
        object = joinPoint.proceed();
        long apiEndTime = System.currentTimeMillis();
        long apiExecuteTime = apiEndTime - apiStartTime;
        eventLogBean.setApiExecutionTime(apiExecuteTime);
        return object;
    }

    /**
     * 在操作之前调用，前置
     * @param joinPoint
     */
    @Before("restAspect()")
    public void doRestAspectBefore(JoinPoint joinPoint) {

        eventLogBean.setLogType(logType);
        eventLogBean.setHostname(hostname);
        eventLogBean.setTimetamp(sdf.format(System.currentTimeMillis()));//执行完需要不全

        eventLogBean.setComponent(component);
        eventLogBean.setSubSystem(subSystem);
        eventLogBean.setFramework(framework);
        // 获取请求参数 从Spring Web 中获取到对应的请求参数，这块可以对请求参数做二次封装，并且记录到日志中
        RequestAttributes requestAttributes=RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletContainer = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest httpServletRequest = servletContainer.getRequest();
        eventLogBean.setClientIp(IpUtil.getRequestClientIp(httpServletRequest));

        eventLogBean.setEventLogLevel(EventLogLevel.INFO);
        eventLogBean.setEventLogType(eventLogType);
        String logId = UUID.randomUUID().toString();
        eventLogBean.setEventLogId(logId);
        ThreadLocalUtil.getInstance().set(logId);
        eventLogBean.setEventLogExecutionTime(-1);//执行完需要不全

        eventLogBean.setHttpMethod(httpServletRequest.getMethod());
        eventLogBean.setClazzName(joinPoint.getTarget().getClass().getName());
        eventLogBean.setMethodName(joinPoint.getSignature().getName());
        eventLogBean.setApiName(httpServletRequest.getRequestURI());
        eventLogBean.setApiVersion(RegexUtil.getRegexFirstPartStr(httpServletRequest.getRequestURI(), REGEX_API_VERSION));
        eventLogBean.setApiParams(reqPathParams2Json(httpServletRequest));
        eventLogBean.setApiExecutionTime(-1);//执行完需要不全

    }

    /**
     * 后置，返回之后做处理
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "restAspect()", returning = "result")
    public void doRestAspectAfterReturning(JoinPoint joinPoint, Object result) {
        if (result != null) {
            if(ResultResp.class.isAssignableFrom(result.getClass())) {
                recordResultRespResponse(result);
            }
            else if(ResponseEntity.class.isAssignableFrom(result.getClass())) {
                recordResponseEntity(result);
            }else if(Response.class.isAssignableFrom(result.getClass())){
                recordJerseyResponse(result);
            }
        }

        long eventLogEndExecuteTime = System.currentTimeMillis();
        long eventLogExecuteTime = eventLogEndExecuteTime - eventLogStartExecuteTime - eventLogBean.getApiExecutionTime();
        eventLogBean.setEventLogExecutionTime(eventLogExecuteTime);

        eventlogger.info(JsonUtil.toJson(eventLogBean));
    }

    /**
     * 在捕获到异常之后进行返回
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "restAspect()", throwing = "e")
    public void doRestAspectAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (e != null) {
            if(ParentException.class.isAssignableFrom(e.getClass())) {

            }else if(ParentRuntimeException.class.isAssignableFrom(e.getClass())) {

            }else {
                systemExceptionlogger.error("系统运行发生异常", e);
                if(e.getCause()!=null) {
                    eventLogBean.setErrorMsg(e.getCause().getMessage());
                }else {
                    eventLogBean.setErrorMsg(e.getMessage());
                }
            }
        }

        eventLogBean.setEventLogLevel(EventLogLevel.EXCEPTION);

        long eventLogEndExecuteTime = System.currentTimeMillis();
        long eventLogExecuteTime = eventLogEndExecuteTime - eventLogStartExecuteTime - eventLogBean.getApiExecutionTime();
        eventLogBean.setEventLogExecutionTime(eventLogExecuteTime);

        eventlogger.info(JsonUtil.toJson(eventLogBean));

    }

    // ============= 扩展内容部分 =============
    /**
     * 用于进行数据格式转换
     * @param result
     */
    private void recordResultRespResponse(Object result) {
        ResultResp<String> resultResp = (ResultResp<String>) result;

        int httpStatus = HttpServletResponse.SC_OK;
        eventLogBean.setHttpStatus(httpStatus + "");

        String status=resultResp.getStatus();
        if(ResultRespStatus.EXCEPTION.equals(status)) {
            eventLogBean.setEventLogLevel(EventLogLevel.EXCEPTION);

            ExceptionMsg exceptionBean = resultResp.getExceptionMsg();
            eventLogBean.setErrorId(exceptionBean.getErrorId());
            eventLogBean.setErrorCode(exceptionBean.getErrorCode() + "");
            eventLogBean.setErrorMsg(exceptionBean.getErrorMsg());
        }else if(ResultRespStatus.ERROR.equals(status)) {
            eventLogBean.setEventLogLevel(EventLogLevel.ERROR);

            ExceptionMsg exceptionBean = resultResp.getExceptionMsg();
            eventLogBean.setErrorId(exceptionBean.getErrorId());
            eventLogBean.setErrorCode(exceptionBean.getErrorCode() + "");
            eventLogBean.setErrorMsg(exceptionBean.getErrorMsg());
        }
    }

    /**
     * 解析 spring response 的数据
     * @param result
     */
    private void recordResponseEntity(Object result) {
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) result;
        int httpStatus = responseEntity.getStatusCodeValue();
        eventLogBean.setHttpStatus(httpStatus + "");
        if (httpStatus >= 400 && result != null) {
            eventLogBean.setEventLogLevel(EventLogLevel.ERROR);
            if(StringUtils.isNotBlank(responseEntity.getBody())){
                ExceptionMsg exceptionBean = JsonUtil.toObject(responseEntity.getBody(), ExceptionMsg.class);
                if(exceptionBean!=null){
                    eventLogBean.setErrorId(exceptionBean.getErrorId());
                    eventLogBean.setErrorCode(exceptionBean.getErrorCode() + "");
                    eventLogBean.setErrorMsg(exceptionBean.getErrorMsg());
                }
            }
        }
    }


    /**
     * 解析 Jersey Response 数据
     * @param result
     */
    private void recordJerseyResponse(Object result) {
        Response response = (Response) result;
        int httpStatus = response.getStatus();
        eventLogBean.setHttpStatus(httpStatus + "");
        if (httpStatus >= 400 && result != null) {
            eventLogBean.setEventLogLevel(EventLogLevel.ERROR);
            Object entity=response.getEntity();
            if(entity!=null&&String.class.isAssignableFrom(entity.getClass())){
                ExceptionMsg exceptionBean = JsonUtil.toObject(entity+"", ExceptionMsg.class);
                if(exceptionBean!=null){
                    eventLogBean.setErrorId(exceptionBean.getErrorId());
                    eventLogBean.setErrorCode(exceptionBean.getErrorCode() + "");
                    eventLogBean.setErrorMsg(exceptionBean.getErrorMsg());
                }
            }
        }
    }

    /***
     * 从请求参数中获取数据进行解析，并且可以过滤掉一部分数据
     * @param httpServletRequest
     * @return
     */
    private String reqPathParams2Json(HttpServletRequest httpServletRequest) {
        Map<String, String[]> params = httpServletRequest.getParameterMap();
        Map<String, String[]> newParams =null;
        String newParamsJson = null;
        if (params != null && params.size() > 0) {
            // cleartextPassword
            newParams = MapUtil.removeEntities(params, new String[] { "cleartextPassword" });
            newParamsJson = JsonUtil.toJson(newParams);
        }
        return newParamsJson;
    }

    /**
     * 获取方法调用的时候传递的参数值
     * @param joinPoint
     * @return
     */
    private Map<String, String> recordParamsJson(JoinPoint joinPoint) {
		// TODO 等待后续扩展
		String clazzName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();

		Object[] objs = joinPoint.getArgs();
		if (objs == null || objs.length == 0) {
			return null;
		}

		String[] paramNames = ClazzUtil.getFieldsName(this.getClass(), clazzName, methodName);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (int i = 0; i < paramNames.length; i++) {
		}
		return null;
	}

}
