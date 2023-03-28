package com.hsf.handlertest;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class CrashManagerUtil {

    private static CrashManagerUtil mInstance;
    private static Context mContext;

    private CrashManagerUtil() {

    }

    public static CrashManagerUtil getInstance(Context context) {
        if (mInstance == null) {
            mContext = context.getApplicationContext();
            mInstance = new CrashManagerUtil();
        }
        return mInstance;
    }

    public void init() {
        //crach 防护
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e("Daisy" , e.toString());
                handleFileException(e);

                Log.d("Daisy", "继续");
                if (t == Looper.getMainLooper().getThread()) {
                    handleMainThread(e);
                }
            }
        });
    }

    //这里对异常信息作处理，可本地保存，可上传至第三方平台
    private void handleFileException(Throwable e) {
        Log.d("Daisy", "对异常进行处理：" + e);
    }

    private void handleMainThread(Throwable e) {
        Log.d("Daisy", "开启循环");
        while (true) {
            try {
                Looper.loop();
            } catch (Throwable e1) {
                handleFileException(e1);
            }
        }
    }
}
