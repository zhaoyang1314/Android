package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;

import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;

public class DemoActivity extends AppFunActivity {

	
	private MyDataGrid dtg;
	
	@Override
	protected void pageLoad() {
		dtg = (MyDataGrid)findViewById(R.id.dtg_demo);
		dtg.buildData(createDemoData());
		dtg.setOnMyDataGridListener(new OnMyDataGridListener() {

			@Override
			public void onItemClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onItemLongClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
				// TODO Auto-generated method stub
				return false;
			}
			
			
		});
	}

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_demo;
	}
	
	private List<Map<String, Object>> createDemoData(){
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		for(int i = 0 ; i < 3; i++){
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("Name", "Name" + i);
			itemMap.put("Sex", "Sex" + i);
			itemMap.put("Value", "Value" + i);
			itemMap.put("Proc", "Proc" + i);
			itemMap.put("DHideCol", "DHideCol" + i);
			data.add(itemMap);
		}
		return data;
	}

	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return null;
	}
}
