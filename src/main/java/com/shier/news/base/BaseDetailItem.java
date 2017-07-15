package com.shier.news.base;

import android.app.Activity;
import android.view.View;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/4/3  14:22
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public abstract class BaseDetailItem {

    public Activity mActivity;
    public View baseDetailView;

    public BaseDetailItem(Activity activity) {
        this.mActivity = activity;
        baseDetailView = initViews();
    }

    public abstract View initViews();

    public abstract void initData();



}
