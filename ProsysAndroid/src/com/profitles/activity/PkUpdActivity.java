package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
 










import com.google.zxing.common.StringUtils;
import com.profitles.biz.PkUpdBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class PkUpdActivity extends AppFunActivity {
	private PkUpdBiz pkupdbiz;
	private MyTextView txvPkUnit,txvPkMultQty;
	private MyEditText etxPkPart,etxPkUm,
					   etxPkPartDesc,etxPkReq,etxPkUnit, etxPkMultPk,etxPkTotPk,
					   etxPkQtyDif,etxPkQoh,etxPkBox,etxPkadvBox,etxPkScat,
					   etxPkQty,etxPkAdvBox,etxPkAd,etxPkYLot;
	private MyReadBQ  etxPkNbr,etxPkBin,etxPkBar,etxPkCmScan;
	private MySpinner  spnPkLot;
	private MyTabHost tbl_pkAdv;
	private LinearLayout lltPkTotReq,lltPkAdv,lltPkActPk;
	private MyDataGrid gdvPklist, gdvPkTotReq,gdvPkAdv,gdvPkActPk;
	private String biaoshi="0", nextBarMsg = "", vend , plSet = "",needCheck="" , endCm="",cust="",LBS="",cmPart="" , isLot = "0";;
	private List<Map<String , Object>> reqList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> advList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> pkList = new ArrayList<Map<String , Object>>();
	private float act=0;
	private ApplicationDB applicationDB;
	private boolean onPageLoad = true;
	private View backRow;
	private int checkRowIndex;
	private int isBcScan = 0; //系统中是否已存在客签   1存在  0不存在
	private String isQuX = "0" ; // 0 代表不是取消带出信息， 1 代表 取消。 如果为 1 则不去添加昭和历史纪录表
	private Map<String , String> dttype =  new HashMap<String, String>(){{
												put("SP", "SP");
												put("TP", "TP");
											}};
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pkupd;
	} 

	@SuppressLint("CutPasteId")
	@Override 
	protected void pageLoad() {
		tbl_pkAdv= ((MyTabHost) this.findViewById(R.id.tbl_pkAdv));
		tbl_pkAdv.setup();
		tbl_pkAdv.setOnTabChangedListener(new OnTabChangeListener() {
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
		plSet = "";
		pkupdbiz = new PkUpdBiz();
		etxPkNbr = (MyReadBQ) findViewById(R.id.etx_pkNbr);
		etxPkBin = (MyReadBQ) findViewById(R.id.etx_pkBin);
		etxPkBar = (MyReadBQ) findViewById(R.id.etx_pkBar);
		etxPkCmScan = (MyReadBQ) findViewById(R.id.etx_pkCmScan); // 客户标签
		
		etxPkCmScan.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etxPkCmScan.getValStr() && !"".equals( etxPkCmScan.getValStr().toString().trim())
						&& null != etxPkBar.getValStr() && !"".equals( etxPkBar.getValStr().toString().trim())/* && isBcScan == 0*/){
					 checkPkCmScan();
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			public void afterTextChanged(Editable s) {
			}
		});
		txvPkMultQty = (MyTextView) findViewById(R.id.txv_pkMultPk);
		txvPkUnit=(MyTextView) findViewById(R.id.txv_pkUnit);
		lltPkTotReq = (LinearLayout) findViewById(R.id.llt_pkTotReq) ; 
		lltPkAdv = (LinearLayout) findViewById(R.id.llt_pkAdv) ; 
		lltPkActPk = (LinearLayout) findViewById(R.id.llt_pkActPk) ; 
		
		gdvPklist = (MyDataGrid) findViewById(R.id.gdv_pklist) ;
		reBindPkList();	//执行分拣单查询
		
		gdvPklist.setOnMyDataGridListener(new OnMyDataGridListener() {
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowDatas) {
				return false;
			}
			public void onItemClick(View view, Object val,  int rowIndex, int colIndex ,Map<String, Object> rowData) {
				if(rowIndex == 0) return;
//				Map<String, Object> map = dtg.getRowDataByKey(rowIndex-1);
				if(backRow != null){ 
					backRow.setBackgroundColor(checkRowIndex == 0  ? Color.TRANSPARENT : 
						(checkRowIndex%2 == 0 ? Color.TRANSPARENT: Color.parseColor(Constants.CHECK_ROW_COLOR)) );//清空上次点击行颜色
				}
				backRow  = (View) view.getParent();		//保存获取到行View
				checkRowIndex = rowIndex;
				View vv = (View) view.getParent();		//获取到行View
				vv.setBackgroundColor(Color.YELLOW);	//更改选中行的背景色
				etxPkNbr.setText(rowData.get("RFPKL_NBR")+"");
				getFocues(etxPkBin, true);
				checkNbr();//单号事件
				//runClickFun();
			}
		});
		
		gdvPkTotReq = (MyDataGrid) findViewById(R.id.gdv_pkTotReq) ;
		gdvPkAdv = (MyDataGrid) findViewById(R.id.gdv_pkAdv) ; 
		gdvPkActPk = (MyDataGrid) findViewById(R.id.gdv_pkActPk) ;
		
		etxPkPart = (MyEditText) findViewById(R.id.etx_pkPart );
		etxPkUm = (MyEditText) findViewById(R.id.etx_pkUm );
		etxPkPartDesc = (MyEditText) findViewById(R.id.etx_pkPartDesc );
		spnPkLot = (MySpinner) findViewById(R.id.spn_pkLot );
		etxPkReq = (MyEditText) findViewById(R.id.etx_pkReq );
		etxPkUnit = (MyEditText) findViewById(R.id.etx_pkUnit );
		etxPkMultPk = (MyEditText) findViewById(R.id.etx_pkMultPk);
		etxPkTotPk = (MyEditText) findViewById(R.id.etx_pkTotPk );
		etxPkQtyDif = (MyEditText) findViewById(R.id.etx_pkQtyDif );
		etxPkQoh = (MyEditText) findViewById(R.id.etx_pkQoh );
		etxPkBox = (MyEditText) findViewById(R.id.etx_pkBox );
		etxPkadvBox = (MyEditText) findViewById(R.id.etx_pkAdvBox );
		etxPkScat = (MyEditText) findViewById(R.id.etx_pkScat );
		etxPkQty = (MyEditText) findViewById(R.id.etx_pkQty );
		etxPkAdvBox= (MyEditText) findViewById(R.id.etx_pkAdvBox);
		etxPkAd= (MyEditText) findViewById(R.id.etx_pkAd);   //最终客户
		etxPkYLot= (MyEditText) findViewById(R.id.etx_pkYLot);   //优先批次
		
		biaoshi="0";
		act	 =0;
		updateUMVis(true);
		updateCmbarVix(true);
/*		spnPkLot.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if (onPageLoad) {	//页面加载
					onPageLoad = false;
				} else {
					//if(!isQuX.equals("1") && isLot.equals("0")){//  1：取消带出信息，不需要再次校验      isLot : 0 建议批次 ，再去校验， 1 非建议批次，不需要再次校验
						checkPkCLoc();//修改批次的方法
					//}
				}
			};
			public void onNothingSelected(AdapterView<?> parent){};
		});*/
		
	}

	private void updateUMVis(boolean flag) { 
		// true 显示每箱  false 显示已拣
		txvPkUnit.setVisibility(flag ? View.VISIBLE : View.GONE);
		etxPkUnit.setVisibility(flag ? View.VISIBLE : View.GONE);
	    txvPkMultQty.setVisibility(flag ? View.GONE : View.VISIBLE);
	    etxPkMultPk.setVisibility(flag ? View.GONE : View.VISIBLE); 
	}
	private void updateCmbarVix(boolean flag) { 
		etxPkCmScan.setVisibility(flag ? View.VISIBLE : View.GONE);
		etxPkCmScan.setEnabled(flag);
		etxPkCmScan.setFocusableInTouchMode(flag);
		// etxPkBox.setFocusable(flag);
	}
	// 获取分拣单列表
	private void reBindPkList(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return pkupdbiz.getPkList(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite());
			}
			
			@Override
			public void onCallBrack(Object data) {
				gdvPklist.buildData((List<Map<String,Object>>)data);
			}
		});
	}

	@Override
	protected boolean onFocus(int id, View v) {
		return super.onFocus(id, v);
	}

	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		// 单号
		if(etxPkNbr.getId() == id){
			runClickFun();
			if(null != etxPkNbr.getValStr() && !"".equals( etxPkNbr.getValStr().toString().trim())){
				 checkNbr();
			}else{
				istrue = true;
			}
		}
		//储位
		if(etxPkBin.getId() == id){
			runClickFun();
			if(null != etxPkBin.getValStr() && !"".equals( etxPkBin.getValStr().toString().trim())){
				checkPkBin();  
			}else{
				istrue = true;
			}
		}
		//条码
		if(etxPkBar.getId() == id){
			runClickFun();
			if(null != etxPkBar.getValStr() && !"".equals( etxPkBar.getValStr().toString().trim())){
				 checkPkBar(); 
			}else{
				istrue = true;
			}
		}
		//客户条码
		if(etxPkCmScan.getId() == id){
			runClickFun();
			/*if(null != etxPkCmScan.getValStr() && !"".equals( etxPkCmScan.getValStr().toString().trim())){
				 checkPkCmScan();
				 runClickFun();
			}else{
				istrue = true;
			}*/
		} 
		if(etxPkBox.getId() == id){
				runClickFun();
		}
		if(etxPkMultPk.getId()==id){
				runClickFun();
		}
		if(etxPkScat.getId() == id){
			runClickFun();
		}
		return istrue ;
	}
	
	//单号事件
	private void checkNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return pkupdbiz.pkCheckNbr(applicationDB.user.getUserDmain() , applicationDB.user.getUserSite() ,etxPkNbr.getValStr().toString() , applicationDB.user.getUserId() , applicationDB.user.getMac());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					if(map!=null){
						plSet = map.get("PLSET")+"";
						if(!StringUtil.isEmpty(map.get("RFLAD_LOT"))){
							String Ylot = (String) map.get("RFLAD_LOT"); //获取单号事件之后的批次 002
							String oldLot = etxPkYLot.getValStr();		 //客户再次点击其它单号，先获取上次批次 null
							if(!Ylot.equals(oldLot)){
								etxPkYLot.setText(Ylot); //单号带出的优先批次，赋值
							}
						}
						
						// 添加销售分拣单的 部分信息
						needCheck = map.containsKey("needCheck") ? map.get("needCheck")+"" : "0" ; 
						updateCmbarVix("1".equals(needCheck));
						endCm = map.containsKey("ENDCM") ? map.get("ENDCM")+"" : " " ; 
						cust = map.containsKey("CM") ? map.get("CM")+"" : " " ; 
						etxPkAd.setText( (map.containsKey("ENDCM") ? map.get("ENDCM")+"" : " "  ) + "-" 
								+ (map.containsKey("ENDCMNM") ?  map.get("ENDCMNM").toString() :" " ));
						
						if("1".equals(map.get("isHavaAct").toString())){
							// 存在已拣信息是否情况 弹出框中处理       确定 则查询所有信息
							showConfirm("单号存在扫描记录是否继续扫(取消会把当前单号所有保存记录清除)?", scfListenPk);
						}else{
						    setChangeTable(map);
						    //清除页面信息
						    etxPkBar.reValue();
							etxPkPart.reValue();
							etxPkPartDesc.reValue();
							etxPkUm.reValue();
							etxPkReq.reValue();
							etxPkUnit.reValue();
							etxPkQoh.reValue();
							etxPkTotPk.reValue();
							etxPkQtyDif.reValue();
							etxPkAdvBox.reValue();
							etxPkBox.reValue();
							etxPkScat.reValue();
							etxPkQty.reValue();
							spnPkLot.clearItems();
							etxPkMultPk.reValue();
							etxPkCmScan.reValue();
							// 是否给建议储位赋值 
							if("1.0".equals(applicationDB.Ctrl.getString("RFC_SETBIN_BYNBR", "0").toString())){ 
								getFocues(etxPkBar, true);
							}
							istrue = true;
							runClickFun();
						}
					}else{
						showErrorMsg(getResources().getString(R.string.pk_getfalse)); 
						istrue = false;
					}
					  tbl_pkAdv.setCurrentTab(1);
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etxPkNbr, true);
					istrue = false;
				}
			}
		});
	}

	//给页面信息Grid 赋值
	private void setChangeTable(Map<String, Object> map) {
		reqList =  (List<Map<String, Object>>) map.get("reqList"); 
		advList =  (List<Map<String, Object>>) map.get("advList");
		pkList =  (List<Map<String, Object>>) map.get("pkList");
		//gdvPkTotReq.clearData();
		//gdvPkAdv.clearData();
		//gdvPkActPk.clearData();
		gdvPkTotReq.buildData(reqList);
		//gdvPkAdv.buildData(advList);
//		gdvPkActPk.buildData(pkList);
		nextBarMsg = "零件："+(StringUtil.isEmpty(map.get("RFLAD_PART"))?"":map.get("RFLAD_PART")) + " 批次:" + (StringUtil.isEmpty(map.get("RFLAD_LOT"))?"":map.get("RFLAD_LOT")) + " 数量:" + (StringUtil.isEmpty(map.get("RFLAD_QTY_REQ"))?"":map.get("RFLAD_QTY_REQ"));
		if(!StringUtil.isEmpty(map.get("RFLAD_LOT"))){
			String Ylot = (String) map.get("RFLAD_LOT"); //获取单号事件之后的批次 002
			String oldLot = etxPkYLot.getValStr();		 //客户再次点击其它单号，先获取上次批次 null
			if(!Ylot.equals(oldLot)){
				etxPkYLot.setText(Ylot);
			}
		}
		// 是否给建议储位赋值 
		String advbin = map.containsKey("advBin") ?
				(("null".toLowerCase()).equals((StringUtil.isEmpty(map.get("advBin"),"").toLowerCase()))?"":map.get("advBin")+"")
				:""; 
		if("1.0".equals(applicationDB.Ctrl.getString("RFC_SETBIN_BYNBR", "0").toString())){  
			etxPkBin.setText(advbin) ;  
		}
		showWarningMsg("建议储位:"+(advbin)+" "+(nextBarMsg==null?"":nextBarMsg));
	}
	
	//获取的是 页面加载信息   304行调用，单号存在扫描记录是否继续扫(取消会把当前单号所有保存记录清除)
	private OnShowConfirmListen scfListenPk=new OnShowConfirmListen() {
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pkupdbiz.checkPkNbrTemp(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), applicationDB.user.getUserId(), applicationDB.user.getMac(), "false");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
					  Map<String, Object> map=wr.getDataToMap();
					  setChangeTable(map);
					  //清除页面信息
						etxPkBar.reValue();
						etxPkPart.reValue();
						etxPkPartDesc.reValue();
						etxPkUm.reValue();
						etxPkReq.reValue();
						etxPkUnit.reValue();
						etxPkQoh.reValue();
						etxPkTotPk.reValue();
						etxPkQtyDif.reValue();
						etxPkAdvBox.reValue();
						etxPkBox.reValue();
						etxPkScat.reValue();
						etxPkQty.reValue();
						spnPkLot.clearItems();
						etxPkMultPk.reValue();
						etxPkCmScan.reValue();
					  
					// 是否给建议储位赋值 
						if("1.0".equals(applicationDB.Ctrl.getString("RFC_SETBIN_BYNBR", "0").toString())){ 
							getFocues(etxPkBar, true);
						}
					  runClickFun();
					}else{
						showErrorMsg(wr.getMessages());
						istrue = false;
					}
				}
			});	
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pkupdbiz.checkPkNbrTemp(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), applicationDB.user.getUserId(), applicationDB.user.getMac(), "true");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String, Object> map=wr.getDataToMap();
						setChangeTable(map);
						// 是否给建议储位赋值 
						if("1.0".equals(applicationDB.Ctrl.getString("RFC_SETBIN_BYNBR", "0").toString())){ 
							getFocues(etxPkBar, true);
						}
					}else{
						showErrorMsg(wr.getMessages());
						istrue = false;
					}
				}
			});	
		}
	};
	
	
	//储位事件    532中的 538行调用，点击确定
	private OnShowConfirmListen scfListenPk2=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			//确定(用户选择了继续)则记录异常操作日志中rfifo_log中
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pkupdbiz.pkExcLocBin(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxPkNbr.getValStr().toString(), etxPkBin.getValStr().toString(),"PkActivity",applicationDB.user.getUserId());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					String meg=(String) wr.getWData();
					if(wr.isSuccess()){
					/*	if(isQuX.equals("1")){ //如果是取消 带出信息，则定位到 散量
							getFocues(etxPkMultPk, true); //
						}else{*/
							//仓储校验,如果 条码不为空,则光标定位到 etxBox
							if(StringUtil.isEmpty(etxPkBar.getValStr())){
								getFocues(etxPkBar, true); //
							}else{
								getFocues(etxPkBox, true);
							}
						//}
						runClickFun();
						istrue = true;
					}else{
						showErrorMsg(wr.getMessages());
						istrue = false;
					}
				}
			});	
			
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			//取消则回到该栏位位置选中该栏位值
			etxPkBar.reValue();
			etxPkPart.reValue();
			etxPkPartDesc.reValue();
			etxPkUm.reValue();
			etxPkReq.reValue();
			etxPkUnit.reValue();
			etxPkQoh.reValue();
			etxPkTotPk.reValue();
			etxPkQtyDif.reValue();
			etxPkAdvBox.reValue();
			etxPkBox.reValue();
			etxPkScat.reValue();
			etxPkQty.reValue();
			spnPkLot.clearItems();
			etxPkMultPk.reValue(); 
			etxPkCmScan.reValue();
			LBS="";
			cmPart="";
			getFocues(etxPkBar, true);
			istrue=false;
		}
	};
	
	
	//批次事件   条码时调用的      当前批次不在分配明细中 
	private OnShowConfirmListen scfListenPkLot=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pkupdbiz.pkCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), etxPkBin.getValStr().toString(), etxPkBar.getValStr().toString(), "1");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String ,Object> map = wr.getDataToMap();
						vend = map.get("RFLOT_VEND")+""; 
						// chackPkBarBack_lot(map); 
						chackPkBarBack(map); 
						showWarningMsg(wr.getMessages());
						runClickFun();
					}else{
						showErrorMsg(wr.getMessages());
						getFocues(etxPkBar, true);
						istrue = false;
					}
				}
			});
			
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			//取消则回到该栏位位置选中该栏位值
			etxPkBar.reValue();
			etxPkPart.reValue();
			etxPkPartDesc.reValue();
			etxPkUm.reValue();
			etxPkReq.reValue();
			etxPkUnit.reValue();
			etxPkQoh.reValue();
			etxPkTotPk.reValue();
			etxPkQtyDif.reValue();
			etxPkAdvBox.reValue();
			etxPkBox.reValue();
			etxPkScat.reValue();
			etxPkQty.reValue();
			spnPkLot.clearItems();
			etxPkMultPk.reValue(); 
			etxPkCmScan.reValue();
			LBS="";
			cmPart="";
			getFocues(etxPkBar, true);
			istrue=false;
		}
	};
	
	private OnShowConfirmListen scfListenPkDltScan=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定 撤销之前的分拣记录
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pkupdbiz.pkDltScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),
							etxPkNbr.getValStr().toString(), etxPkBar.getValStr().toString(),  applicationDB.user.getUserId(),applicationDB.user.getMac());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String, Object> map=wr.getDataToMap();
						setChangeTable(map);//确定撤销删除数据之后，下面信息进行重新获取
						etxPkBar.reValue();
						getFocues(etxPkBar, true);
						istrue = true;
					}else{
						showErrorMsg(wr.getMessages());
						getFocues(etxPkBar, true);
						istrue = false;
					}
				}
			});
			
		}
		
		@Override 
		public void onCancelClick() {  //询问框  点取消
			//取消则返回界面，并带出分拣信息 包括 客签以及分拣的的记录
			etxPkBar.reValue();
			etxPkPart.reValue();
			etxPkPartDesc.reValue();
			etxPkUm.reValue();
			etxPkReq.reValue();
			etxPkUnit.reValue();
			etxPkQoh.reValue();
			etxPkTotPk.reValue();
			etxPkQtyDif.reValue();
			etxPkAdvBox.reValue();
			etxPkBox.reValue();
			etxPkScat.reValue();
			etxPkQty.reValue();
			spnPkLot.clearItems();
			etxPkMultPk.reValue(); 
			etxPkCmScan.reValue();
			LBS="";
			cmPart="";
			//isBcScan= 0 ;
			getFocues(etxPkBar, true);
/*			loadDataBase(new IRunDataBaseListens() { 
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pkupdbiz.pkShowMess(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), etxPkBin.getValStr().toString(), etxPkBar.getValStr().toString(), biaoshi);
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String, Object> map=wr.getDataToMap();
						vend = map.get("RFLOT_VEND")+""; 
						//isBcScan = 1 ; //系统中是否已存在客签   1存在  0不存在
						chackPkBarBack(map); //条码离开赋予
						getFocues(etxPkMultPk, true);
					}else{
						showErrorMsg(wr.getMessages());
						getFocues(etxPkMultPk, true);
					}
				}
			});*/	
		}
	};
	//储位事件  
	private void checkPkBin() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return pkupdbiz.pkCheckLocBin(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString() ,etxPkBin.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					String meg=(String) wr.getWData();
					if("-1".equals(meg)){
						showConfirm(wr.getMessages(), scfListenPk2);
					}else{
						clearMsg();
						runClickFun();
						istrue=true;
					}
				}else{
					getFocues(etxPkBin, true);
					showErrorMsg(wr.getMessages());
					istrue = false;
				}
			}
		});
	}
	@Override
	protected void onChangedAft(int id, Editable s) {
		//箱数改变事件
		if(etxPkBox.getId()==id){ 
			if(null != etxPkBox.getValStr() && !"".equals( etxPkBox.getValStr().toString().trim()) && !"".equals(etxPkUnit.getValStr())){
				float Box =StringUtil.parseFloat(etxPkBox.getValStr()); 		//箱数
				float Unit =StringUtil.parseFloat(etxPkUnit.getValStr());   	//每箱
				float Qoh =StringUtil.parseFloat(etxPkQoh.getValStr());   	//库存
				if("".equals(etxPkScat.getValStr())){    //判断散量是否为空
					float Sum=(Box*Unit);
					if(Sum>Qoh){
//						istrue=false;
//						getFocues(etxPkBox, true);
//						etxPkQty.reValue();
//						showErrorMsg(getResources().getString(R.string.pk_save_Qty_false));
					}else{
						String part=etxPkPart.getValStr();
						if(reqList!=null || reqList.size()>0){
							for (int i = 0; i < reqList.size(); i++) {
								Map<String, Object> map=reqList.get(i);
								if(part.equals(map.get("RFPKLD_PART").toString())){
									act=StringUtil.parseFloat(map.get("QTY_ACT")+"");
								}
							}
						}
						//已检量 + 本次的分拣量 
						if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act<=
								//需求量 + 单包量 
								(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
							etxPkQty.setText(String.valueOf(Sum));
						}else{
							istrue=false;
							getFocues(etxPkBox, true);
							etxPkQty.reValue();
							showErrorMsg(getResources().getString(R.string.pk_save2_false));
						}
					}
				}else{
					float Scat =StringUtil.parseFloat(etxPkScat.getValStr());		//散量
					float Sum=(Box*Unit)+Scat;
					if(Sum>Qoh){
//						istrue=false;
//						getFocues(etxPkBox, true);
//						etxPkQty.reValue();
//						showErrorMsg(getResources().getString(R.string.pk_save_Qty_false));
					}else{
						String part=etxPkPart.getValStr();
						if(reqList!=null || reqList.size()>0){
							for (int i = 0; i < reqList.size(); i++) {
								Map<String, Object> map=reqList.get(i);
								if(part.equals(map.get("RFPKLD_PART").toString())){
									act=StringUtil.parseFloat(map.get("QTY_ACT")+"");
								}
							}
						}
						if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act<=(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
							etxPkQty.setText(String.valueOf(Sum));
						}else{
							getFocues(etxPkBox, true);
							etxPkQty.reValue();
							showErrorMsg(getResources().getString(R.string.pk_save2_false));
							istrue=false;
						}
					}
				}
			}else{
				etxPkQty.reValue();
				istrue = true;
			}
		}
		//散量改变事件
		if(etxPkScat.getId()==id){ 
			if(null != etxPkScat.getValStr() && !"".equals( etxPkScat.getValStr().toString().trim()) && !"".equals(etxPkUnit.getValStr())){
				float Unit =StringUtil.parseFloat(etxPkUnit.getValStr());   	//每箱
				float Scat =StringUtil.parseFloat(etxPkScat.getValStr());		//散量
				float Qoh =StringUtil.parseFloat(etxPkQoh.getValStr());   	//库存
				if(Scat<Unit){
					if("".equals(etxPkBox.getValStr())){
						float Sum=Scat;
						if(Sum>Qoh){
//							istrue=false;
//							getFocues(etxPkScat, true);
//							etxPkQty.reValue();
//							showErrorMsg(getResources().getString(R.string.pk_save_Qty_false));
						}else{
							String part=etxPkPart.getValStr();
							if(reqList!=null || reqList.size()>0){
								for (int i = 0; i < reqList.size(); i++) {
									Map<String, Object> map=reqList.get(i);
									if(part.equals(map.get("RFPKLD_PART").toString())){
										act=StringUtil.parseFloat(map.get("QTY_ACT")+"");
									}
								}
							}
							if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act<=(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
								etxPkQty.setText(String.valueOf(Sum));
							}else{
								istrue=false;
								getFocues(etxPkScat, true);
								etxPkQty.reValue();
								showErrorMsg(getResources().getString(R.string.pk_save2_false));
							}
						}
					}else{
						float Box =StringUtil.parseFloat(etxPkBox.getValStr()); 		//箱数
						float Sum=(Box*Unit)+Scat;
						if(Sum>Qoh){
//							istrue=false;
//							etxPkQty.reValue();
//							getFocues(etxPkScat, true);
//							showErrorMsg(getResources().getString(R.string.pk_save_Qty_false));
						}else{
							String part=etxPkPart.getValStr();
							if(reqList!=null || reqList.size()>0){
								for (int i = 0; i < reqList.size(); i++) {
									Map<String, Object> map=reqList.get(i);
									if(part.equals(map.get("RFPKLD_PART").toString())){
										act=StringUtil.parseFloat(map.get("QTY_ACT")+"");
									}
								}
							}
							if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act<=(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
								etxPkQty.setText(String.valueOf(Sum));
							}else{
								istrue=false;
								getFocues(etxPkScat, true);
								etxPkQty.reValue();
								showErrorMsg(getResources().getString(R.string.pk_save2_false));
							}
						}
					}
				}else{
					if(Scat>Qoh){
//						istrue=false;
//						getFocues(etxPkScat, true);
//						etxPkQty.reValue();
//						showErrorMsg(getResources().getString(R.string.pk_save_Qty_false));
					}else{
						getFocues(etxPkScat, true);
						etxPkQty.reValue();
						showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
						istrue = false;
					}
				}
			}else{
				etxPkQty.reValue();
				istrue = true;
			}
		}
		//分拣数改变事件
		if(etxPkMultPk.getId()==id){ 
			if(null != etxPkMultPk.getValStr() && !"".equals( etxPkMultPk.getValStr().toString().trim())){
				float Unit =StringUtil.parseFloat(etxPkUnit.getValStr());   	//每箱
				float Mult =StringUtil.parseFloat(etxPkMultPk.getValStr());   	//分拣
				if(Mult>Unit){
					istrue=false;
					etxPkQty.reValue();
					getFocues(etxPkMultPk, true);
					showErrorMsg(getResources().getString(R.string.pk_save_QtyBox_false));
				}else{
					etxPkQty.setText(Mult+"");
				}
			}else{
				etxPkQty.reValue();
				istrue = true;
			}
		}
	}
	
	//批次修改方法   建议从  0201 分拣 23
	private void checkPkCLoc() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return pkupdbiz.pkCheckChangeLot(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(),etxPkBin.getValStr().toString(),
						etxPkPart.getValStr().toString(),spnPkLot.getValStr().toString(), etxPkReq.getValStr().toString(), etxPkUnit.getValStr().toString(), biaoshi,vend);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					// -1 代表：不是非建议批次 
					if("-1".equals(map.get("Fal").toString())){
						// 当前批次不存在分配明细中
						showConfirm(wr.getMessages(), scfListenPkLot2);  // 取消： 则回到该栏位位置选中栏位置  etxPkBar
																		 // 确定： pkCheckChangeLot 将 biaoshi="1"   
					}else{
						float qtyOh =StringUtil.parseFloat(map.get("XSLD_QTY_OH").toString());
						etxPkQoh.setText(qtyOh+"");   //库存
						etxPkAdvBox.setText(map.get("TBOX").toString());//推荐箱数
						showWarningMsg(wr.getMessages());
					}
				}else{
					showErrorMsg(wr.getMessages());
					istrue = false;
				}
			}
		});
	}
	//当前批次不在分配明细中
	private OnShowConfirmListen scfListenPkLot2=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pkupdbiz.pkCheckChangeLot(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(),etxPkBin.getValStr().toString(),
							etxPkPart.getValStr().toString(),spnPkLot.getValStr().toString(), etxPkReq.getValStr().toString(), etxPkUnit.getValStr().toString(), "1",vend);
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String ,Object> map = wr.getDataToMap();
						float qtyOh =StringUtil.parseFloat(map.get("XSLD_QTY_OH").toString());
						etxPkQoh.setText(qtyOh+"");   //库存
						etxPkAdvBox.setText(map.get("TBOX").toString());//推荐箱数
						showWarningMsg(wr.getMessages());
					}else{
						showErrorMsg(wr.getMessages());
						istrue = false;
					}
				}
			});
			
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			//取消则回到该栏位位置选中该栏位值
			getFocues(etxPkBar, true);
			istrue=false;
		}
	};
	//条码 事件  
	private void checkPkBar() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if( !"1.0".equals(applicationDB.Ctrl.getString("RFC_CHGBIN_BYBAR", "0").toString() ) 
						&& (etxPkBin.getValStr().trim()).equals("")){
					showErrorMsg("请先输入建议储位!");
				}
				return true;
			}
			@Override
			public Object onGetData() {
				return pkupdbiz.pkCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), etxPkBin.getValStr().toString(), etxPkBar.getValStr().toString(), biaoshi);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					vend = map.get("RFLOT_VEND")+"";
					if(map.containsKey("isHaveScan") && "1".equals(map.get("isHaveScan")+"")){
						showConfirm(wr.getMessages(), scfListenPkDltScan);
					}else {
						if("-1".equals(map.get("Fal").toString())){
							isLot = "1";
							// 非建议批次是否继续	
							showConfirm(wr.getMessages(), scfListenPkLot);
						}else{
							isLot = "0";
							chackPkBarBack(map);
							showWarningMsg(wr.getMessages());
						}
					}
				}else{
					showErrorMsg(wr.getMessages());
					//etxPkBin.reValue();
					etxPkBar.reValue();
					etxPkPart.reValue();
					etxPkPartDesc.reValue();
					etxPkUm.reValue();
					etxPkReq.reValue();
					etxPkUnit.reValue();
					etxPkQoh.reValue();
					etxPkTotPk.reValue();
					etxPkQtyDif.reValue();
					etxPkAdvBox.reValue();
					etxPkBox.reValue();
					etxPkScat.reValue();
					etxPkQty.reValue();
					spnPkLot.clearItems();
					etxPkMultPk.reValue(); 
					etxPkCmScan.reValue();
					LBS="";
					cmPart="";
					//isBcScan= 0 ;
					getFocues(etxPkBar, true);
					istrue = false;
				}
			}
			
		});
	}
	//给文本赋予可编辑，聚焦
	private void setEtxFocusAble(Boolean bl){
		if(bl){
			etxPkBox.setEnabled(true);
			etxPkBox.setFocusableInTouchMode(true);
			etxPkBox.setFocusable(true);
			etxPkScat.setEnabled(true);
			etxPkScat.setFocusableInTouchMode(true);
			etxPkScat.setFocusable(true);
			
			etxPkMultPk.setEnabled(false);
			etxPkMultPk.setFocusable(false);
			etxPkMultPk.setFocusableInTouchMode(false);
		}else{
			etxPkBox.setEnabled(false);
			etxPkBox.setFocusable(false);
			etxPkBox.setFocusableInTouchMode(false);
			etxPkScat.setEnabled(false);
			etxPkScat.setFocusable(false);
			etxPkScat.setFocusableInTouchMode(false);
			
			etxPkMultPk.setEnabled(true);
			etxPkMultPk.setFocusableInTouchMode(true);
			etxPkMultPk.setFocusable(true);
		}
	}
	// 条码离开后带出提示 
	private void chackPkBarBack(Map<String, Object> map) {
		etxPkPart.setText(map.get("RFLOT_PART").toString());   //零件
		etxPkPartDesc.setText(map.get("PART_DESC").toString());	//零件名称
		etxPkUm.setText(map.get("RFLOT_UM").toString());		//单位
		String pkLot=applicationDB.Ctrl.getString("RFC_LOT_GETMODE", "1").toString();
		if("2.0".equals(pkLot)){
			ArrayList<String> list=(ArrayList<String>) map.get("lotList");
			String[] arrLot = (String[])list.toArray(new String[list.size()]);
			spnPkLot.addAndClearItems(arrLot);
		}else{
			spnPkLot.setEnabled(false);
			String lot = "";
			if(!"-".equals(map.get("RFLOT_LOT").toString())){
				lot = map.get("RFLOT_LOT").toString();
			}
			String[] arrLot=new String[]{lot};
			spnPkLot.addAndClearItems(arrLot);
		}
		LBS=map.get("RFPTV_LBS").toString(); 
		float boxQty =StringUtil.parseFloat(map.get("RFLOT_MULT_QTY").toString()); //每箱量
		float qTYREQ =StringUtil.parseFloat(map.get("QTYREQ").toString());
		etxPkReq.setText(qTYREQ+"");			//需求
		float qtyOh =StringUtil.parseFloat(map.get("XSLD_QTY_OH").toString());
		etxPkQoh.setText(qtyOh+"");   //库存
		float qtyAct =StringUtil.parseFloat(map.get("QTYACT").toString());
		etxPkTotPk.setText(qtyAct+"");  //已拣
		float qtyDif=StringUtil.parseFloat(map.get("QTYREQ").toString())-StringUtil.parseFloat(map.get("QTYACT").toString());
		etxPkQtyDif.setText("("+qtyDif+")"); //已拣后面()
		etxPkAdvBox.setText(map.get("TBOX").toString());//推荐箱数
		float qtyscat = StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY")+"");//散量
		String chgbin =applicationDB.Ctrl.getString("RFC_CHGBIN_BYBAR", "0").toString();
		if(LBS.equals("L")){ // 按批分拣
			updateUMVis(true); 
			setEtxFocusAble(true);
			etxPkUnit.setText(boxQty+""); 
			getFocues(etxPkBox, true);
			updateCmbarVix(false);
			if("1.0".equals(chgbin)){
				String newbin = StringUtil.isEmpty( map.get("LOCBIN"),"");
				String oldBin = null == etxPkBin.getValStr().toString() ? "" : etxPkBin.getValStr().toString().trim() ; 
				if( "".equalsIgnoreCase( oldBin) || !newbin.equalsIgnoreCase(oldBin)){
					etxPkBin.setText(newbin);
					//if(!isQuX.equals("1")){ //  1：取消带出信息，不需要再次校验
						checkPkBin();
					//}
				}
			} 
		}else if(LBS.equals("B") || LBS.equals("S")){ // 按箱 序分拣
			updateUMVis(false);
			etxPkUnit.setText(boxQty+""); 
		/*	if(!StringUtil.isEmpty(map.get("cmScan")) && map.containsKey("cmScan")){
				isBcScan = 1; //系统中是否已存在客签   1存在  0不存在
				isQuX = "1";	 // 0 代表不是取消带出信息， 1 代表 取消。 如果为 1 则不去添加昭和历史纪录表 
				etxPkCmScan.setText(map.get("cmScan")+""); 
			}else{
				isBcScan = 0;
				isQuX = "0";
			}*/
			setEtxFocusAble(false);
			if(qtyscat > 0){
				etxPkMultPk.setText(qtyscat+"");
			}else{
				etxPkMultPk.setText(boxQty+"");
			} 
			etxPkBox.setText("1");
			etxPkScat.setText("0"); 
			if("1.0".equals(chgbin)){
				String newbin = StringUtil.isEmpty( map.get("LOCBIN"),"");
				String oldBin = null == etxPkBin.getValStr().toString() ? "" : etxPkBin.getValStr().toString().trim() ; 
				if( "".equalsIgnoreCase( oldBin) || !newbin.equalsIgnoreCase(oldBin)){
					etxPkBin.setText(newbin);
					//if(!isQuX.equals("1")){//  1：取消带出信息，不需要再次校验
						checkPkBin();
					//}
				}
			} 
			if("1".equals(needCheck)){ 
				updateCmbarVix(true);
				etxPkCmScan.setFocusable(true);
				getFocues(etxPkCmScan, true); 
			}else{
				updateCmbarVix(false);
				getFocues(etxPkMultPk, true); 
			}
		}else{ // 其他
			updateUMVis(false);
			etxPkUnit.setText(boxQty+"");
			etxPkMultPk.setText(boxQty+"");
			if(qtyscat > 0){
				etxPkBox.setText("0");
				etxPkScat.setText(qtyscat+"");
			}else{
				etxPkBox.setText("1");
				etxPkScat.setText("0");
			}
//			etxPkBox.setEnabled(false);
			etxPkBox.setKeyListener(null);
			etxPkScat.setEnabled(false);
			getFocues(etxPkMultPk, true);
		}
	}
	private void chackPkBarBack_lot(Map<String, Object> map) {
		etxPkPart.setText(map.get("RFLOT_PART").toString());   //零件
		etxPkPartDesc.setText(map.get("PART_DESC").toString());	//零件名称
		etxPkUm.setText(map.get("RFLOT_UM").toString());		//单位
		String pkLot=applicationDB.Ctrl.getString("RFC_LOT_GETMODE", "1").toString();
		if("2.0".equals(pkLot)){
			ArrayList<String> list=(ArrayList<String>) map.get("lotList");
			String[] arrLot = (String[])list.toArray(new String[list.size()]);
			spnPkLot.addAndClearItems(arrLot);
		}else{
			spnPkLot.setEnabled(false);
			String[] arrLot=new String[]{map.get("RFLOT_LOT").toString()};
			spnPkLot.addAndClearItems(arrLot);
		}
		String LBS=map.get("RFPTV_LBS").toString();
		float boxQty =StringUtil.parseFloat(map.get("RFLOT_MULT_QTY").toString()); //每箱量
		float qTYREQ =StringUtil.parseFloat(map.get("QTYREQ").toString());
		etxPkReq.setText(qTYREQ+"");			//需求
		float qtyOh =StringUtil.parseFloat(map.get("XSLD_QTY_OH").toString());
		etxPkQoh.setText(qtyOh+"");   //库存
		float qtyAct =StringUtil.parseFloat(map.get("QTYACT").toString());
		etxPkTotPk.setText(qtyAct+"");  //已拣
		float qtyDif=StringUtil.parseFloat(map.get("QTYREQ").toString())-StringUtil.parseFloat(map.get("QTYACT").toString());
		etxPkQtyDif.setText("("+qtyDif+")"); //已拣后面()
		etxPkAdvBox.setText(map.get("TBOX").toString());//推荐箱数
		float qtyscat = StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY")+"");//散量
		if(LBS.equals("L")){
			updateUMVis(true);
			etxPkUnit.setText(boxQty+""); 
			getFocues(etxPkBox, true);
		}else if(LBS.equals("B")){
			updateUMVis(false);
			etxPkUnit.setText(boxQty+"");
			etxPkMultPk.setText(boxQty+"");
			etxPkBox.setText("1");
			etxPkScat.setText("0");
//			etxPkBox.setEnabled(false);
			etxPkBox.setKeyListener(null);
			etxPkScat.setEnabled(false);
			getFocues(etxPkMultPk, true);
		}else{
			updateUMVis(false);
			etxPkUnit.setText(boxQty+"");
			etxPkMultPk.setText(boxQty+"");
			if(qtyscat > 0){
				etxPkBox.setText("0");
				etxPkScat.setText(qtyscat+"");
			}else{
				etxPkBox.setText("1");
				etxPkScat.setText("0");
			}
//			etxPkBox.setEnabled(false);
			etxPkBox.setKeyListener(null);
			etxPkScat.setEnabled(false);
			getFocues(etxPkMultPk, true);
		}
	}
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Save,ButtonType.Submit,ButtonType.Help};
	}

	@Override
	public Object OnBtnHelpClick(ButtonType btnType, View v) {
		showSuccessMsg(R.string.help_ok);
		return  null ;
	}

	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals(etxPkNbr.getValStr().toString().trim())){
			if(!"".equals(etxPkQty.getValStr())){
				
				if(dttype.containsKey(plSet) && "1".equals(needCheck) && ("B".equalsIgnoreCase(LBS) ||"S".equalsIgnoreCase(LBS) )){
					if("".equals(etxPkNbr.getValStr().toString().trim())){
						showErrorMsg(getResources().getString(R.string.pk_sub_false));
						getFocues(etxPkNbr, true);
						istrue=false;
						return istrue;
					}
					if("".equals(etxPkBar.getValStr().toString().trim())){
						showErrorMsg(getResources().getString(R.string.pk_save_bar_false));
						getFocues(etxPkBar, true);
						istrue=false;
						return istrue;
					}
					if("".equals(etxPkCmScan.getValStr().toString().trim())){
						showErrorMsg(getResources().getString(R.string.pk_save_cmbar_false));
						getFocues(etxPkCmScan, true);
						istrue=false;
						return istrue;
					}
				}
				
				String part=etxPkPart.getValStr();
				if(reqList!=null || reqList.size()>0){
					for (int i = 0; i < reqList.size(); i++) {
						Map<String, Object> map=reqList.get(i);
						if(part.equals(map.get("RFPKLD_PART").toString())){
							act=StringUtil.parseFloat(map.get("QTY_ACT")+"");
						}
					}
				}
				
				if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act<=(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
					if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act<(StringUtil.parseFloat(etxPkReq.getValStr()))){
						istrue = false;
						showConfirm("该分拣单零件:"+etxPkPart.getValStr()+"没有分拣完成是否提交?", scfListenPkSub);
					}else {
						istrue = true;
					}
				}else{
					showErrorMsg(getResources().getString(R.string.pk_save2_false));
					getFocues(etxPkBin, true);
					istrue=false;
				}
			}else{
				float req=0;
				float act=0;
				if(reqList!=null || reqList.size()>0){
					for (int i = 0; i < reqList.size(); i++) {
						Map<String, Object> map=reqList.get(i);
						req=StringUtil.parseFloat(map.get("RFPKLD_QTY_REQ")+"");
						act=StringUtil.parseFloat(map.get("QTY_ACT")+"");
						if(act<req){
							istrue = false;
							showConfirm("该分拣单没有分拣完成是否提交?", scfListenPkSub);
							break;
						}
					}
				} 
			}	
		}else{
			showErrorMsg(getResources().getString(R.string.pk_sub_false));
			getFocues(etxPkNbr, true);
			istrue=false;
			
		}
		return istrue;
	}
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		String guid = StringUtil.getUUID();
		if(!"".equals(etxPkQty.getValStr().toString().trim())){
			String saveCmBar = "";
			if(dttype.containsKey(plSet) && "1".equals(needCheck) && ("B".equalsIgnoreCase(LBS) ||"S".equalsIgnoreCase(LBS) )){
				saveCmBar = "1";
			}
			return pkupdbiz.pkSubmit(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", etxPkPart.getValStr(), spnPkLot.getValStr(), 
				etxPkQty.getValStr(), applicationDB.user.getUserId(), etxPkUnit.getValStr(), etxPkBox.getValStr(), etxPkScat.getValStr(),
				etxPkBin.getValStr(), etxPkBar.getValStr(),applicationDB.user.getMac(),vend,applicationDB.user.getUserDate(),guid,plSet
				,cust,endCm,saveCmBar,etxPkCmScan.getValStr(),cmPart);
		}else{
			return pkupdbiz.pkSubmitByNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate(),guid,plSet);
		}
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxPkNbr.reValue();
			etxPkBin.reValue();
			etxPkBar.reValue();
			etxPkPart.reValue();
			etxPkPartDesc.reValue();
			etxPkUm.reValue();
			etxPkReq.reValue();
			etxPkUnit.reValue();
			etxPkQoh.reValue();
			etxPkTotPk.reValue();
			etxPkQtyDif.reValue();
			etxPkAdvBox.reValue();
			etxPkBox.reValue();
			etxPkScat.reValue();
			etxPkQty.reValue();
			spnPkLot.clearItems();
			etxPkMultPk.reValue();
			etxPkYLot.reValue();  //提交之后，清除 原批次（优先批次）
			reqList = null;
			gdvPkTotReq.buildData(reqList);
			gdvPkAdv.buildData(reqList);
			gdvPkActPk.buildData(reqList);
			getFocues(etxPkNbr, true);
			showSuccessMsg(wr.getMessages());
			
			reBindPkList();
			tbl_pkAdv.setCurrentTab(0);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxPkNbr, true);
		}
	}
	
	//保存按钮
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(!"".equals(etxPkQty.getValStr().toString().trim())){
			String part=etxPkPart.getValStr();
			if(reqList!=null || reqList.size()>0){
				for (int i = 0; i < reqList.size(); i++) {
					Map<String, Object> map=reqList.get(i);
					if(part.equals(map.get("RFPKLD_PART").toString())){
						act=StringUtil.parseFloat(map.get("QTY_ACT")+"");
					}
				}
			}
			if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act
					<=(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
				if(StringUtil.parseFloat(etxPkQty.getValStr())<=(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
				}else{
					showErrorMsg(getResources().getString(R.string.pk_save2_false));
					getFocues(etxPkBin, true);
					istrue=false;
				}
			}else{
				showErrorMsg(getResources().getString(R.string.pk_save2_false));
				getFocues(etxPkBin, true);
				istrue=false;
			}
		}else{
			showErrorMsg(getResources().getString(R.string.pk_save_false));
			getFocues(etxPkBin, true);
			istrue=false;
		}
		if(dttype.containsKey(plSet) && "1".equals(needCheck) && ("B".equalsIgnoreCase(LBS) ||"S".equalsIgnoreCase(LBS) )){
			if("".equals(etxPkNbr.getValStr().toString().trim())){
				showErrorMsg(getResources().getString(R.string.pk_sub_false));
				getFocues(etxPkNbr, true);
				istrue=false;
			}
			if("".equals(etxPkBar.getValStr().toString().trim())){
				showErrorMsg(getResources().getString(R.string.pk_save_bar_false));
				getFocues(etxPkBar, true);
				istrue=false;
			}
			if("".equals(etxPkCmScan.getValStr().toString().trim())){
				showErrorMsg(getResources().getString(R.string.pk_save_cmbar_false));
				getFocues(etxPkCmScan, true);
				istrue=false;
			}
		}
		if("B".equalsIgnoreCase(LBS)){// 如果是 箱标签
			//如果已检量 + 本次分拣量 大于需求量，则提示 是否保存
			if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr()) > StringUtil.parseFloat(etxPkReq.getValStr())){
				showConfirm("本次分拣量+已检量 大于需求量是否保存", scfListenThan); 
				istrue = false;
			}
		}
		return istrue;
	}
	
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		String saveCmBar = "";
		if(dttype.containsKey(plSet) && "1".equals(needCheck) && ("B".equalsIgnoreCase(LBS) ||"S".equalsIgnoreCase(LBS) ) ){
			saveCmBar = "1";
		}
		return pkupdbiz.pkSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", etxPkPart.getValStr(), spnPkLot.getValStr(), 
				etxPkQty.getValStr(), applicationDB.user.getUserId(), etxPkUnit.getValStr(), etxPkBox.getValStr(), etxPkScat.getValStr(),
				etxPkBin.getValStr(), etxPkBar.getValStr(),vend,cust,endCm,saveCmBar,etxPkCmScan.getValStr(),cmPart,etxPkReq.getValStr(),"PkUpdActivity",isQuX,etxPkYLot.getValStr());
	}
	
	//如果已检量 + 本次分拣量 大于需求量，则提示 是否保存
	private OnShowConfirmListen scfListenThan=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					String saveCmBar = "";
					if(dttype.containsKey(plSet) && "1".equals(needCheck) && ("B".equalsIgnoreCase(LBS) ||"S".equalsIgnoreCase(LBS) ) ){
						saveCmBar = "1";
					} 
					return pkupdbiz.pkSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", etxPkPart.getValStr(), spnPkLot.getValStr(), 
							etxPkQty.getValStr(), applicationDB.user.getUserId(), etxPkUnit.getValStr(), etxPkBox.getValStr(), etxPkScat.getValStr(),
							etxPkBin.getValStr(), etxPkBar.getValStr(),vend,cust,endCm,saveCmBar,etxPkCmScan.getValStr(),cmPart,etxPkReq.getValStr(),"PkUpdActivity",isQuX,etxPkYLot.getValStr());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse)data;
					if(wr.isSuccess()){
						Map<String ,Object> map = wr.getDataToMap();
						//etxPkBin.reValue();
						etxPkBar.reValue();
						etxPkPart.reValue();
						etxPkPartDesc.reValue();
						etxPkUm.reValue();
						etxPkReq.reValue();
						etxPkUnit.reValue();
						etxPkQoh.reValue();
						etxPkTotPk.reValue();
						etxPkQtyDif.reValue();
						etxPkAdvBox.reValue();
						etxPkBox.reValue();
						etxPkScat.reValue();
						etxPkQty.reValue();
						spnPkLot.clearItems();
						etxPkMultPk.reValue(); 
						etxPkCmScan.reValue();
						LBS="";
						cmPart="";
						//isBcScan= 0 ;
						setChangeTable(map);
						if("1.0".equals(applicationDB.Ctrl.getString("RFC_SETBIN_BYNBR", "0").toString())){  
							getFocues(etxPkBar, true);  
						}else{
							getFocues(etxPkBin, true);
						}
					}else{
						showErrorMsg(wr.getMessages());
						getFocues(etxPkNbr, true);
					}
				}
			});
			
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			//取消则回到该栏位位置选中该栏位值
			etxPkMultPk.reValue();
			getFocues(etxPkMultPk, true);
		}
	};
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String ,Object> map = wr.getDataToMap();
			//etxPkBin.reValue();
			etxPkBar.reValue();
			etxPkPart.reValue();
			etxPkPartDesc.reValue();
			etxPkUm.reValue();
			etxPkReq.reValue();
			etxPkUnit.reValue();
			etxPkQoh.reValue();
			etxPkTotPk.reValue();
			etxPkQtyDif.reValue();
			etxPkAdvBox.reValue();
			etxPkBox.reValue();
			etxPkScat.reValue();
			etxPkQty.reValue();
			spnPkLot.clearItems();
			etxPkMultPk.reValue(); 
			etxPkCmScan.reValue();
			LBS="";
			cmPart="";
			setChangeTable(map);
			if("1.0".equals(applicationDB.Ctrl.getString("RFC_SETBIN_BYNBR", "0").toString())){  
				getFocues(etxPkBar, true);  
			}else{
				getFocues(etxPkBin, true);
			}
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxPkNbr, true);
		}
	}
	
	
	//解锁
	@Override
	protected void unLockNbr() {
		/*if(!"".equals(etxPkNbr.getValStr().toString().trim())){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {	 
					return pkupdbiz.pkUnLock(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),
							"RFPKL_MSTR", applicationDB.user.getUserId(),applicationDB.user.getMac());
				}
				@Override
				public void onCallBrack(Object data) {

				}
			});	
		}*/
	}
	//返回按钮
		public boolean OnBtnRtnValidata(ButtonType btnType, View v) {
			return true;
		}
		
		public WebResponse OnBtnRtnClick(ButtonType btnType, View v) {
			if(!"".equals(etxPkNbr.getValStr().toString().trim())){
				return pkupdbiz.pkUnLock(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),
						"RFPKL_MSTR", applicationDB.user.getUserId(),applicationDB.user.getMac());
			}else{
				return null; 
			}
		}	
		
		public void OnBtnRtnCallBack(Object data) {
			System.out.println("ssssssssssssss");
		}
	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
	
	private OnShowConfirmListen scfListenPkSub=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					String guid = StringUtil.getUUID();
					if(!"".equals(etxPkQty.getValStr().toString().trim())){
						String saveCmBar = "";
						if(dttype.containsKey(plSet) && "1".equals(needCheck) && ("B".equalsIgnoreCase(LBS) ||"S".equalsIgnoreCase(LBS) )){
							saveCmBar = "1";
						}
						return pkupdbiz.pkSubmit(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", etxPkPart.getValStr(), spnPkLot.getValStr(), 
							etxPkQty.getValStr(), applicationDB.user.getUserId(), etxPkUnit.getValStr(), etxPkBox.getValStr(), etxPkScat.getValStr(),
							etxPkBin.getValStr(), etxPkBar.getValStr(),applicationDB.user.getMac(),vend,applicationDB.user.getUserDate(),guid,plSet
							,cust,endCm,saveCmBar,etxPkCmScan.getValStr(),cmPart);
					}else{
						return pkupdbiz.pkSubmitByNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate(),guid,plSet);
					}
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse)data;
					if(wr.isSuccess()){
						etxPkNbr.reValue();
						etxPkBin.reValue();
						etxPkBar.reValue();
						etxPkPart.reValue();
						etxPkPartDesc.reValue();
						etxPkUm.reValue();
						etxPkReq.reValue();
						etxPkUnit.reValue();
						etxPkQoh.reValue();
						etxPkTotPk.reValue();
						etxPkQtyDif.reValue();
						etxPkAdvBox.reValue();
						etxPkBox.reValue();
						etxPkScat.reValue();
						etxPkQty.reValue();
						spnPkLot.clearItems();
						etxPkMultPk.reValue();
						etxPkCmScan.reValue();
						etxPkAd.reValue();
						needCheck="" ; 
						endCm="";
						cust="";
						plSet="";
						LBS="";
						cmPart="";
						reqList = null;
						gdvPkTotReq.buildData(reqList);
						gdvPkAdv.buildData(reqList);
						gdvPkActPk.buildData(reqList);
						getFocues(etxPkNbr, true);
						showSuccessMsg(wr.getMessages());
					}else{
						showErrorMsg(wr.getMessages());
					}
				}
			});	
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			getFocues(etxPkBin, true);
		}
	};
	
	// 添加客户标签扫描
		private void checkPkCmScan() {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					boolean flag = true ; 
					if( (etxPkNbr.getValStr().trim()).equals("")){
						showErrorMsg("请先输入单号!");
						flag = false;
					}
					if( !"1.0".equals(applicationDB.Ctrl.getString("RFC_CHGBIN_BYBAR", "0").toString() ) 
							&& (etxPkBin.getValStr().trim()).equals("")){
						showErrorMsg("请先输入建议储位!");
						flag = false;
					}
					if( (etxPkBar.getValStr().trim()).equals("")){
						showErrorMsg("请先输入内部条码!");
						flag = false;
					}
					return flag;
				}
				@Override
				public Object onGetData() {
					return pkupdbiz.pkCheckCmBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), 
							 etxPkBar.getValStr().toString(),etxPkPart.getValStr().toString(),needCheck,cust,endCm,etxPkCmScan.getValStr().toString());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String, Object> mp  = wr.getDataToMap();
						Object p = null ;
						if(null!= mp && mp.containsKey("CMPART")){
							p = mp.get("CMPART");
						}
						cmPart = null == mp || "null".equals(StringUtil.isEmpty(p,"") ) ? "" : p+"";
						if(LBS.equals("L")){  
							getFocues(etxPkBox, true); 
						}else{
							//if(isBcScan == 0){//如果客签在系统中不存在
								if("B".equalsIgnoreCase(LBS)){
									//checkPkAutoBc(); // 如果是箱件标签，并且 需求量 大于等于 （已分拣量 + 本次分拣量） 则自动保存
									if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr()) > StringUtil.parseFloat(etxPkReq.getValStr())){
										showConfirm("本次分拣量+已检量 大于需求量是否保存", scfListenThan); 
									}else{
										checkPkAutoBc();
									}
								}
							/*}else{
								getFocues(etxPkMultPk, true);
							}*/
						}

						// getFocues(etxPkBar, true);
						runClickFun(); 
						istrue = true;
					}else{
						showErrorMsg(wr.getMessages());
						//etxPkBin.reValue();
						etxPkBar.reValue();
						etxPkPart.reValue();
						etxPkPartDesc.reValue();
						etxPkUm.reValue();
						etxPkReq.reValue();
						etxPkUnit.reValue();
						etxPkQoh.reValue();
						etxPkTotPk.reValue();
						etxPkQtyDif.reValue();
						etxPkAdvBox.reValue();
						etxPkBox.reValue();
						etxPkScat.reValue();
						etxPkQty.reValue();
						spnPkLot.clearItems();
						etxPkMultPk.reValue(); 
						etxPkCmScan.reValue();
						LBS="";
						cmPart="";
						//istrue = false;
						getFocues(etxPkBar, true);
					}
				}
			});
		}
		// 如果是箱件标签，并且 需求量 大于等于 （已分拣量 + 本次分拣量） 则自动保存
		private void checkPkAutoBc() {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					boolean flag = true ; 
					if( (etxPkMultPk.getValStr().trim()).equals("")){
						showErrorMsg("请先输入分拣数量!");
						flag = false;
					}
					//如果已检量 + 本次分拣量 大于需求量，则提示 是否保存
					if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr()) > StringUtil.parseFloat(etxPkReq.getValStr())){
						showConfirm("本次分拣量+已检量 大于需求量是否保存", scfListenThan); 
						istrue = false;
					}
					if( StringUtil.parseFloat(etxPkReq.getValStr()) >= ( StringUtil.parseFloat(etxPkTotPk.getValStr()) + StringUtil.parseInt(etxPkQty.getValStr()) )) {
						flag = true;
					}
					
					return flag;
				}
				@Override
				public Object onGetData() {
					String saveCmBar = "";
					if(dttype.containsKey(plSet) && "1".equals(needCheck) && ("B".equalsIgnoreCase(LBS) ||"S".equalsIgnoreCase(LBS) ) ){
						saveCmBar = "1";
					} 
					return pkupdbiz.pkSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", etxPkPart.getValStr(), spnPkLot.getValStr(), 
							etxPkQty.getValStr(), applicationDB.user.getUserId(), etxPkUnit.getValStr(), etxPkBox.getValStr(), etxPkScat.getValStr(),
							etxPkBin.getValStr(), etxPkBar.getValStr(),vend,cust,endCm,saveCmBar,etxPkCmScan.getValStr(),cmPart,etxPkReq.getValStr(),"PkUpdActivity",isQuX,etxPkYLot.getValStr());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse)data;
					if(wr.isSuccess()){
						Map<String ,Object> map = wr.getDataToMap();
						//etxPkBin.reValue();
						etxPkBar.reValue();
						etxPkPart.reValue();
						etxPkPartDesc.reValue();
						etxPkUm.reValue();
						etxPkReq.reValue();
						etxPkUnit.reValue();
						etxPkQoh.reValue();
						etxPkTotPk.reValue();
						etxPkQtyDif.reValue();
						etxPkAdvBox.reValue();
						etxPkBox.reValue();
						etxPkScat.reValue();
						etxPkQty.reValue();
						spnPkLot.clearItems();
						etxPkMultPk.reValue(); 
						etxPkCmScan.reValue();
						LBS="";
						cmPart="";
						//isBcScan= 0 ;
						setChangeTable(map);
						if("1.0".equals(applicationDB.Ctrl.getString("RFC_SETBIN_BYNBR", "0").toString())){  
							getFocues(etxPkBar, true);  
						}else{
							getFocues(etxPkBin, true);
						}
					}else{
						showErrorMsg(wr.getMessages());
						getFocues(etxPkNbr, true);
					}
				}
			});
		}
		
}
