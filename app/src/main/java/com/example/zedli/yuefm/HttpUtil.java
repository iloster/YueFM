package com.example.zedli.yuefm;

import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by ZedLi on 2015/3/2.
 */
public class HttpUtil implements Runnable
{

    private String path;
    private Handler handler;
    public HttpUtil(String path, Handler handler)
    {
        this.path = path;
        this.handler = handler;
    }
    private String requestByGet(String path)
    {
        String result = null;
        HttpGet httpGet = new HttpGet(path);
        //获取httpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200)
            {
                result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            }
            else
            {
                result = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }
    @Override
    public void run() {
        String result = requestByGet(path);
        if(result!=null)
        {
            Message msg = Message.obtain();
            msg.obj = result;
            msg.what = 1;
            handler.sendMessage(msg);
        }
        else
        {
            Message msg = Message.obtain();

            msg.what = 0;
            handler.sendMessage(msg);
        }
    }
}
