package com.example.administrator.mobilesafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.administrator.mobilesafe.bean.Version;
import com.example.administrator.mobilesafe.utils.StreamUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    private static final int UPDATE_VERSION = 101;
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int ENTER_HOME = 104;
    private static final String TAG = "SplashActivity";
    TextView mVersionName;
    int mLocalVersionCode,mOriginVersionCode;
    private String mVersionDes;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    // 弹出对话框
                    getAlertDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtils.show(SplashActivity.this,"网址错误");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtils.show(SplashActivity.this,"读取错误");
                    enterHome();
                    break;
            }
        }
    };

    private void getAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.home_apps);
        builder.setTitle("新版本");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.show();
    }

    private void enterHome() {
        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 初始化数据
        initView();
        // 获取数据
        initData();
    }

    private void initData() {
        // 得到版本名称
        mVersionName.setText("版本名称:"+getVersionName());
        // 得到当前版本号
        mLocalVersionCode = getLocalVersionCode();
        // 获取服务器版本号
        getOriginVersionCode();
    }

    private void getOriginVersionCode() {
        new Thread(){
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                Message msg = Message.obtain();
                try {
                    URL url = new URL("http://192.168.1.101/version.json");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(2000);
                    urlConnection.setReadTimeout(2000);
                    InputStream is = urlConnection.getInputStream();
                    String json = StreamUtils.streamToString(is);
                    // json 数据的解析
                    Gson gson = new Gson();
                    Version version = gson.fromJson(json,Version.class);
                    mOriginVersionCode = version.getVersionCode();
                    mVersionDes = version.getDesc();
                    Log.i(TAG,mVersionDes);
                    if(mLocalVersionCode<mOriginVersionCode){
                        msg.what = UPDATE_VERSION;
                    }else {
                        msg.what = ENTER_HOME;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                }finally {
                    long endTime = System.currentTimeMillis();
                    if(endTime - startTime<4000){
                        try {
                            Thread.sleep(4000-(endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 获取当前版本号
     * @return 
     */
    private int getLocalVersionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到版本名
     * @return String 若为空这版本问题
     */
    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对数据进行初始化
     */
    private void initView() {
        mVersionName = (TextView) findViewById(R.id.tv_version_name);
    }
}
