package com.hkm.slider;

import com.hkm.slider.Transformers.AccordionTransformer;
import com.hkm.slider.Transformers.BackgroundToForegroundTransformer;
import com.hkm.slider.Transformers.BaseTransformer;
import com.hkm.slider.Transformers.CubeInTransformer;
import com.hkm.slider.Transformers.DefaultTransformer;
import com.hkm.slider.Transformers.DepthPageTransformer;
import com.hkm.slider.Transformers.FadeTransformer;
import com.hkm.slider.Transformers.FlipHorizontalTransformer;
import com.hkm.slider.Transformers.FlipPageViewTransformer;
import com.hkm.slider.Transformers.ForegroundToBackgroundTransformer;
import com.hkm.slider.Transformers.RotateDownTransformer;
import com.hkm.slider.Transformers.RotateUpTransformer;
import com.hkm.slider.Transformers.StackTransformer;
import com.hkm.slider.Transformers.TabletTransformer;
import com.hkm.slider.Transformers.ZoomInTransformer;
import com.hkm.slider.Transformers.ZoomOutSlideTransformer;
import com.hkm.slider.Transformers.ZoomOutTransformer;
import com.hkm.slider.Transformers.ParallaxPageTransformer;
import com.hkm.slider.Transformers.FlipVerticalTransformer;
import com.hkm.slider.Transformers.CubeOutTransformer;
import com.hkm.slider.Transformers.DrawFromBackTransformer;

import java.util.Random;

/**
 * Created by hesk on 6/25/2015.
 */
public enum TransformerL {
    Default("Default"),
    Accordion("Accordion"),
    Background2Foreground("Background2Foreground"),
    CubeIn("CubeIn"),
    CubeOutTransformer("CubeOutTransformer"),
    DepthPage("DepthPage"),
    DrawFromBackTransformer("DrawFromBackTransformer"),
    Fade("Fade"),
    FlipHorizontal("FlipHorizontal"),
    FlipPage("FlipPage"),
    FlipVerticalTransformer("FlipVerticalTransformer"),
    Foreground2Background("Foreground2Background"),
    ParallaxPageTransformer("ParallaxPageTransformer"),
    RotateDown("RotateDown"),
    RotateUp("RotateUp"),
    Stack("Stack"),
    Tablet("Tablet"),
    ZoomIn("ZoomIn"),
    ZoomOutSlide("ZoomOutSlide"),
    ZoomOut("ZoomOut"),
    Shuffle("Shuffle");

    private final String name;

    TransformerL(String s) {
        name = s;
    }

    public String toString() {
        return name;
    }

    public boolean equals(String other) {
        return (other == null) ? false : name.equals(other);
    }

    private BaseTransformer bySymbol(TransformerL e) {
        BaseTransformer t = null;
        switch (e) {
            case Default:
                t = new DefaultTransformer();
                break;
            case Accordion:
                t = new AccordionTransformer();
                break;
            case Background2Foreground:
                t = new BackgroundToForegroundTransformer();
                break;
            case CubeIn:
                t = new CubeInTransformer();
                break;
            case DepthPage:
                t = new DepthPageTransformer();
                break;
            case Fade:
                t = new FadeTransformer();
                break;
            case FlipHorizontal:
                t = new FlipHorizontalTransformer();
                break;
            case FlipPage:
                t = new FlipPageViewTransformer();
                break;
            case Foreground2Background:
                t = new ForegroundToBackgroundTransformer();
                break;
            case RotateDown:
                t = new RotateDownTransformer();
                break;
            case RotateUp:
                t = new RotateUpTransformer();
                break;
            case Stack:
                t = new StackTransformer();
                break;
            case Tablet:
                t = new TabletTransformer();
                break;
            case ZoomIn:
                t = new ZoomInTransformer();
                break;
            case ZoomOutSlide:
                t = new ZoomOutSlideTransformer();
                break;
            case ZoomOut:
                t = new ZoomOutTransformer();
                break;
            case ParallaxPageTransformer:
                // t = new ParallaxPageTransformer(4);
                t = new ZoomOutTransformer();
                break;
            case FlipVerticalTransformer:
                t = new FlipVerticalTransformer();
                break;
            case CubeOutTransformer:
                t = new CubeOutTransformer();
                break;
            case DrawFromBackTransformer:
                t = new DrawFromBackTransformer();
                break;
        }
        return t;
    }

    public BaseTransformer getTranformFunction() {
        return bySymbol(this);
    }

    public static BaseTransformer randomSymbol() {
        int transformerNumber = TransformerL.values().length;
        int random = new Random().nextInt(transformerNumber - 1);
        TransformerL ts = TransformerL.values()[random];
        return ts.bySymbol(ts);
    }

    public static TransformerL byName(String name) {
        for (TransformerL t : TransformerL.values()) {
            if (t.equals(name)) {
                return TransformerL.valueOf(name);
            }
        }
        return Default;
    }

    public static TransformerL fromVal(int transformerId) {
        for (TransformerL t : TransformerL.values()) {
            if (t.ordinal() == transformerId) {
                return TransformerL.values()[t.ordinal()];
            }
        }
        return Default;
    }
}
