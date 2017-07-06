package com.hkm.loyalns.demos;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hkm.loyalns.R;
import com.hkm.loyalns.Util.DataProvider;
import com.hkm.loyalns.mod.BaseApp;
import com.hkm.loyalns.modules.NumZero;
import com.hkm.loyalns.modules.TransformerAdapter;
import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.LoyalUtil;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.CompactFrameSliderView;
import com.hkm.slider.SliderTypes.CompactSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.hkm.slider.TransformerL;

import java.util.HashMap;

/**
 * Created by hesk on 24/11/15.
 */
public class MultSections extends BaseApp {

    @SuppressLint("ResourceAsColor")
    protected void setupSlider() {
        // remember setup first
        mDemoSlider.setPresetTransformer(TransformerL.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mDemoSlider.setCustomAnimation(new DescriptionAnimation(230, new DecelerateInterpolator()));
        }
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColorRes(R.color.red_pink_26, R.color.red_pink_27);
        final NumZero n = new NumZero(this);
        mDemoSlider.setNumLayout(n);
        mDemoSlider.presentation(SliderLayout.PresentationConfig.Numbers);
        ListView l = (ListView) findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());

            }
        });
        //and data second. it is a must because you will except the data to be streamed into the pipline.

        defaultCompleteSlider(DataProvider.getFileSrcHorizontal());
    }

    @Override
    protected int getActivityMainLayoutId() {
        return R.layout.full_scn;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final HashMap<String, Integer> maps) {
        try {

            TextSliderView s1ev_dks = new TextSliderView(this);
            // initialize a SliderLayout
            s1ev_dks
                    .description(getString(R.string.head_line_01))
                    .image(getString(R.string.hl_image_01))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .enableImageLocalStorage();
            //add your extra information
            // textSliderView.getBundle().putString("extra", name);
            mDemoSlider.addSlider(s1ev_dks);

            CompactSliderView s_dkf_12 = new CompactSliderView(this, 2);
            s_dkf_12

                    .setDisplayOnlyImageUrls(new String[]{
                            getString(R.string.hl_image_02),
                            getString(R.string.hl_image_03)
                    })
                    .setLinksOnEach(new String[]{
                            "http://www.wifi-egg.com/order.php",
                            "https://www.youtube.com/watch?v=uOx3Zdl4738"
                    })
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .enableImageLocalStorage()
                    .setOnSliderClickListener(this);
            mDemoSlider.addSlider(s_dkf_12.build());

            CompactSliderView s3 = new CompactSliderView(this, 3);
            s3

                    .setDisplayOnlyImageUrls(new String[]{
                            getString(R.string.hl_image_03),
                            getString(R.string.hl_image_04),
                            getString(R.string.hl_image_05)
                    })


                    // initialize a SliderLayout

                    .description(getString(R.string.head_line_02))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .enableImageLocalStorage()
                    .setOnSliderClickListener(this);

            mDemoSlider.addSlider(s3.build());


            CompactSliderView s_flowers_amazing_4 = new CompactSliderView(this, 4);
            s_flowers_amazing_4

                    .setDisplayOnlyImageUrls(new String[]{
                            getString(R.string.hl_image_01),
                            getString(R.string.hl_image_02),
                            getString(R.string.hl_image_03),
                            getString(R.string.hl_image_04)
                    })


                    // initialize a SliderLayout

                    .description(getString(R.string.head_line_02))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .enableImageLocalStorage()
                    .setOnSliderClickListener(this);

            mDemoSlider.addSlider(s_flowers_amazing_4.build());


            CompactFrameSliderView sCompactFrameSliderView5 = new CompactFrameSliderView(this, 4);
            sCompactFrameSliderView5

                    .setDescriptions(new String[]{
                            getString(R.string.head_line_03),
                            getString(R.string.head_line_07),
                            getString(R.string.head_line_05),
                            getString(R.string.head_line_01)

                    })
                    // initialize a SliderLayout

                    .setDisplayOnlyImageUrls(new String[]{
                            getString(R.string.hl_image_03),
                            getString(R.string.hl_image_07),
                            getString(R.string.hl_image_05),
                            getString(R.string.hl_image_01)
                    })

                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .enableImageLocalStorage()
                    .setOnSliderClickListener(this);

            mDemoSlider.addSlider(sCompactFrameSliderView5.build());

            CompactFrameSliderView s_setLinksOnEach_6 = new CompactFrameSliderView(this, 2);
            s_setLinksOnEach_6

                    .setDescriptions(new String[]{
                            getString(R.string.head_line_03),
                            getString(R.string.head_line_07)

                    })
                    //  .setSlideLayoutCustom(CompactFrameSliderView.RIPPLE_SLIDE)
                    // initialize a SliderLayout

                    .setDisplayOnlyImageUrls(new String[]{
                            getString(R.string.hl_image_03),
                            getString(R.string.hl_image_07)
                    })

                    .setLinksOnEach(new String[]{
                            "http://www.wifi-egg.com/order.php",
                            "https://www.youtube.com/watch?v=uOx3Zdl4738"
                    })
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    //  .enableSaveImageByLongClick(getFragmentManager())
                    .enableImageLocalStorage()
                    .setOnSliderClickListener(this);
            mDemoSlider.addSlider(s_setLinksOnEach_6.build());


            CompactFrameSliderView s_refffd = new CompactFrameSliderView(this, 2);
            s_refffd

                    .setDescriptions(new String[]{
                            getString(R.string.head_line_08),
                            getString(R.string.head_line_06)

                    })
                    // .setSlideLayoutCustom(CompactFrameSliderView.RIPPLE_SLIDE)
                    // initialize a SliderLayout

                    .setDisplayOnlyImageUrls(new String[]{
                            getString(R.string.hl_image_08),
                            getString(R.string.hl_image_06)
                    })


                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .enableImageLocalStorage()
                    .setOnSliderClickListener(this);

            mDemoSlider.addSlider(s_refffd.build());


        } catch (Exception e) {
            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }


    @Override
    public void onSliderClick(BaseSliderView coreSlider) {
        if (LoyalUtil.getUri(coreSlider) != null) {
            Toast.makeText(this,
                    LoyalUtil.getUri(coreSlider).getHost().toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
