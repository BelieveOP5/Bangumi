package com.wen.bangumi.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.wen.bangumi.base.Constants;
import com.wen.bangumi.responseentity.Token;
import com.wen.bangumi.util.SharedPreferencesUtils;

/**
 * Created by BelieveOP5 on 2017/1/26.
 */

public class UserPreferences {

    private final static String PREFERENCES_FILE_NAME = Constants.PREFERENCES_FILE_NAME;

    /**
     * 判断是否已经登录过
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        if (!TextUtils.isEmpty(getSign(context)))
            return true;
        else
            return false;
    }

    /**
     * 用户登出，需要删除用户设置文件
     * @param context
     */
    public static void logout(Context context) {
        SharedPreferencesUtils.clear(context, PREFERENCES_FILE_NAME);
    }

    /**
     * 存储从Bangumi返回的用户信息
     * @param context
     * @param token
     */
    public static void saveToken(Context context, Token token) {
        saveId(context, token.getId());
        saveUrl(context, token.getUrl());
        saveUserName(context, token.getUsername());
        saveNickName(context, token.getNickname());
        if (token.getAvatar()!= null && token.getAvatar().getLarge() != null) {
            saveLargeAvatar(context, token.getAvatar().getLarge());
        }
        saveSign(context, token.getSign());
        saveAuth(context, token.getAuth());
        saveAuthEncode(context, token.getAuth_encode());
    }

    private static boolean saveId(Context context, int id) {
        return SharedPreferencesUtils.putInt(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_USER_ID_KEY,
                id
        );
    }

    private static boolean saveUrl(Context context, String url) {
        return SharedPreferencesUtils.putString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_URL_KEY,
                url
        );
    }

    public static String getUserName(Context context) {
        return SharedPreferencesUtils.getString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_USER_NAME_KEY,
                ""
        );
    }

    private static boolean saveUserName(Context context, String username) {
        return SharedPreferencesUtils.putString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_USER_NAME_KEY,
                username
        );
    }

    public static String getNickName(Context context) {
        return SharedPreferencesUtils.getString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_NICKNAME_KEY,
                ""
        );
    }

    private static boolean saveNickName(Context context, String nickname) {
        return SharedPreferencesUtils.putString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_NICKNAME_KEY,
                nickname
        );
    }

    public static String getLargeAvatar(Context context) {
        return SharedPreferencesUtils.getString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_LARGE_AVATAR_KEY,
                ""
        );
    }

    private static boolean saveLargeAvatar(Context context, String large) {
        return SharedPreferencesUtils.putString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_LARGE_AVATAR_KEY,
                large
        );
    }

    public static String getSign(Context context) {
        return SharedPreferencesUtils.getString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_SIGN_KEY,
                ""
        );
    }

    private static boolean saveSign(Context context, String sign) {
        return SharedPreferencesUtils.putString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_SIGN_KEY,
                sign
        );
    }

    private static boolean saveAuth(Context context, String auth) {
        return SharedPreferencesUtils.putString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_AUTH_KEY,
                auth
        );
    }

    private static boolean saveAuthEncode(Context context, String auth_encode) {
        return SharedPreferencesUtils.putString(
                context,
                PREFERENCES_FILE_NAME,
                Constants.LOGIN_AUTH_ENCODE_KEY,
                auth_encode
        );
    }

}
