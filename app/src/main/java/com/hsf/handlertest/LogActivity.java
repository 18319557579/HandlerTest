package com.hsf.handlertest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class LogActivity extends AppCompatActivity {
    protected String className;
    {
        Random random = new Random();
        int randomNum = random.nextInt(999999);
        className = String.valueOf(randomNum);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Daisy", "onCreate回调 " + getClass().getName() + " " + className);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("Daisy", "onNewIntent回调 " + getClass().getName() + " "+ className);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Daisy", "onStart回调 " + getClass().getName() + " " + className);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Daisy", "onResume回调 " + getClass().getName() + " " + className);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Daisy", "onPause回调 " + getClass().getName() + " " + className);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Daisy", "onStop回调 " + getClass().getName() + " " + className);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Daisy", "onDestroy回调 " + getClass().getName() + " " + className);
    }
}
