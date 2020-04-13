package org.nh.core.util.string;

import java.util.regex.Pattern;

/**
 * @Classname StringUtils
 * @Description TODO
 * @Date 2019/11/22 2:48 PM
 * @Created by nihui
 */
public class StringUtils {


    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)"); //key value pair pattern.

    private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");

    /**
     * 判断字符串是否是空
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        if (str==null||str.length()==0){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str){
        if (str == null || str.length()==0){
            return true;
        }
        return false;
    }

    /**
     * 判断字符串非空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return str!=null&&str.length()>0;
    }

    /**
     * 判断字符串是否相等
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isEquals(String s1,String s2){
        if (s1==null && s2 == null){
            return true;
        }
        if (s1 == null || s2 == null){
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * 判断是否可以被转换为整型
     * @param str
     * @return
     */
    public static boolean isInteger(String str){
        if (str==null||str.length()==0){
            return false;
        }
        return INT_PATTERN.matcher(str).matches();
    }

    /**
     * 直接转换
     * @param str
     * @return
     */
    public static int parseInteger(String str){
        if (!isInteger(str)){
            return 0;
        }
        return Integer.parseInt(str);
    }


    public static String join(String[] array,String split){
        if (array.length==0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i>0){
                sb.append(split);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

}
