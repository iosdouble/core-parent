package org.nh.core.util.string;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * @Classname StringCreateUtil
 * @Description TODO 字符串创造工具
 * @Date 2020/1/15 2:57 PM
 * @Created by nihui
 */
public class StringCreateUtil {

    /**
     * 在字符集合中随机选出多个字符（可能会重复选出）
     * @param seedStrs 种子字符集合
     * @param length 选出的字符数量
     * @return
     */
    public static String getRandomString(String seedStrs,int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(seedStrs.length());
            sb.append(seedStrs.charAt(number));
        }
        return sb.toString();
    }

    /**
     *
     * 获取固定长度的随机字符串，随机字段串包括所有的小写字母和数字
     * @param length 字符数量
     * @return
     */
    public static String getRandomString(int length) {
        String strSet = "abcdefghijklmnopqrstuvwxyz0123456789";
        String result=getRandomString(strSet, length);
        return result;
    }

    /**
     * 获取固定长度的随机字符串，随机字段串包括所有的数字
     * @param length 字符数量
     * @return
     */
    public static String getRandomNumString(int length) {
        String seedStrs = "0123456789";
        String result=getRandomString(seedStrs, length);
        return result;
    }

    /**
     *
     * 获取带有前缀和后缀的字符串（中间部分随机生成）
     * @param prefix 前缀
     * @param middleLength 中间生成随机字符串的位数（包含小写字母和数字）
     * @param suffix 后缀
     * @return
     */
    public static String getRandomString(String prefix,int middleLength,String suffix){
        StringBuffer sb=new StringBuffer();
        if(prefix==null){
            prefix="";
        }
        if(suffix==null){
            suffix="";
        }
        sb.append(prefix).append(getRandomString(middleLength)).append(suffix);
        return sb.toString();
    }

    /**
     *
     * 将一个字符串转化为定长的字符串
     *
     * @param str	原始字符串
     * @param prefixChar	用于填补字符串前缀不够长度的部分，传递一个字符的字符串
     * 如果传递了长度大于一的prefixChar，那么会用prefixChar的第一个字符进行填充
     * @param length	生成字符串的总长度，如果总长度小于原始字符串的长度，则会返回原始字符串
     * @return
     */
    /**
     *
     * 使用固定字符补齐原始字符串，使其达到指定的长度
     * 例如：qaz使用字符a补齐到5位，结果为qazaa
     * 如果原始字符串本身就大于指定的长度，则直接返回原始字符串，不再进行补齐，也不会删除多于部分
     * 例如：qaz使用字符a补齐到2为，结果为qaz
     * @param str 原始字符串
     * @param prefixChar 用来补齐的字符（必须是单字符，如果给了多个字符，则会使用第一个字符进行补齐）
     * @param length 需要字符串最少达到的长度
     * @return
     */
    public static String getFixedLengthStr(String str,String prefixChar,int length){
        if(StringUtils.isBlank(prefixChar)){
            return str;
        }
        if(str.length()>=length){
            return str;
        }
        if(prefixChar.length()>1){
            prefixChar=prefixChar.substring(0, 1);
        }
        int prefixCharLength=length-str.length();
        StringBuffer resultStr=new StringBuffer();
        for (int i = 0; i < prefixCharLength; i++) {
            resultStr.append(prefixChar);
        }
        return resultStr.append(str).toString();
    }

    /**
     *
     * 使用固定字符补齐原始字符串，使其达到指定的长度
     * 例如：qaz使用字符字母数字组合随机的字符串补齐到5位，结果为qaza3
     * 如果原始字符串本身就大于指定的长度，则直接返回原始字符串，不再进行补齐，也不会删除多于部分
     * 例如：qaz使用字符a补齐到2为，结果为qaz
     * @param str 原始字符串
     * @param length 需要字符串最少达到的长度
     * @return
     */
    public static String getFixedLengthStrForAbsAndNum(String str,int length){
        if(str.length()>=length){
            return str;
        }
        int prefixCharLength=length-str.length();
        String prefixStr = getRandomString(prefixCharLength);

        StringBuffer resultStr=new StringBuffer();
        resultStr.append(str).append(prefixStr);

        return resultStr.toString();
    }

    /**
     *
     * 使用固定字符补齐原始字符串，使其达到指定的长度
     * 例如：qaz使用字符数字组合随机的字符串补齐到5位，结果为qaz36
     * 如果原始字符串本身就大于指定的长度，则直接返回原始字符串，不再进行补齐，也不会删除多于部分
     * 例如：qaz使用字符a补齐到2为，结果为qaz
     * @param str 原始字符串
     * @param length 需要字符串最少达到的长度
     * @return
     */
    public static String getFixedLengthStrForNum(String str,int length){
        if(str.length()>=length){
            return str;
        }
        int prefixCharLength=length-str.length();
        String prefixStr = getRandomNumString(prefixCharLength);

        StringBuffer resultStr=new StringBuffer();
        resultStr.append(str).append(prefixStr);

        return resultStr.toString();
    }
}
