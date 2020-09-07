package com.profitles.activity;



import java.util.Date;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.LastQcBiz;
import com.profitles.biz.LastWgBiz;
import com.profitles.biz.PdBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class PdNewActivity extends AppFunActivity{

	private MyEditText actPart,actQty,actpdnum;
	private MyTextView actDesc,actUm,actQoh,actLot,actAllQty,actAllBox;
	private MyReadBQ actLocbin,actScan,actPdNbr;
	private PdBiz biz;
	private ApplicationDB applicationDB;
	private String domain, site, userid, vend,loc,bin,date ,fnceffdate;
	private boolean isTrue;
	private int boxQty=0;
	private float rsuQty = 0f;
	private float qty = 0f;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pdshns;
	}
	@Override
	protected void pageLoad() {
		biz = new PdBiz();
		actLocbin	= (MyReadBQ)   findViewById(R.id.ext_pdLocbin);
		actLocbin.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actLocbin.getValStr().equals("")){
					getLocBin();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actPdNbr	= (MyReadBQ)   findViewById(R.id.ext_pdnum);
		actPdNbr.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actPdNbr.getValStr().equals("")){
					getPdNbr();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actScan   	= (MyReadBQ)   findViewById(R.id.ext_pdBar);
		actScan.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actScan.getValStr().equals("")){
					getScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		actPart     = (MyEditText) findViewById(R.id.ext_pdParts); //零件
		actDesc 	= (MyTextView) findViewById(R.id.txv_pdPartname);//描述
		actUm		= (MyTextView) findViewById(R.id.txv_pdUm);//单位
		actQty 		= (MyEditText) findViewById(R.id.ext_pdQty);//盘点数量
		actLot      = (MyTextView) findViewById(R.id.txv_pdLot); //批次
		actAllBox   = (MyTextView) findViewById(R.id.ext_pdBoxQty); //箱数
		actAllQty      = (MyTextView) findViewById(R.id.ext_pdAllQty); //总数
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid		= ApplicationDB.user.getUserId();
		fnceffdate  = ApplicationDB.user.getUserDate();
		
	}
	
	/**
	 * 扫描单号
	 */
	private void getPdNbr(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if(actPdNbr.getValStr().equals("")){
					showErrorMsg("扫描单号之后操作");
					getFocues(actPdNbr, true);
					return false;
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.PdNewSchNbr(domain, site, actPdNbr.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					getFocues(actLocbin, true);
				}else{
					actPdNbr.setText("");
					getFocues(actPdNbr, true);
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	
	
	/**
	 * 扫描标签
	 */
	private void getScan(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if(actPdNbr.getValStr().equals("")  ){
					showErrorMsg("扫描单号之后操作");
					getFocues(actPdNbr, true);
					actScan.setText("");
					return false;
				}else if(actLocbin.getValStr().equals("")){
					showErrorMsg("扫描仓储之后操作");
					getFocues(actLocbin, true);
					actScan.setText("");
					return false;
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.PdNewScan(domain, site, actScan.getValStr(),loc,userid,bin,actPdNbr.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String,Object> map = wr.getDataToMap();
					actPart.setText(StringUtil.isEmpty(map.get("RFLOT_PART"))?"":map.get("RFLOT_PART").toString());
					actDesc.setText(StringUtil.isEmpty(map.get("RFLOT_PART_DESC"))?"":map.get("RFLOT_PART_DESC").toString());
					actUm.setText(StringUtil.isEmpty(map.get("RFLOT_UM"))?"":map.get("RFLOT_UM").toString());
				//	actQoh.setText(StringUtil.isEmpty(map.get("RFLOT_QOH"))?"":map.get("RFLOT_QOH").toString());
					actLot.setText(StringUtil.isEmpty(map.get("RFLOT_LOT"))?"":map.get("RFLOT_LOT").toString());
					
					if(StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY"))>0){
						qty = StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY"));
					}else{
						qty = StringUtil.parseFloat(map.get("RFLOT_MULT_QTY"));
					}
					boxQty += 1;
					actAllBox.setText(boxQty+"");
					float parseFloat = StringUtil.parseFloat(qty);
					rsuQty += qty;
					actAllQty.setText(rsuQty+"");
					actQty.setText(qty+"");
					vend = StringUtil.isEmpty(map.get("RFLOT_VEND"))?"":map.get("RFLOT_VEND").toString();
					Date d = new Date();
					date = d+"";
					getFocues(actScan, true);
					actScan.setText("");
					showSuccessMsg(wr.getMessages());
				}else{
					isTrue = false;
					actScan.setText("");
					getFocues(actScan, true);
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	/**
	 * 扫码仓储
	 */
	private void getLocBin(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if(actPdNbr.getValStr().equals("")){
					showErrorMsg("请扫描单号之后操作");
					getFocues(actPdNbr, true);
					actLocbin.setText("");
					
					return false;
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.PdLocBin(domain, site, actLocbin.getValStr(),actPdNbr.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map = wr.getDataToMap();
					loc = map.get("LOC")+"";
					bin = map.get("BIN")+"";
					isTrue = true;
					getFocues(actScan, true);
				}else{
					isTrue = false;
					loc ="";
					bin="";
					actLocbin.setText("");
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}	
	
	// 页面添加按钮
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[] { ButtonType.Return};
	}
	
	@Override
	protected void unLockNbr() {
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
