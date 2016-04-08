package com.hkm.slider.GalleryWidget;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 Class wraps URLs to adapter, then it instantiates <b>UrlTouchImageView</b> objects to paging up through them.
 */
public class BasePagerAdapter extends PagerAdapter {

	protected final List<String> mResources;
    protected final Context mContext;
    protected int mCurrentPosition = -1;
    protected OnItemChangeListener mOnItemChangeListener;

    public BasePagerAdapter()
    {
        mResources = null;
        mContext = null;
    }

    public BasePagerAdapter(Context context, List<String> resources)
    {
        this.mResources = resources;
        this.mContext = context;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, final int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (mCurrentPosition == position) return;
        GalleryViewPager galleryContainer = ((GalleryViewPager)container);
        if (galleryContainer.mCurrentView != null) {
            galleryContainer.mCurrentView.resetScale();
        }
        mCurrentPosition = position;
        if (mOnItemChangeListener != null) mOnItemChangeListener.onItemChange(mCurrentPosition);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view){
        collection.removeView((View) view);
    }

    @Override
    public int getCount()
    {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view.equals(object);
    }
    
    @Override
    public void finishUpdate(ViewGroup arg0){
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1){
    }

    @Override
    public Parcelable saveState(){
        return null;
    }

    @Override
    public void startUpdate(ViewGroup arg0) { }
    
    public int getCurrentPosition() { return mCurrentPosition; }
    
    public void setOnItemChangeListener(OnItemChangeListener listener) { mOnItemChangeListener = listener; }
    
    public static interface OnItemChangeListener 
    {
    	public void onItemChange(int currentPosition);
    }
};