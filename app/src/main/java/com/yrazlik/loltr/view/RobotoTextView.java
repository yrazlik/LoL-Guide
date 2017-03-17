package com.yrazlik.loltr.view;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoTextView extends TextView {

    public static final String FONT_NORMAL = "fonts/Roboto.ttf";
    public static final String FONT_MEDIUM = "fonts/Roboto-Medium.ttf";
    public static final String FONT_BOLD = "fonts/Roboto-Medium.ttf";

	public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RobotoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RobotoTextView(Context context) {
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
		} else if(currentTypeFace != null && currentTypeFace.getStyle() == Typeface.ITALIC){
			Typeface tf = Typefaces.get(getContext(), FONT_NORMAL);
			this.setTypeface(tf);
		} else {
			Typeface tf = Typefaces.get(getContext(), FONT_NORMAL);
			this.setTypeface(tf);
		}
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