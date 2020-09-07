package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.LinearLayout;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.PmBiz;
import com.profitles.biz.PrBiz;
import com.profitles.biz.SpellBiz;
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
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.util.StringUtil;

public class PrActivity extends AppFunActivity {
	
	private PrBiz prbiz;
	private MyEditText  etx_sendVend,etx_sendVendDesc,etx_sendPart,etx_sendPartDesc,etx_sendlot,etx_sendQty,etx_sendTRqty,etx_sendBin,etx_sendLoc,etx_RtnQty;
	private MyReadBQ etx_sendScan ,etx_sendNbr; 
	private ApplicationDB applicationDB;
	private String jsonStr="";
	private  int QTY=0,RQTY=0;
	boolean execute=false;
	private List<Map<String , Object>> histlist = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> rList = new ArrayList<Map<String , Object>>();
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pr;
	}

	@Override
	protected void pageLoad() {
		  prbiz=new PrBiz();
		  etx_sendNbr = (MyReadBQ) findViewById(R.id.etx_sendNbr);
		  etx_sendScan = (MyReadBQ) findViewById(R.id.etx_sendScan);
		  etx_sendVend =(MyEditText) findViewById(R.id.etx_sendVend);
		  etx_sendVendDesc =(MyEditText) findViewById(R.id.etx_sendVendDesc);
		  etx_sendPart =(MyEditText) findViewById(R.id.etx_sendPart);
		  etx_sendPartDesc=(MyEditText) findViewById(R.id.etx_sendPartDesc);
		  etx_sendlot=(MyEditText) findViewById(R.id.etx_sendlot);
		  etx_sendQty=(MyEditText) findViewById(R.id.etx_sendQty);
		  etx_sendTRqty =(MyEditText) findViewById(R.id.etx_sendTRqty);
		 // etx_sendBin =(MyEditText) findViewById(R.id.etx_sendBin);
		 // etx_sendLoc =(MyEditText) findViewById(R.id.etx_sendLoc);
		//  etx_RtnQty =(MyEditText) findViewById(R.id.etx_rtnQty);
		  
		  etx_sendNbr.addTextChangedListener(new TextWatcher() {
				
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					clearMsg();
					if (!etx_sendNbr.getValStr().equals("")) {
						searchIsNbr();
					}
					
				}
				
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				public void afterTextChanged(Editable s) {
				}
			});
		  
		  
		  etx_sendScan.addTextChangedListener(new TextWatcher() {
				
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					clearMsg();
					if(!etx_sendScan.getValStr().equals("")){
						getReturnInfo();
					}
				}
				
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				public void afterTextChanged(Editable s) {
				}
			});
		  
		  
		  
	} 
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		
	}

	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//扫码光标离开事件相对应的代码
		/*if(etx_sendScan.getId()==id){
			runClickFun();
			if(null != etx_sendScan.getValStr() && !"".equals( etx_sendScan.getValStr().toString().trim())){

				getReturnInfo();
			}
		}*/
		return istrue ;
	}
	
	
	boolean istrue=false;
	
	private void getReturnInfo() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return prbiz.PrCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_sendNbr.getValStr(),etx_sendScan.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					  etx_sendVend.setText(map.get("RFPKGD_VEND")+"");
					  etx_sendVendDesc.setText(map.get("RFPKGD_VEND_NAME")+"");
					  etx_sendPart.setText(map.get("RFPKGD_PART")+"");
					  etx_sendPartDesc.setText(map.get("RFPKGD_PART_NAME")+"");
					  etx_sendlot.setText(map.get("RFPKGD_LOT")+"");
					    
					  etx_sendTRqty.setText(map.get("RFPKGD_QTY_OH")+"");  //可退库量
					
					  Map<String, Object> map2=new HashMap<String, Object>();
					  map2.put("VEND", map.get("RFPKGD_VEND"));
					  map2.put("PART", map.get("RFPKGD_PART"));
					  map2.put("BIN", map.get("RFPKGD_BIN"));
					  map2.put("LOC", map.get("RFPKGD_LOC"));
					  map2.put("LOT", map.get("RFPKGD_LOT"));
					  map2.put("QTY", map.get("RFPKGD_QTY_OH"));
					  map2.put("RQTY", map.get("RFPKGD_QTY"));
					  
					  map2.put("UM", map.get("RFPKGD_UM"));
					  map2.put("SCAN", map.get("RFPKGD_SCAN"));
					  histlist.add(map2);
					  
					  QTY=StringUtil.parseInt(map.get("RFPKGD_QTY_OH"));  
					  RQTY=StringUtil.parseInt(map.get("RFPKGD_QTY"));
					  istrue = true;
				}else{
					showErrorMsg(wr.getMessages());
					etx_sendScan.setText("");
					getFocues(etx_sendScan, true);
					istrue = false;
				}
			}
		});
	}
	
	/*
	 * 扫退货单进行验证是否存在
	 * */
	private void searchIsNbr() {
		try {
		loadDataBase(new IRunDataBaseListens() {
				@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return prbiz.PrSearNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),  applicationDB.user.getUserId(), etx_sendNbr.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
			 
				 WebResponse wr = (WebResponse)data;
					if(wr.isSuccess()){
						Map<String, Object> mpNbrCunt=wr.getDataToMap();
						
						getFocues(etx_sendScan, true);//光标停留
						  istrue = true;
					}else{
						showErrorMsg(wr.getMessages());
						etx_sendNbr.setText("");
						getFocues(etx_sendNbr, true);//光标停留
						istrue = false;
					}
				}
			
		});
		} catch (Exception e) {
			// TODO: handle exception
			showErrorMsg(e.getMessage());
		}
	}
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Search,ButtonType.Submit};
	}	

	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		
		if(!StringUtil.isEmpty(QTY)&&!StringUtil.isEmpty(RQTY)){
			if(RQTY>QTY){
				showErrorMsg("退货量不能大于库存量!");
				istrue=false;
			}
		}
		return istrue;
	}
	
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		jsonStr="";
		List<String> cache = new ArrayList<String>();
		for (int i = 0; i < histlist.size(); i++) {
			JSONObject obj = new JSONObject(histlist.get(i)); 
			cache.add(obj.toString());  
		}
		jsonStr = cache.toString(); 
		 return prbiz.PrCheckSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),jsonStr,ApplicationDB.user.getUserId(),ApplicationDB.user.getUserDate(),etx_sendNbr.getValStr());
	
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etx_sendVend.reValue();
			etx_sendVendDesc.reValue();
			etx_sendPart.setText("");
			etx_sendPartDesc.reValue();
			etx_sendlot.reValue();
			etx_sendTRqty.reValue();
			etx_sendScan.reValue();
			String msg=wr.getMessages();
			showSuccessMsg(msg);
			histlist.clear();
			getFocues(etx_sendScan, true);
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
			getFocues(etx_sendScan, true);
		}
		
	}
	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		return istrue=true;
	}
	
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return prbiz.PrSearNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),  applicationDB.user.getUserId(), etx_sendNbr.getValStr());
	}
	
	@Override
	public void OnBtnSerCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
				Map<String, Object> mpNbrCunt=wr.getDataToMap();
				getFocues(etx_sendScan, true);
			    istrue = true;
		  
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_sendScan, true);
			istrue = false;
		}
	}
	
	@Override
	protected void unLockNbr() {

	}

	@Override
	public void OnBtnHelpCallBack(Object data) {
		showSuccessMsg(R.string.Cnrc_help);
		super.OnBtnHelpCallBack(data);
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
	
}
