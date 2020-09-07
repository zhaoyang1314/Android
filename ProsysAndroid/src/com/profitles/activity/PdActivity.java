package com.profitles.activity;



import java.util.Date;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.LastQcBiz;
import com.profitles.biz.LastWgBiz;
import com.profitles.biz.PdBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class PdActivity extends AppFunActivity{

	private MyEditText actPart,actQty;
	private MyTextView actDesc,actUm,actQoh,actLot;
	private MyReadBQ actLocbin,actScan;
	private PdBiz biz;
	private ApplicationDB applicationDB;
	private String domain, site, userid, vend,loc,bin,date ,fnceffdate;
	private boolean isTrue;
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pd;
	}
	@Override
	protected void pageLoad() {
		biz = new PdBiz();
		actScan   	= (MyReadBQ)   findViewById(R.id.ext_pdBar);
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
		actLocbin	= (MyReadBQ)   findViewById(R.id.ext_pdLocbin);
		actLocbin.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actLocbin.getValStr().equals("")){
					getLocBin();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actPart     = (MyEditText) findViewById(R.id.ext_pdParts);
		actDesc 	= (MyTextView) findViewById(R.id.txv_pdPartname);
		actUm		= (MyTextView) findViewById(R.id.txv_pdUm);
		actQoh 		= (MyTextView) findViewById(R.id.txv_pdQoh);
		actQty 		= (MyEditText) findViewById(R.id.ext_pdQty);
		actLot      = (MyTextView) findViewById(R.id.txv_pdLot);
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
				return biz.PdScan(domain, site, actScan.getValStr(),actLocbin.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String,Object> map = wr.getDataToMap();
					actPart.setText(StringUtil.isEmpty(map.get("RFLOT_PART"))?"":map.get("RFLOT_PART").toString());
					actDesc.setText(StringUtil.isEmpty(map.get("RFLOT_PART_DESC"))?"":map.get("RFLOT_PART_DESC").toString());
					actUm.setText(StringUtil.isEmpty(map.get("RFLOT_UM"))?"":map.get("RFLOT_UM").toString());
					actQoh.setText(StringUtil.isEmpty(map.get("RFLOT_QOH"))?"":map.get("RFLOT_QOH").toString());
					actLot.setText(StringUtil.isEmpty(map.get("RFLOT_LOT"))?"":map.get("RFLOT_LOT").toString());
					float qty = 0f;
					if(StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY"))>0){
						qty = StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY"));
					}else{
						qty = StringUtil.parseFloat(map.get("RFLOT_MULT_QTY"));
					}
					actQty.setText(qty+"");
					vend = StringUtil.isEmpty(map.get("RFLOT_VEND"))?"":map.get("RFLOT_VEND").toString();
					Date d = new Date();
					date = d+"";
					//getFocues(actLocbin, true);
				}else{
					isTrue = false;
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	/**
	 * 扫码仓储
	 */
	private void getLocBin(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.PdLocBin1(domain, site, actLocbin.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map = wr.getDataToMap();
					loc = map.get("LOC")+"";
					bin = map.get("BIN")+"";
					isTrue = true;
				}else{
					isTrue = false;
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
		if(!isTrue){
			return false;
		}
		return true;
	}
	
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return biz.Pdsubmit(domain, site, userid, actUm.getValStr(), actQty.getValStr(), actScan.getValStr(),
				actPart.getValStr(), vend, actLot.getValStr(), loc, bin, date, fnceffdate);
	}
	
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			//actLocbin.setText("");
			actScan.setText("");
			actPart.setText("");
			actDesc.setText("");
			actUm.setText("");
			actQoh.setText("");
			actLot.setText("");
			actQty.setText("");
			vend = "";
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
