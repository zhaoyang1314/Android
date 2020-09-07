package com.profitles.framwork.appdb;

import android.app.Activity;
import android.app.ProgressDialog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


import com.profitles.biz.LoginBiz;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.base.rdb.RunDataBase;
import com.profitles.framwork.params.ParamsBaseMgr;
import com.profitles.framwork.webservice.bean.UserBean;

public class ApplicationDB {

	public static UserBean user;
	public static ParamsBaseMgr Ctrl;
	//public static ParamsBaseMgr Ps;   //获取采购策略
	public static ParamsBaseMgr WoC;   //获取生产管控策略
	public static ParamsBaseMgr SoC;   //获取销售控策略
	public static ParamsBaseMgr LoclLoop;
	public static String MenuName;
	
	public static Activity _qbReqParamTemp;
	public static SSLSocketFactory socketFactory;
	
	public static void initDB(Activity a){
		if (socketFactory == null) {  	//初始化证书
            try {  
                socketFactory = getSSlFactory(a);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
	}
	public static void ReloadDB(Activity a){
		new RunDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				LoginBiz lbiz = new LoginBiz();
				//Load AppactionDB Begin
				lbiz.initSysCtrl(user.getUserDmain(), user.getUserSite());
				//lbiz.initPoStrategy(user.getUserDmain(), user.getUserSite());   //获取采购策略
				lbiz.initWocCtrl(user.getUserDmain(), user.getUserSite());   //获取采购策略
				lbiz.initSocCtrl( user.getUserDmain(), user.getUserSite());
				//Load AppactionDB End
				lbiz.initLoclLoop(user.getUserLoc(), user.getUserDmain(), user.getUserSite());
				return null;
			}
			@Override
			public void onCallBrack(Object data) {
			}
		}, ProgressDialog.show(a, null, "提交中，请稍后...", true))
		.start();
	}
	
	public static SSLSocketFactory getSSlFactory(Activity context) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream caInput = new BufferedInputStream(context.getAssets().open("pftkey.cer"));// 把证书打包在asset文件夹中
			Certificate ca;
			try {
				ca = cf.generateCertificate(caInput);
			} finally {
				caInput.close();
			}

			// Create a KeyStore containing our trusted CAs
			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);

			// Create a TrustManager that trusts the CAs in our KeyStore
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(tmfAlgorithm);
			tmf.init(keyStore);

			// Create an SSLContext that uses our TrustManager
			SSLContext s = SSLContext.getInstance("TLSv1", "AndroidOpenSSL");
			s.init(null, tmf.getTrustManagers(), null);

			return s.getSocketFactory();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		return null;
	}
}
