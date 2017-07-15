package com.shier.news.base.impl;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;

import com.shier.news.base.BasePagerItem;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/4/1  11:44
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class SmartServicePagerItem extends BasePagerItem {

    public SmartServicePagerItem(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tv_title.setText("智能服务");
        TextView tv = new TextView(mActivity);
        tv.setText("智能服务");
        tv.setGravity(Gravity.CENTER);
        fl_basePageItem.addView(tv);
    }
}
