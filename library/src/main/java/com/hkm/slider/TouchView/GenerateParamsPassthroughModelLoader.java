package com.hkm.slider.TouchView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;

/**
 * Created by hesk on 8/3/16.
 */
public class GenerateParamsPassthroughModelLoader implements ModelLoader<GenerateParams, GenerateParams> {
    @Override
    public DataFetcher<GenerateParams> getResourceFetcher(final GenerateParams model, int width, int height) {
        return new DataFetcher<GenerateParams>() {
            @Override
            public GenerateParams loadData(Priority priority) throws Exception {
                return model;
            }

            @Override
            public void cleanup() {
            }

            @Override
            public String getId() {
                return model.getId();
            }

            @Override
            public void cancel() {
            }
        };
    }
}