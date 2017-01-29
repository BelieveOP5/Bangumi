package com.wen.bangumi.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.wen.bangumi.Bangumi;
import com.wen.bangumi.api.bangumi.BangumiApi;
import com.wen.bangumi.collection.BangumiStatus;
import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.user.UserPreferences;
import com.wen.bangumi.util.JsoupUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public class CollectionRepository implements CollectionInterface{

    /**
     * BangumiApi的基本网址
     */
    public static final String BANGUMI_BASE_URL = "http://bgm.tv/";

    @Nullable
    private static CollectionRepository INSTANCE;

    /**
     * 返回一个番剧实例
     * @return
     */
    public static CollectionRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CollectionRepository();
        }
        return INSTANCE;
    }

    /**
     * 使应用的数据库里存的数据无效化，一般在强制刷新的时候修改为true
     */
    @VisibleForTesting
    boolean mCacheIsDirty = false;

    /**
     * 默认连接超时时间为10s
     */
    public static final int DEFAULT_TIMEOUT = 10;

    @Override
    public Observable<List<BangumiItem>> loadBangumi(@NonNull final BangumiStatus status) {
        //如果缓存不Dirty（或者不在强制刷新的情况下），则从数据库中读取数据
        if (!mCacheIsDirty) {

        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        BangumiApi mBangumiApi = new Retrofit.Builder()
                .baseUrl(BANGUMI_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(BangumiApi.class);

        return mBangumiApi
                .listCollection(
                        "anime",
                        UserPreferences.getUserName(Bangumi.getInstance()),
                        status.toString(),
                        // TODO: 2017/1/28 多页获取功能
                        1
                )
                .map(new Function<String, List<BangumiItem>>() {
                    @Override
                    public List<BangumiItem> apply(String s) throws Exception {
                        return JsoupUtils.parseCollHtml(s);
                    }
                })
                //完成了之后
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mCacheIsDirty = false;
                    }
                });

    }

    /**
     * 当需要强制刷新时调用，表示需要清除缓存，并重新加载
     */
    @Override
    public void refreshBangumi() {
        mCacheIsDirty = true;
    }

}
