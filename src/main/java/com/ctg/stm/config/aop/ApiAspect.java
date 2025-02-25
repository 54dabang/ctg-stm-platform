package com.ctg.stm.config.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ctg.stm.util.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static net.logstash.logback.marker.Markers.appendEntries;

@Aspect
@Component
public class ApiAspect {
    private static final Logger logger = LoggerFactory.getLogger(ApiAspect.class);
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    // 定义切入点，匹配Controller层的所有方法
    @Pointcut("execution(* com.ctg.stm.controller.*.*(..))")
    public void webRequest() {
    }

    // 前置通知：打印请求参数
    @Around("webRequest()")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws IOException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String clazzName = methodSignature.getDeclaringType().getSimpleName(); // 类名
        String methodName = methodSignature.getName(); // 方法名


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        // 获取参数名称
        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        // 获取请求体
        Map<String, Object> requestBodyParams = new HashMap<>();
        Map<String, Object> requestParamParams = new HashMap<>();

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramName = parameterNames[i];
            Object argValue = args[i];

            if (parameter.isAnnotationPresent(RequestBody.class)) {
                requestBodyParams.put(paramName, argValue);
            } else if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = parameter.getAnnotation(RequestParam.class);
                String key = annotation.name().isEmpty() ?
                        annotation.value().isEmpty() ? paramName : annotation.value()
                        : annotation.name();
                requestParamParams.put(key, argValue);
            }
        }
       // JSONObject requestBody =JSONObject.parseObject(JSON.toJSONString(requestBodyParams)) ;


        long startTime = System.currentTimeMillis();
        Map<String, Object> logArguments = new HashMap<>();
        logArguments.put(Constants.LOG_KEY_BODY, requestBodyParams);
        logArguments.put(Constants.LOG_KEY_PARAM, requestParamParams);

        logger.info(appendEntries(logArguments),"http参数:[{}.{}]]", clazzName, methodName);
        try {
            Object result = joinPoint.proceed();
            long cost = System.currentTimeMillis() - startTime;
            if (result != null) {
                logArguments.put(Constants.LOG_KEY_RESULT,result);
                logger.info(appendEntries(logArguments),"http结果:[{}.{}] [cost={}ms]", clazzName, methodName, cost);
            } else {
                logArguments.put(Constants.LOG_KEY_RESULT,"success");
                logger.info(appendEntries(logArguments),"http结果:[{}.{}][cost={}ms]", clazzName, methodName , cost);
            }
            return result;
        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - startTime;
            logArguments.put(Constants.LOG_KEY_RESULT,"fail");
            logger.error(appendEntries(logArguments),"http异常:[{}.{}][cost={}ms]", clazzName, methodName,cost,e);
            return Constants.getResponseStr(Constants.CODE_ERROR,e.getMessage());
        }
    }


    private JSONObject getRequestBody(HttpServletRequest request) throws IOException {
        // 检查是否已经包装过请求
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }

        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;

        // 获取缓存的请求体内容
        byte[] buffer = requestWrapper.getContentAsByteArray();
        if (buffer.length == 0) {
            return null; // 没有请求体
        }

        String requestBody = new String(buffer, StandardCharsets.UTF_8);
        return JSONObject.parseObject(requestBody);
    }
}
