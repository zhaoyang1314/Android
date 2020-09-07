package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.profitles.biz.QiBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class QibActivity extends AppFunActivity {

	private MyReadBQ actBin;
	private QiBiz qiBiz;
	private MyDataGrid dtg;
	private String domain, site,bin;
	private List<Map<String, Object>> nbrlist = new ArrayList<Map<String, Object>>();

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_qib;
	}

	@Override
	protected void pageLoad() {
		qiBiz = new QiBiz();
		actBin = (MyReadBQ) findViewById(R.id.etx_qipBin);
		actBin.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actBin.getValStr().equals("")){
					changeBin();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		dtg = (MyDataGrid) findViewById(R.id.mdtg_qi);
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		runClickFun();
	}

	protected void unLockNbr() {
	}
	
	protected boolean onBlur(int id, View v) {
		if(R.id.etx_qipBin==id){  
			runClickFun();
		}
		return true;
	}
	
	private void changeBin(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				bin = actBin.getValStr();
				return true;
			}

			@Override
			public Object onGetData() {
				return qiBiz.qip_queryInvByBin(domain, site, actBin.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				actBin.setText("");
				if(wr.isSuccess()){
					nbrlist=((WebResponse) data).getDataToList();
					dtg.buildData(nbrlist);
					showSuccessMsg("当前扫描储位:"+bin);
				}else{
					showErrorMsg(wr.getMessages());
					clearMsg();
				}
			}
		});
	}	
	
	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
