package com.shier.news.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shier.newsClient.R;
import com.shier.news.utils.T;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/4/23  10:16
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
@SuppressWarnings("deprecation")
public class NewsContentActivity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.webView_newsContent)
    private WebView webView_newsContent;
    @ViewInject(R.id.pb_newsContent)
    private ProgressBar pb_newsContent;
    @ViewInject(R.id.imgBt_back)
    private ImageButton imgBt_back;
    @ViewInject(R.id.imgBt_share)
    private ImageButton imgBt_share;
    @ViewInject(R.id.imgBt_textSize)
    private ImageButton imgBt_textSize;
    private WebSettings webSettings;
    private int mCurrentTextSize = 0;
    private int mChooseItem = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        initData();
        initListener();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            T.showShort(NewsContentActivity.this,platform + " 分享成功啦");

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            T.showShort(NewsContentActivity.this,platform + " 分享失败啦");
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            T.showShort(NewsContentActivity.this,platform + " 分享取消啦");
        }
    };
    private void initListener() {
        imgBt_back.setOnClickListener(this);
        imgBt_share.setOnClickListener(this);
        imgBt_textSize.setOnClickListener(this);
    }

    private void initData() {
        ViewUtils.inject(this);
        webSettings = webView_newsContent.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(true);//显示放大放小按钮
        webSettings.setUseWideViewPort(true);//双击缩放
        String url = (String) getIntent().getExtras().get("url");
        webView_newsContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // pb_newsContent.setVisibility(View.GONE);
            }
        });
        webView_newsContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb_newsContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_newsContent.setVisibility(View.GONE);
            }

        });
        webView_newsContent.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imgBt_back:
                finish();
                break;
            case R.id.imgBt_share:
                showShare();
                break;
            case R.id.imgBt_textSize:
                showTextSizeChoice();
                break;
        }
    }

    private void showTextSizeChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置字体");
        builder.create().getWindow().setBackgroundDrawableResource(R.color.white);
        String[] items = new String[]{"超大号", "大号", "正常", "小号",
                "超小号"};
        builder.setSingleChoiceItems(items, mCurrentTextSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(mChooseItem) {
                    case 0:
                        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        webSettings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentTextSize = mChooseItem;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showShare() {

        new ShareAction(this).withText("hello")
                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
