package org.nh.core.util.math;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @class: NumberUtil
 * @description: TODO 操作数据
 * @date 2020/1/7 4:15 PM
**/
public class NumberUtil {

    /**
     * num1/num2计算百分比
     * @param num1
     * @param num2
     * @return
     */
    public static String numFormat(int num1,int num2){
        if(num2 == 0){
            return "-";
        }
        // 创建一个数值格式化对象

        NumberFormat numberFormat = NumberFormat.getInstance();

        // 设置精确到小数点后2位

        numberFormat.setMaximumFractionDigits(2);

        String result = numberFormat.format((float) num1 / (float) num2 * 100);

        // System.out.println("num1和num2的百分比为:" + result + "%");
        return result+"%";
    }

    /**
     * num1/num2计算百分比
     * @param num1
     * @param num2
     * @return
     */
    public static String numFormat(double num1,double num2){
        // 创建一个数值格式化对象

        NumberFormat numberFormat = NumberFormat.getInstance();

        // 设置精确到小数点后2位

        //numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        String result = numberFormat.format((float) num1 / (float) num2 * 100);

        // System.out.println("num1和num2的百分比为:" + result + "%");
        return result+"%";
    }

    /**
     * 计算环比专用
     * num1-num2/num2参数都是百分数，结果也是百分数
     * @param num1
     * @param num2
     * @return
     * @throws ParseException
     */
    public static String numFormatString(String num1,String num2) {
        try {
            NumberFormat nf=NumberFormat.getPercentInstance();
            Float number1 = null;

            number1 = nf.parse(num1).floatValue();

            Float number2 = nf.parse(num2).floatValue();
            Float num3 = number1- number2;

            BigDecimal bigDecimal = new BigDecimal(num3/number2);
            bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            double f1 = bigDecimal.doubleValue();

            nf.setMaximumFractionDigits(2);
            return nf.format(f1);
            //System.out.println("num1和num2的百分比为:" + result + "%");
            // return result+"%";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
