package org.nh.core.util.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Classname HessianSerializeUtil
 * @Description TODO Hessian序列化及反序列化工具
 * @Date 2020/1/15 2:54 PM
 * @Created by nihui
 */
public class HessianSerializeUtil {

    public static byte[] serialize(Object object){
        if(object==null){
            throw new NullPointerException();
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        HessianOutput hessianOutput=new HessianOutput(byteArrayOutputStream);
        try {
            hessianOutput.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data){
        if(data==null){
            throw new NullPointerException();
        }
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(data);
        HessianInput hessianInput=new HessianInput(byteArrayInputStream);
        try {
            return (T)hessianInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
