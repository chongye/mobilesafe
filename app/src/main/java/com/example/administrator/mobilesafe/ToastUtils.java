package com.example.administrator.mobilesafe;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/7 0007.
 */
public class ToastUtils {
    public static void show(Context ct, String s) {
        Toast.makeText(ct, s, Toast.LENGTH_SHORT).show();
    }
}
