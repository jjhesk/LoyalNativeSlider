package com.hkm.slider;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hkm.slider.Animations.BaseAnimationInterface;
import com.hkm.slider.Indicators.PagerIndicator;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.Transformers.BaseTransformer;
import com.hkm.slider.Tricks.FixedSpeedScroller;
import com.hkm.slider.Tricks.InfinitePagerAdapter;
import com.hkm.slider.Tricks.InfiniteViewPager;
import com.hkm.slider.Tricks.MultiViewPager;
import com.hkm.slider.Tricks.ViewPagerEx;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SliderLayout is compound layout. This is combined with {@link com.hkm.slider.Indicators.PagerIndicator}
 * and {@link com.hkm.slider.Tricks.ViewPagerEx} .
 * /
 * There is some properties you can set in XML:
 * /
 * indicator_visibility
 * visible
 * invisible
 * /
 * indicator_shape
 * oval
 * rect
 * /
 * indicator_selected_color
 * /
 * indicator_unselected_color
 * /
 * indicator_selected_drawable
 * /
 * indicator_unselected_drawable
 * /
 * pager_animation
 */
public class SliderLayout extends RelativeLayout {

    private Context mContext;
    /**
     * InfiniteViewPager is extended from ViewPagerEx. As the name says, it can scroll without bounder.
     */
    private InfiniteViewPager mViewPager;
    /**
     * multiViewPager
     */
    private MultiViewPager mMViewPager;
    /**
     * InfiniteViewPager adapter.
     */
    private SliderAdapter mSliderAdapter;

    /**
     * {@link com.hkm.slider.Tricks.ViewPagerEx} indicator.
     */
    private PagerIndicator mIndicator;


    /**
     * A timer and a TimerTask using to cycle the {@link com.hkm.slider.Tricks.ViewPagerEx}.
     */
    private Timer mCycleTimer;
    private TimerTask mCycleTask;

    /**
     * For resuming the cycle, after user touch or click the {@link com.hkm.slider.Tricks.ViewPagerEx}.
     */
    private Timer mResumingTimer;
    private TimerTask mResumingTask;
    private int pagerType = 0;
    /**
     * If {@link com.hkm.slider.Tricks.ViewPagerEx} is Cycling
     */
    private boolean mCycling;
    private boolean sidebuttons;
    /**
     * Determine if auto recover after user touch the {@link com.hkm.slider.Tricks.ViewPagerEx}
     */
    private boolean mAutoRecover = true;

    private int mTransformerId;
    private int mSliderIndicatorPresentations;
    /**
     * {@link com.hkm.slider.Tricks.ViewPagerEx} transformer time span.
     */
    private int mTransformerSpan = 1100;

    private boolean mAutoCycle;

    private boolean mIsShuffle = false;

    /**
     * the duration between animation.
     */
    private long mSliderDuration = 4000;

    /**
     * Visibility of {@link com.hkm.slider.Indicators.PagerIndicator}
     */
    private PagerIndicator.IndicatorVisibility mIndicatorVisibility = PagerIndicator.IndicatorVisibility.Visible;

    /**
     * {@link com.hkm.slider.Tricks.ViewPagerEx} 's transformer
     */
    private BaseTransformer mViewPagerTransformer;

    /**
     * @see com.hkm.slider.Animations.BaseAnimationInterface
     */
    private BaseAnimationInterface mCustomAnimation;

    /**
     * the margin for setting the view pager margin distance
     */
    private int mPagerMargin;

    /**
     * {@link com.hkm.slider.Indicators.PagerIndicator} shape, rect or oval.
     *
     * @param context context
     */
    public SliderLayout(Context context) {
        this(context, null);
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SliderStyle);
    }

    public SliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.slider_layout, this, true);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SliderLayout, defStyle, 0);
        mTransformerSpan = attributes.getInteger(R.styleable.SliderLayout_pager_animation_span, 1100);
        mTransformerId = attributes.getInt(R.styleable.SliderLayout_pager_animation, TransformerL.Default.ordinal());
        mSliderIndicatorPresentations = attributes.getInt(R.styleable.SliderLayout_lns_use_presentation, PresentationConfig.Smart.ordinal());
        mAutoCycle = attributes.getBoolean(R.styleable.SliderLayout_auto_cycle, true);
        sidebuttons = attributes.getBoolean(R.styleable.SliderLayout_slider_side_buttons, false);
        int visibility = attributes.getInt(R.styleable.SliderLayout_indicator_visibility, 0);
        for (PagerIndicator.IndicatorVisibility v : PagerIndicator.IndicatorVisibility.values()) {
            if (v.ordinal() == visibility) {
                mIndicatorVisibility = v;
                break;
            }
        }
        mSliderAdapter = new SliderAdapter(mContext);
        mSliderAdapter.registerDataSetObserver(sliderDataObserver);
        pagerSetup();
        attributes.recycle();
        setPresetIndicator(PresetIndicators.Center_Bottom);
        setPresetTransformer(mTransformerId);
        setSliderTransformDuration(mTransformerSpan, null);
        setIndicatorVisibility(mIndicatorVisibility);
        if (mAutoCycle) {
            startAutoCycle();
        }
        buttonNSetup();

    }

    private void buttonNSetup() {
        ImageView Arrr_L = (ImageView) findViewById(R.id.arrow_l);
        ImageView Arrr_R = (ImageView) findViewById(R.id.arrow_r);
        if (!sidebuttons) {
            Arrr_L.setVisibility(GONE);
            Arrr_R.setVisibility(GONE);
        } else {
            Arrr_L.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveNextPosition(true);
                }
            });
            Arrr_R.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    movePrevPosition(true);
                }
            });
        }
    }

    private void pagerSetup() {
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(mSliderAdapter);
        if (pagerType == 0) {
            mViewPager = (InfiniteViewPager) findViewById(R.id.daimajia_slider_viewpager);
            if (mPagerMargin > -1) {
                mViewPager.setMargin(mPagerMargin);
            }
            mViewPager.setAdapter(wrappedAdapter);
            mViewPager.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_UP:
                            recoverCycle();
                            break;
                    }
                    return false;
                }
            });
        } else if (pagerType == 1) {
        }
    }


    /**
     * set the current slider
     *
     * @param position the insert position
     * @param smooth   if that is smooth or not
     */
    public void setCurrentPosition(int position, boolean smooth) {
        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");
        if (position >= getRealAdapter().getCount()) {
            throw new IllegalStateException("Item position is not exist");
        }
        int p = mViewPager.getCurrentItem() % getRealAdapter().getCount();
        int n = (position - p) + mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(n, smooth);
    }

    /**
     * set the current position
     *
     * @param position the int in page limit
     */
    public void setCurrentPosition(int position) {
        setCurrentPosition(position, true);
    }

    /**
     * {@link ViewPagerEx#setOffscreenPageLimit(int)} open ViewPager API.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     */
    public void setOffscreenPageLimit(int limit) {
        mViewPager.setOffscreenPageLimit(limit);
    }

    /**
     * set the custom indicator
     *
     * @param indicator the indicator type
     */
    public void setCustomIndicator(PagerIndicator indicator) {
        if (mIndicator != null) {
            mIndicator.destroySelf();
        }
        mIndicator = indicator;
        mIndicator.setIndicatorVisibility(mIndicatorVisibility);
        mIndicator.setViewPager(mViewPager);
        mIndicator.redraw();
    }

    public <T extends BaseSliderView> void addSlider(T imageContent) {
        mSliderAdapter.addSlider(imageContent);
    }


    /**
     * move to the next slide
     *
     * @param smooth bool
     */
    public void moveNextPosition(boolean smooth) {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, smooth);
        if (mIsShuffle) {
            setPagerTransformer(true, getShuffleTransformer());
        }

    }

    public void moveNextPosition() {
        moveNextPosition(true);
    }


    /**
     * move to prev slide.
     *
     * @param smooth bool
     */
    public void movePrevPosition(boolean smooth) {
        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, smooth);
    }

    public void movePrevPosition() {
        movePrevPosition(true);
    }

    public void slideToNextItem() {
        mViewPager.nextItem();
    }

    public void slideToPreviousItem() {
        mViewPager.beforeItem();
    }

    private android.os.Handler mh = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.nextItem();
        }
    };

    public void startAutoCycle() {
        startAutoCycle(1000, mSliderDuration, mAutoRecover);
    }

    /**
     * start auto cycle.
     *
     * @param delay       delay time
     * @param duration    animation duration time.
     * @param autoRecover if recover after user touches the slider.
     */
    public void startAutoCycle(long delay, long duration, boolean autoRecover) {
        if (mCycleTimer != null) mCycleTimer.cancel();
        if (mCycleTask != null) mCycleTask.cancel();
        if (mResumingTask != null) mResumingTask.cancel();
        if (mResumingTimer != null) mResumingTimer.cancel();
        mSliderDuration = duration;
        mCycleTimer = new Timer();
        mAutoRecover = autoRecover;
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                mh.sendEmptyMessage(0);
            }
        };
        mCycleTimer.schedule(mCycleTask, delay, mSliderDuration);
        mCycling = true;
        mAutoCycle = true;
    }

    /**
     * pause auto cycle.
     */
    private void pauseAutoCycle() {
        if (mCycling) {
            mCycleTimer.cancel();
            mCycleTask.cancel();
            mCycling = false;
        } else {
            if (mResumingTimer != null && mResumingTask != null) {
                recoverCycle();
            }
        }
    }

    /**
     * set the duration between two slider changes. the duration value must bigger or equal to 500
     *
     * @param duration how long
     */
    public void setDuration(long duration) {
        if (duration >= 500) {
            mSliderDuration = duration;
            if (mAutoCycle && mCycling) {
                startAutoCycle();
            }
        }
    }

    /**
     * stop the auto circle
     */
    public void stopAutoCycle() {
        if (mCycleTask != null) {
            mCycleTask.cancel();
        }
        if (mCycleTimer != null) {
            mCycleTimer.cancel();
        }
        if (mResumingTimer != null) {
            mResumingTimer.cancel();
        }
        if (mResumingTask != null) {
            mResumingTask.cancel();
        }
        mAutoCycle = false;
        mCycling = false;
    }

    private DataSetObserver sliderDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (mSliderAdapter.getCount() <= 1) {
                pauseAutoCycle();
            } else {
                recoverCycle();
            }
        }
    };

    public void addOnPageChangeListener(ViewPagerEx.OnPageChangeListener onPageChangeListener) {
        if (onPageChangeListener != null) {
            mViewPager.addOnPageChangeListener(onPageChangeListener);
        }
    }

    public void removeOnPageChangeListener(ViewPagerEx.OnPageChangeListener onPageChangeListener) {
        mViewPager.removeOnPageChangeListener(onPageChangeListener);
    }

    /**
     * when paused cycle, this method can weak it up.
     */
    private void recoverCycle() {
        if (!mAutoRecover || !mAutoCycle) {
            return;
        }

        if (!mCycling) {
            if (mResumingTask != null && mResumingTimer != null) {
                mResumingTimer.cancel();
                mResumingTask.cancel();
            }
            mResumingTimer = new Timer();
            mResumingTask = new TimerTask() {
                @Override
                public void run() {
                    startAutoCycle();
                }
            };
            mResumingTimer.schedule(mResumingTask, 6000);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseAutoCycle();
                break;
        }
        return false;
    }

    /**
     * set ViewPager transformer.
     *
     * @param reverseDrawingOrder the boolean for order
     * @param transformer         BaseTransformer
     */
    public void setPagerTransformer(boolean reverseDrawingOrder, BaseTransformer transformer) {
        mViewPagerTransformer = transformer;
        mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        mViewPager.setPageTransformer(reverseDrawingOrder, mViewPagerTransformer);
    }


    /**
     * set the duration between two slider changes.
     *
     * @param period       by how many mil second to slide to the next one
     * @param interpolator with what interpolator
     */
    public void setSliderTransformDuration(int period, Interpolator interpolator) {
        try {
            final Field mScroller = ViewPagerEx.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            final FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), interpolator, period);
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {

        }
    }

    public void setSliderTransformDuration(Interpolator interpolator) {
        setSliderTransformDuration(mTransformerSpan, interpolator);
    }

    /**
     * presentation of indicators
     */
    public enum PresentationConfig {
        Smart("Smart"),
        Dots("Dots"),
        Numbers("Numbers");
        private final String name;

        private PresentationConfig(String s) {
            name = s;
        }

        public String toString() {
            return name;
        }

        public boolean equals(String other) {
            return (other == null) ? false : name.equals(other);
        }

    }

    /**
     * preset transformers and their names
     */


    /**
     * set a preset viewpager transformer by id.
     *
     * @param transformerId the recongized transformer ID
     */
    public void setPresetTransformer(int transformerId) {
        for (TransformerL t : TransformerL.values()) {
            if (t.ordinal() == transformerId) {
                setPresetTransformer(t);
                break;
            }
        }
    }

    /**
     * set preset PagerTransformer via the name of transforemer.
     *
     * @param transformerName the transformer name in string
     */
    public void setPresetTransformer(String transformerName) {
        for (TransformerL t : TransformerL.values()) {
            if (t.equals(transformerName)) {
                setPresetTransformer(t);
                return;
            }
        }
    }

    /**
     * Inject your custom animation into PageTransformer, you can know more details in
     * {@link com.hkm.slider.Animations.BaseAnimationInterface},
     * and you can see a example in {@link com.hkm.slider.Animations.DescriptionAnimation}
     *
     * @param animation the base animation for interface
     */
    public void setCustomAnimation(BaseAnimationInterface animation) {
        mCustomAnimation = animation;
        if (mViewPagerTransformer != null) {
            mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        }
    }

    /**
     * pretty much right? enjoy it. :-D
     *
     * @param ts the transformer object
     */
    public void setPresetTransformer(TransformerL ts) {
        if (ts == TransformerL.Shuffle) {
            setPagerTransformer(true, getShuffleTransformer());
        } else
            setPagerTransformer(true, ts.getTranformFunction());
    }

    /**
     * return a random TransformerL between [0, the length of enum -1)
     *
     * @return BaseTransformer
     */
    public BaseTransformer getShuffleTransformer() {
        mIsShuffle = true;
        return TransformerL.randomSymbol();
    }

    /**
     * Set the visibility of the indicators.
     *
     * @param visibility the page visibility
     */
    public void setIndicatorVisibility(PagerIndicator.IndicatorVisibility visibility) {
        if (mIndicator == null) {
            return;
        }

        mIndicator.setIndicatorVisibility(visibility);
    }

    public PagerIndicator.IndicatorVisibility getIndicatorVisibility() {
        if (mIndicator == null) {
            return mIndicator.getIndicatorVisibility();
        }
        return PagerIndicator.IndicatorVisibility.Invisible;

    }

    /**
     * get the {@link com.hkm.slider.Indicators.PagerIndicator} instance.
     * You can manipulate the properties of the indicator.
     *
     * @return in return with PagerIndicator
     */
    public PagerIndicator getPagerIndicator() {
        return mIndicator;
    }

    /**
     * Preset Indicators
     */
    public enum PresetIndicators {
        Center_Bottom("Center_Bottom", R.id.default_center_bottom_indicator),
        Right_Bottom("Right_Bottom", R.id.default_bottom_right_indicator),
        Left_Bottom("Left_Bottom", R.id.default_bottom_left_indicator),
        Center_Top("Center_Top", R.id.default_center_top_indicator),
        Right_Top("Right_Top", R.id.default_center_top_right_indicator),
        Left_Top("Left_Top", R.id.default_center_top_left_indicator),

        num_Top_Center("num_Top_Center", R.id.default_center_top_left_indicator),
        num_Top_Left("num_Top_Left", R.id.default_center_top_left_indicator),
        num_Top_Right("num_Top_Right", R.id.default_center_top_left_indicator),
        num_Left_Bottom("num_Left_Bottom", R.id.default_center_top_left_indicator),
        num_Right_Bottom("num_Right_Bottom", R.id.default_center_top_left_indicator),
        num_Center_Bottom("num_Center_Bottom", R.id.default_center_top_left_indicator);

        private final String name;
        private final int id;

        private PresetIndicators(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String toString() {
            return name;
        }

        public int getResourceId() {
            return id;
        }
    }

    public void setPresetIndicator(PresetIndicators presetIndicator) {
        PagerIndicator pagerIndicator = (PagerIndicator) findViewById(presetIndicator.getResourceId());
        setCustomIndicator(pagerIndicator);
    }

    private InfinitePagerAdapter getWrapperAdapter() {
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null) {
            return (InfinitePagerAdapter) adapter;
        } else {
            return null;
        }
    }

    private SliderAdapter getRealAdapter() {
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null) {
            return ((InfinitePagerAdapter) adapter).getRealAdapter();
        }
        return null;
    }

    /**
     * get the current item position
     *
     * @return the int position
     */
    public int getCurrentPosition() {
        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");
        return mViewPager.getCurrentItem() % getRealAdapter().getCount();
    }

    /**
     * get current slider.
     *
     * @return the sliderview
     */
    public BaseSliderView getCurrentSlider() {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        int count = getRealAdapter().getCount();
        int realCount = mViewPager.getCurrentItem() % count;
        return getRealAdapter().getSliderView(realCount);
    }

    /**
     * remove  the slider at the position. Notice: It's a not perfect method, a very small bug still exists.
     *
     * @param position int position
     */
    public void removeSliderAt(int position) {
        if (getRealAdapter() != null) {
            getRealAdapter().removeSliderAt(position);
            //mViewPager.setCurrentItem(mViewPager.getCurrentItem(), false);
        }
    }

    /**
     * remove all the sliders. Notice: It's a not perfect method, a very small bug still exists.
     */
    public void removeAllSliders() {
        if (getRealAdapter() != null) {
            int count = getRealAdapter().getCount();
            getRealAdapter().removeAllSliders();
            //a small bug, but fixed by this trick.
            //bug: when remove adapter's all the sliders.some caching slider still alive.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + count, false);
        }
    }

    /**
     * clear self means unregister the dataset observer and cancel all Timer
     */
    public void destroySelf() {
        mSliderAdapter.unregisterDataSetObserver(sliderDataObserver);
        stopAutoCycle();
        mViewPager.removeAllViews();
    }

}
