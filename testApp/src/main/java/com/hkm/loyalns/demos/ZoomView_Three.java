package com.hkm.loyalns.demos;

import com.hkm.loyalns.mod.NsZoomable;
import com.hkm.slider.GalleryWidget.GalleryViewPager;
import com.hkm.slider.GalleryWidget.UrlPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hesk on 8/3/16.
 */
public class ZoomView_Three extends NsZoomable {
    @Override
    protected void startViewPager(GalleryViewPager mViewpager) {
        String[] urls = {
                //   "http://pcdn.500px.net/35939982/127d53ceac436e2e17a11ea42bb2cd7719b9f1e1/4.jpg",
                "https://drscdn.500px.org/photo/149207821/m%3D900/61196063c2685f6c3461bbc8568b3a2e",
                //    special url with error
                "https://drscdn.500px.org/photo/144545025/m%3D900/aa1681951f730749a27dc1aec604ca4f",
                "https://drscdn.500px.org/photo/138365879/m%3D900/a349013757f0766bccd3024e237c3601",
        };
//https://drscdn.500px.org/photo/139154171/m%3D900/d5649ca460658ac3e82f7a2b50a5a3e6

        List<String> items = new ArrayList<String>();
        Collections.addAll(items, urls);
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setCurrentItem(3);
    }
}
