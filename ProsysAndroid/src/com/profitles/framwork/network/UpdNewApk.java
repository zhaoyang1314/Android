package com.profitles.framwork.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.profitles.framwork.fileutil.SDFileUtil;
import com.profitles.framwork.network.interfaces.OnDownFileLicense;

public class UpdNewApk {
	
	private Context context;
	
	public UpdNewApk(Context ctxt){
		context = ctxt;
	}
	
	public void updBegin(final String path){
		final NetworkFile ntfFile = new NetworkFile(context);
		final Handler bh = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				String val = (String) msg.obj;
				new SDFileUtil(context).installApk(val);
		    }
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				ntfFile.setOnDownFileLicense(new OnDownFileLicense(){
					@Override
					public void onDownload() {
//						mpgLoad.setLoaded(false);
//						mpgLoad.setLoading(true);
					}

					@Override
					public void onDownloading(float progress) {
//						sendHandlerMsg(progress+"");
					}

					@Override
					public void onDownloaded(float size, String path) {
						Message msgInit = new Message();
						msgInit.obj = path;
						bh.sendMessage(msgInit);
					}
				});
				ntfFile.downFile(path);
			}
		}).start();
	}
}
