package com.welltory.dds.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Created: Belozerov Sergei
 * E-mail: belozerow@gmail.com
 * Company: THE RED ONE
 * Date: 2019-09-06
 */
public class WelltoryMeasurementUtils {
    public static void startMeasurement(Activity activity) {
        String callBackActivity = String.format(Locale.getDefault(), "%s/%s",
                activity.getPackageName(), activity.getClass().getName());
        String params = String.format(Locale.getDefault(),
                "source=%s&callback=%s&param1=test_param1", "DemoApp", callBackActivity);
        Intent intent = null;
        try {
            String encodedParams = URLEncoder.encode(params, "UTF-8");
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("welltory://branch/Measurement/Start/" + encodedParams));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (intent == null || intent.resolveActivity(activity.getPackageManager()) == null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://welltory.onelink.me/2180424117/bf497b9?" + params));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
