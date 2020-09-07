package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.profitles.biz.TrBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTextView;

public class TrActivity extends AppFunActivity{

	private MyEditText actBox,actScat,actPart;
	private MyTextView actPartum,actpartname,actUnit,actSCount,actQoh;
	private MyReadBQ actBar,actToBin,actFmBin;
	private MySpinner actLot;
	private TrBiz trbiz;
	private String domain, site;
	private boolean actNum;
	private String ComeinScat;
	private String GetoutScat;
	private String vend;
	private boolean onPageLoad = true;
	
	/*	actFmBin 源储   actBar    扫码    actPart     零件             actLot    批次 
	 *	actUnit  每箱   actToBin  至储    actSCount   总量             actScat   散量
	 *	actBox   箱数   actPartum 单位    actpartname 零件号描述  actQoh	     库存
	 **/
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_tr;
	}
	@Override
	protected void pageLoad() {
		actFmBin    = (MyReadBQ) findViewById(R.id.ext_trBin);
		actBox      = (MyEditText) findViewById(R.id.ext_trBox);
		actScat     = (MyEditText) findViewById(R.id.ext_trScat);
		actToBin    = (MyReadBQ) findViewById(R.id.ext_trToBin);
		actPart     = (MyEditText) findViewById(R.id.ext_trParts);
		actPartum   = (MyTextView) findViewById(R.id.txv_trUm);
		actpartname = (MyTextView) findViewById(R.id.txv_trPartname);
		actUnit		= (MyTextView) findViewById(R.id.txv_trUnit);
		actSCount	= (MyTextView) findViewById(R.id.txv_trSCount);
		actQoh		= (MyTextView) findViewById(R.id.txv_trQoh);
		actBar		= (MyReadBQ) findViewById(R.id.ext_trBar);
		actLot      = (MySpinner) findViewById(R.id.spn_trLot);
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		runClickFun();
		
		actLot.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (onPageLoad) {
					onPageLoad = false;
				} else {
					LotChange();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
	
	/**
	 * 光标进入时触发的事件
	 */
	protected boolean onFocus(int id, View v) {
		if(R.id.ext_trBox == id){
			if(actBox.getValStr().equals("0")){
				actBox.setText("");
			}
		}else if(R.id.ext_trScat == id){
			ComeinScat = actScat.getValStr();
			if("0".equals(actScat.getValStr())||"".equals(actScat.getValStr())){
				actScat.setText("");
			}
		}else if(R.id.ext_trParts == id){
			if(!"".equals(actQoh.getValStr())){
				if(!"".equals(actBar.getValStr())){
					getFocues(actBar, true);
				}
			}
		}
		return true;
	}
	
	/**
	 * 光标离开时触发的事件
	 */
	protected boolean onBlur(int id, View v) {
		if(R.id.ext_trBin == id){
			showSuccessMsg("");
			if(!"".equals(actFmBin.getValStr())){
				fmBinOnBlur("FmBin");
			}
			runClickFun();
		}else if(R.id.ext_trBar == id){
			if(!"".equals(actBar.getValStr())){
				if(!"".equals(actFmBin.getValStr())){
					BarNotNull();
				}else{
					showErrorMsg("请输入源储！");
					getFocues(actFmBin, true);
				}
			}
			runClickFun();
		}else if(R.id.ext_trParts == id){
			if(!"".equals(actPart.getValStr())){
				if("".equals(actBar.getValStr()) && !"".equals(actFmBin.getValStr())){
					PartNotnullByBarNull();
				}else if("".equals(actFmBin.getValStr())){
					showErrorMsg("请输入源储！");
					getFocues(actFmBin, true);
				}
			}
			runClickFun();
		}else if(R.id.ext_trBox == id){
			if("".equals(actBox.getValStr())){
				actBox.setText("0");
			}
			float scount = getAtlqty();
			if (actNum) {
				actSCount.setText(String.valueOf(scount));
			} else {
				showErrorMsg("移库数量不得超过库存数量！");
				getFocues(actBox, true);
			}
			runClickFun();
		}else if(R.id.ext_trScat == id){
			if("".equals(actScat.getValStr())){
					actScat.setText("0");
			}else{
				GetoutScat = actScat.getValStr();
				if(!GetoutScat.equals(ComeinScat)){
					getScat();
				}
			}
			runClickFun();
		}else if(R.id.ext_trToBin == id){
			if(!"".equals(actToBin.getValStr())){
				fmBinOnBlur("ToBin");
			}
		}else if(R.id.spn_trLot == id){
			if("".equals(actBar.getValStr())){
				LotChange();
			}
			runClickFun();
		}
		return true;
	}
	
	/**
	 * 获取库存数量和总数量(箱数*每箱数+散量)进行验证判断
	 * 1.总数量不能大于库存数量
	 * 再返回总数量
	 */
	private float getAtlqty(){
		float numQc = Float.valueOf(actQoh.getValStr());
		float nums = Float.valueOf(actBox.getValStr())*Float.valueOf(actUnit.getValStr())+Float.valueOf(actScat.getValStr());
		if (numQc >= nums) {
			actNum = true;
		} else {
			actNum = false;
		}
		return nums;
	}
	private void getScat(){
		float scat = Float.valueOf(actScat.getValStr());
		float unit = Float.valueOf(actUnit.getValStr());
		float scount = Float.valueOf(actBox.getValStr())*Float.valueOf(actUnit.getValStr())+Float.valueOf(actScat.getValStr());
		float qoh = Float.valueOf(actQoh.getValStr());
		if(scat > unit){
			showErrorMsg("散量不得超过每箱数！");
			actScat.setText(ComeinScat);
			getFocues(actScat, true);
		}else{
			if(scount>qoh){
				showErrorMsg("移库数量不得超过库存数量！");
				getFocues(actScat, true);
			}
			String str = String.valueOf(scount);
			actSCount.setText(str);
		}
	}
	
	
	private boolean notNull(){
		if("".equals(actFmBin.getValStr())){
			showErrorMsg("源储不能为空！");
			return false;
		}else if("".equals(actPart.getValStr())){
			showErrorMsg("零件不能为空！");
			return false;
		}else if("".equals(actLot.getValStr())){
			showErrorMsg("批次不能为空！");
			return false;
		}else if("0".equals(actUnit.getValStr())){
			showErrorMsg("每箱数不能为零！");
			return false;
		}else if("".equals(actToBin.getValStr())){
			showErrorMsg("至储不能为空！");
			return false;
		}else if("0.0".equals(actSCount.getValStr())){
			showErrorMsg("移动库存数不能为零！");
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
		return true;
	}
	
	public Object OnBtnSubClick(ButtonType btnType, View v) {
			String fmbin = actFmBin.getValStr();
			String tobin = actToBin.getValStr();
			String part = actPart.getValStr();
			String lot = actLot.getValStr();
			String scount = actSCount.getValStr();
			String um = actPartum.getValStr();
			String partname = actpartname.getValStr();
			String code = actBar.getValStr();
			return trbiz.tr_submit(domain, site,fmbin,tobin,part,lot,scount,um,partname,code,vend,ApplicationDB.user.getUserId(),ApplicationDB.user.getUserDate());
	}	
	
	public void OnBtnSubCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
		if(wrs.isSuccess()){
			actFmBin.setText("");
			actBar.setText("");
			actPart.setText(""); 
			actUnit.setText(""); 
			actToBin.setText("");  
			actSCount.setText("");  
			actScat.setText("");  
			actBox.setText("");
			actPartum.setText("");
			actpartname.setText("");
			actQoh.setText("");
			actLot.clearItems();
			getFocues(actFmBin, true);	//光标停留
			showSuccessMsg(wrs.getMessages());
		}else{
			showErrorMsg(wrs.getMessages());
		}
	}
	
	/**
	 * 扫码不为空情况下，失焦获得所需数据
	 */
	private void BarNotNull(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				actSCount.setText("0");
				return true; 
			}
			
			@Override
			public WebResponse onGetData() {
				return trbiz.tr_scan(domain, site, actFmBin.getValStr() ,actBar.getValStr());
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					Map<String,Object> nbrlist=((WebResponse) data).getDataToMap();
					actPart.setText(nbrlist.get("RFLOT_PART")+"");
				    //将list集合转化成String类型的数组,因为批次是下拉框所有要求放数组类型
				    List<String> list2 = new ArrayList<String>();
				    list2.add(nbrlist.get("RFLOT_LOT")+"");
				    String[] ub = list2.toArray(new String[list2.size()]); 
				    actLot.addAndClearItems(ub);
				    //*****************************************************
					actPartum.setText(nbrlist.get("RFLOT_UM")+"");
					actpartname.setText(nbrlist.get("RFLOT_PART_DESC")+"");
					actUnit.setText(nbrlist.get("RFLOT_MULT_QTY")+"");
					actQoh.setText(nbrlist.get("XSLD_QTY_OH")+"");
					actScat.setText("0");
					vend = nbrlist.get("RFLOT_VEND")+"";
					float nums = 0;
					if(Float.valueOf(actQoh.getValStr())>=Float.valueOf(actUnit.getValStr())){
						actBox.setText("1");
						nums = Float.valueOf(1)*Float.valueOf(nbrlist.get("RFLOT_MULT_QTY")+"");
					}else{
						actBox.setText("0");
					}
					actSCount.setText(nums+"");
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
	public void fmBinOnBlur(final String toTag){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				trbiz = new TrBiz();
				if(toTag.equals("FmBin")){
					return trbiz.tr_fmBinonBlur(actFmBin.getValStr(),domain, site);
				}
				return trbiz.tr_fmBinonBlur(actToBin.getValStr(),domain, site);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse rep = (WebResponse) data;
				if(!rep.isSuccess()){
					getFocues(toTag.equals("ToBin") ? actFmBin : actToBin, true);
					showErrorMsg(rep.getMessages());
				}else{
					if(toTag.equals("ToBin")) runClickFun();
				}
			}
		});
	}
	
	/**
	 * 手动输入零件,且扫码为空时,判断零件是否存在
	 * 若存在则带出相关数据
	 */
	public void PartNotnullByBarNull(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				TrBiz trBiz = new TrBiz();
				return trBiz.tr_part(domain, site, actPart.getValStr(),actFmBin.getValStr());
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
						    list2.add(nbrlist.get(i).get("XSLD_LOT")+"");
					}
					String[] ub = list2.toArray(new String[list2.size()]); 
				    actLot.addAndClearItems(ub);
				    //*****************************************************
					actPartum.setText(nbrlist.get(0).get("XSPT_UM")+"");
					actpartname.setText(nbrlist.get(0).get("RFLOT_PART_DESC")+"");
					actUnit.setText(nbrlist.get(0).get("RFLOT_MULT_QTY")+"");
					actQoh.setText(nbrlist.get(0).get("XSLD_QTY_OH")+"");
					actScat.setText("0");
					vend = nbrlist.get(0).get("RFLOT_VEND")+"";
					float nums = 0;
					if(Float.valueOf(actQoh.getValStr())>=Float.valueOf(actUnit.getValStr())){
						actBox.setText("1");
						nums = Float.valueOf(1)*Float.valueOf(nbrlist.get(0).get("RFLOT_MULT_QTY")+"");
					}else{
						actBox.setText("0");
					}
					actSCount.setText(nums+"");
				}else { 
					getFocues(actPart, true);	//光标停留
					showErrorMsg(wrs.getMessages());
				}
			}
			
		});
	}
	
	/**
	 * 多条批次情况下，选择某条批次每箱，库存发生改变
	 */
	public void LotChange(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				TrBiz trBiz = new TrBiz();
				return trBiz.tr_lot(domain, site, actPart.getValStr(), actLot.getValStr(), actFmBin.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse m = (WebResponse)data;
				if(m.isSuccess()){
					Map<String, Object> map = new HashMap<String, Object>();
					map=m.getDataToMap();
					actQoh.setText(map.get("XSLD_QTY_OH")+"");
					actUnit.setText(map.get("RFLOT_MULT_QTY")+"");
					float s = 0;
					if(Float.valueOf(actQoh.getValStr())>=Float.valueOf(actUnit.getValStr())){
						actBox.setText("1");
						s = Float.valueOf(actBox.getValStr()) * Float.valueOf(map.get("RFLOT_MULT_QTY")+"")+Float.valueOf(actScat.getValStr());
					}else{
						actBox.setText("0");
					}
					actSCount.setText(s+"");
					getFocues(actScat, true);	//光标停留
				}else{
					actQoh.setText("0");
					actBox.setText("0");
					actUnit.setText("0");
					actScat.setText("0");
					actSCount.setText("0");
					showErrorMsg(m.getMessages());
				}
			}
			
		});
	}
	@Override
	protected void unLockNbr() {
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
