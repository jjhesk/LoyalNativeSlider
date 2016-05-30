package com.hkm.loyalns.demos;

import android.widget.Toast;

import com.hkm.loyalns.mod.NsZoomable;
import com.hkm.slider.GalleryWidget.BasePagerAdapter;
import com.hkm.slider.GalleryWidget.FilePagerAdapter;
import com.hkm.slider.GalleryWidget.GalleryViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hesk on 8/3/16.
 */
public class ZoomViewPager2 extends NsZoomable {
    private void copy(InputStream in, File dst) throws IOException {
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private FilePagerAdapter getImages() {
        String[] urls = null;
        List<String> items = new ArrayList<String>();
        try {
            urls = getAssets().list("");

            for (String filename : urls) {
                if (filename.matches(".+\\.jpg")) {
                    String path = getFilesDir() + "/" + filename;
                    copy(getAssets().open(filename), new File(path));
                    items.add(path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FilePagerAdapter pagerAdapter = new FilePagerAdapter(this, items);

        return pagerAdapter;
    }


    @Override
    protected void startViewPager(final GalleryViewPager mViewpager) {

        FilePagerAdapter pagerAdapter = getImages();
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                Toast.makeText(ZoomViewPager2.this, "Current item is " + currentPosition, Toast.LENGTH_SHORT).show();
            }
        });

        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setAdapter(pagerAdapter);
    }
}
