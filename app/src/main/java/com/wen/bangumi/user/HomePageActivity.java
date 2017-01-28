package com.wen.bangumi.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wen.bangumi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BelieveOP5 on 2017/1/26.
 */

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_page_act);

        setupToolbar();

        setupViewPager();

    }

    private void setupToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("");
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupViewPager() {

        List<String> strList = new ArrayList<>();
        strList.add(getString(R.string.doing));
        strList.add(getString(R.string.collect));
        strList.add(getString(R.string.wish));
        strList.add(getString(R.string.on_hold));
        strList.add(getString(R.string.dropped));

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < strList.size(); ++i) {
            viewPagerAdapter.addItem(new Fragment(), strList.get(i));
        }

        final TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(viewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
            }
        });

    }

}
