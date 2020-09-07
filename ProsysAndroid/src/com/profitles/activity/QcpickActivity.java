package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.profitles.biz.QcpickBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class QcpickActivity extends AppFunActivity {
	private MyEditText actBox,actLocBin,actScat;
//	actBar,actNbr,
	private String iqtQty,RcvdQty;
	private MyTextView actPart,actQc,actUnit,actnums,actLot,actpartum,actpartdesc,actKjl;
	private MyReadBQ  actNbr,actBar;
	private QcpickBiz qbiz;
	private MyDataGrid dtg;
	private String domain, site,fnceffdate;
	private boolean actNum;
	private boolean gg = true;
	private List<Map<String,Object>> nbrlist= new ArrayList<Map<String,Object>>();
	WebResponse wrp;
	/*	actNbr 单号 actBar 扫码 actPart 零件 actLot 批次 qctQc检验
	 *	actUnit 每箱 actLocbin 仓储 DTG 检验明细 actNums 总量 actScat散量
	 *	actBox 箱数
	 **/
	@Override
	protected int getMainBodyLayout(){
		return R.layout.act_qcpick;
	}

	
	@Override
	protected void pageLoad() {	
		actKjl=actPart = (MyTextView) findViewById(R.id.etx_Kjl);
		actBox = (MyEditText) findViewById(R.id.etx_qcpickBox);
//      actNbr = (MyEditText) findViewById(R.id.etx_qcpickNbr);
//		actBar = (MyEditText) findViewById(R.id.etx_qcpickBar);
        actNbr = (MyReadBQ) findViewById(R.id.etx_qcpickNbr);
		actBar = (MyReadBQ) findViewById(R.id.etx_qcpickBar);
		actBar.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				barblur();
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actLocBin = (MyEditText) findViewById(R.id.etx_qcpicktoBin);
		actScat = (MyEditText) findViewById(R.id.etx_qcpickScat);
		actPart = (MyTextView) findViewById(R.id.txv_qcpickParts);
		actnums = (MyTextView) findViewById(R.id.txv_qcpicknums);
		actUnit = (MyTextView) findViewById(R.id.etx_qcpickUnit);
		actQc = (MyTextView) findViewById(R.id.etx_qcpickQC);
		actLot =  (MyTextView) findViewById(R.id.ext_qcpickLot);
		dtg = (MyDataGrid) findViewById(R.id.mdtg_qcpick);
		actpartum = (MyTextView) findViewById(R.id.txv_qcpickum);
		actpartdesc = (MyTextView) findViewById(R.id.txv_partdesc);
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		fnceffdate = ApplicationDB.user.getUserDate();
	}
	@Override
	protected void unLockNbr() {
		
	}


	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		_vff.addItemView(actNbr,actLocBin);
	}
	/** 光标进入是激发的方法  */
	@Override
	protected boolean onFocus(int id, View v) {
		if(R.id.etx_qcpickBox == id){
			if("0".equals(actBox.getValStr())){
				actBox.setText("");
			}
		}
		return true;
	}
	

	boolean istrue = false;
	/** 光标离开时激发的方法  */
	@Override
	protected boolean onBlur(int id, View v) {
		if(R.id.etx_qcpickNbr == id){
			showSuccessMsg("");
			checkNbr();
		}else if(R.id.etx_qcpickBar == id && istrue){
			runClickFun();
		}else if(R.id.etx_qcpickBox == id){
			//检验，每箱，箱数，散量
			if ("".equals(actBox.getValStr())) {
				actBox.setText("0");
			}else {
				actnums.setText((StringUtil.parseFloat(actBox.getValStr()==null?"0":actBox.getValStr())*(StringUtil.parseFloat(actUnit.getValStr()==null?"0":actUnit.getValStr())))+"");
			}
			runClickFun();
			return true;
		}else if(R.id.etx_qcpickScat == id){
			//检验，每箱，箱数，散量
			if ("".equals(actScat.getValStr())) {
				actScat.setText("0");
			}else{
				actnums.setText(((StringUtil.parseFloat(actBox.getValStr()==null?"0":actBox.getValStr()))*(StringUtil.parseFloat(actUnit.getValStr()==null?"0":actUnit.getValStr()))+(StringUtil.parseFloat(actScat.getValStr()==null?"0":actScat.getValStr())))+"");
			}
			runClickFun();
			return true;
		}
		return true;
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
				qbiz = new QcpickBiz();					
				return qbiz.qcpick_nbr(ApplicationDB.user.getUserDmain(),ApplicationDB.user.getUserSite(),actNbr.getValStr(),ApplicationDB.user.getUserId(),ApplicationDB.user.getMac());
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse rep = (WebResponse) data;
				if(rep.isSuccess()){
					nbrlist=((WebResponse) data).getDataToList();
						if(nbrlist!=null || nbrlist.size()!=0){
							//赋值 
							int t = 0;
							for (int i = 0; i < nbrlist.size(); i++ ) {
								if(!nbrlist.get(i).get("XRIQD_IQ_QTY").toString().equals("0.0")){
									t += 1;
									continue;
								}else{
									if(nbrlist.get(i).get("XRIQD_BIN") != null){
										actLocBin.setText(nbrlist.get(i).get("XRIQD_BIN").toString());
										break;
									}else{
										actLocBin.setText(nbrlist.get(i).get("XRIQD_LOC").toString());
										break;
									}
								}
							}
							if(t == nbrlist.size()){
								if(nbrlist.get(0).get("XRIQD_BIN") != null){
									actLocBin.setText(nbrlist.get(0).get("XRIQD_BIN").toString());
								}else{
									actLocBin.setText(nbrlist.get(0).get("XRIQD_LOC").toString());
								}
							}
							//My Gratify赋值
							dtg.buildData(nbrlist);
					}
				}else if(!rep.isSuccess()){
					getFocues(actNbr, true);
					showErrorMsg(rep.getMessages());
				}
			}
		});
		
	
	}
	
	public boolean panduan(){
		if("".equals(actnums.getValStr()) || "0".equals(actnums.getValStr()) || "0.0".equals(actnums.getValStr())){
			return false;
		}
		float scount = Float.valueOf(actnums.getValStr());
		float qc = Float.valueOf(actQc.getValStr());
		if(scount > qc){
			return false;
		}
		return true;
	}

	//页面添加按钮
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit,ButtonType.Help,ButtonType.Return};
	}
	//保存按钮
		@Override
		public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
			if("".equals(actNbr.getValStr())){
				showErrorMsg(getResources().getString(R.string.qc_nbrs_false));
				return false;
			}
			if("".equals(actBox.getValStr())||"".equals(actNbr.getValStr())||"".equals( actBar.getValStr())||"".equals( actLocBin.getValStr())||"".equals(actPart.getValStr())||"0.0".equals( actnums.getValStr())||"0".equals( actnums.getValStr())||"".equals( actUnit.getValStr())||"".equals( actQc.getValStr())||"".equals( actLot.getValStr())||"".equals( actpartum .getValStr())||"".equals(actpartdesc.getValStr())){	
				showErrorMsg("所填信息不完整不能保存!");
				return false;
			}
			boolean llp = panduan();
			if(!llp){
				showErrorMsg("实检量超过最大量！");
				getFocues(actBox, true);
				return false;
			}
			return true;
		}
		
		@Override
		public Object OnBtnSaveClick(ButtonType btnType, View v) {
				String nbr = actNbr.getValStr().toString();
				String locbin = actLocBin.getValStr();
				String partn = actPart.getValStr();
				String lotr = actLot.getValStr();
				String nums = actnums.getValStr();
				String scan = actBar.getValStr();
				String actpartumString = actpartum.getValStr();
				return qbiz.qcpick_save(domain, site, nbr, locbin, partn,lotr,nums,scan,actpartumString);
		}
		@Override
		public void OnBtnSaveCallBack(Object data) {
			WebResponse web = (WebResponse) data;
			if(web.isSuccess()){
				actBar.setText(""); 
				actPart.setText("");
				actpartum.setText(""); 
				actpartdesc.setText("");  
				actLot.setText("");  
				actQc.setText("");  
				actUnit.setText("");
				actScat.setText("");
				actnums.setText("");
				actBox.setText("");
				checkNbr();
				getFocues(actBar, true);
				showSuccessMsg("保存成功！");
			}else {
				showErrorMsg(web.getMessages());
			}
		}
		
		//帮助按钮
		@Override
		public Object OnBtnHelpClick(ButtonType btnType, View v) {
			return super.OnBtnHelpClick(btnType, v);
		}
		@Override
		public void OnBtnHelpCallBack(Object data) {
			super.OnBtnHelpCallBack(data);
		}
		
		@Override
		public boolean OnBtnHelpValidata(ButtonType btnType, View v) {
			return super.OnBtnHelpValidata(btnType, v);
		}

		//返回按钮
		public boolean OnBtnRtnValidata(ButtonType btnType, View v) {
			return true;
		}
		
		public WebResponse OnBtnRtnClick(ButtonType btnType, View v) {
			return qbiz.qcpick_return(domain, site, actNbr.getValStr(), ApplicationDB.user.getUserId(),ApplicationDB.user.getMac());
		}	
		
		public void OnBtnRtnCallBack(Object data) {
		
		}
		//提交按钮
		@Override
		public boolean OnBtnSubValidata(ButtonType btnType, View v) {
			if("".equals(actNbr.getValStr())||"".equals(actBar.getValStr())||"".equals( actLocBin.getValStr())||"".equals(actPart.getValStr())||"".equals( actnums.getValStr())||"".equals( actUnit.getValStr())||"".equals( actQc.getValStr())||"".equals( actLot.getValStr())||"".equals( actpartum .getValStr())||"".equals(actpartdesc.getValStr())||("".equals(actScat.getValStr())||"".equals(actBox.getValStr()))){
				showErrorMsg("数据不完整！");
				return false;
			}
			gg =getAtlqty();
//			for (int i = 0; i < nbrlist.size(); i++) {
//				if(nbrlist.size()==1){
//					notNull = "";
//					gg = true;
//					break;
//				}
//				if(!nbrlist.get(i).get("XRIQD_IQ_QTY").toString().equals("0.0")){
//					continue;
//				}else{
//					notNull = "y";
//				}
//			}
//			if(notNull != ""){
//				gg = false;
//				showConfirm("还有检验单未完成确定提交吗?", QueRen);
//			}
			return gg;
		}
		
		@Override
		public Object OnBtnSubClick(ButtonType btnType, View v) {
			if ("".equals(actBar.getValStr())) {
				return qbiz.qcpick_submit(domain, site, actNbr.getValStr(), "", "", "", "",ApplicationDB.user.getUserId(),ApplicationDB.user.getMac(),"","",fnceffdate);	
			} else {
				String nbr = actNbr.getValStr();
				String locbin = actLocBin.getValStr();
				String partn = actPart.getValStr();
				String lotr = actLot.getValStr();
//				String nums = actnums.getValStr();
				String nums =(Float.valueOf(actBox.getValStr())*Float.valueOf(actUnit.getValStr())+Float.valueOf(actScat.getValStr()))+"";
				String actpartumString = actpartum.getValStr();
				return qbiz.qcpick_submit(domain, site, nbr, locbin, partn,lotr,nums,ApplicationDB.user.getUserId(),ApplicationDB.user.getMac(),actpartumString,actBar.getValStr(),fnceffdate);	
			}
		}
		@Override
		public void OnBtnSubCallBack(Object data) {
			WebResponse web = (WebResponse) data;
			if(web.isSuccess()){
				actLocBin.setText("");
				actNbr.setText("");
				actBar.setText(""); 
				actPart.setText("");
				actpartum.setText(""); 
				actpartdesc.setText("");  
				actLot.setText("");  
				actQc.setText("");  
				actUnit.setText("");
				actScat.setText("");
				actnums.setText("");
				actBox.setText("");
				actKjl.setText("");
				List<Map<String , Object>> datanull = null; 
				dtg.buildData(datanull);
				getFocues(actNbr, true);	//光标停留
				showSuccessMsg(web.getMessages());
			}else{
				showErrorMsg(web.getMessages());
			}
		
		}
		
		private void barblur(){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					actnums.setText("0");
					if(actBar.getValStr().equals("")){
						return false;
					}
					return true; 
				}
				
				@Override
				public WebResponse onGetData() {
					return qbiz.qcpick_scan(domain,site,actBar.getValStr(),actNbr.getValStr(),actLocBin.getValStr());
				}
				
				@Override
				public void onCallBrack(Object data) {
					WebResponse wrs = (WebResponse) data;
					if(wrs.isSuccess()){
						Map<String, Object> map = new HashMap<String, Object>();
						map=wrs.getDataToMap();
						actPart.setText(map.get("RFLOT_PART")+"");
						actpartum.setText(map.get("RFLOT_UM")+"");
						actpartdesc.setText(map.get("RFLOT_PART_DESC")+"");
						actQc.setText(map.get("XRIQD_IQD_QTY")+"");
						actUnit.setText(map.get("RFLOT_MULT_QTY")+"");
						actLot.setText(map.get("RFLOT_LOT")+"");
						iqtQty=map.get("XRIQD_IQT_QTY")==null?"0":map.get("XRIQD_IQT_QTY")+"";
						RcvdQty=map.get("XRIQD_RCVD_QTY")+"";
						String box = "0", scat = "0";
						float RcvdQtyF=StringUtil.parseFloat(map.get("XRIQD_RCVD_QTY"));
						float IqtQtyF=StringUtil.parseFloat(map.get("XRIQD_IQT_QTY")==null?"0":map.get("XRIQD_IQT_QTY"));
						float IqdQtyF=StringUtil.parseFloat(map.get("XRIQD_IQD_QTY"));
						try {
							String multqty = map.get("RFLOT_MULT_QTY")!=null?map.get("RFLOT_MULT_QTY")+"":"0";
							if(map.get("XRIQD_IQT_QTY")==null || StringUtil.parseFloat(map.get("XRIQD_IQT_QTY")==null?"0":map.get("XRIQD_IQT_QTY"))==0){
								if(!multqty.equals("0")){
									box = ((int)Math.floor((IqdQtyF>=IqtQtyF?IqdQtyF-IqtQtyF:RcvdQtyF-IqtQtyF)/StringUtil.parseFloat(map.get("RFLOT_MULT_QTY"))))+"";
									scat = ((IqdQtyF>=IqtQtyF?IqdQtyF-IqtQtyF:RcvdQtyF-IqtQtyF)%StringUtil.parseFloat(map.get("RFLOT_MULT_QTY")))+"";
								}else{
									scat = (IqdQtyF>=IqtQtyF?IqdQtyF-IqtQtyF:RcvdQtyF-IqtQtyF)+"";
								}
							}else{
								box="0";
								scat="0";
								
							}
							
//							if(RcvdQtyF>=IqtQtyF){
//							if(!multqty.equals("0")){
//								box = ((int)Math.floor((IqdQtyF>=IqtQtyF?IqdQtyF-IqtQtyF:RcvdQtyF-IqtQtyF)/StringUtil.parseFloat(map.get("RFLOT_MULT_QTY"))))+"";
//								scat = ((IqdQtyF>=IqtQtyF?IqdQtyF-IqtQtyF:RcvdQtyF-IqtQtyF)%StringUtil.parseFloat(map.get("RFLOT_MULT_QTY")))+"";
//							}else{
//								scat = (IqdQtyF>=IqtQtyF?IqdQtyF-IqtQtyF:RcvdQtyF-IqtQtyF)+"";
//							}
//							}
						} catch (Exception e){
						}
						actBox.setText(box);
						actScat.setText(scat);
						actnums.setText((StringUtil.parseFloat(box) * StringUtil.parseFloat(map.get("RFLOT_MULT_QTY")) + StringUtil.parseFloat(scat))+"");
						actKjl.setText((RcvdQtyF-IqtQtyF)+"");
						istrue = true;
						getFocues(actScat, true);	//光标停留
					}else { 
						getFocues(actBar, true);	//光标停留
						showErrorMsg(wrs.getMessages());
						istrue = false;
					}
				}
			});
		}

		private boolean getAtlqty() {
			float numQc = Float.valueOf(actQc.getValStr());
			float nums = Float.valueOf(actBox.getValStr())*Float.valueOf(actUnit.getValStr())+Float.valueOf(actScat.getValStr());
			float iqtQtyF=Float.valueOf(iqtQty==null?"0":iqtQty);
			if(StringUtil.parseFloat(RcvdQty)>=iqtQtyF+nums){
				if(actBox.getValStr().equals("")){
					actBox.setText("0");
				}
				if (numQc >= nums+iqtQtyF) {
					actnums.setText(String.valueOf(nums));
				} else {
					if(!(iqtQty==null|| "".equals(iqtQty) ||"0".equals(iqtQty))){
						iqtQty=iqtQty==null?"0":iqtQty;
						actnums.setText(String.valueOf(nums));
						showConfirm("累加已检量"+iqtQty+"和本次实检量"+nums+"的和,大于建议量"+numQc+",要继续吗?", QueRen1);
					}else{
						showConfirm("实检量大于建议量"+numQc+",要继续吗?", QueRen1);
					}
//					actScat.setText("0");
//					actnums.setText("0");
					return false;
				}
		     }else{
		    	 showErrorMsg("累加已检量"+iqtQty+"和本次实检量"+nums+"的和,大于收货量"+RcvdQty+",不能再操作");
		    	 return false;
		     }
				return true;
		}

private OnShowConfirmListen QueRen1 =new OnShowConfirmListen() {
			
			@Override
			public void onConfirmClick() {
				loadDataBase(new IRunDataBaseListens() {

					@Override
					public boolean onValidata() {
						return true;
					}

					@Override
					public Object onGetData() {
						if ("".equals(actBar.getValStr())) {
							return qbiz.qcpick_submit(domain, site, actNbr.getValStr(), "", "", "", "",ApplicationDB.user.getUserId(),ApplicationDB.user.getMac(),"","",fnceffdate);	
						} else {
							String nbr = actNbr.getValStr();
							String locbin = actLocBin.getValStr();
							String partn = actPart.getValStr();
							String lotr = actLot.getValStr();
//							String nums = actnums.getValStr();
							String nums =(Float.valueOf(actBox.getValStr())*Float.valueOf(actUnit.getValStr())+Float.valueOf(actScat.getValStr()))+"";
							String actpartumString = actpartum.getValStr();
							return qbiz.qcpick_submit(domain, site, nbr, locbin, partn,lotr,nums,ApplicationDB.user.getUserId(),ApplicationDB.user.getMac(),actpartumString,actBar.getValStr(),fnceffdate);	
						}
					}

					@Override
					public void onCallBrack(Object data) {
						WebResponse web = (WebResponse) data;
						if(web.isSuccess()){
							actLocBin.setText("");
							actNbr.setText("");
							actBar.setText(""); 
							actPart.setText("");
							actpartum.setText(""); 
							actpartdesc.setText("");  
							actLot.setText("");  
							actQc.setText("");  
							actUnit.setText("");
							actScat.setText("");
							actnums.setText("");
							actBox.setText("");
							List<Map<String , Object>> datanull = null; 
							dtg.buildData(datanull);
							getFocues(actNbr, true);	//光标停留
							showSuccessMsg(web.getMessages());
						}else{
							showErrorMsg(web.getMessages());
						}  
					}
				});
			}
			
			@Override
			public void onCancelClick() {
				getFocues(actScat, true);	//光标停留
			}
		};
		@Override
		public String getAppVersion() {
			return ApplicationDB.user.getUserDate();
		}
		
		
		private OnShowConfirmListen QueRen =new OnShowConfirmListen() {
			
			@Override
			public void onConfirmClick() {
				loadDataBase(new IRunDataBaseListens() {

					@Override
					public boolean onValidata() {
						return true;
					}

					@Override
					public Object onGetData() {
						if ("".equals(actBar.getValStr())) {
							return qbiz.qcpick_submit(domain, site, actNbr.getValStr(), "", "", "", "",ApplicationDB.user.getUserId(),ApplicationDB.user.getMac(),"","",fnceffdate);	
						} else {
							String nbr = actNbr.getValStr();
							String locbin = actLocBin.getValStr();
							String partn = actPart.getValStr();
							String lotr = actLot.getValStr();
							String nums = actnums.getValStr();
							String actpartumString = actpartum.getValStr();
							return qbiz.qcpick_submit(domain, site, nbr, locbin, partn,lotr,nums,ApplicationDB.user.getUserId(),ApplicationDB.user.getMac(),actpartumString,actBar.getValStr(),fnceffdate);	
						}
					}

					@Override
					public void onCallBrack(Object data) {
						WebResponse web = (WebResponse) data;
						if(web.isSuccess()){
							actLocBin.setText("");
							actNbr.setText("");
							actBar.setText(""); 
							actPart.setText("");
							actpartum.setText(""); 
							actpartdesc.setText("");  
							actLot.setText("");  
							actQc.setText("");  
							actUnit.setText("");
							actScat.setText("");
							actnums.setText("");
							actBox.setText("");
							List<Map<String , Object>> datanull = null; 
							dtg.buildData(datanull);
							getFocues(actNbr, true);	//光标停留
							showSuccessMsg(web.getMessages());
						}else{
							showErrorMsg(web.getMessages());
						}
					}
				});
			}
			
			@Override
			public void onCancelClick() {
				getFocues(actScat, true);	//光标停留
			}
		};
}
