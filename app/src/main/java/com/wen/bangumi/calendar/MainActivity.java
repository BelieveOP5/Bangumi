package com.wen.bangumi.calendar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wen.bangumi.Bangumi;
import com.wen.bangumi.event.Event;
import com.wen.bangumi.util.preferences.UserPreferencesUtils;
import com.wen.bangumi.util.Injection;
import com.wen.bangumi.calendaritem.DailyCalendarFragment;
import com.wen.bangumi.calendaritem.DailyCalendarPresenter;
import com.wen.bangumi.greenDAO.DaoMaster;
import com.wen.bangumi.greenDAO.DaoSession;
import com.wen.bangumi.R;
import com.wen.bangumi.login.LoginActivity;
import com.wen.bangumi.user.HomePageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by BelieveOP5 on 2017/1/14.
 */

public class MainActivity extends AppCompatActivity {

    public static MainActivity mActivity;

    private DrawerLayout mDrawerLayout;

    private View navHeaderView;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private DaoSession mDaoSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_act);

        mActivity = this;

        EventBus.getDefault().register(this);

        //设置数据库连接
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "bangumi-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        mDaoSession = new DaoMaster(db).newSession();

        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setupToolbar();

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }

        setupViewPager();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserPreferencesUtils.isLogin(Bangumi.getInstance()))
            onLoginEvent(new Event.LoginEvent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onLoginEvent(Event.LoginEvent loginEvent) {

        TextView username = (TextView) navHeaderView.findViewById(R.id.username_text_view);
        TextView userInfo = (TextView) navHeaderView.findViewById(R.id.user_info_text_view);
        CircleImageView circleImageView = (CircleImageView) navHeaderView.findViewById(R.id.circle_imageview);

        username.setText(UserPreferencesUtils.getNickName(Bangumi.getInstance()));
        userInfo.setText(UserPreferencesUtils.getSign(Bangumi.getInstance()));
        Picasso.with(this)
                .load(UserPreferencesUtils.getLargeAvatar(Bangumi.getInstance()))
                .config(Bitmap.Config.RGB_565)
                .into(circleImageView);

    }

    private void setupToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(getString(R.string.bangumi_onAir));
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawerContent(NavigationView mNavigationView) {

        navHeaderView = mNavigationView.inflateHeaderView(R.layout.nav_header);

        CircleImageView circleImageView = (CircleImageView) navHeaderView.findViewById(R.id.circle_imageview);
        circleImageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //先判断有没有登录
                        if (!UserPreferencesUtils.isLogin(Bangumi.getInstance()))
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        else
                            startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                    }
                }
        );

        //为下面的菜单栏设置监听事件
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.onAir_navigation_menu_item:
                                // 什么也不做，我们已经在这个页面上了
                                break;

                            // 时光机功能
                            case R.id.time_machine_navigation_menu_item:
                                //先判断有没有登录
                                if (!UserPreferencesUtils.isLogin(Bangumi.getInstance())) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            getString(R.string.not_login_text),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                                else
                                    startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                                break;

                            default:
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    private void setupViewPager() {

        List<String> strList = new ArrayList<>();
        strList.add(getString(R.string.monday));
        strList.add(getString(R.string.tuesday));
        strList.add(getString(R.string.wednesday));
        strList.add(getString(R.string.thursday));
        strList.add(getString(R.string.friday));
        strList.add(getString(R.string.saturday));
        strList.add(getString(R.string.sunday));

        final ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < strList.size(); ++i) {

            //为每一个Fragment连接一个Presenter
            DailyCalendarFragment mDailyCalendarFragment = DailyCalendarFragment.newInstance(i + 1);
            mViewPagerAdapter.addItem(mDailyCalendarFragment, strList.get(i));
            new DailyCalendarPresenter(
                    Injection.provideCalendarRepository(),
                    mDailyCalendarFragment,
                    Injection.provideSchedulerProvider());

        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
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

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
