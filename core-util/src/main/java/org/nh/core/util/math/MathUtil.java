package org.nh.core.util.math;

/**
 * @Classname MathUtil
 * @Description TODO
 * @Date 2020/1/15 2:51 PM
 * @Created by nihui
 */
public class MathUtil {

    /**
     * 高斯函数处理
     * @param a
     * @param b
     * @return
     */
    public static int getBigInt(int a,int b){
        int c=a/b;//1
        float d=a/Float.parseFloat(String.valueOf(b));//1.3333334
        if(c==d){
            return c;
        }else{
            return c+1;
        }
    }

    /**
     *
     * @param a
     * @return
     */
    public static int getBigHalfInt(int a) {
        //向上取整
        double tmp = a/2.0;
        double tmp1 = Math.ceil(tmp);
        if(tmp1==0.0) {
            return 0;
        }
        double tmp2 = tmp1-1;
        int result = (int) tmp2;
        return result;
    }

    /**
     * 四舍五入保留两位小数
     * @param number
     * @return
     */
    public static String get2Float45(String number){
        //NumberFormat nf = NumberFormat.getNumberInstance();
        //nf.setMaximumFractionDigits(2);
        //return nf.format(Double.parseDouble(number));

        //DecimalFormat df = new DecimalFormat("#.00");

        return String.format("%.2f", Double.parseDouble(number));
    }

}
