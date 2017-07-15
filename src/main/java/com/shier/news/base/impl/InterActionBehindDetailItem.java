package com.shier.news.base.impl;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shier.news.base.BaseDetailItem;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/4/7  10:12
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class InterActionBehindDetailItem extends BaseDetailItem {

    public InterActionBehindDetailItem(Activity activity) {
        super(activity);
    }

    @Override
    public View initViews() {
        TextView tv = new TextView(mActivity);
        tv.setGravity(Gravity.CENTER);
        tv.setText("互动");
        return tv;
    }

    @Override
    public void initData() {
    }

}
