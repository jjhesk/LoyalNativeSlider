package com.hkm.loyalns.demos;

import com.hkm.loyalns.mod.NsZoomable;
import com.hkm.slider.GalleryWidget.GalleryViewPager;
import com.hkm.slider.GalleryWidget.UrlPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hesk on 17/3/16.
 */
public class ZoomView_Single extends NsZoomable {
    @Override
    protected void startViewPager(GalleryViewPager mViewpager) {
        String[] urls = {
                "https://drscdn.500px.org/photo/139154171/m%3D900/d5649ca460658ac3e82f7a2b50a5a3e6",
        };
        List<String> items = new ArrayList<String>();
        Collections.addAll(items, urls);
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setCurrentItem(3);
    }
}
