package com.sien.aimanager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sien.aimanager.R;
import com.sien.aimanager.fragment.GuideFragment;

/**
 * @author sien
 * @date 2016/9/12
 * @descript 引导页适配器
 */
public class GuideAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 4; //引导页面数

    public GuideAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment gf = null;
        if (position == 0) {
            gf = GuideFragment.newInstance(R.layout.fragment_welcome1);
        } else if (position == 1) {
            gf = GuideFragment.newInstance(R.layout.fragment_welcome2);
        } else if (position == 2) {
            gf = GuideFragment.newInstance(R.layout.fragment_welcome3);
        } else {
            gf = GuideFragment.newInstance(R.layout.fragment_welcome4);
        }

        return gf;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
