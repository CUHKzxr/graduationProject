package com.example.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static final SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    /**
     * 检查完整长度的时间戳格式，并返回解析出的日期值，不会检查时间戳内容（如闰年）
     *
     * @param timestamp 时间戳
     * @param level 时间戳精度，0：年 1：月 2：日 3：小时 4：分钟 5：秒
     * @return true如果格式正确，false如果格式错误
     */
    public static boolean checkTimestamp(String timestamp, int level){
        if(level<=0 || level>5){
            throw new IllegalArgumentException("utils.TimeUtils.getTimestampValue expected level between 0-5,get"+level);
        }
        if(timestamp.length()!=4+level*3){
            return false;
        }
        try {
            String year = null,month = null,day = null,hour = null,minute = null,second = null;
            switch (level){
                case 5:second=timestamp.substring(17,19);
                case 4:minute=timestamp.substring(14,16);
                case 3:hour=timestamp.substring(11,13);
                case 2:day=timestamp.substring(8,10);
                case 1:month=timestamp.substring(5,7);
                case 0:year =timestamp.substring(0,4);
                    break;
            }

            switch (level){
                case 5:Integer.parseInt(second);
                case 4:Integer.parseInt(minute);
                case 3:Integer.parseInt(hour);
                case 2:Integer.parseInt(day);
                case 1:Integer.parseInt(month);
                case 0:Integer.parseInt(year);
                    break;
            }
            return true;
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 检查的时间戳格式，并返回解析出的日期值，不会检查时间戳内容（如闰年）
     *
     * @param timestamp 时间戳
     * @param level 时间戳精度，0：年 1：月 2：日 3：小时 4：分钟 5：秒
     * @return int[6] 0:年 1：月 2：日 3：小时 4：分钟 5：秒 ，null如果格式出错
     */
    public static int[] getTimestampValue(String timestamp,int level){
        if(level<=0 || level>5){
            throw new IllegalArgumentException("utils.TimeUtils.getTimestampValue expected level between 0-5,get"+level);
        }
        if(timestamp.length()!=4+level*3){
            return null;
        }
        try {
            String year = null,month = null,day = null,hour = null,minute = null,second = null;
            switch (level){
                case 5:second=timestamp.substring(17,19);
                case 4:minute=timestamp.substring(14,16);
                case 3:hour=timestamp.substring(11,13);
                case 2:day=timestamp.substring(8,10);
                case 1:month=timestamp.substring(5,7);
                case 0:year =timestamp.substring(0,4);
                break;
            }
            int[] r=new int[level-1];
            switch (level){
                case 0:r[0]=Integer.parseInt(year);
                case 1:r[1]=Integer.parseInt(month);
                case 2:r[2]=Integer.parseInt(day);
                case 3:r[3]=Integer.parseInt(hour);
                case 4: r[4]=Integer.parseInt(minute);
                case 5:r[5]=Integer.parseInt(second);
                break;
            }
            return r;
        }catch (NumberFormatException e){
            return null;
        }

    }

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

}
