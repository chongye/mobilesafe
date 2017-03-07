package com.example.administrator.mobilesafe.bean;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class Version {

    /**
     * versionCode : 2
     * versionName : 2.0
     * desc : 版本更新了，快来下载
     * download : http://192.168.1.1/mobile.apk
     */

    private int versionCode;
    private String versionName;
    private String desc;
    private String download;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }
}
