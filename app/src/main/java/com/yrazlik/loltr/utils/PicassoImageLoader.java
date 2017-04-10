package com.yrazlik.loltr.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 28/03/17.
 */

public class PicassoImageLoader implements IImageLoader{

    private static PicassoImageLoader mInstance;
    private Context mContext;
    private int placeholderResId;
    private int errorResId;

    public static PicassoImageLoader getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new PicassoImageLoader();
            mInstance.setPlaceholder(R.drawable.placeholder);
            mInstance.setErrorResId(R.drawable.placeholder);
        }
        mInstance.setContext(context);
        return mInstance;
    }

    private void setContext(Context context) {
        mInstance.mContext = context;
    }

    private PicassoImageLoader() {}

    public PicassoImageLoader setPlaceholder(int placeholder) {
        mInstance.placeholderResId = placeholder;
        return mInstance;
    }

    public PicassoImageLoader setErrorResId(int errorResId) {
        mInstance.errorResId = errorResId;
        return mInstance;
    }

    @Override
    public void loadImage(String url, ImageView iv) {
        Picasso.with(mContext).load(url).placeholder(placeholderResId).error(errorResId).into(iv);
    }
}
