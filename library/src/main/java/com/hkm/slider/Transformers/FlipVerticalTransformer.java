package com.hkm.slider.Transformers;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by hesk on 6/25/2015.
 */
public class FlipVerticalTransformer extends BaseTransformer {


    @Override
    protected void onTransform(View view, float position) {
        final float rotation = -180f * position;
        ViewCompat.setAlpha(view, rotation > 90f || rotation < -90f ? 0f : 1f);
        ViewCompat.setPivotX(view, view.getWidth() * 0.5f);
        ViewCompat.setPivotY(view, view.getHeight() * 0.5f);
        ViewCompat.setRotationX(view, rotation);

    }

}
