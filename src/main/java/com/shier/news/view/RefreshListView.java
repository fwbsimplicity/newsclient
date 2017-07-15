package com.shier.news.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shier.newsClient.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/4/20  23:38
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener
        , AdapterView.OnItemClickListener {

    private static final int PULL_REFRESH = 0;
    private static final int RELEASE_REFRESH = 1;
    private static final int REFRESHING = 2;
    private static int mCurrentRefreshState = PULL_REFRESH;
    public TextView tv_refresh_state;
    public TextView tv_refresh_time;
    private View mHeadView;
    private int mHeadViewMeasuredHeight;
    private ImageView iv_arrow;
    private RotateAnimation arrowUpAnim;
    private RotateAnimation arrowDownAnim;
    private ProgressBar pb_refresh;

    private int mFooterViewMeasuredHeight;
    private View mRefreshFooterView;
    private OnRefreshListener mListener;
    private OnItemClickListener mItemClickListener;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(mListener != null ) {
                if(isUpToLastPosition){
                    mListener.onLoadingMore();
                }else {
                    mListener.onRefresh();
                }
            }
        }
    };
    private boolean isUpToLastPosition;

    public RefreshListView(Context context) {
        super(context);
        initHeadView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeadView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeadView();
        initFooterView();
    }

    private void initHeadView() {
        mHeadView = inflate(getContext(), R.layout.refresh_head_view, null);
        iv_arrow = (ImageView) mHeadView.findViewById(R.id.iv_arrow);
        pb_refresh = (ProgressBar) mHeadView.findViewById(R.id.pb_refresh);
        tv_refresh_state = (TextView) mHeadView.findViewById(R.id.tv_refresh_state);
        tv_refresh_time = (TextView) mHeadView.findViewById(R.id.tv_refresh_time);
        mHeadView.measure(0, 0);
        mHeadViewMeasuredHeight = mHeadView.getMeasuredHeight();
        addHeaderView(mHeadView);
        mHeadView.setPadding(0, -mHeadViewMeasuredHeight, 0, 0);
        tv_refresh_time.setText("上次刷新时间:" + getCurrentTime());
        initAnimation();
    }

    private void initFooterView() {
        mRefreshFooterView = inflate(getContext(), R.layout.refresh_footer_view, null);
        mRefreshFooterView.measure(0, 0);
        mFooterViewMeasuredHeight = mRefreshFooterView.getMeasuredHeight();
        addFooterView(mRefreshFooterView);
        mRefreshFooterView.setPadding(0, -mFooterViewMeasuredHeight, 0, 0);
        setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
         int startY = -1;
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(startY == -1) {
                    startY = (int) ev.getRawY();
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;
                int paddingTop = dy - mHeadViewMeasuredHeight;
                //向下滑
                if(dy > 0 && getFirstVisiblePosition() == 0) {
                    mHeadView.setPadding(0, paddingTop, 0, 0);
                    if(paddingTop < 0 && mCurrentRefreshState != PULL_REFRESH) {
                        mCurrentRefreshState = PULL_REFRESH;
                        refresh();
                    }
                    //mHeadView全部加载出来
                    else if(paddingTop >= 0 && mCurrentRefreshState == PULL_REFRESH) {
                        mCurrentRefreshState = RELEASE_REFRESH;
                        refresh();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if(mCurrentRefreshState == RELEASE_REFRESH) {
                    mCurrentRefreshState = REFRESHING;
                    mHeadView.setPadding(0, 0, 0, 0);
                    refresh();
                } else {
                    mCurrentRefreshState = PULL_REFRESH;
                    mHeadView.setPadding(0, -mHeadViewMeasuredHeight, 0, 0);
                    refresh();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refresh() {
        switch(mCurrentRefreshState) {
            case PULL_REFRESH:
                tv_refresh_state.setText("下拉刷新");
                iv_arrow.setVisibility(VISIBLE);
                pb_refresh.setVisibility(INVISIBLE);
                iv_arrow.startAnimation(arrowDownAnim);
                break;
            case RELEASE_REFRESH:
                tv_refresh_state.setText("释放刷新");
                iv_arrow.startAnimation(arrowUpAnim);
                break;
            case REFRESHING:
                tv_refresh_state.setText("正在刷新");
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(INVISIBLE);
                pb_refresh.setVisibility(VISIBLE);
                mHandler.sendEmptyMessageDelayed(0, 2000);
                break;
        }
    }

    private void initAnimation() {
        arrowUpAnim = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        arrowDownAnim = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        arrowUpAnim.setDuration(1000);
        arrowUpAnim.setFillAfter(true);
        arrowDownAnim.setDuration(1000);
        arrowDownAnim.setFillAfter(true);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public void onRefreshCompleted(boolean requestResult) {
        if(isUpToLastPosition) {
            isUpToLastPosition = !isUpToLastPosition;
            mRefreshFooterView.setPadding(0, -mFooterViewMeasuredHeight, 0, 0);
        } else {
            tv_refresh_state.setText("下拉刷新");
            iv_arrow.setVisibility(VISIBLE);
            pb_refresh.setVisibility(INVISIBLE);
            mCurrentRefreshState = PULL_REFRESH;
            mHeadView.setPadding(0, -mHeadViewMeasuredHeight, 0, 0);
            if(requestResult) {
                tv_refresh_time.setText("最后刷新时间:" + getCurrentTime());
            }
        }

    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_IDLE) {
            if(getLastVisiblePosition() == getCount() - 1 && !isUpToLastPosition) {
                mRefreshFooterView.setPadding(0, 0, 0, 0);
                setSelection(getCount());
                isUpToLastPosition = !isUpToLastPosition;
                mHandler.sendEmptyMessageDelayed(0,2000);
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mItemClickListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        //底层会调用传进去的listener的onItemClick()方法
        super.setOnItemClickListener(this);
        mItemClickListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadingMore();
    }
}
