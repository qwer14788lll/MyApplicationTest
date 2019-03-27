package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ComplexColorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mButtonTrue;//正确按钮
    private Button mButtonFalse;//错误按钮
    private Button mButtonNext;//下一题
    private Button mButtonAnswer;//查看答案
    private TextView mQuestionTextView;//显示题目
    private int mQuestionIndex = 0;//题目索引
    private Question[] mQuestions = new Question[]{
            new Question(R.string.Q1, false),
            new Question(R.string.Q2, true),
            new Question(R.string.Q3, true),
            new Question(R.string.Q4, true),
            new Question(R.string.Q5, true),
            new Question(R.string.Q6, true),
            new Question(R.string.Q7, true),
            new Question(R.string.Q8, true)
    };
    private static final String TAG = "MainActivity";//日志来源
    private static final String KEY_INDEX = "index";//索引键
    private static final int REQUEST_CODE_ANSWER=10;//请求代码（发给AnswerActivity的）
    private TranslateAnimation mTranslateAnimation;//平移动画

    private void updateQuestion() {
        int i = mQuestions[mQuestionIndex].getTextId();//获取题目ID
        mQuestionTextView.setText(i);//设置到文本上
//        mTranslateAnimation = new TranslateAnimation(-10, 10, 0, 0);
//        mTranslateAnimation.setDuration(50); //动画持续时间
//        mTranslateAnimation.setRepeatCount(5); //重复次数(不包括第一次)
//        mTranslateAnimation.setRepeatMode(Animation.REVERSE);//动画执行模式，Animation.RESTART:从头开始，Animation.REVERSE:逆序
//        mQuestionTextView.startAnimation(mTranslateAnimation);//文本组件加载指定的动画
        Animation set= AnimationUtils.loadAnimation(this,R.anim.animation_list);
        mQuestionTextView.startAnimation(set);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null)
        {
            mQuestionIndex=savedInstanceState.getInt(KEY_INDEX,0);
            Log.d(TAG, "Bundle恢复状态");
        }
        Log.d(TAG, "onCreate()创建界面");
        setContentView(R.layout.activity_main);

        mButtonTrue = findViewById(R.id.button_true);
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkQuestion(true);
            }
        });
        mButtonFalse = findViewById(R.id.button_false);
        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkQuestion(false);
            }
        });

        mQuestionTextView = findViewById(R.id.question_text_view);
        updateQuestion();//更新题目

        mButtonNext = findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionIndex = (mQuestionIndex + 1) % mQuestions.length;//防溢出循环
                /*if (mQuestionIndex == mQuestions.length - 1) {
                    mQuestionIndex = 0;
                } else {
                    mQuestionIndex++;
                }*/
                updateQuestion();//更新题目
                mButtonNext.setEnabled(false);//恢复下一题按钮不可用状态
                if (mQuestionIndex == mQuestions.length - 1) {
                    Toast.makeText(MainActivity.this, R.string.last, Toast.LENGTH_SHORT).show();
                    mButtonNext.setText(R.string.TextView_reset);//改按钮文字
                    updateButtonNext(R.drawable.ic_reset);
                }
                if (mQuestionIndex == 0) {
                    mButtonNext.setText(R.string.Button_next);//改按钮文字
                    updateButtonNext(R.drawable.ic_next);
                }
            }
        });

        mButtonAnswer=findViewById(R.id.button_answer);
        mButtonAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                if(mQuestions[mQuestionIndex].isAnswer())//解析答案数据
                {
                    temp="正确";
                }
                else
                {
                    temp="错误";
                }
                Intent intent=new Intent(MainActivity.this,AnswerActivity.class);//显式调用
                intent.putExtra("msg",temp);//将数据附加到intent中
//                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)//PackageManager.PERMISSION_GRANTED=0
//                {
//                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);//第二个参数，申请哪些权限，第三个参数，请求代码
//                }
//                Intent intent=new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:17520439994"));
                //startActivity(intent);
                startActivityForResult(intent,REQUEST_CODE_ANSWER);//需要返回值的启动activity方法
            }
        });
    }

    private void checkQuestion(boolean userAnswer) {
        boolean trueAnswer = mQuestions[mQuestionIndex].isAnswer();//取得正确答案
        int message;
        if (userAnswer == trueAnswer) {
            message = R.string.yes;
            mButtonNext.setEnabled(true);
        } else {
            message = R.string.no;
            mButtonNext.setEnabled(false);
        }
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateButtonNext(int imageID) {
        Drawable d = getDrawable(imageID);//获取图片的ID
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        mButtonNext.setCompoundDrawables(null, null, d, null);//图片位于按钮的位置：左、上、右、下
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()使界面可见");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()前台显示");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()界面离开前台");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()界面不可见");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()销毁" + TAG);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()我又肥来了");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()保存状态");
        outState.putInt(KEY_INDEX, mQuestionIndex);//形成键值对
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//请求代码，返回代码，返回来的intent
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ANSWER && resultCode== Activity.RESULT_OK)
        {
            String temp=data.getStringExtra("answer_shown");//取出msg对应的数据
            Toast.makeText(MainActivity.this,temp,Toast.LENGTH_LONG).show();
        }
    }
}