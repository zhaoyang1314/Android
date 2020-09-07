package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.OnTabChangeListener;

import com.profitles.biz.FjBiz;
import com.profitles.biz.PpvBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.cusviews.view.MyTextView;


public class FjActivity extends AppFunActivity {

	private MyEditText fjReq,fjQoh,fjTotFj,fjAdvBox,fjBox,fjUnit,fjScat,fjFz;
	private MyTextView fjUm,fjDESC;
	private MyReadBQ  fjNbr,fjLocBin,fjPart;
	private MySpinner  fjLot;
	private MyTabHost fjAdv;
	private LinearLayout lltPkTotReq,lltPkAdv,lltPkActPk;
	private MyDataGrid gdvPkTotReq,gdvPkAdv,gdvPkActPk;
	private ApplicationDB applicationDB;
	private FjBiz fjBiz;
	private String domain,site;
	private boolean onPageLoad = true;
	
	private List<Map<String , Object>> reqList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> advList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> pkList = new ArrayList<Map<String , Object>>();
	
	@Override
	protected void pageLoad() {
		fjBiz = new FjBiz();
		
		fjAdv		= 	((MyTabHost) findViewById(R.id.tbl_pkAdv));
		fjAdv.setup();
		fjAdv.setOnTabChangedListener(new OnTabChangeListener() {
			@Override	
			public void onTabChanged(String tabId) {
				if(tabId.equals("GetPkAdv")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							gdvPkAdv.buildData(advList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}else if(tabId.equals("GetPkActPk")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							gdvPkActPk.buildData(pkList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}
			}
		});
		
		fjNbr 		= 	(MyReadBQ) findViewById(R.id.etx_FJNBR);
		fjLocBin 	= 	(MyReadBQ) findViewById(R.id.etx_FJBIN );
		fjPart 	 	= 	(MyReadBQ) findViewById(R.id.etx_FJPART);
		
		fjLot 		= 	(MySpinner) findViewById(R.id.spn_FJLOT );
		
		fjReq 		= 	(MyEditText) findViewById(R.id.etx_FJREQ );
		fjQoh 		= 	(MyEditText) findViewById(R.id.etx_FJQOH);
		fjTotFj 	= 	(MyEditText) findViewById(R.id.etx_FJTOTFJ );
		fjAdvBox 	= 	(MyEditText) findViewById(R.id.etx_FJADVBOX );
		fjBox 		= 	(MyEditText) findViewById(R.id.etx_FJBOX);
		fjUnit 		= 	(MyEditText) findViewById(R.id.etx_FJUNIT);
		fjScat 		= 	(MyEditText) findViewById(R.id.etx_FJSCAT );
		fjFz 		= 	(MyEditText) findViewById(R.id.etx_FJFZ );
		fjUm 		= 	(MyTextView) findViewById(R.id.txv_FJUM );
		fjDESC		= 	(MyTextView) findViewById(R.id.txv_FJDESC );
		
		lltPkTotReq = 	(LinearLayout) findViewById(R.id.llt_pkTotReq) ; 
		lltPkAdv 	= 	(LinearLayout) findViewById(R.id.llt_pkAdv) ; 
		lltPkActPk 	= 	(LinearLayout) findViewById(R.id.llt_pkActPk) ; 
		
		gdvPkTotReq = 	(MyDataGrid) findViewById(R.id.gdv_pkTotReq) ;
		gdvPkAdv 	= 	(MyDataGrid) findViewById(R.id.gdv_pkAdv) ; 
		gdvPkActPk 	= 	(MyDataGrid) findViewById(R.id.gdv_pkActPk) ;
		
		domain 		= 	ApplicationDB.user.getUserDmain();
		site 		= 	ApplicationDB.user.getUserSite();
		
		//当你选中批次栏位中的一个条目时触发事件
		fjLot.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if (onPageLoad) {
					onPageLoad = false;
				} else {
					checkFJLot();
				}
			};
			public void onNothingSelected(AdapterView<?> parent){};
		});
	}

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_fj;
	}
	
	//鼠标离开事件
	protected boolean onBlur(int id, View v) {
		if (R.id.etx_FJNBR == id) {
			if(!"".equals(fjNbr.getValStr())){
				NbrBlur();
			}
		}
		if (R.id.etx_FJBIN == id) {
			if(!"".equals(fjLocBin.getValStr())){
				LocBinBlur();
			}
		}
		if (R.id.etx_FJPART == id) {
			if(!"".equals(fjPart.getValStr())){
				PartBlur();
			}
		}
		return true;
	}
	
	//扫描单号失焦后触发事件
	public void NbrBlur(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return fjBiz.fj_nbr(domain, fjNbr.getValStr());
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					getFocues(fjLocBin, true);	//光标停留
				}else{
					fjNbr.setText("");
					getFocues(fjNbr, true);	//光标停留
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}
	
	//扫描仓储失焦后触发事件
	public void LocBinBlur(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if("".equals(fjNbr.getValStr())){
					return false;
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return fjBiz.fj_locbin(domain, site, fjNbr.getValStr(), fjLocBin.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					getFocues(fjPart, true);	//光标停留
				}else{
					fjLocBin.setText("");
					getFocues(fjLocBin, true);	//光标停留
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}
	
	//扫描零件后失焦触发事件
	public void PartBlur(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if("".equals(fjNbr.getValStr())||"".equals(fjLocBin)){
					return false;
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return fjBiz.fj_part(domain,site,fjNbr.getValStr(), fjLocBin.getValStr(), fjPart.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					List<Map<String,Object>> nbrlist=((WebResponse) data).getDataToList();
					//将list集合转化成String类型的数组,因为批次是下拉框所有要求放数组类型
					//手动输入零件获取到的批次可能有多条
					List<String> list2 = new ArrayList<String>();
					for (int i = 0; i < nbrlist.size(); i++) {
						    list2.add(nbrlist.get(i).get("XSRFTRD_LOT")+"");
					}
					String[] ub = list2.toArray(new String[list2.size()]); 
				    fjLot.addAndClearItems(ub);
				    //*****************************************************
				    fjReq.setText(nbrlist.get(0).get("XSRFTRD_QTY")+"");
				    fjQoh.setText(nbrlist.get(0).get("XSLD_QTY_OH")+"");
				    fjAdvBox.setText(nbrlist.get(0).get("JX")+"");
				    fjUnit.setText(nbrlist.get(0).get("RFPTV_MULT_BOX")+"");
				    fjFz.setText(nbrlist.get(0).get("xsrftr_to_loc")+"");
				    getFocues(fjBox, true);	//光标停留
				}else{
					fjPart.setText("");
					getFocues(fjPart, true);	//光标停留
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}
	
	//选择中批次触发事件
	public void checkFJLot(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if("".equals(fjNbr.getValStr())||"".equals(fjLocBin.getValStr())||"".equals(fjPart.getValStr())){
					return false;
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return fjBiz.fj_lot(domain,site,fjNbr.getValStr(), fjLocBin.getValStr(), fjPart.getValStr(),fjLot.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
				}else{
					getFocues(fjPart, true);	//光标停留
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}
	

	@Override
	protected void unLockNbr() {		
		
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
}
