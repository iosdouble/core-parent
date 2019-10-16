package org.nh.core.util.date;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

/**
 * @Classname DateUtils
 * @Description TODO
 * @Date 2019/8/1 4:56 PM
 * @Created by nihui
 */
public class DateTimeUtil {
    /**
     * 支持方案
     */
    public static final String DATE_FORMAT="yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 拓展方案
     */
    public static final String FORMAT_DATETIME_NORMAL="yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_NORMAL="yyyy-MM-dd";
    public static final String FORMAT_DATE_CHINA="yyyy年MM月dd日";
    public static final String FORMAT_DATE_NUMBER="yyyy/MM/dd";
    public static final String FORMAT_DATE_SMALL="yyyyMMdd";

    /**
     * 格式化当前时间 yyyy-MM-dd
     * @return 返回指定方式格式化后的数据
     */
    public static String getCurrentDate(){
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateTimeUtil.DATE_FORMAT);
        datestr = df.format(new Date());
        return datestr;
    }

    /**
     * 按照自定义格式格式化日期
     * @param Dateformat 传入的自定义日期格式。
     * @return 格式化日期数据
     */
    public static String getCurrentDateTime(String Dateformat){
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(Dateformat);
        datestr = df.format(new Date());
        return datestr;
    }

    /**
     * 时间转换日期格式 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return 返回转换后的日期
     */
    public static String dateToDateTime(Date date){
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateTimeUtil.DATE_TIME_FORMAT);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 日期格式转时间
     * @param datestr
     * @return
     */
    public static Date stringToDate(String datestr){
        if (datestr==null|| datestr.equals("")){
            return null;
        }
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(DateTimeUtil.DATE_FORMAT);
        try{
            date = df.parse(datestr);
        } catch (ParseException e) {
            date = DateTimeUtil.stringToDate(datestr,"yyyyMMdd");
        }
        return date;
    }

    /**
     * 按照指定格式转换
     * @param datestr
     * @param dateformat
     * @return
     */
    public static Date stringToDate(String datestr,String dateformat){
        Date date =new Date();
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        try{
            date = df.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间转日期格式
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(DateTimeUtil.DATE_FORMAT);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 按照指定格式转换
     * @param date
     * @param dateformat
     * @return
     */
    public static String dateToString(Date date,String dateformat){
        String datestr = null;
        SimpleDateFormat df = new SimpleDateFormat(dateformat);
        datestr = df.format(date);
        return datestr;
    }

    /**
     * 获取天
     * @param date
     * @return
     */
    public static int getDayOfDate(Date date){
        int d = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        d = cd.get(Calendar.DAY_OF_MONTH);
        return d;
    }

    /**
     * 获取月
     * @param date
     * @return
     */

    public static int getMonthOfDate(Date date){
        int m = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        m = cd.get(Calendar.MONTH)+1;
        return m;
    }

    /**
     * 获取年
     * @param date
     * @return
     */

    public static int getYearOfDate(Date date){
        int y = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        y = cd.get(Calendar.YEAR);
        return y;
    }

    /**
     * 获取周
     * @param date
     * @return
     */
    public static int getWeekOfDate(Date date){
        int wd = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        wd = cd.get(Calendar.DAY_OF_WEEK)-1;
        return wd;
    }

    /**
     * 获取一月第一天
     * @param date
     * @return
     */
    public static Date getFristDayOfMonth(Date date){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.set(Calendar.DAY_OF_MONTH,1);
        return cd.getTime();
    }

    /**
     * 获取最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date){
        return DateTimeUtil.addDay(DateTimeUtil.getFristDayOfMonth(DateTimeUtil.addMonth(date,1)),-1);
    }

    /**
     * 是否是闰年
     * @param date
     * @return
     */
    public static boolean isLeapYEAR(Date date){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int year = cd.get(Calendar.YEAR);
        if (year % 4 == 0&&year%100!=0||year%400==0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 设置年月日时间
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDateByYMD(int year,int month,int day){
        Calendar cd = Calendar.getInstance();
        cd.set(year,month-1,day);
        return cd.getTime();
    }

    /**
     * 年循环
     * @param date
     * @param iyear
     * @return
     */
    public static Date getYearCycleOfDate(Date date,int iyear){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(Calendar.YEAR,iyear);
        return cd.getTime();
    }

    /**
     * 月循环
     * @param date
     * @param i
     * @return
     */
    public static Date getMonthCycleOfDate(Date date,int i){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(Calendar.MONTH,i);
        return cd.getTime();
    }

    /**
     * 获取指定年差
     * @param fromDate
     * @param toDate
     * @return
     */
    public static int getYearByMinusDate(Date fromDate,Date toDate){
        Calendar df = Calendar.getInstance();
        df.setTime(fromDate);
        Calendar dt = Calendar.getInstance();
        dt.setTime(toDate);
        return dt.get(Calendar.YEAR)-df.get(Calendar.YEAR);
    }

    /**
     * 获取指定月差
     * @param fromDate
     * @param toDate
     * @return
     */
    public static int getMonthByMinusDate(Date fromDate,Date toDate){
        Calendar df = Calendar.getInstance();
        df.setTime(fromDate);
        Calendar dt = Calendar.getInstance();
        dt.setTime(toDate);
        return dt.get(Calendar.YEAR)*12+dt.get(Calendar.MONTH)-(df.get(Calendar.YEAR)*12+df.get(Calendar.MONTH));
    }

    /**
     * 获取日期差
     * @param fromDate
     * @param toDate
     * @return
     */
    public static long getDayByMinusDate(Object fromDate,Object toDate){
        Date f = DateTimeUtil.chgObject(fromDate);
        Date t = DateTimeUtil.chgObject(toDate);

        long fd = f.getTime();
        long td = t.getTime();

        return (td-fd)/(24L*60L*60L*1000L);
    }

    /**
     * 统计时间天数
     * @param birthday
     * @param calcDate
     * @return
     */
    public static int calcAge(Date birthday,Date calcDate){
        int cYear = DateTimeUtil.getYearOfDate(calcDate);
        int cMonth = DateTimeUtil.getMonthOfDate(calcDate);
        int cDay = DateTimeUtil.getDayOfDate(calcDate);
        int bYear = DateTimeUtil.getYearOfDate(birthday);
        int bMonth = DateTimeUtil.getMonthOfDate(birthday);
        int bDay = DateTimeUtil.getDayOfDate(birthday);

        if (cMonth>bMonth||(cMonth==bMonth&&cDay>bDay)){
            return cYear-bYear;
        }else {
            return cYear-1-bYear;
        }
    }

    /**
     * 从身份证号中统计年龄时间
     * @param idno
     * @return
     */
    public static String getBirthDayFromIDCard(String idno){
        Calendar cd = Calendar.getInstance();
        if (idno.length()==15){
            cd.set(Calendar.YEAR,Integer.valueOf("19"+idno.substring(6,8)).intValue());
            cd.set(Calendar.MONTH,Integer.valueOf(idno.substring(8,10)).intValue()-1);
            cd.set(Calendar.DAY_OF_MONTH,Integer.valueOf(idno.substring(10,12)).intValue());
        }else if (idno.length()==18){
            cd.set(Calendar.YEAR,Integer.valueOf(idno.substring(6,10)).intValue());
            cd.set(Calendar.MONTH,Integer.valueOf(idno.substring(10,12)).intValue()-1);
            cd.set(Calendar.DAY_OF_MONTH,Integer.valueOf(idno.substring(12,14)).intValue());
        }
        return DateTimeUtil.dateToDateTime(cd.getTime());
    }

    /**
     * 某天起增加几天
     * @param date
     * @param iday
     * @return
     */
    public static Date addDay(Date date,int iday){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(Calendar.DAY_OF_MONTH,iday);
        return cd.getTime();
    }

    /**
     * 某月增加几月
     * @param date
     * @param imonth
     * @return
     */
    public static Date addMonth(Date date,int imonth){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(Calendar.MONTH,imonth);
        return cd.getTime();
    }

    /**
     * 某年增加几年
     * @param date
     * @param iyear
     * @return
     */
    public static Date addYear(Date date,int iyear){
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        cd.add(Calendar.YEAR,iyear);
        return cd.getTime();
    }

    /**
     * 日期对象转换
     * @param date
     * @return
     */
    public static Date chgObject(Object date){
        if (date!=null&&date instanceof Date){
            return (Date) date;
        }
        if (date!=null&& date instanceof String){
            return DateTimeUtil.stringToDate((String)date);
        }
        return null;
    }

    /**
     * 通过日期算年龄
     * @param date
     * @return
     */
    public static long getAgeByBirthday(String date){
        Date birthday = stringToDate(date,"yyyy-MM-dd");
        long sce = new Date().getTime()-birthday.getTime();
        long age = sce/(1000*60*60*24)/365;
        return age;
    }

    /**
     * 获取当前系统时间
     * @return
     */
    public static Date getCurrentTime(){
        return new Date(System.currentTimeMillis());
    }




    public static Date getNowDateTime4BeiJing(){
        Clock clock=Clock.system(ZoneId.of("+08"));
        Instant instant=clock.instant();
        Date date=Date.from(instant);
        return date;
//		ZonedDateTime.now(clock);
//		ZonedDateTime zonedDateTime=ZonedDateTime.of(LocalDateTime.of(LocalDate.now(clock), LocalTime.now(clock)), null);
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		String dateTimeStr=zonedDateTime.format(formatter);
    }



    public static Date getNowDateTime4UTC(){
        Clock clock=Clock.system(ZoneOffset.UTC);
        Instant instant=clock.instant();
        Date date=Date.from(instant);
        return date;
    }


    public static String getFormatDateTime(Date date,String dateFormat){
        if(date==null){
            return null;
        }
        if(StringUtils.isBlank(dateFormat)){
            dateFormat=FORMAT_DATETIME_NORMAL;
        }
        SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
        String dateStr=sdf.format(date);
        return dateStr;
    }


    public static Date getFormatDateTime(String dateStr,String sourceDateFormat){
        SimpleDateFormat sdf=new SimpleDateFormat(sourceDateFormat);
        Date date=null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getMonthLastDay(String year, String month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, Integer.parseInt(year));
        ca.set(Calendar.MONTH, Integer.parseInt(month)-1);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(ca.getTime());
    }

    public static long changeToTimeMillis(String date) throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = format.parse(date);

        return dateTime.getTime();
    }

}

