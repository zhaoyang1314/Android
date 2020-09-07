package com.profitles.activity;

import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.profitles.biz.ReplocBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

public class ReplocActivity extends AppFunActivity {

	private ReplocBiz pbiz;
	private MyEditText etxReplocPart,etxReplocLot;
	private MyReadBQ  etxReplocLoc,etxReplocBin,etxReplocscan;
	private MyDataGrid dtgReplocConsInfo;
	private ApplicationDB applicationDB;
	
	@Override
	protected void pageLoad() {
		pbiz = new ReplocBiz();
		
		etxReplocPart = (MyEditText)findViewById(R.id.etx_replocPart);
		etxReplocLot = (MyEditText)findViewById(R.id.etx_replocLot);
		etxReplocscan = (MyReadBQ)findViewById(R.id.etx_replocscan);
		etxReplocLoc = (MyReadBQ)findViewById(R.id.etx_replocLoc);
		etxReplocBin = (MyReadBQ)findViewById(R.id.etx_replocBin);
		dtgReplocConsInfo = (MyDataGrid)findViewById(R.id.dtg_ReplocConsInfo);
		runClickFun();
		
		etxReplocscan.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etxReplocscan.getValStr() && !"".equals( etxReplocscan.getValStr().toString().trim())){
					 checkPkCmScan();
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			public void afterTextChanged(Editable s) {
			}
		});
	}

	// 扫码 条码 查询库存
	private void checkPkCmScan() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				boolean flag = true ; 
				try {
					String scan = etxReplocscan.getValStr().toString().toUpperCase();
//					String PART,SHNSCAN;
//					String[] split = scan.split("\\+");
//					PART = split[0];
//					SHNSCAN = split[5];
					String PART = "";
					String[] scans = scan.split("[$]");
					for (int i = 1; i < scans.length; i++) {
						 if (scans[i].startsWith("P")){
							 PART = scans[i].substring(1).toString();//零件编码(去掉‘P'标记)
		                 }
					}
					etxReplocPart.reValue();
					etxReplocPart.setText(PART);
				} catch (Exception e) {
					flag = false;
					showErrorMsg("条码格式不正确，请重新扫码");
					etxReplocscan.reValue();
					etxReplocPart.reValue();
				}
				return flag;
			}
			@Override
			public Object onGetData() {
				return pbiz.pkCheckNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxReplocscan.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				dtgReplocConsInfo.buildData((List<Map<String, Object>>)data);
				etxReplocscan.reValue();
				getFocues(etxReplocscan, true);
			}
		});
	}
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_reploc;
	}
	
	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		if(etxReplocscan.getId() == id){
			runClickFun();
			if(null != etxReplocscan.getValStr() && !"".equals( etxReplocscan.getValStr().toString().trim())){
				 checkPkCmScan();
			}
		}
		if(etxReplocPart.getId() == id){
			runClickFun();
		}
		if(etxReplocLot.getId()==id){
			runClickFun();
		}
		if(etxReplocLoc.getId() == id){
			runClickFun();
		}
		if(etxReplocBin.getId() == id){
			runClickFun();
		}
		return istrue ;
	}
	
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ ButtonType.Search};
	}

	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		if(!"".equals(etxReplocPart.getValStr().toString().trim()) || !"".equals(etxReplocLot.getValStr().toString().trim()) ||
				!"".equals(etxReplocLoc.getValStr().toString().trim()) || !"".equals(etxReplocBin.getValStr().toString().trim())){
		
		}else{
			istrue = false;
			showErrorMsg(getResources().getString(R.string.Reploc_false));
			getFocues(etxReplocPart, true);
		}
		return istrue;
	}
	
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return pbiz.getConsInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxReplocPart.getValStr(), etxReplocLot.getValStr(), etxReplocLoc.getValStr(), etxReplocBin.getValStr()).getDataToList();
	}

	@Override
	public void OnBtnSerCallBack(Object data) {
		dtgReplocConsInfo.buildData((List<Map<String, Object>>)data);
	}

	@Override
	protected void unLockNbr() {
		
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
}