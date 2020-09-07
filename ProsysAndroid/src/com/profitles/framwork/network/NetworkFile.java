package com.profitles.framwork.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;

import com.profitles.framwork.fileutil.FileUtilBase;
import com.profitles.framwork.network.base.NetworkBase;
import com.profitles.framwork.network.interfaces.OnDownFileLicense;
import com.profitles.framwork.util.Constants;

public class NetworkFile extends NetworkBase {
	
	private OnDownFileLicense onDownFileLicense;
	
	public NetworkFile() {
		super();
	}

	public NetworkFile(Context context) {
		super(context);
	}

	/**
	 * 根据地址获取文件流程
	 */
	public String getXmlStringFile(String path, String coding){
		StringBuffer sbf = new StringBuffer();
		FileUtilBase fUtil = new FileUtilBase();
		try {
			BufferedReader bReader = fUtil.getFileReStream(path, coding);
			String strline = null;
			while ((strline = bReader.readLine()) != null) {
				sbf.append(strline);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sbf.toString();
	}
	
	/**
	 * 获取服务器XML的InputStream流对象
	 * @param path
	 * @param coding
	 * @return
	 */
	public InputStream getXmlInpISMFile(String path){
		try {
			HttpURLConnection huConn = getHttpURLConnection(path, "GET");	//这里我们使用Get获取网络更新文件
			if(huConn.getResponseCode() == 200){
				return huConn.getInputStream();
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 下载文件 当前下载函数没有启用线程，因此当调用是请加入线程
	 * @param urlPath
	 * @return
	 */
	public boolean downFile(final String urlPath){
		try {
			HttpURLConnection huConn = getHttpURLConnection(urlPath, "GET");
			InputStream is = huConn.getInputStream();
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File isEx = new File(Constants.App_Path);
				if(!isEx.exists()) isEx.mkdirs();
				File file = new File(Constants.App_Path+"/", Constants.App_Save_Name);
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[4*1024];
				int ch = -1, count = 0, length = huConn.getContentLength();
				if(getOnDownFileLicense() != null)
					getOnDownFileLicense().onDownload();	//开始下载事件
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
					count += ch;
					if(getOnDownFileLicense() != null)
						getOnDownFileLicense().onDownloading((float)count / length);	//下载中事件
				}
				if(getOnDownFileLicense() != null)
					getOnDownFileLicense().onDownloaded(length, file.getPath());	//下载结束事件
			}
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
			if(is != null) is.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}


	public OnDownFileLicense getOnDownFileLicense() {
		return onDownFileLicense;
	}

	public void setOnDownFileLicense(OnDownFileLicense onDownFileLicense) {
		this.onDownFileLicense = onDownFileLicense;
	}
}
