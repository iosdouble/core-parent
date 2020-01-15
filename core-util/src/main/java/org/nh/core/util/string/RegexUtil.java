package org.nh.core.util.string;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname RegexUtil
 * @Description TODO 正则表达式工具
 * @Date 2020/1/15 2:54 PM
 * @Created by nihui
 */
public class RegexUtil {

    /**
     * email
     */
    private static final String REGEX_EMAIL="\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    /**
     * 手机号
     */
    private static final String REGEX_DOMESTIC_MOBILE="0?(13|14|15|17|18)[0-9]{9}";

    /**
     * 短信验证码
     */
    private static final String REGEX_SMS_CODE="[0-9]{4,6}";

    /**
     * 图形验证码
     */
    private static final String REGEX_VALIDATE_IMAGE_CODE="[0-9a-zA-Z]{4,8}";
    /**
     * 密码（6到30位，字母数字字符至少两种的组合）
     */
//	private static final  String REGEX_PASSWORD="(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,30}";
    private static final  String REGEX_PASSWORD="[^\\s]{6,20}";

    /**
     * 用于存储数据的KEY
     */
    private static final  String REGEX_DB_KEY="[0-9a-zA-Z\\-]{1,50}";


    /**
     * 通用方法验证
     * @param regex
     * @param str
     * @return
     */
    public static boolean regex(String regex ,String str){
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 获取字符串中符合正则表达式的第一个子字符串
     * @param str
     * @param regex
     * @return
     */
    public static String getRegexFirstPartStr(String str,String regex){
        return getRegexPartStr(str, regex, 0);
    }

    /**
     * 获取字符串中符合正则表达式中的第N个子字符串
     * @param str
     * @param regex
     * @param index
     * @return
     */
    public static String getRegexPartStr(String str,String regex,int index){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        for (int i = 0; i < index+1; i++) {
            if(matcher.find() && i==index){
                return matcher.group(0);
            }
        }
        return null;
    }

    /**
     * 获取字符串中符合正则表达式的子字符串的数量
     * @param str
     * @param regex
     * @return
     */
    public static int getRegexPartCount(String str,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        int result = 0;
        while (matcher.find()) {
            result++;
        }
        return result;
    }

    public static boolean isEmail(String email){
        if(StringUtils.isBlank(email)){
            return false;
        }
        return regex(REGEX_EMAIL, email);
    }


    public static boolean isDomesticMobile(String mobile){
        if(StringUtils.isBlank(mobile)){
            return false;
        }
        return regex(REGEX_DOMESTIC_MOBILE, mobile);
    }


    public static boolean isSmsCode(String smsCode){
        if(StringUtils.isBlank(smsCode)){
            return false;
        }
        return regex(REGEX_SMS_CODE, smsCode);
    }

    public static boolean isValidateImageCode(String imageCode){
        if(StringUtils.isBlank(imageCode)){
            return false;
        }
        return regex(REGEX_VALIDATE_IMAGE_CODE, imageCode);
    }


    public static boolean isPassword(String password){
        if(StringUtils.isBlank(password)){
            return false;
        }
        return regex(REGEX_PASSWORD, password);
    }


    /**
     * 判断是否是存储redis等db的key
     * @param dbKey
     * @return
     */
    public static boolean isDBKey(String dbKey){
        if(StringUtils.isBlank(dbKey)){
            return false;
        }
        return regex(REGEX_DB_KEY, dbKey);
    }
}
