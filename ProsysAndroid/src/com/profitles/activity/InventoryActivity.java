package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.profitles.biz.InventoryBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;

import android.view.View;

public class InventoryActivity extends AppFunActivity {

	private MyReadBQ actNbr,actBar,actLocBin;
	private MyEditText actPart,actUm,actDesc,actLot,actVend,actPandian;
	private InventoryBiz iBiz;
	private MyDataGrid dtg;
	private String domain, site,userid,loc,bin,fnceffdate;
	private List<Map<String, Object>> nbrlist = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> barlist = new ArrayList<Map<String, Object>>();

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_inventory;
	}

	@Override
	protected void pageLoad() {
		iBiz = new InventoryBiz();
		actNbr		= (MyReadBQ)   findViewById(R.id.etx_IvtNbr);
		actBar		= (MyReadBQ)   findViewById(R.id.etx_IvtBar);
		actLocBin		= (MyReadBQ)   findViewById(R.id.etx_IvtLocBin);
		actPart      = (MyEditText) findViewById(R.id.etx_IvtPart);
		actDesc      = (MyEditText) findViewById(R.id.etx_IvtDesc);
		actUm      = (MyEditText) findViewById(R.id.etx_IvtUm);
		actLot      = (MyEditText) findViewById(R.id.etx_IvtLot);
		actVend      = (MyEditText) findViewById(R.id.etx_IvtVend);
		actPandian      = (MyEditText) findViewById(R.id.etx_IvtPandian);
		dtg = (MyDataGrid) findViewById(R.id.mdtg_Ivt);
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid		= ApplicationDB.user.getUserId();
		fnceffdate  = ApplicationDB.user.getUserDate();
		runClickFun();
	}

	@Override
	protected void unLockNbr() {
	}

	protected boolean onBlur(int id, View v) {
		if(R.id.etx_IvtNbr == id){
			if(!"".equals(actNbr.getValStr())){
				BlurNBR();
			}
		}
		if(R.id.etx_IvtLocBin == id){
			if(!"".equals(actLocBin.getValStr())){
				BlurLocBin();
			}
			runClickFun();
		}
		if(R.id.etx_IvtBar == id){
			if(!"".equals(actBar.getValStr())){
				BlurBar();
			}
			runClickFun();
		}
		if(R.id.etx_IvtPart == id){
			runClickFun();
		}
		if(R.id.etx_IvtVend == id){
			runClickFun();
		}
		if(R.id.etx_IvtLot == id){
			runClickFun();
		}
		if(R.id.etx_IvtPandian == id){
			runClickFun();
		}
		return true;
	}
	
	private void BlurNBR(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return iBiz.Inventory_nbr(domain, site, actNbr.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if (wrs.isSuccess()) {
					nbrlist=((WebResponse) data).getDataToList();
					dtg.buildData(nbrlist);
					getFocues(actLocBin, true);	//光标停留
					showSuccessMsg(wrs.getMessages());
				} else {
					getFocues(actNbr, true);	//光标停留
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}	
	
	
	
	private void BlurLocBin(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return iBiz.Inventory_locbin(domain, site, actNbr.getValStr(),actLocBin.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if (wrs.isSuccess()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map=wrs.getDataToMap();
					loc = map.get("LOC")+"";
					bin = map.get("BIN")+"";
					getFocues(actBar, true);	//光标停留
				} else {
					getFocues(actLocBin, true);	//光标停留
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}	
	
	private void BlurBar(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return iBiz.Inventory_scan(domain, site,actNbr.getValStr(), actBar.getValStr(),loc,bin);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if (wrs.isSuccess()) {
					barlist = ((WebResponse) data).getDataToList();
					Map<String, Object> map = barlist.get(0);
					actPart.setText(map.containsKey("XRICH_PART") ? map.get("XRICH_PART")+"" : map.get("RFLOT_PART")+"");
					actDesc.setText(map.containsKey("XSPT_DESC1") ? map.get("XSPT_DESC1")+"" : map.get("RFLOT_PART_DESC")+"");
					actUm.setText(map.containsKey("XSPT_UM") ? map.get("XSPT_UM")+"" : map.get("RFLOT_UM")+"");
					actVend.setText(map.containsKey("XRICH_VEND") ? map.get("XRICH_VEND")+"" : map.get("RFLOT_VEND")+"");
					actLot.setText(map.containsKey("XRICH_LOT") ? map.get("XRICH_LOT")+"" : map.get("RFLOT_LOT")+"");
					getFocues(actPandian, true);	//光标停留
				} else {
					showErrorMsg(wrs.getMessages());
					getFocues(actBar, true);	//光标停留
				}
			}
		});
	}	
	
	public void cleanText(String value){
		actLocBin.setText("");
		actBar.setText("");
		actPart.setText("");
		actDesc.setText("");
		actUm.setText("");
		actVend.setText("");
		actLot.setText("");
		actPandian.setText("");
		if(value.equals("T")){
			nbrlist = null;
			barlist = null;
			dtg.buildData(nbrlist);
		}
	}
	
	public Boolean Verification(String value){
		if(value.equals("T")){
			if("".equals(actNbr.getValStr())){
				showErrorMsg("单号不能为空!");
				return false;
			}
		}else{
			if("".equals(actLocBin.getValStr())){
				showErrorMsg("仓储不能为空!");
				return false;
			}else if("".equals(actBar.getValStr())){
				showErrorMsg("条码不能为空!");
				return false;
			}else if("".equals(actPart.getValStr())){
				showErrorMsg("零件不能为空!");
				return false;
			}else if("".equals(actVend.getValStr())){
				showErrorMsg("供方不能为空!");
				return false;
			}else if("".equals(actLot.getValStr())){
				showErrorMsg("批次不能为空!");
				return false;
			}else if("".equals(actPandian.getValStr())){
				showErrorMsg("盘点数量不能为空!");
				return false;
			}
		}
		return true;
	}
	
	// 页面添加按钮
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[] { ButtonType.Submit,ButtonType.Save,ButtonType.Search};
	}
	
	//保存按钮
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		Verification("");
		return true;
	}
	
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return iBiz.Inventory_save_val(domain, site, actNbr.getValStr(), actLot.getValStr(), loc, bin);
	}
	
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
		if (wrs.isSuccess()) {
			loadDataBase(rdblSave); 
		} else {
			showConfirm(wrs.getMessages(), new OnShowConfirmListen(){
				@Override
				public void onConfirmClick() {
					loadDataBase(rdblSave); 
				}
				@Override
				public void onCancelClick() {}
			});
		}
	}
	
	private IRunDataBaseListens rdblSave = new IRunDataBaseListens() {
		@Override
		public boolean onValidata() {
			return true;
		}
		@Override
		public Object onGetData() {
			return iBiz.Inventory_save(domain, site, actNbr.getValStr(), actPart.getValStr(), actLot.getValStr(), loc, bin,actPandian.getValStr(),actBar.getValStr(), actVend.getValStr());
		}
		@Override
		public void onCallBrack(Object data) {
			WebResponse web = (WebResponse) data;
			if(web.isSuccess()){
				BlurNBR();
				cleanText("");
				getFocues(actLocBin, true);
				showSuccessMsg(web.getMessages());
			}else {
				showErrorMsg(web.getMessages());
			}
		}
	};
	
	

	// 提交按钮
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		Verification("T");
		for (int i = 0; i < nbrlist.size(); i++) {
			if("".equals(nbrlist.get(0).get("XRICH_FIRST_QTY"))
					||"0".equals(nbrlist.get(0).get("XRICH_FIRST_QTY"))
					||"0.0".equals(nbrlist.get(0).get("XRICH_FIRST_QTY"))){
				showErrorMsg("无法提交！还有盘点信息未保存.");
				return false;
			}
		}
		return true;
	}

	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return iBiz.Inventory_vaildAllDo(domain, site,actNbr.getValStr());
	}
	
	private IRunDataBaseListens rdblSubmin = new IRunDataBaseListens() {
		@Override
		public boolean onValidata() {
			return true;
		}
		@Override
		public Object onGetData() {
			return iBiz.Inventory_submit(domain, site,actNbr.getValStr(), userid,fnceffdate);
		}
		@Override
		public void onCallBrack(Object data) {
			WebResponse web = (WebResponse) data;
			if(web.isSuccess()){
				cleanText("T");
				getFocues(actNbr, true);
				showSuccessMsg(web.getMessages());
			}else {
				showErrorMsg(web.getMessages());
			}
		}
	};

	public void OnBtnSubCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
		if (wrs.isSuccess()) {
			loadDataBase(rdblSubmin); 
		} else {
			showConfirm(wrs.getMessages(), new OnShowConfirmListen(){
				@Override
				public void onConfirmClick() {
					loadDataBase(rdblSubmin); 
				}
				@Override
				public void onCancelClick() {}
			});
		}
//		WebResponse web = (WebResponse) data;
//		if(web.isSuccess()){
//			cleanText("T");
//			getFocues(actNbr, true);
//			showSuccessMsg(web.getMessages());
//		}else {
//			showErrorMsg(web.getMessages());
//		}
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
