package com.profitles.activity;



import java.util.Date;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.PdBiz;
import com.profitles.biz.QtyInsBiz;
import com.profitles.biz.QuaInsBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class QuaInsActivity extends AppFunActivity{

	private MyEditText actPart,actQty,actUm,actLot,actBin,actState,actLoc,actDesc,actVersion,actsendType,actsendStats,actsendOrga,actsendLabel
	,actsendLen,actxnum,actsenBnum,actsendOrgaAdd;
	//private MyEditText actPart,actQty,actLot,actBin,actState,actLoc,actDesc;
	private MyReadBQ actScan;
	private QuaInsBiz biz;
	private ApplicationDB applicationDB;
	private String domain, site, userid,marking = "",table="",date ,fnceffdate,rflot_type="";
	private boolean isTrue;
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_quain;
	}
	@Override
	protected void pageLoad() {
		biz = new QuaInsBiz();
		actScan   	= (MyReadBQ)   findViewById(R.id.etx_sendNbr);
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
		actPart     = (MyEditText) findViewById(R.id.etx_sendPart);
		actDesc 	= (MyEditText) findViewById(R.id.etx_sendPartDesc);
		//actQty 		= (MyEditText) findViewById(R.id.etx_sendQty);  
		actLot      = (MyEditText) findViewById(R.id.etx_sendLot); 
		actUm       = (MyEditText) findViewById(R.id.etx_sendUm); 
		actState    = (MyEditText) findViewById(R.id.etx_state);  
		actLoc      = (MyEditText) findViewById(R.id.etx_sendLoc); 
		actBin      = (MyEditText) findViewById(R.id.etx_sendBin);  
		actVersion  = (MyEditText) findViewById(R.id.etx_sendVersion); 
		actsendType = (MyEditText) findViewById(R.id.etx_sendType);	
		actsendStats= (MyEditText) findViewById(R.id.etx_sendStats);
		actxnum		= (MyEditText) findViewById(R.id.etx_senXnum);
		actsenBnum  = (MyEditText) findViewById(R.id.etx_senBnum);
		actsendOrga = (MyEditText) findViewById(R.id.etx_sendOrga);
		actsendOrgaAdd = (MyEditText) findViewById(R.id.etx_sendOrgaAdd);
		actsendLabel= (MyEditText) findViewById(R.id.etx_sendLabel);
		actsendLen  = (MyEditText) findViewById(R.id.etx_sendLen);
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid		= ApplicationDB.user.getUserId();
		fnceffdate  = ApplicationDB.user.getUserDate();
		
	}
	
	/**
	 * 扫描标签
	 */
	private void getScan(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.QuaInsScan(domain, site, actScan.getValStr(),marking="-1");
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String,Object> mapRtn = wr.getDataToMap();
					String status = mapRtn.get("STATUS")+"";
					String table = mapRtn.get("TABLE")+"";
					if("0.0".equals(status)){
						showConfirm(wr.getMessages(), scfListenPk2);
					}else if("OQC".equals(table)){
						showConfirm(wr.getMessages(), scfListenPk3);
					}
					else{
						actsendType.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_TYPE"))?"":mapRtn.get("RFLOT_TYPE").toString());
						actsendStats.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_SCAN_STATUS"))?"":mapRtn.get("RFLOT_SCAN_STATUS").toString());
						actxnum.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_SCAN_STATUS_ZH"))?"":mapRtn.get("RFLOT_SCAN_STATUS_ZH").toString());
						actsenBnum.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_NUM_LBL"))?"":mapRtn.get("RFLOT_NUM_LBL").toString());
						actPart.setText(StringUtil.isEmpty(mapRtn.get("PART"))?"":mapRtn.get("PART").toString());
						actVersion.setText(StringUtil.isEmpty(mapRtn.get("VERSION"))?"":mapRtn.get("VERSION").toString());
						actDesc.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_PART_DESC"))?"":mapRtn.get("RFLOT_PART_DESC").toString());
						actLoc.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_LOC"))?"":mapRtn.get("RFLOT_LOC").toString());
						actBin.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_BIN"))?"":mapRtn.get("RFLOT_BIN").toString());
						actLot.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_LOT"))?"":mapRtn.get("RFLOT_LOT").toString());
						actsendOrga.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_VEND"))?"":mapRtn.get("RFLOT_VEND").toString());
						actsendOrgaAdd.setText(StringUtil.isEmpty(mapRtn.get("XSAD_NAME"))?"":mapRtn.get("XSAD_NAME").toString());
						actsendLen.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_DIRECTION"))?"":mapRtn.get("RFLOT_DIRECTION").toString());
						//actQty.setText(StringUtil.isEmpty(mapRtn.get("QTY").toString())?"":mapRtn.get("QTY").toString());
						actUm.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_UM").toString())?"":mapRtn.get("RFLOT_UM").toString());
						actsendLabel.setText(StringUtil.isEmpty(mapRtn.get("NUMBERS"))?"":mapRtn.get("NUMBERS").toString());
					}
				}else{
					isTrue = false;
					showErrorMsg(wr.getMessages());
					actScan.setText("");
					getFocues(actScan, true);
				}
			}
		});
	}	
	
	private OnShowConfirmListen scfListenPk2=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return biz.QuaInsScan(domain, site, actScan.getValStr(),marking="1");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String,Object> mapRtn = wr.getDataToMap();
						actsendType.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_TYPE"))?"":mapRtn.get("RFLOT_TYPE").toString());
						actsendStats.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_SCAN_STATUS"))?"":mapRtn.get("RFLOT_SCAN_STATUS").toString());
						actxnum.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_SCAN_STATUS_ZH"))?"":mapRtn.get("RFLOT_SCAN_STATUS_ZH").toString());
						actsenBnum.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_NUM_LBL"))?"":mapRtn.get("RFLOT_NUM_LBL").toString());
						actPart.setText(StringUtil.isEmpty(mapRtn.get("PART"))?"":mapRtn.get("PART").toString());
						actVersion.setText(StringUtil.isEmpty(mapRtn.get("VERSION"))?"":mapRtn.get("VERSION").toString());
						actDesc.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_PART_DESC"))?"":mapRtn.get("RFLOT_PART_DESC").toString());
						actLoc.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_LOC"))?"":mapRtn.get("RFLOT_LOC").toString());
						actBin.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_BIN"))?"":mapRtn.get("RFLOT_BIN").toString());
						actLot.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_LOT"))?"":mapRtn.get("RFLOT_LOT").toString());
						actsendOrga.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_VEND"))?"":mapRtn.get("RFLOT_VEND").toString());
						actsendLen.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_DIRECTION"))?"":mapRtn.get("RFLOT_DIRECTION").toString());
						actsendOrgaAdd.setText(StringUtil.isEmpty(mapRtn.get("XSAD_NAME"))?"":mapRtn.get("XSAD_NAME").toString());
						//actQty.setText(StringUtil.isEmpty(mapRtn.get("QTY").toString())?"":mapRtn.get("QTY").toString());
						actsendLabel.setText(StringUtil.isEmpty(mapRtn.get("NUMBERS"))?"":mapRtn.get("NUMBERS").toString());
						actUm.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_UM").toString())?"":mapRtn.get("RFLOT_UM").toString());
					}else{
						isTrue = false;
						showErrorMsg(wr.getMessages());
						actScan.setText("");
						getFocues(actScan, true);
					}
				}
			});	
			
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			//取消则回到该栏位位置选中该栏位值
			clearInfo();
		}
	};
	
	
	private OnShowConfirmListen scfListenPk3=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return biz.QuaInsDel(domain, site, actScan.getValStr(),table="OQC");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						showSuccessMsg(wr.getMessages());
						actScan.setText("");
					}else{
						isTrue = false;
						showErrorMsg(wr.getMessages());
						actScan.setText("");
						getFocues(actScan, true);
					}
				}
			});	
			
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			//取消则回到该栏位位置选中该栏位值
			clearInfo();
		}
	};
	
	private void clearInfo(){
		actPart.setText("");
		actDesc.setText("");
		actUm.setText("");
		actVersion.setText("");
		actsendOrgaAdd.setText("");
		actxnum.setText("");
		actsenBnum.setText("");
		actsendOrga.setText("");
		actsendType.setText("");
		actsendStats.setText("");
		actsendLen.setText("");
		actsendLabel.setText("");
		actLot.setText("");
		//actQty.setText("");
		actLoc.setText("");
		actBin.setText("");
		actScan.setText("");
		rflot_type = "";
		getFocues(actScan, true);
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
		if(StringUtil.isEmpty(actScan.getValStr())){
			showErrorMsg("请扫描条码后操作");
			return false;
		}
		return true;
	}
	
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return biz.QuaInsSubmit(domain, site, userid, actUm.getValStr(), actsendLabel.getValStr(), actScan.getValStr(), actPart.getValStr(), actsendOrga.getValStr(), actLot.getValStr(), actLoc.getValStr(), actBin.getValStr(), date, fnceffdate,actsendStats.getValStr());
	}
	
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			clearInfo();
			date = "";
			getFocues(actScan, true);
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
