package com.profitles.activity;

import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;

import com.profitles.biz.RftriqBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyDateView;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

public class CustPartSerActivity extends AppFunActivity {

	private RftriqBiz pbiz;
	private MyEditText etx_cust,etx_part,etx_part_desc,etx_cust_part;
	private MyDataGrid dtgRftriqConsInfo;
	private ApplicationDB applicationDB;
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_custpart;		
	}
	
	@Override
	protected void pageLoad() {
		pbiz = new RftriqBiz();
		etx_cust = (MyEditText)findViewById(R.id.etx_cust); // 客户
		etx_part_desc = (MyEditText)findViewById(R.id.etx_part_desc); // 零件描述
		etx_part = (MyEditText)findViewById(R.id.etx_rftriqPart); // 零件
		etx_cust_part = (MyEditText)findViewById(R.id.etx_cust_part); // 客户图号
		dtgRftriqConsInfo = (MyDataGrid)findViewById(R.id.dtg_rftriqConsInfo);
	}

	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		return istrue;
	
	}
	
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ ButtonType.Search};
	}
	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		/*if(StringUtil.isEmpty(etx_part.getValStr()+"")){
			etx_part.setText("");
		}else if(StringUtil.isEmpty(etx_cust.getValStr())){
			etx_cust.setText("");
		}else if(StringUtil.isEmpty(etx_part_desc.getValStr())){
			etx_part_desc.setText("");
		}else if(StringUtil.isEmpty(etx_cust_part.getValStr())){
			etx_cust_part.setText("");
		}*/
		return true;
	}
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return pbiz.getSearchPartCust(applicationDB.user.getUserDmain(), etx_part.getValStr(), etx_cust.getValStr(), etx_part_desc.getValStr(), etx_cust_part.getValStr());
	}

	@Override
	public void OnBtnSerCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
			if(wrs.isSuccess()){
				List<Map<String, Object>> dataToList = wrs.getDataToList();
				dtgRftriqConsInfo.buildData(dataToList);
			}else{
				getFocues(etx_cust, true);
				showMessage(wrs.getMessages());
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