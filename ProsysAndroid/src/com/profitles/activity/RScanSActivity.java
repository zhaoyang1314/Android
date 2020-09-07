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
import com.profitles.biz.RScanSBiz;
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

public class RScanSActivity extends AppFunActivity {
	private RScanSBiz RScanSbiz;
	private MyTextView txv_RScansYxj;
	private MyEditText etx_RScansPart,etx_RScansINbr,etx_RScansVend,etx_RScansVend_Name,etx_RScansSI,
		etx_RScansQty,etx_RScansRQty,etx_RScansPart_Desc,etx_RScansUM;
	private MyReadBQ  etx_RScansScan,etx_RScansSI2;
	private MySpinner spn_RScansType,spn_RScansYxj;
	private String vend = "",vendName = "",sType = "",poType = "";
	private MyDataGrid mdtg_Split;	
	private ApplicationDB applicationDB;
	private List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
	private View backRow;
	private int checkRowIndex;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_rscans;
	}

	@Override
	protected void pageLoad() {
		RScanSbiz=new RScanSBiz();
		etx_RScansScan =(MyReadBQ) findViewById(R.id.etx_RScansScan);
		etx_RScansSI2 =(MyReadBQ) findViewById(R.id.etx_RScansSI2);
		etx_RScansPart = (MyEditText) findViewById(R.id.etx_RScansPart);
		etx_RScansPart_Desc = (MyEditText) findViewById(R.id.etx_RScansPart_Desc);
		etx_RScansUM = (MyEditText) findViewById(R.id.etx_RScansUM);
		etx_RScansINbr = (MyEditText) findViewById(R.id.etx_RScansINbr);
		etx_RScansVend = (MyEditText) findViewById(R.id.etx_RScansVend);
		etx_RScansVend_Name = (MyEditText) findViewById(R.id.etx_RScansVend_Name);
		etx_RScansSI = (MyEditText) findViewById(R.id.etx_RScansSI);
		etx_RScansQty = (MyEditText) findViewById(R.id.etx_RScansQty);
		etx_RScansRQty = (MyEditText) findViewById(R.id.etx_RScansRQty);
		spn_RScansType = (MySpinner) findViewById(R.id.spn_RScansType);
		txv_RScansYxj = (MyTextView) findViewById(R.id.txv_RScansYxj);
		spn_RScansYxj = (MySpinner) findViewById(R.id.spn_RScansYxj);
		
		mdtg_Split = (MyDataGrid) findViewById(R.id.mdtg_Split);
		
		checkRScansType();
		
		//mdtg_cons = (MyDataGrid)findViewById(R.id.mdtg_cons);
		etx_RScansScan.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_RScansScan.getValStr() && !"".equals( etx_RScansScan.getValStr().toString().trim())){
					checkRScansScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			} 
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		spn_RScansType.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				changedRScansType();
			};
			public void onNothingSelected(AdapterView<?> parent){};
		});
		etx_RScansSI2.setVisibility(View.GONE); //暂时隐藏扫描的子项
		
		etx_RScansSI2.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!"".equals( etx_RScansPart.getValStr().toString().trim()) && !"".equals( etx_RScansSI2.getValStr().toString().trim())
						&& !"0".equals( spn_RScansType.getValStr().toString().trim())){
					checkRScansScanSI();
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
						etx_RScansSI.setText(lot);
					}else{
						etx_RScansSI2.setText(lot);
					}
				}else{
					etx_RScansSI.setText("");
					etx_RScansSI2.setText("");
				}
				if(!"null".equals(qty) && !"".equals(qty)){
					etx_RScansQty.setText(qty);
				}else{
					etx_RScansQty.setText("");
				}
				//etx_RScansSI.setText(StringUtil.isEmpty(rowData.get("RFLOTD_MFG_LOT")+"") ? "" : rowData.get("RFLOTD_MFG_LOT")+"");
				//etx_RScansQty.setText(StringUtil.isEmpty(rowData.get("RFLOTD_MFG_PKG_QTY")+"") ? "" : rowData.get("RFLOTD_MFG_PKG_QTY")+"");
				//etx_RScansRQty.setText(StringUtil.isEmpty(rowData.get("RFSLT_SPT_QTY")+"") ? "" : rowData.get("RFSLT_SPT_QTY")+"");
				getFocues(etx_RScansRQty, true);
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

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//扫码光标离开事件相对应的代码
		if(etx_RScansScan.getId()==id){
			runClickFun();
			if(null != etx_RScansScan.getValStr() && !"".equals( etx_RScansScan.getValStr().toString().trim())){
				checkRScansScan();
				runClickFun();
			}else{
				istrue = true;
			}
		}
		
		//元凶件承担商光标离开事件相对应的代码 根据二期的需求进行规避改代码验证
		if(etx_RScansVend.getId()==id){
			runClickFun();
			if(!"".equals(etx_RScansPart.getValStr().toString().trim()) && !"".equals( etx_RScansVend.getValStr().toString().trim()) && "S".equals(poType)){
				if("3".equals(spn_RScansType.getValStr().toString().trim())||
				   "4".equals(spn_RScansType.getValStr().toString().trim())){
					checkRScansBom();
				}
				runClickFun();
			}else{
				istrue = true;
			}
		}
		//元凶件承担商光标离开事件相对应的代码
		if(etx_RScansRQty.getId()==id){
			runClickFun();
		}		
		
		return istrue;
	}
	
	private void checkRScansType() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return RScanSbiz.rScansType(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					spn_RScansType.addItem("请选择拆分类型","0");
					Map<String ,Object> map = wr.getDataToMap();
					List<Map<String , Object>> tList = (List<Map<String, Object>>) map.get("LIST");
					for (int i = 0; i < tList.size(); i++) {
						Map sMap = tList.get(i);
						spn_RScansType.addItem(sMap.get("NAME")+"",sMap.get("CODE")+"");
					}
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	//处理单号
	private void checkRScansScan() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return RScanSbiz.rScansScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_RScansScan.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					list =(List<Map<String, Object>>) map.get("LIST");
					mdtg_Split.buildData(list);
					if("B".equals(map.get("TYPE")+"")){  // 有明细标签
						etx_RScansSI.setVisibility(View.GONE);
						etx_RScansSI2.setVisibility(View.VISIBLE);
					}else{
						etx_RScansSI.setVisibility(View.VISIBLE);
						etx_RScansSI2.setVisibility(View.GONE);
					}
					etx_RScansPart.setText(map.get("RFLOT_PART")+"");
					etx_RScansPart_Desc.setText(map.get("RFLOT_PART_DESC")+"");
					etx_RScansUM.setText(map.get("RFLOT_UM")+"");
					String iNbr = map.get("RFLOT_IQC_NBR")+"";
					if("0".equals(iNbr)){
						etx_RScansINbr.setText("");
					}else{
						etx_RScansINbr.setText(iNbr);
					}
					//etx_RScansQty.setText(map.get("RFLOT_MULT_QTY")+"");
					vend = map.get("RFLOT_VEND")+"";
					vendName = map.get("RFLOT_VEND_NAME")+"";
					sType = map.get("TYPE")+"";
					poType = map.get("poType")+"";
					spn_RScansType.clearItems();
					if("P".equals(poType)){
						spn_RScansType.addItem("请选择拆分类型","0");
						List<Map<String , Object>> tList = (List<Map<String, Object>>) map.get("splList");
						for (int i = 0; i < tList.size(); i++) {
							Map sMap = tList.get(i);
							spn_RScansType.addItem(sMap.get("NAME")+"",sMap.get("CODE")+"");
						}
					}else{
						spn_RScansType.addItem("请选择拆分类型","0");
						List<Map<String , Object>> tList = (List<Map<String, Object>>) map.get("splList");
						for (int i = 0; i < tList.size(); i++) {
							Map sMap = tList.get(i);
							spn_RScansType.addItem(sMap.get("NAME")+"",sMap.get("CODE")+"");
						}
					}
					if("S".equals(poType)){
						txv_RScansYxj.setVisibility(View.VISIBLE);
						spn_RScansYxj.setVisibility(View.VISIBLE);
					}else{
						txv_RScansYxj.setVisibility(View.GONE); //隐藏
						spn_RScansYxj.setVisibility(View.GONE); //隐藏
					}
					runClickFun();
					getFocues(etx_RScansRQty, true);
					
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					list = null;
					mdtg_Split.buildData(list);
					etx_RScansPart.reValue();
					etx_RScansPart_Desc.reValue();
					etx_RScansUM.reValue();
					etx_RScansINbr.reValue();
					vend = "";
					vendName ="";
					sType ="";
					poType ="";
					getFocues(etx_RScansScan, true);
				}
			}
		});
	}
	
	//根据供应商零件去找Bom
	private void checkRScansBom() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if("4".equals(spn_RScansType.getValStr().toString().trim())){
					if(vend.toUpperCase().equals(etx_RScansVend.getValStr().toUpperCase())){
						showErrorMsg("错误：料废承担方不是为生产供应商");
						return false;
					}
				}
				return true;
			}
			@Override
			public Object onGetData() {
				return RScanSbiz.rScansBom(applicationDB.user.getUserDmain(), 
						applicationDB.user.getUserSite(), 
						etx_RScansPart.getValStr().toString(), 
						etx_RScansVend.getValStr().toString(),
						spn_RScansType.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					if(spn_RScansYxj != null){
						spn_RScansYxj.clearItems();
					}
					List<Map<String , Object>> bList = (List<Map<String, Object>>) map.get("bomList");
					for (int i = 0; i < bList.size(); i++) {
						Map sMap = bList.get(i);
						spn_RScansYxj.addItem(sMap.get("XSPS_COMP")+"",sMap.get("XSPS_COMP")+"");
					}
					etx_RScansVend_Name.setText(map.get("vendName")+"");
					mdtg_Split.buildData(list);
					getFocues(etx_RScansSI2, true);
				}else{
					istrue = false;
					etx_RScansVend.reValue();
					getFocues(etx_RScansVend, true);
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	private void changedRScansType() {
		if(!"".equals(etx_RScansPart.getValStr())){
			if("1".equals(spn_RScansType.getValStr())){
				etx_RScansVend.setText(vend);
				etx_RScansVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_RScansSI2, true);
				}else{
					getFocues(etx_RScansQty, true);
				}
				if(spn_RScansYxj != null){
					spn_RScansYxj.clearItems();
				}
				txv_RScansYxj.setVisibility(View.GONE);
				spn_RScansYxj.setVisibility(View.GONE); 
			}else if("2".equals(spn_RScansType.getValStr())){
				etx_RScansVend.setText(vend);
				etx_RScansVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_RScansSI2, true);
				}else{
					getFocues(etx_RScansQty, true);
				}
				if(spn_RScansYxj != null){
					spn_RScansYxj.clearItems();
				}
				txv_RScansYxj.setVisibility(View.GONE);
				spn_RScansYxj.setVisibility(View.GONE); 
			}else if("3".equals(spn_RScansType.getValStr())){
				if(spn_RScansYxj != null){
					spn_RScansYxj.clearItems();
				}
				if(poType != "" && "S".equals(poType)){
					txv_RScansYxj.setVisibility(View.VISIBLE);
					spn_RScansYxj.setVisibility(View.VISIBLE); 
					etx_RScansVend.setText("");
					etx_RScansVend_Name.reValue();
					etx_RScansRQty.reValue();
					getFocues(etx_RScansVend, true);
				}else{
					etx_RScansVend.setText(vend);
					etx_RScansVend_Name.setText(vendName);
					if(sType.equals("B")){
						getFocues(etx_RScansSI2, true);
					}else{
						getFocues(etx_RScansQty, true);
					}
				}
				
			}else if("4".equals(spn_RScansType.getValStr())){
				if(spn_RScansYxj != null){
					spn_RScansYxj.clearItems();
				}
				if(poType != "" && "S".equals(poType)){
					txv_RScansYxj.setVisibility(View.VISIBLE);
					spn_RScansYxj.setVisibility(View.VISIBLE); 
					etx_RScansVend.setText("");
					etx_RScansVend_Name.reValue();
					etx_RScansRQty.reValue();
					getFocues(etx_RScansVend, true);
				}else{
					etx_RScansVend.setText(vend);
					etx_RScansVend_Name.setText(vendName);
					if(sType.equals("B")){
						getFocues(etx_RScansSI2, true);
					}else{
						getFocues(etx_RScansQty, true);
					}
				}
			}else if("5".equals(spn_RScansType.getValStr())){
				etx_RScansVend.setText(vend);
				etx_RScansVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_RScansSI2, true);
				}else{
					getFocues(etx_RScansQty, true);
				}
				if(spn_RScansYxj != null){
					spn_RScansYxj.clearItems();
				}
				txv_RScansYxj.setVisibility(View.GONE);
				spn_RScansYxj.setVisibility(View.GONE); 
			}else if("6".equals(spn_RScansType.getValStr())){
				etx_RScansVend.setText(vend);
				etx_RScansVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_RScansSI2, true);
				}else{
					getFocues(etx_RScansQty, true);
				}
				if(spn_RScansYxj != null){
					spn_RScansYxj.clearItems();
				}
				txv_RScansYxj.setVisibility(View.GONE);
				spn_RScansYxj.setVisibility(View.GONE); 
			}else if("7".equals(spn_RScansType.getValStr())){
				etx_RScansVend.setText(vend);
				etx_RScansVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_RScansSI2, true);
				}else{
					getFocues(etx_RScansQty, true);
				}
				if(spn_RScansYxj != null){
					spn_RScansYxj.clearItems();
				}
				txv_RScansYxj.setVisibility(View.GONE);
				spn_RScansYxj.setVisibility(View.GONE); 
			}else if("8".equals(spn_RScansType.getValStr())){
				etx_RScansVend.setText(vend);
				etx_RScansVend_Name.setText(vendName);
				if(sType.equals("B")){
					getFocues(etx_RScansSI2, true);
				}else{
					getFocues(etx_RScansQty, true);
				}
				if(spn_RScansYxj != null){
					spn_RScansYxj.clearItems();
				}
				txv_RScansYxj.setVisibility(View.GONE);
				spn_RScansYxj.setVisibility(View.GONE); 
			}
		}else{
			getFocues(etx_RScansScan, true);
		}
	}	
	
	/*private void updateUMVis(boolean flag) { 
		spn_RScansYxj.setEnabled(flag ? true : false);
		etx_RScansVend.setReadOnly(flag ? false : true);
	}	*/

	private void checkRScansScanSI(){
		loadDataBase(new IRunDataBaseListens() {  
			@Override
			public boolean onValidata() {
				if(etx_RScansSI2.getValStr().equals(etx_RScansScan.getValStr())){
					showErrorMsg("错误：扫描子项与扫码相同");
					return false;
				}else{
					return true;
				}
			}
			@Override
			public Object onGetData() {
				return RScanSbiz.rScansScanSI(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_RScansScan.getValStr(),etx_RScansSI2.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					clearMsg();
					istrue=true;
					runClickFun();
					etx_RScansRQty.setText("1");
					etx_RScansQty.setText("1");
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					etx_RScansSI2.reValue();
					getFocues(etx_RScansSI2, true);
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
		if("0".equals(spn_RScansType.getValStr().toString().trim())){
			showErrorMsg("请选择分拆类型");
			istrue=false;
		}else if("1".equals(spn_RScansType.getValStr().toString().trim()) || 
				 "2".equals(spn_RScansType.getValStr().toString().trim()) ||
				 "5".equals(spn_RScansType.getValStr().toString().trim()) ||
				 "6".equals(spn_RScansType.getValStr().toString().trim())){
			if(!"".equals(etx_RScansPart.getValStr().toString().trim())  && 
				!"".equals(etx_RScansVend.getValStr().toString().trim()) && 
				!"".equals(etx_RScansRQty.getValStr().toString().trim()) &&
				!"".equals(etx_RScansQty.getValStr().toString().trim())) {
				float qty = Float.parseFloat(etx_RScansQty.getValStr()); //标签数量
				float rQty = Float.parseFloat(etx_RScansRQty.getValStr()); //分拆数量
				if("S".equals(sType)){  //S 代表子件标签    没有子项了
					if(rQty >= qty){ 
						showErrorMsg("分拆数量不能大于等于标签数量");
						istrue=false;
					}else{
						istrue=true;
					}
				}else{ // 必须要扫描  或者回填子项
					if(!"".equals(etx_RScansSI.getValStr().toString().trim()) || 
					   !"".equals(etx_RScansSI2.getValStr().toString().trim())){
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
				showErrorMsg("扫码"+"\n"+"质检单"+"\n"+"子项"+"\n"+"拆出量"+"\n"+"必填！！！");
				istrue=false;
			}
		}else if("3".equals(spn_RScansType.getValStr().toString().trim()) || 
				 "4".equals(spn_RScansType.getValStr().toString().trim())){
			if(!"".equals(etx_RScansPart.getValStr().toString().trim()) && 
				!"".equals(etx_RScansVend.getValStr().toString().trim()) && 
				!"".equals(etx_RScansRQty.getValStr().toString().trim()) &&
				!"".equals(etx_RScansQty.getValStr().toString().trim())){
				float qty = Float.parseFloat(etx_RScansQty.getValStr()); //标签数量
				float rQty = Float.parseFloat(etx_RScansRQty.getValStr()); ///分拆数量
				if("S".equals(sType)){  //S 代表子件标签    没有子项了
					if(rQty >= qty){ 
						showErrorMsg("分拆数量不能大于等于标签数量");
						istrue=false;
					}else{
						istrue=true;
					}
				}else{ // 必须要扫描  或者回填子项
					if(!"".equals(etx_RScansSI.getValStr().toString().trim()) || 
					   !"".equals(etx_RScansSI2.getValStr().toString().trim())){
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
				showErrorMsg("扫码"+"\n"+"质检单"+"\n"+"子项"+"\n"+"拆出量"+"\n"+"必填！！！");
				istrue=false;
			}
			
		}		
		return istrue;
	}

	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return RScanSbiz.rScansSave(applicationDB.user.getUserDmain(), 
				applicationDB.user.getUserSite(),
				applicationDB.user.getUserId(),
				etx_RScansScan.getValStr(),
				etx_RScansPart.getValStr(),
				etx_RScansINbr.getValStr(),
				etx_RScansVend.getValStr(),
				StringUtil.isEmpty(etx_RScansSI.getValStr()) ? etx_RScansSI2.getValStr() : etx_RScansSI.getValStr(),
				etx_RScansQty.getValStr(),		
				etx_RScansRQty.getValStr(),
				spn_RScansType.getValStr(),
				spn_RScansYxj.getValStr());
	}
	
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			etx_RScansSI.reValue();
			etx_RScansSI2.reValue();
			etx_RScansQty.reValue();
			etx_RScansRQty.reValue();
			/*if("3".equals(spn_RScansType.getValStr()) || "4".equals(spn_RScansType.getValStr())){
				etx_RScansVend.reValue();
				etx_RScansVend_Name.reValue();
			}
			if(spn_RScansYxj != null){
				spn_RScansYxj.clearItems();
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
		if(!"".equals(etx_RScansScan.getValStr().toString().trim()) && !"".equals(etx_RScansPart.getValStr().toString().trim())){
			if(list != null && list.size() >0){
				String sltType = "";
				for (int i = 0; i < list.size(); i++) {
					Map map = list.get(i);
					sltType += map.get("RFSLT_TYPE")+"";
				}
				if(!StringUtil.isEmpty(sltType)){
					
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
		return RScanSbiz.rScansSub(applicationDB.user.getUserDmain(), 
				applicationDB.user.getUserSite(),
				applicationDB.user.getUserId(),
				etx_RScansScan.getValStr(),
				applicationDB.user.getUserDate());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			list = null;
			mdtg_Split.buildData(list);
			etx_RScansScan.reValue();
			etx_RScansPart.reValue();
			etx_RScansPart_Desc.reValue();
			etx_RScansUM.reValue();
			etx_RScansINbr.reValue();
			etx_RScansVend.reValue();
			etx_RScansVend_Name.reValue();
			vend = "";
			vendName ="";
			sType ="";
			poType ="";
			etx_RScansSI.reValue();
			etx_RScansSI2.reValue();
			etx_RScansQty.reValue();
			etx_RScansRQty.reValue();
			if(spn_RScansYxj != null){
				spn_RScansYxj.clearItems();
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
