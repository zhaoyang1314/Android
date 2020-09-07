package com.profitles.activity;

import java.util.ArrayList;
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
import com.profitles.biz.PkBiz;
import com.profitles.framwork.activity.AppFunActivity;
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
@SuppressWarnings(value = { "all" })
public class PkActivity extends AppFunActivity {
	private PkBiz pkbiz;
	private MyTextView txvPkUnit,txvPkMultQty;
	private MyEditText etxPkPart,etxPkUm,
					   etxPkPartDesc,etxPkReq,etxPkUnit, etxPkMultPk,etxPkTotPk,
					   etxPkQtyDif,etxPkQoh,etxPkBox,etxPkadvBox,etxPkScat,
					   etxPkQty,etxPkAdvBox,extWjlQty;
	private MyReadBQ  etxPkNbr,etxPkBin,etxPkBar;
	private MySpinner  spnPkLot;
	private MyTabHost tbl_pkAdv;
	private LinearLayout lltPkTotReq,lltPkAdv,lltPkActPk;
	private MyDataGrid gdvPklist, gdvPkTotReq,gdvPkAdv,gdvPkActPk;
	private String biaoshi="0", nextBarMsg = "", vend , plSet = "",check="";
	private List<Map<String , Object>> reqList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> advList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> pkList = new ArrayList<Map<String , Object>>();
	private float act=0;
	private ApplicationDB applicationDB;
	private boolean onPageLoad = true;
	private View backRow;
	private int checkRowIndex;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pk;
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
		pkbiz = new PkBiz();
		etxPkNbr = (MyReadBQ) findViewById(R.id.etx_pkNbr);
		etxPkBin = (MyReadBQ) findViewById(R.id.etx_pkBin);
		etxPkBar = (MyReadBQ) findViewById(R.id.etx_pkBar);
		
		txvPkMultQty = (MyTextView) findViewById(R.id.txv_pkMultPk);
		txvPkUnit=(MyTextView) findViewById(R.id.txv_pkUnit);
		lltPkTotReq = (LinearLayout) findViewById(R.id.llt_pkTotReq) ; 
		lltPkAdv = (LinearLayout) findViewById(R.id.llt_pkAdv) ; 
		lltPkActPk = (LinearLayout) findViewById(R.id.llt_pkActPk) ; 
		
		gdvPklist = (MyDataGrid) findViewById(R.id.gdv_pklist) ;
		reBindPkList();
		
		gdvPklist.setOnMyDataGridListener(new OnMyDataGridListener() {
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowDatas) {
				return false;
			}
			public void onItemClick(View view, Object val,  int rowIndex, int colIndex ,Map<String, Object> rowData) {
				if(rowIndex == 0) return;
//				Map<String, Object> map = dtg.getRowDataByKey(rowIndex-1);
				if(backRow != null){
					// backRow.setBackgroundColor(Color.TRANSPARENT);//清空上次点击行颜色
					backRow.setBackgroundColor(checkRowIndex == 0  ? Color.TRANSPARENT : 
						(checkRowIndex%2 == 0 ? Color.TRANSPARENT: Color.parseColor(Constants.CHECK_ROW_COLOR)) );//清空上次点击行颜色
				}
				backRow  = (View) view.getParent();		//保存获取到行View
				checkRowIndex = rowIndex;
				View vv = (View) view.getParent();		//获取到行View
				vv.setBackgroundColor(Color.YELLOW);	//更改选中行的背景色
				etxPkNbr.setText(rowData.get("RFPKL_NBR")+"");
				getFocues(etxPkBin, true);
				checkNbr();
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
		extWjlQty= (MyEditText) findViewById(R.id.ext_wjlQty);
		biaoshi="0";
		act	 =0;
		updateUMVis(true);
		spnPkLot.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if (onPageLoad) {
					onPageLoad = false;
				} else {
//					checkPkCLoc();
				}
			};
			public void onNothingSelected(AdapterView<?> parent){};
		});
		
		etxPkNbr.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!etxPkNbr.getValStr().equals("")){
					checkNbr();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		etxPkBar.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etxPkBar.getValStr() && !"".equals( etxPkBar.getValStr().toString().trim())){
					 checkPkBar();
				}else{
					istrue = true;
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
	}

	private void updateUMVis(boolean flag) { 
		// true 显示每箱  false 显示已拣
		txvPkUnit.setVisibility(flag ? View.VISIBLE : View.GONE);
		etxPkUnit.setVisibility(flag ? View.VISIBLE : View.GONE);
	    txvPkMultQty.setVisibility(flag ? View.GONE : View.VISIBLE);
	    etxPkMultPk.setVisibility(flag ? View.GONE : View.VISIBLE); 
	}

	private void reBindPkList(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return pkbiz.getPkList(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite());
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
	/*	if(etxPkNbr.getId() == id){
			runClickFun();
			if(null != etxPkNbr.getValStr() && !"".equals( etxPkNbr.getValStr().toString().trim())){
				 checkNbr();
				 runClickFun();
			}else{
				istrue = true;
			}
		}*/
		//条码
		/*if(etxPkBar.getId() == id){
			runClickFun();
			if(null != etxPkBar.getValStr() && !"".equals( etxPkBar.getValStr().toString().trim())){
				 checkPkBar();
				 runClickFun();
			}else{
				istrue = true;
			}
		}*/
		if(etxPkBox.getId() == id){
				runClickFun();
		}
		if(etxPkMultPk.getId()==id){
				runClickFun();
		}
		if(etxPkScat.getId() == id){
			runClickFun();
		}
		//储位
				if(etxPkBin.getId() == id){
					runClickFun();
					if(null != etxPkBin.getValStr() && !"".equals( etxPkBin.getValStr().toString().trim())){
						loadDataBase(new IRunDataBaseListens() {
							@Override
							public boolean onValidata() {
								return true;
							}
							@Override
							public Object onGetData() {
								return pkbiz.pkCheckLocBin(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString() ,etxPkBin.getValStr().toString());
							}
							@Override
							public void onCallBrack(Object data) {
								WebResponse wr=(WebResponse) data;
								if(wr.isSuccess()){
									String meg=(String) wr.getWData();
									if("1".equals(meg)){
										showErrorMsg(wr.getMessages());	
										etxPkBin.setText("");
										getFocues(etxPkBin, true);
									}else
									if("-1".equals(meg)){
										showConfirm(wr.getMessages(), scfListenPk2);
									}else{
										clearMsg();
										runClickFun();
										istrue=true;
									}
								}else{
									istrue = false;
									getFocues(etxPkBin, true);
									showErrorMsg(wr.getMessages());
								}
							}
						}); 
					}else{
						istrue = true;
					}
				}
		return istrue ;
	}
	
	private void checkNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return pkbiz.pkCheckNbr(applicationDB.user.getUserDmain() , applicationDB.user.getUserSite() ,etxPkNbr.getValStr().toString() , applicationDB.user.getUserId() , applicationDB.user.getMac());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					if(map!=null){
						plSet = map.get("PLSET")+"";
						if("1".equals(map.get("isHavaAct").toString())){
							// 存在已拣信息是否情况 弹出框中处理
							showConfirm("单号存在扫描记录是否继续扫(取消会把当前单号所有保存记录清除)?", scfListenPk);
						}else{
							setChangeTable(map);
							istrue = true;
							runClickFun();
						}
					}else{
						showErrorMsg(getResources().getString(R.string.pk_getfalse)); 
						istrue = false;
					}
					tbl_pkAdv.setCurrentTab(1);
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					getFocues(etxPkNbr, true);
				}
			}
		});
	}

	private void setChangeTable(Map<String, Object> map) {
		reqList =  (List<Map<String, Object>>) map.get("reqList"); 
		advList =  (List<Map<String, Object>>) map.get("advList");
		pkList =  (List<Map<String, Object>>) map.get("pkList");
		//gdvPkTotReq.clearData();
		//gdvPkAdv.clearData();
		//gdvPkActPk.clearData();
		extWjlQty.setText(StringUtil.isEmpty(map.get("WJ_QTY"))?"":map.get("WJ_QTY").toString());
		gdvPkTotReq.buildData(reqList);
		//gdvPkAdv.buildData(advList);
//		gdvPkActPk.buildData(pkList);
		nextBarMsg = "零件："+(StringUtil.isEmpty(map.get("RFLAD_PART")+"")?"":map.get("RFLAD_PART")+"") + " 批次:" + (StringUtil.isEmpty(map.get("RFLAD_LOT"))?"":map.get("RFLAD_LOT")) + " 数量:" + (StringUtil.isEmpty(map.get("RFLAD_QTY_REQ"))?"":map.get("RFLAD_QTY_REQ"));
		showWarningMsg("建议储位:"+(("null".toLowerCase()).equals((StringUtil.isEmpty(map.get("advBin"))?"":(map.get("advBin")+"").toLowerCase()))?"":map.get("advBin")+"")+" "+(nextBarMsg==null?"":nextBarMsg));
	}
	
	
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
					return pkbiz.checkPkNbrTemp(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxPkNbr.getValStr().toString(), applicationDB.user.getUserId(), applicationDB.user.getMac(), "false");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
					  Map<String, Object> map=wr.getDataToMap();
					  setChangeTable(map);
					  runClickFun();
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
					}
					getFocues(etxPkBar, true);
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
					return pkbiz.checkPkNbrTemp(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxPkNbr.getValStr().toString(), applicationDB.user.getUserId(), applicationDB.user.getMac(), "true");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String, Object> map=wr.getDataToMap();
						setChangeTable(map);
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
					}
					getFocues(etxPkBar, true);
				}
			});	
		}
	};
	
	
	
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
					return pkbiz.pkExcLocBin(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxPkNbr.getValStr().toString(), etxPkBin.getValStr().toString(),"PkActivity",applicationDB.user.getUserId());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						getFocues(etxPkBar, true);
						runClickFun();
						istrue = true;
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
					}
				}
			});	
			
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			//取消则回到该栏位位置选中该栏位值
			getFocues(etxPkBin, true);
			istrue=false;
		}
	};
	
	
	private void checkPkBar() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				/*if( (etxPkBin.getValStr().trim()).equals("")){
					showErrorMsg("请先输入建议储位!");
				}*/
				return true;
			}
			@Override
			public Object onGetData() {
				return pkbiz.pkCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(),etxPkBar.getValStr().toString(), biaoshi);
			}
			/*@Override
			public Object onGetData() {
				return pkbiz.pkCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), etxPkBin.getValStr().toString(), etxPkBar.getValStr().toString(), biaoshi);
			}*/
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					vend = map.get("RFLOT_VEND")+"";
					float act_qty = 0f;
					if(reqList!=null || reqList.size()>0){
						for (int i = 0; i < reqList.size(); i++) {
							Map<String, Object> mapinfo=reqList.get(i);
							if(map.get("RFLOT_PART").equals(mapinfo.get("RFPKLD_PART"))){
								act_qty=StringUtil.parseFloat(mapinfo.get("QTY_ACT")+"");
							}
						}
					}
					if("-1".equals(map.get("Fal").toString())){
						// 当前批次不存在分配明细中	 
						showConfirm(wr.getMessages(), scfListenPkLot);
					}else if((StringUtil.parseFloat(map.get("RFLOT_MULT_QTY"))  + act_qty )>StringUtil.parseFloat(map.get("QTYREQ"))){
//						showErrorMsg("扫描量大于需求量,不能进行分拣");
//						etxPkBar.setText("");
//						getFocues(etxPkBar, true);
						showConfirm("扫描量大于需求量,是否继续分拣扫描?", scfListenPkLot3);
					}else{
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
						etxPkBin.setText(StringUtil.isEmpty(map.get("RFLOT_BIN"))?"":map.get("RFLOT_BIN").toString());
						String LBS=map.get("RFPTV_LBS").toString();
						float boxQty =StringUtil.parseFloat(map.get("RFLOT_MULT_QTY").toString()); //每箱量
						float scatQty =StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY").toString()); //散量
						float qTYREQ =StringUtil.parseFloat(map.get("QTYREQ").toString());
						etxPkReq.setText(qTYREQ+"");			//需求
						float qtyOh =StringUtil.parseFloat(map.get("XSLD_QTY_OH").toString());
						etxPkQoh.setText(qtyOh+"");   //库存
						float qtyAct =StringUtil.parseFloat(map.get("QTYACT").toString());
						etxPkTotPk.setText(qtyAct+"");  //已拣
						float qtyDif=StringUtil.parseFloat(map.get("QTYREQ").toString())-StringUtil.parseFloat(map.get("QTYACT").toString());
						etxPkQtyDif.setText("("+qtyDif+")"); //已拣后面()
						etxPkAdvBox.setText(map.get("TBOX").toString());//推荐箱数
						extWjlQty.setText(map.get("WJ_QTY")+"");// 未拣量
						if(LBS.equals("L")){
							updateUMVis(true);
							etxPkUnit.setText(boxQty+""); 
							getFocues(etxPkBox, true);
						}else if(LBS.equals("B") || LBS.equals("S")){
							updateUMVis(false);
							if(scatQty > 0){
								etxPkUnit.setText(scatQty+"");
								etxPkMultPk.setText(scatQty+"");
							}else{
								etxPkUnit.setText(boxQty+"");
								etxPkMultPk.setText(boxQty+"");
							}
							
							etxPkBox.setText("1");
							etxPkScat.setText("0"); 
//							etxPkBox.setEnabled(false); //此处为是否进行光标的使用
							etxPkBox.setKeyListener(null);
							etxPkScat.setEnabled(false);
							getFocues(etxPkMultPk, true);
						}
						//showWarningMsg(wr.getMessages());
					}
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					getFocues(etxPkBar, true);
				}
			}
		});
	}
	
	
	
	
	
	private OnShowConfirmListen scfListenPkLot3=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pkbiz.pkCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(),etxPkBar.getValStr().toString(), "1");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String ,Object> map = wr.getDataToMap();
						biaoshi = "1";
						float qTYREQ =StringUtil.parseFloat(map.get("QTYREQ").toString());
						float qtyAct =StringUtil.parseFloat(map.get("QTYACT").toString());
							vend = map.get("RFLOT_VEND")+"";
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
							etxPkBin.setText(StringUtil.isEmpty(map.get("RFLOT_BIN"))?"":map.get("RFLOT_BIN").toString());
							String LBS=map.get("RFPTV_LBS").toString();
							float boxQty =StringUtil.parseFloat(map.get("RFLOT_MULT_QTY").toString()); //每箱量
							float scatQty =StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY").toString()); //散量
							etxPkReq.setText(qTYREQ+"");			//需求
							float qtyOh =StringUtil.parseFloat(map.get("XSLD_QTY_OH").toString());
							etxPkQoh.setText(qtyOh+"");   //库存
							etxPkTotPk.setText(qtyAct+"");  //已拣
							float qtyDif=StringUtil.parseFloat(map.get("QTYREQ").toString())-StringUtil.parseFloat(map.get("QTYACT").toString());
							etxPkQtyDif.setText("("+qtyDif+")"); //已拣后面()
							etxPkAdvBox.setText(map.get("TBOX").toString());//推荐箱数
							float qtyscat = StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY")+"");//散量
							extWjlQty.setText(map.get("WJ_QTY")+"");// 未拣量
							if(LBS.equals("L")){
								updateUMVis(true);
								etxPkUnit.setText(boxQty+""); 
								getFocues(etxPkBox, true);
							}else if(LBS.equals("B")){
								updateUMVis(false);
								if(scatQty > 0){
									etxPkUnit.setText(scatQty+"");
									etxPkMultPk.setText(scatQty+"");
								}else{
									etxPkUnit.setText(boxQty+"");
									etxPkMultPk.setText(boxQty+"");
								}
								etxPkBox.setText("1");
								etxPkScat.setText("0");
//								etxPkBox.setEnabled(false);
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
//								etxPkBox.setEnabled(false);
								etxPkBox.setKeyListener(null);
								etxPkScat.setEnabled(false);
								getFocues(etxPkMultPk, true);
							}
							showWarningMsg(wr.getMessages());
							runClickFun();
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
					}
				}
			});
			
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			//取消则回到该栏位位置选中该栏位值
			etxPkBar.setText("");
			etxPkBin.setText("");
//			etxPkNbr.setText("");
			getFocues(etxPkBar, true);
			istrue=false;
		}
	};
	
	
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
					return pkbiz.pkCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), etxPkBar.getValStr().toString(), "1");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String ,Object> map = wr.getDataToMap();
						float qTYREQ =StringUtil.parseFloat(map.get("QTYREQ").toString());
						float qtyAct =StringUtil.parseFloat(map.get("QTYACT").toString());
							vend = map.get("RFLOT_VEND")+"";
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
							float scatQty =StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY").toString()); //散量
							etxPkReq.setText(qTYREQ+"");			//需求
							float qtyOh =StringUtil.parseFloat(map.get("XSLD_QTY_OH").toString());
							etxPkQoh.setText(qtyOh+"");   //库存
							etxPkTotPk.setText(qtyAct+"");  //已拣
							float qtyDif=StringUtil.parseFloat(map.get("QTYREQ").toString())-StringUtil.parseFloat(map.get("QTYACT").toString());
							etxPkQtyDif.setText("("+qtyDif+")"); //已拣后面()
							etxPkAdvBox.setText(map.get("TBOX").toString());//推荐箱数
							float qtyscat = StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY")+"");//散量
							extWjlQty.setText(map.get("WJ_QTY")+"");// 未拣量
							etxPkBin.setText(map.get("RFLOT_BIN")+"");// 储位
							if( (qtyscat == 0 ? boxQty : qtyscat) >  (qTYREQ - qtyAct)){
								showConfirm("扫描量大于需求量,是否继续分拣扫描?", scfListenPkLot3);
							}else{
							if(LBS.equals("L")){
								updateUMVis(true);
								etxPkUnit.setText(boxQty+""); 
								getFocues(etxPkBox, true);
							}else if(LBS.equals("B")){
								updateUMVis(false);
								if(scatQty > 0){
									etxPkUnit.setText(scatQty+"");
									etxPkMultPk.setText(scatQty+"");
								}else{
									etxPkUnit.setText(boxQty+"");
									etxPkMultPk.setText(boxQty+"");
								}
								etxPkBox.setText("1");
								etxPkScat.setText("0");
//								etxPkBox.setEnabled(false);
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
//								etxPkBox.setEnabled(false);
								etxPkBox.setKeyListener(null);
								etxPkScat.setEnabled(false);
								getFocues(etxPkMultPk, true);
							}
							biaoshi = "1";
							showWarningMsg(wr.getMessages());
							runClickFun();
							}
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
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
	
	@Override
	protected void onChangedAft(int id, Editable s) {
		//箱数改变事件 etxPkTotPk
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
						if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act<=(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
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
							istrue=false;
							getFocues(etxPkBox, true);
							etxPkQty.reValue();
							showErrorMsg(getResources().getString(R.string.pk_save2_false));
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
						istrue = false;
						getFocues(etxPkScat, true);
						etxPkQty.reValue();
						showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
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
	
	private void checkPkCLoc() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return pkbiz.pkCheckChangeLot(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(),etxPkBin.getValStr().toString(),
						etxPkPart.getValStr().toString(),spnPkLot.getValStr().toString(), etxPkReq.getValStr().toString(), etxPkUnit.getValStr().toString(), biaoshi,vend);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					if("-1".equals(map.get("Fal").toString())){
						// 当前批次不存在分配明细中
						showConfirm(wr.getMessages(), scfListenPkLot2);
					}else{
						float qtyOh =StringUtil.parseFloat(map.get("XSLD_QTY_OH").toString());
						etxPkQoh.setText(qtyOh+"");   //库存
						etxPkAdvBox.setText(map.get("TBOX").toString());//推荐箱数
						showWarningMsg(wr.getMessages());
					}
					biaoshi = "0";
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
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
					return pkbiz.pkCheckChangeLot(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(),etxPkBin.getValStr().toString(),
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
						istrue = false;
						showErrorMsg(wr.getMessages());
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
			return pkbiz.pkSubmit(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", etxPkPart.getValStr(), spnPkLot.getValStr(), 
				etxPkQty.getValStr(), applicationDB.user.getUserId(), etxPkUnit.getValStr(), etxPkBox.getValStr(), etxPkScat.getValStr(),
				etxPkBin.getValStr(), etxPkBar.getValStr(),applicationDB.user.getMac(),vend,applicationDB.user.getUserDate(),guid,plSet);
		}else{
			return pkbiz.pkSubmitByNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate(),guid,plSet);
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
			extWjlQty.reValue();
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
	//etx_pkReq需求量  etxPkTotPk已检  etx_pkMultPk分拣
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

			if(StringUtil.parseFloat(etxPkTotPk.getValStr())+StringUtil.parseFloat(etxPkQty.getValStr())-act<=(StringUtil.parseFloat(etxPkReq.getValStr())+StringUtil.parseFloat(etxPkUnit.getValStr())-0.0001)){
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
		return istrue;
	}
	
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return pkbiz.pkSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", etxPkPart.getValStr(), spnPkLot.getValStr(), 
				etxPkQty.getValStr(), applicationDB.user.getUserId(), etxPkUnit.getValStr(), etxPkBox.getValStr(), etxPkScat.getValStr(),
				etxPkBin.getValStr(), etxPkBar.getValStr(),vend);
	}
		
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String ,Object> map = wr.getDataToMap();
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
			extWjlQty.reValue();
			spnPkLot.clearItems();
			etxPkMultPk.reValue();
			setChangeTable(map);
			getFocues(etxPkBar, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxPkNbr, true);
		}
	}
	
	
	//解锁
	@Override
	protected void unLockNbr() {
		if(!"".equals(etxPkNbr.getValStr().toString().trim())){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {	
					String guid = StringUtil.getUUID();
					return pkbiz.pkSubmitByNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"RFPKL_MSTR", applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate(),guid,plSet);
				}
				@Override
				public void onCallBrack(Object data) {

				}
			});	
		}
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
						return pkbiz.pkSubmit(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", etxPkPart.getValStr(), spnPkLot.getValStr(), 
							etxPkQty.getValStr(), applicationDB.user.getUserId(), etxPkUnit.getValStr(), etxPkBox.getValStr(), etxPkScat.getValStr(),
							etxPkBin.getValStr(), etxPkBar.getValStr(),applicationDB.user.getMac(),vend,applicationDB.user.getUserDate(),guid,plSet);
					}else{
						return pkbiz.pkSubmitByNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr(),"rfpkld_det", applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate(),guid,plSet);
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
						extWjlQty.reValue();
						reqList = null;
						gdvPkTotReq.buildData(reqList);
						gdvPkAdv.buildData(reqList);
						gdvPkActPk.buildData(reqList);
						getFocues(etxPkBin, true);
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
}
