package com.wen.bangumi.data;

import android.support.annotation.NonNull;

import com.wen.bangumi.module.calendaritem.WeekDay;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.entity.calendar.DailyCalendar;
import com.wen.bangumi.entity.calendar.TimeLine_BiliBili;

import java.util.List;

import io.reactivex.Observable;

/**
 * 获取每日番剧的接口
 * Created by BelieveOP5 on 2017/1/18.
 */

public interface CalendarInterface {

    /**
     * 获取每日的番剧放送列表
     * @return
     */
    Observable<List<BangumiItem>> loadBangumi(@NonNull WeekDay mDate);

    /**
     * Bangmi
     * 将每日的番剧数据存入到数据库中，并返回该模块所处的日期的番剧信息
     * @param mList 要储存的番剧信息
     * @param mDate 模块所处的日期
     * @return 该模块所处的日期的番剧信息
     */
    List<BangumiItem> saveBangumi(@NonNull List<DailyCalendar> mList,
                                  @NonNull WeekDay mDate);

    /**
     * BiliBili
     * 将每日的番剧数据存入到数据库中，并返回该模块所处的日期的番剧信息
     * @param mList 要储存的番剧信息
     * @param mDate 模块所处的日期
     * @return 该模块所处的日期的番剧信息
     */
    List<BangumiItem> saveBiliBiliBangumi(@NonNull TimeLine_BiliBili mList,
                                          @NonNull WeekDay mDate);

    /**
     * 当需要强制刷新时调用，表示需要清除缓存，并重新加载
     */
    void refreshBangumi();

}
