package com.profitles.activity;

import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;

import com.profitles.biz.RftriqBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyDateView;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;

public class RftriqActivity extends AppFunActivity {

	private RftriqBiz pbiz;
	private MyEditText etxRftriqPart,etxRftriqLoc,etxRftriqVend;
	private MyReadBQ  etxRftriqScan;
	private MyDateView dtvEtaDate;
	private MyDataGrid dtgRftriqConsInfo;
	private ApplicationDB applicationDB;
	private CheckBox etx_rftriqUser;
	
	@Override
	protected void pageLoad() {
		pbiz = new RftriqBiz();
		
		etxRftriqPart = (MyEditText)findViewById(R.id.etx_rftriqPart);
		etxRftriqVend = (MyEditText)findViewById(R.id.etx_rftriqVend);
		etxRftriqLoc = (MyEditText)findViewById(R.id.etx_rftriqLoc);
		dtvEtaDate = (MyDateView)findViewById(R.id.dtv_rftriqETA);
		etxRftriqScan = (MyReadBQ)findViewById(R.id.etx_rftriqScan);
		dtgRftriqConsInfo = (MyDataGrid)findViewById(R.id.dtg_rftriqConsInfo);
		etx_rftriqUser = (CheckBox)findViewById(R.id.etx_rftriqUser);
		getFocues(etxRftriqScan, true);
		etxRftriqScan.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etxRftriqScan.getValStr() && !"".equals( etxRftriqScan.getValStr().toString().trim())){
					getScanInfo();
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
	protected int getMainBodyLayout() {
		return R.layout.act_rftriq;
	}
	
	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		if(etxRftriqPart.getId() == id){
				runClickFun();
		}
		if(etxRftriqVend.getId()==id){
				runClickFun();
		}
		if(etxRftriqLoc.getId() == id){
			runClickFun();
		}
		if(dtvEtaDate.getId() == id){
			runClickFun();
		}
		if(etxRftriqScan.getId() == id){
			runClickFun();
			if(etxRftriqScan.getValStr() != null && !(etxRftriqScan.getValStr().toString().trim()).equals("")){
				getScanInfo();
			}
		}
		return istrue ;
	}
	// 扫码 条码 查询库存
	private void getScanInfo() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				boolean flag = true ; 
				try {
					String scan = etxRftriqScan.getValStr().toString().toUpperCase();
//					String PART,SHNSCAN;
//					String[] split = scan.split("\\+");
//					PART = split[0];
//					SHNSCAN = split[5];
					String PART = "";
					String[] scnas = scan.split("[$]");
					for (int i = 1; i < scnas.length; i++) {
						if (scnas[i].startsWith("P")) {
							PART = scnas[i].substring(1).toString();// 零件编码(去掉‘P'标记)
						}
					}
					etxRftriqPart.reValue();
					etxRftriqPart.setText(PART);
				} catch (Exception e) {
					flag = false;
					showErrorMsg("条码格式不正确，请重新扫码");
					etxRftriqScan.reValue();
					etxRftriqPart.reValue();
				}
				return flag;
			}
			@Override
			public Object onGetData() {
				return pbiz.getRftriqInfoScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),dtvEtaDate.getValStr(),applicationDB.user.getUserId(),etxRftriqScan.getValStr(),etx_rftriqUser.isChecked());
			}
			@Override
			public void onCallBrack(Object data) {
				dtgRftriqConsInfo.buildData((List<Map<String, Object>>)data);
				etxRftriqScan.reValue();
			}
		});
	}
	
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ ButtonType.Search};
	}

	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return pbiz.getRftriqInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxRftriqPart.getValStr(), etxRftriqVend.getValStr(), etxRftriqLoc.getValStr(), dtvEtaDate.getValStr(),applicationDB.user.getUserId(),etxRftriqScan.getValStr(),
				etx_rftriqUser.isChecked()).getDataToList();
	}

	@Override
	public void OnBtnSerCallBack(Object data) {
		dtgRftriqConsInfo.buildData((List<Map<String, Object>>)data);
	}

	@Override
	protected void unLockNbr() {
		
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
}