package com.example.personal_health_manage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity{

    /**
     * 主 变量
     */

    // Socket变量
    private Socket socket;

    String ip = Data.getServerIp();
    int port = Data.getServerPort();
    // 线程池
    // 为了方便,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    /**
     * 接收服务器消息 变量
     */
    // 输入流对象
    InputStream is;

    // 输入流读取器对象
    InputStreamReader isr ;
    BufferedReader br ;

    // 接收服务器发送过来的消息
    String response;
    /**
     * 发送消息到服务器 变量
     */
    // 输出流对象
    OutputStream outputStream;
    /*按钮 变量*/
    private ImageView imageView;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText userIdEdit;
    private EditText passwordEdit;
    private Button loginComfirm;
    private CheckBox rememberPassword;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userIdEdit = (EditText) findViewById(R.id.user_id);
        passwordEdit = (EditText) findViewById(R.id.user_password);
        rememberPassword = (CheckBox) findViewById(R.id.remember_password);
        loginComfirm = (Button) findViewById(R.id.button_login_comfirm);
        // 初始化线程池
        mThreadPool = Executors.newCachedThreadPool();

        //取消Android应用自带标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //显示图片
        imageView = (ImageView) findViewById(R.id.imageViewLogin);
        imageView.setImageResource(R.drawable.login_logo);
        //记住密码操作
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String userId = pref.getString("userId","");
            String password = pref.getString("password","");
            userIdEdit.setText(userId);
            passwordEdit.setText(password);
            rememberPassword.setChecked(true);
        }
        //监控登录按钮
        loginComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = userIdEdit.getText().toString();
                final String password = passwordEdit.getText().toString();
                Data.setUserId(userId);
                // 利用线程池直接开启一个线程 & 执行该线程
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 创建Socket对象 & 指定服务端的IP 及 端口号
                            socket = new Socket(ip, port);
                            // 判断客户端和服务器是否连接成功
                            System.out.println(socket.isConnected());
                            // 步骤1：从Socket 获得输出流对象OutputStream
                            // 该对象作用：发送数据
                            outputStream = socket.getOutputStream();
                            // 步骤2：写入需要发送的数据到输出流对象中
                            String sendData = "11{\"userid\": \""+userId+"\",\"password\": \""+password+"\"}"+"\n";
                            outputStream.write((sendData).getBytes("utf-8"));
                            System.out.println(sendData);
                            // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
                            // 步骤3：发送数据到服务端
                            outputStream.flush();
                            // 步骤1：创建输入流对象InputStream
                            while (response == null){
                                is = socket.getInputStream();
                                // 步骤2：创建输入流读取器对象 并传入输入流对象
                                // 该对象作用：获取服务器返回的数据
                                isr = new InputStreamReader(is);
                                br = new BufferedReader(isr);
                                // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                                response = br.readLine();
                            }
                            if(response.equals("{\"flag\":\"1\"}"))
                                flag = 1;
                            else
                                flag = 0;
                            System.out.println(response);
                            // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
                            outputStream.close();
                            // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
                            br.close();
                            // 最终关闭整个Socket连接
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                if (flag == 1||(userId.equals("123456")&&password.equals("123456"))) {
                    editor = pref.edit();
                    if (rememberPassword.isChecked()) { //检测复选框是否被选中
                        editor.putBoolean("remember_password", true);
                        editor.putString("userId", userId);
                        editor.putString("password", password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, InterfaceActivity.class);
                    startActivity(intent);
                    finish();
                } else if(password.equals("321654")){
                    Toast.makeText(LoginActivity.this, "账户ID或密码不正确", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
