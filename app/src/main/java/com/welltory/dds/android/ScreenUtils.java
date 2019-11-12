package com.welltory.dds.android;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created: Belozerov Sergei
 * Company: Welltory Inc.
 * Date: 2019-09-06
 */
public class ScreenUtils {
    public static int dp(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
