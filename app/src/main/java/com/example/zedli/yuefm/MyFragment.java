package com.example.zedli.yuefm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zedli.data.ArticleSharePreferences;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private int position;
    private TextView mTitleTextView,mSourceTextView;
    public TextView mBodyTextView;
    private LinearLayout mErrorLinear;
    private ProgressBar mProgressBar;
    private Button mRetryBtn;
    private RelativeLayout mPagerRootRelative;

    private ArticleSharePreferences mArticleSharePreferences;
    private TextSizeReceiveBroadCast mTextSizeReceiveBroadCast;
    private TextColorReceiveBroadCast mTextColorReceiveBroadCast;
    // TODO: Rename and change types of parameters

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment ViewPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(int num) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments() != null ? getArguments().getInt("num") : 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_pager, container, false);

        mPagerRootRelative = (RelativeLayout)view.findViewById(R.id.mPagerRootRelative);
        mTitleTextView = (TextView)view.findViewById(R.id.mTitle);
        mSourceTextView = (TextView)view.findViewById(R.id.mSource);
        mBodyTextView = (TextView)view.findViewById(R.id.mBody);
        mErrorLinear = (LinearLayout)view.findViewById(R.id.errorLinear);
        mProgressBar = (ProgressBar)view.findViewById(R.id.mProgressBar);
        mRetryBtn = (Button)view.findViewById(R.id.retryBtn);

        mProgressBar.setVisibility(View.VISIBLE);
        mErrorLinear.setVisibility(View.INVISIBLE);
        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mErrorLinear.setVisibility(View.INVISIBLE);
                initArticle();
            }
        });
        changeTextSizeBroadCast();
        changeTextColorBroadCast();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isVisible()) {
            Log.v("YueFM","initData1");
            initArticle(); //加载数据的方法
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getUserVisibleHint()) {

            initArticle();
        }
        super.onActivityCreated(savedInstanceState);
    }

    private void initArticle()
    {
        Log.v("YueFM","initData");
        ArticleHandler articleHandler = new ArticleHandler();
        new Thread(new HttpUtil("http://yue.fm/api/articles/random",articleHandler)).start();
        articleHandler.setOnGetResultHandlerListener(new onGetResultHandler());
    }

    private void showArticle(String bodyString,String titleString,String sourceString)
    {
        mBodyTextView.setText(Html.fromHtml(bodyString));
        mTitleTextView.setText(Html.fromHtml(titleString));
        mSourceTextView.setText(Html.fromHtml("来源："+sourceString));


        mArticleSharePreferences = new ArticleSharePreferences(getActivity());
        setTextSize(mArticleSharePreferences.getTextSizeFromSp("title"), mArticleSharePreferences.getTextSizeFromSp("source"), mArticleSharePreferences.getTextSizeFromSp("body"));
        setTextColor(mArticleSharePreferences.getTextColorFromSp());
    }

    private void setTextSize(int titleTextSize,int sourceTextSize,int bodyTextSize)
    {
        mTitleTextView.setTextSize(titleTextSize);
        mBodyTextView.setTextSize(bodyTextSize);
        mSourceTextView.setTextSize(sourceTextSize);
    }

    private void setTextColor(boolean colorFlag)
    {
        //colorFlag 为true表示夜间模式
        if(colorFlag)
        {
            mPagerRootRelative.setBackgroundColor(getResources().getColor(R.color.background_color_night));
            mTitleTextView.setTextColor(getResources().getColor(R.color.title_color_night));
            mSourceTextView.setTextColor(getResources().getColor(R.color.source_color_night));
            mBodyTextView.setTextColor(getResources().getColor(R.color.body_color_night));
        }else
        {
            mPagerRootRelative.setBackgroundColor(getResources().getColor(R.color.background_color_day));
            mTitleTextView.setTextColor(getResources().getColor(R.color.title_color_day));
            mSourceTextView.setTextColor(getResources().getColor(R.color.source_color_day));
            mBodyTextView.setTextColor(getResources().getColor(R.color.body_color_day));
        }


    }
    private class onGetResultHandler implements ArticleHandler.OnGetResultHandler
    {

        @Override
        public void getResultSuccess(String result) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mErrorLinear.setVisibility(View.INVISIBLE);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String bodyString = jsonObject.getString("body");
                String titleString = jsonObject.getString("title");
                String sourceString = jsonObject.getString("source");

               showArticle(bodyString,titleString,sourceString);
            } catch (JSONException e) {
                e.printStackTrace();
                getResultFailed();
            }


        }

        @Override
        public void getResultFailed() {
            mProgressBar.setVisibility(View.INVISIBLE);
            mErrorLinear.setVisibility(View.VISIBLE);

        }
    }

    private void changeTextSizeBroadCast()
    {
        // 注册广播接收
        mTextSizeReceiveBroadCast = new TextSizeReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("textsize");    //只有持有相同的action的接受者才能接收此广播
        getActivity().registerReceiver(mTextSizeReceiveBroadCast, filter);
    }



    public class TextSizeReceiveBroadCast extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            //得到广播中得到的数据，并显示出来
            int titleTextSize = intent.getIntExtra("titleTextSize",30);
            int sourceTextSize = intent.getIntExtra("sourceTextSize",15);
            int bodyTextSize = intent.getIntExtra("bodyTextSize",17);
            setTextSize(titleTextSize, sourceTextSize, bodyTextSize);
//            mBodyTextView.setTextSize(18);
            Log.v("YueFM","bodyTextSize:"+bodyTextSize);
        }
    }

    private void changeTextColorBroadCast()
    {
        mTextColorReceiveBroadCast = new TextColorReceiveBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("textColor");
        getActivity().registerReceiver(mTextColorReceiveBroadCast,intentFilter);
    }
    public class TextColorReceiveBroadCast extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean colorFlag = intent.getBooleanExtra("colorFlag",false);
            setTextColor(colorFlag);
            Log.v("YueFM","colorFlag:"+colorFlag);
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mTextSizeReceiveBroadCast);
        getActivity().unregisterReceiver(mTextColorReceiveBroadCast);
        super.onDestroy();
    }
}
