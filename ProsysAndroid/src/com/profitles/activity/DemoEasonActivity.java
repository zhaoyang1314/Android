package com.profitles.activity;

import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.cusviews.view.MyReadBQ;


public class DemoEasonActivity extends AppFunActivity {

	private MyReadBQ  etx_Tese,etx_Tese2,etx_Tese3;
	
	@Override
	protected void pageLoad() {
		etx_Tese =(MyReadBQ) findViewById(R.id.etx_Tese);
		etx_Tese2 =(MyReadBQ) findViewById(R.id.etx_Tese2);
		etx_Tese3 =(MyReadBQ) findViewById(R.id.etx_Tese3);
	}

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_demoeason;
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
