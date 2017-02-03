package com.wen.bangumi.network.api.bilibili;

import com.wen.bangumi.entity.TimeLine_BiliBili;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by BelieveOP5 on 2017/1/20.
 */

public interface BiliBiliApi {

    /**
     * 获取每日放送的番剧
     */
    @GET("api/timeline_v4?" +
            "appkey=1d8b6e7d45233436&" +
            "area_id=1%2C2%2C-1&" +
            "build=433003&" +
            "date_after=6&" +
            "date_before=6&" +
            "mobi_app=android&" +
            "platform=android&" +
            "see_mine=0&" +
            "ts=1484297157000&" +
            "sign=5fb9efdb3e96112c135757e6c87e8c27")
    Observable<TimeLine_BiliBili> listCalendar();
}
