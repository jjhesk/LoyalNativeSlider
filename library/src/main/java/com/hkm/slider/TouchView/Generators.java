package com.hkm.slider.TouchView;

import android.content.Context;
import android.graphics.*;
import android.view.*;
import android.widget.*;

import static android.util.TypedValue.*;
import static android.view.View.MeasureSpec.*;

/**
 * Created by hesk on 8/3/16.
 */
public class Generators {
    /**
     * OP's original implementation fixed for real centering
     *
     * @param context context input
     * @param bitmap  bitmpa
     * @param params  other control factors
     * @return Bitmap object
     */
    public static Bitmap imageWithText(Context context, Bitmap bitmap, GenerateParams params) {
        TextView view = new TextView(context);
        view.setText(params.text);
        view.setTextColor(params.color);
        view.setBackgroundColor(params.background);
        view.setTypeface(null, Typeface.BOLD);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(20 /*sp*/);
        Canvas canvas = new Canvas(bitmap);
        view.measure(makeMeasureSpec(canvas.getWidth(), EXACTLY), makeMeasureSpec(canvas.getHeight(), EXACTLY));
        view.layout(0, 0, canvas.getWidth(), canvas.getHeight());
        view.draw(canvas);
        return bitmap;
    }

    /**
     * Generate centered text without creating a View, more lightweight.
     * Consider http://stackoverflow.com/a/8369690/253468 for multiline support.
     *
     * @param context context input
     * @param bitmap  bitmpa
     * @param params  other control factors
     * @return Bitmap object
     */
    public static Bitmap imageWithTextNoLayout(Context context, Bitmap bitmap, GenerateParams params) {
        Paint paint = new Paint();
        paint.setColor(params.color);
        paint.setTextAlign(Paint.Align.CENTER); // text's anchor for the x given in drawText
        paint.setTextSize(applyDimension(COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics()));
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(params.background);
        canvas.drawText(params.text, canvas.getWidth() / 2, canvas.getHeight() / 2, paint);
        return bitmap;
    }
}