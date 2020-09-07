package com.profitles.framwork.activity.base.rdb;


public interface IRunDataBaseListens {

	public boolean onValidata();

	public Object onGetData();
	
	public void onCallBrack(Object data);
	
}
