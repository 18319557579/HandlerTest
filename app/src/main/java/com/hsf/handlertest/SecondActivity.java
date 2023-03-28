package com.hsf.handlertest;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SecondActivity extends BaseActivity{
    private Handler threadHandler1;
    private Handler threadHandler2;

    private Handler threadHandlerSpecial;

    private Handler asyncHandler;

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createChildThreadHandler();
        createChildThreadHandlerSpecial();
        createAsyncHandler();

        threadHandlerSpecial = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.d("Daisy", "处理消息：" + msg.what);
            }
        };

        Button btnSkip3 = ((Button)findViewById(R.id.btn_skip_3));
        btnSkip3.setVisibility(View.VISIBLE);
        btnSkip3.setText("我可见了");

        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                Log.d("Daisy", "我设置了文本");
                btnSkip3.setText("你好");
            }
        }).start();

        setClickSkip1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mAlertDialog = new AlertDialog.Builder(SecondActivity.this)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle("子线程创建的对话框")
                                .create();
                        mAlertDialog.show();
                        Looper.loop();
                    }
                }).start();
            }
        }).setText("在子线程中创建Dialog");

        setClickSkip2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.show();
            }
        }).setText("在UI线程中展示子线程的Dialog");

        setClickSkip4(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadHandler1.sendEmptyMessageDelayed(1, 1000);
                threadHandler2.sendEmptyMessageDelayed(3,  1500);
                threadHandler1.sendEmptyMessageDelayed(2 ,2000);
            }
        }).setText("给两个Handler发送消息");

        setClickSkip5(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadHandler1.removeCallbacksAndMessages(null);
//                Log.d("Daisy", "是否相等：" + threadHandler1.getLooper().equals(threadHandler2.getLooper()));
            }
        }).setText("取消发送的延迟消息");

        setClickSkip6(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Daisy", "threadHandlerSpecial为空吗：" + (threadHandlerSpecial == null));
                threadHandlerSpecial.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Daisy", "处理消息2：");
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        Log.d("Daisy", "处理消息：");
//                        Looper.prepare();
//
//                        threadHandlerSpecial = new Handler(Looper.myLooper()) {
//                            @Override
//                            public void handleMessage(@NonNull Message msg) {
//                                super.handleMessage(msg);
//                                Log.d("Daisy", "处理消息：" + msg.what);
//                            }
//                        };
//                        Looper.loop();


                    }
                }).start();
            }
        }).setText("特殊的发消息");

        setClickSkip7(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {
                        Log.d("Daisy", "我先处理消息，并截断了");
                        return true;
                    }
                }) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.d("Daisy", "在这里处理消息：" + msg.what + " / " + Thread.currentThread());
                    }
                };

                handler.sendEmptyMessage(999);
            }
        }).setText("使用main的looper，并有东西截断");

        //这个例子说明了，message被handleMessage回调后，就被回收了
        setClickSkip8(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = 999;
                message.arg1 = 3;
                message.obj = new String("大家好");
                asyncHandler.sendMessage(message);
            }
        }).setText("异步处理Message消息");

        setClickSkip9(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.hsf.handlertest.ThirdActivity");
                intent.addCategory("android.intent.category.TTT");
                startActivity(intent);
            }
        }).setText("跳去ThirdActivity");
    }



    private void createChildThreadHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                threadHandler1 = new Handler(Looper.myLooper()){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.d("Daisy", "Handler1执行了：" + msg.what);
                    }
                };

                threadHandler2 = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.d("Daisy", "Handler2执行了：" + msg.what);
                    }
                };
                Looper.loop();
            }
        }).start();
    }

    private void createChildThreadHandlerSpecial() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Looper.loop();

                //下面都是走不到的了，因为loop()中开启了死循环
                Log.d("Daisy", "这里走到了吗");

            }
        }).start();
    }

    public void createAsyncHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                asyncHandler = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.d("Daisy", "之前：" + msg.toString());
                        handleMessageAsync(msg);
                    }
                };
                Looper.loop();
            }
        }).start();
    }

    private void handleMessageAsync(Message msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Daisy", "之后：" + msg.toString());
            }
        }).start();
    }
}
