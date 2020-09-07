package com.profitles.activity;

import java.util.HashMap;
import java.util.Map;

import android.view.View;

import com.profitles.biz.InventoryPQCBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;


public class InventoryPQCActivity extends AppFunActivity {
	private InventoryPQCBiz pqcBiz;
	private MyEditText etxpart,etxpartName,etxWIP,
						etxproqty,etxckqty;
	private MyReadBQ  etxprolist;

	private ApplicationDB applicationDB;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_inventorypqc;//对应的页面  R.layout.act_demot
	}

	@Override
	protected void pageLoad() {
		pqcBiz = new InventoryPQCBiz();
		etxprolist = (MyReadBQ)findViewById(R.id.etx_prolist);
		etxpart = (MyEditText) findViewById(R.id.etx_part);
		etxpartName = (MyEditText) findViewById(R.id.etx_partDesc);
		etxWIP = (MyEditText) findViewById(R.id.etx_WIP);
		etxproqty = (MyEditText) findViewById(R.id.etx_proqty);
		etxckqty = (MyEditText) findViewById(R.id.etx_ckqty);
	}	

	@Override
	protected boolean onFocus(int id, View v) {
		return super.onFocus(id, v);
	}

	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		if(etxprolist.getId() == id){
			if(null != etxprolist.getValStr() && !"".equals(etxprolist.getValStr().trim())){
				checkLapCode();
				//runClickFun();
			}else{
				clearMsg();
				istrue = true;
			}
			runClickFun();
		}
		
		if(etxpart.getId() == id){
			runClickFun();
		}
		
		if(etxpartName.getId() == id){
			runClickFun();
		}
		
		if(etxWIP.getId() == id){
			runClickFun();
		}
		
		if(etxproqty.getId() == id){
			runClickFun();
		}
		
		if(etxckqty.getId() == id){
			runClickFun();
		}
		
		return istrue ;
	}


	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Save};
	}

	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(etxprolist.getValStr().trim() == "" || etxckqty.getValStr().trim() == ""){
			showErrorMsg(getResources().getString(R.string.lap_consSub_false));
			getFocues(etxprolist, true);
			istrue = false;
		}
	  return true;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return pqcBiz.PQCDateSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),
				applicationDB.user.getUserId(),etxprolist.getValStr(), etxpart.getValStr(), 
				etxproqty.getValStr(), etxckqty.getValStr(),etxWIP.getValStr());
	}
		
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			etxprolist.reValue();
			etxpart.reValue();
			etxpartName.reValue();
			etxWIP.reValue();
			etxproqty.reValue();
			etxckqty.reValue();
			showMessage(wr.getMessages());
			clearMsg();
			//istrue=true;
			getFocues(etxprolist, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxprolist, true);
		}
	}
	
	//处理条码解析
		private void checkLapCode() {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pqcBiz.getPQCDate(applicationDB.user.getUserDmain(), 
							applicationDB.user.getUserSite(), etxprolist.getValStr().toString());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr = (WebResponse)data;
					if(wr.isSuccess()){
						Map<String, Object> map=wr.getDataToMap();
						if(map.size() > 0){
							etxprolist.setText(map.containsKey("XSWOD_NBR") ? map.get("XSWOD_NBR").toString() : "");	
							etxpart.setText(map.containsKey("XSWOD_PART") ? map.get("XSWOD_PART").toString() : ""); 	
							etxpartName.setText(map.containsKey("PART_NAME") ? map.get("PART_NAME").toString() : ""); 
							etxWIP.setText(map.containsKey("XSWOD_LOC") ? map.get("XSWOD_LOC").toString() : "");	
							etxproqty.setText(map.containsKey("XSWOD_QTY_REQ") ? map.get("XSWOD_QTY_REQ").toString() : ""); 	
							checkNBR();//检验加工单是否存在
							runClickFun();
							clearMsg();
							istrue=true;
						}
						
					}else{
						showErrorMsg(wr.getMessages());
						getFocues(etxprolist, true);
						istrue = false;
					}
				}
			});
		}
		
		//检验加工单是否存在
		private void checkNBR() {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return pqcBiz.CKDateSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), 
							etxprolist.getValStr(),etxpart.getValStr());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr = (WebResponse)data;
					if(wr != null && !StringUtil.isEmpty(wr.getMessages())){
						showConfirm(wr.getMessages(), pqcListen);
					}
				}
			});
		}
	
		private OnShowConfirmListen pqcListen = new OnShowConfirmListen() {
			@Override
			public void onConfirmClick() {
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						return true;
					}
					@Override
					public Object onGetData() {
						return null;
					}
					@Override
					public void onCallBrack(Object data) {
						
					}
				});
			}
			@Override
			public void onCancelClick() {
				etxprolist.reValue();
				etxpart.reValue();
				etxpartName.reValue();
				etxWIP.reValue();
				etxproqty.reValue();
				etxckqty.reValue();
				getFocues(etxprolist, true);
				istrue=false;
			}
		};
		
	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
}



