package com.wen.bangumi.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wen.bangumi.R;
import com.wen.bangumi.module.collection.BangumiStatus;
import com.wen.bangumi.module.collection.SingleCollectionFragment;
import com.wen.bangumi.module.collection.SingleCollectionPresent;
import com.wen.bangumi.util.Injection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by BelieveOP5 on 2017/1/26.
 */

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_act);

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

        Map<BangumiStatus, String> map = new LinkedHashMap<>();
        map.put(BangumiStatus.WISH, getString(R.string.wish));
        map.put(BangumiStatus.DO, getString(R.string.doing));
        map.put(BangumiStatus.COLLECT, getString(R.string.collect));
        map.put(BangumiStatus.ON_HOLD, getString(R.string.on_hold));
        map.put(BangumiStatus.DROPPED, getString(R.string.dropped));

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (Map.Entry<BangumiStatus, String> i : map.entrySet()) {
            SingleCollectionFragment fragment = SingleCollectionFragment.newInstance(i.getKey());
            viewPagerAdapter.addItem(fragment, i.getValue());
            new SingleCollectionPresent(
                    Injection.provideCollectionRepository(),
                    fragment
            );
        }

        final TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(viewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);

                //设置初始界面为在看的界面
                mViewPager.setCurrentItem(1);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
