package com.wen.bangumi.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by BelieveOP5 on 2017/2/6.
 */

public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends AppCompatActivity implements BaseContract.BaseView<T> {

    private T mPresenter;

    @Override
    public void setPresenter(@NonNull T presenter) {
        this.mPresenter = checkNotNull(presenter);
    }

    @Override
    public T getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);

        initView(savedInstanceState);

        initAdapter();

        loadData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
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
