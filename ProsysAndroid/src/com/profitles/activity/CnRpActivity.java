package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.View;

import com.profitles.biz.ReportBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyDateView;

public class CnRpActivity extends AppFunActivity {

	private ReportBiz pbiz;
	private MyDateView dtvEtaDate;
	private MyDataGrid dtgConsInfo;
	
	@Override
	protected void pageLoad() {
		pbiz = new ReportBiz();
		
		dtvEtaDate = (MyDateView)findViewById(R.id.dtv_CnrcETA);
		dtgConsInfo = (MyDataGrid)findViewById(R.id.dtg_CnrpConsInfo);
	}

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_cnrp;
	}
	
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ ButtonType.Search, ButtonType.Return, ButtonType.Help };
	}

	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return pbiz.getConsInfo(dtvEtaDate.getValStr()).getDataToList();
	}

	@Override
	public void OnBtnSerCallBack(Object data) {
		dtgConsInfo.buildData((List<Map<String, Object>>)data);
	}

	@Override
	protected void unLockNbr() {
		
	}

	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return null;
	}
}