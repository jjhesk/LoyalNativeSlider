package com.hkm.slider;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hkm.slider.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A slider adapter
 */
public class SliderAdapter<T extends BaseSliderView> extends PagerAdapter implements BaseSliderView.ImageLoadListener {

    private Context mContext;
    private ArrayList<T> mImageContents;
    private int mLoadConfiguration = POSITION_NONE;

    public SliderAdapter(Context context) {
        mContext = context;
        mImageContents = new ArrayList<>();
    }

    public void addSlider(T slider) {
        slider.setOnImageLoadListener(this);
        mImageContents.add(slider);
        addSingleNotification();
    }

    public void loadSliders(List<T> slider) {
        mLoadConfiguration = POSITION_UNCHANGED;
        mImageContents.clear();
        addSliders(slider);
    }

    private void addSingleNotification() {
        Iterator<T> it = mImageContents.iterator();
        int orderNumber = 0;
        while (it.hasNext()) {
            T slide = it.next();
            slide.setSlideOrderNumber(orderNumber);
            orderNumber++;
        }
        notifyDataSetChanged();
    }

    public void addSliders(List<T> slider) {
        Iterator<T> it = slider.iterator();
        int orderNumber = 0;
        while (it.hasNext()) {
            T slide = it.next();
            slide.setOnImageLoadListener(this);
            slide.setSlideOrderNumber(orderNumber);
            mImageContents.add(slide);
            orderNumber++;
        }
        notifyDataSetChanged();
    }

    public BaseSliderView getSliderView(int position) {
        if (position < 0 || position >= mImageContents.size()) {
            return null;
        } else {
            return mImageContents.get(position);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return mLoadConfiguration;
    }

    public void removeSlider(BaseSliderView slider) {
        if (mImageContents.contains(slider)) {
            mImageContents.remove(slider);
            notifyDataSetChanged();
        }
    }

    public void removeSliderAt(int position) {
        if (mImageContents.size() < position) {
            mImageContents.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeAllSliders() {
        mImageContents.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageContents.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseSliderView b = mImageContents.get(position);
        View v = b.getView();
        container.addView(v);
        return v;
    }

    @Override
    public void onStart(BaseSliderView target) {

    }

    /**
     * When image download error, then remove.
     *
     * @param result bool
     * @param target the based slider target
     */
    @Override
    public void onEnd(boolean result, BaseSliderView target) {
        if (target.isErrorDisappear() == false || result == true) {
            return;
        }
        if (!mRemoveItemOnFailureToLoad) return;
        for (BaseSliderView slider : mImageContents) {
            if (slider.equals(target)) {
                removeSlider(target);
                break;
            }
        }
    }

    private boolean mRemoveItemOnFailureToLoad = true;

    public final void setRemoveItemOnFailureToLoad(boolean b) {
        mRemoveItemOnFailureToLoad = b;
    }

}
