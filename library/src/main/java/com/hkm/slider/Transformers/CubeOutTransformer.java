package com.hkm.slider.Transformers;

/**
 * Created by hesk on 6/25/2015.
 */

import android.support.v4.view.ViewCompat;
import android.view.View;

public class CubeOutTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        ViewCompat.setPivotX(view, position < 0f ? view.getWidth() : 0f);
        ViewCompat.setPivotY(view, view.getHeight() * 0.5f);
        ViewCompat.setRotationY(view, 90f * position);
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

}