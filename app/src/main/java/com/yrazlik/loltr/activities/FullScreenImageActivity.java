package com.yrazlik.loltr.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.view.ZoomableImageView;

/**
 * Created by yrazlik on 22/03/17.
 */

public class FullScreenImageActivity extends AppCompatActivity{

    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";
    private String imageUrl;

    private ZoomableImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        img = (ZoomableImageView) findViewById(R.id.img);
        getExtras();

        LolImageLoader.getInstance().loadImage(imageUrl, img);
    }

    private void getExtras() {
        imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
    }

}
