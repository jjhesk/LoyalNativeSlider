package com.hkm.slider;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hkm.slider.Animations.BaseAnimationInterface;
import com.hkm.slider.Indicators.NumContainer;
import com.hkm.slider.Indicators.PagerIndicator;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.Transformers.BaseTransformer;
import com.hkm.slider.Tricks.AnimationHelper;
import com.hkm.slider.Tricks.ArrowControl;
import com.hkm.slider.Tricks.FixedSpeedScroller;
import com.hkm.slider.Tricks.InfinitePagerAdapter;
import com.hkm.slider.Tricks.InfiniteViewPager;
import com.hkm.slider.Tricks.MultiViewPager;
import com.hkm.slider.Tricks.ViewPagerEx;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.hkm.slider.SliderLayout.PresentationConfig.Dots;
import static com.hkm.slider.SliderLayout.PresentationConfig.Numbers;
import static com.hkm.slider.SliderLayout.PresentationConfig.Smart;
import static com.hkm.slider.SliderLayout.PresentationConfig.byVal;

/**
 * SliderLayout is compound layout. This is combined with {@link com.hkm.slider.Indicators.PagerIndicator}
 * and {@link com.hkm.slider.Tricks.ViewPagerEx} .
 * -----
 * There is some properties you can set in XML:
 * -----
 * indicator_visibility
 * visible
 * invisible
 * -----
 * indicator_shape
 * oval
 * rect
 * indicator_selected_color
 * indicator_unselected_color
 * indicator_selected_drawable
 * indicator_unselected_drawable
 * pager_animation
 */
public class SliderLayout extends RelativeLayout {
    public static final int
            ZOOMABLE = 1, NONZOOMABLE = 0;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ZOOMABLE, NONZOOMABLE})
    public @interface SliderLayoutType {
    }

    public Activity activity;
    public Context mContext;
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

    @SliderLayoutType
    private int pagerType = NONZOOMABLE;
    /**
     * If {@link com.hkm.slider.Tricks.ViewPagerEx} is Cycling
     */
    private boolean mCycling;
    private boolean sidebuttons;
    private boolean mDisabledSlider = false;
    private boolean mAutoAdjustSliderHeight = false;
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
    private long mTransitionAnimation = 1000;
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
    private int buttondr, buttondl;
    private int frame_width, frame_height;
    /**
     * this is the limit for switching from dot types into the page number type
     */
    private int slideDotLimit;

    /**
     * hold number specific
     */
    private RelativeLayout holderNum;

    /**
     * callback from the measurement collection
     */
    private OnViewConfigurationDetected mViewSizeMonitor;

    /**
     * callback will be triggered only when the {@link []}
     */
    private OnImageLoadWithAdjustableHeight mOnImageLoadWithAdjustableHeight;

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
        mSliderIndicatorPresentations = attributes.getInt(R.styleable.SliderLayout_lns_use_presentation, Smart.ordinal());
        buttondl = attributes.getResourceId(R.styleable.SliderLayout_image_button_l, R.drawable.arrow_l);
        buttondr = attributes.getResourceId(R.styleable.SliderLayout_image_button_r, R.drawable.arrow_r);
        button_side_function_flip = attributes.getBoolean(R.styleable.SliderLayout_slider_side_buttons_function_flip, false);
        mAutoCycle = attributes.getBoolean(R.styleable.SliderLayout_auto_cycle, true);
        sidebuttons = attributes.getBoolean(R.styleable.SliderLayout_slider_side_buttons, false);
        slideDotLimit = attributes.getInt(R.styleable.SliderLayout_slide_dot_limit, 5);
        int visibility = attributes.getInt(R.styleable.SliderLayout_indicator_visibility, 0);
        checkVisibility(visibility);
        pagerSetup();
        attributes.recycle();
        setPresetIndicator(PresetIndicators.Center_Bottom);
        setPresetTransformer(mTransformerId);
        setSliderTransformDuration(mTransformerSpan, null);
        setIndicatorVisibility(mIndicatorVisibility);
        if (mAutoCycle) {
            startAutoCycle();
        }
        start_detect_frame_size();
        navigation_button_initialization();
    }

    private void checkVisibility(int xml_config) {
        for (PagerIndicator.IndicatorVisibility v : PagerIndicator.IndicatorVisibility.values()) {
            if (v.ordinal() == xml_config) {
                mIndicatorVisibility = v;
                break;
            }
        }
    }

    private void start_detect_frame_size() {
        ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                frame_width = getMeasuredWidth();
                frame_height = getMeasuredHeight();
                return false;
            }
        });
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
    private Handler postHandler = new Handler();
    private ImageView mButtonLeft, mButtonRight;
    private int mLWidthB, mRWidthB;
    private boolean mLopen, mRopen, button_side_function_flip = false;
    private ArrowControl arrow_instance;

    private void navigation_button_initialization() {
        mButtonLeft = (ImageView) findViewById(R.id.arrow_l);
        mButtonRight = (ImageView) findViewById(R.id.arrow_r);
        mButtonLeft.setImageResource(buttondl);
        mButtonRight.setImageResource(buttondr);
        arrow_instance = new ArrowControl(mButtonLeft, mButtonRight);
        mLWidthB = mButtonLeft.getDrawable().getIntrinsicWidth();
        mRWidthB = mButtonRight.getDrawable().getIntrinsicWidth();
        if (!sidebuttons) {
            arrow_instance.noSlideButtons();
        } else {
            arrow_instance.setListeners(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!button_side_function_flip)
                                moveNextPosition(true);
                            else movePrevPosition(true);
                        }
                    },
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!button_side_function_flip)
                                movePrevPosition(true);
                            else moveNextPosition(true);
                        }
                    }
            );
        }
        mLopen = mRopen = true;
    }

    private void notify_navigation_buttons() {
        arrow_instance.setTotal(mSliderAdapter.getCount());
        int count = mSliderAdapter.getCount();

        //DisplayMetrics m = getResources().getDisplayMetrics();
        // final int width = m.widthPixels;
        final int end_close_left = -mLWidthB;
        final int end_close_right = mRWidthB;
        final int open_left = 0;
        final int open_right = 0;

        if (count <= 1) {
            postHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mButtonLeft != null && mLopen) {
                        mButtonLeft.animate().translationX(end_close_left);
                        mLopen = false;
                    }
                    if (mButtonRight != null && mRopen) {
                        mButtonRight.animate().translationX(end_close_right);
                        mRopen = false;
                    }
                }
            }, mTransitionAnimation);
        } else {
            postHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mButtonLeft != null && !mLopen) {
                        mButtonLeft.animate().translationX(open_left);
                        mLopen = true;
                    }
                    if (mButtonRight != null && !mRopen) {
                        mButtonRight.animate().translationX(open_right);
                        mRopen = true;
                    }
                }
            }, mTransitionAnimation);
        }
    }

    public SliderLayout setType(final @SliderLayoutType int t) {
        pagerType = t;
        return this;
    }

    public SliderLayout setActivity(final Activity t) {
        activity = t;
        return this;
    }

    private void pagerSetup() {
        mSliderAdapter = new SliderAdapter(mContext);
        if (pagerType == NONZOOMABLE) {
            PagerAdapter wrappedAdapter = new InfinitePagerAdapter(mSliderAdapter);
            mViewPager = (InfiniteViewPager) findViewById(R.id.daimajia_slider_viewpager);
            if (mPagerMargin > -1) {
                mViewPager.setMargin(mPagerMargin);
            }
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
            mViewPager.setAdapter(wrappedAdapter);
        } else if (pagerType == ZOOMABLE) {

        }
        mSliderAdapter.registerDataSetObserver(sliderDataObserver);
    }

    public <TN extends NumContainer> void setNumLayout(final TN container) {
        holderNum = (RelativeLayout) findViewById(R.id.number_count_layout);
        //   final RelativeLayout mSmallBox = (RelativeLayout) findViewById(R.id.subcontainer);
        container
                .withView(holderNum)
                .setViewPager(mViewPager)
                .build();
    }

    /**
     * set the current slider
     *
     * @param position the insert position
     * @param smooth   if that is smooth or not
     */
    private void setCurrentPosition(int position, boolean smooth) {
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
     * this function will be removed soon in the next release. see {@link #setCurrentPositionAnim} with detail
     *
     * @param position number
     */
    @Deprecated
    public final void setCurrentPosition(int position) {
        setCurrentPositionAnim(position);
    }

    /**
     * set the current position with animation
     *
     * @param position the int of the slide position
     */
    public final void setCurrentPositionAnim(int position) {
        setCurrentPosition(position, true);
    }

    /**
     * set current position without animation
     *
     * @param position the int of the slide position
     */
    public final void setCurrentPositionStatic(int position) {
        setCurrentPosition(position, false);
    }

    /**
     * {@link ViewPagerEx#setOffscreenPageLimit(int)} open ViewPager API.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     */
    public final void setOffscreenPageLimit(int limit) {
        mViewPager.setOffscreenPageLimit(limit);
    }

    /**
     * set the custom indicator
     *
     * @param indicator the indicator type
     */
    public final void setCustomIndicator(PagerIndicator indicator) {
        if (mIndicator != null) {
            mIndicator.destroySelf();
        }
        mIndicator = indicator;
        mIndicator.setIndicatorVisibility(mIndicatorVisibility);
        mIndicator.setViewPager(mViewPager);
        mIndicator.redraw();
    }

    public final <T extends BaseSliderView> void addSlider(T slide) {
        mSliderAdapter.addSlider(slide);
        autoDetermineLayoutDecoration();
        notify_navigation_buttons();
        AnimationHelper.notify_component(mIndicator, mSliderAdapter, postHandler);
    }

    public final <T extends BaseSliderView> void addSliderList(List<T> slide_sequence) {
        if (mViewSizeMonitor != null) {
            mSliderAdapter.setOnInitiateViewListener(mViewSizeMonitor);
        }
        mSliderAdapter.addSliders(slide_sequence);
        afterLoadSliders();
    }


    /**
     * this is for internal use when each item is instaniated from the adapter
     */
    public interface OnViewConfigurationDetected {
        void onLayoutGenerated(final SparseArray<Integer> data);
    }

    public interface OnViewConfigurationFinalized {
        void onDeterminedMaxHeight(final int height);
    }

    public interface OnImageLoadWithAdjustableHeight {
        void onNotified(final int new_height, boolean isFullScreenHeight);
    }

    private int total_length = 0;

    public final <T extends BaseSliderView> void loadSliderList(List<T> slide_sequence) {
        mSliderAdapter.removeAllSliders();
        if (mViewSizeMonitor != null) {
            total_length = slide_sequence.size();
            mSliderAdapter.setOnInitiateViewListener(mViewSizeMonitor);
        }
        if (isAutoAdjustSlideHeightInternal()) {
            mSliderAdapter.setSliderContainerInternal(this);
        }
        mSliderAdapter.loadSliders(slide_sequence);
        afterLoadSliders();
    }


    public final void afterLoadSliders() {
        autoDetermineLayoutDecoration();
        notify_navigation_buttons();
        if (!mDisabledSlider) {
            AnimationHelper.notify_component(mIndicator, mSliderAdapter, postHandler);
        } else {
            mIndicator.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }
    }

    public void setDisablePageIndicator() {
        mDisabledSlider = true;
    }

    /**
     * boardcast the height changes from the slider layout
     *
     * @param setFinal OnViewConfigurationFinalized
     */
    public final void setEnableMaxHeightFromAllSliders(final OnViewConfigurationFinalized setFinal) {

        mViewSizeMonitor = new OnViewConfigurationDetected() {
            @Override
            public void onLayoutGenerated(SparseArray<Integer> data) {
                if (total_length == data.size()) {
                    List<Integer> arrayList = new ArrayList<>(data.size());
                    for (int i = 0; i < data.size(); i++) {
                        arrayList.add(data.valueAt(i));
                    }
                    mSliderAdapter.endLayoutObserver();
                    setFinal.onDeterminedMaxHeight(Collections.max(arrayList));
                }
            }
        };
    }

    /**
     * after slider has been defined
     *
     * @param enabled bool
     */
    public final void setRemoveItemOnFailureToLoad(boolean enabled) {
        mSliderAdapter.setRemoveItemOnFailureToLoad(enabled);
    }

    private void autoDetermineLayoutDecoration() {
        final boolean over_limit = mSliderAdapter.getCount() > slideDotLimit;
        switch (byVal(mSliderIndicatorPresentations)) {
            case Smart:
                presentation(over_limit ? Numbers : Dots);
                break;
            case Dots:
                break;
            case Numbers:
                break;
        }
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

    public void presentation(PresentationConfig pc) {
        // usingPresentation = pc.ordinal();
        if (pc == Dots) {
            if (mIndicator != null)
                mIndicator.setVisibility(View.VISIBLE);
            if (holderNum != null)
                holderNum.setVisibility(View.GONE);
        } else if (pc == Numbers) {
            if (mIndicator != null)
                mIndicator.setVisibility(View.GONE);
            if (holderNum != null)
                holderNum.setVisibility(View.VISIBLE);
        }
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
     * runtime call
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
     * when paused cycle, this method can wake it up.
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
            mResumingTimer.schedule(mResumingTask, mSliderDuration);
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

    public void setOnSliderMeasurementFinal() {

    }

    public void addOnPageChangeListener(ViewPagerEx.OnPageChangeListener onPageChangeListener) {
        if (onPageChangeListener != null) {
            mViewPager.addOnPageChangeListener(onPageChangeListener);
        }
    }

    public void removeOnPageChangeListener(ViewPagerEx.OnPageChangeListener onPageChangeListener) {
        mViewPager.removeOnPageChangeListener(onPageChangeListener);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseAutoCycle();
                break;

            case MotionEvent.ACTION_MOVE:
                if (getRealAdapter().getCount() <= 1) {
                    return true;
                }
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
            e.printStackTrace();
        }
    }

    public void setSliderTransformDuration(Interpolator interpolator) {
        setSliderTransformDuration(mTransformerSpan, interpolator);
    }

    /**
     * presentation of indicators
     */
    public enum PresentationConfig {
        Smart("Smart")/* {
            @Override
            public Object getInstance() {
                return new Object();
            }
        }*/,
        Dots("Dots"),
        Numbers("Numbers");
        private final String name;

        PresentationConfig(String s) {
            name = s;
        }

        public String toString() {
            return name;
        }

        public boolean equals(String other) {
            return (other == null) ? false : name.equals(other);
        }

        public static PresentationConfig byVal(final int presentationInt) {
            for (TransformerL t : TransformerL.values()) {
                if (t.ordinal() == presentationInt) {
                    return values()[t.ordinal()];
                }
            }
            return Smart;
        }

        // public abstract <T> getInstance();

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
        setPresetTransformer(TransformerL.fromVal(transformerId));
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
        if (mIndicator != null) {
            mIndicator.setIndicatorVisibility(visibility);
        }
    }

    /**
     * get the indicator if it is visible
     *
     * @return PagerIndicator
     */
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
        int itemsCount = getRealAdapter().getCount();
        if (itemsCount < 1) {
            return 0;
        }
        return mViewPager.getCurrentItem() % itemsCount;
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

    /**
     * Preset Indicators
     */
    public enum PresetIndicators {
        Center_Bottom("Center_Bottom", R.id.default_center_bottom_indicator),
        Right_Bottom("Right_Bottom", R.id.default_bottom_right_indicator),
        Left_Bottom("Left_Bottom", R.id.default_bottom_left_indicator),
        Center_Top("Center_Top", R.id.default_center_top_indicator),
        Right_Top("Right_Top", R.id.default_center_top_right_indicator);

        private final String name;
        private final int id;

        PresetIndicators(String name, int id) {
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


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCycleTimer != null) mCycleTimer.cancel();
        if (mCycleTask != null) mCycleTask.cancel();
        if (mResumingTask != null) mResumingTask.cancel();
        if (mResumingTimer != null) mResumingTimer.cancel();
        mh.removeCallbacksAndMessages(null);
    }

    public boolean isAutoAdjustSlideHeightInternal() {
        return mAutoAdjustSliderHeight;
    }

    public void setAutoAdjustImageByHeight() {
        mAutoAdjustSliderHeight = true;
        addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setFitToCurrentImageHeight();
                Log.d("tagup", "position start " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("tagup", "position selected " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("tagup", "position state " + state);
                if (mOnImageLoadWithAdjustableHeight != null && temp_adjustment_height > 0 && state == 0) {
                    mOnImageLoadWithAdjustableHeight.onNotified(temp_adjustment_height, temp_adjustment_height > temp_adjustmentcontent_max_height);
                    temp_adjustment_height = -1;
                }
            }
        });
        setAutoAdjustImageFullContentMaxHeight(CONTEXT_DEFAULT_MAX_HEIGHT_AS_SCREEN_HEIGHT);
    }

    private int temp_adjustment_height = -1;
    private int temp_adjustmentcontent_max_height = -1;
    public static final int CONTEXT_DEFAULT_MAX_HEIGHT_AS_SCREEN_HEIGHT = -2;

    /**
     * set this param to enable the height of the
     *
     * @param maxHeight the maximum height
     */
    public void setAutoAdjustImageFullContentMaxHeight(final int maxHeight) {
        if (maxHeight == CONTEXT_DEFAULT_MAX_HEIGHT_AS_SCREEN_HEIGHT) {
            Resources resources = getContext().getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            temp_adjustmentcontent_max_height = metrics.heightPixels;
        } else {
            temp_adjustmentcontent_max_height = maxHeight;
        }
    }

    /**
     * additional call that is optional for user to make their own implementation
     *
     * @param wing listener
     */
    public final void setOnImageLoadWithAdjustableHeightListener(final OnImageLoadWithAdjustableHeight wing) {
        setAutoAdjustImageByHeight();
        mOnImageLoadWithAdjustableHeight = wing;
    }


    public void setFitToCurrentImageHeight() {
        if (getCurrentSlider().getImageView() instanceof ImageView) {
            ImageView p = (ImageView) getCurrentSlider().getImageView();
            if (p.getDrawable() != null) {
                int current_width = getMeasuredWidth();
                //(int) LoyalUtil.convertDpToPixel(image.getIntrinsicHeight(), getContext())
                Drawable image = p.getDrawable();
                float ratio = (float) image.getIntrinsicHeight() / (float) image.getIntrinsicWidth();

                final int fit_height = (int) (current_width * ratio);
                // Rect rec = new Rect(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                // requestRectangleOnScreen(rec);
                // onLayout(true, 0, 0, p.getDrawable().getIntrinsicWidth(), p.getDrawable().getIntrinsicHeight());
                if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                    //  RelativeLayout.LayoutParams m = (RelativeLayout.LayoutParams) getLayoutParams();
                    // int[] rules = m.getRules();
                    RelativeLayout.LayoutParams h = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, fit_height);
                    /*   if (rules.length > 0) {
                           for (int i = 0; i < rules.length; i++) {
                            h.addRule(rules[i]);
                           }
                         }
                    */
                    setLayoutParams(h);
                } else if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    //   LinearLayout.LayoutParams m = (LinearLayout.LayoutParams) getLayoutParams();
                    //   int[] rules = m.getRules();
                    LinearLayout.LayoutParams h = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, fit_height);
                    setLayoutParams(h);
                }
                temp_adjustment_height = fit_height;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
