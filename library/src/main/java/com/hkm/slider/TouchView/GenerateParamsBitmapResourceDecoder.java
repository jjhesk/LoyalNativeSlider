package com.hkm.slider.TouchView;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.io.IOException;

/**
 * Created by hesk on 8/3/16.
 */
public class GenerateParamsBitmapResourceDecoder implements ResourceDecoder<GenerateParams, Bitmap> {
    private final Context context;

    public GenerateParamsBitmapResourceDecoder(Context context) {
        this.context = context;
    }

    @Override
    public Resource<Bitmap> decode(GenerateParams source, int width, int height) throws IOException {
        BitmapPool pool = Glide.get(context).getBitmapPool();
        Bitmap bitmap = pool.getDirty(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Generators.imageWithTextNoLayout(context, bitmap, source);
        return BitmapResource.obtain(bitmap, pool);
    }

    @Override
    public String getId() {
        // be careful if you change the Generator implementation you have to change this
        // otherwise the users may see a cached image; or clear cache on app update
        return "com.example.MyImageGenerator";
    }
}

