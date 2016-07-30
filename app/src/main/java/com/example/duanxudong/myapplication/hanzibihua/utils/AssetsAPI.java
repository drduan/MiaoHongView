package com.example.duanxudong.myapplication.hanzibihua.utils;
 

import android.content.Context;

/**
 * 读取asses文件
 *
 * @author freePC
 *
 */
public class AssetsAPI {
    private static AssetsAPI instance = null;
    FileUtils file;

    public AssetsAPI() {}

    public static AssetsAPI getInstance() {
        if (instance == null) {
            instance = new AssetsAPI();
        }

        return instance;
    }

    public String getFontJson(Context context, String fileName) {
    	String result=null;
        try {
            result  = new FileUtils().getAssets(context,fileName);
            
        } catch (Exception e) {
        }

        return result;
    }
    
    public String getFromAssets(Context context, String fileName){
    	String result=null;
        try {
            result  = new FileUtils().getFromAssets(context,fileName);
            
        } catch (Exception e) {
        }

        return result;
    }
} 
