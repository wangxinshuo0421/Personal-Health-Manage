package com.example.personal_health_manage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Date;

public class TimePickerActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button timeConfirmButton;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        /*消除标题*/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        timePicker = (TimePicker)findViewById(R.id.timePick);
        timeConfirmButton = (Button)findViewById(R.id.timeConfirmButton);
        SharedPreferences pref = getSharedPreferences("sleepTime", Context.MODE_PRIVATE);
        editor = pref.edit();
        timePicker.setIs24HourView(true);

        timeConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(10);  //设值任意 和响应吗没有关系 互不影响
                finish();
//                Intent intent = new Intent(TimePickerActivity.this, InterfaceActivity.class);
//                Data.setSleepFragmentFlag(1);
//                startActivity(intent);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String flag = getIntent().getStringExtra("flag");
                if(flag.equals("getUpTime")){
                    Data.setGetUpHour(hourOfDay);
                    Data.setGetUpMinute(minute);
                    editor.putInt("getUpHour",hourOfDay);
                    editor.putInt("getUpMinute",minute);
                    editor.apply();
                }else if(flag.equals("fallAsleepTime")){
                    Data.setFallAsleepHour(hourOfDay);
                    Data.setFallAsleepMinute(minute);
                    editor.putInt("fallAsleepHour",hourOfDay);
                    editor.putInt("fallAsleepMinute",minute);
                    editor.apply();
                }
            }
        });

    }
}
