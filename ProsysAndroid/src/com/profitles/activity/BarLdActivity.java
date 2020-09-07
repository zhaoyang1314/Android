package com.profitles.activity;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;

import com.profitles.biz.BarLdBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

public class BarLdActivity extends AppFunActivity {

	private BarLdBiz barldBiz;
	//private MyReadBQ extBarldBar,etxScanBin;
	private MyReadBQ extBarldBar;
	private MyEditText etxBarldType, etxBarldStatus, etxBarldNumlbl, etxBarldPart, etxBarldUm, 
					etxBarldPartNm, etxBarldLot, etxBarldPartRev, etxBarldAddr, etxBarldAddrNm, 
					etxBarldSite, etxBarldPlQty, etxBarldBin, etxBarldBinQty, etxBarldLoc,
					etxBarldLocQty, etxBarldStatue, etxBarldNbr, etxBarldDrtn, etxBarldRef, 
					etxBarldColor, etxBarldParScan, etxBarldCrtUser, etxBarldCrtDate, etxBarldScan;
	// private MyDataGrid mdtgBarldGrid;
	// private List<Map<String, Object>> barldlist = new ArrayList<Map<String, Object>>();

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_barld;
	}

	@Override
	protected void pageLoad() {
		barldBiz = new BarLdBiz();
		
		extBarldBar = (MyReadBQ) findViewById(R.id.etx_barldBar);
		//etxScanBin = (MyReadBQ) findViewById(R.id.etx_ScanBin);
		
		etxBarldType = (MyEditText) findViewById(R.id.etx_barldType);
		etxBarldStatus = (MyEditText) findViewById(R.id.etx_barldStatus);
		etxBarldNumlbl = (MyEditText) findViewById(R.id.etx_barldNumlbl);
		etxBarldPart = (MyEditText) findViewById(R.id.etx_barldPart);
		etxBarldUm = (MyEditText) findViewById(R.id.etx_barldUm);
		etxBarldPartNm = (MyEditText) findViewById(R.id.etx_barldPartNm);
		etxBarldLot = (MyEditText) findViewById(R.id.etx_barldLot);
		etxBarldPartRev = (MyEditText) findViewById(R.id.etx_barldPartRev);
		etxBarldAddr = (MyEditText) findViewById(R.id.etx_barldAddr);
		etxBarldAddrNm = (MyEditText) findViewById(R.id.etx_barldAddrNm);
		etxBarldSite = (MyEditText) findViewById(R.id.etx_barldSite);
		etxBarldPlQty = (MyEditText) findViewById(R.id.etx_barldPlQty);
		etxBarldBin = (MyEditText) findViewById(R.id.etx_barldBin);
		etxBarldBinQty = (MyEditText) findViewById(R.id.etx_barldBinQty);
		etxBarldLoc = (MyEditText) findViewById(R.id.etx_barldLoc);
		etxBarldLocQty = (MyEditText) findViewById(R.id.etx_barldLocQty);
		etxBarldStatue = (MyEditText) findViewById(R.id.etx_barldStatue);
		etxBarldNbr = (MyEditText) findViewById(R.id.etx_barldNbr);
		etxBarldDrtn = (MyEditText) findViewById(R.id.etx_barldDrtn);
		etxBarldRef = (MyEditText) findViewById(R.id.etx_barldRef);
		etxBarldColor = (MyEditText) findViewById(R.id.etx_barldColor);
		etxBarldParScan = (MyEditText) findViewById(R.id.etx_barldParScan);
		etxBarldCrtUser = (MyEditText) findViewById(R.id.etx_barldCrtUser);
		etxBarldCrtDate = (MyEditText) findViewById(R.id.etx_barldCrtDate);
		etxBarldScan = (MyEditText) findViewById(R.id.etx_barldScan);
		
		
		extBarldBar.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!extBarldBar.getValStr().equals("")){
					changeBar();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		// mdtgBarldGrid = (MyDataGrid) findViewById(R.id.mdtg_barldGrid);
		runClickFun();
	}

	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Clear, ButtonType.Return, ButtonType.Block};
	}
	protected void unLockNbr() {
	}
	
	protected boolean onBlur(int id, View v) {
		if(R.id.etx_barldBar==id){  
			runClickFun();
		}
		/*if(etxScanBin.getId() == id){
			runClickFun();
			if(null != etxScanBin.getValStr() && !"".equals(etxScanBin.getValStr().toString().trim())){
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						return true;
					}
					@Override
					public Object onGetData() {
						return barldBiz.getScanBin(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite(),
								etxScanBin.getValStr(),
								etxBarldPart.getValStr(),
								etxBarldLot.getValStr(),
								extBarldBar.getValStr(),
								etxBarldAddr.getValStr(),
								ApplicationDB.user.getUserId());
					}
					@Override
					public void onCallBrack(Object data) {
						WebResponse wr = (WebResponse)data;
						if(wr.isSuccess()){
							Map<String ,Object> map = wr.getDataToMap();
							etxBarldLoc.setText(StringUtil.isEmpty(map.get("LOC"), ""));
							etxBarldBin.setText(StringUtil.isEmpty(map.get("BIN"), ""));
						}else{
							showErrorMsg(wr.getMessages());
							
						}
						
					}
				}); 
				
			}else{
				showErrorMsg("储位不能为空!");
			}
		}*/
		
		return true;
	}
	
	
	private void changeBar(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if(StringUtil.isEmpty(extBarldBar.getValStr())){
					return false;
				}else{
					return true;
				}
				
			}

			@Override
			public Object onGetData() {
				return barldBiz.getBarLdInfo(ApplicationDB.user.getUserDmain(), 
						ApplicationDB.user.getUserSite(),
						extBarldBar.getValStr(),
						ApplicationDB.user.getUserId());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map = new HashMap<String, Object>() ;
					map=((WebResponse) data).getDataToMap(); 
					renderingView(map);
					getFocues(extBarldBar, true);
				}else{
					extBarldBar.reValue();
					showErrorMsg(wr.getMessages());
					getFocues(extBarldBar, true);
					clearMsg();
				}
			}

			
		});
	}
	private void renderingView(Map<String, Object> map) {
		if(!StringUtil.isEmpty(map.get("MESSAGE"))){
			showErrorMsg(map.get("MESSAGE")+"");
			extBarldBar.reValue();
			clearView();
		}else{
		extBarldBar.reValue();
		clearView();
		etxBarldType.setText(StringUtil.isEmpty(map.get("SCANTYPENM"), ""));
		etxBarldStatus.setText(StringUtil.isEmpty(map.get("PLSTATUS"), ""));
		etxBarldNumlbl.setText(StringUtil.isEmpty(map.get("NUMLBL"), ""));
		etxBarldPart.setText(StringUtil.isEmpty(map.get("PART"), ""));
		etxBarldUm.setText(StringUtil.isEmpty(map.get("PARTUM"), ""));
		etxBarldPartNm.setText(StringUtil.isEmpty(map.get("PARTNM"), ""));
		etxBarldLot.setText(StringUtil.isEmpty(map.get("LOTS"), ""));
		etxBarldPartRev.setText(StringUtil.isEmpty(map.get("PARTREV"), ""));
		etxBarldAddr.setText(StringUtil.isEmpty(map.get("ADDR"), ""));
		etxBarldAddrNm.setText(StringUtil.isEmpty(map.get("ADDRNM"), ""));
		etxBarldSite.setText(StringUtil.isEmpty(map.get("SITE"), ""));
		etxBarldPlQty.setText(StringUtil.isEmpty(map.get("PLQTY"), ""));
		etxBarldBin.setText(StringUtil.isEmpty(map.get("BIN"), ""));
		etxBarldLoc.setText(StringUtil.isEmpty(map.get("LOC"), ""));
		/*String loc = map.get("LOC")+"";
		String bin = map.get("BIN")+"";
		String B = map.get("B")+"";
		if("B".equals(B)){
			etxBarldBin.setText(StringUtil.isEmpty(map.get("BIN"), ""));
			etxBarldLoc.setText(StringUtil.isEmpty(map.get("LOC"), ""));
			etxScanBin.setVisibility(View.GONE);
			findViewById(R.id.txv_ScanBin).setVisibility(View.GONE);
		}else{
			if(StringUtil.isEmpty(loc)&&StringUtil.isEmpty(bin)){
				etxScanBin.setVisibility(View.VISIBLE);
				findViewById(R.id.txv_ScanBin).setVisibility(View.VISIBLE);
				etxBarldBin.setText(bin);
				etxBarldLoc.setText(loc);
			}else{
				etxScanBin.setVisibility(View.GONE);
				findViewById(R.id.txv_ScanBin).setVisibility(View.GONE);
				etxBarldBin.setText(bin);
				etxBarldLoc.setText(loc);
			}
		}*/
		etxBarldBinQty.setText(StringUtil.isEmpty(map.get("BINQTY"), ""));
		etxBarldLocQty.setText(StringUtil.isEmpty(map.get("LOCQTY"), ""));
		etxBarldStatue.setText(StringUtil.isEmpty(map.get("LDSTATUSS"), ""));
		etxBarldNbr.setText(StringUtil.isEmpty(map.get("NBR"), ""));
		etxBarldDrtn.setText(StringUtil.isEmpty(map.get("DIRECTION"), ""));
		etxBarldRef.setText(StringUtil.isEmpty(map.get("REF"), ""));
		etxBarldColor.setText(StringUtil.isEmpty(map.get("PARTCOLOR"), ""));
		etxBarldParScan.setText(StringUtil.isEmpty(map.get("PARSCAN"), ""));
		etxBarldCrtUser.setText(StringUtil.isEmpty(map.get("CRTUSERNAME"), ""));
		etxBarldCrtDate.setText(StringUtil.isEmpty(map.get("CRTDATE"), ""));
		etxBarldScan.setText(StringUtil.isEmpty(map.get("SCAN"), ""));
		}
	}
	private void clearView() {
		etxBarldType.reValue();
		etxBarldStatus.reValue();
		etxBarldNumlbl.reValue();
		etxBarldPart.reValue();
		etxBarldUm.reValue();
		etxBarldPartNm.reValue();
		etxBarldLot.reValue();
		etxBarldPartRev.reValue();
		etxBarldAddr.reValue();
		etxBarldAddrNm.reValue();
		etxBarldSite.reValue();
		etxBarldPlQty.reValue();
		etxBarldBin.reValue();
		etxBarldBinQty.reValue();
		etxBarldLoc.reValue();
		etxBarldLocQty.reValue();
		etxBarldStatue.reValue();
		etxBarldNbr.reValue();
		etxBarldDrtn.reValue();
		etxBarldRef.reValue();
		etxBarldColor.reValue();
		etxBarldParScan.reValue();
		etxBarldCrtUser.reValue();
		etxBarldCrtDate.reValue();
		etxBarldScan.reValue();
		//etxScanBin.reValue();
	}
	


	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		if(StringUtil.isEmpty(extBarldBar.getValStr())){
			return false;
		}else{
			return true;
		} 
	}
 
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return barldBiz.getBarLdInfo(ApplicationDB.user.getUserDmain(), 
				ApplicationDB.user.getUserSite(),
				extBarldBar.getValStr(),
				ApplicationDB.user.getUserId());
	}

	@Override
	public void OnBtnSerCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map = new HashMap<String, Object>() ;
			map=((WebResponse) data).getDataToMap(); 
			renderingView(map);
			getFocues(extBarldBar, true);
		}else{
			extBarldBar.reValue();
			showErrorMsg(wr.getMessages());
			getFocues(extBarldBar, true);
			clearMsg();
		}
	}

	@Override
	public boolean OnBtnBloValidata(ButtonType btnType, View v) {
		if(StringUtil.isEmpty(etxBarldScan.getValStr())){
			showErrorMsg("请扫描标签之后进行操作");
			return false;
		}else if(StringUtil.isEmpty(etxBarldLoc.getValStr())){
			showErrorMsg("标签库位异常,不能提交");
			return false;
		}
		return true;
	}

	@Override
	public Object OnBtnBloClick(ButtonType btnType, View v) {
		return barldBiz.blockScan(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite(), etxBarldScan.getValStr(),
				etxBarldLot.getValStr(), etxBarldLoc.getValStr(), etxBarldBin.getValStr(), etxBarldAddr.getValStr(), etxBarldPlQty.getValStr(), 
				etxBarldPart.getValStr(), etxBarldStatue.getValStr(), etxBarldUm.getValStr(), ApplicationDB.user.getUserId(),  etxBarldStatus.getValStr());
	}

	@Override
	public void OnBtnBloCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			clearView();
			showMessage(wr.getMessages());
			getFocues(extBarldBar, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(extBarldBar, true);
		}
	}

	@Override
	public boolean OnBtnClrValidata(ButtonType btnType, View v) {
		return true;
	}
 
	@Override
	public Object OnBtnClrClick(ButtonType btnType, View v) {
		return null;
	}

	@Override
	public void OnBtnClrCallBack(Object data) {
		extBarldBar.reValue();
		clearView();
		getFocues(extBarldBar, true);
	}
	private void changeScan(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if(StringUtil.isEmpty(extBarldBar.getValStr())){
					return false;
				}else{
					return true;
				}
				
			}

			@Override
			public Object onGetData() {
				return barldBiz.getBarLdInfo(ApplicationDB.user.getUserDmain(), 
						ApplicationDB.user.getUserSite(),
						extBarldBar.getValStr(),
						ApplicationDB.user.getUserId());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map = new HashMap<String, Object>() ;
					map=((WebResponse) data).getDataToMap();  
					// barldlist = (List)map.get("list");
					// mdtgBarldGrid.buildData(barldlist);
					getFocues(extBarldBar, true);
				}else{
					extBarldBar.reValue();
					showErrorMsg(wr.getMessages());
					getFocues(extBarldBar, true);
					clearMsg();
				}
			}
		});
	}
	
	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
