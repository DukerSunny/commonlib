package com.github.commonlib.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

    /**
     * 计算当前时间和给定时间的差值
     * @param time2 样式:yyyy-mm-dd hh-mm-ss
     * @param zone2 样式:+8
     * @return
     */
    public static long getDatesOffsetWithTimeZone(String time2, String zone2){
        //将YYYY-MM-DD hh:mm:ss格式转为Date类型
        Date date1 = new Date(System.currentTimeMillis());
        Date date2 = new Date();
        if(time2!=null && !time2.equals("") ){
            date2 = toDate(time2);
        }

        //获得时区
        String gmt2 = "";
        if(zone2!=null && !zone2.equals("")){
            //拼接成GMT格式
            gmt2 = "GMT"+zone2+":00";
        }

        TimeZone timeZone1 = TimeZone.getDefault();
        TimeZone timeZone2 = TimeZone.getTimeZone(gmt2);
        //计算相差的毫秒数
        long timeZoneOffset = timeZone2.getOffset(date2.getTime()) - timeZone1.getOffset(date1.getTime());
        long millisOffset = date2.getTime() - date1.getTime();
        long ms = millisOffset - timeZoneOffset;

        return ms/1000;
    }

    /**
     * 将输入格式为2004-8-13 12:31:22类型的字符串转换为标准的Date类型
     * @param dateStr
     * @return
     */
    public static synchronized Date toDate(String dateStr){
        String[] list0 = dateStr.split(" ");
        String date = list0[0];
        String time = list0[1];
        String[] list1 = date.split("-");
        int year = new Integer(list1[0]).intValue();
        int month = new Integer(list1[1]).intValue();
        int day = new Integer(list1[2]).intValue();
        String[] list2 = time.split(":");
        int hour = new Integer(list2[0]).intValue();
        int min = new Integer(list2[1]).intValue();
        int second = new Integer(list2[2]).intValue();
        Calendar cale =  Calendar.getInstance();
        cale.set(year,month-1,day,hour,min,second);
        return cale.getTime();
    }
}
