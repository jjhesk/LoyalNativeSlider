package com.hkm.slider.Tricks;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.hkm.slider.SliderAdapter;

/**
 * Created by hesk on 17/3/16.
 */
public class AnimationHelper {
    public static long mTransitionAnimation = 1000;

    public static void notify_component(final @Nullable View mObject, final SliderAdapter mSliderAdapter, Handler postHandler, final int delayPost) {
        mTransitionAnimation = delayPost;
        notify_component(mObject, mSliderAdapter, postHandler);
    }

    public static void notify_component(final @Nullable View mObject, final SliderAdapter mSliderAdapter, Handler postHandler) {
        if (mObject == null) return;
        int count = mSliderAdapter.getCount();
        if (count <= 1) {
            postHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mObject != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mObject.animate().alpha(0).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    mObject.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            mObject.animate().alpha(0);
                        }
                    }
                }
            }, mTransitionAnimation);
        } else {
            if (mObject.getVisibility() != View.GONE) return;
            postHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mObject != null) {
                        if (mObject.getVisibility() != View.GONE) return;
                        mObject.setVisibility(View.VISIBLE);
                        mObject.setAlpha(0);
                        mObject.animate().alpha(1);
                    }
                }
            }, mTransitionAnimation);
        }
    }


}
