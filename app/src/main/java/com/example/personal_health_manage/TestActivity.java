package com.example.personal_health_manage;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager mSensorManager;//管理器实例
    Sensor stepCounter;//传感器
    float mSteps = 0;//步数
    TextView steps;//显示步数
    TextView time;//显示时间
    private Object Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // 获取SensorManager管理器实例
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        // getSensorList用于列出设备支持的所有sensor列表
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        System.out.println("Sensor size:"+sensorList.size());
        for (Sensor sensor : sensorList) {
            System.out.println("Supported Sensor: "+sensor.getName());
        }

        steps = (TextView)findViewById(R.id.steps);
        time = (TextView)findViewById(R.id.time);
        // 获取计步器sensor
        stepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCounter != null){
            // 如果sensor找到，则注册监听器
            mSensorManager.registerListener(this,stepCounter,1000000);
            System.out.println("\n\n\n\nthere\n\n\n");
        }
        else{
            System.out.println("no step counter sensor found");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mSteps = event.values[0];
        steps.setText("你已经走了"+String.valueOf((int)mSteps)+"步");
        System.out.println("\n\n\n\n走了" + String.valueOf((int)mSteps)+"\n\n\n");


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("here we go\n\n\n\n\n\n here we go");
    }
}