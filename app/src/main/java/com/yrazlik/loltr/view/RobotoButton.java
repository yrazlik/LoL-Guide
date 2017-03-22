package com.yrazlik.loltr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.Hashtable;

import static com.yrazlik.loltr.commons.Commons.FONT_BOLD;
import static com.yrazlik.loltr.commons.Commons.FONT_ITALIC;
import static com.yrazlik.loltr.commons.Commons.FONT_NORMAL;

/**
 * Created by yrazlik on 22/03/17.
 */

public class RobotoButton extends Button{

    public RobotoButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RobotoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoButton(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void init() {

        Typeface currentTypeFace = getTypeface();

        if (currentTypeFace != null && currentTypeFace.getStyle() == Typeface.BOLD) {
            Typeface tf = Typefaces.get(getContext(), FONT_BOLD);
            this.setTypeface(tf);
        } else if(currentTypeFace != null && currentTypeFace.getStyle() == Typeface.ITALIC) {
            Typeface tf = Typefaces.get(getContext(), FONT_ITALIC);
            this.setTypeface(tf);
        } else {
            Typeface tf = Typefaces.get(getContext(), FONT_NORMAL);
            this.setTypeface(tf);
        }

        setTransformationMethod(null);
    }

    public static class Typefaces {

        private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

        public static Typeface get(Context c, String assetPath) {
            synchronized (cache) {
                if (!cache.containsKey(assetPath)) {
                    try {
                        Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                        cache.put(assetPath, t);
                    } catch (Exception e) {
                        return null;
                    }
                }
                return cache.get(assetPath);
            }
        }
    }
}
