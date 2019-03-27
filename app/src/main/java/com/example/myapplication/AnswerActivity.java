package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.util.Objects;

public class AnswerActivity extends AppCompatActivity {

    private TextView mAnswerTextView;
    private static final String EXTRA_ANSWER_SHOWN="answer_shown";//返回的键值
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏标题栏
        setContentView(R.layout.activity_answer);

        mAnswerTextView=findViewById(R.id.Answer_Text_View);
        Intent data=getIntent();//获取传过来的Intent对象
        String answer=data.getStringExtra("msg");//取出msg对应的数据
        mAnswerTextView.setText(answer);//显示到文本组件中

        data.putExtra(EXTRA_ANSWER_SHOWN,"您已查看了答案");
        setResult(Activity.RESULT_OK,data);//返回代码和intent对象（包含要返回的数据）

        mImageView=findViewById(R.id.imageView);
        Animator set= AnimatorInflater.loadAnimator(this,R.animator.animator_alpha);//加载本地属性动画
        set.setTarget(mImageView);//将动画设置到图片组件
        set.start();//启动动画
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //TODO 动画开始前
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //TODO 动画结束时
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //TODO 动画取消时
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //TODO 动画重复时
            }
        });

        ValueAnimator moneyAnimator=ValueAnimator.ofFloat(0f,152019.03f);//float估值器
        moneyAnimator.setInterpolator(new DecelerateInterpolator());//减速插值器
        moneyAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//当动画执行时发生
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float money=(float)animation.getAnimatedValue();//取出动画的值
                mAnswerTextView.setText(String.format("%.2f元",money));
            }
        });
        //moneyAnimator.setDuration(3000);
        //moneyAnimator.start();
        int startColor= Color.parseColor("#FFDEAF");
        int endColor= Color.parseColor("#FF4500");
        ValueAnimator colorAnimator =ValueAnimator.ofArgb(startColor,endColor);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color=(int)animation.getAnimatedValue();
                mAnswerTextView.setTextColor(color);
            }
        });
        AnimatorSet Set=new AnimatorSet();
        Set.playTogether(moneyAnimator,colorAnimator);
        Set.setDuration(3000);
        Set.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        mImageView.setImageResource(R.drawable.animation_frame);//将 帧动画文件 设置到 图片组件的图片资源中
//        AnimationDrawable animationDrawable=(AnimationDrawable) mImageView.getDrawable();//将 组件的图片资源 设置到 逐帧动画对象 中
//        animationDrawable.start();//启动动画
    }
}
