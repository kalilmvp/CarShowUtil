package br.com.google.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConexaoUtil {
	
	private static final String TAG = "HTTP";
	
	public static final String connect(String url, String charset) {
		Log.d(TAG, ">>Http.doGet() - " + url);
		
		String dados = null;
		
		try {
			URL urlConnect = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection)urlConnect.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			httpConnection.connect();
			
			InputStream in = httpConnection.getInputStream();
			
			dados = IOUtils.getData(in, charset);
			
			Log.d(TAG, ">>Http.doGet() - " + dados);
			
			in.close();
			httpConnection.disconnect();
			
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} 
		return dados;
	}
	
	public static boolean isNetworkAvailable(Context context) {
		
		ConnectivityManager connectivityManager = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (connectivityManager == null) {
			return false;
		} else {
			NetworkInfo [] info = connectivityManager.getAllNetworkInfo();
			
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
//	@Override
//	protected String doInBackground(String... params) {
//		String url = params[0];
//		String charset = params[1];
//		
//		Log.d(TAG, ">>Http.doGet() - " + url);
//		
//		String dados = null;
//		
//		try {
//			URL urlConnect = new URL(url);
//			HttpURLConnection httpConnection = (HttpURLConnection)urlConnect.openConnection();
//			httpConnection.setRequestMethod("POST");
//			httpConnection.setDoOutput(true);
//			httpConnection.setDoInput(true);
//			httpConnection.connect();
//			
//			InputStream in = httpConnection.getInputStream();
//			
//			dados = IOUtils.getData(in, charset);
//			
//			Log.d(TAG, ">>Http.doGet() - " + dados);
//			
//			in.close();
//			httpConnection.disconnect();
//			
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		return dados;
//	}
}