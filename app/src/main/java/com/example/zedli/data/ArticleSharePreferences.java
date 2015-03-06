package com.example.zedli.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by ZedLi on 2015/3/5.
 */
public class ArticleSharePreferences {

    private Context context;
    private HashMap defaultSizeHashMap = new HashMap();

    public ArticleSharePreferences(Context context)
    {
        this.context = context;
        initHashMap();
    }
    private void initHashMap()
    {
        defaultSizeHashMap.put("body",17);
        defaultSizeHashMap.put("title",30);
        defaultSizeHashMap.put("source",15);
    }
    public int getTextSizeFromSp(String str)
    {
        SharedPreferences sp = context.getSharedPreferences(str, Activity.MODE_PRIVATE);

        int textSize = sp.getInt("textsize",(int)defaultSizeHashMap.get(str));
        return textSize;
    }

    public void setTextSizeToSp(String str,int textSize)
    {
        SharedPreferences sp = context.getSharedPreferences(str, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("textsize",textSize);
        editor.commit();
    }

    public void setTextColorToSp(boolean colorFlag)
    {
        SharedPreferences sp = context.getSharedPreferences("COLOR", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("color",colorFlag);
        editor.commit();
    }
    public boolean getTextColorFromSp()
    {
        SharedPreferences sp = context.getSharedPreferences("COLOR", Activity.MODE_PRIVATE);

        boolean colorFlag = sp.getBoolean("color",false);
        return colorFlag;
    }
}
