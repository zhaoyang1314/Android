package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.PkBiz;
import com.profitles.biz.PoRtnBiz;
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
import com.profitles.framwork.util.StringUtil;

public class RtnPoActivity extends AppFunActivity {
	private PoRtnBiz poRtnBiz;
	private MyEditText etx_pkPart,etx_pkPartDesc,
	etx_pkPoQty,etx_pknum,etx_pkPoLoc, etx_pkLot,etx_pkPoState,
	etx_pkVend,etx_pkVendDesc,etx_pkPoRecQtyfu,etx_pkPorOrQ,txv_pkPoBin,txv_pkPoNbr,txv_pkPoRtnQty;
	private MyReadBQ  txv_pkNbr;
	private MySpinner  ext_poLineSpinner;
	private String rtnQty="0", nextBarMsg = "", vend , plSet = "";
	private List<Map<String , Object>> reqList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> advList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> pkList = new ArrayList<Map<String , Object>>();
	private float act=0;
	private ApplicationDB applicationDB;
	private boolean onPageLoad = true;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_rtnpo;
	}

	//@SuppressLint("CutPasteId")
	@Override 
	protected void pageLoad() {
		poRtnBiz = new PoRtnBiz();
		txv_pkNbr = (MyReadBQ) findViewById(R.id.etx_pkNbr); //标签
		
		etx_pkPart = (MyEditText) findViewById(R.id.etx_pkPart);//零件
		etx_pkPartDesc=(MyEditText) findViewById(R.id.etx_pkPartDesc);   //零件描述
		txv_pkPoBin = (MyEditText) findViewById(R.id.etx_pkPoBin) ; //储位
		etx_pkPoLoc = (MyEditText) findViewById(R.id.etx_pkLoc) ;  //库位
		etx_pkPoQty = (MyEditText) findViewById(R.id.etx_pkPoQty) ;// 数量
		
		etx_pknum = (MyEditText) findViewById(R.id.etx_pknum) ; //单位
		etx_pkLot = (MyEditText) findViewById(R.id.etx_pkLot) ; //批次
		txv_pkPoNbr = (MyEditText) findViewById(R.id.etx_pkPoNbr) ;  //采购单
		etx_pkVend = (MyEditText) findViewById(R.id.etx_pkVend) ;//供应商 
		etx_pkPoState = (MyEditText) findViewById(R.id.etx_pkPoStatefu) ;//状态
		etx_pkVendDesc = (MyEditText) findViewById(R.id.etx_pkVendDesc) ; //供应商描述
		
		ext_poLineSpinner = (MySpinner) findViewById(R.id.ext_poLineSpinner ); //行号ext_poLineSpinner
		etx_pkPoRecQtyfu = (MyEditText) findViewById(R.id.etx_pkPoRecQtyfu ); //收货量
		etx_pkPorOrQ = (MyEditText) findViewById(R.id.etx_pkPorOrQty ); //已退货量
		txv_pkPoRtnQty = (MyEditText) findViewById(R.id.etx_pkPoRtnQty); //退货量
		runClickFun();
		// 行号下拉框
					ext_poLineSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
								@Override
								public void onItemSelected(AdapterView<?> parent, View view,
										int position, long id) {
									if (onPageLoad) {
										onPageLoad = false;
									} else {
										checkPoLine(ext_poLineSpinner.getValStr());
									}
								}

								@Override
								public void onNothingSelected(AdapterView<?> parent) {

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
		if(txv_pkNbr.getId() == id){
			runClickFun();
			if(null !=txv_pkNbr.getValStr() && !"".equals(txv_pkNbr.getValStr().toString().trim())){
				checkNbr(); 
			}else{
				showErrorMsg("请扫码操作");
				getFocues(txv_pkNbr, true);
			}
		}
		if(txv_pkPoNbr.getId() == id){
			runClickFun();
			if(null !=txv_pkPoNbr.getValStr() && !"".equals(txv_pkPoNbr.getValStr().toString().trim()) ){
				checkPoNo();
			}else{
				showErrorMsg("请输入订单号");
				getFocues(txv_pkPoNbr, true);
			}
		}
		if(ext_poLineSpinner.getId() == id){
			checkPoLine(ext_poLineSpinner.getValStr());
			runClickFun();
		}
		return true;
	}
	
	private void checkNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return poRtnBiz.searchScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), txv_pkNbr.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
			Map<String ,Object> mpRflot = wr.getDataToMap();	
					//进行文本框赋值
			etx_pkPart.setText(mpRflot.get("RFLOT_PART").toString());	//零件
			etx_pkPartDesc.setText(mpRflot.get("RFLOT_PART_DESC").toString()); //零件描述
			//float mult_qty = StringUtil.parseFloat(mpRflot.get("RFLOT_MULT_QTY"));
			etx_pkPoQty.setText(mpRflot.get("RFLOT_MULT_QTY").toString()); //数量
			etx_pknum.setText(mpRflot.get("RFLOT_UM").toString()); //单位
			if("000".equals(mpRflot.get("RFLOT_BIN").toString())){
				txv_pkPoBin.setText("");
			}else{
				txv_pkPoBin.setText(mpRflot.get("RFLOT_BIN").toString()); //储位
			}
			etx_pkPoLoc.setText(mpRflot.get("RFLOT_LOC").toString()); //库位
			etx_pkLot.setText(mpRflot.get("RFLOT_LOT").toString()); //批次
			rtnQty = mpRflot.get("RFLOT_MULT_QTY").toString(); //退货量
			
				}else{
					txv_pkNbr.setText("");
					getFocues(txv_pkNbr, true);
					showErrorMsg(wr.getMessages());
				}
				
			}
		});
	}
	//采购订单查询
	private void checkPoNo() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if(StringUtil.isEmpty(txv_pkPoBin.getValStr().toString())){
					showErrorMsg("储位不能为空");
					getFocues(txv_pkPoBin, true);
				}
				if(null ==txv_pkPoNbr.getValStr() && "".equals(txv_pkPoNbr.getValStr().toString().trim()) ){
					showErrorMsg("请输入采购单号");
					getFocues(txv_pkPoNbr, true);
				}
				return true;
			}
			@Override
			public Object onGetData() {
				return poRtnBiz.searchPoNo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), txv_pkPoNbr.getValStr(),etx_pkPart.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
			Map<String ,Object> mpPoNo = wr.getDataToMap();	
			List<Map<String , Object>> poList = (List<Map<String, Object>>) mpPoNo.get("listPoInfo");
			etx_pkVend.setText(poList.get(0).get("XSPO_VEND").toString()); //供应商
			etx_pkVendDesc.setText(poList.get(0).get("XSAD_NM").toString());  //供应商描述
			etx_pkPoState.setText(poList.get(0).get("XSPO_STAT").toString()); //状态
			List<Map<String , Object>> poLineList = (List<Map<String, Object>>) mpPoNo.get("listPoLine"); //采购订单行号
			List<String> selList = new ArrayList<String>();
			if(ext_poLineSpinner.getValStr() !=null)
				ext_poLineSpinner.clearItems();
			for (int i = 0; i < poLineList.size(); i++) {
				selList.add(poLineList.get(i).get("XSPOD_LINE")+"");
			}
			String[] ub = selList.toArray(new String[selList.size()]); 
			ext_poLineSpinner.addItems(ub);
			if(ub.length ==1){
				 checkPoLine(ub[0]);
			}
			onPageLoad = false;  
				}else{
					txv_pkPoNbr.setText("");
					getFocues(txv_pkPoNbr, true);
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	//选择行号
	public void checkPoLine(final String line){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return poRtnBiz.searchPoLineInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),txv_pkPoNbr.getValStr(), line);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> mpPoLine = wr.getDataToMap();
					etx_pkPorOrQ.setText(mpPoLine.get("XSPOD_QTY_RTND").toString()); //已退货量
					etx_pkPoRecQtyfu.setText(mpPoLine.get("XSPOD_QTY_RCVD").toString()); // 收货量
					txv_pkPoRtnQty.setText(rtnQty); //退货量
				}else{
					showErrorMsg(wr.getMessages());
				}
				
			}
			
		});
	}
	
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Submit,ButtonType.Help};
	}

	@Override
	public Object OnBtnHelpClick(ButtonType btnType, View v) {
		showSuccessMsg(R.string.help_ok);
		return  null ;
	}

	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		
		if((StringUtil.parseFloat(etx_pkPoRecQtyfu.getValStr())-StringUtil.parseFloat(etx_pkPorOrQ.getValStr())) < StringUtil.parseFloat(rtnQty)){
			showErrorMsg("收货量减去已退货量需大于退货量");
			getFocues(txv_pkPoRtnQty, true);
			return false;
		}
		return true;
	}
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return poRtnBiz.submit(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),txv_pkPoNbr.getValStr(),ext_poLineSpinner.getValStr(), 
				etx_pkPoRecQtyfu.getValStr(), etx_pkPorOrQ.getValStr(), rtnQty, txv_pkNbr.getValStr(),txv_pkPoBin.getValStr(),etx_pkPoLoc.getValStr()
				,etx_pkPart.getValStr(),etx_pkLot.getValStr(),etx_pkVend.getValStr(),etx_pknum.getValStr(),applicationDB.user.getUserId());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etx_pkPoRecQtyfu.setText("");
			etx_pkPorOrQ.setText("");
			txv_pkPoRtnQty.setText("");
			showMessage("提交成功");
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(txv_pkPoRtnQty, true);
		}
	}
	
	//解锁
	@Override
	protected void unLockNbr() {
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
	
	
}
