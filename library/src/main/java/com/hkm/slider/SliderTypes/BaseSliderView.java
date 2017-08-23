package com.hkm.slider.SliderTypes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.hkm.slider.CapturePhotoUtils;
import com.hkm.slider.LoyalUtil;
import com.hkm.slider.R;
import com.hkm.slider.SliderLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods.
 * I provide two example: {@link com.hkm.slider.SliderTypes.DefaultSliderView} and
 * {@link com.hkm.slider.SliderTypes.TextSliderView}
 * if you want to show progressbar, you just need to set a progressbar id as @+id/loading_bar.
 */
public abstract class BaseSliderView {
    protected Object current_image_holder;
    protected Context mContext;
    protected boolean imageLoaded = false;
    private RequestCreator rq = null;
    private final Bundle mBundle;
    protected int mTargetWidth, mTargetHeight;
    /**
     * Error place holder image.
     */
    private int mErrorPlaceHolderRes;

    /**
     * Empty imageView placeholder.
     */
    private int mEmptyPlaceHolderRes;

    private String mUrl;
    private File mFile;
    private int mRes;
    private int mSlideNumber;
    protected OnSliderClickListener mOnSliderClickListener;

    protected boolean mErrorDisappear, mLongClickSaveImage;
    protected boolean mImageLocalStorageEnable;

    private ImageLoadListener mLoadListener;

    private String mDescription;

    private Uri touch_information;
    /**
     * Scale type of the image.
     */
    protected ScaleType mScaleType = ScaleType.Fit;

    /**
     * reference of the parent
     */
    protected WeakReference<SliderLayout> sliderContainer;
    protected Typeface mTypeface;

    public void setSliderContainerInternal(SliderLayout b) {
        this.sliderContainer = new WeakReference<SliderLayout>(b);
    }

    public enum ScaleType {
        CenterCrop, CenterInside, Fit, FitCenterCrop
    }

    protected BaseSliderView(Context context) {
        mContext = context;
        this.mBundle = new Bundle();
        mLongClickSaveImage = false;
        mImageLocalStorageEnable = false;
    }

    public final void setSlideOrderNumber(final int order) {
        mSlideNumber = order;
    }

    public final int getSliderOrderNumber() {
        return mSlideNumber;
    }

    /**
     * the placeholder image when loading image from url or file.
     *
     * @param resId Image resource id
     * @return BaseSliderView
     */
    public BaseSliderView empty(int resId) {
        mEmptyPlaceHolderRes = resId;
        return this;
    }

    public BaseSliderView descriptionTypeface(Typeface typeface) {
        mTypeface = typeface;
        return this;
    }

    protected void setupDescription(TextView descTextView) {
        descTextView.setText(mDescription);
        if (mTypeface != null) {
            descTextView.setTypeface(mTypeface);
        }
    }

    /**
     * determine whether remove the image which failed to download or load from file
     *
     * @param disappear boolean
     * @return BaseSliderView
     */
    public BaseSliderView errorDisappear(boolean disappear) {
        mErrorDisappear = disappear;
        return this;
    }

    /**
     * if you set errorDisappear false, this will set a error placeholder image.
     *
     * @param resId image resource id
     * @return BaseSliderView
     */
    public BaseSliderView error(int resId) {
        mErrorPlaceHolderRes = resId;
        return this;
    }

    /**
     * the description of a slider image.
     *
     * @param description String
     * @return BaseSliderView
     */
    public BaseSliderView description(String description) {
        mDescription = description;
        return this;
    }

    /**
     * the url of the link or something related when the touch happens.
     *
     * @param info Uri
     * @return BaseSliderView
     */
    public BaseSliderView setUri(Uri info) {
        touch_information = info;
        return this;
    }

    /**
     * set a url as a image that preparing to load
     *
     * @param url String
     * @return BaseSliderView
     */
    public BaseSliderView image(String url) {
        if (mFile != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mUrl = url;
        return this;
    }

    /**
     * set a file as a image that will to load
     *
     * @param file java.io.File
     * @return BaseSliderView
     */
    public BaseSliderView image(File file) {
        if (mUrl != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mFile = file;
        return this;
    }

    public BaseSliderView image(@DrawableRes int res) {
        if (mUrl != null || mFile != null) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mRes = res;
        return this;
    }

    /**
     * get the url of the image path
     *
     * @return the path in string
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * get the url from the touch event
     *
     * @return the touch event URI
     */
    public Uri getTouchURI() {
        return touch_information;
    }

    public boolean isErrorDisappear() {
        return mErrorDisappear;
    }

    public int getEmpty() {
        return mEmptyPlaceHolderRes;
    }

    public int getError() {
        return mErrorPlaceHolderRes;
    }

    public String getDescription() {
        return mDescription;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * set a slider image click listener
     *
     * @param l the listener
     * @return the base slider
     */
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
        mOnSliderClickListener = l;
        return this;
    }

    protected View.OnLongClickListener mDefaultLongClickListener = null;
    protected WeakReference<FragmentManager> fmg;

    /**
     * to enable the slider for saving images
     *
     * @param mfmg FragmentManager
     * @return this thing
     */
    public BaseSliderView enableSaveImageByLongClick(FragmentManager mfmg) {
        mLongClickSaveImage = true;
        mDefaultLongClickListener = null;
        this.fmg = new WeakReference<FragmentManager>(mfmg);
        return this;
    }

    /**
     * to set custom listener for long click event
     *
     * @param listen the listener
     * @return thos thomg
     */
    public BaseSliderView setSliderLongClickListener(View.OnLongClickListener listen) {
        mDefaultLongClickListener = listen;
        mLongClickSaveImage = false;
        return this;
    }

    public BaseSliderView setSliderLongClickListener(View.OnLongClickListener listen, FragmentManager mfmg) {
        mDefaultLongClickListener = listen;
        mLongClickSaveImage = false;
        this.fmg = new WeakReference<FragmentManager>(mfmg);
        return this;
    }

    public BaseSliderView resize(int targetWidth, int targetHeight) {
        if (targetWidth < 0) {
            throw new IllegalArgumentException("Width must be positive number or 0.");
        }
        if (targetHeight < 0) {
            throw new IllegalArgumentException("Height must be positive number or 0.");
        }
        if (targetHeight == 0 && targetWidth == 0) {
            throw new IllegalArgumentException("At least one dimension has to be positive number.");
        }
        mTargetWidth = targetWidth;
        mTargetHeight = targetHeight;
        return this;
    }

    public BaseSliderView enableImageLocalStorage() {
        mImageLocalStorageEnable = true;
        return this;
    }


    protected void hideLoadingProgress(View mView) {
        if (mView.findViewById(R.id.ns_loading_progress) != null) {
            hideoutView(mView.findViewById(R.id.ns_loading_progress));
        }
    }

    /**
     * when {@link #mLongClickSaveImage} is true and this function will be triggered to watch the long action run
     *
     * @param mView the slider view object
     */
    private void triggerOnLongClick(View mView) {
        if (mLongClickSaveImage && fmg != null) {
            if (mDefaultLongClickListener == null) {
                mDefaultLongClickListener = new View.OnLongClickListener() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public boolean onLongClick(View v) {
                        final saveImageDialog saveImageDial = new saveImageDialog();
                        saveImageDial.show(fmg.get(), mDescription);
                        return false;
                    }
                };
            }
            mView.setOnLongClickListener(mDefaultLongClickListener);
        }
    }

    private final View.OnClickListener click_triggered = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnSliderClickListener != null) {
                mOnSliderClickListener.onSliderClick(BaseSliderView.this);
            }
        }
    };

    /**
     * When you want to implement your own slider view, please call this method in the end in `getView()` method
     *
     * @param v               the whole view
     * @param targetImageView where to place image
     */
    protected void bindEventAndShowPicasso(final View v, final ImageView targetImageView) {
        current_image_holder = targetImageView;
        v.setOnClickListener(click_triggered);
        mLoadListener.onStart(this);
        final Picasso p = Picasso.with(mContext);
        rq = null;
        if (mUrl != null) {
            rq = p.load(mUrl);
        } else if (mFile != null) {
            rq = p.load(mFile);
        } else if (mRes != 0) {
            rq = p.load(mRes);
        } else {
            return;
        }
        if (rq == null) {
            return;
        }
        if (getEmpty() != 0) {
            rq.placeholder(getEmpty());
        }
        if (getError() != 0) {
            rq.error(getError());
        }
        if (mTargetWidth > 0 || mTargetHeight > 0) {
            rq.resize(mTargetWidth, mTargetHeight);
        }
        if (mImageLocalStorageEnable) {
            rq.memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE);
        }

        switch (mScaleType) {
            case Fit:
                rq.fit();
                break;
            case CenterCrop:
                rq.fit().centerCrop();
                break;
            case CenterInside:
                rq.fit().centerInside();
                break;
        }

        rq.into(targetImageView, new Callback() {
            @Override
            public void onSuccess() {
                imageLoaded = true;
                hideLoadingProgress(v);
                triggerOnLongClick(v);
                reportStatusEnd(true);
            }

            @Override
            public void onError() {
                reportStatusEnd(false);
            }
        });
    }

    protected void bindEventShowGlide(final View v, final ImageView targetImageView) {
        RequestOptions requestOptions = new RequestOptions();

        v.setOnClickListener(click_triggered);
        final RequestManager glideRM = Glide.with(mContext);
        RequestBuilder rq;
        if (mUrl != null) {
            rq = glideRM.load(mUrl);
        } else if (mFile != null) {
            rq = glideRM.load(mFile);
        } else if (mRes != 0) {
            rq = glideRM.load(mRes);
        } else {
            return;
        }

        if (getEmpty() != 0) {
            requestOptions.placeholder(getEmpty());
        }
        if (getError() != 0) {
            requestOptions.error(getError());
        }

        switch (mScaleType) {
            case Fit:
                requestOptions.fitCenter();
                break;
            case CenterCrop:
                requestOptions.centerCrop();
                break;
            case CenterInside:
                requestOptions.fitCenter();
                break;
        }

        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        if (mTargetWidth > 0 || mTargetHeight > 0) {
            requestOptions.override(mTargetWidth, mTargetHeight);
        }

        rq.apply(requestOptions);

        rq.listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target target, boolean isFirstResource) {
                reportStatusEnd(false);
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, com.bumptech.glide.request.target.Target target, DataSource dataSource, boolean isFirstResource) {
                hideLoadingProgress(v);
                triggerOnLongClick(v);
                reportStatusEnd(true);
                return false;
            }
        });
        rq.transition(DrawableTransitionOptions.withCrossFade());

        additionalGlideModifier(rq);
        rq.into(targetImageView);
    }

    protected void additionalGlideModifier(RequestBuilder requestBuilder) {

    }
    protected void applyImageWithGlide(View v, final ImageView targetImageView) {
        current_image_holder = targetImageView;
        LoyalUtil.glideImplementation(getUrl(), targetImageView, getContext());
        hideLoadingProgress(v);
        triggerOnLongClick(v);
        reportStatusEnd(true);
        imageLoaded = true;
    }

    protected void applyImageWithPicasso(View v, final ImageView targetImageView) {
        current_image_holder = targetImageView;
        LoyalUtil.picassoImplementation(getUrl(), targetImageView, getContext());
        hideLoadingProgress(v);
        triggerOnLongClick(v);
        imageLoaded = true;
        reportStatusEnd(true);
    }

    protected void applyImageWithSmartBoth(View v, final ImageView target) {
        current_image_holder = target;
        LoyalUtil.hybridImplementation(getUrl(), target, getContext());
        hideLoadingProgress(v);
        triggerOnLongClick(v);
        imageLoaded = true;
        reportStatusEnd(true);
    }


    protected void applyImageWithSmartBothAndNotifyHeight(View v, final ImageView target) {
        current_image_holder = target;
        LoyalUtil.hybridImplementation(getUrl(), target, getContext(), new Runnable() {
            @Override
            public void run() {
                imageLoaded = true;
                if (sliderContainer == null) return;
                if (sliderContainer.get().getCurrentPosition() == getSliderOrderNumber()) {
                    sliderContainer.get().setFitToCurrentImageHeight();
                }

            }
        });
        hideLoadingProgress(v);
        triggerOnLongClick(v);
        reportStatusEnd(true);
    }

    private void reportStatusEnd(boolean b) {
        if (mLoadListener != null) {
            mLoadListener.onEnd(b, this);
        }
    }

    final android.os.Handler nh = new android.os.Handler();

    private int notice_save_image_success = R.string.success_save_image;

    public final void setMessageSaveImageSuccess(@StringRes final int t) {
        notice_save_image_success = t;
    }

    protected void workAroundGetImagePicasso() {

        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

    protected void workGetImage(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        imageView.buildDrawingCache(true);
        output_bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);
    }

    private Bitmap output_bitmap = null;

    private class getImageTask extends AsyncTask<Void, Void, Integer> {
        private ImageView imageView;

        public getImageTask(ImageView taskTarget) {
            imageView = taskTarget;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int tried = 0;
            while (tried < 5) {
                try {
                    workGetImage(imageView);
                    return 1;
                } catch (Exception e) {
                    tried++;
                }
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1) {
                CapturePhotoUtils.insertImage(mContext, output_bitmap, mDescription, new CapturePhotoUtils.Callback() {
                            @Override
                            public void complete() {
                                nh.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (fmg == null) return;
                                        String note = mContext.getString(notice_save_image_success);
                                        final SMessage sm = SMessage.message(note);
                                        sm.show(fmg.get(), "done");
                                    }
                                });
                            }
                        }
                );
            } else {
                String m = mContext.getString(R.string.image_not_read);
                final SMessage sm = SMessage.message(m);
                sm.show(fmg.get(), "try again");
            }
        }
    }

    /**
     * should use OnImageSavedListener instead or other listener for dialogs
     */
    @Deprecated
    @SuppressLint("ValidFragment")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SMessage extends DialogFragment {
        public static SMessage message(final String mes) {
            Bundle h = new Bundle();
            h.putString("message", mes);
            SMessage e = new SMessage();
            e.setArguments(h);
            return e;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getArguments().getString("message"))
                    .setNeutralButton(R.string.okay_now, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
            ;
            return builder.create();
        }
    }


    @SuppressLint("ValidFragment")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class saveImageDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (mContext == null) return null;
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.save_image)
                    .setPositiveButton(R.string.yes_save, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveImageActionTrigger();
                        }
                    })
                    .setNegativeButton(R.string.no_keep, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    protected void saveImageActionTrigger() {
        if (current_image_holder == null) return;
        if (current_image_holder instanceof ImageView) {
            ImageView fast = (ImageView) current_image_holder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mContext.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //   mContext.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AnyNumber);
                } else {
                    getImageTask t = new getImageTask(fast);
                    t.execute();
                }
            } else {
                getImageTask t = new getImageTask(fast);
                t.execute();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    final protected void hideoutView(@Nullable final View view) {
        if (view == null) return;
        view.animate().alpha(0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        });
    }

    public BaseSliderView setScaleType(ScaleType type) {
        mScaleType = type;
        return this;
    }

    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return View
     */
    public abstract View getView();

    /**
     * set a listener to get a message , if load error.
     *
     * @param l ImageLoadListener
     */
    public void setOnImageLoadListener(ImageLoadListener l) {
        mLoadListener = l;
    }

    public interface OnSliderClickListener {
        void onSliderClick(BaseSliderView coreSlider);
    }

    /**
     * when you have some extra information, please put it in this bundle.
     *
     * @return Bundle
     */
    public Bundle getBundle() {
        return mBundle;
    }

    public interface ImageLoadListener {
        void onStart(BaseSliderView target);

        void onEnd(boolean result, BaseSliderView target);
    }

    public Object getImageView() {
        return current_image_holder;
    }

    public interface OnImageSavedListener {
        void onImageSaved(String description);

        void onImageSaveFailed();
    }

    protected OnImageSavedListener onImageSavedListener = null;

    public OnImageSavedListener getOnImageSavedListener() {
        return onImageSavedListener;
    }

    public void setOnImageSavedListener(OnImageSavedListener onImageSavedListener) {
        this.onImageSavedListener = onImageSavedListener;
    }
}
