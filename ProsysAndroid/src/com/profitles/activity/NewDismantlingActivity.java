package com.profitles.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.DisBiz;
import com.profitles.biz.NewDisBiz;
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
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.util.StringUtil;

public class NewDismantlingActivity extends AppFunActivity {
	
	private MyEditText  etx_sendPart,etx_sendPartDesc,etx_sendUnit,etx_sendmodel,etx_num,etx_type;
	private MyReadBQ etxParentScan,etxsendSn; 
	private MyTabHost tblSocfmAdv;
	private MyDataGrid rflot_SnList,gdvAddARemove; 
	private String type,seq,domain,site,userid,date,scanType,DelAll,qty;
	private StringBuffer strScan = new StringBuffer("");
	private List<Map<String,Object>> TrayList = new ArrayList<Map<String,Object>>();//托盘信息
	private List<Map<String,Object>> DisList = new ArrayList<Map<String,Object>>(); //拼拆信息
	private boolean istrue = false;
	private NewDisBiz biz ;
	private ApplicationDB applicationDB;
	private int k = 0 ;
	private String jsonStr="";
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_newdis;
	}

	@Override
	protected void pageLoad() {
		  biz = new NewDisBiz();
		  domain = ApplicationDB.user.getUserDmain();
		  site = ApplicationDB.user.getUserSite();  
		  userid = ApplicationDB.user.getUserId();  
		  date = ApplicationDB.user.getUserDate(); 
		  etxParentScan = (MyReadBQ) findViewById(R.id.etx_parentScan); // 父标签
		  etxsendSn = (MyReadBQ) findViewById(R.id.etx_sonScan); // 子标签
		  etx_sendPart =(MyEditText) findViewById(R.id.etx_sendPart); // 零件
		  etx_sendPartDesc =(MyEditText) findViewById(R.id.etx_sendPartDesc); // 零件描述
		  etx_sendUnit=(MyEditText) findViewById(R.id.etx_sendUnit); // 单位
		  etx_num =(MyEditText) findViewById(R.id.etx_num); // 标签数量
		  etx_type =(MyEditText) findViewById(R.id.etx_type); // 标签类型
		  tblSocfmAdv= ((MyTabHost) this.findViewById(R.id.tbl_socfmAdv));
		  tblSocfmAdv.setup();
		  rflot_SnList = (MyDataGrid)findViewById(R.id.rflot_SN); // 父标签明细
		  gdvAddARemove=(MyDataGrid)findViewById(R.id.gdvAddARemove); // 扫描标签
		  etxParentScan.addTextChangedListener(new TextWatcher(){ // 父标签扫码操作

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(!StringUtil.isEmpty(etxParentScan.getValStr())){
					getParentScan();
				}
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
			  
		  } );
		  etxsendSn.addTextChangedListener(new TextWatcher() { // 字标签扫码操作
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!StringUtil.isEmpty(etxsendSn.getValStr())){
					getSonScan();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		}); 
		  
	} 
	
	/**
	 * 扫描父标签方法
	 * @return 
	 * */
	private void getParentScan(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.DisCheckBar(domain, site, etxParentScan.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					List<Map<String, Object>> dataToList = wr.getDataToList();
					
					rflot_SnList.buildData(dataToList);
					
					getFocues(etxsendSn, true);
				}else{
					showErrorMsg(wr.getMessages());
					etxParentScan.setText("");
					rflot_SnList.removeAllViews();
					getFocues(etxParentScan, true);
				}
			}
			
		});
	}
	
	/**
	 * 扫描子标签方法
	 * @return 
	 * */
	private void getSonScan(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.checkSonBar(domain, site, etxsendSn.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					List<Map<String, Object>> rflotInfoList = wr.getDataToList();
					Map<String, Object> mpScan = new HashMap<String, Object>();
					etx_sendPart.setText(rflotInfoList.get(0).get("RFLOT_PART")+"");
					etx_sendPartDesc.setText(StringUtil.isEmpty(rflotInfoList.get(0).get("RFLOT_PART_DESC")+"", ""));
					etx_sendUnit.setText(StringUtil.isEmpty(rflotInfoList.get(0).get("RFLOT_UM")+"", ""));
					if(StringUtil.parseInt(rflotInfoList.get(0).get("RFLOT_SCATTER_QTY")) != 0){
						etx_num.setText(rflotInfoList.get(0).get("RFLOT_SCATTER_QTY")+"");
					}else{
						etx_num.setText(rflotInfoList.get(0).get("RFLOT_BOX_QTY")+"");
					}
					etx_type.setText(rflotInfoList.get(0).get("RFLOT_TYPE")+"");
					if(DisList.size() >0){
						for (int i = 0; i < DisList.size(); i++) {
							if(etxsendSn.getValStr().equals(DisList.get(i).get("RFLOT_SCAN")+"")){
								DisList.remove(i);
								istrue = true;
								break;
							}
						}
					}
					if(!istrue){
						mpScan.put("RFLOT_SCAN", etxsendSn.getValStr());
						DisList.add(mpScan);
					}
					gdvAddARemove.buildData(DisList);
					etxsendSn.setText("");
					getFocues(etxsendSn, true);
				}else{
					showErrorMsg(wr.getMessages());
					etxsendSn.setText("");
					getFocues(etxsendSn, true);
				}
			}
			
		});
	}
	/**
	 * 进行提交操作
	 * */
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v){
		jsonStr=""; // 将子标签转换成json的数据进行传值
		List<String> cache = new ArrayList<String>();
		for (int i = 0; i < DisList.size(); i++) {
			JSONObject obj = new JSONObject(DisList.get(i)); 
			cache.add(obj.toString());  
		}
		jsonStr = cache.toString(); 
		return true;
		
	}
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		
		return biz.submit(domain, site, jsonStr, etxParentScan.getValStr());
	}
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse) data;
		if(wr.isSuccess()){
			clear();
			showMessage1(wr.getMessages());
			getFocues(etxParentScan, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxParentScan, true);
		}
	}
	protected boolean onBlur(int id, View v) {
		//扫码光标离开事件相对应的代码
		if(etxParentScan.getId()==id){
			runClickFun();
		}
		//扫码光标离开事件相对应的代码
		if(etxsendSn.getId()==id){
			runClickFun();
		}	
		return true ;
	}
	
	
	//清空
	public void clear(){
		strScan = new StringBuffer("");
		etxsendSn.reValue();
		etx_sendPart.setText("");  
		etx_sendPartDesc.setText("");
		etx_sendUnit.setText("");
		etxParentScan.setText("");
		etx_num.setText("");
		etx_type.setText("");
		TrayList = new ArrayList<Map<String,Object>>();
		DisList = new ArrayList<Map<String,Object>>();
		rflot_SnList.buildData(TrayList); 
		gdvAddARemove.buildData(TrayList);
	
	}

	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		
	}
	
	@Override
	protected void unLockNbr() {

	}
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Submit,ButtonType.Help};
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
