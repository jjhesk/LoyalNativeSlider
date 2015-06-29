package com.hkm.slider.Indicators;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.slider.R;
import com.hkm.slider.Tricks.InfinitePagerAdapter;
import com.hkm.slider.Tricks.ViewPagerEx;

/**
 * Created by hesk on 6/28/2015.
 */
public class NumContainer<customText extends TextView> implements IContainer, ViewPagerEx.OnPageChangeListener {
    @Override
    public int getResLayout() {
        return R.layout.defaultnumberindicator;
    }

    @Override
    public int getTextField() {
        return R.id.numberholder;
    }

    @Override
    public int getAlignment() {
        return 0;
    }

    protected customText tv;
    protected int totalSlides, now;
    protected FrameLayout holder;
    protected View childview;

    public NumContainer(Context c) {
        View v = LayoutInflater.from(c).inflate(getResLayout(), null, false);
        tv = (customText) v.findViewById(getTextField());
        childview = v;
    }

    public NumContainer setTotal(final int k) {
        totalSlides = k;
        return this;
    }

    public NumContainer setAtSlide(final int e) {
        if (e > totalSlides) {
            now = totalSlides;
        } else {
            now = e;
        }
        return this;
    }

    public String buildText() {
        return now + "/" + totalSlides;
    }

    public void notifyDataSet() {
        if (holder != null && tv != null) {
            tv.setText(buildText());
        }
    }

    public boolean redraw() {
        destroySelf();
        if (holder != null) {
            holder.addView(childview);
            totalSlides = getShouldDrawCount();
        }
        return holder != null;
    }

    public void destroySelf() {
        if (holder != null) {
            holder.removeAllViews();
        }

        if (mPager == null || mPager.getAdapter() == null) {
            return;
        }
        InfinitePagerAdapter wrapper = (InfinitePagerAdapter) mPager.getAdapter();
        PagerAdapter adapter = wrapper.getRealAdapter();
        if (adapter != null) {
            adapter.unregisterDataSetObserver(dataChangeObserver);
        }
        //   removeAllViews();
        ShapeDrawable shapeDrawable;
    }

    public void initNumberContainer(View root) {
        holder = (FrameLayout) root.findViewById(R.id.number_count_layout);
        holder.addView(childview);
        tv.setText(buildText());
    }

    public void setIndicatorVisibility(PagerIndicator.IndicatorVisibility visibility) {
        if (holder != null) {
            if (visibility == PagerIndicator.IndicatorVisibility.Visible) {
                holder.setVisibility(View.VISIBLE);
            } else {
                holder.setVisibility(View.INVISIBLE);
            }
        }
    }

    private ViewPagerEx mPager;

    public void setViewPager(ViewPagerEx pager) {
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("Viewpager does not have adapter instance");
        }
        mPager = pager;
        mPager.setOnPageChangeListener(this);
        ((InfinitePagerAdapter) mPager.getAdapter()).getRealAdapter().registerDataSetObserver(dataChangeObserver);
    }

    private int getShouldDrawCount() {
        if (mPager.getAdapter() instanceof InfinitePagerAdapter) {
            return ((InfinitePagerAdapter) mPager.getAdapter()).getRealCount();
        } else {
            return mPager.getAdapter().getCount();
        }
    }

    private DataSetObserver dataChangeObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            PagerAdapter adapter = mPager.getAdapter();
            int count = 0;
            if (adapter instanceof InfinitePagerAdapter) {
                count = ((InfinitePagerAdapter) adapter).getRealCount();
            } else {
                count = adapter.getCount();
            }
            totalSlides = count;
            // mPager.setCurrentItem(totalSlides * 20 + mPager.getCurrentItem());
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            redraw();
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        now = position;
        notifyDataSet();
    }

    @Override
    public void onPageSelected(int position) {
        now = position;
        notifyDataSet();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
