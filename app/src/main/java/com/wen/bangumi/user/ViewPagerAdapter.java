package com.wen.bangumi.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wen.bangumi.collection.SingleCollFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BelieveOP5 on 2017/1/26.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<SingleCollFragment> mFragmentList = new ArrayList<>();
    List<String> mPageTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addItem(SingleCollFragment fragment, String pageTitle) {
        mFragmentList.add(fragment);
        mPageTitles.add(pageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    /**
     * 获取当前页面的标题，这个标题会在{@link android.support.design.widget.TabLayout}中显示出来
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitles.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


}
