package com.profitles.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.profitles.biz.BlockBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class BlockActivity extends AppFunActivity {

	private BlockBiz biz;
	private MyEditText  etx_rfShift,etx_rfState,etx_rfPart,
	                    etx_rfPartDesc,etx_rfVend,etx_rfVendDesc,etx_rfVersion,
	                    etx_rfLoc,etx_rfBin,etx_rfSn, etx_rfDate, etx_rfQty;
	private MyReadBQ  etx_rfScan;
	private MyTextView etx_rfSnv,etx_rfPar;
	private List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
	private ApplicationDB applicationDB;
	Map<String, Object> infoMap = new HashMap<String, Object>();
	boolean isTrue=false;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_block;
	}

	@Override
	protected void pageLoad() {
		biz=new BlockBiz();
		etx_rfScan = (MyReadBQ) findViewById(R.id.etx_rfScan);
		etx_rfShift = (MyEditText) findViewById(R.id.etx_rfShift);
		etx_rfState = (MyEditText) findViewById(R.id.etx_rfState);
		etx_rfPart = (MyEditText) findViewById(R.id.etx_rfPart);
		etx_rfSn=(MyEditText) findViewById(R.id.etx_rfSn);
		etx_rfSnv=(MyTextView) findViewById(R.id.etx_rfSnv);
		etx_rfPar=(MyTextView) findViewById(R.id.etx_rfPar);
		etx_rfPartDesc = (MyEditText) findViewById(R.id.etx_rfPartDesc);
		etx_rfVend = (MyEditText) findViewById(R.id.etx_rfVend);
		etx_rfVendDesc = (MyEditText) findViewById(R.id.etx_rfVendDesc);
		etx_rfVersion = (MyEditText) findViewById(R.id.etx_rfVersion);
		etx_rfLoc = (MyEditText) findViewById(R.id.etx_rfLoc);
		etx_rfBin = (MyEditText) findViewById(R.id.etx_rfBin);
		etx_rfDate = (MyEditText) findViewById(R.id.etx_rfDate);
		etx_rfScan.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_rfScan.getValStr() && !"".equals( etx_rfScan.getValStr().toString().trim())){
					checkWostScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
	} 

	
	//处理条码解析
	private void checkWostScan() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return biz.getScanInfo(ApplicationDB.user.getUserDmain(), 
						ApplicationDB.user.getUserSite(),
						etx_rfScan.getValStr(),
						ApplicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					infoMap = wr.getDataToMap();
					if(!StringUtil.isEmpty(infoMap.get("RFLOT_SHIFT"))){
						etx_rfShift.setText(infoMap.get("RFLOT_SHIFT")+"");
					}else{
						etx_rfShift.setText("");
					}
					
					etx_rfState.setText(infoMap.get("RFLOT_SCAN_STATUS_NM")+"");
					if(StringUtil.isEmpty(infoMap.get("RFLOT_PART_DESC"))){
						etx_rfPar.setVisibility(View.GONE);
						etx_rfPart.setVisibility(View.GONE);      //没有零件描述隐藏
						etx_rfPartDesc.setVisibility(View.GONE);
						etx_rfSnv.setVisibility(View.VISIBLE);
						etx_rfSn.setVisibility(View.VISIBLE);
						etx_rfSn.setText(infoMap.get("RFLOT_SERIAL")+"");
					}else{
						etx_rfPar.setVisibility(View.VISIBLE);
						etx_rfPart.setVisibility(View.VISIBLE);
						etx_rfPartDesc.setVisibility(View.VISIBLE);
						etx_rfSnv.setVisibility(View.GONE);
						etx_rfSn.setVisibility(View.GONE);
						etx_rfPart.setText(infoMap.get("RFLOT_PART")+"");
						etx_rfPartDesc.setText(infoMap.get("RFLOT_PART_DESC")+"");
					}
					etx_rfVend.setText(infoMap.get("RFLOT_VEND")+"");
					if(!StringUtil.isEmpty(infoMap.get("VENDNAME"))){
						etx_rfVendDesc.setText(infoMap.get("VENDNAME")+"");
					}else{
						etx_rfVendDesc.setText("");
					}
					
					if(!StringUtil.isEmpty(infoMap.get("RFLOT_PART_REV"))){
						etx_rfVersion.setText(infoMap.get("RFLOT_PART_REV")+"");
					}else{
						etx_rfVersion.setText("");
					}
					if(!StringUtil.isEmpty(infoMap.get("RFLOT_LOC"))){
						etx_rfLoc.setText(infoMap.get("RFLOT_LOC")+"");
					}else{
						etx_rfLoc.setText("");
					}
					if(!StringUtil.isEmpty(infoMap.get("RFLOT_BIN"))){
						etx_rfBin.setText(infoMap.get("RFLOT_BIN")+"");
					}else{
						etx_rfBin.setText("");
					}
					etx_rfDate.setText(infoMap.get("ORD_DATE")+"");
					isTrue=true;
				}else{
					etx_rfScan.setText("");
					etx_rfShift.reValue();
					etx_rfState.reValue();
					etx_rfPart.reValue();
					etx_rfSn.reValue();
					etx_rfPartDesc.reValue();
					etx_rfVend.reValue();
					etx_rfVendDesc.reValue();
					etx_rfVersion.reValue();
					etx_rfLoc.reValue();
					etx_rfBin.reValue();
					etx_rfDate.reValue();
					showErrorMsg(wr.getMessages());
					getFocues(etx_rfScan, true);
					isTrue = false;
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
		if(isTrue==true){
			return true;
		}else{
			showErrorMsg("请先扫描正确的标签");
			return false;
		}
		
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return biz.updateState(ApplicationDB.user.getUserDmain(), 
						ApplicationDB.user.getUserSite(),
						etx_rfScan.getValStr(),
						ApplicationDB.user.getUserId());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			showMessage(wr.getMessages());
			etx_rfScan.reValue();
			etx_rfShift.reValue();
			etx_rfState.reValue();
			etx_rfPart.reValue();
			etx_rfSn.reValue();
			etx_rfPartDesc.reValue();
			etx_rfVend.reValue();
			etx_rfVendDesc.reValue();
			etx_rfVersion.reValue();
			etx_rfLoc.reValue();
			etx_rfBin.reValue();
			etx_rfDate.reValue();
		}else{
			showErrorMsg(wr.getMessages());
			
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
