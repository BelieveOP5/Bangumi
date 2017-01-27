package com.wen.bangumi.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.wen.bangumi.api.bangumi.BangumiApi;
import com.wen.bangumi.responseentity.Token;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BelieveOP5 on 2017/1/25.
 */

public class LoginRepository implements LoginInterface {

    @Nullable
    private static LoginRepository INSTANCE;

    /**
     * BangumiApi的基本网址
     */
    public static final String BANGUMI_BASE_URL = "http://api.bgm.tv/";

    /**
     * 默认连接超时时间为10s
     */
    public static final int DEFAULT_TIMEOUT = 10;

    public static LoginRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoginRepository();
        }
        return INSTANCE;
    }

    @Override
    public Observable<Token> login(@NonNull String mail, @NonNull String pwd) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        BangumiApi mBangumiApi = new Retrofit.Builder()
                .baseUrl(BANGUMI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(BangumiApi.class);

        return mBangumiApi.login(mail, pwd);

    }

}
