package com.hkm.slider.Indicators;

import android.content.Context;
import android.database.DataSetObserver;
<<<<<<< Updated upstream
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.slider.R;
=======
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hkm.slider.R;
import com.hkm.slider.SliderAdapter;
>>>>>>> Stashed changes
import com.hkm.slider.Tricks.InfinitePagerAdapter;
import com.hkm.slider.Tricks.ViewPagerEx;

/**
 * Created by hesk on 6/28/2015.
 */
public class NumContainer<customText extends TextView> implements IContainer, ViewPagerEx.OnPageChangeListener {
<<<<<<< Updated upstream
    @Override
    public int getResLayout() {
        return R.layout.defaultnumberindicator;
=======
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
        if (totalSlides == 0) {
            return;
        }
        int n = position % totalSlides + 1;
        update(now = n, totalSlides);
>>>>>>> Stashed changes
    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
<<<<<<< Updated upstream
    public int getTextField() {
        return R.id.numberholder;
=======
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

    public enum Alignment {
        Center_Bottom("Center_Bottom") {
            @Override
            public void makeParam(final RelativeLayout.LayoutParams param, final RelativeLayout rv) {
                param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM | RelativeLayout.CENTER_HORIZONTAL);
            }
        },
        Right_Bottom("Right_Bottom") {
            @Override
            public void makeParam(final RelativeLayout.LayoutParams param, final RelativeLayout rv) {
                param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
        },
        Left_Bottom("Left_Bottom") {
            @Override
            public void makeParam(final RelativeLayout.LayoutParams param, final RelativeLayout rv) {
                param.addRule(RelativeLayout.ALIGN_PARENT_LEFT | RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
        },
        Center_Top("Center_Top") {
            @Override
            public void makeParam(final RelativeLayout.LayoutParams param, final RelativeLayout rv) {
                param.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.CENTER_HORIZONTAL);
            }
        },
        Right_Top("Right_Top") {
            @Override
            public void makeParam(final RelativeLayout.LayoutParams param, final RelativeLayout rv) {
                param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_TOP);
            }
        },
        Left_Top("Left_Top") {
            @Override
            public void makeParam(final RelativeLayout.LayoutParams param, final RelativeLayout rv) {
                param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
        };

        private final String name;
        // private final int id;

        Alignment(String name) {
            this.name = name;
            //  this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public abstract void makeParam(final RelativeLayout.LayoutParams param, final RelativeLayout rv);
>>>>>>> Stashed changes
    }

    @Override
    public int getResLayout() {
        return R.layout.default_num_layout;
    }

    @Override
    public int getTextField() {
        return R.id.tvv;
    }


    protected int totalSlides, now;
<<<<<<< Updated upstream
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

=======
    protected Alignment malignment;
    protected RelativeLayout container;
    protected final View wrapper;
    protected final customText tv;
    protected boolean isInfinite = false;

    public NumContainer(Context c) {
        wrapper = LayoutInflater.from(c).inflate(getResLayout(), null, false);
        tv = (customText) wrapper.findViewById(getTextField());
    }

    public NumContainer setAlignment(Alignment ali) {
        malignment = ali;
        return this;
    }


    public NumContainer withView(RelativeLayout container) {
        this.container = container;
        adjustmentContainer(container);
        return this;
    }

    /**
     * this is provided for making additional customization for contain look and feel
     *
     * @param container Relativelayout
     */
    protected void adjustmentContainer(RelativeLayout container) {
    }

    public NumContainer setViewPager(ViewPagerEx pager) {
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("Viewpager does not have adapter instance");
        }
        mPager = pager;
        mPager.addOnPageChangeListener(this);
        ((InfinitePagerAdapter) mPager.getAdapter()).getRealAdapter().registerDataSetObserver(dataChangeObserver);
        return this;
>>>>>>> Stashed changes
    }

    public void build() {
        if (container == null) {
            throw new NullPointerException("Container is not an instance");
        }
        if (malignment == null) malignment = Alignment.Center_Bottom;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) container.getLayoutParams();
        malignment.makeParam(params, container);
        container.setLayoutParams(params);
        container.requestLayout();
    }

    protected ViewPagerEx mPager;


    private DataSetObserver dataChangeObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            PagerAdapter adapter = mPager.getAdapter();
            if (adapter instanceof InfinitePagerAdapter) {
                totalSlides = ((InfinitePagerAdapter) adapter).getRealCount();
            } else {
                totalSlides = adapter.getCount();
            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    protected void update(final int now, final int total) {
        tv.setText(now + "/" + total);
    }

    /**
     * called by module
     *
     * @return View object
     */
    public View getView() {
        return wrapper;
    }


}
