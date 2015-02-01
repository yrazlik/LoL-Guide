package com.yrazlik.loltr.view;

import android.os.Handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class RemoteImageLoader implements Runnable {

	private static final int DEFAULT_POOL_SIZE = 3;
	private static ThreadPoolExecutor executor;

	private String imageUrl;
	private String mLocal;
	private Handler handler;

	public RemoteImageLoader(String imageUrl, String mLocal, Handler handler) {
		this.imageUrl = imageUrl;
		this.mLocal = mLocal;
		this.handler = handler;
	}

	public static void shutdown() {
		if (executor != null) {
			executor.shutdown();
			executor = null;
		}
	}

	public static void start(String imageUrl, String mLocal, Handler handler) {
		if (executor == null) {
			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
		}
		executor.execute(new RemoteImageLoader(imageUrl, mLocal, handler));
	}

	@Override
	public void run() {

		HttpURLConnection connection = null;
		InputStream inputStream = null;
		FileOutputStream fileStream = null;

		boolean success = false;

		try {
			URL url = new URL(imageUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(10000);
			
			if (connection.getResponseCode() == 200) {

				inputStream = (InputStream) connection.getInputStream();

				if (inputStream != null) {
					fileStream = new FileOutputStream(mLocal);

					byte[] buffer = new byte[4096];
					int l;
					while ((l = inputStream.read(buffer)) != -1) {
						fileStream.write(buffer, 0, l);
					}
					success = true;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (fileStream != null) {
					fileStream.close();
					fileStream = null;
				}
			} catch (Exception e2) {
			}

			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (Exception e2) {
			}

			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}

		if (handler != null) {
			handler.sendEmptyMessage(success ? 0 : 1);
		}
	}

}
