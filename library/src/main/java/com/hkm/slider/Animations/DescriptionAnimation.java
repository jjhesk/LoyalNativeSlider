package com.hkm.slider.Animations;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.hkm.slider.R;


/**
 * A demo class to show how to use {@link com.hkm.slider.Animations.BaseAnimationInterface}
 * to make  your custom animation in {@link com.hkm.slider.Tricks.ViewPagerEx.PageTransformer} action.
 */
public class DescriptionAnimation implements BaseAnimationInterface {

    @Override
    public void onPrepareCurrentItemLeaveScreen(View current) {
        View descriptionLayout = current.findViewById(R.id.ns_desc_frame);
        if (descriptionLayout != null) {
            current.findViewById(R.id.ns_desc_frame).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * When next item is coming to show, let's hide the description layout.
     *
     * @param next view
     */
    @Override
    public void onPrepareNextItemShowInScreen(View next) {
        View descriptionLayout = next.findViewById(R.id.ns_desc_frame);
        if (descriptionLayout != null) {
            next.findViewById(R.id.ns_desc_frame).setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onCurrentItemDisappear(View view) {

    }

    /**
     * When next item show in ViewPagerEx, let's make an animation to show the
     * description layout.
     *
     * @param view view
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onNextItemAppear(View view) {

        View descriptionLayout = view.findViewById(R.id.ns_desc_frame);
        if (descriptionLayout != null) {
            float layoutY = ViewCompat.getY(descriptionLayout);
            view.findViewById(R.id.ns_desc_frame).setVisibility(View.VISIBLE);
            ValueAnimator animator = ObjectAnimator.ofFloat(
                    descriptionLayout,
                    "y",
                    (float) (layoutY + descriptionLayout.getHeight()),
                    layoutY)
                    .setDuration(500);
            animator.start();
        }

    }
}
