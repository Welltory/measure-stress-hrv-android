package com.welltory.dds.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created: Belozerov Sergei
 * Company: Welltory Inc.
 * Date: 2019-09-05
 */
public class StressLevel extends View {

    private Bitmap bgBitmap;
    private Paint bitmapPaint = new Paint();
    private Bitmap arrowBitmap;

    private float angle = 0;

    public void setStress(float stress) {
        setAngle(stress * 180f);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public StressLevel(Context context) {
        this(context, null);
    }

    public StressLevel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StressLevel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public StressLevel(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StressLevel, 0, 0);
            angle = typedArray.getFloat(R.styleable.StressLevel_sl_angle, 0);
            typedArray.recycle();
        }
        bitmapPaint.setAntiAlias(true);
        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_stress_level);
        arrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_stress_arrow);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bgBitmap, (getWidth() - bgBitmap.getWidth()) / 2f, getHeight() - bgBitmap.getHeight(), bitmapPaint);
        canvas.save();
        canvas.rotate(angle, getWidth() / 2, getHeight());
        canvas.drawBitmap(arrowBitmap, 0, getHeight() - arrowBitmap.getHeight() / 2f, bitmapPaint);
        canvas.restore();
    }
}
