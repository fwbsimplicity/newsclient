package com.shier.news.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.shier.news.adapter.MyBaseAdapter;
import com.shier.news.base.BaseDetailItem;
import com.shier.news.domain.PhotosData;
import com.shier.news.utils.ConstantUtils;
import com.shier.news.utils.SPUtils;

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
class GroupPhotoBehindDetailItem extends BaseDetailItem {
    @ViewInject(R.id.lv_photoGroup)
    private ListView lv_photoGroup;
    @ViewInject(R.id.gv_photoGroup)
    private GridView gv_photoGroup;
    private ImageButton imgBtn;
    private boolean isListViewDisplay = true;

    GroupPhotoBehindDetailItem(Activity activity, ImageButton imgBtn) {
        super(activity);
        this.imgBtn = imgBtn;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.item_group_photo_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        String cacheData = SPUtils.getString(mActivity, ConstantUtils.PHOTOS_URL, null);
        if(!TextUtils.isEmpty(cacheData)) {
            parseDataByGSon(cacheData);
        }
        getDataFromServer();
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDisplay();
            }
        });
    }

    private void getDataFromServer() {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                ConstantUtils.PHOTOS_URL,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        parseDataByGSon(result);
                        SPUtils.saveString(mActivity, ConstantUtils.PHOTOS_URL, result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.e("NewsCenterPager", error.toString());
                    }
                });
    }

    private void parseDataByGSon(String data) {
        Gson gson = new Gson();
        PhotosData photosData = gson.fromJson(data, PhotosData.class);
        ArrayList<PhotosData.NewsInfo> mNews = photosData.data.news;
        if(mNews != null) {
            GroupPhotoAdapter groupPhotoAdapter = new GroupPhotoAdapter(mNews);
            lv_photoGroup.setAdapter(groupPhotoAdapter);
            gv_photoGroup.setAdapter(groupPhotoAdapter);
        }
    }

    private void changeDisplay() {
        if(isListViewDisplay) {
            lv_photoGroup.setVisibility(View.INVISIBLE);
            gv_photoGroup.setVisibility(View.VISIBLE);
            imgBtn.setImageResource(R.mipmap.icon_pic_grid_type);
        } else {
            lv_photoGroup.setVisibility(View.VISIBLE);
            gv_photoGroup.setVisibility(View.VISIBLE);
            imgBtn.setImageResource(R.mipmap.icon_pic_list_type);
        }
        isListViewDisplay = !isListViewDisplay;
    }

   private class GroupPhotoAdapter extends MyBaseAdapter {

        private BitmapUtils bitmapUtils;

        private GroupPhotoAdapter(List lists) {
            super(lists);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            bitmapUtils = new BitmapUtils(mActivity);
            MyHolder holder;
            if(convertView == null) {
                convertView = View.inflate(mActivity, R.layout.item_group_photo_adapter, null);
                holder = new MyHolder();
                holder.iv_photoDisplay = (ImageView) convertView.findViewById(R.id.iv_displayPhoto);
                holder.tv_photoDesc = (TextView) convertView.findViewById(R.id.tv_photoDesc);
                convertView.setTag(holder);
            } else {
                holder = (MyHolder) convertView.getTag();
                PhotosData.NewsInfo newsInfo = (PhotosData.NewsInfo) getItem(position);
                bitmapUtils.display(holder.iv_photoDisplay, newsInfo.listimage);
                holder.tv_photoDesc.setText(newsInfo.title);
            }
            return convertView;
        }

        class MyHolder {
            ImageView iv_photoDisplay;
            TextView tv_photoDesc;
        }
    }

}
