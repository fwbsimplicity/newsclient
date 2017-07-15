package com.shier.news.domain;

import java.util.ArrayList;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/4/10  17:46
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class TabViewData {
    public TabNewsData data;

    public class TabNewsData {
        public ArrayList<News> news;
        public ArrayList<Topnews> topnews;
    }

    public class News {
        public String title;
        public String id;
        public String listimage;
        public String url;
        public String pubdate;
    }


    public class Topnews {
        public String title;
        public String id;
        public String topimage;
        public String url;
    }
}
