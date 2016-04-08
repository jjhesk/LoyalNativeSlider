package com.hkm.slider.GalleryWidget;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hkm.slider.TouchView.FileTouchImageView;

/**
 Class wraps file paths to adapter, then it instantiates {@link com.hkm.slider.TouchView.FileTouchImageView} objects to paging up through them.
 */
public class FilePagerAdapter extends BasePagerAdapter {


    public FilePagerAdapter(Context context, List<String> resources)
	{
		super(context, resources);
	}

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((GalleryViewPager)container).mCurrentView = ((FileTouchImageView)object).getImageView();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position){
        final FileTouchImageView iv = new FileTouchImageView(mContext);
        iv.setUrl(mResources.get(position));
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        collection.addView(iv, 0);
        return iv;
    }

}
