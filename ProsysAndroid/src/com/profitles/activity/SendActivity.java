package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

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


public class SendActivity extends AppFunActivity {
	private SendBiz sendBiz;
	private MyEditText etxSendPart,etxSendUm,etxSendPartDesc,etxSendSend,
					etxSendUnit,etxSendReceipts,etxSendLot_Gone;
	private MyReadBQ  etxSendNbr;
	private MySpinner  spnSendReason;
	private MyDataGrid dtgSend;
	private ApplicationDB applicationDB;
	private List<Map<String , Object>> sendList = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_send;
	}

	@Override
	protected void pageLoad() {
		sendBiz=new SendBiz();
		etxSendNbr = (MyReadBQ) findViewById(R.id.etx_sendNbr);
		etxSendPart = (MyEditText) findViewById(R.id.etx_sendPart);
		etxSendUm = (MyEditText) findViewById(R.id.etx_sendUm);
		etxSendPartDesc = (MyEditText) findViewById(R.id.etx_sendPartDesc);
		etxSendLot_Gone = (MyEditText) findViewById(R.id.etx_sendLot_Gone);
		etxSendSend = (MyEditText) findViewById(R.id.etx_sendSend);
		etxSendUnit = (MyEditText) findViewById(R.id.etx_sendUnit);
		etxSendReceipts = (MyEditText) findViewById(R.id.etx_sendReceipts);
		spnSendReason = (MySpinner) findViewById(R.id.etx_sendReason );
		dtgSend = (MyDataGrid) findViewById(R.id.mdtg_send);
		
		
		dtgSend.setOnMyDataGridListener(new OnMyDataGridListener() {
			@Override	//长按事件
			public boolean onItemLongClick(View view,Object val, int rowIndex, int colIndex,Map<String, Object> rowData) {
				return false;
			}
			
			@Override	//单元格事件
			public void onItemClick(View view,Object val, int rowIndex, int colIndex,Map<String, Object> rowData) {
				//showSuccessMsg(dtgSend.getSelValue()+"   " + rowIndex + "   " + colIndex);
				if(rowIndex == 0) return;
//				Map<String, Object> map = dtgSend.getRowDataByKey(rowIndex);
				etxSendPart.setText(rowData.get("RFPKGD_PART") == null ? "" : rowData.get("RFPKGD_PART")+"");
				etxSendPartDesc.setText(rowData.get("PART_DESC") == null ? "" : rowData.get("PART_DESC")+"");
				etxSendUm.setText(rowData.get("UM") == null ? "" : rowData.get("UM")+"");
				etxSendSend.setText(rowData.get("RFPKGD_SHIP_QTY") == null ? "" : rowData.get("RFPKGD_SHIP_QTY")+"");
				etxSendUnit.setText(rowData.get("UNIT") == null ? "" : rowData.get("UNIT")+"");
				etxSendReceipts.setText(("0.0").equals(rowData.get("RFPKGD_ISS_QTY").toString()) ? "" : rowData.get("RFPKGD_ISS_QTY")+"");
				etxSendLot_Gone.setText(rowData.get("RFPKGD_LOT") == null ? "" : rowData.get("RFPKGD_LOT")+"");
				//spnSendReason.
			}
			
		});
		checkSendRSN();
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
		if(etxSendNbr.getId()==id){
			runClickFun();
			if(null != etxSendNbr.getValStr() && !"".equals( etxSendNbr.getValStr().toString().trim())){
				checkSendNbr();
			}

		}
		if(etxSendReceipts.getId()==id){
			runClickFun();
		}
		return istrue ;
	}

	@Override
	protected void onChangedAft(int id, Editable s) {
		if(etxSendReceipts.getId()==id){ 
			if(!"".equals( etxSendSend.getValStr().toString().trim()) && !"".equals(etxSendReceipts.getValStr().toString().trim())){
				float send =StringUtil.parseFloat(etxSendSend.getValStr()); 		//发货数量
				float receipts =StringUtil.parseFloat(etxSendReceipts.getValStr());   	//实际收货量
				if(receipts>send){
					istrue=false;
					getFocues(etxSendReceipts, true);
					etxSendReceipts.reValue();
					showErrorMsg(getResources().getString(R.string.send_qty_false));
				}else{
					if(receipts==0.0){
						istrue=false;
						getFocues(etxSendReceipts, true);
						etxSendReceipts.reValue();
						showErrorMsg(getResources().getString(R.string.send_qty2_false));
					}else{
						istrue=true;
					}
				}
			}else{
				istrue = true;
			}
		}

	}
	private void checkSendRSN(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return sendBiz.sendByRSN(applicationDB.user.getUserDmain());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					ArrayList<String> list=(ArrayList<String>) map.get("rsnList");
					String[] arrRsn = (String[])list.toArray(new String[list.size()]);
					spnSendReason.addAndClearItems(arrRsn);
				}else{
					showErrorMsg(wr.getMessages());
					istrue = false;
				}
			}
		});
	}
	
	private void checkSendNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return sendBiz.sendNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxSendNbr.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					sendList = (List<Map<String, Object>>) map.get("sendList");
					dtgSend.buildData(sendList);
					runClickFun();
					istrue=true;
				}else{
					istrue = false;
					etxSendNbr.reValue();
					showErrorMsg(wr.getMessages());
					getFocues(etxSendNbr, true);
				}
			}
		});
	}
	

	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Submit,ButtonType.Save};
	}

	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals(etxSendNbr.getValStr().toString().trim())){
			float qty=0;
			if(sendList!=null || sendList.size()>0){
				for (int i = 0; i < sendList.size(); i++) {
					Map<String, Object> map=sendList.get(i);
					qty=StringUtil.parseFloat(map.get("RFPKGD_ISS_QTY")+"");
					if(qty==0){
						istrue = false;
						showConfirm("零件:"+map.get("RFPKGD_PART")+""+"还没有收货确认是否提交?", scfListenSendSub);
					}
					
				}
			}else{
				showErrorMsg(getResources().getString(R.string.send_qtySub_false));
				getFocues(etxSendNbr, true);
				istrue=false;
			}
		}else{
			showErrorMsg(getResources().getString(R.string.pk_sub_false));
			getFocues(etxSendNbr, true);
			istrue=false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		if(!"".equals(etxSendPart.getValStr().toString().trim()) || !"".equals(etxSendReceipts .getValStr().toString().trim())){
			String nbr = etxSendNbr.getValStr().toString();
			String part = etxSendPart.getValStr();
			String um = etxSendUm.getValStr();
			String lot = etxSendLot_Gone.getValStr();
			String nuit = etxSendUnit .getValStr();
			String qty = etxSendReceipts.getValStr();
			String spnReason = spnSendReason.getValStr();
			return sendBiz.sendSubmit(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), nbr, part, um,lot,nuit,qty,spnReason,"SendActivity",applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate());
		}else{
			String nbr = etxSendNbr.getValStr().toString();
			return sendBiz.sendSubmitByNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),nbr,"SendActivity",applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate());
		}
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxSendNbr.reValue();
			etxSendPart.reValue();
			etxSendUm.reValue();
			etxSendPartDesc.reValue();
			etxSendLot_Gone.reValue();
			etxSendSend.reValue();
			etxSendUnit.reValue();
			etxSendReceipts.reValue();
			List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
			dtgSend.buildData(list);
			getFocues(etxSendNbr, true);
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxSendNbr, true);
		}
	}
	
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(!"".equals(etxSendNbr.getValStr().toString().trim()) && !"".equals(etxSendReceipts .getValStr().toString().trim()) && !"".equals(etxSendSend .getValStr().toString().trim())){

		}else{
			showErrorMsg(getResources().getString(R.string.lap_sendSave_false));
			getFocues(etxSendReceipts, true);
			istrue=false;
		}
		return istrue;
	}
	
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return sendBiz.sendSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxSendNbr.getValStr().toString(), etxSendPart.getValStr(), etxSendLot_Gone.getValStr(), etxSendReceipts.getValStr(), spnSendReason.getValStr());
	}
		
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String ,Object> map = wr.getDataToMap();
			sendList = (List<Map<String, Object>>) map.get("sendList");
			dtgSend.buildData(sendList);
			etxSendPart.reValue();
			etxSendUm.reValue();
			etxSendPartDesc.reValue();
			etxSendLot_Gone.reValue();
			etxSendSend.reValue();
			etxSendUnit.reValue();
			etxSendReceipts.reValue();
			getFocues(etxSendNbr, true);
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxSendNbr, true);
		}
	}
	

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
	
	private OnShowConfirmListen scfListenSendSub=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					if(!"".equals(etxSendPart.getValStr().toString().trim()) || !"".equals(etxSendReceipts .getValStr().toString().trim())){
						String nbr = etxSendNbr.getValStr().toString();
						String part = etxSendPart.getValStr();
						String um = etxSendUm.getValStr();
						String lot = etxSendLot_Gone.getValStr();
						String nuit = etxSendUnit .getValStr();
						String qty = etxSendReceipts.getValStr();
						String spnReason = etxSendReceipts.getValStr();
						return sendBiz.sendSubmit(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), nbr, part, um,lot,nuit,qty,spnReason,"SendActivity",applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate());
					}else{
						String nbr = etxSendNbr.getValStr().toString();
						return sendBiz.sendSubmitByNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),nbr,"SendActivity",applicationDB.user.getUserId(),applicationDB.user.getMac(),applicationDB.user.getUserDate());
					}
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse)data;
					if(wr.isSuccess()){
						etxSendNbr.reValue();
						etxSendPart.reValue();
						etxSendUm.reValue();
						etxSendPartDesc.reValue();
						etxSendLot_Gone.reValue();
						etxSendSend.reValue();
						etxSendUnit.reValue();
						etxSendReceipts.reValue();
						List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
						dtgSend.buildData(list);
						getFocues(etxSendNbr, true);
						showSuccessMsg(wr.getMessages());
					}else{
						showErrorMsg(wr.getMessages());
						getFocues(etxSendNbr, true);
					}
				}
			});	
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			getFocues(etxSendNbr, true);
		}
	};
	
}



