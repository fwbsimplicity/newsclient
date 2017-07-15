package com.shier.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/4/1  13:41
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch(ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                int moveY = (int) ev.getRawY();
                //右滑
                if(moveX - startX > 0) {
                    if(getCurrentItem() == 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }else {
                    if(getCurrentItem() == getAdapter().getCount() - 1){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }

                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
