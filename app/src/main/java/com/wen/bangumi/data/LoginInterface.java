package com.wen.bangumi.data;

import android.support.annotation.NonNull;

import com.wen.bangumi.entity.user.Token;

import io.reactivex.Observable;

/**
 * Created by BelieveOP5 on 2017/1/25.
 */

public interface LoginInterface {

    /**
     * 登录（对数据的操作部分）
     * @param mail 账号
     * @param pwd 密码
     */
    Observable<Token> login(@NonNull String mail,
                            @NonNull String pwd);

}
