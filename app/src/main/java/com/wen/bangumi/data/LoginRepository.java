package com.wen.bangumi.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wen.bangumi.network.RetrofitHelper;
import com.wen.bangumi.entity.user.Token;

import io.reactivex.Observable;

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
