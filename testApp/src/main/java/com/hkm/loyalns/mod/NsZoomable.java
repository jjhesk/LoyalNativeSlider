package com.hkm.loyalns.mod;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hkm.loyalns.R;
import com.hkm.slider.GalleryWidget.BasePagerAdapter;
import com.hkm.slider.GalleryWidget.FilePagerAdapter;
import com.hkm.slider.GalleryWidget.GalleryViewPager;

/**
 * Created by hesk on 8/3/16.
 */
public abstract class NsZoomable extends AppCompatActivity {
    private GalleryViewPager mViewPager;


    protected abstract void startViewPager(GalleryViewPager mViewpager);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_slide);
        mViewPager = (GalleryViewPager) findViewById(R.id.slider);
        startViewPager(mViewPager);

    }

    /**
     * Call this when your activity is done and should be closed.  The
     * ActivityResult is propagated back to whoever launched you via
     * onActivityResult().
     */
    @Override
    public void finish() {
        super.finish();
        Runtime.getRuntime().gc();
    }
}
