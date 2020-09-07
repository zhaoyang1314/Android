package com.profitles.activity;



import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.profitles.biz.LastQcBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;

public class LastQcActivity extends AppFunActivity{

	private MyEditText actPart,actDesc,actProList,actShift,actLot,actWorkShop,actLine;
	private MyReadBQ actLastBar,actScan;
	private LastQcBiz lBiz;
	private ApplicationDB applicationDB;
	private String domain, site, userid, ukey ;
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_lastqc;
	}
	@Override
	protected void pageLoad() {
		lBiz = new LastQcBiz();
		actLastBar	= (MyReadBQ)   findViewById(R.id.ext_lastBar);
		actLastBar.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actLastBar.getValStr().equals("")){
					getNbr();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actProList	= (MyEditText) findViewById(R.id.ext_lastProList);
		actPart     = (MyEditText) findViewById(R.id.ext_lastParts);
		actWorkShop = (MyEditText) findViewById(R.id.ext_lastWorkShop);
		actDesc 	= (MyEditText) findViewById(R.id.etx_lastDesc);
		actShift	= (MyEditText) findViewById(R.id.ext_lastShift);
		actLot      = (MyEditText) findViewById(R.id.ext_lastLot);
		actLine     = (MyEditText) findViewById(R.id.ext_lastLine);
		actScan   	= (MyReadBQ)   findViewById(R.id.ext_lastSacn);
		actScan.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actScan.getValStr().equals("")){
					getScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid		= ApplicationDB.user.getUserId();
		
		if(applicationDB.WoC != null){
			String sample = applicationDB.WoC.getString("PFTWOC_SAMPLE","0");
			String noFull = applicationDB.WoC.getString("PFTWOC_NO_FULL","0");
		}
	}
	
	/**
	 * 扫描加工单
	 */
	private void getNbr(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return lBiz.LastQcLastBar(domain, site, actLastBar.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String,Object> map = wr.getDataToMap();
					actProList.setText(map.get("NO")+"");
					actPart.setText(map.get("PART")+"");
					actDesc.setText(map.get("DESC")+"");
					actWorkShop.setText(map.get("WORKSHOP")+"");
					actShift.setText(map.get("SHIFT")+"");
					actLot.setText(map.get("LOT")+"");
					actLine.setText(map.get("LINE")+"");
					ukey = map.get("UKEY")+"";
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	/**
	 * 扫码标签
	 */
	private void getScan(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return lBiz.LastQcScan(domain, site, actScan.getValStr(),actPart.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(!wr.isSuccess()){
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	// 页面添加按钮
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[] { ButtonType.Submit};
	}
	
	/**
	 * 提交
	 */
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		return true;
	}
	
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return lBiz.LastQcsubmit(domain, site, userid, actDesc.getValStr(), ukey);
	}
	
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			actProList.setText("");
			actPart.setText("");
			actDesc.setText("");
			actWorkShop.setText("");
			actShift.setText("");
			actLot.setText("");
			actLine.setText("");
			actLastBar.setText("");
			ukey = "";
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
		}
	}
	
	
	@Override
	protected void unLockNbr() {
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
