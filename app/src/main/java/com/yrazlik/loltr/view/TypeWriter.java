package com.yrazlik.loltr.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by yrazlik on 06/08/16.
 */
public class TypeWriter extends TextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 75; //Default 500ms delay


    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            try {
                setText(mText.subSequence(0, mIndex++));
                if (mIndex <= mText.length()) {
                    mHandler.postDelayed(characterAdder, mDelay);
                }
            }catch (Exception ignored) {}
        }
    };

    public void animateText(CharSequence text) {
        try {
            mText = text;
            mIndex = 0;

            setText("");
            mHandler.removeCallbacks(characterAdder);
            mHandler.postDelayed(characterAdder, mDelay);
        } catch (Exception ignored) {}
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}