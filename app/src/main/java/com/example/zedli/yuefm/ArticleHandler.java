package com.example.zedli.yuefm;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZedLi on 2015/3/3.
 */
public class ArticleHandler extends Handler
{
    private OnGetResultHandler mOnGetResultHandler;


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what == 1)
        {
            String result = (String)msg.obj;
            Log.v("YueFM", "result:" + result);
            JSONObject jsonObj = null;
            try {

                jsonObj = new JSONObject(result);
                String bodyString = jsonObj.getString("body");
                mOnGetResultHandler.getResultSuccess(bodyString);
            } catch (JSONException e) {
                e.printStackTrace();
                mOnGetResultHandler.getResultFailed();
            }
        }
        else
        {
            mOnGetResultHandler.getResultFailed();
        }
    }

    public interface OnGetResultHandler
    {
        void getResultSuccess(String str);
        void getResultFailed();

    }

    public OnGetResultHandler getOnGetResultHandlerListener() {
        return mOnGetResultHandler;
    }

    public void setOnGetResultHandlerListener(OnGetResultHandler mOnGetResultHandler) {
        this.mOnGetResultHandler = mOnGetResultHandler;
    }
}

