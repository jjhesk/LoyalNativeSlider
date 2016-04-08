package com.hkm.slider.GalleryWidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hkm.slider.TouchView.TouchImageView;


/**
 * This class implements method to help <b>TouchImageView</b> fling, draggin and scaling.
 */
public class GalleryViewPager extends ViewPager {

    PointF last;
    public TouchImageView mCurrentView;

    /**
     * add OnItemClickListener interface
     */
    protected OnItemClickListener mOnItemClickListener;

    public GalleryViewPager(Context context) {
        super(context);
    }

    public GalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private float[] handleMotionEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                last = new PointF(event.getX(0), event.getY(0));
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                PointF curr = new PointF(event.getX(0), event.getY(0));
                return new float[]{curr.x - last.x, curr.y - last.y};

        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            //super.onInterceptTouchEvent(event);

            float endX = event.getX();
            float endY = event.getY();
            if (isAClick(startX, endX, startY, endY)) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClicked(mCurrentView, getCurrentItem());
                }
                //launchFullPhotoActivity(imageUrls);// WE HAVE A CLICK!!
            } else {
                super.onTouchEvent(event);
            }
        }

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
        }

        /*if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP)
        {
            super.onTouchEvent(event);
        }*/

        float[] difference = handleMotionEvent(event);

        if (mCurrentView.pagerCanScroll()) {
            return super.onTouchEvent(event);
        } else {
            if (difference != null && mCurrentView.onRightSide && difference[0] < 0) //move right
            {
                return super.onTouchEvent(event);
            }
            if (difference != null && mCurrentView.onLeftSide && difference[0] > 0) //move left
            {
                return super.onTouchEvent(event);
            }
            if (difference == null && (mCurrentView.onLeftSide || mCurrentView.onRightSide)) {
                return super.onTouchEvent(event);
            }
        }

        return false;
    }

    private float startX;
    private float startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            //super.onInterceptTouchEvent(event);

            float endX = event.getX();
            float endY = event.getY();
            if (isAClick(startX, endX, startY, endY)) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClicked(mCurrentView, getCurrentItem());
                }
            } else {
                super.onInterceptTouchEvent(event);
            }
        }

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
        }


        float[] difference = handleMotionEvent(event);

        if (mCurrentView.pagerCanScroll()) {
            return super.onInterceptTouchEvent(event);
        } else {
            if (difference != null && mCurrentView.onRightSide && difference[0] < 0) //move right
            {
                return super.onInterceptTouchEvent(event);
            }
            if (difference != null && mCurrentView.onLeftSide && difference[0] > 0) //move left
            {
                return super.onInterceptTouchEvent(event);
            }
            if (difference == null && (mCurrentView.onLeftSide || mCurrentView.onRightSide)) {
                return super.onInterceptTouchEvent(event);
            }
        }
        return false;
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        if (differenceX > CLICK_ACTION_THRESHHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHHOLD) {
            return false;
        }
        return true;
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    private final static int CLICK_ACTION_THRESHHOLD = 5;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
};