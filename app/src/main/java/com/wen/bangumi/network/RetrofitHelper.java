package com.wen.bangumi.network;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.wen.bangumi.network.api.bangumi.BangumiApi;
import com.wen.bangumi.network.api.bilibili.BiliBiliApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by BelieveOP5 on 2017/2/2.
 */

public class RetrofitHelper {

    private static OkHttpClient mOkHttpClient;

    /**
     * static代码块，在加载这个类的时候会自动调用代码块内部的方法一次（不需要实例化也会自动调用）
     */
    static {
        initOkHttpClient();
    }

    /**
     * 获取Bangumi网页的api
     * @return
     */
    public static BangumiApi getBangumiWebApi() {
        return createApi(BangumiApi.class, ApiConstants.BANGUMI_WEB_BASE_URL);
    }

    /**
     * 获取Bangumi网站的api
     * @return
     */
    public static BangumiApi getBangumiApi() {
        return createApi(BangumiApi.class, ApiConstants.BANGUMI_BASE_URL);
    }

    /**
     * 获取BiliBili网站的api
     * @return
     */
    public static BiliBiliApi getBiliBiliApi() {
        return createApi(BiliBiliApi.class, ApiConstants.BiliBili_BASE_URL);
    }

    private static <T> T createApi(Class<T> tClass, String baseUrl) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        switch (baseUrl) {
            case ApiConstants.BANGUMI_WEB_BASE_URL:
                //返回信息为html网页模式
                builder.addConverterFactory(ScalarsConverterFactory.create());
                break;
            default:
                //返回信息为json格式，用gson解析
                builder.addConverterFactory(GsonConverterFactory.create());
        }

        return builder.build().create(tClass);

    }

    /**
     * 初始化OKHttpClient
     */
    private static void initOkHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {

                    mOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();

                }
            }
        }

    }



}
