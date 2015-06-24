package com.hkm.slider.Transformers;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by hesk on 6/25/2015.
 */
public class DrawFromBackTransformer extends BaseTransformer {
    private static final float MIN_SCALE = 0.75f;
    @Override
    protected void onTransform(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1f || position > 1f) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewCompat.setAlpha(view, 0);
            return;
        }
        if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            // Fade the page out.
            ViewCompat.setAlpha(view, 1 + position);
            // Counteract the default slide transition
            ViewCompat.setTranslationX(view, pageWidth * -position);
            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE + (1f - MIN_SCALE) * (1f - Math.abs(position));
            ViewCompat.setScaleX(view, scaleFactor);
            ViewCompat.setScaleY(view, scaleFactor);
            return;

        }
        if (position > 0.5f && position <= 1) { // (0,1]
            // Fade the page out.
            ViewCompat.setAlpha(view, 0);
            // Counteract the default slide transition
            ViewCompat.setTranslationX(view, pageWidth * -position);
            return;
        }
        if (position > 0.3f && position <= 0.5f) { // (0,1]
            // Fade the page out.
            ViewCompat.setAlpha(view, 1);
            // Counteract the default slide transition
            ViewCompat.setTranslationX(view, pageWidth * position);
            float scaleFactor = MIN_SCALE;
            ViewCompat.setScaleX(view, scaleFactor);
            ViewCompat.setScaleY(view, scaleFactor);
            return;
        }
        if (position <= 0.3f) { // (0,1]
            // Fade the page out.
            ViewCompat.setAlpha(view, 1);
            // Counteract the default slide transition
            ViewCompat.setTranslationX(view, pageWidth * position);
            // Scale the page down (between MIN_SCALE and 1)
            float v = (float) (0.3f - position);
            v = v >= 0.25f ? 0.25f : v;
            float scaleFactor = MIN_SCALE + v;
            ViewCompat.setScaleX(view, scaleFactor);
            ViewCompat.setScaleY(view, scaleFactor);
        }
    }
}
