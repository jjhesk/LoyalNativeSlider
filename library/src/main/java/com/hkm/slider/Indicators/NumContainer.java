package com.hkm.slider.Indicators;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hkm.slider.R;

import android.widget.RelativeLayout;

import com.hkm.slider.Tricks.InfinitePagerAdapter;
import com.hkm.slider.Tricks.ViewPagerEx;

/**
 * Created by hesk on 6/28/2015.
 */
public class NumContainer<customText extends TextView> implements IContainer, ViewPagerEx.OnPageChangeListener {
    protected int mTotalSlides, mCurrentSlide;
    private ViewPagerEx mPager;
    protected RelativeLayout mContainer;
    protected View mWrapper;
    protected customText tv;
    protected boolean isInfinite = false;
    protected Context mContext;

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mTotalSlides == 0) {
            return;
        }
        int n = position % mTotalSlides + 1;
        update(mCurrentSlide = n, mTotalSlides);

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPagerEx#SCROLL_STATE_IDLE
     * @see ViewPagerEx#SCROLL_STATE_DRAGGING
     * @see ViewPagerEx#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public int getResLayout() {
        return R.layout.default_num_layout;
    }

    @Override
    public int getTextField() {
        return R.id.ns_number_holder;
    }


    public NumContainer(Context c) {
        mContext = c;
        setCustomLayoutForCustomAttachDisplay(getResLayout());
    }

    public NumContainer(Context c, @LayoutRes int mLayout) {
        this(c);
        setCustomLayoutForCustomAttachDisplay(mLayout);
    }

    public NumContainer setCustomLayoutForCustomAttachDisplay(@LayoutRes int mLayout) {
        mWrapper = LayoutInflater.from(mContext).inflate(mLayout, null, false);
        tv = (customText) mWrapper.findViewById(getTextField());
        return this;
    }


    public NumContainer setViewPager(ViewPagerEx pager) {
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("Viewpager does not have adapter instance");
        }
        mPager = pager;
        mPager.addOnPageChangeListener(this);
        ((InfinitePagerAdapter) mPager.getAdapter()).getRealAdapter().registerDataSetObserver(dataChangeObserver);
        return this;
    }

    public NumContainer withView(RelativeLayout mContainer) {
        this.mContainer = mContainer;
        return this;
    }

    public void build() {
        if (mContainer == null) {
            throw new NullPointerException("Container is not an instance");
        }
        mContainer.removeAllViews();
        mContainer.addView(mWrapper);
    }


    private DataSetObserver dataChangeObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            PagerAdapter adapter = mPager.getAdapter();
            if (adapter instanceof InfinitePagerAdapter) {
                mTotalSlides = ((InfinitePagerAdapter) adapter).getRealCount();
            } else {
                mTotalSlides = adapter.getCount();
            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    protected void update(final int mCurrentSlide, final int total) {
        tv.setText(mCurrentSlide + "/" + total);
    }

    /**
     * called by module
     *
     * @return View object
     */
    public View getView() {
        return mWrapper;
    }


}
