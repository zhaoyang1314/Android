package com.profitles.activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.WoqcngBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class WoqcngActivity extends AppFunActivity {
	private WoqcngBiz Woqcngbiz;
	private MyTextView txv_WoqcngYxj,txv_WoqcngSI;
	private MyEditText etx_WoqcngPart,etx_WoqcngVend,etx_WoqcngVend_Name,etx_WoqcngSI,
		etx_WoqcngQty,etx_WoqcngRQty,etx_WoqcngPart_Desc,etx_WoqcngUM,etx_WoqcngWoNbr,
		etx_WoqcngShift,etx_WoqcngOp;
	private MyReadBQ  etx_WoqcngScan,etx_WoqcngSI2;
	private MySpinner spn_WoqcngType,spn_WoqcngYxj;
	private String vend = "",vendName = "",sType = "";
	private MyDataGrid mdtg_Split;	
	private ApplicationDB applicationDB;
	private List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
	private View backRow;
	private int checkRowIndex;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_woqcng;
	}

	@Override
	protected void pageLoad() {
		Woqcngbiz=new WoqcngBiz();
		etx_WoqcngScan =(MyReadBQ) findViewById(R.id.etx_WoqcngScan);
		etx_WoqcngSI2 =(MyReadBQ) findViewById(R.id.etx_WoqcngSI2);
		etx_WoqcngPart = (MyEditText) findViewById(R.id.etx_WoqcngPart);
		etx_WoqcngPart_Desc = (MyEditText) findViewById(R.id.etx_WoqcngPart_Desc);
		etx_WoqcngWoNbr = (MyEditText) findViewById(R.id.etx_WoqcngWoNbr);
		etx_WoqcngShift = (MyEditText) findViewById(R.id.etx_WoqcngShift);
		etx_WoqcngOp = (MyEditText) findViewById(R.id.etx_WoqcngOp);
		etx_WoqcngUM = (MyEditText) findViewById(R.id.etx_WoqcngUM);
		etx_WoqcngVend = (MyEditText) findViewById(R.id.etx_WoqcngVend);
		etx_WoqcngVend_Name = (MyEditText) findViewById(R.id.etx_WoqcngVend_Name);
		txv_WoqcngSI = (MyTextView) findViewById(R.id.txv_WoqcngSI);
		etx_WoqcngSI = (MyEditText) findViewById(R.id.etx_WoqcngSI);
		etx_WoqcngQty = (MyEditText) findViewById(R.id.etx_WoqcngQty);
		etx_WoqcngRQty = (MyEditText) findViewById(R.id.etx_WoqcngRQty);
		spn_WoqcngType = (MySpinner) findViewById(R.id.spn_WoqcngType);
		txv_WoqcngYxj = (MyTextView) findViewById(R.id.txv_WoqcngYxj);
		spn_WoqcngYxj = (MySpinner) findViewById(R.id.spn_WoqcngYxj);
		
		mdtg_Split = (MyDataGrid) findViewById(R.id.mdtg_Split);
		txv_WoqcngYxj.setVisibility(View.GONE);
		spn_WoqcngYxj.setVisibility(View.GONE);
		checkWoqcngType();
		
		//mdtg_cons = (MyDataGrid)findViewById(R.id.mdtg_cons);
		etx_WoqcngScan.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_WoqcngScan.getValStr() && !"".equals( etx_WoqcngScan.getValStr().toString().trim())){
					checkWoqcngScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			} 
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		spn_WoqcngType.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				changedWoqcngType();
			};
			public void onNothingSelected(AdapterView<?> parent){};
		});
		etx_WoqcngSI2.setVisibility(View.GONE); //暂时隐藏扫描的子项
		
		etx_WoqcngSI2.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!"".equals( etx_WoqcngPart.getValStr().toString().trim()) && !"".equals( etx_WoqcngSI2.getValStr().toString().trim())
						&& !"0".equals( spn_WoqcngType.getValStr().toString().trim())){
					checkWoqcngScanSI();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		mdtg_Split.setOnMyDataGridListener(new OnMyDataGridListener() {
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
				String lot= rowData.get("RFLOTD_MFG_LOT")+"";
				String qty= rowData.get("RFLOTD_MFG_PKG_QTY")+"";
				if(!"null".equals(lot) && !"".equals(lot)){
					if(sType != "" && "A".equals(sType)){
						etx_WoqcngSI.setText(lot);
					}else{
						etx_WoqcngSI2.setText(lot);
					}
				}else{
					etx_WoqcngSI.setText("");
					etx_WoqcngSI2.setText("");
				}
				if(!"null".equals(qty) && !"".equals(qty)){
					etx_WoqcngQty.setText(qty);
				}else{
					etx_WoqcngQty.setText("");
				}
				//etx_WoqcngSI.setText(StringUtil.isEmpty(rowData.get("RFLOTD_MFG_LOT")+"") ? "" : rowData.get("RFLOTD_MFG_LOT")+"");
				//etx_WoqcngQty.setText(StringUtil.isEmpty(rowData.get("RFLOTD_MFG_PKG_QTY")+"") ? "" : rowData.get("RFLOTD_MFG_PKG_QTY")+"");
				//etx_WoqcngRQty.setText(StringUtil.isEmpty(rowData.get("RFSLT_SPT_QTY")+"") ? "" : rowData.get("RFSLT_SPT_QTY")+"");
				getFocues(etx_WoqcngRQty, true);
			}
		});
		
		
	} 

	/*//从采购策略获取是否开启送货单输入
	PRIVATE VOID UPDATECNNBR(BOOLEAN FLAG) { 
		// TRUE 需要验证送货单   FALSE 不需要验证送货单
		ETX_PFTPORCCNNBR.SETVISIBILITY(FLAG ? VIEW.VISIBLE : VIEW.GONE);
		ETX_PFTPORCCNNBR_GONE.SETVISIBILITY(FLAG ? VIEW.GONE : VIEW.VISIBLE);
	}*/
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_PftporcNbr,etx_PftporcBar);
	}
	
	private void checkWoqcngType() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Woqcngbiz.WoqcngType(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					spn_WoqcngType.addItem("请选择拆分类型","0");
					Map<String ,Object> map = wr.getDataToMap();
					List<Map<String , Object>> tList = (List<Map<String, Object>>) map.get("LIST");
					for (int i = 0; i < tList.size(); i++) {
						Map sMap = tList.get(i);
						spn_WoqcngType.addItem(sMap.get("NAME")+"",sMap.get("CODE")+"");
					}
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}

	private void changedWoqcngType() {
		if(!"".equals(etx_WoqcngPart.getValStr())){
			if("1".equals(spn_WoqcngType.getValStr())){ //用不到
				etx_WoqcngVend.setText(vend);
				etx_WoqcngVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_WoqcngSI2, true);
				}else{
					getFocues(etx_WoqcngQty, true);
				}
				if(spn_WoqcngYxj != null){
					spn_WoqcngYxj.clearItems();
				}
				txv_WoqcngYxj.setVisibility(View.GONE);
				spn_WoqcngYxj.setVisibility(View.GONE); 
			}else if("2".equals(spn_WoqcngType.getValStr())){
				etx_WoqcngVend.setText(vend);
				etx_WoqcngVend_Name.setText(vendName);
				if(sType.equals("S")){
					getFocues(etx_WoqcngRQty, true);
				}else if(sType.equals("B")){
					getFocues(etx_WoqcngSI2, true); 
				}else{
					getFocues(etx_WoqcngQty, true);
				}
				if(spn_WoqcngYxj != null){
					spn_WoqcngYxj.clearItems();
				}
				txv_WoqcngYxj.setVisibility(View.GONE);
				spn_WoqcngYxj.setVisibility(View.GONE); 
			}else if("3".equals(spn_WoqcngType.getValStr())){
				if(spn_WoqcngYxj != null){
					spn_WoqcngYxj.clearItems();
				}
				etx_WoqcngVend.setText(vend);
				etx_WoqcngVend_Name.setText(vendName);
				getFocues(etx_WoqcngVend, true);
				/*if(sType.equals("S")){
					getFocues(etx_WoqcngVend, true);
				}else if(sType.equals("B")){
					getFocues(etx_WoqcngVend, true);
				}else{
					getFocues(etx_WoqcngVend, true);
				}*/
				txv_WoqcngYxj.setVisibility(View.GONE);
				spn_WoqcngYxj.setVisibility(View.GONE); 
			}else if("4".equals(spn_WoqcngType.getValStr())){
				if(spn_WoqcngYxj != null){
					spn_WoqcngYxj.clearItems();
				}
				txv_WoqcngYxj.setVisibility(View.VISIBLE);
				spn_WoqcngYxj.setVisibility(View.VISIBLE);
				etx_WoqcngVend.setText("");
				etx_WoqcngVend_Name.reValue();
				etx_WoqcngRQty.reValue();
				getFocues(etx_WoqcngVend, true);
			}else if("5".equals(spn_WoqcngType.getValStr())){ //用不到
				etx_WoqcngVend.setText(vend);
				etx_WoqcngVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_WoqcngSI2, true);
				}else{
					getFocues(etx_WoqcngQty, true);
				}
				if(spn_WoqcngYxj != null){
					spn_WoqcngYxj.clearItems();
				}
				txv_WoqcngYxj.setVisibility(View.GONE);
				spn_WoqcngYxj.setVisibility(View.GONE); 
			}else if("6".equals(spn_WoqcngType.getValStr())){//用不到
				etx_WoqcngVend.setText(vend);
				etx_WoqcngVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_WoqcngSI2, true);
				}else{
					getFocues(etx_WoqcngQty, true);
				}
				if(spn_WoqcngYxj != null){
					spn_WoqcngYxj.clearItems();
				}
				txv_WoqcngYxj.setVisibility(View.GONE);
				spn_WoqcngYxj.setVisibility(View.GONE); 
			}else if("7".equals(spn_WoqcngType.getValStr())){//用不到
				etx_WoqcngVend.setText("");
				etx_WoqcngVend_Name.reValue();
				if(sType.equals("B")){
					getFocues(etx_WoqcngSI2, true);
				}else{
					getFocues(etx_WoqcngQty, true);
				}
				if(spn_WoqcngYxj != null){
					spn_WoqcngYxj.clearItems();
				}
				txv_WoqcngYxj.setVisibility(View.VISIBLE);
				spn_WoqcngYxj.setVisibility(View.VISIBLE);
			}else if("8".equals(spn_WoqcngType.getValStr())){//用不到
				etx_WoqcngVend.setText("");
				etx_WoqcngVend_Name.reValue();
				if(sType.equals("B")){
					getFocues(etx_WoqcngSI2, true);
				}else{
					getFocues(etx_WoqcngQty, true);
				}
				if(spn_WoqcngYxj != null){
					spn_WoqcngYxj.clearItems();
				}
				txv_WoqcngYxj.setVisibility(View.VISIBLE);
				spn_WoqcngYxj.setVisibility(View.VISIBLE);
			}
		}else{
			getFocues(etx_WoqcngScan, true);
		}
	}
	
	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//扫码光标离开事件相对应的代码
		if(etx_WoqcngScan.getId()==id){
			runClickFun();
			if(null != etx_WoqcngScan.getValStr() && !"".equals( etx_WoqcngScan.getValStr().toString().trim())){
				checkWoqcngScan();
				runClickFun();
			}else{
				istrue = true;
			}
		}
		
		//元凶件承担商光标离开事件相对应的代码 根据新的业务流程,规避该段代码执行
		if(etx_WoqcngVend.getId()==id){
			runClickFun();
			if(!"".equals(etx_WoqcngPart.getValStr().toString().trim()) && !"".equals( etx_WoqcngVend.getValStr().toString().trim())){
				if("4".equals(spn_WoqcngType.getValStr().toString().trim())){
					checkWoqcngBom();
				}else{
					checkWoqcngVend();
				}
				runClickFun();
			}else{
				istrue = true;
			}
		}
		//元凶件承担商光标离开事件相对应的代码
		if(etx_WoqcngRQty.getId()==id){
			runClickFun();
		}		
		
		return istrue;
	}
	
	
	//处理单号
	private void checkWoqcngScan() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Woqcngbiz.WoqcngScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_WoqcngScan.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					list =(List<Map<String, Object>>) map.get("LIST");
					mdtg_Split.buildData(list);
					if("S".equals(map.get("TYPE")+"")){ //类型为S的时候  是单件  没有子件把子件隐藏
						txv_WoqcngSI.setVisibility(View.GONE);
						etx_WoqcngSI.setVisibility(View.GONE);
						etx_WoqcngSI2.setVisibility(View.GONE);
						etx_WoqcngQty.setText("1");
						etx_WoqcngRQty.setText("1");
					}else if("B".equals(map.get("TYPE")+"")){  // 有明细标签
						txv_WoqcngSI.setVisibility(View.VISIBLE);
						etx_WoqcngSI.setVisibility(View.GONE);
						etx_WoqcngSI2.setVisibility(View.VISIBLE);
					}else{
						txv_WoqcngSI.setVisibility(View.VISIBLE);
						etx_WoqcngSI.setVisibility(View.VISIBLE);
						etx_WoqcngSI2.setVisibility(View.GONE);
					}
					etx_WoqcngPart.setText(map.get("RFLOT_PART")+"");
					etx_WoqcngPart_Desc.setText(map.get("RFLOT_PART_DESC")+"");
					etx_WoqcngUM.setText(map.get("RFLOT_UM")+"");
					etx_WoqcngWoNbr.setText(map.get("RFLOT_WOID")+"");
					etx_WoqcngShift.setText(map.get("SHIFT")+"");
					etx_WoqcngOp.setText(map.get("wrDesc")+"");
					vend = map.get("RFLOT_VEND")+"";
					vendName = map.get("RFLOT_VEND_NAME")+"";
					sType = map.get("TYPE")+"";
					runClickFun();
					getFocues(etx_WoqcngRQty, true);
					
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					list = null;
					mdtg_Split.buildData(list);
					etx_WoqcngPart.reValue();
					etx_WoqcngPart_Desc.reValue();
					etx_WoqcngUM.reValue();
					vend = "";
					vendName ="";
					sType ="";
					getFocues(etx_WoqcngScan, true);
				}
			}
		});
	}
	
	//根据供应商零件去找Bom
	private void checkWoqcngBom() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if("4".equals(spn_WoqcngType.getValStr().toString().trim())){
					if(vend.toUpperCase().equals(etx_WoqcngVend.getValStr().toUpperCase())){
						showErrorMsg("错误：料废承担方不是为生产供应商");
						return false;
					}
				}
				return true;
			}
			@Override
			public Object onGetData() {
				return Woqcngbiz.WoqcngBom(applicationDB.user.getUserDmain(), 
						applicationDB.user.getUserSite(), 
						etx_WoqcngPart.getValStr().toString(), 
						etx_WoqcngVend.getValStr().toString(),
						spn_WoqcngType.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					if(spn_WoqcngYxj != null){
						spn_WoqcngYxj.clearItems();
					}
					List<Map<String , Object>> bList = (List<Map<String, Object>>) map.get("bomList");
					for (int i = 0; i < bList.size(); i++) {
						Map sMap = bList.get(i);
						spn_WoqcngYxj.addItem(sMap.get("XSPS_COMP")+"",sMap.get("XSPS_COMP")+"");
					}
					etx_WoqcngVend_Name.setText(map.get("vendName")+"");
					mdtg_Split.buildData(list);
					getFocues(etx_WoqcngSI2, true);
				}else{
					istrue = false;
					etx_WoqcngVend.reValue();
					getFocues(etx_WoqcngVend, true);
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	//验证供应商是否存在，与零件是否是关系
	private void checkWoqcngVend() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Woqcngbiz.WoqcngBom(applicationDB.user.getUserDmain(), 
						applicationDB.user.getUserSite(), 
						etx_WoqcngPart.getValStr().toString(), 
						etx_WoqcngVend.getValStr().toString(),
						spn_WoqcngType.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					etx_WoqcngVend_Name.setText(map.get("vendName")+"");
				}else{
					istrue = false;
					etx_WoqcngVend.reValue();
					getFocues(etx_WoqcngVend, true);
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	/*private void updateUMVis(boolean flag) { 
		spn_WoqcngYxj.setEnabled(flag ? true : false);
		etx_WoqcngVend.setReadOnly(flag ? false : true);
	}	*/

	private void checkWoqcngScanSI(){
		loadDataBase(new IRunDataBaseListens() {  
			@Override
			public boolean onValidata() {
				if(etx_WoqcngSI2.getValStr().equals(etx_WoqcngScan.getValStr())){
					showErrorMsg("错误：扫描子项与扫码相同");
					return false;
				}else{
					return true;
				}
			}
			@Override
			public Object onGetData() {
				return Woqcngbiz.WoqcngScanSI(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_WoqcngScan.getValStr(),etx_WoqcngSI2.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					clearMsg();
					istrue=true;
					runClickFun();
					etx_WoqcngRQty.setText("1");
					etx_WoqcngQty.setText("1");
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					etx_WoqcngSI2.reValue();
					getFocues(etx_WoqcngSI2, true);
				}
			}
		});	
	}	

	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Save,ButtonType.Submit};
	}	
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if("0".equals(spn_WoqcngType.getValStr().toString().trim())){
			showErrorMsg("请选择分拆类型");
			istrue=false;
		}else if("1".equals(spn_WoqcngType.getValStr().toString().trim()) || 
				 "2".equals(spn_WoqcngType.getValStr().toString().trim()) ||
				 "3".equals(spn_WoqcngType.getValStr().toString().trim()) ||
				 "5".equals(spn_WoqcngType.getValStr().toString().trim()) ||
				 "6".equals(spn_WoqcngType.getValStr().toString().trim())){
			if(!"".equals(etx_WoqcngPart.getValStr().toString().trim())  && 
				!"".equals(etx_WoqcngVend.getValStr().toString().trim()) && 
				!"".equals(etx_WoqcngRQty.getValStr().toString().trim()) &&
				!"".equals(etx_WoqcngQty.getValStr().toString().trim())) {
				float qty = Float.parseFloat(etx_WoqcngQty.getValStr()); //标签数量
				float rQty = Float.parseFloat(etx_WoqcngRQty.getValStr()); //分拆数量
				if("S".equals(sType)){  //S 代表子件标签    没有子项了
					if(rQty > qty){ 
						showErrorMsg("分拆数量不能大于标签数量");
						istrue=false;
					}else{
						istrue=true;
					}
				}else{ // 必须要扫描  或者回填子项
					if(!"".equals(etx_WoqcngSI.getValStr().toString().trim()) || 
					   !"".equals(etx_WoqcngSI2.getValStr().toString().trim())){
						if(rQty > qty){ 
							showErrorMsg("分拆数量不能大于标签数量");
							istrue=false;
						}else{
							istrue=true;
						}
					}else{
						showErrorMsg("请扫描要分拆的子项");
						istrue=false;
					}
				}
			}else{
				showErrorMsg("扫码"+"\n"+"质检单"+"\n"+"承担方"+"\n"+"拆出量"+"\n"+"必填！！！");
				istrue=false;
			}
		}else if("4".equals(spn_WoqcngType.getValStr().toString().trim())){
			if(!"".equals(etx_WoqcngPart.getValStr().toString().trim()) && 
				!"".equals(etx_WoqcngVend.getValStr().toString().trim()) && 
				!"".equals(etx_WoqcngRQty.getValStr().toString().trim()) &&
				!"".equals(etx_WoqcngQty.getValStr().toString().trim())){
				float qty = Float.parseFloat(etx_WoqcngQty.getValStr()); //标签数量
				float rQty = Float.parseFloat(etx_WoqcngRQty.getValStr()); ///分拆数量
				if("S".equals(sType)){  //S 代表子件标签    没有子项了
					if(rQty > qty){ 
						showErrorMsg("分拆数量不能大于标签数量");
						istrue=false;
					}else{
						istrue=true;
					}
				}else{ // 必须要扫描  或者回填子项
					if(!"".equals(etx_WoqcngSI.getValStr().toString().trim()) || 
					   !"".equals(etx_WoqcngSI2.getValStr().toString().trim())){
						if(rQty > qty){ 
							showErrorMsg("分拆数量不能大于标签数量");
							istrue=false;
						}else{
							istrue=true;
						}
					}else{
						showErrorMsg("请扫描要分拆的子项");
						istrue=false;
					}
				}
				
			}else{
				showErrorMsg("扫码"+"\n"+"质检单"+"\n"+"承担方"+"\n"+"拆出量"+"\n"+"必填！！！");
				istrue=false;
			}
			
		}		
		return istrue;
	}

	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return Woqcngbiz.WoqcngSave(applicationDB.user.getUserDmain(), 
				applicationDB.user.getUserSite(),
				applicationDB.user.getUserId(),
				etx_WoqcngScan.getValStr(),
				etx_WoqcngPart.getValStr(),
				etx_WoqcngVend.getValStr(),
				StringUtil.isEmpty(etx_WoqcngSI.getValStr()) ? etx_WoqcngSI2.getValStr() : etx_WoqcngSI.getValStr(),
				etx_WoqcngQty.getValStr(),		
				etx_WoqcngRQty.getValStr(),
				spn_WoqcngType.getValStr(),
				spn_WoqcngYxj.getValStr());
	}
	
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			etx_WoqcngSI.reValue();
			etx_WoqcngSI2.reValue();
			//etx_WoqcngQty.reValue();
			etx_WoqcngRQty.reValue();
			/*if("3".equals(spn_WoqcngType.getValStr()) || "4".equals(spn_WoqcngType.getValStr())){
				etx_WoqcngVend.reValue();
				etx_WoqcngVend_Name.reValue();
			}
			if(spn_WoqcngYxj != null){
				spn_WoqcngYxj.clearItems();
			}*/
			list =(List<Map<String, Object>>) map.get("LIST");
			mdtg_Split.buildData(list);
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
		}
	}
	
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals(etx_WoqcngScan.getValStr().toString().trim()) && !"".equals(etx_WoqcngPart.getValStr().toString().trim())){
			if(list != null && list.size() >0){
				String sltType = "";
				for (int i = 0; i < list.size(); i++) {
					Map map = list.get(i);
					sltType += map.get("RFSLT_TYPE")+"";
				}
				if(!"".equals(sltType)){
					
				}else{
					showErrorMsg("不存在拆分记录");
					istrue=false;
				}
			}else{
				showErrorMsg("不存在拆分记录");
				istrue=false;
			}
			
		}else{
			showErrorMsg("扫码必输");
			istrue=false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return Woqcngbiz.WoqcngSub(applicationDB.user.getUserDmain(), 
				applicationDB.user.getUserSite(),
				applicationDB.user.getUserId(),
				etx_WoqcngScan.getValStr(),
				applicationDB.user.getUserDate());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			list = null;
			mdtg_Split.buildData(list);
			etx_WoqcngScan.reValue();
			etx_WoqcngPart.reValue();
			etx_WoqcngPart_Desc.reValue();
			etx_WoqcngUM.reValue();
			etx_WoqcngVend.reValue();
			etx_WoqcngVend_Name.reValue();
			vend = "";
			vendName ="";
			sType ="";
			etx_WoqcngSI.reValue();
			etx_WoqcngSI2.reValue();
			etx_WoqcngQty.reValue();
			etx_WoqcngRQty.reValue();
			etx_WoqcngWoNbr.reValue();
			etx_WoqcngShift.reValue();
			etx_WoqcngOp.reValue();
			if(spn_WoqcngYxj != null){
				spn_WoqcngYxj.clearItems();
			}
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
		}
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
}
