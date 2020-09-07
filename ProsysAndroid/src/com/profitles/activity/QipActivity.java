package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
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

public class QipActivity extends AppFunActivity {

	private MyReadBQ actPart;
	private QiBiz qiBiz;
	private MyDataGrid dtg;
	private String domain, site,bar;
	private List<Map<String, Object>> nbrlist = new ArrayList<Map<String, Object>>();

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_qip;
	}

	@Override
	protected void pageLoad() {
		qiBiz = new QiBiz();
		actPart = (MyReadBQ) findViewById(R.id.etx_qipPart);
		actPart.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actPart.getValStr().equals("")){
					changePart();
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
		if(R.id.etx_qipPart==id){  
			runClickFun();
		}
		return true;
	}
	
	private void changePart(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				bar = actPart.getValStr();
				return true;
			}

			@Override
			public Object onGetData() {
				return qiBiz.qip_queryInvByPart(domain, site, actPart.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				actPart.setText("");
				if(wr.isSuccess()){
					Map<String, Object> map = new HashMap<String, Object>() ;
					map=((WebResponse) data).getDataToMap();
					nbrlist = (List)map.get("list");
					String SumQty = map.get("SumQty")+"";
					dtg.buildData(nbrlist);
					showSuccessMsg("当前扫描零件:"+bar+"的总库存量:"+SumQty);
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
