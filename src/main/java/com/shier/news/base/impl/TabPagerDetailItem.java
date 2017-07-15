package com.shier.news.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shier.newsClient.R;
import com.shier.news.activity.NewsContentActivity;
import com.shier.news.adapter.BasePagerAdapter;
import com.shier.news.adapter.MyBaseAdapter;
import com.shier.news.base.BaseDetailItem;
import com.shier.news.domain.TabViewData;
import com.shier.news.domain.ViewData;
import com.shier.news.mode.DatasMode;
import com.shier.news.utils.ConstantUtils;
import com.shier.news.utils.SPUtils;
import com.shier.news.utils.T;
import com.shier.news.view.RentalsSunHeaderView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.shier.newsClient.R.id.iv_tabNews;
import static com.shier.newsClient.R.id.tv_newsDescription;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/4/10  17:30
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
class TabPagerDetailItem extends BaseDetailItem implements ViewPager.OnPageChangeListener {
    @ViewInject(R.id.lv_newsDetails)
    private ListView lv_newsDetails;
    @ViewInject(R.id.tabPagerDetail_viewPager)
    private ViewPager tabPagerDetail_viewPager;
    @ViewInject(R.id.tv_tabPagerDetailItem)
    private TextView tv_tabPagerDetailItem;
    @ViewInject(R.id.circleIndicator)
    private CirclePageIndicator circleIndicator;
    private ViewData.SubNewsData mSubNewsData;
    private ArrayList<TabViewData.News> mNews;
    private ArrayList<TabViewData.Topnews> mTopNews;
    private BitmapUtils bitmapUtils;
    private TabNewsDetailAdapter mTabNewsAdapter;
    private String mMoreUrl;
    private DatasMode mDataModel;
    private int refreshCount = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentItem = tabPagerDetail_viewPager.getCurrentItem();
            currentItem++;
            tabPagerDetail_viewPager.setCurrentItem(currentItem % mTopNews.size());
            mHandler.sendEmptyMessageDelayed(0, 5000);
        }
    };

    TabPagerDetailItem(Activity activity, ViewData.SubNewsData subNewsData) {
        super(activity);
        mSubNewsData = subNewsData;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.item_tab_pager_detail, null);
        final PtrFrameLayout frame = (PtrFrameLayout) view.findViewById(R.id.material_style_ptr_frame);
        // header
        final RentalsSunHeaderView header = new RentalsSunHeaderView(mActivity);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setUp(frame);

        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(1500);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        // frame.setPullToRefresh(true);
//        frame.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                frame.autoRefresh(true);
//            }
//        }, 100);

        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,lv_newsDetails, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                if(refreshCount == 0){
                    refreshCount++;
                    parseDataByGSon(mDataModel.getMoreDatas(),true);
                }else{
                    T.showShort(mActivity,"没有更多数据了");
                }

                long delay = 1500;
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, delay);
            }
        });
        View headerView = View.inflate(mActivity, R.layout.lv_headview_tab, null);
        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);
        lv_newsDetails.addHeaderView(headerView);
        initListener();
        return view;
    }

    @Override
    public void initData() {
        bitmapUtils = new BitmapUtils(mActivity);
        BmobQuery<DatasMode> query = new BmobQuery<>();
        query.getObject("RSMqJJJQ", new QueryListener<DatasMode>() {
            @Override
            public void done(DatasMode datasMode, BmobException e) {
                if(e == null) {
                    mDataModel = datasMode;
                    parseDataByGSon(datasMode.getPhotos(), false);
                } else {
                    Log.e("error", e.toString());
                }

            }
        });

    }

    private void initListener() {
//        lv_newsDetails.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getDataFromServer();
//            }
//
//            @Override
//            public void onLoadingMore() {
//                if(mMoreUrl != null) {
//                    getMoreDataFromServer();
//                } else {
//                    Toast.makeText(mActivity, "没有更多数据了!", Toast.LENGTH_SHORT).show();
//                    lv_newsDetails.onRefreshCompleted(false);
//                }
//
//            }
//        });
        lv_newsDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemId = mNews.get(position).id;
                String ids = SPUtils.getString(mActivity, "ids", "");
                if(!ids.contains(itemId)) {
                    ids += itemId + ";";
                    SPUtils.saveString(mActivity, "ids", ids);
                    changeViewItemReadState(view);
                }
                changeViewItemReadState(view);
                Intent intent = new Intent(mActivity, NewsContentActivity.class);
                intent.putExtra("url",mNews.get(position).url);
                mActivity.startActivity(intent);
            }
        });
    }

    private void changeViewItemReadState(View view) {
        TextView tv_newsDescription = (TextView) view.findViewById(R.id.tv_newsDescription);
        tv_newsDescription.setTextColor(Color.GRAY);
    }

    private void getDataFromServer() {
        String url = ConstantUtils.SERVER_URL + mSubNewsData.url;
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                final String result = (String) responseInfo.result;
                new Thread() {
                    @Override
                    public void run() {
                        parseDataByGSon(result, false);
                    }
                };
                //refresh_lv_tabPager.onRefreshCompleted(true);
                SPUtils.saveString(mActivity, ConstantUtils.SERVER_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //refresh_lv_tabPager.onRefreshCompleted(false);
            }
        });
    }

    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                String result = (String) responseInfo.result;
                parseDataByGSon(result, true);
                //refresh_lv_tabPager.onRefreshCompleted(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //refresh_lv_tabPager.onRefreshCompleted(false);
            }
        });
    }

    private synchronized void parseDataByGSon(String data, boolean isMore) {
        Gson gson = new Gson();
        TabViewData mTabViewData = gson.fromJson(data, TabViewData.class);
        if(!isMore) {
            mNews = mTabViewData.data.news;
            mTopNews = mTabViewData.data.topnews;
            if(mTopNews != null) {
                tabPagerDetail_viewPager.setAdapter(new TabPagerDetailPagerAdapter(mActivity,
                        mTopNews));
                circleIndicator.setViewPager(tabPagerDetail_viewPager);
                circleIndicator.setOnPageChangeListener(this);
                circleIndicator.setCurrentItem(0);
                tv_tabPagerDetailItem.setText(mTopNews.get(0).title);
                if(mNews != null) {
                    mTabNewsAdapter = new TabNewsDetailAdapter(mNews);
                    lv_newsDetails.setAdapter(mTabNewsAdapter);
                }
                mHandler.sendEmptyMessageDelayed(0, 5000);
            }

        } else {
            ArrayList<TabViewData.News> news = mTabViewData.data.news;
            //把数据添加到原来数据的后面
            mNews.addAll(0,news);
            mTabNewsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tv_tabPagerDetailItem.setText(mTopNews.get(position).title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class TabPagerDetailPagerAdapter extends BasePagerAdapter {

        private TabPagerDetailPagerAdapter(Activity activity, List lists) {
            super(activity, lists);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = new ImageView(mActivity);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, NewsContentActivity.class);
                    intent.putExtra("url", mTopNews.get(position).url);
                    mActivity.startActivity(intent);
                }
            });
            bitmapUtils.display(iv, mTopNews.get(position).topimage);
            container.addView(iv);
            return iv;
        }
    }

    private class TabNewsDetailAdapter extends MyBaseAdapter {

        private MyViewHolder mHolder;

        public TabNewsDetailAdapter(List lists) {
            super(lists);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mHolder = new MyViewHolder();
            if(convertView == null) {
                convertView = View.inflate(mActivity, R.layout.lv_tab_news_item, null);
                mHolder.iv_tabNews = (ImageView) convertView.findViewById(iv_tabNews);
                mHolder.tv_newsDescription = (TextView) convertView.findViewById(tv_newsDescription);
                mHolder.tv_pubDate = (TextView) convertView.findViewById(R.id.tv_pubDate);
                convertView.setTag(mHolder);
            } else {
                mHolder = (MyViewHolder) convertView.getTag();
            }
            TabViewData.News news = mNews.get(position);
            bitmapUtils.display(mHolder.iv_tabNews, news.listimage);
            mHolder.tv_newsDescription.setText(news.title);
            mHolder.tv_pubDate.setText(news.pubdate);
            return convertView;
        }

        class MyViewHolder {
            private ImageView iv_tabNews;
            private TextView tv_newsDescription;
            private TextView tv_pubDate;
        }
    }
}
