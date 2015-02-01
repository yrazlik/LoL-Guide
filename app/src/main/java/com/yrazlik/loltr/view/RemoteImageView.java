package com.yrazlik.loltr.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RemoteImageView extends ImageView {

    private static String BASE_PATH;
    private static String FOLDER_NAME = ".lol-remote-image-view-cache";
    private static String TEMPORARY_CACHE = "temporary-cache";
    private static String PERMANENT_CACHE = "permanent-cache";
    private static String PACKAGE_NAME = "com.yrazlik.leagueoflegends";
    /**
     * DEFAULT MAX IMAGE CACHE SIZE: 5MB
     */
    private static long DEFAULT_MAX_SIZE = 5 * 1024 * 1024;
    private static long GB_SIZE_5 = 5 * 1024 * 1024 * 1024;
    private static long GB_SIZE_1 = 1024 * 1024 * 1024;

    static {
        try {

            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                BASE_PATH = "/data/data/" + PACKAGE_NAME;
            } else {
                BASE_PATH = Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME;
            }

            StatFs stat = new StatFs(BASE_PATH);
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();

            /**
             * Decide Cache Size
             */
            long totalFreeSpace = availableBlocks * blockSize;
            if (totalFreeSpace > GB_SIZE_5) {
                DEFAULT_MAX_SIZE = DEFAULT_MAX_SIZE * 10;
            } else if (totalFreeSpace > GB_SIZE_1) {
                DEFAULT_MAX_SIZE = DEFAULT_MAX_SIZE * 3;
            } else if (totalFreeSpace < DEFAULT_MAX_SIZE) {
                /* SDCard has no space so base path turn internal storage */
                BASE_PATH = "/data/data/" + PACKAGE_NAME;
            }

            File temporaryCacheDirectory = new File(BASE_PATH + "/" + TEMPORARY_CACHE);
            TreeMap<String, String> expiredFiles = new TreeMap<String, String>();
            long TOTAL_LENGTH = 0;
            if (temporaryCacheDirectory.isDirectory()) {
                String[] children = temporaryCacheDirectory.list();
                for (int i = 0; i < children.length; i++) {
                    File file = new File(temporaryCacheDirectory, children[i]);
                    TOTAL_LENGTH += file.length();
                    expiredFiles.put(file.lastModified() + children[i],
                            temporaryCacheDirectory + "/" + children[i]);
                }
            }

            if (TOTAL_LENGTH > DEFAULT_MAX_SIZE) {
                clearCache(TOTAL_LENGTH, expiredFiles);
            }

        } catch (Exception e) {
            // No problem
        }

    }

    Drawable d;
    private List<ImageDownloadedListener> imageDownloadedListener;
    private boolean isLoyalty = false;
    private LinearLayout progressLayout;
    private String mLocal;
    private String mRemote;
    private int emptyImageResource;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setFromLocal();
        }
    };

    public RemoteImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static void clearCache(long totalFileSize,
                                   TreeMap<String, String> expiredFiles) {

        long MAX_FLUSHED_SIZE = totalFileSize - (DEFAULT_MAX_SIZE / 2);
        long flushedSize = 0;

        for (Map.Entry<String, String> entry : expiredFiles.entrySet()) {
            String filePath = entry.getValue();
            File expiredFile = new File(filePath);
            if (expiredFile.exists()) {
                flushedSize += expiredFile.length();
                if (flushedSize > MAX_FLUSHED_SIZE) {
                    break;
                }
                expiredFile.delete();
            }
        }
    }

    public void setOnImageLoadedListener(ImageDownloadedListener listener) {
        if(imageDownloadedListener==null)
            imageDownloadedListener = new ArrayList<ImageDownloadedListener>();

        imageDownloadedListener.add(listener);
    }

    public void setLocalURI(String local) {
        mLocal = local;
    }

    public void setRemoteURI(String uri) {
        if (uri != null && uri.startsWith("http")) {
            mRemote = uri;
        }
    }

    public void loadImage() {
        loadImage(false);
    }

    public void loadImage(boolean permanentCache) {
        if (mRemote != null) {
            if (mLocal == null) {
                String folder = permanentCache ? PERMANENT_CACHE
                        : TEMPORARY_CACHE;
                mLocal = BASE_PATH + "/" + folder + "/" + mRemote.hashCode();
            }

            if (isLoyalty) {
                progressLayout.setVisibility(View.VISIBLE);
            }

            File local = new File(mLocal);
            if (local.exists()) {
                setFromLocal();
            } else {
                local.getParentFile().mkdirs();
                queue();
            }
        }
    }

    private void queue() {
        RemoteImageLoader.start(mRemote, mLocal, mHandler);
    }

    private void setFromLocal() {
    	new LoadFromLocal().execute();
//        d = Drawable.createFromPath(mLocal);
//
//        if (d != null) {
//
//            if (imageDownloadedListener != null) {
//
//                for(ImageDownloadedListener listener:imageDownloadedListener)
//                    listener.onImageLoaded(d);
//
//            } else {
//                setImageDrawable(d);
//            }
//        } else {
//            clearImage();
//        }
//
//        if (isLoyalty) {
//            progressLayout.setVisibility(View.GONE);
//        }

    }
    
    private class LoadFromLocal extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			d = Drawable.createFromPath(mLocal);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
	        if (d != null) {

	            if (imageDownloadedListener != null) {

	                for(ImageDownloadedListener listener:imageDownloadedListener)
	                    listener.onImageLoaded(d);

	            } else {
	                setImageDrawable(d);
	            }
	        } else {
	            clearImage();
	        }

	        if (isLoyalty) {
	            progressLayout.setVisibility(View.GONE);
	        }
		}
    	
    }

    private void clearImage() {
        if (emptyImageResource != 0) {
            setImageResource(emptyImageResource);
        }
    }

    public void setEmptyImageResource(int emptyImageResource) {
        this.emptyImageResource = emptyImageResource;
    }

    public void setLoyaltyState(boolean isLoyalty_, LinearLayout progress) {
        isLoyalty = isLoyalty_;
        progressLayout = progress;
    }

    public interface ImageDownloadedListener {
        public void onImageLoaded(Drawable image);
    }

}