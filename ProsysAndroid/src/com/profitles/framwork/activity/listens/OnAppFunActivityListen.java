package com.profitles.framwork.activity.listens;

import android.view.View;

import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.listens.base.OnBaseListener;

public interface OnAppFunActivityListen extends OnBaseListener{

	public Object OnBtnClick(ButtonType btnType, View v);
	public Object OnBtnSubClick(ButtonType btnType, View v);
	public Object OnBtnSaveClick(ButtonType btnType, View v);
	public Object OnBtnEditClick(ButtonType btnType, View v);
	public Object OnBtnHelpClick(ButtonType btnType, View v);
	public Object OnBtnCanlClick(ButtonType btnType, View v);
	public Object OnBtnClsClick(ButtonType btnType, View v);
	public Object OnBtnRtnClick(ButtonType btnType, View v);
	public Object OnBtnPntClick(ButtonType btnType, View v);
	public Object OnBtnSerClick(ButtonType btnType, View v);
	
	public void OnBtnCallBack(Object data);
	public void OnBtnSubCallBack(Object data);
	public void OnBtnSaveCallBack(Object data);
	public void OnBtnEditCallBack(Object data);
	public void OnBtnHelpCallBack(Object data);
	public void OnBtnCanlCallBack(Object data);
	public void OnBtnClsCallBack(Object data);
	public void OnBtnRtnCallBack(Object data);
	public void OnBtnPntCallBack(Object data);
	public void OnBtnSerCallBack(Object data);


}
