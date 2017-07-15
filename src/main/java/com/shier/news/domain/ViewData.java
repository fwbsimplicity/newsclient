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
 * 创 建日期 ： 2017/4/6  14:15
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class ViewData {

      public ArrayList<NewsData> data;

      public class NewsData {
          public String id;
          public String title;
          public ArrayList<SubNewsData> children;
      }

     public class SubNewsData {
         public String id;
         public String title;
         public String url;
     }

}
