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

import com.profitles.biz.JhwrkBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.util.StringUtil;

public class JhwrkActivity extends AppFunActivity{

	private MyEditText actBox,actScat,actPart,actUm,actDesc,actUnit,actSCount,actLot,actVend;
	private MyReadBQ actBar,actLocBin;
	private MySpinner actReason;
	private JhwrkBiz jBiz;
	private String domain, site, userid, loc , bin , rfptv_rcvloc_stu,fnceffdate;
	private boolean onPageLoad = true;
	
	/*	actFmBin 源储   actBar    扫码    actPart     零件             actLot    批次 
	 *	actUnit  每箱   actToBin  至储    actSCount   总量             actScat   散量
	 *	actBox   箱数   actPartum 单位    actpartname 零件号描述  actQoh	     库存
	 **/
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_jhwrk;
	}
	@Override
	protected void pageLoad() {
		jBiz = new JhwrkBiz();
		actBox      = (MyEditText) findViewById(R.id.ext_jhBox);
		actScat     = (MyEditText) findViewById(R.id.ext_jhScat);
//		actScat.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if(!"".equals(actScat.getValStr())){
//					jisuan();
//				}
//			}
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//			public void afterTextChanged(Editable s) {
//			}
//		});
		actPart     = (MyEditText) findViewById(R.id.ext_jhParts);
		actVend     = (MyEditText) findViewById(R.id.ext_jhVend);
		actUm   	= (MyEditText) findViewById(R.id.etx_jhUm);
		actDesc 	= (MyEditText) findViewById(R.id.etx_jhDesc);
		actUnit		= (MyEditText) findViewById(R.id.etx_jhUnit);
		actSCount	= (MyEditText) findViewById(R.id.ext_jhSCount);
		actLot      = (MyEditText) findViewById(R.id.ext_jhLot);
		actReason   = (MySpinner)  findViewById(R.id.spn_jhReason);
		actBar		= (MyReadBQ)   findViewById(R.id.ext_jhBar);
		actLocBin   = (MyReadBQ)   findViewById(R.id.ext_jhLocBin);
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid		= ApplicationDB.user.getUserId();
		fnceffdate  = ApplicationDB.user.getUserDate();
		actReason.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (onPageLoad) {
					onPageLoad = false;
				} else {
					
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		OnLoadReason();
	}
	
	/**
	 * 光标进入时触发的事件
	 */
	protected boolean onFocus(int id, View v) {
		if(R.id.ext_jhBox == id){
			if("0".equals(actBox.getValStr())){
				actBox.setText("");
			}
		}
		if(R.id.ext_jhScat == id){
			if("0".equals(actScat.getValStr())){
				actScat.setText("");
			}
		}
		return true;
	}
	
	/**
	 * 光标离开时触发的事件
	 */
	protected boolean onBlur(int id, View v) {
		if(R.id.ext_jhBar == id){
			if(!"".equals(actBar.getValStr())){
				BarNotNull();
			}else{
				actVend.setEnabled(true);
				actPart.setEnabled(true);
			}
			runClickFun();
		}
		if(R.id.ext_jhLocBin == id){
			if(!"".equals(actLocBin.getValStr())){
				LocBinOnBlur();
			}
			runClickFun();
		}
		if(R.id.ext_jhBox == id){
			if(!"".equals(actBox.getValStr())){
				jisuan();
			}else{
				actBox.setText("0");
			}
			runClickFun();
		}
		if(R.id.ext_jhScat == id){
			if("".equals(actScat.getValStr())){
				actScat.setText("0");
			}
			runClickFun();
		}
		if(R.id.ext_jhParts == id){
			if(!"".equals(actPart.getValStr())){
				OnLoadPart();
			}
			runClickFun();
		}
		if(R.id.ext_jhLot == id){
			runClickFun();
		}
		if(R.id.ext_jhVend == id){
			if("".equals(actBar.getValStr())){
				SfCzVend();
			}
			runClickFun();
		}
		return true;
	}
	
	/**
	 * 扫码不为空情况下，失焦获得所需数据
	 */
	private void BarNotNull(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				clearMsg();
				return true; 
			}
			
			@Override
			public WebResponse onGetData() {
				return jBiz.jhwrk_scan(domain, site, actBar.getValStr());
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					Map<String, Object> map = new HashMap<String, Object>();
					map=wrs.getDataToMap();
					if(map.get("LBS").equals("L")){
						actBox.setText("0");
						actScat.setText("0");
						actSCount.setText("0");
					}else{
						float unit = StringUtil.parseFloat(map.get("RFLOT_MULT_QTY"));
						float scat = StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY"));
						actBox.setText("1");
						actScat.setText("0");
						if(scat > 0){
							actScat.setText(scat+"");
							actBox.setText("0");
						}
						actSCount.setText((unit * StringUtil.parseFloat(actBox.getValStr()) + scat) + "");
						actScat.setEnabled(false);
						actBox.setEnabled(false);
					}
					actVend.setText(map.get("RFLOT_VEND")+"");
					actPart.setText(map.get("RFLOT_PART")+"");
					actDesc.setText(map.get("PART_DESC")+"");
					actUm.setText(map.get("RFLOT_UM")+"");
					actUnit.setText(map.get("RFLOT_MULT_QTY")+"");
					actLot.setText(map.get("RFLOT_LOT")+"");
					actVend.setEnabled(false);
					actPart.setEnabled(false);
					getFocues(actLocBin, true);	//光标停留
				}else { 
					getFocues(actBar, true);	//光标停留
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}
	
	/**
	 * 失焦判断仓储是否存在
	 */
	public void LocBinOnBlur(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				clearMsg();
				return true;
			}

			@Override
			public Object onGetData() {
				return jBiz.jhwrk_locbin(actLocBin.getValStr(), domain, site);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse rep = (WebResponse) data;
				if(rep.isSuccess()){
					Map<String, Object> map = rep.getDataToMap();
					loc = map.get("LOC")+"";
					bin = map.get("BIN")+"";
				}else{
					showErrorMsg(rep.getMessages());
					getFocues(actLocBin, true);	//光标停留
				}
			}
		});
	}
	
	/**
	 * 原因数据加载
	 */
	public void OnLoadReason(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return jBiz.jhwrk_OnLoadReason(domain);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse rep = (WebResponse) data;
				if(rep.isSuccess()){
					List<Map<String,Object>> nbrlist=((WebResponse) data).getDataToList();
					List<String> list2 = new ArrayList<String>();
					for (int i = 0; i < nbrlist.size(); i++) {
						    list2.add(nbrlist.get(i).get("CODE_DESC")+"");
					}
					String[] ub = list2.toArray(new String[list2.size()]); 
					actReason.addAndClearItems(ub);
				}else{
					showErrorMsg(rep.getMessages());
				}
			}
		});
	}
	
	/**
	 * 扫码为空操作
	 */
	public void OnLoadPart(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if(!"".equals(actBar.getValStr())){
					return false;
				}
				if("".equals(actVend.getValStr())){
					showErrorMsg("请先输入供应商!");
					return false;
				}
				return true;
			}
			
			@Override
			public Object onGetData() {
				return jBiz.jhwrk_part(domain, site, actVend.getValStr(),actPart.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse rep = (WebResponse) data;
				if(rep.isSuccess()){
					Map<String, Object> map = new HashMap<String, Object>();
					map=rep.getDataToMap();
					actDesc.setText(map.get("XSPT_DESC1")+"");
					actUm.setText(map.get("XSPT_UM")+"");
					actUnit.setText(map.get("RFPTV_MULT_BOX")+"");
					actBox.setText("0");
					actScat.setText("0");
					actSCount.setText("0");
					rfptv_rcvloc_stu = map.get("RFPTV_RCVLOC_STU")+"";
				}else{
					getFocues(actPart, true);	//光标停留
					showErrorMsg(rep.getMessages());
				}
			}
		});
	}
	
	//计算
	public void jisuan(){
		float Scount =  Float.valueOf(actBox.getValStr()) * Float.valueOf(actUnit.getValStr())
					+ Float.valueOf(actScat.getValStr());
		actSCount.setText(Scount+"");
		runClickFun();
	}
	
	//验证供应商是否存在
	public void SfCzVend(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return jBiz.jhwrk_vend(domain, userid, actVend.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse rep = (WebResponse) data;
				if(!rep.isSuccess()){
					getFocues(actVend, true);	//光标停留
					showErrorMsg(rep.getMessages());
				}
			}
		});
	}
	
	
	
	
	
		// 提交按钮
		@Override
		public boolean OnBtnSubValidata(ButtonType btnType, View v) {
			boolean gg = notNull();
			if(gg == false){
				return false;
			}
			return true;
		}

		@Override
		public Object OnBtnSubClick(ButtonType btnType, View v) {
			return jBiz.jhwrk_submit(domain, site, loc, bin, 
					actPart.getValStr(), actLot.getValStr(),
					actSCount.getValStr(), actUm.getValStr(),
					actDesc.getValStr(), actBar.getValStr(), 
					actVend.getValStr(), userid,actReason.getValStr(),
					rfptv_rcvloc_stu,actScat.getValStr(),
					actUnit.getValStr(),actBox.getValStr(),fnceffdate
					);
		}

		@Override
		public void OnBtnSubCallBack(Object data) {
			WebResponse wrs = (WebResponse) data;
			if(wrs.isSuccess()){
				showSuccessMsg(wrs.getMessages());
				actBar.setText("");
				actVend.setText("");
				actPart.setText("");
				actDesc.setText("");
				actUm.setText("");
				actUnit.setText("");
				actLot.setText("");
				actBox.setText("");
				actScat.setText("");
				actSCount.setText("");
				actLocBin.setText("");
			}else{
				showErrorMsg(wrs.getMessages());
			}
		}
		
		private boolean notNull(){
			if("".equals(actLocBin.getValStr())){
				showErrorMsg("仓储不能为空！");
				return false;
			}else if("".equals(actPart.getValStr())){
				showErrorMsg("零件不能为空！");
				return false;
			}else if("".equals(actLot.getValStr())){
				showErrorMsg("批次不能为空！");
				return false;
			}else if("".equals(actVend.getValStr())){
				showErrorMsg("供方不能为空！");
				return false;
			}else if(StringUtil.parseFloat(actSCount.getValStr()) == 0){
				showErrorMsg("总数量不能为零！");
				return false;
			}
			return true;
		}
	
	@Override
	protected void unLockNbr() {
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
