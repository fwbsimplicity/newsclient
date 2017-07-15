package com.shier.news.base.impl;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.shier.newsClient.R;
import com.shier.news.activity.MainActivity;
import com.shier.news.adapter.BasePagerAdapter;
import com.shier.news.base.BaseDetailItem;
import com.shier.news.domain.ViewData;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

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
class NewsBehindDetailItem extends BaseDetailItem implements ViewPager.OnPageChangeListener{

    private ArrayList<ViewData.SubNewsData> mSubNewsData;
    private ArrayList<TabPagerDetailItem> mTabPagerDetail;
    private TabPageIndicator indicator;
    private ViewPager newsDetail_viewPager;
    private ImageButton imgBt_nextItem;

    NewsBehindDetailItem(Activity activity, ArrayList<ViewData.SubNewsData> lists) {
        super(activity);
        mSubNewsData = lists;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_detail_item, null);
        newsDetail_viewPager = (ViewPager) view.findViewById(R.id.newsDetail_viewPager);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        imgBt_nextItem = (ImageButton) view.findViewById(R.id.imgBt_nextItem);
        return view;
    }


    @Override
    public void initData() {
        mTabPagerDetail = new ArrayList<>();
        for(int i = 0; i < mSubNewsData.size(); i++) {
            TabPagerDetailItem tabPager = new TabPagerDetailItem(mActivity,
                    mSubNewsData.get(i));
            mTabPagerDetail.add(tabPager);
        }
        newsDetail_viewPager.setAdapter(new NewsDetailPagerAdapter(mActivity, mSubNewsData));
        indicator.setViewPager(newsDetail_viewPager);
        indicator.setOnPageChangeListener(this);
        imgBt_nextItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = newsDetail_viewPager.getCurrentItem();
                newsDetail_viewPager.setCurrentItem(++currentItem);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        //int touchModeAbove = slidingMenu.getTouchModeAbove();
        if(position == 0  ){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class NewsDetailPagerAdapter extends BasePagerAdapter {

        private NewsDetailPagerAdapter(Activity activity, List lists) {
            super(activity, lists);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mSubNewsData.get(position).title;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabPagerDetailItem tabDetailItem = mTabPagerDetail.get(position);
            container.addView(tabDetailItem.baseDetailView);
            tabDetailItem.initData();
            return tabDetailItem.baseDetailView;
        }
    }

}
