package com.wen.bangumi.util.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Created by BelieveOP5 on 2017/1/26.
 */

public class SharedPreferencesUtils {

    private static final int PRIVATE = Context.MODE_PRIVATE;

    public static boolean putString(Context context,
                                    String filename,
                                    String key,
                                    @Nullable String value) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    @Nullable
    public static String getString(Context context,
                                   String filename,
                                   String key,
                                   @Nullable String defValue) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        return sp.getString(key, defValue);
    }

    public static boolean putStringSet(Context context,
                                       String filename,
                                       String key,
                                       @Nullable Set<String> value) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, value);
        return editor.commit();
    }

    @Nullable
    public static Set<String> getStringSet(Context context,
                                           String filename,
                                           String key,
                                           @Nullable Set<String> defValue) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        return sp.getStringSet(key, defValue);
    }

    public static boolean putInt(Context context,
                                 String filename,
                                 String key,
                                 int value) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(Context context,
                             String filename,
                             String key,
                             int defValue) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static boolean putLong(Context context,
                                  String filename,
                                  String key,
                                  long value) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(Context context,
                               String filename,
                               String key,
                               long defValue) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        return sp.getLong(key, defValue);
    }


    public static boolean putFloat(Context context,
                                   String filename,
                                   String key,
                                   float value) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(Context context,
                                 String filename,
                                 String key,
                                 float defValue) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        return sp.getFloat(key, defValue);
    }

    public static boolean putBoolean(Context context,
                                     String filename,
                                     String key,
                                     boolean value) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context,
                                     String filename,
                                     String key,
                                     boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void clear(Context context,
                             String filename) {
        SharedPreferences sp = context.getSharedPreferences(filename, PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }
}
