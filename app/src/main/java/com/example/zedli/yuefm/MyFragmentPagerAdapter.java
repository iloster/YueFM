package com.example.zedli.yuefm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by ZedLi on 2015/3/3.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter
{
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Log.v("YueFM","getItem:"+i);
        return MyFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
