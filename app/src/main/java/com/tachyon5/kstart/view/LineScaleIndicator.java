package com.tachyon5.kstart.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Jack on 2015/10/19.
 */
public class LineScaleIndicator extends Indicator {

    public static final float SCALE = 1.0f;
    float[] scaleYFloats = new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE
    };

    @Override
    public void draw(Canvas canvas, Paint paint) {
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

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        long[] durations = new long[]{1500, 1000, 500, 1000, 1500, 1000, 500, 1000, 1500};
        long[] delays = new long[]{500, 250, 500, 250, 500, 250, 500, 250, 500};
        for (int i = 0; i < 9; i++) {
            final int index = i;
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.3f, 1);
            scaleAnim.setDuration(durations[i]);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            addUpdateListener(scaleAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleYFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(scaleAnim);
        }
        return animators;
    }

}
