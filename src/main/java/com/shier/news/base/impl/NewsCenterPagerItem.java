package com.shier.news.base.impl;

import android.app.Activity;
import android.view.View;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.shier.news.activity.MainActivity;
import com.shier.news.base.BaseDetailItem;
import com.shier.news.base.BasePagerItem;
import com.shier.news.domain.ViewData;
import com.shier.news.fragment.BehindFragment;
import com.shier.news.mode.DatasMode;
import com.shier.news.utils.ConstantUtils;
import com.shier.news.utils.SPUtils;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/3/31  11:48
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class NewsCenterPagerItem extends BasePagerItem {

    private ArrayList<BaseDetailItem> detailItems;
    private ViewData mNewsData;

    public NewsCenterPagerItem(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        BmobQuery<DatasMode> query = new BmobQuery<>();
        query.getObject("RSMqJJJQ", new QueryListener<DatasMode>() {
            @Override
            public void done(DatasMode datasMode, BmobException e) {
                if(e == null){
                    parseDataByGSon(datasMode.getCategories());

                }

            }
        });

         }


    private void getDataFromServer() {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                ConstantUtils.CATEGORIES_URL,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        parseDataByGSon(result);
                        SPUtils.saveString(mActivity,ConstantUtils.CATEGORIES_URL,result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }
                });
    }

    private void parseDataByGSon(String data) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(data, ViewData.class);
        MainActivity mainActivity = (MainActivity) mActivity;
        BehindFragment fragment = (BehindFragment) mainActivity.getFragmentByTag(ConstantUtils.BEHIND_FRAGMENT_TAG);
        fragment.setBehindFragmentData(mNewsData);
        detailItems = new ArrayList<>();
        detailItems.add(new NewsBehindDetailItem(mainActivity,mNewsData.data.get(0).children));
        detailItems.add(new TopicBehindDetailItem(mActivity));
        detailItems.add(new GroupPhotoBehindDetailItem(mActivity,imgBt_ic_lv));
        detailItems.add(new InterActionBehindDetailItem(mActivity));
        setCurrentDetailItem(0);
    }

    public void setCurrentDetailItem(int position) {
        if(position == 2){
            imgBt_ic_lv.setVisibility(View.VISIBLE);
        }else {
            imgBt_ic_lv.setVisibility(View.INVISIBLE);
        }
        BaseDetailItem detailItem = detailItems.get(position);
        fl_basePageItem.removeAllViews();
        ViewData.NewsData behindViewData = mNewsData.data.get(position);
        fl_basePageItem.addView(detailItem.baseDetailView);
        tv_title.setText(behindViewData.title);
        detailItem.initData();
    }
}
