package com.wen.bangumi.module.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wen.bangumi.Bangumi;
import com.wen.bangumi.base.BaseActivity;
import com.wen.bangumi.event.Event;
import com.wen.bangumi.module.user.UserPreferences;
import com.wen.bangumi.R;
import com.wen.bangumi.module.login.LoginActivity;
import com.wen.bangumi.module.user.HomePageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主界面
 * Created by BelieveOP5 on 2017/1/14.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    public NavigationView mNavigationView;

    public View navHeaderView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }

        initFragment();

        if (UserPreferences.isLogin(Bangumi.getInstance()))
            onLoginEvent(new Event.LoginEvent());
    }

    private void initFragment() {

        HomeFragment homeFragment = new HomeFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, homeFragment)
                .show(homeFragment).commit();
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
        CircleImageView circleImageView = (CircleImageView) navHeaderView.findViewById(R.id.circle_image_view);

        username.setText(UserPreferences.getNickName(Bangumi.getInstance()));
        userInfo.setText(UserPreferences.getSign(Bangumi.getInstance()));
        Picasso.with(this)
                .load(UserPreferences.getLargeAvatar(Bangumi.getInstance()))
                .config(Bitmap.Config.RGB_565)
                .into(circleImageView);

    }

    private void setupDrawerContent(NavigationView mNavigationView) {

        navHeaderView = mNavigationView.inflateHeaderView(R.layout.nav_header);

        CircleImageView circleImageView = (CircleImageView) navHeaderView.findViewById(R.id.circle_image_view);
        circleImageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //先判断有没有登录
                        if (!UserPreferences.isLogin(Bangumi.getInstance()))
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        else
                            startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                    }
                }
        );

        ImageView logoutImageView = (ImageView) navHeaderView.findViewById(R.id.log_out_image_view);
        logoutImageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //先判断有没有登录
                        if (!UserPreferences.isLogin(Bangumi.getInstance())) {
                            Snackbar.make(v, "你还没有登录呢！！", Snackbar.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Bangumi.getInstance());
                            builder.setTitle("退出登录");
                            builder.setMessage("你确定要要退出登录吗？？");

                            builder.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            UserPreferences.logout(Bangumi.getInstance());
                                            // TODO: 2017/2/15 完成注销的功能
                                        }
                                    }
                            );

                            builder.setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            );

                            builder.create().show();

                        }

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
                                if (!UserPreferences.isLogin(Bangumi.getInstance())) {
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
