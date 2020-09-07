package com.profitles.framwork.network.base;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.profitles.framwork.BaseObject;

public class NetworkBase extends BaseObject {
	
	public NetworkBase(){}

	public NetworkBase(Context context){
		setContext(context);
	}
	/** 上下文对象 **/
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * 检测网络是否通畅
	 * @return
	 */
	public boolean isNetworkOK() {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.v("error", e.toString());
		}
		return false;
	}
	
	public HttpURLConnection getHttpURLConnection(String path){
		try {
			return getHttpURLConnection(new URL(path));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public HttpURLConnection getHttpURLConnection(URL url){
		try {
			HttpURLConnection huConn = (HttpURLConnection)url.openConnection();
			huConn.setConnectTimeout(6 * 1000);
			return huConn;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public HttpURLConnection getHttpURLConnection(URL url, String mode){
		HttpURLConnection huConn = getHttpURLConnection(url);
		try {
			huConn.setRequestMethod(mode);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		return huConn;
	}

	public HttpURLConnection getHttpURLConnection(String path, String mode){
		HttpURLConnection huConn = getHttpURLConnection(path);
		try {
			huConn.setRequestMethod(mode);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		return huConn;
	}
}
