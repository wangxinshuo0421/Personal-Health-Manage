package com.example.personal_health_manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;


public class MyFragment extends Fragment {
    private EditText heightEdit,weightEdit,highBloodPressureEdit,lowBloodPressureEdit,bloodSugarEdit;
    private Button refreshButton;

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

    private int flag;

    @Override
    /***
    fragment不能用findviewbyid
     ***/
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myLayout = inflater.inflate(R.layout.my_layout, container, false);
        return myLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        heightEdit = (EditText) getView().findViewById(R.id.height);
        weightEdit = (EditText) getView().findViewById(R.id.weight);
        highBloodPressureEdit = (EditText) getView().findViewById(R.id.high_blood_pressure);
        lowBloodPressureEdit = (EditText) getView().findViewById(R.id.low_blood_pressure);
        bloodSugarEdit = (EditText) getView().findViewById(R.id.blood_sugar);


        refreshButton = (Button) getView().findViewById(R.id.button_Refresh);
        mThreadPool = Executors.newCachedThreadPool();
        String heightStr,weightStr,highBloodPressureStr,lowBloodPressureStr,bloodSugarStr;

        heightStr = sharedPref.getString("heightStr","");
        weightStr = sharedPref.getString("weightStr","");
        highBloodPressureStr = sharedPref.getString("highBloodPressureStr","");
        lowBloodPressureStr  = sharedPref.getString("lowBloodPressureStr","");
        bloodSugarStr = sharedPref.getString("bloodSugarStr","");
        heightEdit.setText(heightStr);
        weightEdit.setText(weightStr);
        highBloodPressureEdit.setText(highBloodPressureStr);
        lowBloodPressureEdit.setText(lowBloodPressureStr);
        bloodSugarEdit.setText(bloodSugarStr);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                final String userId = Data.getUserId();
                final String height = heightEdit.getText().toString();
                final String weight = weightEdit.getText().toString();
                final String highBloodPressure = highBloodPressureEdit.getText().toString();
                final String lowBloodPressure = lowBloodPressureEdit.getText().toString();
                final String bloodSugar = bloodSugarEdit.getText().toString();

                /*  如果某位置为空 从云端获取相应数据  表示刷新*/

                if(height.equals("")||weight.equals("")||highBloodPressure.equals("")
                ||lowBloodPressure.equals("")||bloodSugar.equals("")){
                    Toast.makeText(getActivity(),"已通过云平台数据刷新",Toast.LENGTH_SHORT).show();
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
                                String sendData = "01{\"userId\": \""+userId+"\"}"+"\n";
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
                                if(response.equals("{\"flag\":\"0\"}"));
                                else{
                                    try{
                                        /**
                                         * 为什么要使用jsonObject.optString， 不使用jsonObject.getString
                                         * 因为jsonObject.optString获取null不会报错
                                         */
                                        JSONObject jsonObject = new JSONObject(response);
                                        String heightValue = jsonObject.optString("height",null);
                                        String weightValue = jsonObject.optString("weight",null);
                                        String highBloodPressureValue = jsonObject.optString("highBloodPressure",null);
                                        String lowBloodPressureValue = jsonObject.optString("lowBloodPressure",null);
                                        String bloodSugarValue = jsonObject.optString("bloodSugar",null);
                                        Data.setHeight(heightValue);
                                        Data.setWeight(weightValue);
                                        Data.setHighBloodPressure(highBloodPressureValue);
                                        Data.setLowBloodPressure(lowBloodPressureValue);
                                        Data.setBloodSugar(bloodSugarValue);
                                        SharedPreferences.Editor editor2 = sharedPref.edit();
                                        editor2.putString("weightStr",weightValue);
                                        editor2.putString("heightStr",heightValue);
                                        editor2.putString("highBloodPressureStr",highBloodPressureValue);
                                        editor2.putString("lowBloodPressureStr",lowBloodPressureValue);
                                        editor2.putString("bloodSugarStr",bloodSugarValue);
                                        editor2.apply();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    /**
                                     * 为什么要使用jsonObject.optString， 不使用jsonObject.getString
                                     * 因为jsonObject.optString获取null不会报错
                                     */
                                }
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
                }
                /*  如果全满，，则存储本地和云端两份数据  */
                else{
                    Toast.makeText(getActivity(),"数据已存储",Toast.LENGTH_SHORT).show();
                    editor.putString("weightStr",weight);
                    editor.putString("heightStr",height);
                    editor.putString("highBloodPressureStr",highBloodPressure);
                    editor.putString("lowBloodPressureStr",lowBloodPressure);
                    editor.putString("bloodSugarStr",bloodSugar);
                    editor.apply();
                    Toast.makeText(getActivity(),"数据存储成功",Toast.LENGTH_SHORT).show();
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
                                String sendData = "00{\"userId\": \""+userId+"\",\"height\": \""+height+
                                        "\",\"weight\": \""+weight+"\",\"highBloodPressure\": \""+highBloodPressure+
                                        "\",\"lowBloodPressure\":\""+lowBloodPressure+"\",\"bloodSugar\":\""+
                                        bloodSugar+"\"}"+"\n";
                                outputStream.write((sendData).getBytes("utf-8"));
                                System.out.println(sendData);
                                // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞
                                // 步骤3：发送数据到服务端
                                outputStream.flush();

                                // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
                                outputStream.close();
                                // 最终关闭整个Socket连接
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });
        heightEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightEdit.setText("");
            }
        });
        weightEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightEdit.setText("");
            }
        });
        highBloodPressureEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highBloodPressureEdit.setText("");
            }
        });
        lowBloodPressureEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowBloodPressureEdit.setText("");
            }
        });
        bloodSugarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloodSugarEdit.setText("");
            }
        });
    }

}