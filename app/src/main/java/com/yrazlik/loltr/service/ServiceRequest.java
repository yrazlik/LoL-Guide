package com.yrazlik.loltr.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.requestclasses.Request;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.responseclasses.AllItemsResponse;
import com.yrazlik.loltr.responseclasses.ChampionLegendResponse;
import com.yrazlik.loltr.responseclasses.ChampionOverviewResponse;
import com.yrazlik.loltr.responseclasses.ChampionRpIpCostsResponse;
import com.yrazlik.loltr.responseclasses.ChampionSkinsResponse;
import com.yrazlik.loltr.responseclasses.ChampionSpellsResponse;
import com.yrazlik.loltr.responseclasses.ChampionStrategyResponse;
import com.yrazlik.loltr.responseclasses.ItemDetailResponse;
import com.yrazlik.loltr.responseclasses.LeagueInfoResponse;
import com.yrazlik.loltr.responseclasses.LiveChannelsResponse;
import com.yrazlik.loltr.responseclasses.MatchInfoResponse;
import com.yrazlik.loltr.responseclasses.RecommendedItemsResponse;
import com.yrazlik.loltr.responseclasses.RuneResponse;
import com.yrazlik.loltr.responseclasses.StaticDataWithAltImagesResponse;
import com.yrazlik.loltr.responseclasses.StatsResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfoResponse;
import com.yrazlik.loltr.responseclasses.WeeklyFreeChampionsResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ServiceRequest {

	private boolean success = true;
	private static ServiceRequest instance;
	private Context mContext;
	private int TIMER_OF_INTERNET_PROBLEM;
	public static Dialog progressDialog;
	private boolean showedInternetErrorMessage = false;
	private Dialog infoDialog;
	private HttpClient httpClient;
	private ThreadSafeClientConnManager threadSafeClientConnManager;
	private HttpResponse response;

	public static ServiceRequest getInstance() {
		if (instance == null) {
			instance = new ServiceRequest();
		}
		return instance;
	}

	public void makeGetRequest(int requestID, ArrayList<String> pathParams,
			HashMap<String, String> queryParams, Object requestData,
			ResponseListener listener) {
		Request request = new Request(requestID, pathParams, queryParams);
		request.setListener(listener);
		request.setCancelled(false);
		mContext = request.getListener().getContext();
		connect(request, requestData);
	}

	public HttpClient getNewHttpClient() {
		try {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "UTF-8");
			HttpConnectionParams.setConnectionTimeout(params, 95 * 1000);
			HttpConnectionParams.setSoTimeout(params, 95 * 1000);
			HttpConnectionParams.setStaleCheckingEnabled(params, false);
			params.setBooleanParameter("http.protocol.expect-continue", false);

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

	protected void connect(Request request, Object requestData) {

		Gson gSon = new Gson();

		if (requestData != null) {
			try {
				request.setData(gSon.toJson(requestData));
			} catch (Exception e) {

			}
			// request.data = Utils.removeTurkishLettersWithCode(request.data);
			Log.d(Commons.TAG, request.getData());
		}

        try {
            progressDialog = showLoading((Activity)mContext, "sss");
            progressDialog.show();
        }catch (Exception e){}
		new httpGetRequestTask().execute(request);
	}

	private void waitForNetwork() {
		ConnectivityManager conMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		while (true) {
			NetworkInfo activeInfo = conMgr.getActiveNetworkInfo();
			if (activeInfo != null && activeInfo.isConnected()) {
				TIMER_OF_INTERNET_PROBLEM = 0;
				if (showedInternetErrorMessage) {
					try {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
					} catch (Exception e) {
						Log.e(Commons.TAG, e.toString());
					}
				}
				return;
			} else {

				TIMER_OF_INTERNET_PROBLEM++;
				if (!showedInternetErrorMessage
						&& TIMER_OF_INTERNET_PROBLEM == 6) {
					showedInternetErrorMessage = true;
					break;
				}
			}
		}
	}
	
	private String getServiceEndpointUrl(int requestId){
		switch(requestId){
			case Commons.WEEKLY_FREE_CHAMPIONS_REQUEST:
				return Commons.SERVICE_BASE_URL;
			case Commons.STATIC_DATA_WITH_ALT_IMAGES_REQUEST:
			case Commons.CHAMPION_OVERVIEW_REQUEST:
			case Commons.CHAMPION_SPELLS_REQUEST:
			case Commons.ALL_CHAMPIONS_REQUEST:
			case Commons.CHAMPION_LEGEND_REQUEST:
			case Commons.CHAMPION_STRATEGY_REQUEST:
			case Commons.RECOMMENDED_ITEMS_REQUEST:
			case Commons.ALL_ITEMS_REQUEST:
			case Commons.ITEM_DETAIL_REQUEST:
			case Commons.ALL_RUNES_REQUEST:
            case Commons.CHAMPION_SKINS_REQUEST:
				return Commons.STATIC_DATA_BASE_URL;
			case Commons.CHAMPION_RP_IP_COSTS_REQUEST:
				return Commons.URL_CHAMPION_PRICES;
			case Commons.CHAMPION_SPLASH_IMAGE_REQUEST:
				return Commons.CHAMPION_SPLASH_IMAGE_BASE_URL;
            case Commons.LIVE_CHANNELS_REQUEST:
                return Commons.LIVE_CHANNELS_URL;
            case Commons.MATCH_INFO_REQUEST:
                return Commons.SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED;
            case Commons.SUMMONER_INFO_REQUEST:
                return Commons.SERVICE_BASE_URL_CURRENT;
            case Commons.LEAGUE_INFO_REQUEST:
                return Commons.SERVICE_BASE_URL_CURRENT;
            case Commons.STATS_REQUEST:
                return Commons.SERVICE_BASE_URL_CURRENT;
			default:
				return "";
		}
	}

	private HttpResponse getResponse(Request request) throws Exception {
		String urlString = getServiceEndpointUrl(request.getRequestID())
				+ request.getPathParametersString()
				+ request.getQueryParametersString();

		httpClient = getNewHttpClient();

		Log.d(Commons.TAG, "CONNECTION OPENED");

		int tryLimit = 10;
		boolean errorFlag;

		do {
			errorFlag = false;
			try {
				response = httpClient.execute(doGet(urlString));
				break;

			} catch (SocketTimeoutException e) {
				throw e;
			} catch (HttpHostConnectException e) {
				errorFlag = true;
				--tryLimit;
			}
		} while (tryLimit > 0 && errorFlag);

		Log.d(Commons.TAG, "CONNECTION CLOSED");

		return response;
	}

	private HttpGet doGet(String urlString) {

		HttpGet get = new HttpGet(urlString);

		get.setHeader("Accept", "application/json");
		// get.setHeader("Content-Type", "application/json");
		// get.setHeader("Accept-Encoding", "gzip,deflate");
		// get.setHeader("Accept-Language", "en-US,en");

		return get;
	}

	private String getResponseBodyString(Object response) {
		if (response != null) {
			response = (HttpResponse) response;
			HttpEntity entity = null;
			entity = ((HttpResponse) response).getEntity();
			InputStream responseStream = null;
			try {
				responseStream = entity.getContent();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			StringBuilder buffer = new StringBuilder();
			char[] chrBuffer = new char[1024];
			Reader reader = null;
			try {
				reader = new InputStreamReader(responseStream, "UTF-8");
				int lengthOfReadBytes;
				while ((lengthOfReadBytes = reader.read(chrBuffer)) != -1) {
					buffer.append(chrBuffer, 0, lengthOfReadBytes);
				}
				return buffer.toString();
			} catch (Exception e) {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} finally {
				try {
					responseStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				chrBuffer = null;
			}
		}

		return null;
	}
	
	private String filterTurkishCharacters(String responseString){
		String replaced = responseString;
		if(replaced.contains(mContext.getResources().getString(R.string.c))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.c), "c");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.g))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.g), "g");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.i))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.i), "i");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.o))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.o), "o");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.s))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.s), "s");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.u))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.u), "u");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.C))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.C), "C");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.G))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.G), "G");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.I))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.I), "I");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.O))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.O), "O");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.S))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.S), "S");
		}
		if(replaced.contains(mContext.getResources().getString(R.string.U))){
			replaced = replaced.replaceAll(mContext.getResources().getString(R.string.U), "U");
		}
		
		return replaced;
	}

	public class httpGetRequestTask extends AsyncTask<Request, Void, Object> {

		private ResponseListener listener;
		private Request request;
		
		@Override
		protected Object doInBackground(Request... params) {
			success = true;
			Object parsedResponse = null;
			Request request = (Request) params[0];
			listener = request.getListener();
			this.request = request;
			waitForNetwork();
			try {
				HttpResponse response = getResponse(request);
				String responseString = getResponseBodyString(response);
			//	responseString = filterTurkishCharacters(responseString);
				parsedResponse = parseResponse(request.getRequestID(), responseString);
			} catch (Exception e) {
				success = false;
				e.printStackTrace();
			}

			return parsedResponse;
		}

		@Override
		protected void onPostExecute(Object result) {
			progressDialog.dismiss();
			if(success){
				listener.onSuccess(result);
			}else{
				listener.onFailure("Internet baglantisinda bir sorun algilandi. Lutfen ayarlarinizi kontrol ediniz.");
			}

		}

		private Object parseResponse(int requestID, String response) {
			Gson gson = new Gson();
			switch (requestID) {
			case Commons.WEEKLY_FREE_CHAMPIONS_REQUEST:
				return gson.fromJson(response, WeeklyFreeChampionsResponse.class);
			case Commons.STATIC_DATA_WITH_ALT_IMAGES_REQUEST:
				return gson.fromJson(response, StaticDataWithAltImagesResponse.class);
			case Commons.CHAMPION_RP_IP_COSTS_REQUEST:
				return gson.fromJson(response, ChampionRpIpCostsResponse.class);
			case Commons.CHAMPION_OVERVIEW_REQUEST:
				return gson.fromJson(response, ChampionOverviewResponse.class);
			case Commons.CHAMPION_SPELLS_REQUEST:
				return gson.fromJson(response, ChampionSpellsResponse.class);
			case Commons.ALL_CHAMPIONS_REQUEST:
				return gson.fromJson(response, AllChampionsResponse.class);
			case Commons.CHAMPION_LEGEND_REQUEST:
				return gson.fromJson(response, ChampionLegendResponse.class);
			case Commons.CHAMPION_STRATEGY_REQUEST:
				return gson.fromJson(response, ChampionStrategyResponse.class);
			case Commons.RECOMMENDED_ITEMS_REQUEST:
				return gson.fromJson(response, RecommendedItemsResponse.class);
			case Commons.ALL_ITEMS_REQUEST:
				return gson.fromJson(response, AllItemsResponse.class);
			case Commons.ITEM_DETAIL_REQUEST:
				return gson.fromJson(response, ItemDetailResponse.class);
			case Commons.ALL_RUNES_REQUEST:
				return gson.fromJson(response, RuneResponse.class);
            case Commons.LIVE_CHANNELS_REQUEST:
                return gson.fromJson(response, LiveChannelsResponse.class);
            case Commons.CHAMPION_SKINS_REQUEST:
                return gson.fromJson(response, ChampionSkinsResponse.class);
            case Commons.SUMMONER_INFO_REQUEST:
                return gson.fromJson(response, SummonerInfoResponse.class);
            case Commons.MATCH_INFO_REQUEST:
                return gson.fromJson(response, MatchInfoResponse.class);
            case Commons.LEAGUE_INFO_REQUEST:
                return gson.fromJson(response, LeagueInfoResponse.class);
                case Commons.STATS_REQUEST:
                return gson.fromJson(response, StatsResponse.class);
			default:
				return null;
			}
		}

	}
	
	public static Dialog showLoading(Context context, String message) {

		if (progressDialog == null) {
			progressDialog = new Dialog(context, R.style.customDialogTheme);
			progressDialog.setContentView(R.layout.loading_view);
		}

		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);

		return progressDialog;
	}

    public static Dialog hideLoading(Context context, String message) {

        if (progressDialog == null) {
            progressDialog.hide();
        }

        return progressDialog;
    }

}
