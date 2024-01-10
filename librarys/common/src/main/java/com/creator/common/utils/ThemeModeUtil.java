package com.creator.common.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

public class ThemeModeUtil {

    /**
     * 判断当前是否为深色模式
     * @param context
     * @return
     */
    public static boolean isDarkMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int currentNightMode = context.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
        } else {
            return false;
        }
    }
}
