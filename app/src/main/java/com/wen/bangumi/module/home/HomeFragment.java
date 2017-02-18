package com.wen.bangumi.module.home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wen.bangumi.R;
import com.wen.bangumi.base.BaseLazyFragment;
import com.wen.bangumi.module.calendaritem.DailyCalendarFragment;
import com.wen.bangumi.module.calendaritem.DailyCalendarPresenter;
import com.wen.bangumi.module.calendaritem.WeekDay;
import com.wen.bangumi.util.Injection;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by BelieveOP5 on 2017/2/18.
 */

public class HomeFragment extends BaseLazyFragment<HomeContract.Presenter> implements HomeContract.View {

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    public ViewPager mViewPager;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_page;
    }

    @Override
    protected void initViews(View view) {
        initToolbar();
        initViewPager();
    }

    private void initToolbar() {

        ((MainActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setTitle(getString(R.string.bangumi_onAir));
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initViewPager() {

        Map<WeekDay, String> map = new LinkedHashMap<>();
        map.put(WeekDay.MONDAY, getString(R.string.monday));
        map.put(WeekDay.TUESDAY, getString(R.string.tuesday));
        map.put(WeekDay.WEDNESDAY, getString(R.string.wednesday));
        map.put(WeekDay.THURSDAY, getString(R.string.thursday));
        map.put(WeekDay.FRIDAY, getString(R.string.friday));
        map.put(WeekDay.SATURDAY, getString(R.string.saturday));
        map.put(WeekDay.SUNDAY, getString(R.string.sunday));

        final ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        for (Map.Entry<WeekDay, String> i : map.entrySet()) {
            DailyCalendarFragment fragment = DailyCalendarFragment.newInstance(i.getKey());
            mViewPagerAdapter.addItem(fragment, i.getValue());
            new DailyCalendarPresenter(
                    Injection.provideCalendarRepository(),
                    fragment
            );
        }

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(mViewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);

                //获取当前日期，并设置初始界面为当前日期
                Date today = new Date();
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.setTime(today);
                int i = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
                i = i == 0 ? 7 : i;
                mViewPager.setCurrentItem(i - 1);

            }
        });

    }

}
