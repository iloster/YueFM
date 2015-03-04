package com.example.zedli.yuefm;

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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private int position;
    private TextView mTitleTextView,mSourceTextView,mBodyTextView;
    private LinearLayout mErrorLinear;
    private ProgressBar mProgressBar;
    private Button mRetryBtn;
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

                mBodyTextView.setText(Html.fromHtml(bodyString));
                mTitleTextView.setText(Html.fromHtml(titleString));
                mSourceTextView.setText(Html.fromHtml("来源："+sourceString));
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
}
