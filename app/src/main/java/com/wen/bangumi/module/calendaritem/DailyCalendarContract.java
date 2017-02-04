package com.wen.bangumi.module.calendaritem;

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


        void showBangumiView();

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
         * @param weekday
         */
        void subscribe(WeekDay weekday);

        /**
         *
         * @param weekday
         * @param forceUpdate
         */
        void loadDailyCalendar(WeekDay weekday, boolean forceUpdate);

    }

}
