package com.hkm.loyalns.demos;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hkm.loyalns.R;
import com.hkm.loyalns.Util.DataProvider;
import com.hkm.loyalns.mod.BaseApp;
import com.hkm.loyalns.modules.CustomNumberView;
import com.hkm.loyalns.modules.NumZero;
import com.hkm.loyalns.modules.TransformerAdapter;
import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.Indicators.PagerIndicator;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.hkm.slider.TransformerL;

import java.util.ArrayList;
import java.util.HashMap;


public class ExampleClassic extends BaseApp {

    @SuppressLint("ResourceAsColor")
    protected void setupSlider() {
        // remember setup first
        mDemoSlider.setPresetTransformer(TransformerL.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
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
                Toast.makeText(ExampleClassic.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //and data second. it is a must because you will except the data to be streamed into the pipline.
        mDemoSlider.setEnableMaxHeightFromAllSliders(new SliderLayout.OnViewConfigurationFinalized() {
            @Override
            public void onDeterminedMaxHeight(int height) {
                final int heifght = 700;
                mFrameMain.setLayoutParams(new RelativeLayout.LayoutParams(-1, heifght));
                mDemoSlider.setCurrentPositionStatic(0);
            }
        });
        defaultCompleteSlider(DataProvider.getFileSrcHorizontal());
    }

    /**
     * this is the example of the dynamic loading of the sliders
     *
     * @param maps the list of the slider
     */
    protected void customSliderView(final HashMap<String, String> maps) {
        for (String name : maps.keySet()) {
            CustomNumberView textSliderView = new CustomNumberView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(maps.get(name))

                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.getBundle().putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }
    }

    Handler mHandler = new Handler();

    /**
     * try this new loading mechanism
     *
     * @param maps the map of loading list
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final HashMap<String, Integer> maps) {
        final ArrayList<TextSliderView> loadingList = new ArrayList<>();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (String name : maps.keySet()) {
                    TextSliderView textSliderView = new TextSliderView(ExampleClassic.this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .enableSaveImageByLongClick(getFragmentManager())
                            .setOnSliderClickListener(ExampleClassic.this);
                    //add your extra information
                    textSliderView.getBundle().putString("extra", name);
                    loadingList.add(textSliderView);
                }


                mDemoSlider.loadSliderList(loadingList);
                mDemoSlider.setCurrentPositionStatic(2);
            }
        }, 3000);
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle_number_display:
                numbered = !numbered;
                mDemoSlider.presentation(numbered ? SliderLayout.PresentationConfig.Numbers : SliderLayout.PresentationConfig.Dots);
                break;

            case R.id.indicator_default:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator2));
                break;

            case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;


            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;

            case R.id.custom_slider_layout:
                newcustomSliderView();
                break;

            case R.id.default_slider_layout:
                newloaddefaultCompleteSlider();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    protected void newcustomSliderView() {
        final Handler n = new Handler();
        mDemoSlider.removeAllSliders();
        n.postDelayed(new Runnable() {
            @Override
            public void run() {
                customSliderView(DataProvider.getDataUrlSource());
            }
        }, 2000);
    }

    protected void newloaddefaultCompleteSlider() {
        final Handler n = new Handler();
        mDemoSlider.removeAllSliders();
        n.postDelayed(new Runnable() {
            @Override
            public void run() {
                defaultCompleteSlider(DataProvider.getFileSrcHorizontal());
            }
        }, 2000);
    }


}
