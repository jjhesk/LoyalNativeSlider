package com.hkm.slider.Animations;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.BaseInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.hkm.slider.R;


/**
 * A demo class to show how to use {@link com.hkm.slider.Animations.BaseAnimationInterface}
 * to make  your custom animation in {@link com.hkm.slider.Tricks.ViewPagerEx.PageTransformer} action.
 */
public class DescriptionAnimation implements BaseAnimationInterface {

    private int time_threadhold = 400;
    private TimeInterpolator animation_behavior;
    private Handler handler = new Handler();

    /**
     * @param animation_behavior the interpolator behavior
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public DescriptionAnimation(BaseInterpolator animation_behavior) {
        this.animation_behavior = animation_behavior;
    }

    /**
     * @param min_sec            additional setting minute second that controls the time threadhold between each slide
     * @param animation_behavior the interpolator behavior
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public DescriptionAnimation(int min_sec, BaseInterpolator animation_behavior) {
        this.time_threadhold = min_sec;
        this.animation_behavior = animation_behavior;
    }

    /**
     * @param min_sec additional setting minute second that controls the time threadhold between each slide
     */
    public DescriptionAnimation(int min_sec) {
        this.time_threadhold = min_sec;
    }

    public DescriptionAnimation() {

    }

    @Override
    public void onPrepareCurrentItemLeaveScreen(View current) {
        frame_disappear(current);
    }

    /**
     * When next item is coming to show, let's hide the description layout.
     *
     * @param next view
     */
    @Override
    public void onPrepareNextItemShowInScreen(final View next) {
        frame_disappear(next);
    }

    protected void frame_disappear(View next) {
        int startm = layoutCounts(next);
        if (startm == 1) {
            View descriptionLayout = next.findViewById(R.id.ns_desc_frame);
            if (descriptionLayout != null) descriptionLayout.setVisibility(View.INVISIBLE);
        } else {
            for (int n = 1; n < startm; n++) {
                getFrameLayoutDescriptionView(next, n).setVisibility(View.INVISIBLE);
            }
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
    public void onNextItemAppear(final View view) {
        int startm = layoutCounts(view);
        if (startm == 1) {
            View descriptionLayout = view.findViewById(R.id.ns_desc_frame);
            workOnTarget(descriptionLayout);
        } else {
            for (int n = 1; n < startm; n++) {
                final int h = n;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        workOnTarget(getFrameLayoutDescriptionView(view, h));
                    }
                }, getDelayThreadHold(n - 1));
            }
        }

    }

    private int layoutCounts(View view) {
        int startm = 1;
        while (hasFrame(view, startm)) {
            startm++;
        }

        return startm;
    }

    private int getDelayThreadHold(int i) {
        return i * time_threadhold;
    }

    protected boolean hasFrame(View view, int i) {
        int Id = view.getContext().getResources().getIdentifier("ns_display_i_s" + i, "id", view.getContext().getPackageName());
        return view.findViewById(Id) != null;
    }

    protected View getFrameLayoutDescriptionView(View view, int i) {
        int Id = view.getContext().getResources().getIdentifier("ns_display_i_s" + i, "id", view.getContext().getPackageName());
        View descriptionLayout = view.findViewById(Id).findViewById(R.id.ns_desc_frame);
        return descriptionLayout;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void workOnTarget(@Nullable View descriptionLayout) {
        if (descriptionLayout == null) return;
        float layoutY = ViewCompat.getY(descriptionLayout);
        descriptionLayout.setVisibility(View.VISIBLE);
        ValueAnimator animator = ObjectAnimator.ofFloat(
                descriptionLayout,
                "y",
                (float) (layoutY + descriptionLayout.getHeight()),
                layoutY)
                .setDuration(500);

        if (animation_behavior != null) {
            animator.setInterpolator(animation_behavior);
        }
        animator.start();
    }
}
