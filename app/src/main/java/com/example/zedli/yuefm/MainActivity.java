package com.example.zedli.yuefm;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;


public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
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
    }

    private void init()
    {
        FragmentManager fm = getSupportFragmentManager();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(fm);
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
        AlertDialog dialog = new AlertDialog.Builder(this).
                                setView(LayoutInflater.from(this).inflate(R.layout.setting,null)).
                                create();
        dialog.show();
    }
}