package com.shier.news.fragment;


import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shier.newsClient.R;
import com.shier.news.activity.MainActivity;
import com.shier.news.adapter.MyBaseAdapter;
import com.shier.news.base.impl.NewsCenterPagerItem;
import com.shier.news.domain.ViewData;
import com.shier.news.utils.ConstantUtils;

import java.util.ArrayList;
import java.util.List;


public class BehindFragment extends BaseFragment {

    private int mCurrentPos = 0;
    private ArrayList<ViewData.NewsData> mNewsData;
    private ListView lv_behindFragment;
    private BehindFragmentAdapter adapter;
    private MainActivity mainActivity;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_behind, null);
        lv_behindFragment = (ListView) view.findViewById(R.id.lv_behindFragment);
        mainActivity = (MainActivity) mActivity;
        initListener();
        return view;
    }

    private void initListener() {
        lv_behindFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                adapter.notifyDataSetChanged();
                mainActivity.toggle();
                setCurrentDetailItem(position);
            }
        });
    }

    public void setBehindFragmentData(ViewData newsData) {
        mNewsData = newsData.data;
        adapter = new BehindFragmentAdapter(mNewsData);
        lv_behindFragment.setAdapter(adapter);
    }

    private void setCurrentDetailItem(int position) {
        MainFragment mainFragment = (MainFragment) mainActivity.getFragmentByTag(ConstantUtils.MAIN_FRAGMENT_TAG);
        NewsCenterPagerItem pagerItem = mainFragment.getNewsCenterPagerItem();
        pagerItem.setCurrentDetailItem(position);
    }


    class BehindFragmentAdapter extends MyBaseAdapter {

        public BehindFragmentAdapter(List lists) {
            super(lists);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(mActivity, R.layout.item_fragment_behind, null);
            TextView tv_tabTitle = (TextView) view.findViewById(R.id.tv_tabTitle);
            ViewData.NewsData behindViewData = mNewsData.get(position);
            tv_tabTitle.setText(behindViewData.title);
            //当前绘制的View被选中就设置为可用(颜色变为红色)
            tv_tabTitle.setEnabled(mCurrentPos == position);
            return view;
        }
    }
}
