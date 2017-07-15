package com.shier.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shier.newsClient.R;
import com.shier.news.adapter.BasePagerAdapter;

import java.util.Arrays;
import java.util.List;


public class GuideActivity extends Activity {

    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;
    @ViewInject(R.id.bt_start)
    private Button bt_start;
    @ViewInject(R.id.redDot)
    private View redDot;
    @ViewInject(R.id.ll_dot)
    private LinearLayout ll_dot;
    private Integer[] imgIds;
    private int interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_guide);
        ViewUtils.inject(this);
        imgIds = new Integer[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    }

    @SuppressWarnings("deprecation")
    private void initData() {
        GuidePagerAdapter adapter = new GuidePagerAdapter(this, Arrays.asList(imgIds));
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });
        ll_dot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_dot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                interval = ll_dot.getChildAt(1).getLeft() - ll_dot.getChildAt(0).getLeft();
            }
        });
    }

    class GuidePagerAdapter extends BasePagerAdapter {

        public GuidePagerAdapter(Activity activity, List lists) {
            super(activity, lists);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(GuideActivity.this, R.layout.adapter_guide_viewpager, null);
            ImageView iv_guide = (ImageView) view.findViewById(R.id.iv_guide);
            iv_guide.setBackgroundResource(imgIds[position]);
            container.addView(view);
            return view;
        }

          }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               int s = (int) (interval * positionOffset + position * interval);
//             System.out.println("当前位置:" + position + ";百分比:" + positionOffset
//             + ";移动距离:" + positionOffsetPixels);
            RelativeLayout.LayoutParams params  = (RelativeLayout.LayoutParams) redDot.getLayoutParams();
            params.leftMargin = s;
            redDot.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
            if(position == 2){
                bt_start.setVisibility(View.VISIBLE);
            }else {
                bt_start.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
