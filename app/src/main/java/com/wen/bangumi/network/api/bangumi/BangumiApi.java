package com.wen.bangumi.network.api.bangumi;

import com.wen.bangumi.entity.bangumi.EpisodeStatus;
import com.wen.bangumi.entity.calendar.DailyCalendar;
import com.wen.bangumi.entity.user.Token;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by BelieveOP5 on 2017/1/19.
 */

public interface BangumiApi {

    /**
     *  获取每日放送的番剧
     * @return
     */
    @GET("calendar")
    Observable<List<DailyCalendar>> listCalendar();

    /**
     * 登录Bangumi
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("auth?source=onAir")
    Observable<Token> login(@Field("username") String username,
                            @Field("password") String password);


    /**
     * 获取番剧的每集状态，比如第一集看过，第二季抛弃，想看等状态
     * @param id 登录后返回的用户id
     * @param subject_id 番剧 id
     * @param auth 登录成功后的 auth 信息
     * @return
     */
    @GET("user/{user}/progress?source=onAir")
    Observable<EpisodeStatus> loadEpisodeStatus(@Path("user") int id,
                                                @Query("subject_id") String subject_id,
                                                @Query("auth") String auth);


    /**
     * 上面的BaseUrl是http://api.bgm.tv/
     * ---------------------------------------------------------------------------------------------
     * 下面是通过往返数据解析来抓取数据，不是通过api
     * BaseUrl是http://bgm.tv/
     */

    /**
     * 获取用户的动画，游戏，图书等状态
     * @param category anime 动画 game 游戏 book 图书
     * @param userName 用户登录后的id, 有些用户的id 不是数字
     * @param type wish 想看 collect 看过 do在看 on_hold搁置 dropped 抛弃
     * @param page 页数
     * @return
     */
    @GET("{category}/list/{userName}/{type}")
    Observable<String> listCollection(@Path("category") String category,
                                      @Path("userName") String userName,
                                      @Path("type") String type,
                                      @Query("page") int page);

    /**
     * 获取该番剧的章节信息，不包括用户的观看状态
     * @param id 番剧id
     * @return
     */
    @GET("subject/{subject}/ep")
    Observable<String> loadEpisodes(@Path("subject") String id);

}
