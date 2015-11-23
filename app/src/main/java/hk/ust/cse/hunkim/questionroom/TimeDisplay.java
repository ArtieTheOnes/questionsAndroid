package hk.ust.cse.hunkim.questionroom;

import java.util.Date;

/**
 * Created by Yuxuan on 10/30/2015.
 */
public class TimeDisplay {
    public static String fromTimestamp(long inputTime){
        String outputTime;
        long currentTime = new Date().getTime();
        long difference = currentTime - inputTime;
        difference = difference/1000;
        if (difference < 60*60*24){
            if (difference < 60){
                if (difference < 2) outputTime = "now";
                else outputTime = difference + "s";
            }
            else if (difference < 60*60){
                outputTime = ((int)(difference/60)) + " min" + (difference/60 > 1 ? "s": "");
            }
            else{
                outputTime = ((int)(difference/60/60)) + " hr" + (difference/60/60 > 1 ? "s": "") + "";
            }
        }
        else{
            outputTime = ((int)(difference/60/60/24)) + " day" + (difference/60/60/24 > 1 ? "s": "") + "";
        }
        return outputTime;
    }

    public static long toTimestamp(String time) {
        long currentTime= System.currentTimeMillis();
        long returnTime=0;
        if (time.contains("Now")){
            returnTime=currentTime;
        } else if (time.contains("1 hour ago")){
            returnTime=currentTime-3600*1000;
        } else if (time.contains("2 hours ago")){
            returnTime=currentTime-2*3600*1000;
        } else if (time.contains("1 day ago")){
            returnTime=currentTime-24*3600*1000;
        } else if (time.contains("1 week ago")){
            returnTime=currentTime-7*24*3600*1000;
        } else if (time.contains("30 days ago")){
            returnTime=currentTime- 2592000000L;
        } else if (time.contains("365 days ago")){
            returnTime=currentTime-31536000000L;
        } else if (time.contains("The Start")){
            returnTime=0L;
        }
        return returnTime;
    }
}
