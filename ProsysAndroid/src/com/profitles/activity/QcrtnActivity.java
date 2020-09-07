package com.profitles.activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.profitles.biz.QcrtnBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class QcrtnActivity extends AppFunActivity {
	private QcrtnBiz qcrtnbiz;
	private MyEditText actLocBin,actPart,actUM,actPartDesc,actQC,actUnit,actLot,actOkQty,actNgQty,actRsvQty,actExpQty,actIqiQty;
	private MyReadBQ  actNbr,actBar;
	private MyDataGrid dtgQcrtn;
	private String domain, site ,userId ,macId ,qad, SubminOrSubminByNbr,fnceffdate;
	private List<Map<String , Object>> qcrtnList = new ArrayList<Map<String , Object>>();
	private Map<String, Object> map=new HashMap<String,Object>();
	@Override
	protected int getMainBodyLayout(){
		return R.layout.act_qcrtn;
	}
	
	@Override
	protected void pageLoad() {	
		qcrtnbiz=new QcrtnBiz();
		actNbr = (MyReadBQ) findViewById(R.id.etx_qcrtnNbr);
		actLocBin = (MyEditText) findViewById(R.id.etx_qcrtntoBin);
		actBar = (MyReadBQ) findViewById(R.id.etx_qcrtnBar); 
		actBar.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(null != actBar.getValStr() && !"".equals( actBar.getValStr().toString().trim())){
						checkBar();
					} 
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actPart = (MyEditText) findViewById(R.id.etx_qcrtnPart);
		actUM = (MyEditText) findViewById(R.id.etx_qcrtnUM);
		actPartDesc = (MyEditText) findViewById(R.id.etx_qcrtnPartDesc);
		actLot = (MyEditText) findViewById(R.id.etx_qcrtnLot);
		actQC = (MyEditText) findViewById(R.id.etx_qcrtnQC);
		actUnit = (MyEditText) findViewById(R.id.etx_qcrtnUnit);
		dtgQcrtn = (MyDataGrid) findViewById(R.id.mdtg_qcrtn);
		actOkQty = (MyEditText) findViewById(R.id.etx_okQty);
		actNgQty = (MyEditText) findViewById(R.id.etx_ngQty);
		actRsvQty = (MyEditText) findViewById(R.id.etx_rsvQty);
		actExpQty = (MyEditText) findViewById(R.id.etx_expQty);
		actIqiQty = (MyEditText) findViewById(R.id.etx_qcrtnIqi);
		
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		userId=ApplicationDB.user.getUserId();
		macId=ApplicationDB.user.getMac();
		fnceffdate = ApplicationDB.user.getUserDate();
		qad = ApplicationDB.Ctrl.getString("RFC_PO_QAD", "0.0");
	}
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(actNbr,actLocBin,actBar);
	}
	
	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		if(actNbr.getId() == id){
			showSuccessMsg("");
			if(null != actNbr.getValStr() && !"".equals( actNbr.getValStr().toString().trim())){
				checkNbr();
			} 
		}
		if(actBar.getId() == id && isSchBarSuc){  
			runClickFun();
		}
		if(actOkQty.getId()==id){
			runClickFun();
		}
		return istrue;
	}
	
	/** 扫描单号时调用的函数 */
	private void checkNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public WebResponse onGetData() {
				return qcrtnbiz.qctrnNbr(domain,site,actNbr.getValStr(),userId,macId);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse) data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					actLocBin.setText(map.get("okLoc")+"");
					qcrtnList = (List<Map<String, Object>>) map.get("qcpList");
					dtgQcrtn.buildData(qcrtnList);
					runClickFun();
				}else{
					getFocues(actNbr, true);
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	/** 扫码时调用的函数 */
	boolean isSchBarSuc = false;
	private void checkBar() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public WebResponse onGetData() {
				return qcrtnbiz.qctrnBar(domain,site,actBar.getValStr(),actNbr.getValStr(),actLocBin.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
//					Map<String, Object> map = new HashMap<String, Object>();
					map=wrs.getDataToMap();
					actPart.setText(map.get("RFLOT_PART")+"");
					actOkQty.setText(map.get("XRIQD_OK_QTY")+"");
					actNgQty.setText(map.get("XRIQD_NG_QTY")+"");
					actRsvQty.setText(map.get("XRIQD_RSV_QTY")+"");
					actExpQty.setText(map.get("XRIQD_EXP_QTY")+"");
					actUM.setText(map.get("RFLOT_UM")+"");
					actPartDesc.setText(map.get("PART_DESC")+"");
					actQC.setText(map.get("XRIQD_IQ_QTY")+"");
					actIqiQty.setText(map.get("XRIQD_IQT_QTY")+"");
//					actRcvdQty = map.get("XRIQD_RCVD_QTY")+"";
//					if(map.get("RFLOT_BOX_QTY")==null){
//						actUnit.setText(map.get("RFPTV_MULT_ISS")+"");
//					}else{
//						actUnit.setText(map.get("RFLOT_BOX_QTY")+"");
//					}
					actUnit.setText(map.get("BOX")+"");
					actLot.setText(map.get("RFLOT_LOT")+"");
					isSchBarSuc = true;
				}else { 
					getFocues(actBar, true);
					showErrorMsg(wrs.getMessages());
					isSchBarSuc = false;
				}
			}
		});
	}
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit,ButtonType.Help};
	}
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if("".equals(actNbr.getValStr())){
			showErrorMsg(getResources().getString(R.string.pk_sub_false));
			istrue = false;
		}
		return istrue;
	}
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		String nbr = actNbr.getValStr().toString();
		String locbin = actLocBin.getValStr();
		String partn = actPart.getValStr();
		String lotr = actLot.getValStr();
		String nums = actQC.getValStr();
		String scan = actBar.getValStr();
		String actpartumString = actUM.getValStr();
		return qcrtnbiz.qcrtnSave(domain, site, nbr, locbin, partn,lotr,nums,scan,actpartumString);
	}
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr = (WebResponse) data;
		if(wr.isSuccess()){
			actBar.reValue();
			actLocBin.reValue();
			actPart.reValue();
			actUM.reValue(); 
			actPartDesc.reValue();  
			actLot.reValue();  
			actQC.reValue();  
			actUnit.reValue();
			actOkQty.reValue();
			actNgQty.reValue();
			actRsvQty.reValue();
			actExpQty.reValue();
			checkNbr();
			showSuccessMsg("保存成功！");
			getFocues(actBar, true);
		}else{
			getFocues(actBar, true);
			showErrorMsg(wr.getMessages());
		}
	}
	
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
	if(!"".equals(actQC.getValStr().toString().trim()) && !"".equals(actIqiQty.getValStr().toString().trim())){
			float qc =StringUtil.parseFloat(actQC.getValStr()); 		//箱数
			float iqi =StringUtil.parseFloat(actIqiQty.getValStr());   	//每箱
			if(qc==iqi){
				float okQty=StringUtil.parseFloat(map.get("XRIQD_OK_QTY"));
				if(StringUtil.parseFloat(actOkQty.getValStr().toString().trim())>okQty){
					showErrorMsg("返库合格数不能大于录入的合格数");
					istrue = false;
				}
			}else{
				showErrorMsg(getResources().getString(R.string.qcrnt_false_sub));
				istrue = false;
			}
			
		} else{
			showErrorMsg(getResources().getString(R.string.kbsc_consSub_false));
			istrue = false;
		}
		return istrue;
		
	}
	//actOkQty,actNgQty,actRsvQty,actExpQty
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		String nbr = actNbr.getValStr().toString();
		String locbin = actLocBin.getValStr()!=null?actLocBin.getValStr():"";
		String partn = actPart.getValStr()!=null?actPart.getValStr():"";
		String lotr = actLot.getValStr()!=null?actLot.getValStr():"";
		String num = actQC.getValStr()!=null?actQC.getValStr():"";
		String scan = actBar.getValStr()!=null?actBar.getValStr():"";
		String actpartumString = actUM.getValStr()!=null?actUM.getValStr():"";
		if(!"".equals(actOkQty.getValStr().toString().trim())){
			SubminOrSubminByNbr = "1";
			return qcrtnbiz.qcrtnSubmit(actOkQty.getValStr().toString().trim(),actNgQty.getValStr().toString().trim(),actRsvQty.getValStr().toString().trim(),actExpQty.getValStr().toString().trim(),domain, site, nbr, locbin, partn,lotr,num,scan,actpartumString,"QcrtnActivity",userId,macId,qad,SubminOrSubminByNbr,fnceffdate);
		}else{
			SubminOrSubminByNbr = "2";
			return qcrtnbiz.qcrtnSubmit(actOkQty.getValStr().toString().trim(),actNgQty.getValStr().toString().trim(),actRsvQty.getValStr().toString().trim(),actExpQty.getValStr().toString().trim(),domain, site, nbr, locbin, partn,lotr,num,scan,actpartumString,"QcrtnActivity",userId,macId,qad,SubminOrSubminByNbr,fnceffdate);
		}
	}
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			actNbr.reValue();
			actLocBin.reValue();
			actBar.reValue();
			actPart.reValue();
			actUM.reValue();
			actPartDesc.reValue();
			actLot.reValue();
			actQC.reValue();
			actUnit.reValue();
			actOkQty.reValue();
			actNgQty.reValue();
			actRsvQty.reValue();
			actExpQty.reValue();
			getFocues(actNbr, true);
			qcrtnList = null;
			dtgQcrtn.buildData(qcrtnList);
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(actLocBin, true);
		}
	}
	
	@Override
	public void OnBtnHelpCallBack(Object data) {
		super.OnBtnHelpCallBack(data);
	}
	
	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	} 
	
	@Override
	protected void unLockNbr() {
//		qbiz.qcpick_return(domain, site, actNbr.getValStr(), ApplicationDB.user.getUserId(),ApplicationDB.user.getMac());
	}
	
	private OnShowConfirmListen scfListenQcrtnSub=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					String nbr = actNbr.getValStr().toString();
					String locbin = actLocBin.getValStr()!=null?actLocBin.getValStr():"";
					String partn = actPart.getValStr()!=null?actPart.getValStr():"";
					String lotr = actLot.getValStr()!=null?actLot.getValStr():"";
					String num = actQC.getValStr()!=null?actQC.getValStr():"";
					String scan = actBar.getValStr()!=null?actBar.getValStr():"";
					String actpartumString = actUM.getValStr()!=null?actUM.getValStr():"";
					if(!"".equals(actOkQty.getValStr().toString().trim())){
						SubminOrSubminByNbr = "1";
						return qcrtnbiz.qcrtnSubmit(actOkQty.getValStr().toString().trim(),actNgQty.getValStr().toString().trim(),actRsvQty.getValStr().toString().trim(),actExpQty.getValStr().toString().trim(),domain, site, nbr, locbin, partn,lotr,num,scan,actpartumString,"QcrtnActivity",userId,macId,qad,SubminOrSubminByNbr,fnceffdate);
					}else{
						SubminOrSubminByNbr = "2";
						return qcrtnbiz.qcrtnSubmit(actOkQty.getValStr().toString().trim(),actNgQty.getValStr().toString().trim(),actRsvQty.getValStr().toString().trim(),actExpQty.getValStr().toString().trim(),domain, site, nbr, locbin, partn,lotr,num,scan,actpartumString,"QcrtnActivity",userId,macId,qad,SubminOrSubminByNbr,fnceffdate);
					}
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse)data;
					if(wr.isSuccess()){
						actNbr.reValue();
						actLocBin.reValue();
						actBar.reValue();
						actPart.reValue();
						actUM.reValue();
						actPartDesc.reValue();
						actLot.reValue();
						actQC.reValue();
						actUnit.reValue();
						actOkQty.reValue();
						actNgQty.reValue();
						actRsvQty.reValue();
						actExpQty.reValue();
						getFocues(actNbr, true);
						qcrtnList = null;
						dtgQcrtn.buildData(qcrtnList);
						showSuccessMsg(wr.getMessages());
					}else{
						showErrorMsg(wr.getMessages());
						getFocues(actNbr, true);
					}
				}
			});	
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			getFocues(actNbr, true);
		}
	};
}
