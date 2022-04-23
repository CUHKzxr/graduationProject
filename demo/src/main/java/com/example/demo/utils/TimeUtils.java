package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static final SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");


    public static String changeTimestamp2(String time,int num) throws ParseException {
        Date date=dateFormat.parse(time);
        Date rDate=new Date(date.getTime()+num);
        return dateFormat.format(rDate);
    }
    private static String changeTimestamp(String time,int num) {
        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(5, 7));
        int day = Integer.parseInt(time.substring(8, 10));
        int hour = Integer.parseInt(time.substring(11, 13));
        int minute = Integer.parseInt(time.substring(14, 16));
        int second = Integer.parseInt(time.substring(17, 19));
        String r="";
        second += num;
        minute += Math.floor(second / 60);
        second =second % 60;
        second = second>=0 ? second :second+60;
        hour += Math.floor(minute / 60);
        minute = minute % 60;
        minute = minute >=0 ? minute:minute+60;
        day +=Math.floor(hour/24);
        hour = hour % 24;
        hour = hour>=0? hour:hour+24;
        day=day-1;
        month+=Math.floor(day/30);
        day =day % 30;
        day = day>=0 ? day+1:day+31;
        r= r+year+"-";
        if(month >= 10){
            r=r+ month;
        }else{
            r+= "0"+month;
        }
        r+="-";
        if(day>=10){
            r+=""+day;
        }else{
            r+="0"+day;
        }
        r+="T";
        if(hour>=10){
            r+=""+hour;
        }else{
            r+="0"+hour;
        }
        r+=":";
        if(minute>=10){
            r+=""+minute;
        }else{
            r+="0"+minute;
        }
        r+=":";
        if(second>=10){
            r+=""+second;
        }else{
            r+="0"+second;
        }
        r+="Z";
        return r;
    }

    public static Date parseDate(String str) throws ParseException {
        SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
        return formater.parse(str);

    }

}
