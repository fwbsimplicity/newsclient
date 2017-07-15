package com.shier.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.shier.newsClient.R;
import com.shier.news.utils.SPUtils;

public class SplashActivity extends Activity {

    private RelativeLayout rl_splash_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        setContentView(R.layout.activity_splash);
        rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        startAnimation();
    }

    private void startAnimation() {
        //false代表集合不使用同一个插补器
        AnimationSet animationSet = new AnimationSet(false);

        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);

        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);

        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                  jumpToNextActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

       rl_splash_root.startAnimation(animationSet);
    }

    private void jumpToNextActivity() {
        boolean isFirstEnter = SPUtils.getBoolean(this, "isFirstEnter",true);
        if(isFirstEnter){
            startActivity(new Intent(this,GuideActivity.class));
            SPUtils.saveBoolean(this,"isFirstEnter",false);
        }else {
            startActivity(new Intent(this,MainActivity.class));
        }
        finish();
    }
}
