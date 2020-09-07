package com.profitles.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.WostQcBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyLinearLayout;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.util.StringUtil;

public class WostQcActivity extends AppFunActivity {

	private WostQcBiz wostQcbiz;
	private MyEditText  etx_wostQcWoNbr,etx_wostQcShift,etx_wostQcPart,
			etx_wostQcPartDesc,etx_wostQcLot,etx_wostQcWkctr,
			etx_wostQcLine,etx_wostQcWoDate;
	private MyReadBQ  etx_wostQcScan,etx_wostQcSn;
	private ApplicationDB applicationDB;
	private String cPart = "",uKey = "",lbs = "",shift = "";
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_wostqc;
	}

	@Override
	protected void pageLoad() {
		wostQcbiz=new WostQcBiz();
		etx_wostQcScan = (MyReadBQ) findViewById(R.id.etx_wostQcScan);
		etx_wostQcSn = (MyReadBQ) findViewById(R.id.etx_wostQcSn);
		
		etx_wostQcWoDate = (MyEditText) findViewById(R.id.etx_wostQcWoDate);
		etx_wostQcWoNbr = (MyEditText) findViewById(R.id.etx_wostQcWoNbr);
		etx_wostQcShift = (MyEditText) findViewById(R.id.etx_wostQcShift);
		etx_wostQcPart = (MyEditText) findViewById(R.id.etx_wostQcPart);
		etx_wostQcPartDesc = (MyEditText) findViewById(R.id.etx_wostQcPartDesc);
		etx_wostQcLot = (MyEditText) findViewById(R.id.etx_wostQcLot);
		etx_wostQcWkctr = (MyEditText) findViewById(R.id.etx_wostQcWkctr);
		etx_wostQcLine = (MyEditText) findViewById(R.id.etx_wostQcLine);
	
		etx_wostQcScan.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_wostQcScan.getValStr() && !"".equals( etx_wostQcScan.getValStr().toString().trim())){
					checkwostQcScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		etx_wostQcSn.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_wostQcScan.getValStr() && !"".equals( etx_wostQcSn.getValStr().toString().trim()) 
						&& !"".equals( etx_wostQcPart.getValStr().toString().trim())){
					checkwostQcSn();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});	
	} 
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_PftporcNbr,etx_PftporcBar);
	}

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		if(etx_wostQcScan.getId()==id){
			runClickFun();
			if(null != etx_wostQcScan.getValStr() && !"".equals( etx_wostQcScan.getValStr().toString().trim())){
				//checkwostQcScan();
				runClickFun();
			}else{
				istrue = true;
			}
		}
		if(etx_wostQcSn.getId()==id){
			runClickFun();
			if(null != etx_wostQcScan.getValStr() && !"".equals( etx_wostQcSn.getValStr().toString().trim()) 
					&& !"".equals( etx_wostQcPart.getValStr().toString().trim())){
				checkwostQcSn();
				runClickFun();
			}else{
				istrue = true;
			}
		}
		
		return istrue;
	}
	
	//处理条码解析
	private void checkwostQcScan() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return wostQcbiz.wostQcScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_wostQcScan.getValStr().toString(),applicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					etx_wostQcWoNbr.setText(map.get("NO")+"");
					etx_wostQcShift.setText(map.get("SHIFT")+"");
					shift = map.get("ID")+"";
					etx_wostQcPart.setText(map.get("PART")+"");
					etx_wostQcPartDesc.setText(map.get("DESC")+"");
					etx_wostQcLot.setText(map.get("LOT")+"");
					etx_wostQcWkctr.setText(map.get("WORKSHOP")+"");
					etx_wostQcLine.setText(map.get("LINE")+"");
					cPart = map.get("CUST_PART")+"";
					uKey = map.get("UKEY")+"";
					etx_wostQcWoDate.setText(map.get("DATE")+"");
					lbs = map.get("LBS")+"";
					getFocues(etx_wostQcSn, true);
					runClickFun();
					istrue=true;
				}else{
					etx_wostQcWoNbr.reValue();
					etx_wostQcShift.reValue();
					shift = "";
					etx_wostQcPart.reValue();
					etx_wostQcPartDesc.reValue();
					etx_wostQcLot.reValue();
					etx_wostQcWkctr.reValue();
					etx_wostQcLine.reValue();
					cPart = "";
					uKey = "";
					lbs = "";
					etx_wostQcWoDate.reValue();
					showErrorMsg(wr.getMessages());
					getFocues(etx_wostQcScan, true);
					istrue = false;
				}
			}
		});
	}
	
	//处理条码解析
	private void checkwostQcSn() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return wostQcbiz.wostQcSn(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_wostQcPart.getValStr().toString(),etx_wostQcSn.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					getFocues(etx_wostQcSn, true);
					runClickFun();
					istrue=true;
				}else{
					showErrorMsg(wr.getMessages());
					etx_wostQcSn.reValue();
					getFocues(etx_wostQcSn, true);
					istrue = false;
				}
			}
		});
	}
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit};
	}	

	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals(etx_wostQcScan.getValStr().toString().trim()) && !"".equals(etx_wostQcPart.getValStr().toString().trim())
				&& !"".equals(etx_wostQcWkctr.getValStr().toString().trim()) && !"".equals(etx_wostQcLine.getValStr().toString().trim())){
			if(!"S".equals(lbs)){
				
			}else{
				if(!"".equals(etx_wostQcSn.getValStr().toString().trim())){
					
				}else{
					showErrorMsg("扫标签不能为空");
					istrue=false;	
				}
			}
		}else{
			showErrorMsg("请先扫描正确的首件标签");
			istrue=false;
		}	
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return wostQcbiz.wostQcSub(applicationDB.user.getUserDmain(), 
									applicationDB.user.getUserSite(),
									applicationDB.user.getUserId(),
									etx_wostQcScan.getValStr(),
									etx_wostQcWoNbr.getValStr(),
									shift,
									etx_wostQcPart.getValStr(),
									etx_wostQcPartDesc.getValStr(),
									etx_wostQcLot.getValStr(),
									etx_wostQcWkctr.getValStr(),
									etx_wostQcLine.getValStr(),
									cPart,
									uKey,
									etx_wostQcWoDate.getValStr(),
									etx_wostQcSn.getValStr());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etx_wostQcScan.reValue();
			etx_wostQcWoNbr.reValue();
			etx_wostQcShift.reValue();
			shift = "";
			etx_wostQcPart.reValue();
			etx_wostQcPartDesc.reValue();
			etx_wostQcLot.reValue();
			etx_wostQcWkctr.reValue();
			etx_wostQcLine.reValue();
			etx_wostQcSn.reValue();
			cPart = "";
			uKey = "";
			lbs = "";
			etx_wostQcWoDate.reValue();
			showSuccessMsg(wr.getMessages());
			getFocues(etx_wostQcScan, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_wostQcScan, true);
		}
	}
	
	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
}
