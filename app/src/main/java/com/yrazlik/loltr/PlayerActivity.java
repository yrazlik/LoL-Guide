package com.yrazlik.loltr;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlayerActivity extends Activity implements SurfaceHolder.Callback {
    String path;
    private MediaPlayer mp;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    boolean pausing = false;
    public static String filepath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        mPreview = (SurfaceView)findViewById(R.id.surfaceview);
        holder = mPreview.getHolder();
        holder.setFixedSize(800, 480);
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mp = new MediaPlayer();


        try{
            Intent intent = getIntent();

            Uri fileuri = Uri.parse("http://cdn.leagueoflegends.com/champion-abilities/videos/mp4/0099_05.mp4");
            filepath=fileuri.toString();
        }catch(Exception e){}


    }
    protected void onPause(){
        super.onPause();
        mp.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mp.setDisplay(holder);
        play();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }
    void play(){
    	Handler h = new Handler();
    	h.post(new Runnable() {
			
			@Override
			public void run() {
				 try {
			            mp.setDataSource(filepath);

			            mp.prepare(); 

			        } catch (IllegalArgumentException e) {
			            e.printStackTrace();
			        } catch (IllegalStateException e) {
			            e.printStackTrace();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        mp.start();
				
			}
		});
       
    }
}