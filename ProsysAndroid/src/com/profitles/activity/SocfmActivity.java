package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.SocfmBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class SocfmActivity extends AppFunActivity {
	private SocfmBiz socfmBiz;
	private MyReadBQ  etxSocfmNbr;
 	private MyEditText etxSocfmCust,etxSocfmReq,etxSocfmEtd,etxSocfmMes,actQty;
	private MyTabHost tblSocfmAdv;
 	private LinearLayout lltSocfmSrfd,lltSocfmPkl;
	private MyDataGrid gdvSocfmSrfd,gdvSocfmPkl; 
	private List<Map<String , Object>> srfdList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> pklList = new ArrayList<Map<String , Object>>();
	private ApplicationDB applicationDB;
	private boolean onPageLoad = true;
	private String domain , site , line="",nbr="";
	private View vi;
	private int checkRowIndex;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_socfm;
	}

	// @SuppressLint("CutPasteId")
	@Override 
	protected void pageLoad() {
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		actQty = (MyEditText) findViewById(R.id.etx_socfmqty);
		tblSocfmAdv= ((MyTabHost) this.findViewById(R.id.tbl_socfmAdv));
		tblSocfmAdv.setup();
		tblSocfmAdv.setOnTabChangedListener(new OnTabChangeListener() {
			@Override	
			public void onTabChanged(String tabId) {
				if(tabId.equals("GetSocfmSrfd")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							gdvSocfmSrfd.buildData(srfdList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}else if(tabId.equals("GetSocfmPkl")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							gdvSocfmPkl.buildData(pklList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}
			}
		});
		socfmBiz = new SocfmBiz();
		etxSocfmNbr = (MyReadBQ) findViewById(R.id.etx_socfmNbr);
		etxSocfmNbr.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { 
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(null != etxSocfmNbr.getValStr() && !"".equals( etxSocfmNbr.getValStr().toString().trim())){
					checkNbr();
				} 
			}
			public void afterTextChanged(Editable s) {
			}
			
		});
		etxSocfmCust = (MyEditText) findViewById(R.id.etx_socfmCust);
		etxSocfmReq=(MyEditText) findViewById(R.id.etx_socfmReq);
		etxSocfmEtd=(MyEditText) findViewById(R.id.etx_socfmEtd);
		etxSocfmMes=(MyEditText) findViewById(R.id.etx_socfmMes);
		
		
		lltSocfmSrfd = (LinearLayout) findViewById(R.id.llt_socfmSrfd) ; 
		lltSocfmPkl = (LinearLayout) findViewById(R.id.llt_socfmPkl) ;  
		
		gdvSocfmSrfd = (MyDataGrid) findViewById(R.id.gdv_socfmSrfd) ;
		gdvSocfmPkl = (MyDataGrid) findViewById(R.id.gdv_socfmPkl) ; 
		gdvSocfmPkl.setOnMyDataGridListener(new OnMyDataGridListener() {

			@Override
			public void onItemClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
					if(rowIndex == 0) return;
	//				Map<String, Object> map = gdvSocfmPkl.getRowDataByKey(rowIndex-1);
					if(StringUtil.isEmpty(rowData.get("RF_PK_QTY"))){
						showErrorMsg("请选择已分拣的行数据!");
					}else{
						if(vi != null){
							// vi.setBackgroundColor(Color.TRANSPARENT);//清空上次点击行颜色
							vi.setBackgroundColor(checkRowIndex == 0  ? Color.TRANSPARENT : 
								(checkRowIndex%2 == 0 ? Color.TRANSPARENT: Color.parseColor(Constants.CHECK_ROW_COLOR)) );//清空上次点击行颜色
						}
						vi  = (View) view.getParent();		//保存获取到行View
						checkRowIndex = rowIndex;
						View vv = (View) view.getParent();		//获取到行View
						vv.setBackgroundColor(Color.YELLOW);	//更改选中行的背景色
						actQty.setText(rowData.get("RF_PK_QTY")+"");
						line = StringUtil.isEmpty(rowData.get("RF_LINE"))?"":rowData.get("RF_LINE")+"";
						nbr = StringUtil.isEmpty(rowData.get("RFPKL_NBR"))?"":rowData.get("RFPKL_NBR")+"";
					}
				
			}

			@Override
			public boolean onItemLongClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
				// TODO Auto-generated method stub
				return false;
			}

		});
		map = new HashMap();
	}
  
	@Override
	protected boolean onFocus(int id, View v) {
		return super.onFocus(id, v);
	}
	boolean istrue = false; 
	Map map = new HashMap();
	@Override
	protected boolean onBlur(int id, View v) { 
		// 单号
		if(etxSocfmNbr.getId() == id ){ 
				 runClickFun(); 
		} 
		if(actQty.getId() == id ){ 
			 runClickFun(); 
		} 
		return istrue ;
	}
	
	private void checkNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if(!"".equals(etxSocfmNbr.getValStr().toString().trim())){
					return istrue = true;
				}else{
					showErrorMsg(getResources().getString(R.string.pk_sub_false));
					getFocues(etxSocfmNbr, true);
					return  istrue = false; 
				}
			}
			@Override
			public Object onGetData() {
				return socfmBiz.getSoShipByNbr(applicationDB.user.getUserDmain() , 
						applicationDB.user.getUserSite() ,
						etxSocfmNbr.getValStr().toString() , 
						applicationDB.user.getUserId() , 
						applicationDB.user.getMac());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					 map = wr.getDataToMap();
					if(map!=null){
						if(map.containsKey("mes") && !StringUtil.isEmpty(map.get("mes") ) ){
							// 存在已拣信息是否情况 弹出框中处理
							 showConfirm(map.get("mes") + " 确认要继续吗？", scfListenSocfmNbr);
						}else{
							setChangeTable(map);
							istrue = true;
						}
					}else{
						showErrorMsg(getResources().getString(R.string.pk_getfalse)); 
						etxSocfmNbr.reValue();
						istrue = false;
					}
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					etxSocfmNbr.reValue();
					getFocues(etxSocfmNbr, true);
				}
			}
		});
	}

	private void setChangeTable(Map<String, Object> map) {
		srfdList =  (List<Map<String, Object>>) map.get("detList"); //发货信息
		pklList =  (List<Map<String, Object>>) map.get("pkllist");//分拣信息
		if(pklList.size()>0&&pklList!=null){
			for (int i = 0; i < pklList.size(); i++) {
				String line= pklList.get(i).get("RFPKLD_LINE").toString();
				line=line.substring(0,line.length()-2);
				pklList.get(i).put("RFPKLD_LINE", line);
			}
		}
		if(srfdList.size()>0&&srfdList!=null){
			for (int i = 0; i < srfdList.size(); i++) {
				String line= srfdList.get(i).get("RFSRFD_LINE").toString();
				String soLine= srfdList.get(i).get("RFSRFD_SO_LN").toString();
				line=line.substring(0,line.length()-2);
				soLine=soLine.substring(0,soLine.length()-2);
				srfdList.get(i).put("RFSRFD_LINE", line);
				srfdList.get(i).put("RFSRFD_SO_LN", soLine);
			}
		}
		gdvSocfmSrfd.buildData(srfdList); 
		gdvSocfmPkl.buildData(pklList);  
		etxSocfmCust.setText(StringUtil.isEmpty(map.get("RFSRF_CUST"), "") + " " +StringUtil.isEmpty(map.get("CUST_NAME"), "") );
		etxSocfmReq.setText(StringUtil.isEmpty(map.get("RFSRF_REQ_DATE"), ""));
		etxSocfmEtd.setText(StringUtil.isEmpty(map.get("RFSRF_ETD"), ""));
		etxSocfmMes.setText(StringUtil.isEmpty(map.get("mes"), ""));
	}
	
/*	  
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Submit,ButtonType.Save};
	}
	
	//保存
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(StringUtil.parseFloat(!actQty.getValStr().equalsIgnoreCase("") ? actQty.getValStr() : "0") > 0
				&& !StringUtil.isEmpty(line)){
			return true;
		}
		showErrorMsg("请选中行数据或输入正确修改数量");
		return false;
	}

	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return socfmBiz.updateSoShipSave(domain, site, nbr, line, actQty.getValStr());
	}

	public void OnBtnSaveCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
		if (wrs.isSuccess()) {
			checkNbr();
			showSuccessMsg(wrs.getMessages());
		}else{
			showErrorMsg(wrs.getMessages());
		}
	}*/

	@Override
	public Object OnBtnHelpClick(ButtonType btnType, View v) {
		showSuccessMsg(R.string.help_ok);
		return  null ;
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
/*	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
			return true; 
	}
	
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return null;
	}
	
	@Override
	public void OnBtnSerCallBack(Object data) {
		checkNbr();
	}*/
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!istrue){
			showErrorMsg("请扫描正确的单号");
			return false ;
		}
		if(!"".equals(etxSocfmNbr.getValStr().toString().trim())){
			return true;
		}else{
			showErrorMsg(getResources().getString(R.string.pk_sub_false));
			getFocues(etxSocfmNbr, true);
			return false; 
		}
	}
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
			return socfmBiz.updateSoShipSt(applicationDB.user.getUserDmain(), 
					applicationDB.user.getUserSite(), 
					etxSocfmNbr.getValStr().toString().trim(), 
					applicationDB.user.getUserId(), 
					"SocfmActivity", getAppVersion(),
					applicationDB.Ctrl.getString("RFC_PO_QAD","0"), 
					applicationDB.user.getMac(),
					applicationDB.user.getUserDate());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxSocfmNbr.reValue(); 
			srfdList =   new ArrayList<Map<String, Object>>() ; 
			pklList =  new ArrayList<Map<String, Object>>() ; 
			gdvSocfmSrfd.buildData(srfdList); 
			gdvSocfmPkl.buildData(pklList);  
			etxSocfmCust.reValue();
			etxSocfmReq.reValue();
			etxSocfmEtd.reValue();
			actQty.reValue();
			getFocues(etxSocfmNbr, true);
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxSocfmNbr, true);
		}
	}
	
	//解锁
	@Override
	protected void unLockNbr() {
		/*if(!"".equals(etxSocfmNbr.getValStr().toString().trim())){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {	
					return socfmBiz.unLockSoShip(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxSocfmNbr.getValStr(), applicationDB.user.getUserId(),applicationDB.user.getMac());
				}
				@Override
				public void onCallBrack(Object data) {

				}
			});	
		}*/
	}
	//返回按钮
			public boolean OnBtnRtnValidata(ButtonType btnType, View v) {
				return true;
			}
			
			public WebResponse OnBtnRtnClick(ButtonType btnType, View v) {
				if(!"".equals(etxSocfmNbr.getValStr().toString().trim() )){
					return socfmBiz.unLockSoShip(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxSocfmNbr.getValStr(), applicationDB.user.getUserId(),applicationDB.user.getMac());
				}else{
					return null; 
				}
			}	
			
			public void OnBtnRtnCallBack(Object data) {
				System.out.println("ssssssssssssss");
			}
	private OnShowConfirmListen scfListenSocfmNbr=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					 return  map;
				}
				@Override
				public void onCallBrack(Object data) {
					setChangeTable(map);
					istrue = true;
				}
			});	
		}
		@Override
		public void onCancelClick() {  //询问框  点取消
			getFocues(etxSocfmNbr, true);
			istrue = false;
		}
	};
}
