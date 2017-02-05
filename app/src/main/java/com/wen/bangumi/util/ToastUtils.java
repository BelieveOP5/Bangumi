package com.wen.bangumi.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 防止出现Toast多次显示的工具
 * Created by BelieveOP5 on 2017/2/5.
 */

public class ToastUtils {

    private static Toast toast;

    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

}
