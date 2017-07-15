package com.shier.news.fragment;


import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shier.newsClient.R;
import com.shier.news.adapter.BasePagerAdapter;
import com.shier.news.base.BasePagerItem;
import com.shier.news.base.impl.GovernmentAffairsPagerItem;
import com.shier.news.base.impl.HomePagerItem;
import com.shier.news.base.impl.NewsCenterPagerItem;
import com.shier.news.base.impl.SettingPagerItem;
import com.shier.news.base.impl.SmartServicePagerItem;
import com.shier.news.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends BaseFragment {

    @ViewInject(R.id.radioGroup)
    private RadioGroup radioGroup;
    @ViewInject(R.id.mainFragment_viewPager)
    private NoScrollViewPager mainFragment_viewPager;
    private ArrayList<BasePagerItem> al_items;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_main, null);
        ViewUtils.inject(this, view);//注入view和事件,在Fragment中注入使用这个方法
        return view;
    }


    @Override
    public void initData() {
        //默认选中首页
        radioGroup.check(R.id.rb_home);
        al_items = new ArrayList<>();
        al_items.add(new HomePagerItem(mActivity));
        al_items.add(new NewsCenterPagerItem(mActivity));
        al_items.add(new SmartServicePagerItem(mActivity));
        al_items.add(new GovernmentAffairsPagerItem(mActivity));
        al_items.add(new SettingPagerItem(mActivity));
        mainFragment_viewPager.setAdapter(new MainFragmentPagerAdapter(mActivity, al_items));
        initListener();
    }

    @SuppressWarnings("deprecation")
    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rb_home:
                        mainFragment_viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_newscenter:
                        mainFragment_viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smartservice:
                        mainFragment_viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_govaffair:
                        mainFragment_viewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mainFragment_viewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });
        mainFragment_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BasePagerItem basePagerItem = al_items.get(position);
                boolean enabled = position!= 0 && position!=4;
                basePagerItem.setBehindViewEnabled(enabled);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MainFragmentPagerAdapter extends BasePagerAdapter {

        public MainFragmentPagerAdapter(Activity activity, List lists) {
            super(activity, lists);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePagerItem basePagerItem = al_items.get(position);
            basePagerItem.initData();
            container.addView(basePagerItem.basePagerView);
            return basePagerItem.basePagerView;

        }
    }

    public NewsCenterPagerItem getNewsCenterPagerItem(){
        return (NewsCenterPagerItem) al_items.get(1);
    }
}
