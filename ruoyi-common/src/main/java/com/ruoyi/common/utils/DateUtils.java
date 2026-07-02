package com.ruoyi.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 * 
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate 最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取下一天Data对象，时间从00:00:00算起
     * @author sweet
     */
    public static Date getNextDay(Date date)
    {
       Date originalDate = new Date(date.getTime());

       //使用Calendar进行日历操作
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);
        calendar.add(Calendar.DAY_OF_MONTH,1);//将日历加一天

        //将时间设置为00:00:00
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Date nextDayDate = calendar.getTime();
        return nextDayDate;

    }

    /**
     * 获取上一天Data对象，时间从00:00:00算起
     * @author sweet
     */
    public static Date getBeforeDay(Date date)
    {
        Date originalDate = new Date(date.getTime());

        //使用Calendar进行日历操作
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);
        calendar.add(Calendar.DAY_OF_MONTH,-1);//将日历加一天

        //将时间设置为00:00:00
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Date beforeDayDate = calendar.getTime();
        return beforeDayDate;
    }
    /**
     * 获取当天Data对象的00:00:00点
     * @author sweet
     */
    public static Date getNowDateAndStartOfZero()
    {
        Date originalDate = new Date();

        //使用Calendar进行日历操作
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);
        //将时间设置为00:00:00
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Date nowDate = calendar.getTime();
        return nowDate;
    }

    /**
     * 获取传入Data对象，时间从00:00:00算起
     * @author sweet
     */
    public static Date getToday(Date date)
    {
        Date originalDate = new Date(date.getTime());

        //使用Calendar进行日历操作
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalDate);

        //将时间设置为00:00:00
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Date today = calendar.getTime();
        return today;
    }

    /**
     * 获取每周周一和周日的日期
     * @return
     */
    public static List<Date> getMonDayAndSunDay(){
        // 获取当前日期
        LocalDate today = LocalDate.now();

        // 计算本周的周一和周日的日期
        LocalDate monday = today;
        LocalDate sunday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        //要相隔一周的日期，所以得到星期一
        while (sunday.getDayOfWeek() != DayOfWeek.MONDAY) {
            sunday = sunday.plusDays(1);
        }

        // 转换为java.util.Date类型
        Date mondayDate = Date.from(monday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date sundayDate = Date.from(sunday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Date> dateList = new ArrayList<>();
        dateList.add(mondayDate);
        dateList.add(sundayDate);
        return dateList;
    }
    /**
     * 判断时间段是否有冲突
     * @author sweet
     */
    public static boolean isConflict(Date startTime1, Date endTime1, Date startTime2, Date endTime2) {
        // 判断时间段是否有冲突
         if (startTime1.before(endTime2) && endTime1.after(startTime2)) {
          return true;
         }
         // 没有冲突
         return false;
    }
}
