package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.profitles.biz.LoclpBiz;
import com.profitles.biz.SendBiz;
import com.profitles.biz.XBoxBiz;
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
import com.profitles.framwork.util.StringUtil;


public class LocLPActivity extends AppFunActivity {
	private LoclpBiz loclpBiz;
	/***
	 * @+id/txv_loclpParts"
		@+id/etx_loclpParts"
		@+id/txv_loclpLots"
		@+id/etx_loclpLots"
		@+id/txv_loclpLocs"
		@+id/etx_loclpLocs"
		@+id/txv_loclpBins"
		@+id/etx_loclpBins"
		@+id/txv_loclpAllQty"
		@+id/etx_loclpAllQty"
		@+id/txv_loclpMultQty"
		@+id/etx_loclpMultQty"
		@+id/txv_loclpBoxNum"
		@+id/etx_loclpBoxNum"
		@+id/txv_loclpSanNum"
		@+id/etx_loclpSanNum"
		@+id/etx_loclpLineId"
		@+id/dtg_LocLpInfo" 
	 * */
	private MyReadBQ  etxLoclpParts, etxLoclpLots, etxLoclpLocs, etxLoclpBins;
	private MyEditText etxLoclpAllQty, etxLoclpMultQty, etxLoclpBoxNum, etxLoclpSanNum,
			etxLoclpLineId;
	private MyDataGrid dtgLoclpList;
	private ApplicationDB applicationDB;
	private List<Map<String , Object>> locList = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_loclp;
	}

	@Override
	protected void pageLoad() {
		loclpBiz = new LoclpBiz();
		etxLoclpParts = (MyReadBQ) findViewById(R.id.etx_loclpParts);
		etxLoclpLots = (MyReadBQ) findViewById(R.id.etx_loclpLots);
		etxLoclpLocs = (MyReadBQ) findViewById(R.id.etx_loclpLocs);
		etxLoclpBins = (MyReadBQ) findViewById(R.id.etx_loclpBins);
		
		etxLoclpAllQty = (MyEditText) findViewById(R.id.etx_loclpAllQty);
		etxLoclpMultQty = (MyEditText) findViewById(R.id.etx_loclpMultQty);
		etxLoclpBoxNum = (MyEditText) findViewById(R.id.etx_loclpBoxNum);
		etxLoclpSanNum = (MyEditText) findViewById(R.id.etx_loclpSanNum);
		etxLoclpLineId = (MyEditText) findViewById(R.id.etx_loclpLineId);

		dtgLoclpList = (MyDataGrid) findViewById(R.id.dtg_LoclpList);
		
		
		dtgLoclpList.setOnMyDataGridListener(new OnMyDataGridListener() {
			@Override	//长按事件
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowData) {
				return false;
			}
			
			@Override	//单元格事件
			public void onItemClick(View view,Object val, int rowIndex, int colIndex,Map<String, Object> rowData) {
				//showSuccessMsg(dtgSend.getSelValue()+"   " + rowIndex + "   " + colIndex);
				if(rowIndex == 0) return;
				//Map<String, Object> map = dtgSend.getRowDataByIndex(rowIndex);
//				Map<String, Object> map = dtgLoclpList.getRowDataByKey(rowIndex);
				etxLoclpLineId.setText(rowIndex+"");
				if(StringUtil.parseDouble(rowData.get("ALL_QTY")) < 0){
					etxLoclpAllQty.setText("0");
					etxLoclpMultQty.setText(rowData.get("MULT_QTY") == null ? "0" : rowData.get("MULT_QTY")+"");
					etxLoclpBoxNum.setText("0");
					etxLoclpSanNum.setText("0");
				}else{
					etxLoclpAllQty.setText(rowData.get("ALL_QTY") == null ? "0" : rowData.get("ALL_QTY")+"");
					etxLoclpMultQty.setText(rowData.get("MULT_QTY") == null ? "0" : rowData.get("MULT_QTY")+"");
					etxLoclpBoxNum.setText(rowData.get("BOX_NUM") == null ? "0" : rowData.get("BOX_NUM")+"");
					etxLoclpSanNum.setText(rowData.get("SAN_NUM") == null ? "0" : rowData.get("SAN_NUM")+"");
				}
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
		//扫码光标离开事件相对应的代码
		
		if(etxLoclpAllQty.getId()==id){
			runClickFun();
			if(StringUtil.isEmpty(etxLoclpAllQty.getValStr() ))  return false ; 
			setqty1();
		}
		if(etxLoclpMultQty.getId()==id){
			runClickFun();
			if(StringUtil.isEmpty(etxLoclpMultQty.getValStr() ))  return false ; 
			setqty1();
		}
		if(etxLoclpBoxNum.getId()==id){
			runClickFun();
			if(StringUtil.isEmpty(etxLoclpBoxNum.getValStr() ))  return false ; 
			setqty2();
		}
		if(etxLoclpSanNum.getId()==id){
			runClickFun();
			if(StringUtil.isEmpty(etxLoclpSanNum.getValStr() ))  return false ; 
			setqty2();
		}
		return istrue ;
	}
	
 	
	private void setqty1() {
		// TODO Auto-generated method stub
 		if(StringUtil.parseDouble(etxLoclpMultQty.getValStr()) == 0){
			etxLoclpBoxNum.setText("0");
			etxLoclpSanNum.setText(etxLoclpAllQty.getValStr());
		}else{
			etxLoclpBoxNum.setText((int)Math.floor(StringUtil.mathDivide2(etxLoclpAllQty.getValStr(),etxLoclpMultQty.getValStr())) + "");
			etxLoclpSanNum.setText(StringUtil.mathSubtract( etxLoclpAllQty.getValStr() , 
					StringUtil.mathMultiply2( 
							Math.floor(StringUtil.mathDivide2(etxLoclpAllQty.getValStr(),etxLoclpMultQty.getValStr())) 
							, etxLoclpMultQty.getValStr())  ) +"") ;
		}
	}
	private void setqty2() {
		etxLoclpAllQty.setText( StringUtil.mathAdd( 
					StringUtil.mathMultiply2( etxLoclpBoxNum.getValStr() , etxLoclpMultQty.getValStr()) 
					,etxLoclpSanNum.getValStr()) +"") ; 
	}

	/*
	 * 添加按钮
	 * */
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Search,ButtonType.Print};
	}

	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		etxLoclpLineId.reValue();
		etxLoclpAllQty.reValue();
		etxLoclpMultQty.reValue();
		etxLoclpBoxNum.reValue();
		etxLoclpSanNum.reValue();
		return true;
	}

	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
			String part = etxLoclpParts.getValStr().toString().trim();
			String lot = etxLoclpLots.getValStr().toString().trim();
			String loc = etxLoclpLocs.getValStr().toString().trim();
			String bin = etxLoclpBins.getValStr().toString().trim();
			return loclpBiz.getList(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),part, lot, loc,bin,applicationDB.user.getUserId(),applicationDB.user.getMac());
	}
	
	@Override
	public void OnBtnSerCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		// showSuccessMsg(wr.isSuccess()+"");
		locList = (List<Map<String, Object>>) wr.getDataToList();
		dtgLoclpList.buildData(locList);
		/*if(wr.isSuccess()){
			List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
			Map<String ,Object> map = wr.getDataToMap();
			locList = (List<Map<String, Object>>) map.get("sendList");
			dtgLoclpList.buildData((List<Map<String, Object>>)data);
			// showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
		}*/
	}
	
	
	@Override
	public boolean OnBtnPntValidata(ButtonType btnType, View v) {
		StringBuffer mesBuffer = new StringBuffer("");
		if(StringUtil.isEmpty(etxLoclpLineId.getValStr())){
			mesBuffer.append("请选择一行库存数据;");
		}
		if( StringUtil.isEmpty(etxLoclpAllQty.getValStr() ) || 0 == StringUtil.parseDouble(etxLoclpAllQty.getValStr()) ){
			mesBuffer.append("数量不能为空能为0;");
		}
		if( StringUtil.isEmpty(etxLoclpMultQty.getValStr() )){
			mesBuffer.append("单包量不能为空;");
		}
		if( StringUtil.isEmpty(etxLoclpBoxNum.getValStr() )){
			mesBuffer.append("箱数不能为空;");
		}
		if( StringUtil.isEmpty(etxLoclpBoxNum.getValStr() )){
			mesBuffer.append("散件不能为空;");
		}
		if(StringUtil.parseDouble(etxLoclpAllQty.getValStr()) - 
		StringUtil.parseDouble(etxLoclpMultQty.getValStr())*StringUtil.parseDouble(etxLoclpBoxNum.getValStr())- 
		StringUtil.parseDouble(etxLoclpSanNum.getValStr()) != 0 ){
			mesBuffer.append(StringUtil.parseDouble(etxLoclpAllQty.getValStr()) - 
					StringUtil.parseDouble(etxLoclpMultQty.getValStr())*StringUtil.parseDouble(etxLoclpBoxNum.getValStr())- 
					StringUtil.parseDouble(etxLoclpSanNum.getValStr()));
		}
		if(mesBuffer.length() > 0 ){
			showErrorMsg(mesBuffer.toString());
			istrue = false;
			return istrue;
		}
		istrue = true ;
		return istrue ;
	}
	
	@Override
	public Object OnBtnPntClick(ButtonType btnType, View v) {
		Map<String, Object> map = dtgLoclpList.getRowDataByKey(StringUtil.parseInt(etxLoclpLineId.getValStr())); 
		
		return loclpBiz.locPrint(applicationDB.user.getUserDmain(), 
				applicationDB.user.getUserSite(), 
				map.get("PART")+"", 
				StringUtil.isEmpty(map.get("LOT")) ? "" : map.get("LOT")+"", 
				StringUtil.isEmpty(map.get("LOC")) ? "" : map.get("LOC")+"", 
				StringUtil.isEmpty(map.get("BIN")) ? "" : map.get("BIN")+"",
				StringUtil.isEmpty(map.get("XSLD_REF")) ? "" : map.get("XSLD_REF")+"", 
				etxLoclpAllQty.getValStr(), 
				etxLoclpMultQty.getValStr(), 
				etxLoclpBoxNum.getValStr(), 
				etxLoclpSanNum.getValStr(), 
				StringUtil.isEmpty(map.get("XSLD_STATUS")) ? "" : map.get("XSLD_STATUS")+"", 
				StringUtil.isEmpty(map.get("VEND")) ? "" : map.get("VEND")+"", 
				applicationDB.user.getUserId(), 
				applicationDB.user.getMac());
	}
		
	@Override
	public void OnBtnPntCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxLoclpLineId.reValue();
			etxLoclpAllQty.reValue();
			etxLoclpMultQty.reValue();
			etxLoclpBoxNum.reValue();
			etxLoclpSanNum.reValue();
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



