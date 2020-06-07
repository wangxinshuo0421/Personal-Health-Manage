package com.example.personal_health_manage;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Calendar;

public class SleepFragment extends Fragment {
    private Button getUpTimeButton,fallAsleepTimeButton,sleepingTimeButton;
    private Switch getUpAlarmSwitch,fallAsleepAlarmSwitch;
    private AlarmManager alarmManager;  //闹钟服务
    private PendingIntent getUpPendingIntent,fallAsleepPendingIntent;//挂起操作
    int getUpHour, getUpMinute, fallAsleepHour, fallAsleepMinute;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View sleepLayout = inflater.inflate(R.layout.sleep_layout, container, false);
        return sleepLayout;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SharedPreferences pref = getActivity().getSharedPreferences("sleepTime",Context.MODE_PRIVATE);
        getUpTimeButton = (Button)getView().findViewById(R.id.getUpTimeButton);
        fallAsleepTimeButton = (Button)getView().findViewById(R.id.fallAsleepTimeButton);
        sleepingTimeButton = (Button)getView().findViewById(R.id.sleepingTimeButton);
        getUpAlarmSwitch = (Switch)getView().findViewById(R.id.getUpAlarmSwitch);
        fallAsleepAlarmSwitch = (Switch)getView().findViewById(R.id.fallAsleepAlarmSwitch);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        final SharedPreferences.Editor editor = pref.edit();

        getUpHour = pref.getInt("getUpHour",-1);
        getUpMinute = pref.getInt("getUpMinute",-1);
        fallAsleepHour = pref.getInt("fallAsleepHour",-1);
        fallAsleepMinute = pref.getInt("fallAsleepMinute",-1);
        getUpTimeButton.setText(" 起床时间  "+getUpHour+" : "+getUpMinute);
        fallAsleepTimeButton.setText(" 入睡时间  "+fallAsleepHour+" : "+fallAsleepMinute);
        startTimeCount();

        Boolean getUpSwitchCheck = pref.getBoolean("getUpSwitchCheck",false);
        Boolean fallAsleepSwitchCheck = pref.getBoolean("fallAsleepSwitchCheck",false);

        getUpAlarmSwitch.setChecked(getUpSwitchCheck);
        fallAsleepAlarmSwitch.setChecked(fallAsleepSwitchCheck);

        getUpAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("getUpSwitchCheck",isChecked);
                editor.apply();
                if (isChecked){
                    //选中状态 可以做一些操作
                    /*设置当前时间:*/
                    Calendar c1 = Calendar.getInstance();
                    c1.setTimeInMillis(System.currentTimeMillis());
                    System.out.println(c1.getTime());
                    /*根据用户选择的时间来设置Calendar对象*/
                    c1.set(Calendar.HOUR_OF_DAY,getUpHour);
                    c1.set(Calendar.MINUTE,getUpMinute);
                    /*设置AlarmManager在Calendar对应的时间启动Activity*/
                    Intent intent = new Intent(getActivity(),GetUpAlarmActivity.class);
                    getUpPendingIntent = PendingIntent.getActivity(getActivity(),0,intent,0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,c1.getTimeInMillis(),getUpPendingIntent);
                    Toast.makeText(getActivity(),"起床闹钟已开启!",Toast.LENGTH_SHORT).show();
                }else {
                    //未选中状态 可以做一些操作
                    System.out.println("notChecked");
                    try {
                        /*设置当前时间:*/
                        Calendar c1 = Calendar.getInstance();
                        c1.setTimeInMillis(System.currentTimeMillis());
                        System.out.println(c1.getTime());
                        /*根据用户选择的时间来设置Calendar对象*/
                        c1.set(Calendar.HOUR_OF_DAY,getUpHour);
                        c1.set(Calendar.MINUTE,getUpMinute);
                        /*设置AlarmManager在Calendar对应的时间启动Activity*/
                        Intent intent = new Intent(getActivity(),GetUpAlarmActivity.class);
                        getUpPendingIntent = PendingIntent.getActivity(getActivity(),0,intent,0);
                        alarmManager.cancel(getUpPendingIntent);
                    }catch (Exception e){
                        System.out.println(e);
                        Toast.makeText(getActivity(),"请重新设置起床提醒！",Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getActivity(),"起床闹钟已关闭！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fallAsleepAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("fallAsleepSwitchCheck",isChecked);
                editor.apply();
                if (isChecked){
                    //选中状态 可以做一些操作
                    /*设置当前时间:*/
                    Calendar c1 = Calendar.getInstance();
                    c1.setTimeInMillis(System.currentTimeMillis());
                    System.out.println(c1.getTime());
                    /*根据用户选择的时间来设置Calendar对象*/
                    c1.set(Calendar.HOUR_OF_DAY,fallAsleepHour);
                    c1.set(Calendar.MINUTE,fallAsleepMinute);
                    /*设置AlarmManager在Calendar对应的时间启动Activity*/
                    Intent intent = new Intent(getActivity(),FallAsleepAlarmActivity.class);
                    fallAsleepPendingIntent = PendingIntent.getActivity(getActivity(),0,intent,0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,c1.getTimeInMillis(),fallAsleepPendingIntent);
                    Toast.makeText(getActivity(),"入睡提醒已开启!",Toast.LENGTH_SHORT).show();
                }else {
                    //未选中状态 可以做一些操作
                    System.out.println("notChecked");
                    try {
                        Calendar c1 = Calendar.getInstance();
                        c1.setTimeInMillis(System.currentTimeMillis());
                        System.out.println(c1.getTime());
                        /*根据用户选择的时间来设置Calendar对象*/
                        c1.set(Calendar.HOUR_OF_DAY,fallAsleepHour);
                        c1.set(Calendar.MINUTE,fallAsleepMinute);
                        /*设置AlarmManager在Calendar对应的时间启动Activity*/
                        Intent intent = new Intent(getActivity(),FallAsleepAlarmActivity.class);
                        fallAsleepPendingIntent = PendingIntent.getActivity(getActivity(),0,intent,0);
                        alarmManager.cancel(fallAsleepPendingIntent);
                    }catch (Exception e) {
                        System.out.println(e);
                        Toast.makeText(getActivity(), "请重新设置入睡提醒！", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getActivity(),"入睡提醒已关闭！",Toast.LENGTH_SHORT).show();

                }
            }
        });

        getUpTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int getUpHour = pref.getInt("getUpHour",-1);
                int getUpMinute = pref.getInt("getUpMinute",-1);
                Intent intent = new Intent(getActivity(),TimePickerActivity.class);
                intent.putExtra("flag","getUpTime");
                getActivity().startActivityForResult(intent,1);
                getUpTimeButton.setText(" 起床时间  "+getUpHour+" : "+getUpMinute);
            }
        });

        fallAsleepTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fallAsleepHour = pref.getInt("fallAsleepHour",-1);
                int fallAsleepMinute = pref.getInt("fallAsleepMinute",-1);
                Intent intent = new Intent(getActivity(),TimePickerActivity.class);
                intent.putExtra("flag","fallAsleepTime");
                getActivity().startActivityForResult(intent,1);
                fallAsleepTimeButton.setText(" 入睡时间  "+fallAsleepHour+" : "+fallAsleepMinute);
            }
        });


        sleepingTimeButton.setOnClickListener(new View.OnClickListener() {
            int hour,minute,temp;
            String stringShow;
            @Override
            public void onClick(View v) {
                temp = 24*60-fallAsleepHour*60-fallAsleepMinute+
                        getUpHour*60+getUpMinute;
                System.out.println(temp);
                hour = temp/60;
                minute = temp-hour*60;
                stringShow = String.format(" 持续时间: %dh%dm",hour,minute);
                sleepingTimeButton.setText(stringShow);
            }
        });
    }

    private void startTimeCount() {
        new CountDownTimer(500,100){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(isAdded()){
                    SharedPreferences pref = getActivity().getSharedPreferences("sleepTime",Context.MODE_PRIVATE);
                    getUpHour = pref.getInt("getUpHour",-1);
                    getUpMinute = pref.getInt("getUpMinute",-1);
                    fallAsleepHour = pref.getInt("fallAsleepHour",-1);
                    fallAsleepMinute = pref.getInt("fallAsleepMinute",-1);

                    getUpTimeButton.setText(" 起床时间  "+getUpHour+" : "+getUpMinute);
                    fallAsleepTimeButton.setText(" 入睡时间  "+fallAsleepHour+" : "+fallAsleepMinute);

                    int hour,minute,temp;
                    String stringShow;
                    temp = 24*60-fallAsleepHour*60-fallAsleepMinute+
                            getUpHour*60+getUpMinute;
                    System.out.println(temp);
                    hour = temp/60;
                    minute = temp-hour*60;
                    stringShow = String.format(" 持续时间: %dh%dm",hour,minute);
                    sleepingTimeButton.setText(stringShow);
                    cancel();
                    startTimeCount();
                }
            }
        }.start();
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