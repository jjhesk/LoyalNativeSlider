package com.hkm.slider.Transformers;

import android.support.v4.view.ViewCompat;
import android.view.View;

import com.hkm.slider.Animations.BaseAnimationInterface;
import com.hkm.slider.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is all transformers father.
 * /
 * BaseTransformer implement {@link com.hkm.slider.Tricks.ViewPagerEx.PageTransformer}
 * which is just same as {@link android.support.v4.view.ViewPager.PageTransformer}.
 * /
 * After you call setPageTransformer(), transformPage() will be called by {@link com.hkm.slider.Tricks.ViewPagerEx}
 * when your slider are animating.
 * /
 * In onPreTransform() function, that will make {@link com.hkm.slider.Animations.BaseAnimationInterface}
 * work.
 * /
 * if you want to make an acceptable transformer, please do not forget to extend from this class.
 */
public abstract class BaseTransformer implements ViewPagerEx.PageTransformer {

    private BaseAnimationInterface mCustomAnimationInterface;

    /**
     * Called each {@link #transformPage(View, float)}.
     *
     * @param view     view
     * @param position float position
     */
    protected abstract void onTransform(View view, float position);

    private HashMap<View, ArrayList<Float>> h = new HashMap<View, ArrayList<Float>>();

    @Override
    public void transformPage(View view, float position) {
        onPreTransform(view, position);
        onTransform(view, position);
        onPostTransform(view, position);
    }

    /**
     * If the position offset of a fragment is less than negative one or greater than one, returning true will set the
     * visibility of the fragment to {@link View#GONE}. Returning false will force the fragment to {@link View#VISIBLE}.
     *
     * @return boolean   boolean
     */
    protected boolean hideOffscreenPages() {
        return true;
    }

    /**
     * Indicates if the default animations of the view pager should be used.
     *
     * @return boolean   boolean
     */
    protected boolean isPagingEnabled() {
        return false;
    }

    /**
     * Called each {@link #transformPage(View, float)} before {{@link #onTransform(View, float)} is called.
     *
     * @param view     view
     * @param position position
     */
    protected void onPreTransform(View view, float position) {
        final float width = view.getWidth();

        ViewCompat.setRotationX(view, 0);
        ViewCompat.setRotationY(view, 0);
        ViewCompat.setRotation(view, 0);
        ViewCompat.setScaleX(view, 1);
        ViewCompat.setScaleY(view, 1);
        ViewCompat.setPivotX(view, 0);
        ViewCompat.setPivotY(view, 0);
        ViewCompat.setTranslationY(view, 0);
        ViewCompat.setTranslationX(view, isPagingEnabled() ? 0f : -width * position);

        if (hideOffscreenPages()) {
            ViewCompat.setAlpha(view, position <= -1f || position >= 1f ? 0f : 1f);
        } else {
            ViewCompat.setAlpha(view, 1f);
        }
        if (mCustomAnimationInterface != null) {
            if (h.containsKey(view) == false || h.get(view).size() == 1) {
                if (position > -1 && position < 1) {
                    if (h.get(view) == null) {
                        h.put(view, new ArrayList<Float>());
                    }
                    h.get(view).add(position);
                    if (h.get(view).size() == 2) {
                        float zero = h.get(view).get(0);
                        float cha = h.get(view).get(1) - h.get(view).get(0);
                        if (zero > 0) {
                            if (cha > -1 && cha < 0) {
                                //in
                                mCustomAnimationInterface.onPrepareNextItemShowInScreen(view);
                            } else {
                                //out
                                mCustomAnimationInterface.onPrepareCurrentItemLeaveScreen(view);
                            }
                        } else {
                            if (cha > -1 && cha < 0) {
                                //out
                                mCustomAnimationInterface.onPrepareCurrentItemLeaveScreen(view);
                            } else {
                                //in
                                mCustomAnimationInterface.onPrepareNextItemShowInScreen(view);
                            }
                        }
                    }
                }
            }
        }
    }

    boolean isApp, isDis;

    /**
     * Called each {@link #transformPage(View, float)} call after {@link #onTransform(View, float)} is finished.
     *
     * @param view     view
     * @param position position
     */
    protected void onPostTransform(View view, float position) {
        if (mCustomAnimationInterface != null) {
            if (position == -1 || position == 1) {
                mCustomAnimationInterface.onCurrentItemDisappear(view);
                isApp = true;
            } else if (position == 0) {
                mCustomAnimationInterface.onNextItemAppear(view);
                isDis = true;
            }
            if (isApp && isDis) {
                h.clear();
                isApp = false;
                isDis = false;
            }
        }
    }


    public void setCustomAnimationInterface(BaseAnimationInterface animationInterface) {
        mCustomAnimationInterface = animationInterface;
    }

}