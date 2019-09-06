package com.welltory.dds.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;

/**
 * Created: Belozerov Sergei
 * E-mail: belozerow@gmail.com
 * Company: THE RED ONE
 * Date: 2019-09-04
 */
public class ResultActivity extends AppCompatActivity {
    private StressLevel stressLevel = null;
    private TextView stressLevelText = null;
    private TextView stressLevelPercent = null;
    private TextView detailsButton = null;
    private static final String PROD_TOKEN_URL = "https://app.welltory.com/share-measurement?token=%s";
    private static final String STAGE_TOKEN_URL = "https://stage-app.welltory.com/share-measurement?token=%s";

    public static final String EXTRA_STRESS = "stress";
    public static final String EXTRA_STRESS_LEVEL = "stress_c";
    public static final String EXTRA_TOKEN = "token";
    public static final String EXTRA_IS_STAGE = "is_stage_server";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        stressLevel = findViewById(R.id.stressLevel);
        stressLevelPercent = findViewById(R.id.stressLevelPercent);
        stressLevelText = findViewById(R.id.stressLevelText);
        detailsButton = findViewById(R.id.details);
        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://support.welltory.com/article/show/68986-how-to-use-your-measurement-results"));
                startActivity(intent);
            }
        });
        parseIntent();
    }

    public static Intent getIntent(Context context, float stress, String level, String token, boolean isStage) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_STRESS, stress);
        intent.putExtra(EXTRA_STRESS_LEVEL, level);
        if (token != null) {
            intent.putExtra(EXTRA_TOKEN, token);
        }
        intent.putExtra(EXTRA_IS_STAGE, isStage);
        return intent;
    }

    public static Intent getIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtras(extras);
        return intent;
    }

    private void parseIntent() {
        float stress = getIntent().getFloatExtra(EXTRA_STRESS, 0);
        String level = getIntent().getStringExtra(EXTRA_STRESS_LEVEL);
        final String token = getIntent().getStringExtra(EXTRA_TOKEN);

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

        if (!TextUtils.isEmpty("token")) {
            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isStage = getIntent().getBooleanExtra(EXTRA_IS_STAGE, false);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.getDefault(), isStage ? STAGE_TOKEN_URL : PROD_TOKEN_URL, token)));
                    startActivity(intent);
                }
            });
        }
    }
}
