package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;

import com.profitles.biz.CnrcBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyLinearLayout;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.util.StringUtil;

public class CnrcActivity extends AppFunActivity {

	private String biaoshi1="0";
	private String biaoshi2="0";
	private String fnceffdate="";
	private CnrcBiz cnrcbiz;
	private MyEditText  etx_CnrcBox,etx_CnrcLot,
						etx_CnrcPart,etx_CnrcPart_Desc,etx_CnrcLot_Gone,
						etx_CnrcScat,etx_CnrcSum,etx_CnrcUM,etx_CnrcNbr_Gone,
						etx_CnrcUnit,etx_CnrcVend,etx_CnrcVend_Name,etx_CnrMes;
	private MyReadBQ  etx_CnrcBar,etx_CnrcNbr,etx_CnrcLocBin;
	private MyTabHost tbh_CnrcOne;
	private MyDataGrid dtg_CnrcDetail , dtg_CnrcBoxQuery;
	private ApplicationDB applicationDB;
	private List<Map<String , Object>> trList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> trBoxList = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_cnrc;
	}

	@Override
	protected void pageLoad() {
		tbh_CnrcOne=((MyTabHost) this.findViewById(R.id.tbh_CnrcOne));
		tbh_CnrcOne.setup();
		tbh_CnrcOne.setOnTabChangedListener(new OnTabChangeListener() {
			@Override	
			public void onTabChanged(String tabId) {
				if(tabId.equals("BoxQuery")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							dtg_CnrcBoxQuery.buildData(trBoxList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}
			}
		});
		
		  cnrcbiz=new CnrcBiz();
		  etx_CnrcNbr =(MyReadBQ) findViewById(R.id.etx_CnrcNbr);
		  etx_CnrcBar =(MyReadBQ) findViewById(R.id.etx_CnrcBar);
		  etx_CnrcBox =(MyEditText) findViewById(R.id.etx_CnrcBox);
		  etx_CnrcScat =(MyEditText) findViewById(R.id.etx_CnrcScat);
		  etx_CnrcSum= (MyEditText) findViewById(R.id.etx_CnrcSum);
		  etx_CnrcVend= (MyEditText) findViewById(R.id.etx_CnrcVend);
		  etx_CnrcVend_Name=(MyEditText) findViewById(R.id.etx_CnrcVend_Name);
		  etx_CnrcPart= (MyEditText) findViewById(R.id.etx_CnrcPart);
		  etx_CnrcPart_Desc=(MyEditText) findViewById(R.id.etx_CnrcPart_Desc);
		  etx_CnrcUM= (MyEditText) findViewById(R.id.etx_CnrcUM);
		  etx_CnrcUnit=(MyEditText) findViewById(R.id.etx_CnrcUnit);
		  etx_CnrcLocBin= (MyReadBQ) findViewById(R.id.etx_CnrcLocBin);
		  etx_CnrcLot=(MyEditText) findViewById(R.id.etx_CnrcLot);
		  etx_CnrcLot_Gone=(MyEditText) findViewById(R.id.etx_CnrcLot_Gone);
		  fnceffdate = ApplicationDB.user.getUserDate();
		  biaoshi1="0";
		  biaoshi2="0";
		  
		dtg_CnrcDetail = (MyDataGrid)findViewById(R.id.dtg_CnrcDetail);
		 // List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		//dtg_CnrcDetail.buildData(list);
		
		dtg_CnrcBoxQuery = (MyDataGrid)findViewById(R.id.dtg_CnrcBoxQuery);
		
		//dtg_CnrcBoxQuery.buildData(list);
	} 
	
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_CnrcNbr,etx_CnrcBar);
	}

	@Override
	protected void onChangedAft(int id, Editable s) {
		//箱数光标离开事件相对应的代码
		if(etx_CnrcBox.getId()==id){  	
			if(null != etx_CnrcBox.getValStr() && !"".equals( etx_CnrcBox.getValStr().toString().trim()) && !"".equals(etx_CnrcUnit.getValStr())){
				String rfc_Cons=applicationDB.Ctrl.getString("RFC_IS_VAL_CONS", "N");
				float Box =StringUtil.parseFloat(etx_CnrcBox.getValStr()); 		//箱数
				float Unit =StringUtil.parseFloat(etx_CnrcUnit.getValStr());   	//每箱
				if("".equals(etx_CnrcNbr.getValStr())){  //判断单号是否为空
					if("".equals(etx_CnrcScat.getValStr())){    //判断散量是否为空
						float Sum=(Box*Unit);
						etx_CnrcSum.setText(String.valueOf(Sum));
					}else{
						float Scat =StringUtil.parseFloat(etx_CnrcScat.getValStr());		//散量
						float Sum=(Box*Unit)+Scat;
						etx_CnrcSum.setText(String.valueOf(Sum));	
					}
				}else{
					if("N".equals(rfc_Cons)){     //系统控制文件  是否收货精确验证送货单  
						if("".equals(etx_CnrcScat.getValStr())){    //判断散量是否为空
							float Sum=(Box*Unit);
							etx_CnrcSum.setText(String.valueOf(Sum));
						}else{
							float Scat =StringUtil.parseFloat(etx_CnrcScat.getValStr());		//散量
							float Sum=(Box*Unit)+Scat;
							etx_CnrcSum.setText(String.valueOf(Sum));	
						}
					}else{
						loadDataBase(new IRunDataBaseListens() {    //先根据单号+零件查询送货单的发货量
							@Override
							public boolean onValidata() {
								return true;
							}
							@Override
							public Object onGetData() {
								return cnrcbiz.cnrcXrconsNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),
										etx_CnrcNbr.getValStr().toString(),etx_CnrcPart.getValStr().toString());
							}
							@Override
							public void onCallBrack(Object data) {
								float Box =StringUtil.parseFloat(etx_CnrcBox.getValStr()); 		//箱数
								float Unit =StringUtil.parseFloat(etx_CnrcUnit.getValStr());   	//每箱
								WebResponse wr = (WebResponse)data;
								if(wr.isSuccess()){
									String qty = wr.getWData().toString();
									float consQty=StringUtil.parseFloat(qty);
									if("".equals(etx_CnrcScat.getValStr())){    //判断散量是否为空
										float Sum=(Box*Unit);
										if(consQty==-1){
											etx_CnrcSum.setText(String.valueOf(Sum));
											clearMsg();
										}else{
											if(Sum!=consQty){
												istrue = false;
												showErrorMsg(getResources().getString(R.string.cnrc_consQty_false));
											}else{
												etx_CnrcSum.setText(String.valueOf(Sum));
												clearMsg();
											}
										}
									}else{
										float Scat =StringUtil.parseFloat(etx_CnrcScat.getValStr());		//散量
										float Sum=(Box*Unit)+Scat;
										if(consQty==-1){
											etx_CnrcSum.setText(String.valueOf(Sum));
											clearMsg();
										}else{
											if(Sum!=consQty){
												istrue = false;
												showErrorMsg(getResources().getString(R.string.cnrc_consQty_false));
											}else{
												etx_CnrcSum.setText(String.valueOf(Sum));
												clearMsg();
											}
										}
									}
								}else{
									istrue = false;
									showErrorMsg(getResources().getString(R.string.pk_getfalse));
								}
							}
						});
						
					}
				}
				
			}else{
				istrue = true;
			}

		}
		
		//散量光标离开事件相对应的代码
		if(etx_CnrcScat.getId()==id){
			//先判断散量不为空  
			if(null != etx_CnrcScat.getValStr() && !"".equals( etx_CnrcScat.getValStr().toString().trim())){
				//获取系统控制文件  是否收货精确验证送货单  YES NO    默认查询不到给N
				String rfc_Cons=applicationDB.Ctrl.getString("RFC_IS_VAL_CONS", "N");
				//获取散量文本框   强转Folat
				float Scat =StringUtil.parseFloat(etx_CnrcScat.getValStr());	
				//判断每箱量是否为空
				if("".equals(etx_CnrcUnit.getValStr())){  
					//判断散量是否大于等于0   
					if(Scat>=0){
						istrue = false;
						showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
					}else{
						etx_CnrcSum.setText(String.valueOf(Scat));	//总量
						clearMsg();
					}
				}else{     //每箱量不为空
					//获取每箱量文本值
					float Unit =StringUtil.parseFloat(etx_CnrcUnit.getValStr());
					//判断箱数是否为空
					if("".equals(etx_CnrcBox.getValStr())){  
						//散量大于等于每箱量
						if(Scat>=Unit){
							istrue = false;
							showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
						}else{
							etx_CnrcSum.setText(String.valueOf(Scat));	//总量
							clearMsg();
						}
						
					}else{   //箱数不为空
						//箱数 /每箱量/散量 都 不为空 判断单号是否为空  
						if("".equals(etx_CnrcNbr.getValStr())){  
							float Box =StringUtil.parseFloat(etx_CnrcBox.getValStr()); 		//箱数
							float Sum=(Box*Unit)+Scat;
							if(Scat>=Unit){
								istrue = false;
								showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
							}else{
								etx_CnrcSum.setText(String.valueOf(Sum));	//总量
								clearMsg();
							}
							
						}else{  //单号不为空  
							//系统控制文件 为N  不需要精确验证送货单  
							if("N".equals(rfc_Cons)){     
								float Box =StringUtil.parseFloat(etx_CnrcBox.getValStr()); 		//箱数
								float Sum=(Box*Unit)+Scat;
								if(Scat>=Unit){
									istrue = false;
									showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
								}else{
									etx_CnrcSum.setText(String.valueOf(Sum));	//总量
									clearMsg();
								}
								
							}else{//系统控制文件 为Y  需要精确验证送货单  
								loadDataBase(new IRunDataBaseListens() {    //先根据单号+零件查询送货单的发货量
									@Override
									public boolean onValidata() {
										return true;
									}
									@Override
									public Object onGetData() {
										//查询数据库  获取到送货单送货量
										return cnrcbiz.cnrcXrconsNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),
												etx_CnrcNbr.getValStr().toString(),etx_CnrcPart.getValStr().toString());
									}
									@Override
									public void onCallBrack(Object data) {
										float Scat =StringUtil.parseFloat(etx_CnrcScat.getValStr());		//散量
										float Box =StringUtil.parseFloat(etx_CnrcBox.getValStr()); 		//箱数
										float Unit =StringUtil.parseFloat(etx_CnrcUnit.getValStr());   	//每箱
										//前面已经判断  散量   箱数   每箱 都不会为空   
										float Sum=(Box*Unit)+Scat;					
										WebResponse wr = (WebResponse)data;
										if(wr.isSuccess()){
											String qty = wr.getWData().toString();
											//把数据库查出来的送货单数量转换成Float类型便于判断
											float consQty=StringUtil.parseFloat(qty);
											if(Scat>=Unit){
												istrue = false;
												showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
											}else{
												if(consQty==-1){
													etx_CnrcSum.setText(String.valueOf(Sum));	//总量
													clearMsg();
												}else{
													if(Sum!=consQty){
														istrue = false;
														showErrorMsg(getResources().getString(R.string.cnrc_consQty_false));
													}else{
														etx_CnrcSum.setText(String.valueOf(Sum));	//总量
														clearMsg();
													}
												}
											}
										}else{
											istrue = false;
											showErrorMsg(getResources().getString(R.string.pk_getfalse));
										}
									}
								});
							}
						}
					}
				}
			}else{
				istrue = true;
			}
		}		
	}



	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//单号光标离开事件相对应的代码
		if(etx_CnrcNbr.getId()==id){
			runClickFun();
			if(null != etx_CnrcNbr.getValStr() && !"".equals( etx_CnrcNbr.getValStr().toString().trim())){
				checkCnrcNbr();
				runClickFun();
			}else{
				istrue = true;
			}
		}
		
		//扫码光标离开事件相对应的代码
		if(etx_CnrcBar.getId()==id){
			runClickFun();
			if(null != etx_CnrcBar.getValStr() && !"".equals( etx_CnrcBar.getValStr().toString().trim())){
				checkCnrcCode();
				runClickFun();
			}else{
				etx_CnrcVend.reValue();
				etx_CnrcVend_Name.reValue();
				etx_CnrcPart.reValue();
				etx_CnrcPart_Desc.reValue();
				etx_CnrcUM.reValue();
				etx_CnrcUnit.reValue();
				etx_CnrcLocBin.reValue();
				etx_CnrcLot.reValue();
				etx_CnrcLot_Gone.reValue();
				etx_CnrcPart.setReadOnly(false);
				etx_CnrcVend.setReadOnly(false);
				clearMsg();
				istrue = true;
			}

		}
		
		//供方光标离开事件相对应的代码
		if(etx_CnrcVend.getId()==id){
			runClickFun();
			if(null != etx_CnrcVend.getValStr() && !"".equals( etx_CnrcVend.getValStr().toString().trim())){
				loadDataBase(new IRunDataBaseListens() {    
					@Override
					public boolean onValidata() {
						return true;
					}
					@Override
					public Object onGetData() {
						//查询数据库  获取到送货单送货量
						return cnrcbiz.cnrcVend(applicationDB.user.getUserDmain(), etx_CnrcVend.getValStr().toString());
					}
					@Override
					public void onCallBrack(Object data) {
						WebResponse wr = (WebResponse)data;
						if(wr.isSuccess()){
							String vend_Desc=(String) wr.getWData();
							etx_CnrcVend_Name.setText(vend_Desc);
							clearMsg();
							istrue = true;
							runClickFun();
						}else{
							showErrorMsg(wr.getMessages());
							getFocues(etx_CnrcVend, true);
						}
					}
				});				
			}else{
				istrue = true;
			}
		}
		
		if(etx_CnrcScat.getId() == id){
			runClickFun();
		}
		if(etx_CnrcBox.getId() == id){
			runClickFun();
		}
		//零件光标离开事件相对应的代码
		if(etx_CnrcPart.getId()==id){
			if(null != etx_CnrcPart.getValStr() && !"".equals( etx_CnrcPart.getValStr().toString().trim())){
				loadDataBase(new IRunDataBaseListens() {    
					@Override
					public boolean onValidata() {
						return true;
					}
					@Override
					public Object onGetData() {
						//查询数据库  获取到送货单送货量
						return cnrcbiz.cnrcPart(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(), etx_CnrcPart.getValStr().toString(),etx_CnrcVend.getValStr().toString());
					}
					@Override
					public void onCallBrack(Object data) {
						WebResponse wr = (WebResponse)data;
						if(wr.isSuccess()){
							Map<String, Object> map=wr.getDataToMap();
							etx_CnrcPart_Desc.setText(map.get("PART_DESC").toString());
							etx_CnrcUM.setText(map.get("XSPT_UM").toString());
							etx_CnrcUnit.setText(map.get("RFPTV_MULT_BOX").toString());
							etx_CnrcLocBin.setText(map.get("LocBin").toString());
							clearMsg();
							istrue = true;
							runClickFun();
						}else{
							showErrorMsg(wr.getMessages());
							etx_CnrcPart_Desc.reValue();
							etx_CnrcUM.reValue();
							etx_CnrcUnit.reValue();
							etx_CnrcLocBin.reValue();
							getFocues(etx_CnrcPart, true);
						}
					}
				});				
			}else{
				istrue = true;
			}

		}		
		
		//仓储光标离开事件相对应的代码		
		if(etx_CnrcLocBin.getId()==id){
			if(null != etx_CnrcLocBin.getValStr() && !"".equals( etx_CnrcLocBin.getValStr().toString().trim())){
				loadDataBase(new IRunDataBaseListens() {    //先根据单号+零件查询送货单的发货量
					@Override
					public boolean onValidata() {
						return true;
					}
					@Override
					public Object onGetData() {
						return cnrcbiz.cnrcLocBin(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_CnrcLocBin.getValStr().toString());
					}
					@Override
					public void onCallBrack(Object data) {
						WebResponse wr = (WebResponse)data;
						if(wr.isSuccess()){
							Map<String, Object> map=wr.getDataToMap();
							clearMsg();
							istrue=true;
							runClickFun();
						}else{
							if(!wr.isSuccess()){
								istrue = false;
								WebResponse wrp = (WebResponse)data;
								String mes=wrp.getMessages();
								showErrorMsg(mes);
								getFocues(etx_CnrcLocBin, true);
							}else{
								istrue = false;
								showErrorMsg(getResources().getString(R.string.pk_getfalse));
								getFocues(etx_CnrcLocBin, true);
							}
						}
					}
				});	
			}
		}		
		return istrue;
	}
	

	//处理单号校验
	private void checkCnrcNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return cnrcbiz.cnrcNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), 
						etx_CnrcNbr.getValStr().toString(), "XSPRH_HIST", applicationDB.user.getUserId(), applicationDB.user.getMac(),
						biaoshi1, biaoshi2);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				String cnrc=wr.getWData().toString();
				if(null != cnrc && wr.isSuccess()){
					String strc=cnrc.substring(cnrc.length()-2);   //由于这里长度减2   传过来的值必须长度大于等于2
					String crncData=cnrc.substring(0, cnrc.length()-2);
					if("B1".equals(cnrc)){    //系统收货精确验证为Y   并且单号不存在
						showConfirm("输入的单号错误 ,是否继续?", scfListen);
					}else if("B2".equals(strc)){   //系统收货精确验证为Y   单号存在
						showConfirm("该单已经在"+crncData+"进行了收货,继续收?", scfListenCrnc2);
					}else if("B3".equals(strc)){    //系统收货精确验证为N   单号存在
						showConfirm("该单已经在"+crncData+"进行了收货,继续收?", scfListenCrnc2);
					}else{
						if("S1".equals(cnrc) && wr.isSuccess()){
							clearMsg();
							istrue = true;
						}else{
							istrue = false;
							showErrorMsg(wr.getMessages());
						}
					}
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	private OnShowConfirmListen scfListen = new OnShowConfirmListen() {
		@Override
		public void onConfirmClick() {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return cnrcbiz.cnrcNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), 
							etx_CnrcNbr.getValStr().toString(), "XSPRH_HIST", applicationDB.user.getUserId(), applicationDB.user.getMac(),
							"B1", biaoshi2);
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					String cnrc=wr.getWData().toString();
					if(null != cnrc && wr.isSuccess()){
						String strc=cnrc.substring(cnrc.length()-2);
						String crncData=cnrc.substring(0, cnrc.length()-2);
						if("B2".equals(strc)){
							showConfirm("该单已经在"+crncData+"进行了收货,继续收?", scfListenCrnc);
						}
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
					}
				}
			});
		}
		@Override
		public void onCancelClick() {
			getFocues(etx_CnrcNbr, true);
			istrue=false;
		}
	};
	
	private OnShowConfirmListen scfListenCrnc=new OnShowConfirmListen() {   //验证事务表查看是否有收货记录
		@Override
		public void onConfirmClick() {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return cnrcbiz.cnrcNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), 
							etx_CnrcNbr.getValStr().toString(), "XSPRH_HIST", applicationDB.user.getUserId(), applicationDB.user.getMac(),
							"B1", "B2");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					String cnrc=wr.getWData().toString();
					if("S1".equals(cnrc) && wr.isSuccess()){
						clearMsg();
						istrue=true;
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
					}
				}
			});	
		}
		
		@Override
		public void onCancelClick() {
			getFocues(etx_CnrcNbr, true);
			istrue=false;
		}
	};
	
	private OnShowConfirmListen scfListenCrnc2=new OnShowConfirmListen() {   //系统收货精确验证为N 然后验证事务表查看是否有收货记录
		@Override
		public void onConfirmClick() {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return cnrcbiz.cnrcNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), 
							etx_CnrcNbr.getValStr().toString(), "XSPRH_HIST", applicationDB.user.getUserId(), applicationDB.user.getMac(),
							"0", "B2");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					String cnrc=wr.getWData().toString();
					if("S1".equals(cnrc) && wr.isSuccess()){
						clearMsg();
						runClickFun();
						istrue=true;
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
					}
				}
			});	
		}
		
		@Override
		public void onCancelClick() {
			getFocues(etx_CnrcNbr, true);
			istrue=false;
		}
	};
	
	//处理条码解析
	private void checkCnrcCode() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return cnrcbiz.cnrcCode(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_CnrcBar.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					etx_CnrcVend.setText(map.get("VEND").toString());
					etx_CnrcVend_Name.setText(map.get("VendDesc").toString());
					etx_CnrcPart.setText(map.get("PART").toString());
					etx_CnrcPart_Desc.setText(map.get("PartDesc").toString());
					etx_CnrcUM.setText(map.get("UM").toString());
					etx_CnrcUnit.setText(map.get("Unit").toString());
					etx_CnrcLocBin.setText(map.get("LocBin").toString());
					if("PFT_VEND_LOT".equals(map.get("RFPTV_LOT_MODE").toString())){
					  etx_CnrcLot.setText(map.get("LOT").toString()); 
					}
					etx_CnrcLot_Gone.setText(map.get("LOT").toString());
					etx_CnrcPart.setReadOnly(true);
					etx_CnrcVend.setReadOnly(true);
					clearMsg();
					runClickFun();
					istrue=true;
				}else{
					 etx_CnrcVend.reValue();
					 etx_CnrcVend_Name.reValue();
					 etx_CnrcPart.reValue();
					 etx_CnrcPart_Desc.reValue();
					 etx_CnrcUM.reValue();
					 etx_CnrcUnit.reValue();
					 etx_CnrcLocBin.reValue();
					 etx_CnrcLot.reValue();
					 etx_CnrcLot_Gone.reValue();
					 etx_CnrcPart.setReadOnly(false);
					 etx_CnrcVend.setReadOnly(false);
					 showErrorMsg(wr.getMessages());
					getFocues(etx_CnrcBar, true);
					istrue = false;
				}
			}
		});
	}
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Save,ButtonType.Help};
	}	
	
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return cnrcbiz.cnrcSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_CnrcLocBin.getValStr().toString(),
				etx_CnrcLot.getValStr().toString(), etx_CnrcPart.getValStr().toString(), etx_CnrcVend.getValStr().toString(),etx_CnrcBar.getValStr().toString(),
				etx_CnrcSum.getValStr().toString(), etx_CnrcUM.getValStr().toString(),etx_CnrcNbr.getValStr().toString(),
				"CnrcActivity", applicationDB.user.getUserId(), "maxnumber","POShipper", "3", "5",etx_CnrcLot_Gone.getValStr().toString(),fnceffdate);
	}
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(!"".equals(etx_CnrcBar.getValStr().toString().trim()) && !"".equals(etx_CnrcVend.getValStr().toString().trim()) && !"".equals(etx_CnrcPart.getValStr().toString().trim()) 
				&& !"".equals(etx_CnrcBox.getValStr().toString().trim()) && !"".equals(etx_CnrcLocBin.getValStr().toString().trim())){
		}else{
			showErrorMsg(getResources().getString(R.string.cnrc_consSave_false));
			istrue=false;
		}
		return istrue;
	}

	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			if("".equals(etx_CnrcNbr.getValStr().toString())){
				etx_CnrcNbr.setText(map.get("cnrcNbr").toString());
			}
			if("".equals(etx_CnrcLot.getValStr().toString())){
				etx_CnrcLot.setText(map.get("cnrcLot").toString());
			}
			 etx_CnrcBar.reValue();
			 etx_CnrcVend.reValue();
			 etx_CnrcVend_Name.reValue();
			 etx_CnrcPart.reValue();
			 etx_CnrcPart_Desc.reValue();
			 etx_CnrcUM.reValue();
			 etx_CnrcUnit.reValue();
			 etx_CnrcSum.reValue();
			 etx_CnrcScat.reValue();
			 etx_CnrcBox.reValue();
			 etx_CnrcLocBin.reValue();
			 etx_CnrcPart.setReadOnly(false);
			 etx_CnrcVend.setReadOnly(false);
			String msg=wr.getMessages();
			showSuccessMsg(msg);
			trList=(List<Map<String, Object>>) map.get("trList");
			trBoxList=(List<Map<String, Object>>) map.get("trBoxList");
			//dtg_CnrcDetail.clearData();
			//dtg_CnrcBoxQuery.clearData();
			dtg_CnrcDetail.buildData(trList);
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
		}
		
		super.OnBtnSaveCallBack(data);
	}
	
	@Override
	protected void unLockNbr() {
		if(!"".equals(etx_CnrcNbr.getValStr().toString().trim())){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {	
					return cnrcbiz.cnrcUnLock(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_CnrcNbr.getValStr(),"XSPRH_HIST", applicationDB.user.getUserId(),applicationDB.user.getMac());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse)data;
				}
			});	
		}
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
