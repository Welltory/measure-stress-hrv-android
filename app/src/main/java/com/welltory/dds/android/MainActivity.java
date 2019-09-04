
package com.welltory.dds.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = findViewById(R.id.result);
        resultView.setText(parseIntent(getIntent()));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMeasurement();
            }
        });
    }

    private void startMeasurement() {
        String callBackActivity = String.format(Locale.getDefault(), "%s/%s",
                getPackageName(), MainActivity.class.getName());
        String params = String.format(Locale.getDefault(),
                "source=%s&callback=%s", "DemoApp", callBackActivity);
        Intent intent = null;
        try {
            String encodedParams = URLEncoder.encode(params, "UTF-8");
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("welltory://branch/Measurement/Start/" + encodedParams));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (intent == null || intent.resolveActivity(getPackageManager()) == null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://welltory.onelink.me/2180424117/bf497b9?" + params));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resultView.setText(parseIntent(intent));
    }

    private String parseIntent(Intent data) {
        if (data != null && data.hasExtra("stress")) {
            return String.format(Locale.getDefault(), "productivity=%s\nrmssd=%s\nenergy=%s\npower=%s\nstress=%s\nsdnn=%s",
                    data.getFloatExtra("productivity", -1),
                    data.getFloatExtra("rmssd", -1),
                    data.getFloatExtra("energy", -1),
                    data.getFloatExtra("power", -1),
                    data.getFloatExtra("stress", -1),
                    data.getFloatExtra("sdnn", -1));
        } else {
            return null;
        }
    }
}
