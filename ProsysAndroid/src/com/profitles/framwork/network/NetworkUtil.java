package com.profitles.framwork.network;

import java.net.InetAddress;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.profitles.framwork.network.base.NetworkBase;
import com.profitles.framwork.util.StringUtil;

public class NetworkUtil extends NetworkBase {
	public NetworkUtil(){}
	
	public NetworkUtil(Context context){
		super(context);
	}
	
	/** 
	* 获取网络状态，wifi,wap,2g,3g. 
	* 
	* @param context 上下文 
	* @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},          *{@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI} 
	*/  
	public NetWorkType getNetWorkType() {
		NetWorkType mNetWorkType = null;
		ConnectivityManager manager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				mNetWorkType = NetWorkType.NETWORKTYPE_WIFI;
			} else if (type.equalsIgnoreCase("MOBILE")) {
				String proxyHost = android.net.Proxy.getDefaultHost();

				mNetWorkType = StringUtil.isEmpty(proxyHost) ? (isFastMobileNetwork(getContext()) ? NetWorkType.NETWORKTYPE_3G
						: NetWorkType.NETWORKTYPE_2G)
						: NetWorkType.NETWORKTYPE_WAP;
			}
		} else {
			mNetWorkType = NetWorkType.NETWORKTYPE_INVALID;
		}
		return mNetWorkType;
	}
	
	private boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (telephonyManager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		default:
			return false;
		}
	}


	public enum NetWorkType{
		NETWORKTYPE_INVALID,	/** 没有网络 */  
		NETWORKTYPE_WAP,		/** wap网络 */  
		NETWORKTYPE_2G,			/** 2G网络 */  
		NETWORKTYPE_3G,			/** 3G和3G以上网络，或统称为快速网络 */  
		NETWORKTYPE_WIFI		/** wifi网络 */  
	}
	
	public static String getMacFromWifi(Context context) {
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String mResult = wifiInfo.getMacAddress();
		return mResult;
	}
}
