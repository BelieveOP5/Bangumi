package com.wen.bangumi.calendaritem;

import com.wen.bangumi.base.BasePresenter;
import com.wen.bangumi.base.BaseView;
import com.wen.bangumi.greenDAO.BangumiItem;

import java.util.List;

/**
 * Created by BelieveOP5 on 2017/1/17.
 */

public interface DailyCalendarContract {


    interface View extends BaseView<Presenter> {

        /**
         * 显示某日的番剧
         * @param mBangumiItemList 当日的番剧列表
         */
        void showDailyCalendar(List<BangumiItem> mBangumiItemList);

        /**
         * 当日没有番剧可以获取
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

        /**
         *
         * @param mDate
         */
        void subscribe(int mDate);

        /**
         * 加载一日的番剧列表
         * @param mDate 要加载的日期
         * @param forceUpdate 是否要强制刷新
         */
        void loadDailyCalendar(int mDate, boolean forceUpdate);

    }

}
