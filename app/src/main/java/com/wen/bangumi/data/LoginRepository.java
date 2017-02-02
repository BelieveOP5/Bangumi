package com.wen.bangumi.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.wen.bangumi.api.bangumi.BangumiApi;
import com.wen.bangumi.network.RetrofitHelper;
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

    public static LoginRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoginRepository();
        }
        return INSTANCE;
    }

    @Override
    public Observable<Token> login(@NonNull String mail, @NonNull String pwd) {

        return RetrofitHelper.getBangumiApi().login(mail, pwd);

    }

}
