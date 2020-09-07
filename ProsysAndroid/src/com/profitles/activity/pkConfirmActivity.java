package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class pkConfirmActivity extends AppFunActivity {

	private MyReadBQ actNbr;
	private MyEditText actQty;
	private com.profitles.biz.pkConfirmBiz pkcmBiz;
	private MyDataGrid dtg;
	private String domain, site,userid,fnceffdate,line="";
	private View vi;
	private int checkRowIndex;
	private List<Map<String, Object>> nbrlist = new ArrayList<Map<String, Object>>();

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pkconfirm;
	}

	@Override
	protected void pageLoad() {
		pkcmBiz = new com.profitles.biz.pkConfirmBiz();
		actNbr = (MyReadBQ) findViewById(R.id.etx_pkconfirmNbr);
		actNbr.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actNbr.getValStr().equals("")){
					BlurNBR();
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actQty = (MyEditText) findViewById(R.id.etx_pkconfirmQty);
		dtg = (MyDataGrid) findViewById(R.id.mdtg_pkconfirm);
		dtg.setOnMyDataGridListener(new OnMyDataGridListener() {
			
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowDatas) {
				return false;
			}
			
			public void onItemClick(View view, Object val,  int rowIndex, int colIndex ,Map<String, Object> rowData) {
				if(rowIndex == 0) return;
//				Map<String, Object> map = dtg.getRowDataByKey(rowIndex-1);
				if(vi != null){
					vi.setBackgroundColor(Color.TRANSPARENT);//清空上次点击行颜色
					vi.setBackgroundColor(checkRowIndex == 0  ? Color.TRANSPARENT : 
						(checkRowIndex%2 == 0 ? Color.TRANSPARENT: Color.parseColor(Constants.CHECK_ROW_COLOR)) );//清空上次点击行颜色
				}
				vi  = (View) view.getParent();		//保存获取到行View
				checkRowIndex = rowIndex;
				View vv = (View) view.getParent();		//获取到行View
				vv.setBackgroundColor(Color.YELLOW);	//更改选中行的背景色
				actQty.setText(rowData.get("RF_PK_QTY")+"");
				line = rowData.get("RF_LINE")+"";
			}


		});
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		userid = ApplicationDB.user.getUserId();
		fnceffdate = ApplicationDB.user.getUserDate();
		runClickFun();
	}

	protected void unLockNbr() {
	}
	
	boolean isSchBarSuc = false;
	protected boolean onBlur(int id, View v) {
		if(R.id.etx_pkconfirmNbr == id && isSchBarSuc){  
			runClickFun();
		}
		if(R.id.etx_pkconfirmQty == id && isSchBarSuc){  
			runClickFun();
		}
		return true;
	}
	
	private void BlurNBR(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return pkcmBiz.pkConfirm_nbr(domain, site, actNbr.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					nbrlist=((WebResponse) data).getDataToList();
					dtg.buildData(nbrlist);
					actQty.setText("0");
					isSchBarSuc = true;
				}else{
					showErrorMsg(wr.getMessages());
					isSchBarSuc = false;
				}
			}
		});
	}	
	
	// 页面添加按钮
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[] { ButtonType.Submit,ButtonType.Save};
	}
	
	// 保存
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(StringUtil.parseFloat(!actQty.getValStr().equalsIgnoreCase("") ? actQty.getValStr() : "0") > 0
				&& !StringUtil.isEmpty(line)){
			return true;
		}
		showErrorMsg("请选中行数据或输入正确修改数量");
		return false;
	}

	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return pkcmBiz.onItemClickUpQty(domain, site, actNbr.getValStr(), line,actQty.getValStr());
	}

	public void OnBtnSaveCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
		if (wrs.isSuccess()) {
			BlurNBR();
			showSuccessMsg(wrs.getMessages());
		}else{
			showErrorMsg(wrs.getMessages());
		}
	}

	// 提交按钮
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		showSuccessMsg("");
		if(nbrlist == null || nbrlist.size() < 1){
			showErrorMsg("请您先查询出数据在确认提交!");
			return false;
		}
		return true;
	}

	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return pkcmBiz.pkConfirm_submit(domain, site, actNbr.getValStr(),userid,fnceffdate);
	}

	public void OnBtnSubCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
		if (wrs.isSuccess()) {
			nbrlist = null;
			dtg.buildData(nbrlist);
			actNbr.setText("");
			actQty.setText("");
			showSuccessMsg(wrs.getMessages());
		} else {
			showErrorMsg(wrs.getMessages());
		}
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
