package com.wen.bangumi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by BelieveOP5 on 2017/2/6.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        initView(savedInstanceState);

        initAdapter();

        loadData();

    }

    public abstract int getLayoutId();

    /**
     * 在该方法中初始化视图
     * @param savedInstanceState
     */
    protected void initView(@Nullable Bundle savedInstanceState) {}

    /**
     * 在该方法中初始化adapter
     */
    protected void initAdapter() {}

    /**
     * 在该方法中加载数据
     */
    protected void loadData() {}

}
