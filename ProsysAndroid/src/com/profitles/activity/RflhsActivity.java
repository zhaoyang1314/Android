package com.profitles.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.View;

import com.profitles.biz.RflhsBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

public class RflhsActivity extends AppFunActivity {
	//标签号，批次，零件，采购单号，送货单号，供应商

	private RflhsBiz rflbiz;

	private MyReadBQ  etxrflscan,etxrfllot,etxrflpart,etxrflpo,etxconsnbr,etxrflvend;
	private MyDataGrid dtgRflhist;
	
	private ApplicationDB applicationDB;
	
	
	@Override
	protected void pageLoad() {
		rflbiz = new RflhsBiz();
		etxrflscan = (MyReadBQ) findViewById(R.id.etx_rflscan);
		etxrfllot = (MyReadBQ) findViewById(R.id.etx_rfllot);
		etxrflpart = (MyReadBQ) findViewById(R.id.etx_rflpart);
		etxrflpo = (MyReadBQ) findViewById(R.id.etx_rflpo);
		etxconsnbr = (MyReadBQ) findViewById(R.id.etx_consnbr);
		etxrflvend = (MyReadBQ) findViewById(R.id.etx_rflvend);
		
		
		dtgRflhist = (MyDataGrid)findViewById(R.id.dtg_rflhistInfo);
		runClickFun();
	}

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_rflhs;
	}
	
	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		if(etxrflscan.getId() == id){
			runClickFun();
		}
		if(etxrfllot.getId()==id){
			runClickFun();
		}
		if(etxrflpart.getId() == id){
			runClickFun();
		}
		if(etxrflpo.getId() == id){
			runClickFun();
		}
		
		if(etxconsnbr.getId() == id){
			runClickFun();
		}
		if(etxrflvend.getId() == id){
			runClickFun();
		}
		return istrue ;
	}
	
	
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ ButtonType.Search};
	}

	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		if(!"".equals(etxrflscan.getValStr().toString().trim()) || !"".equals(etxrfllot.getValStr().toString().trim()) ||!"".equals(etxrflpart.getValStr().toString().trim()) ||
			!"".equals(etxrflpo.getValStr().toString().trim()) || !"".equals(etxconsnbr.getValStr().toString().trim()) || !"".equals(etxrflvend.getValStr().toString().trim())){
		
		}else{
			istrue = false;
			showErrorMsg(getResources().getString(R.string.Rfl_false));
			getFocues(etxrflscan, true);
		}
		return istrue;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return rflbiz.getRflInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxrflscan.getValStr(), 
				etxrfllot.getValStr(), etxrflpart.getValStr(), etxrflpo.getValStr(),etxconsnbr.getValStr(),etxrflvend.getValStr()).getDataToList();
	}


	@SuppressWarnings("unchecked")
	@Override
	public void OnBtnSerCallBack(Object data) {
		List<Map<String , Object>> rflList   = (List<Map<String, Object>>) data;
		for (int i = 0; i < rflList.size(); i++) {
			int x = StringUtil.parseInt(rflList.get(i).get("RFLOT_BOX_QTY"));
			int y = StringUtil.parseInt(rflList.get(i).get(",RFLOT_PRINTED"));
			if (x==-1) {
				rflList.get(i).put("RFLOT_BOX_QTY", 0);
				rflList.get(i).put("RFLOT_PRINTED", y);
			}else if(y==-1){
				rflList.get(i).put("RFLOT_BOX_QTY", x);
				rflList.get(i).put("RFLOT_PRINTED", 0);
			}else{
			rflList.get(i).put("RFLOT_BOX_QTY", x);
			rflList.get(i).put("RFLOT_PRINTED", y);}
		}
		dtgRflhist.buildData(rflList);
	}

	@Override
	protected void unLockNbr() {
		
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
}