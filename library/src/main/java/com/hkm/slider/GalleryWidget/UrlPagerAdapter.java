package com.hkm.slider.GalleryWidget;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hkm.slider.TouchView.UrlTouchImageView;


/**
 * Class wraps URLs to adapter, then it instantiates {@link com.hkm.slider.TouchView.FileTouchImageView} objects to paging up through them.
 */
public class UrlPagerAdapter extends BasePagerAdapter {

    public UrlPagerAdapter(Context context, List<String> resources) {
        super(context, resources);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((GalleryViewPager) container).mCurrentView = ((UrlTouchImageView) object).getImageView();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        final UrlTouchImageView iv = new UrlTouchImageView(mContext);
        iv.setUrl(mResources.get(position));
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        collection.addView(iv, 0);
        return iv;
    }
}
