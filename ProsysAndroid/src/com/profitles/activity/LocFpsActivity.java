package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.R.string;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.profitles.biz.LocFpsBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.util.StringUtil;

public class LocFpsActivity extends AppFunActivity {
	private LocFpsBiz locFpsBiz;
	private MyReadBQ  etxLocFpsBar;
 	private MyEditText etxLocFpsPart,etxLocFpsPartDesc,etxLocFpsQty,etxLocFpsUm,etxLocFpsBox,etxLocFpsNum; 
	private boolean onPageLoad = true;
	private String domain , site  ; 
	private int boxqty = 0 , resqty=0  ;
	//,boxqty,resqty
	private ApplicationDB applicationDB;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_locfps;
	}

	// @SuppressLint("CutPasteId")
	@Override 
	protected void pageLoad() {
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite(); 
		 
		locFpsBiz = new LocFpsBiz();
		etxLocFpsBar = (MyReadBQ) findViewById(R.id.etx_locFpsBar);
		etxLocFpsBar.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { 
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(null != etxLocFpsBar.getValStr() && !"".equals( etxLocFpsBar.getValStr().toString().trim())){
					checkBar();
				} 
			}
			public void afterTextChanged(Editable s) {
			}
			
		});
		etxLocFpsPart= (MyEditText) findViewById(R.id.etx_locFpsPart);
		etxLocFpsPartDesc= (MyEditText) findViewById(R.id.etx_locFpsPartDesc);
		etxLocFpsQty= (MyEditText) findViewById(R.id.etx_locFpsQty);
		etxLocFpsUm= (MyEditText) findViewById(R.id.etx_locFpsUm);
		etxLocFpsBox= (MyEditText) findViewById(R.id.etx_locFpsBox);
		etxLocFpsNum= (MyEditText) findViewById(R.id.etx_locFpsNum);  
	}
  
	@Override
	protected boolean onFocus(int id, View v) {
		return super.onFocus(id, v);
	}
	boolean istrue = true; 
	Map map = new HashMap();
	@Override
	protected boolean onBlur(int id, View v) { 
		runClickFun();
		return istrue ;
	}
	
	private void checkBar() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if(!"".equals(etxLocFpsBar.getValStr().toString().trim())){
					boolean flag = true ; 
					try {
						String scan = etxLocFpsBar.getValStr().toString();
						String PART,SHNSCAN;
						String[] split = scan.split("\\+");
						PART = split[0];
						SHNSCAN = split[5];
						String[] splitSHNS = SHNSCAN.split("[$]");
						if(splitSHNS.length ==9 ){
								for (int i = 1; i < splitSHNS.length; i++) {
									 if (splitSHNS[i].toUpperCase().substring(0,1).equals("P")){
										 PART = splitSHNS[i].substring(1).toString();//零件编码(去掉‘P'标记)
					                 }
								}
						 }
					} catch (Exception e) {
						flag = false;
						etxLocFpsBar.reValue();
						showErrorMsg("条码格式不正确，请重新扫码");
					}
					return flag;
				}else{
					showErrorMsg(getResources().getString(R.string.pk_sub_false));
					getFocues(etxLocFpsBar, true);
					return  istrue = false; 
				}
			}
			@Override
			public Object onGetData() {
				return locFpsBiz.saveLocFpsBar(applicationDB.user.getUserDmain() , 
						applicationDB.user.getUserSite() ,
						etxLocFpsBar.getValStr().toString() , 
						applicationDB.user.getUserId() , 
						applicationDB.user.getUserDate(),
						applicationDB.user.getMac());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					  Map<String, Object> mpRtn = wr.getDataToMap();
					  etxLocFpsPart.setText(mpRtn.get("RFLOT_PART").toString());   //零件
					  etxLocFpsPartDesc.setText(mpRtn.get("RFLOT_PART_DESC").toString());  //零件描述
					  etxLocFpsQty.setText(mpRtn.get("RFLOT_MULT_QTY").toString());  //数量
					  etxLocFpsUm.setText(mpRtn.get("RFLOT_UM").toString());  //单位
					  boxqty += 1;
					  String valueOf = String.valueOf(boxqty);
					  etxLocFpsBox.setText(valueOf); //已扫箱数
					  resqty += StringUtil.parseInt(etxLocFpsQty.getValStr()) ;
					  String valueOf2 = String.valueOf(resqty);
					  etxLocFpsNum.setText(valueOf2); //已扫总量
					  showMessage1(wr.getMessages());
					  etxLocFpsBar.reValue();
					  getFocues(etxLocFpsBar, true);
				}else{
					istrue = false;
					 etxLocFpsBar.reValue();
					showErrorMsg(wr.getMessages());
					getFocues(etxLocFpsBar, true);
				}
			}
		});
	}

	private void setChangeTable(Map<String, Object> map) {   
	}
	
	  
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Return};
	}
	
	
	@Override
	public Object OnBtnHelpClick(ButtonType btnType, View v) {
		showSuccessMsg(R.string.help_ok);
		return  null ;
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
	
	//解锁
	@Override
	protected void unLockNbr() {
	
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {	
					return  locFpsBiz;
					}
				@Override
				public void onCallBrack(Object data) {

				}
			});	
	
	}
}
