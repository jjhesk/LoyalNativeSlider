package com.hkm.loyalns.demos;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hkm.loyalns.R;
import com.hkm.loyalns.Util.DataProvider;
import com.hkm.loyalns.modules.NumZero;
import com.hkm.loyalns.modules.TransformerAdapter;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.hkm.slider.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hesk on 17/3/16.
 */
public class BigScreenDL extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    protected SliderLayout mDemoSlider;

    protected boolean shouldRequestAPIBeforeLayoutRender() {
        return false;
    }


    @SuppressLint("ResourceAsColor")
    protected void setupSlider(final SliderLayout mDemoSlider) {
        // remember setup first
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //   mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColorRes(R.color.red_pink_26, R.color.red_pink_27);
        mDemoSlider.setNumLayout(new NumZero(this));
        mDemoSlider.presentation(SliderLayout.PresentationConfig.Numbers);
        ListView l = (ListView) findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
                Toast.makeText(BigScreenDL.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        if (!shouldRequestAPIBeforeLayoutRender()) {
            //and data second. it is a must because you will except the data to be streamed into the pipline.
            defaultCompleteSlider(mDemoSlider, DataProvider.getSingle());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final SliderLayout slide, final HashMap<String, String> maps) {
        ArrayList<TextSliderView> list = new ArrayList<>();
        for (String name : maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(maps.get(name))
                    .enableImageLocalStorage()
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.getBundle().putString("extra", name);
            list.add(textSliderView);
        }
        slide.loadSliderList(list);
    }

    private void add_one_more_slide() {
        TextSliderView textSliderView = new TextSliderView(this);
        String name = DataProvider.getRandomSingle().getKey();
        String image = DataProvider.getRandomSingle().getValue();
        // initialize a SliderLayout
        textSliderView
                .description(name)
                .image(image)
                .enableImageLocalStorage()
                .setScaleType(BaseSliderView.ScaleType.CenterInside)
                .enableSaveImageByLongClick(getFragmentManager())
                .setOnSliderClickListener(this);

        mDemoSlider.addSlider(textSliderView);
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPagerEx#SCROLL_STATE_IDLE
     * @see ViewPagerEx#SCROLL_STATE_DRAGGING
     * @see ViewPagerEx#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView coreSlider) {

    }

    ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_slider);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        add = (ImageButton) findViewById(R.id.addMore);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_one_more_slide();
            }
        });
        setupSlider(mDemoSlider);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }
}
