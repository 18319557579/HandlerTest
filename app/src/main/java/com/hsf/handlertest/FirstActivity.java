package com.hsf.handlertest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstActivity extends BaseActivity{

    private Handler handler;
    private Handler threadHandler;

    ExecutorService service = Executors.newFixedThreadPool(4);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createChildThreadHandler();

        handler = new Handler(this.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    String message = (String) msg.obj;
                    Toast.makeText(FirstActivity.this, "获得信息:" + message , Toast.LENGTH_SHORT).show();
                }
            }
        };

        setClickSkip1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = Message.obtain();
                        message.obj = "这是子线程的消息";
                        message.arg1 = 10;
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        }).setText("发送常规消息");

        setClickSkip2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.execute(new Runnable() {
                    @Override

                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FirstActivity.this, "用post弹出" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).setText("使用post发送消息");

        setClickSkip3(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.execute(new Runnable() {
                    @Override
                    public void run() {

                        handler.sendEmptyMessage(1);
                    }
                });

            }
        }).setText("发送空消息");

        setClickSkip4(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessageDelayed(message, 3000);
                    }
                });
            }
        }).setText("发送延迟消息");

        setClickSkip5(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "我直接弹好吧", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).setText("在子线程中得到UI线程的handler对象");

        setClickSkip6(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
            }
        }).setText("取消发送的延迟消息");

        setClickSkip7(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtils.directToast();
            }
        }).setText("调用工具类来弹");

        setClickSkip8(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FirstActivity.this, "我也能弹了", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).setText("在子线程的Handler操作吧");

        setClickSkip9(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.hsf.handlertest.SecondActivity");
                startActivity(intent);
            }
        }).setText("去SecondActivity");
    }

    private void createChildThreadHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                threadHandler = new Handler(Looper.myLooper());
                Looper.loop();
            }
        }).start();
    }
}
