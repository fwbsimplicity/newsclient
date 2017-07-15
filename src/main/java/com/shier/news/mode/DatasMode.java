package com.shier.news.mode;

import cn.bmob.v3.BmobObject;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/6/2  11:17
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class DatasMode extends BmobObject {

    private String categories;
    private String photos;
    private String moreDatas;

    public String getCategories() {
        return categories;
    }


    public String getPhotos() {
        return photos;
    }

    public String getMoreDatas() {
        return moreDatas;
    }
}
