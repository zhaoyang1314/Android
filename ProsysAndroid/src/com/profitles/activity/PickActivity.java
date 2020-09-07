package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;

import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;


public class PickActivity extends AppFunActivity {
	//private XBoxBiz xBoxbiz;
	private MyEditText etxXBoxPart,etxXBoxUm,etxXBoxPartDesc,etxXBoxLot,
						etxXBoxSplit,etxXBoxUnit,etxXBoxSurplus,etxXBoxSplitBox;
	private MyReadBQ  etxXBoxBar;

	private MyDataGrid dtg;
	private ApplicationDB applicationDB;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pick;
	}

	@Override
	protected void pageLoad() {
		dtg = (MyDataGrid)findViewById(R.id.mdtg_qcpick22);
		
	}	
	private List<Map<String, Object>> createDemoData(){
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();

		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("NAME", "1110506");
		itemMap.put("NAMEDESC", "氯醚橡胶");
		itemMap.put("DW", "KG");
		itemMap.put("MX", "20.0" );
		itemMap.put("GW", "CHG-01");
		itemMap.put("SUM", "20.0");
		data.add(itemMap);
		
		Map<String, Object> itemMap2 = new HashMap<String, Object>();
		itemMap.put("NAME", "1110506");
		itemMap.put("NAMEDESC", "氯醚橡胶");
		itemMap.put("DW", "KG");
		itemMap.put("MX", "20.0" );
		itemMap.put("GW", "CHG-01");
		itemMap.put("SUM", "20.0");
		data.add(itemMap2);
		
		Map<String, Object> itemMap3 = new HashMap<String, Object>();
		itemMap.put("NAME", "1110506");
		itemMap.put("NAMEDESC", "氯醚橡胶");
		itemMap.put("DW", "KG");
		itemMap.put("MX", "20.0" );
		itemMap.put("GW", "CHG-01");
		itemMap.put("SUM", "20.0");
		data.add(itemMap3);
		
		Map<String, Object> itemMap4 = new HashMap<String, Object>();
		itemMap.put("NAME", "1110506");
		itemMap.put("NAMEDESC", "氯醚橡胶");
		itemMap.put("DW", "KG");
		itemMap.put("MX", "20.0" );
		itemMap.put("GW", "CHG-01");
		itemMap.put("SUM", "20.0");
		data.add(itemMap4);
		
		return data;
	}

	@Override
	protected boolean onFocus(int id, View v) {
		return super.onFocus(id, v);
	}

	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		dtg.buildData(createDemoData());
		istrue = true;
		return istrue ;
	}


	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Submit,ButtonType.Save};
	}

	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		return true;
	}
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return null;
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {

	}
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
	  return true;
	}
	
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return null;
	}
		
	@Override
	public void OnBtnSaveCallBack(Object data) {
		
	}
	

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
}



