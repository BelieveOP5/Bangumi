package com.wen.bangumi.collection;

import com.wen.bangumi.base.BasePresenter;
import com.wen.bangumi.base.BaseView;
import com.wen.bangumi.greenDAO.BangumiItem;

import java.util.List;

/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public interface SingleCollContract {

    interface View extends BaseView<Presenter> {

        /**
         * 显示番剧
         * @param mBangumiItemList 番剧列表
         */
        void showCollBangumi(List<BangumiItem> mBangumiItemList);

        /**
         * 没有番剧可以获取
         */
        void showNoBangumiView();

        /**
         * 显示正在读取番剧的旋转小圆圈
         * @param active
         */
        void setLoadingIndicator(boolean active);

        /**
         * 显示读取番剧失败的消息
         */
        void showLoadingBangumiError();

    }

    interface Presenter extends BasePresenter {

        void subscribe(BangumiStatus status);

        /**
         * 加载番剧
         * @param status
         * @param forceUpdate
         */
        void loadBangumi(BangumiStatus status, boolean forceUpdate);

    }
}
