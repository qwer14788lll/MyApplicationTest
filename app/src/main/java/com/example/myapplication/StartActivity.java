package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();//创建线程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏标题栏
        setContentView(R.layout.activity_start);

        mHandler.postDelayed(new Runnable() {//启动线程运行时
            @Override
            public void run() {
                Intent it = new Intent(StartActivity.this,MainActivity.class);
                startActivity(it);
                finish();//销毁当前activity
            }
        },3000);//延迟3秒
    }
    public static void NavigationBarStatusBar(Activity activity, boolean hasFocus){
        if (hasFocus && Build.VERSION.SDK_INT >= 26) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//隐藏导航栏
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {//窗体初始化时
        super.onWindowFocusChanged(hasFocus);
        NavigationBarStatusBar(this,hasFocus);
    }
}
