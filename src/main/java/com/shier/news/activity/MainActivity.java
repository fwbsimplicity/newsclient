package com.shier.news.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.shier.newsClient.R;
import com.shier.news.fragment.BehindFragment;
import com.shier.news.fragment.MainFragment;
import com.shier.news.utils.ConstantUtils;

import cn.bmob.v3.Bmob;

public class MainActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,ConstantUtils.APP_ID);
        initView();
        initFragment();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.behind_view);//设置侧边栏的布局
        SlidingMenu slidingMenu = getSlidingMenu();//获取侧边栏对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置触摸模式
        slidingMenu.setBehindOffset(200);//预留的宽度
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //将FrameLayout布局换成Fragment
        transaction.replace(R.id.fl_behind_view, new BehindFragment(), ConstantUtils.BEHIND_FRAGMENT_TAG);
        transaction.replace(R.id.fl_main, new MainFragment(), ConstantUtils.MAIN_FRAGMENT_TAG);
        transaction.commit();
    }

    public Fragment getFragmentByTag(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentByTag(tag);
    }
}
