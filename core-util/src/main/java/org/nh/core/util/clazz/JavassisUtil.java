package org.nh.core.util.clazz;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * @Classname JavassisUtil
 * @Description TODO 用来进行类梳理
 * @Date 2020/1/15 1:52 PM
 * @Created by nihui
 */
public class JavassisUtil {

    /**
     * 获取某个类的某个方法的参数名称列表
     * @param cls
     * @param clazzName
     * @param methodName
     * @return
     * @throws NotFoundException 当没有找到对应的类或者没有找到对应的方法会抛出异常
     */
    public static String[] getFieldName(Class<?> cls,String clazzName,String methodName) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);
        CtClass cc = null;
        CtMethod cm = null;
        String[] paramNames = null;
        cc = pool.get(clazzName);
        cm = cc.getDeclaredMethod(methodName);
        paramNames  = new String[cm.getParameterTypes().length];

        //使用javassist的反射方法获取方法的参数名
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

        if (attr ==null){
            return null;
        }
        int pos =Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        //paramNames即参数名
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i+pos);
        }
        return paramNames;

    }
}
