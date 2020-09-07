package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.telephony.gsm.SmsMessage.SubmitPdu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TabHost.OnTabChangeListener;

import com.google.zxing.common.StringUtils;
import com.profitles.activity.R.string;
import com.profitles.biz.PrBiz;
import com.profitles.biz.PsterBiz;
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
import com.profitles.framwork.cusviews.view.interfaces.IMyEditText;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class PsterActivity extends AppFunActivity {
	
	private PsterBiz psterbiz;
	private MyEditText  etx_sendUm,etx_sendPart,etx_sendQty,etx_sendDqty,etx_sendAllPk,etx_sendRqty;
	private MyReadBQ etx_sendScan ,etx_sendNbr; 
	private ApplicationDB applicationDB;
	private MyDataGrid gdv_drawlist,gdv_drawDet,gdv_drawSumm;
	private MyTabHost tbl_pkAdv;
	private String jsonStr="",status="",scan = "",part="",type = "" ,seq="";
	private View backRow;
	private int checkRowIndex;
	private  int DQTY=0,AQTY=0,QTY=0,p=0;
	boolean execute=false;
	private List<Map<String , Object>> mstrlist = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> detList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> summList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> statis = new ArrayList<Map<String , Object>>();
	private Map<String , Object> boxseq = new HashMap<String , Object>();
	private boolean onPageLoad = true;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pster;
	}
	
	
	@Override
	protected void pageLoad() {
		tbl_pkAdv=((MyTabHost) this.findViewById(R.id.tbl_pkAdv));
		tbl_pkAdv.setup();
		tbl_pkAdv.setOnTabChangedListener(new OnTabChangeListener() {
			@Override	
			public void onTabChanged(String tabId) {
				if(tabId.equals("GetDrawDet")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							gdv_drawDet.buildData(detList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}else if(tabId.equals("GetDrawSumm")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							gdv_drawSumm.buildData(summList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}
			}
		});
		psterbiz = new PsterBiz();
		gdv_drawlist = (MyDataGrid) findViewById(R.id.gdv_drawlist);
	    gdv_drawDet = (MyDataGrid) findViewById(R.id.gdv_drawDet);
	    gdv_drawSumm = (MyDataGrid) findViewById(R.id.gdv_drawSumm);
	    
	    etx_sendNbr = (MyReadBQ) findViewById(R.id.etx_sendNbr); //单号
	    etx_sendScan = (MyReadBQ) findViewById(R.id.etx_sendScan); //标签
	    etx_sendPart =(MyEditText) findViewById(R.id.etx_sendPart); //零件
	    etx_sendUm =(MyEditText) findViewById(R.id.etx_sendUm); // 单位
	    etx_sendQty = (MyEditText) findViewById(R.id.etx_sendQty); //数量
	    etx_sendDqty =(MyEditText) findViewById(R.id.etx_sendDqty);//总撤
	    etx_sendAllPk =(MyEditText) findViewById(R.id.etx_sendAllPk); // 总捡
	    etx_sendRqty =(MyEditText) findViewById(R.id.etx_sendRqty); // 实发
	    
	    getPKList();
	    etx_sendNbr.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				clearMsg();
				if (!etx_sendNbr.getValStr().equals("")) { // 单号验证
					searchIsNbr();
				}
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
	    // 标签位置扫描处理
	    etx_sendScan.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				clearMsg();
				if(!etx_sendScan.getValStr().equals("")){
					
					if (etx_sendNbr.getValStr().equals("")) {//退货单号验证
						showMessage("请先扫描单号！");
					}else{
						getReturnByScan(); // 对扫描的标签进行处理
					}
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
	    // 第一个列表的点击事件处理
	    gdv_drawlist.setOnMyDataGridListener(new OnMyDataGridListener() {
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowDatas) {
				return false;
			}
			public void onItemClick(View view, Object val,  int rowIndex, int colIndex ,Map<String, Object> rowData) {
				if(rowIndex == 0) return;
				if(backRow != null){ 
					backRow.setBackgroundColor(checkRowIndex == 0  ? Color.TRANSPARENT : 
						(checkRowIndex%2 == 0 ? Color.TRANSPARENT: Color.parseColor(Constants.CHECK_ROW_COLOR)) );//清空上次点击行颜色
				}
				backRow  = (View) view.getParent();		//保存获取到行View
				checkRowIndex = rowIndex;
				View vv = (View) view.getParent();		//获取到行View
				vv.setBackgroundColor(Color.YELLOW);	//更改选中行的背景色
				etx_sendNbr.setText(rowData.get("RFPKL_NBR").toString());
				getFocues(etx_sendScan, true);
				runClickFun();
			}
		});
	}
	
	// pageload执行 自动按照仓管员所属的组加载他/她所负责的【分拣单】以及空的仓管员组的分拣单
	private void getPKList(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return psterbiz.getPkList(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite(),ApplicationDB.user.getUserId());
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					mstrlist = wr.getDataToList();
					gdv_drawlist.buildData(mstrlist);
				}else{
					showErrorMsg(wr.getMessages());
					etx_sendNbr.setText("");
				}
			}
		});
	}
	
	boolean istrue=false;
	/*
	 * 扫单号进行验证是否存在
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
				return psterbiz.PsterSearNbr(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite(),  ApplicationDB.user.getUserId(), etx_sendNbr.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				
				 WebResponse wr = (WebResponse)data;
					if(wr.isSuccess()){
						List<Map<String, Object>> dataToList = wr.getDataToList();
						detList = dataToList;
						getStatis();
					}else{
						showErrorMsg(wr.getMessages());
						etx_sendNbr.setText("");
						getFocues(etx_sendNbr, true);//光标停留
						istrue = false;
					}
				}
			
			
		});
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
		}
	}
	/**
	 * 扫描标签处理
	 */
	private void getReturnByScan() {
		try {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return psterbiz.PsterSearScan(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite(),  ApplicationDB.user.getUserId(), etx_sendScan.getValStr(),etx_sendNbr.getValStr());
				}
				@Override
				public void onCallBrack(Object data) {
					 WebResponse wr = (WebResponse)data;
						if(wr.isSuccess()){
							//if(!wr.getMessages().equals("suc")){
							List<Map<String, Object>> dataToList = wr.getDataToList();
							type = dataToList.get(0).get("RFLOT_TYPE")+"";
							if(type.equals("L")){
								showErrorMsg("批标签不支持撤消!");
								type = "";
								istrue = false;
							}else{
								status = StringUtil.isEmpty(dataToList.get(0).get("RF_PK_STATUS")+"", "");
								etx_sendPart.setText(StringUtil.isEmpty(dataToList.get(0).get("RFLOT_PART")+"", "")); // 零件赋值
								etx_sendUm.setText(StringUtil.isEmpty(dataToList.get(0).get("RFLOT_UM")+"", "")); // 单位赋值
								etx_sendQty.setText(StringUtil.isEmpty(dataToList.get(0).get("RF_PK_QTY")+"", "")); // 数量赋值
								etx_sendAllPk.setText(StringUtil.isEmpty(dataToList.get(0).get("ALLCOUNT")+"", "")); // 总捡赋值
								boxseq = (Map<String, Object>)dataToList.get(0).get("BOXSEQ");
								if(status.equals("C")){ // c是撤消
									float dracont = StringUtil.parseFloat(dataToList.get(0).get("DRACOUNT").toString()) + StringUtil.parseFloat(dataToList.get(0).get("RF_PK_QTY").toString());
									etx_sendDqty.setText(dracont + ""); // 总撤赋值
									float rcount = StringUtil.parseFloat(dataToList.get(0).get("ALLCOUNT").toString()) - dracont;
									etx_sendRqty.setText(rcount + ""); // 实发赋值
								}else{
									float dracont = StringUtil.parseFloat(dataToList.get(0).get("DRACOUNT").toString()) - StringUtil.parseFloat(dataToList.get(0).get("RF_PK_QTY").toString());
									etx_sendDqty.setText(dracont + ""); // 总撤赋值
									float rcount = StringUtil.parseFloat(dataToList.get(0).get("ALLCOUNT").toString()) - dracont;
									etx_sendRqty.setText(rcount + ""); // 实发赋值
								}
								seq = dataToList.get(0).get("RFLOT_SEQ").toString();
								if(boxseq.size() != 0){
									if(status.equals("C") && !boxseq.get("RFLOT_PAR_BOX").equals("0")  ){
										istrue = false;
										showConfirm("该标签绑定有托标签，是否解除绑定？", removeBox);
									}else{
										istrue = false;
										submit(); // 
									}
								}else{
									istrue = false;
									submit(); // 
								}
								
							}
							
						}else{
							status = "";
							showErrorMsg(wr.getMessages());
							etx_sendScan.setText("");
							getFocues(etx_sendScan, true);//光标停留
							istrue = false;
						}
					}
			});
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
		}
	}
	/**
	 * 统计当前分拣单的总撤 总捡 实发
	 */
	private void getStatis(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
					return true;
			}
			@Override
			public Object onGetData() {
				return psterbiz.getPsterStatis(ApplicationDB.user.getUserDmain(),
						ApplicationDB.user.getUserSite(),ApplicationDB.user.getUserId(),etx_sendNbr.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				 WebResponse wr = (WebResponse)data;
					if(wr.isSuccess()){
						List<Map<String, Object>> dataToList = wr.getDataToList();
						summList = dataToList;
						getFocues(etx_sendScan, true);//光标停留
						tbl_pkAdv.setCurrentTab(1);
						runClickFun();
						istrue = true;
					}else{
						showErrorMsg(wr.getMessages());
						istrue = false;
					}
				}
		});
	}
	
	/**
	 * 提交
	 */
	private void submit(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
					return true;
			}
			@Override
			public Object onGetData() {
				return psterbiz.submitPsterStatis(ApplicationDB.user.getUserDmain(),
						ApplicationDB.user.getUserSite(),ApplicationDB.user.getUserId(),etx_sendNbr.getValStr(),
						etx_sendScan.getValStr(),status,etx_sendPart.getValStr(),type,seq);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse)data;
				if(wr.isSuccess()){
					//etx_sendNbr.setText(""); // 单号
					etx_sendScan.setText(""); // 标签
					status = "";
					seq = "";
					type = "";
					getFocues(etx_sendNbr, true);
					showMessage(wr.getMessages()); // 2019-04-17 by Gerry 弹框提示信息
					searchIsNbr();
					tbl_pkAdv.setCurrentTab(0);
				}else{
					showErrorMsg(wr.getMessages());
					istrue = false;
				}
			}
		});
	}
	
	/**
	 * 查询仓管员组，进行比较
	 */
	private void selectXrskg(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return psterbiz.selectXrskg(ApplicationDB.user.getUserDmain(),
						ApplicationDB.user.getUserSite(),ApplicationDB.user.getUserId(),etx_sendNbr.getValStr(),
						etx_sendScan.getValStr(),status,etx_sendPart.getValStr(),type,seq);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> dataToMap = wr.getDataToMap();
					List<Map<String, Object>> xrsList = (List<Map<String, Object>>)dataToMap.get("xskgList");
					List<Map<String, Object>> keeperList = (List<Map<String, Object>>)dataToMap.get("keeperList");
					
					int b=0;
					
					if(boxseq.size() != 0){
						if(status.equals("C") && !boxseq.get("RFLOT_PAR_BOX").equals("0")  ){
							istrue = false;
							showConfirm("该标签绑定有托标签，是否解除绑定？", removeBox);
						}else{
							istrue = false;
							submit(); // 
						}
					}else{
						istrue = false;
						submit(); // 
					}
				}else{
					showErrorMsg(wr.getMessages());
					istrue = false;
				}
			}
		});
	}
	
	/*private OnShowConfirmListen scfXrskgSub=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			if(boxseq.size()>0 && !boxseq.get("RFLOT_PAR_BOX").equals("0")){
				showConfirm("该标签绑定有托标签，是否解除绑定？", removeBox);
			}else{
				submit(); 
			}
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			
		}
	};*/
	/**
	 * I460解除箱拖绑定
	 */
	private OnShowConfirmListen removeBox=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						return true;
					}
					@Override
					public Object onGetData() {
						return psterbiz.removeBox(ApplicationDB.user.getUserDmain(),
								ApplicationDB.user.getUserSite(),ApplicationDB.user.getUserId(),etx_sendNbr.getValStr(),
								etx_sendScan.getValStr(),status,etx_sendPart.getValStr(),type,seq);
					}
					@Override
					public void onCallBrack(Object data) {
						WebResponse wr=(WebResponse)data;
						if(wr.isSuccess()){
							etx_sendScan.setText(""); // 标签
							status = "";
							seq = "";
							type = "";
							getFocues(etx_sendScan, true);
							showMessage(wr.getMessages()); // 2019-04-17 by Gerry 弹框提示信息
							searchIsNbr();
							tbl_pkAdv.setCurrentTab(0);
							istrue = true;
						}else{
							showErrorMsg(wr.getMessages());
							istrue = false;
						}
					}
				});	
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			
		}
	};
	
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Help}; // 2019-04-17 by Gerry 删除提交按钮
	}
	
	@Override
	public Object OnBtnHelpClick(ButtonType btnType, View v) {
		showSuccessMsg(R.string.help_ok);
		return  null ;
	}
	
	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}
	/**
	 * 提交按钮
	 
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v){
		clearMsg();
			if (!etx_sendNbr.getValStr().equals("")) { // 单号验证
				if(etx_sendScan.getValStr().equals("")){
					showMessage("请先扫描标签！");
					istrue = false;
					return istrue;
				}else{
					istrue = true;
					return istrue;
				}
			}else{
				showMessage("请先扫描单号！");
				istrue = false;
				return istrue;
			}
	}
	//etx_sendPart.setText(""); // 零件赋值
					//etx_sendUm.setText(""); // 单位赋值
					//etx_sendQty.setText(""); // 数量赋值
					//etx_sendDqty.setText(""); // 总撤赋值
					//etx_sendAllPk.setText(""); // 总捡赋值
					//etx_sendRqty.setText(""); // 实发赋值
					//detList = null;
					//summList = null;
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return psterbiz.psterSubmit(ApplicationDB.user.getUserDmain(),
				ApplicationDB.user.getUserSite(),ApplicationDB.user.getUserId(),etx_sendNbr.getValStr(),
				etx_sendScan.getValStr(),status,etx_sendPart.getValStr());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etx_sendNbr.setText(""); // 单号
			etx_sendScan.setText(""); // 标签
			etx_sendPart.setText(""); // 零件赋值
			etx_sendUm.setText(""); // 单位赋值
			etx_sendQty.setText(""); // 数量赋值
			status = "";
			etx_sendDqty.setText(""); // 总撤赋值
			etx_sendAllPk.setText(""); // 总捡赋值
			etx_sendRqty.setText(""); // 实发赋值
			detList = null;
			summList = null;
			getFocues(etx_sendNbr, true);
			showSuccessMsg(wr.getMessages());
			getPKList();
			tbl_pkAdv.setCurrentTab(0);
		}
	}*/
	
	

}
