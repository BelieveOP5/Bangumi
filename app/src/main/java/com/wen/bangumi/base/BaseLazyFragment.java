package com.wen.bangumi.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.wen.bangumi.R;

/**
 * 基本的单页fragment
 * Created by BelieveOP5 on 2017/1/28.
 */

public class BaseLazyFragment extends Fragment {

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