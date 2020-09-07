package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.TrlBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class TrLActivity extends AppFunActivity{

	private MyEditText  ext_trBin,etx_sendNbr,etx_sendPart,etx_sendPartDesc,etx_sendQty,etx_sendUnit,etx_sendCus,etx_bnum;
	//private MyTextView actPartum,actpartname,actUnit,actSCount,actQoh;
	private MyReadBQ ext_trBar,ext_trToBin,ext_trBin2;
	private MyDataGrid rflot_SN; 
	private MySpinner actLot;
	private TrlBiz trlbiz;
	private String domain, site,qty,fmloc,fmbin;
	private boolean actNum;
	private String ComeinScat;
	private String GetoutScat;
	private String vend=null,lot=null,status="",ycLoc="";
	private boolean onPageLoad = true;
	
	private ApplicationDB applicationDB;
	private List<Map<String , Object>> List = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_trl;
	}
	@Override
	protected void pageLoad() {
		ext_trBin    = (MyEditText) findViewById(R.id.ext_trBin);
		ext_trBar    = (MyReadBQ) findViewById(R.id.ext_trBar);
		etx_sendNbr    = (MyEditText) findViewById(R.id.etx_sendNbr);
		etx_sendPart    = (MyEditText) findViewById(R.id.etx_sendPart);
		etx_sendPartDesc    = (MyEditText) findViewById(R.id.etx_sendPartDesc);
		etx_sendQty    = (MyEditText) findViewById(R.id.etx_sendQty);
		etx_sendUnit    = (MyEditText) findViewById(R.id.etx_sendUnit);
		etx_sendCus    = (MyEditText) findViewById(R.id.etx_sendCus);
		etx_bnum    = (MyEditText) findViewById(R.id.etx_bnum);
		ext_trToBin    = (MyReadBQ) findViewById(R.id.ext_trToBin);
		rflot_SN =(MyDataGrid) findViewById(R.id.rflot_SN);
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		ext_trBin2    = (MyReadBQ) findViewById(R.id.ext_trBin2);
		ycLoc = "";	
		ext_trBin2.setVisibility(View.GONE); //暂时隐藏扫描的子项
		ext_trBar.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!"".equals( ext_trBar.getValStr().toString().trim())){
					fmBarOnBlur();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		ext_trBin2.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!"".equals( ext_trBar.getValStr().toString().trim()) && !"".equals( ext_trBin2.getValStr().toString().trim())){
					trBinSOnBlur();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
	}
	
	
	Boolean istrue=true;
	
	/**
	 * 光标离开时触发的事件
	 */
	protected boolean onBlur(int id, View v) {
//		if(R.id.ext_trBin == id){
//			if(!"".equals(ext_trBin.getValStr())){
//				fmBinOnBlur();
//			}
//			runClickFun();
//		}else
		if(R.id.ext_trBar == id){
			if(!"".equals(ext_trBar.getValStr())){
				fmBarOnBlur();
			}
			runClickFun();
		}
		else if(R.id.ext_trToBin == id){
			if(!"".equals(ext_trBar.getValStr())){
				fmBinOnBlur();
			}
			runClickFun();
		}
		return true;
	}
	
	private boolean notNull(){
		if(StringUtil.isEmpty(ext_trBin.getValStr())){
			if(StringUtil.isEmpty(ext_trBin2.getValStr())){
				showErrorMsg("源储不能为空！");
				return false;
			}
		}else if(StringUtil.isEmpty(etx_sendPart.getValStr())){
			showErrorMsg("零件不能为空！");
			return false;
		}else if(StringUtil.isEmpty(ext_trBar.getValStr()) ){
			showErrorMsg("托标签不能为空！");
			return false;
		}
//		else if(StringUtil.parseInt(etx_bnum.getValStr())>0){
//			showErrorMsg("数量不能为零且大于0！");
//			return false;
//		}
		else if(StringUtil.isEmpty(ext_trToBin.getValStr())){
			showErrorMsg("至储不能为空！");
			return false;
		}
		return true;
	}
	
	//页面添加按钮
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit};
	}
	
	//提交按钮
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
//		getFocues(actScat, true);
		boolean gg = notNull();
		if(gg == false){
			return false;
		}
		if(ext_trToBin.getValStr().toUpperCase().equals(ext_trBin.getValStr().toUpperCase())){
			showErrorMsg("源储与至储不能相同!");
			ext_trToBin.reValue();
			return false;
		}
		if(ext_trToBin.getValStr().toUpperCase().equals(ext_trBin2.getValStr().toUpperCase())){
			showErrorMsg("源储与至储不能相同!");
			ext_trToBin.reValue();
			return false;
		}
		fmBinOnBlur();
		if(!istrue){
			return false;
		}
		return true;
	}
	
	public Object OnBtnSubClick(ButtonType btnType, View v) {
			String tobin = ext_trToBin.getValStr(); //至储
			String part = etx_sendPart.getValStr(); //零件
			String partname = etx_sendPartDesc.getValStr(); //零件号描述
			//String lot2 = lot;   //批次 
			//String vend2=vend;  //供应商
			String scount = etx_bnum.getValStr(); //数量  
			String um = etx_sendUnit.getValStr();  //单位
			
			String code = ext_trBar.getValStr();  //托标签

			return trlbiz.trl_submit(domain, site,fmbin,fmloc,tobin,part,lot,scount,um,partname,code,vend,ApplicationDB.user.getUserId(),ApplicationDB.user.getUserDate(),ycLoc);
	}	
	
	public void OnBtnSubCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
		if(wrs.isSuccess()){
			ext_trBin.setText("");
			ext_trToBin.setText("");
			etx_sendPart.setText(""); 
			etx_sendPartDesc.setText(""); 
			etx_sendQty.setText("");  
			etx_sendUnit.setText("");  
			ext_trBar.setText("");  
			etx_sendCus.setText("");  
			etx_bnum.setText(""); 
			fmloc = "";
			fmbin = "";
			ycLoc = "";
			ext_trBin2.reValue();
			ext_trBin.setVisibility(View.VISIBLE); 
	    	ext_trBin2.setVisibility(View.GONE);
			getFocues(ext_trBin, true);
			rflot_SN.buildData(List);
			showSuccessMsg(wrs.getMessages());
		}else{
			showErrorMsg(wrs.getMessages());
		}
	}
	
	/**
	 * 失焦判断仓储是否存在
	 */
	public void fmBinOnBlur(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				trlbiz = new TrlBiz();
				return trlbiz.trl_fmBinonBlur(ext_trToBin.getValStr(),domain, site);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse rep = (WebResponse) data;
				if(rep.isSuccess()){
					//showErrorMsg(rep.getMessages());
				}else{
					showErrorMsg(rep.getMessages());
					getFocues(ext_trBin, true);
					istrue = false;
				}
			}
		});
	}
	
	/**
	 * 失焦判断扫描的源储是否存在
	 */
	public void trBinSOnBlur(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				trlbiz = new TrlBiz();
				return trlbiz.trBinSonBlur(ext_trBin2.getValStr(),domain, site);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse rep = (WebResponse) data;
				if(rep.isSuccess()){
					ycLoc = "1";
					fmloc = ext_trBin2.getValStr();
					fmbin = "";
					getFocues(ext_trToBin, true);
				}else{
					showErrorMsg(rep.getMessages());
					fmloc = "";
					fmbin = "";
					ext_trBin2.reValue();
					getFocues(ext_trBin2, true);
					istrue = false;
				}
			}
		});
	}	
	
	/**
	 * 托标签是否存在
	 */
	public void fmBarOnBlur(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				trlbiz = new TrlBiz();
				return trlbiz.trl_CheckBar(domain, site,ext_trBar.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse) data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					    //status=map.get("RFLOT_SCAN_STATUS")+"";
					    if(StringUtil.isEmpty(map.get("RFLOT_SCAN_STATUS"))){
					    etx_sendPart.setText(map.get("RFLOT_PART")+"");
					    etx_sendPartDesc.setText(map.get("RFLOT_PART_DESC")+"");
					    etx_sendCus.setText(map.get("RFLOT_CUST_PART")+"");
					    if(StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY")) > 0){
					    	map.put("RFLOT_MULT_QTY", map.get("RFLOT_SCATTER_QTY"));
					    }
					    etx_sendQty.setText(map.get("RFLOT_MULT_QTY")+"");
					    
					    etx_sendUnit.setText(map.get("RFLOT_UM")+"");
					    //etx_bnum.setText(map.get("RFLOT_ITEM_QTY")+"");
					    lot=map.get("RFLOT_LOT")+"";
					    vend=map.get("RFLOT_VEND")+"";
					    if(!StringUtil.isEmpty(map.get("RFLOT_SCAN_TYPE"))){
					    	qty=map.get("RFLOT_MULT_QTY")+"";
					    }
					    if(map.containsKey("RFLOT_LOC") && !StringUtil.isEmpty(map.get("RFLOT_LOC"))){
					    	ext_trBin.setVisibility(View.VISIBLE); 
					    	ext_trBin2.setVisibility(View.GONE);
					    	if(StringUtil.isEmpty(map.get("RFLOT_BIN"))){
						    	 ext_trBin.setText(map.get("RFLOT_LOC")+"");
						    }else{
						    	 ext_trBin.setText(map.get("RFLOT_BIN")+"");
						    }
					    	fmloc = StringUtil.isEmpty(map.get("RFLOT_LOC"))?"":map.get("RFLOT_LOC")+"";
							fmbin = StringUtil.isEmpty(map.get("RFLOT_BIN"))?"":map.get("RFLOT_BIN")+"";
					    }else{  //库位为null或者空说明是退料移库 
					    	ext_trBin2.setVisibility(View.VISIBLE); 
					    	ext_trBin.setVisibility(View.GONE);
					    	getFocues(ext_trBin2, true);
					    }
					    checkSNAll();
					    
					    }else{
							showErrorMsg("此托标签未完成拼托,不能进行移库操作!");
							getFocues(ext_trBin, true);
							istrue = false;
					    }

				}else{
					showErrorMsg(wr.getMessages());
					getFocues(ext_trBin, true);
					istrue = false;
				}
			}
		});
	}
	
	
	//托标签查询出所有关联单件标签
	private void checkSNAll() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return trlbiz.trl_CheckSnList(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),ext_trBar.getValStr(),"");
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					List<Map<String, Object>>list=(List<Map<String, Object>>) wr.getDataToList();
					rflot_SN.buildData(list);
					
				    if(list!=null&&list.size()>0){
				    	if(!StringUtil.isEmpty(qty)){
				    		etx_bnum.setText(qty+"");
				    	}else{
				    	 etx_bnum.setText(list.size()+"");
				    	}
				    }else{
				    	
				    	if(!StringUtil.isEmpty(qty)){
				    		etx_bnum.setText(qty+"");
				    	}else{
				    		etx_bnum.setText("0");
				    	}
						showErrorMsg("此托标签未完成拼托,不能进行移库操作!");
						getFocues(ext_trBin, true);
						istrue = false;
				    }
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(ext_trBar, true);
					istrue = false;
				}
			}
		});
	}
	
	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
		
	}

}
