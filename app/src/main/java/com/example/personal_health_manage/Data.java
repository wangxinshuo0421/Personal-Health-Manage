package com.example.personal_health_manage;

public class Data {
    private static String ip = "115.29.232.65";
    private static int port = 8888;
    private static String userId,height,weight,highBloodPressure,lowBloodPressure,bloodSugar;
    private static int getUpHour,fallAsleepHour,getUpMinute,fallAsleepMinute,sleepFragmentFlag=0,
            getUpClickTimes,fallAsleepClickTimes;
    private static int oneHourStepCount,oneDayStepCount;

    /*网络接口*/
    public static String getServerIp(){
        return ip;
    }
    public static int getServerPort() {
        return port;
    }


    /* MyFragment 数据 */
    public static String getUserId(){return userId;}
    public static String getHeight(){return height;}
    public static String getWeight(){return weight;}
    public static String getHighBloodPressure(){return highBloodPressure;}
    public static String getLowBloodPressure(){return lowBloodPressure;}
    public static String getBloodSugar(){return bloodSugar;}

    public static void setUserId(String ID){Data.userId = ID;}
    public static void setHeight(String h){Data.height = h;}
    public static void setWeight(String w){Data.weight = w;}
    public static void setHighBloodPressure(String HP){Data.highBloodPressure = HP;}
    public static void setLowBloodPressure(String LP){Data.lowBloodPressure = LP;}
    public static void setBloodSugar(String BS){Data.bloodSugar = BS;}

    /*  sleepFragment 数据*/
    public static int getSleepFragmentFlag(){return sleepFragmentFlag;}
    public static int getGetUpHour(){return getUpHour;}
    public static int getGetUpMinute(){return getUpMinute;}
    public static int getFallAsleepHour(){return fallAsleepHour;}
    public static int getFallAsleepMinute(){return fallAsleepMinute;}

    public static void setSleepFragmentFlag(int x){Data.sleepFragmentFlag = x;}
    public static void setGetUpHour(int x){Data.getUpHour = x;}
    public static void setGetUpMinute(int x){Data.getUpMinute = x;}
    public static void setFallAsleepHour(int x){Data.fallAsleepHour = x;}
    public static void setFallAsleepMinute(int x){Data.fallAsleepMinute = x;}

    /* stepCountFragment 数据*/
    public static int getOneDayStepCount(){return oneDayStepCount;}
    public static int getOneHourStepCount(){return oneHourStepCount;}

    public static void setOneDayStepCount(int x){Data.oneDayStepCount = x;}
    public static void setOneHourStepCount(int x){Data.oneHourStepCount = x;}
}
