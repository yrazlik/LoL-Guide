package com.yrazlik.loltr;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yrazlik.loltr.utils.IImageLoader;

/**
 * Created by yrazlik on 31/01/17.
 */

public class LolImageLoader implements IImageLoader{

    public static LolImageLoader mInstance;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    private LolImageLoader() {
        initImageLoader();
    }

    public static LolImageLoader getInstance() {

        if(mInstance == null) {
            mInstance = new LolImageLoader();
        }
        return mInstance;
    }

    private void initImageLoader() {
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).showImageOnLoading(R.drawable.placeholder).showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder).cacheOnDisk(true).cacheInMemory(true)
                .build();

        if(!mImageLoader.isInited()) {
            mImageLoader.init(ImageLoaderConfiguration.createDefault(LolApplication.getAppContext()));
        }
    }

    @Override
    public void loadImage(final String imageUrl, final ImageView iv) {
        load(imageUrl, iv, mOptions);
    }

    public void loadImage(final String imageUrl, final ImageView iv, DisplayImageOptions options) {
        load(imageUrl, iv, options);
    }

    private void load(final String imageUrl, final ImageView iv, DisplayImageOptions options) {
        if(imageUrl != null) {
            mImageLoader.displayImage(imageUrl, iv, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {}

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}

                @Override
                public void onLoadingCancelled(String imageUri, View view) {}
            });
        } else {
            mImageLoader.displayImage("", iv, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {}

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {}

                @Override
                public void onLoadingCancelled(String imageUri, View view) {}
            });
        }
    }

}
