package com.hsf.handlertest;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        CrashManagerUtil.getInstance(getContext()).init();
    }

    public static Context getContext() {
        return context;
    }
}
