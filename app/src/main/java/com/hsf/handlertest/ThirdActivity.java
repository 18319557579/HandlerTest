package com.hsf.handlertest;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class ThirdActivity extends BaseActivity {
    Handler threadHandler;
    Handler useThreadHandler;

    Handler threadOneHandler;
    Handler threadTwoHandler;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMainLooper().getQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.d("Daisy", "主线程空闲了");
                getSupportActionBar().setTitle("我更新了");
                return false;
            }
        });

        createChildThreadHandler();
        createThreadHandler();
        create();

        setClickSkip1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadHandler.sendEmptyMessage(666);
                threadHandler.sendEmptyMessageDelayed(777, 1000);
                threadHandler.sendEmptyMessageDelayed(778, 1000);
                threadHandler.sendEmptyMessageDelayed(888, 2000);
            }
        }).setText("给threadHandler发送消息");

        setClickSkip2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadHandler.getLooper().quit();
            }
        }).setText("取消掉threadHandler");

        setClickSkip3(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadHandler.getLooper().getQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                    @Override
                    public boolean queueIdle() {
                        Log.d("Daisy", "在空闲时打印一下");
                        return true;
                    }
                });
            }
        }).setText("设置在空闲时打印");

        setClickSkip4(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 100;
                threadHandler.sendMessageAtFrontOfQueue(message);
            }
        }).setText("发送Front消息");

        setClickSkip5(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Daisy", "可可儿科");
                    }
                });
            }
        }).setText("给ThreadHandler发送消息");

        setClickSkip5(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        threadTwoHandler = new Handler(threadOneHandler.getLooper()) {
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                Log.d("Daisy", "Thread_Two接收到消息：" + msg.what + " / " + Thread.currentThread());
                            }
                        };

                        threadTwoHandler.sendEmptyMessage(102);
                        threadOneHandler.sendEmptyMessage(101);

                    }
                }, "Thread_Two").start();
            }
        }).setText("用其他线程的Looper");

        setClickSkip6(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ThirdActivity.this, "", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(ThirdActivity.this, "我可以弹" + Thread.currentThread(), Toast.LENGTH_SHORT).show();
//                        Looper.loop();
                    }
                }, "Thread_ttt").start();
            }
        }).setText("在子线程中弹出Toast");

        //捕获主线程的任意异常了，做到主线程永不崩溃
        setClickSkip7(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Looper.loop();
                            }catch (Throwable throwable) {
                                Log.e("Daisy", "又出错了" + throwable);
                            }
                        }
                    }
                });
            }
        }).setText("全局捕获主线程异常");

        //在主线程手动抛出异常
        setClickSkip8(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("爱咋咋地");
            }
        }).setText("抛出异常");

        setClickSkip9(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, FourthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setText("跳去FourthActivity");
    }

    private void createChildThreadHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                threadHandler = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.d("Daisy", "接收到消息：" + msg.what);
                    }
                };
                Looper.loop();
            }
        }).start();
    }

    private void createThreadHandler() {
        HandlerThread handlerThread = new HandlerThread("子线程");
        handlerThread.start();
        useThreadHandler = new Handler(handlerThread.getLooper());
    }

    private void create() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                threadOneHandler = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.d("Daisy", "Thread_One接收到消息：" + msg.what + " / " + Thread.currentThread());
                    }
                };
                Looper.loop();
            }
        }, "Thread_One").start();
    }
}
