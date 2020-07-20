//package org.nh.core.web.filter.security;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.log4j.Logger;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
//@Aspect
//public class ServiceAspect {
//    private static final Logger logger = Logger.getLogger(ServiceAspect.class);
//    private static final Set<String> methodsExcept = new HashSet<>();
//    static {
//        methodsExcept.add("compileLog");
//        methodsExcept.add("compileGitLog");
//    }
//
//    private Object debugOutput(ProceedingJoinPoint pjp, String src) throws Throwable {
//        Signature s = pjp.getSignature();
//        MethodSignature ms = (MethodSignature) s;
//        Method m = ms.getMethod();
//
//        long s_t = System.nanoTime();
//        if (!methodsExcept.contains(m.getName())) {
//            logger.warn("[ " + src + " ] " + pjp.getSignature().getDeclaringTypeName() +
//                    "." + pjp.getSignature().getName() + "() - 参数：" + Arrays.toString(pjp.getArgs()));
//        }
//        Object object = pjp.proceed();
//
//        boolean jsonReturn = false;
//        String jsonString = "{}";
//        if (object instanceof JSONObject) {
//            jsonReturn = true;
//            jsonString = object.toString();
//        }
//        long delta = (System.nanoTime() - s_t) / 1000000;
//
//        if (!methodsExcept.contains(m.getName())) {
//            if(object == null)
//                object = "";
//            String log = (jsonReturn ? jsonString : Arrays.toString(new Object[]{object}));
//            if(log.length() > 201){
//                log = log.substring(0, 200) + "......";
//            }
//            logger.warn("[ " + src + " ] " + pjp.getSignature().getDeclaringTypeName() +
//                    "." + pjp.getSignature().getName() + "() " + delta + "ms - 返回：" + log
//                    );
//        }
//        return object;
//    }
//
//    @Around("execution(* com.gome.gscm.service.impl.*ServiceImpl.*(..))")
//    private Object logic(ProceedingJoinPoint pjp) throws Throwable {
//        return debugOutput(pjp, "GSCM SVN Service");
//    }
//
//    @Around("execution(* com.gome.gscm.git.service.impl.*ServiceImpl.*(..))")
//    private Object logicForGit(ProceedingJoinPoint pjp) throws Throwable {
//        return debugOutput(pjp, "GSCM Git Service");
//    }
//
//}