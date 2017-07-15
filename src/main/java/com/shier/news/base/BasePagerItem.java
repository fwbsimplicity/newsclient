package com.shier.news.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shier.newsClient.R;
import com.shier.news.activity.MainActivity;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/3/31  11:12
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public abstract class BasePagerItem {

    public Activity mActivity;
    public View basePagerView;
    public ImageButton imgBt_ic_lv;
    protected TextView tv_title;
    protected FrameLayout fl_basePageItem;
    private ImageButton imgBt;
    private MainActivity mainActivity;

    public BasePagerItem(Activity activity) {
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        basePagerView = View.inflate(mActivity, R.layout.base_pager_item, null);
        imgBt = (ImageButton) basePagerView.findViewById(R.id.imgBt);
        tv_title = (TextView) basePagerView.findViewById(R.id.tv_title);
        fl_basePageItem = (FrameLayout) basePagerView.findViewById(R.id.fl_basePageItem);
        imgBt_ic_lv = (ImageButton) basePagerView.findViewById(R.id.ib_ic_lv);
        mainActivity = (MainActivity) mActivity;
        initListener();
    }

    private void initListener() {
        imgBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.toggle();
            }
        });
    }

    public abstract void initData();

    public void setBehindViewEnabled(boolean enabled) {
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if(enabled) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            imgBt.setVisibility(View.VISIBLE);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            imgBt.setVisibility(View.INVISIBLE);
        }

    }
}
