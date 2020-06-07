package com.example.personal_health_manage;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

public class StepCountFragment extends Fragment  {

    private TextView stepCountTopTextView,stepCountBottomTextView;
    private Typeface typeface;
    private int todayStepCount;     //今日步数和
    private int oneHourStepCount;   //过去1小时内的步数和

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View stepCountLayout = inflater.inflate(R.layout.step_count_layout, container, false);
        return stepCountLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stepCountTopTextView = (TextView)getActivity().findViewById(R.id.step_count_text_top);
        stepCountBottomTextView = (TextView)getActivity().findViewById(R.id.step_count_text_bottom);
        typeface =Typeface.createFromAsset(getActivity().getAssets(),"font/HYQuHeiW.ttf");
        stepCountBottomTextView.setTypeface(typeface);
        stepCountTopTextView.setTypeface(typeface);
        todayStepCount = Data.getOneDayStepCount();
        oneHourStepCount = Data.getOneHourStepCount();
        String str = String.format(" 过去1小时: %d步",oneHourStepCount);
        stepCountBottomTextView.setText(str);
        str = String.format(" 今日步数: %d步",todayStepCount);
        stepCountTopTextView.setText(str);
        startTimeCount();
        if(oneHourStepCount<=100){  //如果过去一小时步数小于100
            Toast.makeText(getActivity(),"久坐提醒：请起身运动一下",Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimeCount() {
        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(isAdded()){
                    todayStepCount = Data.getOneDayStepCount();
                    oneHourStepCount = Data.getOneHourStepCount();
                    String str = String.format(" 过去1小时: %d步",oneHourStepCount);
                    stepCountBottomTextView.setText(str);
                    str = String.format(" 今日步数: %d步",todayStepCount);
                    stepCountTopTextView.setText(str);
                    cancel();
                    startTimeCount();
                }
            }
        }.start();
    }


}