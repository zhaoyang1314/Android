package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.View;

import com.profitles.biz.CnRtBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;


public class CnRtActivity extends AppFunActivity {

	private CnRtBiz cbiz;
	private MyEditText etxCnrtVend,etxCnrtVendName,etxCnrtDataset,etxCnrtDate;
	private MyReadBQ etxCrnrtNbr; 
	private MyDataGrid dtgCnrtConsInfo;
	private ApplicationDB applicationDB;
	private String fnceffdate;
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_cnrt;
	}
	
	@Override
	protected void pageLoad() {
		cbiz = new CnRtBiz();
		etxCrnrtNbr = (MyReadBQ) findViewById(R.id.etx_cnrtNbr);
		etxCnrtVend = (MyEditText) findViewById(R.id.etx_cnrtVend);
		etxCnrtVendName = (MyEditText) findViewById(R.id.etx_cnrtVendName);
		etxCnrtDataset = (MyEditText) findViewById(R.id.etx_cnrtDataset);
		etxCnrtDate = (MyEditText) findViewById(R.id.etx_cnrtDate);
		dtgCnrtConsInfo = (MyDataGrid) findViewById(R.id.dtg_CnrtConsInfo);
		fnceffdate = ApplicationDB.user.getUserDate();
	}

	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		if(etxCrnrtNbr.getId() == id){
			runClickFun();
		}
		return istrue;
	}
	
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ ButtonType.Search, ButtonType.Submit, ButtonType.Return, ButtonType.Help };
	}
	
	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		if(!"".equals(etxCrnrtNbr.getValStr().toString().trim())){
			istrue = true;
		}else{
			showErrorMsg(getResources().getString(R.string.pk_sub_false));
			getFocues(etxCrnrtNbr, true);
			istrue = false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return cbiz.getConsNbr(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(), etxCrnrtNbr.getValStr());
	}

	@Override
	public void OnBtnSerCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			etxCnrtVend.setText(map.get("RFPKG_VEND")+"");
			etxCnrtVendName.setText(map.get("RFPKG_VEND_NAME")+"");
			etxCnrtDataset.setText(map.get("RFPKG_DATASET")+"");
			etxCnrtDate.setText(map.get("RFPKG_DATE")+"");
			dtgCnrtConsInfo.buildData((List<Map<String, Object>>) map.get("List"));
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxCrnrtNbr, true);
		}
		
	}
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals(etxCrnrtNbr.getValStr().toString().trim()) && !"".equals(etxCnrtVend.getValStr().toString().trim())){
			istrue = true;
		}else{
			showErrorMsg(getResources().getString(R.string.cnrt_sub_false));
			getFocues(etxCrnrtNbr, true);
			istrue = false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return cbiz.getCnrcSubmit(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(), etxCrnrtNbr.getValStr(), applicationDB.user.getUserId(),fnceffdate);
	}

	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxCrnrtNbr.reValue();
			etxCnrtVend.reValue();
			etxCnrtVendName.reValue();
			etxCnrtDataset.reValue();
			etxCnrtDate.reValue();
			List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
			dtgCnrtConsInfo.buildData(list);
			getFocues(etxCrnrtNbr, true);
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxCrnrtNbr, true);
		}
	}

	@Override
	protected void unLockNbr() {
		
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
}