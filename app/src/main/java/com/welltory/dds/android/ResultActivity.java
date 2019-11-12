package com.welltory.dds.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created: Belozerov Sergei
 * Company: Welltory Inc.
 * Date: 2019-09-04
 */
public class ResultActivity extends AppCompatActivity {
    private StressLevel stressLevel = null;
    private TextView stressLevelText = null;
    private TextView stressLevelPercent = null;
    private TextView detailsButton = null;
    private LinearLayout extraParamsContainer = null;
    private static final String PROD_TOKEN_URL = "https://app.welltory.com/share-measurement?token=%s";
    private static final String STAGE_TOKEN_URL = "https://stage-app.welltory.com/share-measurement?token=%s";

    public static final String EXTRA_STRESS = "stress";
    public static final String EXTRA_STRESS_LEVEL = "stress_c";
    public static final String EXTRA_TOKEN = "token";
    public static final String EXTRA_IS_STAGE = "is_stage_server";
    public static final String EXTRA_ENERGY = "energy";
    public static final String EXTRA_PRODUCTIVITY = "productivity";
    public static final String EXTRA_RMSSD = "rmssd";
    public static final String EXTRA_SDNN = "sdnn";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initViews();
        initClickListeners();

        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    private void initClickListeners() {
        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://support.welltory.com/article/show/68986-how-to-use-your-measurement-results"));
                startActivity(intent);
            }
        });
        findViewById(R.id.repeatButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelltoryMeasurementUtils.startMeasurement(ResultActivity.this);
            }
        });
    }

    private void initViews() {
        stressLevel = findViewById(R.id.stressLevel);
        stressLevelPercent = findViewById(R.id.stressLevelPercent);
        stressLevelText = findViewById(R.id.stressLevelText);
        detailsButton = findViewById(R.id.details);
        extraParamsContainer = findViewById(R.id.extraParams);
    }

    public static Intent startIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    private void parseIntent(Intent intent) {
        float stress = intent.getFloatExtra(EXTRA_STRESS, 0);
        String level = intent.getStringExtra(EXTRA_STRESS_LEVEL);
        final String token = intent.getStringExtra(EXTRA_TOKEN);
        if (level == null)
            level = "green";
        stressLevelPercent.setText(String.format(Locale.getDefault(), "%.0f%%", stress * 100));
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(ScreenUtils.dp(this, 10));
        String stressText = "Unknown";
        switch (level) {
            case "green":
                stressText = getString(R.string.stressLevelGood);
                shape.setColor(ContextCompat.getColor(this, R.color.green));
                break;
            case "yellow":
                stressText = getString(R.string.stressLevelAverage);
                shape.setColor(ContextCompat.getColor(this, R.color.yellow));
                break;
            case "red":
                stressText = getString(R.string.stressLevelBad);
                shape.setColor(ContextCompat.getColor(this, R.color.red));
                break;
            case "unknown":
                shape.setColor(ContextCompat.getColor(this, R.color.grey_6_40));
                break;
        }
        stressLevelText.setText(stressText);
        stressLevelPercent.setBackground(shape);
        stressLevel.setStress(stress);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token != null) {
                    boolean isStage = getIntent().getBooleanExtra(EXTRA_IS_STAGE, false);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.getDefault(), isStage ? STAGE_TOKEN_URL : PROD_TOKEN_URL, token)));
                    startActivity(intent);
                }
            }
        });

        addExtraParamsViews(intent);
    }

    private void addExtraParamsViews(Intent intent) {
        ArrayList<ExtraParam> extraParams = new ArrayList<>();
        extraParams.add(new ExtraParam(getString(R.string.energy),
                getString(R.string.percentValue, intent.getFloatExtra(EXTRA_ENERGY, 0) * 100)));
        extraParams.add(new ExtraParam(getString(R.string.productivity),
                getString(R.string.percentValue, intent.getFloatExtra(EXTRA_PRODUCTIVITY, 0) * 100)));
        extraParams.add(new ExtraParam(getString(R.string.rmssd),
                getString(R.string.msValue, intent.getFloatExtra(EXTRA_RMSSD, 0))));
        extraParams.add(new ExtraParam(getString(R.string.sdnn),
                getString(R.string.msValue, intent.getFloatExtra(EXTRA_SDNN, 0))));

        extraParamsContainer.removeAllViews();
        for (ExtraParam extraParam : extraParams) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_extra_param, extraParamsContainer, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            ((TextView) view.findViewById(R.id.title)).setText(extraParam.name);
            ((TextView) view.findViewById(R.id.value)).setText(extraParam.value);
            extraParamsContainer.addView(view, layoutParams);
        }
    }

    private class ExtraParam {
        String name;
        String value;

        public ExtraParam(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
