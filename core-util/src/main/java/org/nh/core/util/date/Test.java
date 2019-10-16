package org.nh.core.util.date;

import java.util.Date;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2019/10/15 7:42 PM
 * @Created by nihui
 */
public class Test {

    public static void main(String[] args) {
        String currentDate = DateTimeUtil.dateToDateTime(new Date());


        //2019-10-16 15:28:13

        Date date = DateTimeUtil.stringToDate("2019-10-16 15:28:13");
        System.out.println(date);
        System.out.println(currentDate);
    }
}
