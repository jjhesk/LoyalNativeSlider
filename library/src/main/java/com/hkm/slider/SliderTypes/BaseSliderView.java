package com.hkm.slider.SliderTypes;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.hkm.slider.CapturePhotoUtils;
import com.hkm.slider.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods.
 * I provide two example: {@link com.hkm.slider.SliderTypes.DefaultSliderView} and
 * {@link com.hkm.slider.SliderTypes.TextSliderView}
 * if you want to show progressbar, you just need to set a progressbar id as @+id/loading_bar.
 */
public abstract class BaseSliderView {

    protected Context mContext;
    protected RequestCreator rq = null;
    private final Bundle mBundle;

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

    protected OnSliderClickListener mOnSliderClickListener;

    private boolean mErrorDisappear, mLongClickSaveImage;

    private ImageLoadListener mLoadListener;

    private String mDescription;

    private Uri touch_information;
    /**
     * Scale type of the image.
     */
    private ScaleType mScaleType = ScaleType.Fit;

    public enum ScaleType {
        CenterCrop, CenterInside, Fit, FitCenterCrop
    }

    protected BaseSliderView(Context context) {
        mContext = context;
        this.mBundle = new Bundle();
        mLongClickSaveImage = false;
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

    public BaseSliderView image(int res) {
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
    protected FragmentManager fmg;

    /**
     * to enable the slider for saving images
     *
     * @param defaultPath
     * @return this thing
     */
    public BaseSliderView enableSaveImageByLongClick(FragmentManager fmg) {
        mLongClickSaveImage = true;
        mDefaultLongClickListener = null;
        this.fmg = fmg;
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

    public BaseSliderView setSliderLongClickListener(View.OnLongClickListener listen, FragmentManager fmg) {
        mDefaultLongClickListener = listen;
        mLongClickSaveImage = false;
        this.fmg = fmg;
        return this;
    }

    /**
     * When you want to implement your own slider view, please call this method in the end in `getView()` method
     *
     * @param v               the whole view
     * @param targetImageView where to place image
     */
    protected void bindEventAndShow(final View v, final ImageView targetImageView) {
        final BaseSliderView me = this;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null) {
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        mLoadListener.onStart(me);
        Picasso p = Picasso.with(mContext);
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
                if (v.findViewById(R.id.ns_loading_progress) != null) {
                    hideoutView(v.findViewById(R.id.ns_loading_progress));
                }

                if (mLongClickSaveImage && fmg != null) {
                    mDefaultLongClickListener = new View.OnLongClickListener() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                        @Override
                        public boolean onLongClick(View v) {
                            final saveImageDialog saveImageDial = new saveImageDialog();
                            saveImageDial.show(fmg, mDescription);
                            return false;
                        }
                    };
                    v.setOnLongClickListener(mDefaultLongClickListener);
                }
            }

            @Override
            public void onError() {
                if (mLoadListener != null) {
                    mLoadListener.onEnd(false, me);
                }
            }
        });

    }

    protected void saveImage() {

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        try {
            CapturePhotoUtils.insertImage(
                    mContext.getContentResolver(),
                    rq.get(),
                    df.toString(),
                    mDescription);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorMessage er = ErrorMessage.message(e.getMessage());
            er.show(fmg, "erroropen");
        }
    }

    @SuppressLint("ValidFragment")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ErrorMessage extends DialogFragment {
        public static ErrorMessage message(final String mes) {
            Bundle h = new Bundle();
            h.putString("message", mes);
            ErrorMessage e = new ErrorMessage();
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
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.save_image)
                    .setPositiveButton(R.string.yes_save, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveImage();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void hideoutView(final View view) {
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
        public void onSliderClick(BaseSliderView coreSlider);
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
        public void onStart(BaseSliderView target);

        public void onEnd(boolean result, BaseSliderView target);
    }

}
