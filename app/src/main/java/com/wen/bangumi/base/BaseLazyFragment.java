package com.wen.bangumi.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wen.bangumi.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 基本的单页fragment
 * Created by BelieveOP5 on 2017/1/28.
 */

public abstract class BaseLazyFragment<T extends BaseContract.BasePresenter> extends Fragment implements BaseContract.BaseView<T> {

    private Unbinder unbinder;

    private T mPresenter;

    @Override
    public void setPresenter(@NonNull T presenter) {
        this.mPresenter = checkNotNull(presenter);
    }

    @Override
    public T getPresenter() {
        return mPresenter;
    }

    public abstract
    @LayoutRes
    int getLayoutResId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initViews(view);
    }

    /**
     * 当界面加载完成的时候，并且在正确的运行的时候调用该函数
     */
    @Override
    public void onResume() {
        super.onResume();

        //获取数据
        if (!isPrepared || !isVisible)
            return;

        lazyLoad();
        isPrepared = false;

    }

    protected abstract void initViews(View view);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;

    //标志位 fragment是否可见
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            OnVisible();
        } else {
            isVisible = false;
            OnInVisible();
        }
    }

    protected void OnVisible() {
        lazyLoad();
    }

    protected void OnInVisible() {}

    protected void lazyLoad() {}

    /**
     * 当该Fragment有刷新的必要的时候才进行初始化
     * @param view
     */
    protected void initSwipeRefreshLayout(View view) {

        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );

    }

    public void refresh() {}

    protected void initAdapter() {}

    /**
     * Whether or not the view should show refresh progress.
     * @param active
     */
    public void refresh(final boolean active) {

        if (getView() == null) return;

        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });

    }

}
