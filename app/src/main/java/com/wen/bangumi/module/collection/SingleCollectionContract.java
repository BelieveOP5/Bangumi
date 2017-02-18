package com.wen.bangumi.module.collection;

import com.wen.bangumi.base.BaseContract;
import com.wen.bangumi.greenDAO.BangumiItem;

import java.util.List;

/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public interface SingleCollectionContract {

    interface View extends BaseContract.BaseView<Presenter> {

        /**
         * 显示番剧
         * @param mBangumiItemList 番剧列表
         */
        void showBangumiCollection(List<BangumiItem> mBangumiItemList);

        /**
         * 显示番剧视图
         */
        void showBangumiView();

        /**
         * 显示没有番剧的视图
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

    interface Presenter extends BaseContract.BasePresenter {

        void subscribe(BangumiStatus status);

        /**
         * 加载番剧
         * @param status
         * @param forceUpdate
         */
        void loadBangumi(BangumiStatus status, boolean forceUpdate);

    }
}
