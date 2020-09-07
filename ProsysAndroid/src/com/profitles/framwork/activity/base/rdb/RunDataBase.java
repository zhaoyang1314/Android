package com.profitles.framwork.activity.base.rdb;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

public class RunDataBase extends Thread {
	
	private IRunDataBaseListens rdbl;
	private ProgressDialog pd;
	
	public RunDataBase(IRunDataBaseListens _rdbl, ProgressDialog pd){
		this.rdbl = _rdbl;
		this.pd = pd;
	}
	
	private Handler handler = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        pd.hide();
	        pd.cancel();
	        rdbl.onCallBrack(msg.obj);
	    }
	};
	
	@Override
	public void run() {
		Message msg = new Message();
		try {
			msg.obj = rdbl.onGetData();
			msg.arg1 = 0;
		} catch (Exception e) {
			e.printStackTrace();
			msg.obj = e.getMessage();
			msg.arg1 = 1;
		}
		handler.sendMessage(msg);
	}
}
