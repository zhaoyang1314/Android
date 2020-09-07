package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.profitles.biz.BlockBiz;
import com.profitles.biz.SelfiViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.util.StringUtil;

public class DetermineActivity extends AppFunActivity {
	private MyEditText txvPart, txvPartDesc, txvLoc, txvBin, txvCustPart, txvVend, txvQty, txvUm;
	private MyReadBQ txvScan;
	private MySpinner spn_type, spn_scan_type;
	private String domain, site, userid;
	private ApplicationDB applicationDB;
	private SelfiViewBiz selBiz;
	private List<Map<String , Object>> tList = new ArrayList<Map<String,Object>>();
	Map<String, Object> infoMap = new HashMap<String, Object>();
	private Map sMap = new HashMap();
	private BlockBiz biz;
	boolean isTrue=false;
	@Override
	protected void pageLoad() {
		biz=new BlockBiz();
		selBiz = new SelfiViewBiz();
		txvScan =(MyReadBQ) findViewById(R.id.etx_RScansScan);
		txvPart = (MyEditText) findViewById(R.id.etx_RScansPart);
		txvPartDesc = (MyEditText) findViewById(R.id.etx_RScansPart_Desc);
		txvLoc = (MyEditText) findViewById(R.id.etx_Loc);
		txvCustPart = (MyEditText) findViewById(R.id.etx_CustPart);
		txvBin = (MyEditText) findViewById(R.id.etx_Bin);
		txvVend = (MyEditText) findViewById(R.id.etx_Vend);
		txvQty = (MyEditText) findViewById(R.id.etx_Qty);
		txvUm = (MyEditText) findViewById(R.id.etx_Um);
		
		spn_type = (MySpinner) findViewById(R.id.spn_Type);
		spn_scan_type = (MySpinner) findViewById(R.id.spn_RScansType);
		domain = applicationDB.user.getUserDmain();
		site = applicationDB.user.getUserSite();
		userid = applicationDB.user.getUserId();
//		spn_type.addItem("来料检验", "IQC");
		spn_type.addItem("过程检验", "PQC");
//		spn_type.addItem("出货检验", "OQC");
		
		txvScan.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != txvScan.getValStr() && !"".equals( txvScan.getValStr().toString().trim())){
					checkWostScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
	
		
		spn_type.setOnItemSelectedListener(new OnItemSelectedListener() { // 检验类型
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
					getRscanType();
			};
			public void onNothingSelected(AdapterView<?> parent){
				
			};
		});
		
		spn_scan_type.setOnItemSelectedListener(new OnItemSelectedListener() { // 拆分类型
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				
			};
			public void onNothingSelected(AdapterView<?> parent){
				
			};
		});
		
	}

	@Override
	protected int getMainBodyLayout() {
		// TODO Auto-generated method stub
		return R.layout.act_determine;
	}

	/**
	 * 编写业务实际操作区域  开始
	 * */
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
							txvScan.getValStr(),
							ApplicationDB.user.getUserId());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr = (WebResponse)data;
					if(wr.isSuccess()){
						infoMap = wr.getDataToMap();
						if(!StringUtil.isEmpty(infoMap.get("RFLOT_PART"))){
							txvPart.setText(infoMap.get("RFLOT_PART")+"");
						}else{
							txvPart.setText("");
						}
						if(!StringUtil.isEmpty(infoMap.get("RFLOT_PART_DESC"))){
							txvPartDesc.setText(infoMap.get("RFLOT_PART_DESC")+"");
						}else{
							txvPartDesc.setText("");
						}
						if(!StringUtil.isEmpty(infoMap.get("RFLOT_LOC"))){
							txvLoc.setText(infoMap.get("RFLOT_LOC")+"");
						}else{
							txvLoc.setText("");
						}
						if(!StringUtil.isEmpty(infoMap.get("RFLOT_CUST_PART"))){
							txvCustPart.setText(infoMap.get("RFLOT_CUST_PART")+"");
						}else{
							txvCustPart.setText("");
						}
						if(StringUtil.isEmpty(infoMap.get("RFLOT_SCATTER_QTY")) || infoMap.get("RFLOT_SCATTER_QTY").toString().equals("0.0")){
							txvQty.setText(infoMap.get("RFLOT_MULT_QTY")+"");
						}else{
							txvQty.setText(infoMap.get("RFLOT_SCATTER_QTY")+"");
						}
						txvVend.setText(infoMap.get("RFLOT_VEND")+"");
						
							txvUm.setText("PCS");
						
						
						if(!StringUtil.isEmpty(infoMap.get("RFLOT_BIN"))){
							txvBin.setText(infoMap.get("RFLOT_BIN")+"");
						}else{
							txvBin.setText("");
						}
						isTrue=true;
					}else{
						txvPart.reValue();
						txvPartDesc.reValue();
						txvLoc.reValue();
						txvCustPart.reValue();
						txvQty.reValue();
						txvUm.reValue();
						txvBin.reValue();
						showErrorMsg(wr.getMessages());
						getFocues(txvScan, true);
						isTrue = false;
					}
				}
			});
		}
	
	
	private void getRscanType(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return selBiz.getRscanType(domain, spn_type.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					if(tList.size() > 0) tList.clear();
					if(!StringUtil.isEmpty(spn_scan_type.getValStr())){
						spn_scan_type.clearItems();
					}
					spn_scan_type.addItem("请选择拆分类型","0");
					Map<String ,Object> map = wr.getDataToMap();
					tList = (List<Map<String, Object>>) map.get("LIST");
					for (int i = 0; i < tList.size(); i++) {
						 sMap = tList.get(i);
						 spn_scan_type.addItem(sMap.get("NAME")+"",sMap.get("CODE")+"");
					}
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	
	/**
	 * 编写业务实际操作区域  结束
	 * */
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit};
	}	

	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(StringUtil.isEmpty(txvScan.getValStr())){
			showErrorMsg("请扫描标签之后进行操作");
			return false;
		}else if(spn_scan_type.getValStr().equals("0")){
			showErrorMsg("请选择拆分类型后操作");
			return false;
		}
		return true;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return biz.getScanPd(ApplicationDB.user.getUserDmain(), 
						ApplicationDB.user.getUserSite(),
						txvScan.getValStr(),
						ApplicationDB.user.getUserId(),spn_scan_type.getValStr());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			showMessage(wr.getMessages());
			txvPart.reValue();
			txvPartDesc.reValue();
			txvLoc.reValue();
			txvCustPart.reValue();
			txvQty.reValue();
			txvUm.reValue();
			txvBin.reValue();
			txvScan.reValue();
		}else{
			showErrorMsg(wr.getMessages());
		}
	}
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}

}
