package com.example.zedli.yuefm;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zedli.data.ArticleSharePreferences;
import com.example.zedli.widget.Switch;


public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private  MyFragmentPagerAdapter myFragmentPagerAdapter;

    private LinearLayout mSettingRootLinear;
    private RelativeLayout mRootRelative;
    private Button mSmallBtn;
    private Button mBigBtn;
    private TextView mTextSize,mNightLabel,mTextSizeLabel;
    private Switch mNightSwitch;
    private ArticleSharePreferences mArticleSharePreferences = new ArticleSharePreferences(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        findViews();
        init();
    }

    private void findViews()
    {
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        mRootRelative = (RelativeLayout)findViewById(R.id.mRootRelative);
    }

    private void init()
    {
        FragmentManager fm = getSupportFragmentManager();
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fm);
        mViewPager.setAdapter(myFragmentPagerAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode)
        {
            case KeyEvent.KEYCODE_MENU:showSetting();break;
            case KeyEvent.KEYCODE_BACK:finish();break;
        }

        return true;
    }

    private void showSetting()
    {
        View settingView = LayoutInflater.from(this).inflate(R.layout.setting,null);
        AlertDialog dialog = new AlertDialog.Builder(this).
                                setView(settingView).
                                create();
        dialog.show();

        mSettingRootLinear = (LinearLayout)settingView.findViewById(R.id.mSettingRootLinear);
        mNightLabel = (TextView)settingView.findViewById(R.id.mNightLabel);
        mTextSizeLabel = (TextView)settingView.findViewById(R.id.mTextSizeLabel);
        mSmallBtn = (Button)settingView.findViewById(R.id.mSmallBtn);
        mBigBtn = (Button)settingView.findViewById(R.id.mBigBtn);
        mTextSize = (TextView)settingView.findViewById(R.id.mTextSize);
        mNightSwitch = (Switch)settingView.findViewById(R.id.mNightSwitch);

        mTextSize.setText(mArticleSharePreferences.getTextSizeFromSp("body")+"号");
        mNightSwitch.setChecked(mArticleSharePreferences.getTextColorFromSp());
        setTextColor(mArticleSharePreferences.getTextColorFromSp());

        mSmallBtn.setOnClickListener(new onSmallBtnClick());
        mBigBtn.setOnClickListener(new onBigBtnClick());
        mNightSwitch.setOnCheckedChangeListener(new onNightSwitchClick());
    }

    private class onNightSwitchClick implements CompoundButton.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("YueFM","onNightSwitchClick:"+isChecked);
                mArticleSharePreferences.setTextColorToSp(isChecked);
                setTextColor(isChecked);
                Intent intent = new Intent();
                intent.putExtra("colorFlag",isChecked);
                intent.setAction("textColor");
                sendBroadcast(intent);
        }
    }

    private void setTextColor(boolean colorFlag)
    {
        //true:为夜间模式 false:正常模式
        if(colorFlag)
        {
            mRootRelative.setBackgroundColor(getResources().getColor(R.color.body_color_night));
            mSettingRootLinear.setBackgroundColor(getResources().getColor(R.color.background_color_night));
            mNightLabel.setTextColor(getResources().getColor(R.color.body_color_night));
            mTextSizeLabel.setTextColor(getResources().getColor(R.color.body_color_night));
            mTextSize.setTextColor(getResources().getColor(R.color.body_color_night));
        }else
        {
            mRootRelative.setBackgroundColor(getResources().getColor(R.color.body_color_day));
            mSettingRootLinear.setBackgroundColor(getResources().getColor(R.color.background_color_day));
            mNightLabel.setTextColor(getResources().getColor(R.color.body_color_day));
            mTextSizeLabel.setTextColor(getResources().getColor(R.color.body_color_day));
            mTextSize.setTextColor(getResources().getColor(R.color.body_color_day));
        }
    }

    private class onSmallBtnClick implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            Log.v("YueFM","onSmallBtnClick");
            changeTextSize(-1);
        }
    }

    private class onBigBtnClick implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            Log.v("YueFM","onBigBtnClick");

//            Intent intent = new Intent();  //Itent就是我们要发送的内容
//            int titleTextSize =  articleTextSizeSp.getTextSizeFromSp("title");
//            int bodyTextSize =  articleTextSizeSp.getTextSizeFromSp("body");
//            int sourceTextSize =  articleTextSizeSp.getTextSizeFromSp("source");
//            articleTextSizeSp.setTextSizeToSp("title",titleTextSize+1);
//            articleTextSizeSp.setTextSizeToSp("body",bodyTextSize+1);
//            articleTextSizeSp.setTextSizeToSp("source",sourceTextSize+1);
//            intent.putExtra("titleTextSize", titleTextSize+1);
//            intent.putExtra("bodyTextSize", bodyTextSize+1);
//            intent.putExtra("sourceTextSize", sourceTextSize+1);
//            intent.setAction("textsize");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
//            sendBroadcast(intent);   //发送广播
            changeTextSize(1);
        }
    }

    private void changeTextSize(int size)
    {
        Intent intent = new Intent();  //Itent就是我们要发送的内容
        int titleTextSize =  mArticleSharePreferences.getTextSizeFromSp("title");
        int bodyTextSize =  mArticleSharePreferences.getTextSizeFromSp("body");
        int sourceTextSize =  mArticleSharePreferences.getTextSizeFromSp("source");
        mArticleSharePreferences.setTextSizeToSp("title",titleTextSize + size);
        mArticleSharePreferences.setTextSizeToSp("body",bodyTextSize + size);
        mArticleSharePreferences.setTextSizeToSp("source",sourceTextSize + size);
        if(bodyTextSize +size ==17)
            mTextSize.setText("默认");
        else
            mTextSize.setText(bodyTextSize +size +"号");
        intent.putExtra("titleTextSize", titleTextSize + size);
        intent.putExtra("bodyTextSize", bodyTextSize + size);
        intent.putExtra("sourceTextSize", sourceTextSize + size);
        intent.setAction("textsize");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
        sendBroadcast(intent);   //发送广播
    }

}