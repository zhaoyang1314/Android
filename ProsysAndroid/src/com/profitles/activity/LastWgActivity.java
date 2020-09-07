package com.profitles.activity;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.LastQcBiz;
import com.profitles.biz.LastWgBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

public class LastWgActivity extends AppFunActivity{

	private MyEditText actPart,actDesc,actProList,actShift,actLot,actWorkShop,actLine;
	private MyReadBQ actLastBar,actScan;
	private LastWgBiz lBiz;
	private ApplicationDB applicationDB;
	private String domain, site, userid, ukey, scanbox = "";
	private boolean isTrue;
	private Map<String,Object> hmap = new HashMap<String, Object>();
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_lastqwg;
	}
	@Override
	protected void pageLoad() {
		lBiz = new LastWgBiz();
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
		actScan   	= (MyReadBQ)   findViewById(R.id.ext_lastscanBox);
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
			String lab = applicationDB.WoC.getString("PFTWOC_LAB_POLICY","0");
			if(lab.equals("1")){
				actScan.setVisibility(View.VISIBLE);
			}else{
				actScan.setVisibility(View.GONE);
			}
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
				return lBiz.LastWgLastBar(domain, site, actLastBar.getValStr(),userid);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String,Object> map = wr.getDataToMap();
					if(StringUtil.isEmpty(map.get("RFQCLOT_F_PASS"))){
						hmap = map;
						showConfirm("首件品质未放行是否继续操作?", lastwgInfor);
					}else{
						actProList.setText(map.get("NO")+"");
						actPart.setText(map.get("PART")+"");
						actDesc.setText(map.get("DESC")+"");
						actWorkShop.setText(map.get("WORKSHOP")+"");
						actShift.setText(map.get("SHIFT")+"");
						actLot.setText(map.get("LOT")+"");
						actLine.setText(map.get("LINE")+"");
						ukey = map.get("UKEY")+"";
						isTrue = true;
					}
				}else{
					isTrue = false;
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	private OnShowConfirmListen lastwgInfor=new OnShowConfirmListen() {   
		@Override
		public void onConfirmClick() {
			if(!StringUtil.isEmpty(hmap)){
				actProList.setText(hmap.get("NO")+"");
				actPart.setText(hmap.get("PART")+"");
				actDesc.setText(hmap.get("DESC")+"");
				actWorkShop.setText(hmap.get("WORKSHOP")+"");
				actShift.setText(hmap.get("SHIFT")+"");
				actLot.setText(hmap.get("LOT")+"");
				actLine.setText(hmap.get("LINE")+"");
				ukey = hmap.get("UKEY")+"";
				isTrue = true;
			}
		}
		
		@Override
		public void onCancelClick() {
			
		}
	};	
	
	
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
				return lBiz.LastWgScan(domain, site, actScan.getValStr(),actPart.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(!wr.isSuccess()){
					isTrue = false;
					showErrorMsg(wr.getMessages());
				}else{
					isTrue = true;
					Map<String, Object> map = wr.getDataToMap();
					scanbox = StringUtil.isEmpty(map.get("SCANBOX"))?"":map.get("SCANBOX")+"";
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
		if(!isTrue){
			return false;
		}
		return true;
	}
	
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return lBiz.LastWgsubmit(domain, site, userid, ukey,scanbox);
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
			actScan.setText("");
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
