package com.wen.bangumi.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.wen.bangumi.R;

/**
 * 基本的单页fragment
 * Created by BelieveOP5 on 2017/1/28.
 */

public class BaseLazyFragment extends Fragment {

    /**
     * 显示正在读取的旋转小圆圈
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
