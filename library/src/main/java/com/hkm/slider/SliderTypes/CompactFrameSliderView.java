package com.hkm.slider.SliderTypes;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hkm.slider.Layouts.MaterialRippleLayout;
import com.hkm.slider.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zSH
 * This one will be more widely used in the real world applications
 */
public class CompactFrameSliderView extends CompactSliderView {

    /**
     * declare the mini frame resource id first
     *
     * @return the mini frame resource id
     */
    @LayoutRes
    protected int getCompactFrameLayout() {
        return DEFAULT_SLIDE;
    }

    /**
     * the best way to product the miniframe
     *
     * @param n_order the input order
     * @return the miniframe instance
     */
    protected MiniSliderFrame produceMiniFrame(final int n_order) {
        final int layout_id = setCustomLayoutSlide == 0 ? getCompactFrameLayout() : setCustomLayoutSlide;
        final View layoutview = LayoutInflater.from(mContext).inflate(layout_id, null);
        final FrameImage frame = new FrameImage(n_order, layoutview);
        return frame;
    }

    /**
     * this is the demo default use only
     */
    private class FrameImage implements MiniSliderFrame {
        private ImageView mImage;
        private ProgressBar mProgress;
        public TextView mTextView;
        private LinearLayout ly;
        private MaterialRippleLayout touch;
        private View full_view;
        private int loc_order;

        public FrameImage(final int order_n, View frame) {
            mImage = (ImageView) frame.findViewById(R.id.ns_slider_image);
            mProgress = (ProgressBar) frame.findViewById(R.id.ns_loading_progress);
            mTextView = (TextView) frame.findViewById(R.id.ns_slider_desc);
            ly = (LinearLayout) frame.findViewById(R.id.ns_desc_frame);
            if (frame.findViewById(R.id.ns_touch_frame) != null) {
                touch = (MaterialRippleLayout) frame.findViewById(R.id.ns_touch_frame);
            }
            this.loc_order = order_n;
            full_view = frame;
        }

        @Override
        public View getView() {
            return full_view;
        }

        @Override
        public View getTouch() {
            if (touch == null) return mImage;
            return touch;
        }

        @Override
        public void setClickListener(final View.OnClickListener listner) {
            if (touch == null) {
                mImage.setOnClickListener(listner);
            } else {
                touch.setOnClickListener(listner);
            }
        }

        @Override
        public ImageView getImageTarget() {
            return mImage;
        }

        @Override
        public ProgressBar getLoadingBar() {
            return mProgress;
        }

        @Override
        public void applyDescription(String word) {
            if (mTextView == null) return;
            mTextView.setText(word);
        }

        @Override
        public void applyDescription(String[] list) {
            if (mTextView == null) return;
            mTextView.setText(list[0]);
        }
    }

    /**
     * specialized slider click listener for extensive use
     */
    public interface OnMiniSliderClickListener {
        void onMiniSlideClick(MiniSliderFrame minislide, int order, String extra);
    }

    public static final int DEFAULT_SLIDE = R.layout.hb_feature_slide;
    public static final int RIPPLE_SLIDE = R.layout.hb_feature_slide_ripple;
    private int setCustomLayoutSlide = 0;
    private List<String> descriptions = new ArrayList<>();
    private List<String[]> description_fragments = new ArrayList<>();

    /**
     * auto configuration of using the special layout from 1-4 mini frames
     *
     * @return the res layout
     */
    @LayoutRes
    protected int getLayoutConfig() {
        switch (number_of_pieces) {
            case 2:
                return R.layout.compact_frame_2;
            case 3:
                return R.layout.compact_frame_3;
            case 4:
                return R.layout.compact_frame_4;
            default:
                return R.layout.compact_frame_2;
        }
    }

    public CompactFrameSliderView setSlideLayoutCustom(@LayoutRes int layoutId) {
        setCustomLayoutSlide = layoutId;
        return this;
    }

    public CompactFrameSliderView setDescriptions(final String[] descL) throws Exception {
        if (descL.length < number_of_pieces) {
            throw new Exception("not enough descriptions. There in total we will need to have " + number_of_pieces + " of them");
        }
        this.descriptions = Arrays.asList(descL);
        return this;
    }

    public CompactFrameSliderView setDescriptions(final ArrayList<String> descriptions) throws Exception {
        if (descriptions.size() < number_of_pieces) {
            throw new Exception("not enough descriptions. There in total we will need to have " + number_of_pieces + " of them");
        }

        this.descriptions = descriptions;
        return this;
    }

    public CompactFrameSliderView setDescriptionFragments(final ArrayList<String[]> description_fragments) throws Exception {
        if (description_fragments.size() < number_of_pieces) {
            throw new Exception("not enough descriptions. There in total we will need to have " + number_of_pieces + " of them");
        }
        this.description_fragments = description_fragments;
        return this;
    }

    /**
     * This the description to overlay all the miniframe on the screen
     *
     * @param description String
     * @return BaseSliderView
     */
    @Override
    public CompactFrameSliderView description(String description) {
        super.description(description);
        return this;
    }

    public CompactFrameSliderView(Context context, int pieces) throws Exception {
        super(context, pieces);
    }

    protected FrameLayout f1, f2, f3, f4;
    protected OnMiniSliderClickListener mSliderCKlistener;

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return View
     */
    @Override
    public int bindviews(View layout) {
        if (number_of_pieces >= 1) {
            f1 = (FrameLayout) layout.findViewById(R.id.ns_display_i_s1);
        }
        if (number_of_pieces >= 2) {
            f2 = (FrameLayout) layout.findViewById(R.id.ns_display_i_s2);
        }
        if (number_of_pieces >= 3) {
            f3 = (FrameLayout) layout.findViewById(R.id.ns_display_i_s3);
        }
        if (number_of_pieces >= 4) {
            f4 = (FrameLayout) layout.findViewById(R.id.ns_display_i_s4);
        }
        onBindOverLay((FrameLayout) layout.findViewById(R.id.ns_framecompact_overlay));
        return number_of_pieces;
    }

    /**
     * only triggered once when the initalization is ready
     *
     * @param f the number of mini frame that has been determined.
     */
    @Override
    protected void filter_apply_event_to_view(int f) {
        if (f == 0) {
            apply_event_to_frame(f1, urls.get(f), f);
        } else if (f == 1) {
            apply_event_to_frame(f2, urls.get(f), f);
        } else if (f == 2) {
            apply_event_to_frame(f3, urls.get(f), f);
        } else if (f == 3) {
            apply_event_to_frame(f4, urls.get(f), f);
        }
    }

    /**
     * choose or direct implementation of graphic loading system here we can use whatever we like
     *
     * @param mUrl       the image url
     * @param mMiniframe the instance of the miniframe
     */
    protected void onBindImageLoadingSystem(final String mUrl, final MiniSliderFrame mMiniframe) {
        bindCompatPicasso(mUrl, mMiniframe);
    }

    /**
     * this call is built for external use. please feel free to make override of this call to make your work started in here
     *
     * @param the_overlay_layout the layout found
     */
    protected void onBindOverLay(@Nullable FrameLayout the_overlay_layout) {
/*
        final int layout_id = setCustomLayoutSlide == 0 ? getCompactFrameLayout() : setCustomLayoutSlide;
        final View layoutview = LayoutInflater.from(mContext).inflate(layout_id, null);
        final FrameImage frame = new FrameImage(n_order, layoutview);

        the_overlay_layout.addView(mF.getView());
  */
    }

    /**
     * will trigger multiple times that dictated by the number of the total mini frames
     *
     * @param mframeLayout frame layout
     * @param image_url    the image url
     * @param n            the mini frame index
     */
    private void apply_event_to_frame(
            @Nullable FrameLayout mframeLayout,
            @NonNull final String image_url,
            final int n
    ) {
        if (mframeLayout == null) return;
        final MiniSliderFrame mF = produceMiniFrame(n);
        mframeLayout.addView(mF.getView());

        if (descriptions.size() > 0) {
            String desc = descriptions.get(n);
            mF.applyDescription(desc);
        } else if (description_fragments.size() > 0) {
            String[] descf = description_fragments.get(n);
            mF.applyDescription(descf);
        }

        onBindImageLoadingSystem(image_url, mF);

        mF.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSliderCKlistener != null && uris.size() > 0) {
                    link_on_click_current = uris.get(n);
                    mSliderCKlistener.onMiniSlideClick(mF, n, link_on_click_current);
                }
            }
        });
    }

    public CompactFrameSliderView setMiniSilderClickListener(final OnMiniSliderClickListener listener) {
        mSliderCKlistener = listener;
        return this;
    }

    protected void bindCompatPicasso(String mURI, final MiniSliderFrame Fr) {

        final Picasso p = Picasso.with(mContext);
        final RequestCreator mreq = p.load(mURI);
        if (getEmpty() != 0) {
            mreq.placeholder(getEmpty());
        }
        if (getError() != 0) {
            mreq.error(getError());
        }
        if (mImageLocalStorageEnable) {
            mreq.memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE);
        }
        switch (mScaleType) {
            case Fit:
                mreq.fit();
                break;
            case CenterCrop:
                mreq.fit().centerCrop();
                break;
            case CenterInside:
                mreq.fit().centerInside();
                break;
        }


        mreq.into(Fr.getImageTarget(), new Callback() {
            @Override
            public void onSuccess() {
                hideoutView(Fr.getLoadingBar());
                if (mLongClickSaveImage && fmg != null) {
                    Fr.getTouch().setOnLongClickListener(new View.OnLongClickListener() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                        @Override
                        public boolean onLongClick(View v) {
                            prepare_request_save_image = mreq;
                            final saveImageDialog saveImageDial = new saveImageDialog();
                            saveImageDial.show(fmg.get(), "DESC_SAVE_IM");
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onError() {
                //if (mLoadListener != null) {
                //     mLoadListener.onEnd(false, me);
                // }
            }
        });
    }

    @Override
    public CompactFrameSliderView build() {
        return this;
    }

    @Deprecated
    @Override
    public BaseSliderView enableSaveImageByLongClick(FragmentManager mfmg) {
        return this;
    }

    @Override
    public BaseSliderView enableImageLocalStorage() {
        return super.enableImageLocalStorage();
    }

    @Deprecated
    @Override
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
        return super.setOnSliderClickListener(l);
    }

    @Deprecated
    @Override
    public BaseSliderView setSliderLongClickListener(View.OnLongClickListener listen) {
        return super.setSliderLongClickListener(listen);
    }

    @Deprecated
    @Override
    public BaseSliderView setSliderLongClickListener(View.OnLongClickListener listen, FragmentManager mfmg) {
        return super.setSliderLongClickListener(listen, mfmg);
    }
}
