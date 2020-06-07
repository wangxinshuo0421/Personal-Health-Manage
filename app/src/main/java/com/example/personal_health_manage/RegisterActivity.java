package com.example.personal_health_manage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText registerId;
    private EditText password1;
    private EditText password2;
    private Button registerComfirm;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageView = (ImageView) findViewById(R.id.imageViewRegister);
        imageView.setImageResource(R.drawable.register_logo);
        registerId = (EditText)findViewById(R.id.registerId);
        password1 = (EditText)findViewById(R.id.user_password1);
        password2 = (EditText)findViewById(R.id.user_password2);
        registerComfirm = (Button)findViewById(R.id.registerComfirm);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        // 初始化线程池
        mThreadPool = Executors.newCachedThreadPool();

        registerComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = registerId.getText().toString();
                final String strPassword1 = password1.getText().toString();
                final String strPassword2 = password2.getText().toString();
                if(userId.length() == 11 && strPassword1.equals(strPassword2)){
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
                                String sendData = "10{\"userid\": \""+userId+"\",\"password\": \""+strPassword2+"\"}"+"\n";
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
//                                if(response.equals("{\"flag\":\"1\"}"))
//                                    flag = 1;
//                                else
//                                    flag = 0;
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
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    if(userId.length() < 11)
                        Toast.makeText(RegisterActivity.this, "账户ID小于11位，非电话用户", Toast.LENGTH_SHORT).show();
                    else if(userId.length() > 11)
                        Toast.makeText(RegisterActivity.this, "账户ID大于11位，非电话用户", Toast.LENGTH_SHORT).show();
                    else if(strPassword1 == null)
                        Toast.makeText(RegisterActivity.this, "第一次密码为空，请输入", Toast.LENGTH_SHORT).show();
                    else if(strPassword2 == null)
                        Toast.makeText(RegisterActivity.this, "第二次密码为空，请输入", Toast.LENGTH_SHORT).show();
                    else if(!strPassword1.equals(strPassword2))
                        Toast.makeText(RegisterActivity.this, strPassword1+' '+strPassword2, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
