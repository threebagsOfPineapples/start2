package com.tachyon5.kstart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by guofe on 2017/3/31 0031.
 */

public class LineView extends View {
    public static final float SCALE = 1.0f;
    float[] scaleYFloats = new float[]{16 * SCALE / 75,
            42 * SCALE / 75,
            SCALE,
            42 * SCALE / 75,
            16 * SCALE / 75,
            42 * SCALE / 75,
            SCALE,
            42 * SCALE / 75,
            16 * SCALE / 75
    };

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 设置画笔为抗锯齿
        paint.setColor(Color.BLACK); // 设

        float translateX = getWidth() / 19;
        float translateY = getHeight() / 2;
        for (int i = 0; i < 9; i++) {
            canvas.save();
            canvas.translate((2 + i * 2) * translateX - translateX / 2, translateY);
            canvas.scale(SCALE, scaleYFloats[i]);
            RectF rectF = new RectF(-translateX / 3, -getHeight() / 3f, translateX / 3, getHeight() / 3f);
            canvas.drawRoundRect(rectF, translateX / 2, translateX / 2, paint);
            canvas.restore();
        }

    }
}
