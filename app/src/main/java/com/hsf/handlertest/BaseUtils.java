package com.hsf.handlertest;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class BaseUtils {
    public static void directToast() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getContext(), "我可以直接弹了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
