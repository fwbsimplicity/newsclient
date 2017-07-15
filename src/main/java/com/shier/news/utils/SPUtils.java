package com.shier.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ============================================================
 * <p/>
 * 版     权 ： 石耳集团版权所有(c) 2017
 * <p/>
 * 作     者  :  樊文斌
 * <p/>
 * 版     本 ： 1.0
 * <p/>
 * 创 建日期 ： 2017/3/28  14:16
 * <p/>
 * 描     述 ：
 * <p/>
 * <p/>
 * 修 订 历史：
 * <p/>
 * ============================================================
 */
public class SPUtils {

    public static void saveBoolean (Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context,String key,boolean defaultValue){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean result = sp.getBoolean(key, defaultValue);
        return result;
    }

    public static void saveString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static String getString(Context context,String key,String defaultValue){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String result = sp.getString(key, defaultValue);
        return result;
    }
}
