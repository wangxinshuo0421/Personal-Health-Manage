package com.example.personal_health_manage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

public class FallAsleepAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        final MediaPlayer player = new MediaPlayer().create(this,R.raw.fall_asleep_music);
        player.start();
        /*显示对话框*/
        new AlertDialog.Builder(FallAsleepAlarmActivity.this).
                setTitle("个人健康管理提醒您：").
                setMessage("该休息了哟！！！！").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        player.stop();
                        FallAsleepAlarmActivity.this.finish();
                    }
                }).show();
    }
}
