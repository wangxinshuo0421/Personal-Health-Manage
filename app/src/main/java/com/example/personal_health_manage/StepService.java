package com.example.personal_health_manage;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.List;


public class StepService extends Service implements SensorEventListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SensorManager sensorManager;
    private static int saveDuration = 3000;  //3秒进行一次存储
    private int mStepDetector = 0, mStepCounter = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                addCountStepListener();
            }
        }).start();
        startTimeCount();

    }

    /**
     * 开始倒计时，
     */
    private void startTimeCount() {
        new CountDownTimer(saveDuration,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                saveStepData();
                cancel();
                startTimeCount();
            }
        }.start();
    }

    /*保存一次数据*/
    private void saveStepData(){

        int pushIndex = 0,oneHourStepCountSum,pushStep = 0;
        int oneDayStepCountSum = 0,needBeSubtractStep = 0;
        String pushStr = pref.getString("pushIndex","-1");//读取输入指针
        String oneHourSumStr = pref.getString("oneHourSum","0");  //读取过去一小时步数和

        if(pushStr.equals("-1")) { //第一次读取写入，未存过指针string
            pushIndex=1;
            editor.putString(String.valueOf(pushIndex),String.valueOf(mStepDetector));//保存当前时刻的步数
            mStepDetector = 0;
            editor.putString("pushIndex",String.valueOf(++pushIndex));
        }else {
            pushIndex = strToInt(pushStr);
            if(pushIndex>=301){
                pushIndex = 1;
            }
            pushStep = strToInt(pref.getString(String.valueOf(pushIndex),"0"));
            oneHourStepCountSum = strToInt(oneHourSumStr)-pushStep+mStepDetector;
            editor.putString("oneHourSum",String.valueOf(oneHourStepCountSum));
            editor.putString(String.valueOf(pushIndex),String.valueOf(mStepDetector));
            editor.putString("pushIndex",String.valueOf(++pushIndex));
            mStepDetector=0;
            Data.setOneHourStepCount(oneHourStepCountSum);
            System.out.println("save data one hour step count");
            System.out.println(Data.getOneHourStepCount());
        }
        /*判断日期的更新*/
       // if(isNewDay()){
       //     needBeSubtractStep = mStepCounter;
        needBeSubtractStep = 0;
       // }else {  //不是新的一天
        oneDayStepCountSum = mStepCounter-needBeSubtractStep;
        Data.setOneDayStepCount(oneDayStepCountSum);
       // }
   //     editor.putString("stepCountSum",String.valueOf(mStepCounter));
        editor.apply();
    }
    /*
    * 判断是否是新的一天，，新的一天到来则刷新步数
    * 返回1：新的一天
    * 返回0：还是当天
    * */
    private boolean isNewDay(){
        int lastDay;  //之前的日期
        int nowDay;   //现在读取到的日期
        Calendar calendar = Calendar.getInstance();

        lastDay = strToInt(pref.getString("lastDay","100"));
        nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        if(lastDay == 100){  //未进行过数据存储
            editor.putString("lastDay",String.valueOf(nowDay));
            editor.apply();
            return true;
        }else {
            if(lastDay==nowDay){
                return false;
            }else {
                editor.putString("lastDay",String.valueOf(nowDay));
                editor.apply();
                return true;
            }
        }
    }

    /**
     * 添加传感器监听
     */
    private void addCountStepListener() {
        int suitable = 0;
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor:sensorList){
            if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
                suitable += 1;
            }else if(sensor.getType() == Sensor.TYPE_STEP_COUNTER){
                suitable += 10;
            }
        }
        if(suitable/10>0&&suitable%10>0){
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                    SensorManager.SENSOR_DELAY_NORMAL);
            System.out.println("Can Step Count");
        }else {
            /*不支持计步*/
            System.out.println("Can't Step Count");
            Toast.makeText(this,"计步器传感器不可用",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println("Change sensor");
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {  //步行检测
                mStepDetector++;
                System.out.println("detector");
            }
        }else if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            mStepCounter = (int)event.values[0];
            System.out.println("Step Counter");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private int strToInt(String str){
        if (str == null|| str.trim().equals("")){
            return 0;
        }
        char[] chars = str.trim().toCharArray();
        int result =0;
        for (int i= 0;i<chars.length;i++){

            if (chars[i]>'9'||chars[i]<'0'){
                return 0;
            }
            result = result *10 +(int)(chars[i]-'0');
        }
        return result;
    }
}
