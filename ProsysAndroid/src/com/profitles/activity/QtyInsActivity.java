package com.profitles.activity;



import java.util.Date;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.PdBiz;
import com.profitles.biz.QtyInsBiz;
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

public class QtyInsActivity extends AppFunActivity{

	private MyEditText actPart,actQty,actUm,actLot,actBin,actState,actLoc,actDesc;
	private MyReadBQ actScan;
	private QtyInsBiz biz;
	private ApplicationDB applicationDB;
	private String domain, site, userid,marking = "",date ,fnceffdate,rflot_type="";
	private boolean isTrue;
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_qtyinsp;
	}
	@Override
	protected void pageLoad() {
		biz = new QtyInsBiz();
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
		actPart     = (MyEditText) findViewById(R.id.etx_sendPart); // 零件
		actDesc 	= (MyEditText) findViewById(R.id.etx_sendPartDesc); // 零件描述
		actUm		= (MyEditText) findViewById(R.id.etx_sendUim);  // 单位
		actQty 		= (MyEditText) findViewById(R.id.etx_sendQty);  // 数量
		actLot      = (MyEditText) findViewById(R.id.etx_sendLot);  // 批次
		actState    = (MyEditText) findViewById(R.id.etx_state);  // 状态
		actLoc      = (MyEditText) findViewById(R.id.etx_sendLoc);  // 库位
		actBin      = (MyEditText) findViewById(R.id.etx_sendBin);  // 储位
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
				return biz.QtyInsScan(domain, site, actScan.getValStr(),marking="1");
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String,Object> mapRtn = wr.getDataToMap();
					String status = mapRtn.get("STATUS")+"";
					if(status.equals("1")){
						showConfirm(wr.getMessages(), scfListenPk2);
					}else{
						actPart.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_PART"))?"":mapRtn.get("RFLOT_PART").toString());
						actDesc.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_PART_DESC"))?"":mapRtn.get("RFLOT_PART_DESC").toString());
						actUm.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_UM"))?"":mapRtn.get("RFLOT_UM").toString());
						actLot.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_LOT"))?"":mapRtn.get("RFLOT_LOT").toString());
						actQty.setText(StringUtil.isEmpty(mapRtn.get("QTY"))?"":mapRtn.get("QTY").toString());
						actState.setText(StringUtil.isEmpty(mapRtn.get("TYPE"))?"":mapRtn.get("TYPE").toString());
						actLoc.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_LOC"))?"":mapRtn.get("RFLOT_LOC").toString());
						actBin.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_BIN"))?"":mapRtn.get("RFLOT_BIN").toString());
						rflot_type = StringUtil.isEmpty(mapRtn.get("RFLOT_TYPE"))?"":mapRtn.get("RFLOT_TYPE").toString();
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
			//确定(用户选择了继续)则记录异常操作日志中rfifo_log中
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return biz.QtyInsScan(domain, site, actScan.getValStr(),marking ="-1");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String,Object> mapRtn = wr.getDataToMap();
						actPart.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_PART"))?"":mapRtn.get("RFLOT_PART").toString());
						actDesc.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_PART_DESC"))?"":mapRtn.get("RFLOT_PART_DESC").toString());
						actUm.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_UM"))?"":mapRtn.get("RFLOT_UM").toString());
						actLot.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_LOT"))?"":mapRtn.get("RFLOT_LOT").toString());
						actQty.setText(StringUtil.isEmpty(mapRtn.get("QTY"))?"":mapRtn.get("QTY").toString());
						actState.setText(StringUtil.isEmpty(mapRtn.get("TYPE"))?"":mapRtn.get("TYPE").toString());
						actLoc.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_LOC"))?"":mapRtn.get("RFLOT_LOC").toString());
						actBin.setText(StringUtil.isEmpty(mapRtn.get("RFLOT_BIN"))?"":mapRtn.get("RFLOT_BIN").toString());
						rflot_type = StringUtil.isEmpty(mapRtn.get("RFLOT_TYPE"))?"":mapRtn.get("RFLOT_TYPE").toString();
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
		actLot.setText("");
		actQty.setText("");
		actState.setText("");
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
		return biz.QtyInsSubmit(domain, site, userid, actUm.getValStr(), actQty.getValStr(), actScan.getValStr(), actPart.getValStr(), site, actLot.getValStr(), actLoc.getValStr(), actBin.getValStr(), date, fnceffdate,rflot_type);
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
