package com.hkm.slider.Transformers;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by hesk on 6/24/2015.
 */
public class ParallaxPageTransformer extends BaseTransformer {

    private final int viewToParallax;

    public ParallaxPageTransformer(@IdRes final int viewToParallax) {
        this.viewToParallax = viewToParallax;
    }

    @Override
    protected void onTransform(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1f) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewCompat.setAlpha(view, 1f);
        } else if (position <= 1f) { // [-1,1]
            ViewCompat.setTranslationX(view.findViewById(viewToParallax), -position * (pageWidth / 2)); //Half the normal speed

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewCompat.setAlpha(view, 1f);
        }


    }

}