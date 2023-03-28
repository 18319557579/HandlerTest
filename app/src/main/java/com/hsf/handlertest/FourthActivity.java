package com.hsf.handlertest;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class FourthActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setClickSkip1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("子线程的异常");
                    }
                }).start();
            }
        }).setText("在子线程中抛出异常");

        setClickSkip2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("主线程的异常");
            }
        }).setText("在主线程中抛出异常");
    }
}
