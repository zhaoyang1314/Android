
package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;

import com.profitles.biz.UpBinPrsBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class UpBinPrsActivity extends AppFunActivity {
	/**初始化数据*/
	private UpBinPrsBiz upPrsbiz;
	private MyEditText  etx_ubpScat , etx_ubpBox ,txv_ubpUnit;
	private MyTextView  etx_ubpPart,txv_ubpDesc, txv_upbCount,txv_upbBox,txv_ubpUm,txv_ubpQty,txv_ubpQtyp,txvBaseMsg;
	private MySpinner spn_upbLot;//下拉框
	private MyReadBQ etx_ubpBar , etx_ubpFmBin;
	private LinearLayout txv_Box,txv_Boxs,txv_BoxOthers;
	private MyDataGrid dateGrid;
	private int num=0,partCount = 0,mult_pallet = 0,num_lbl = 0,k=0; 
	private float zQty, qty,scan_qty,QTY;
	private String domain, site, vend, statue="",fBin, scan, Lot, fLoc,part,user,rfptv_bin_group,
	 rfptv_lbs,locbin = "" , tlocbin = "",etx_ubpToBin=""/*仓库、储位*/,rfqc,RFLOT_NUM_LBL_NM,ref="",errStr="",RFBIN_GROUP="",RFPTV_BIN_GROUP="";
	private List<Map<String,Object>> listReturn = new ArrayList<Map<String,Object>>();
	private String jsonStr="";
	private boolean retrue = true,scanTrue = true,onPageLoad = true,isBar = false;
	private Map<String, Object> gridMap = new HashMap<String, Object>();
	/** 得到Android页面的主布局  */
	@Override
	protected int getMainBodyLayout(){
		return R.layout.act_upbinprs;
	}
	/** 加载页面时初始化参数  */
	@Override
	protected void pageLoad() {
		upPrsbiz = new UpBinPrsBiz();
		
		 etx_ubpBar =(MyReadBQ) findViewById(R.id.etx_ubpBar); // 条码
		 etx_ubpFmBin =(MyReadBQ) findViewById(R.id.etx_ubpFmBin); // 源储
		 etx_ubpPart = (MyTextView)findViewById(R.id.etx_ubpPart); // 零件
		 txv_ubpDesc = (MyTextView)findViewById(R.id.txv_ubpDesc); // 零件描述
		 txv_upbBox = (MyTextView)findViewById(R.id.txv_upbBox); // 箱数
		 txv_upbCount = (MyTextView)findViewById(R.id.txv_upbCount); //总数
		 spn_upbLot = (MySpinner)findViewById(R.id.spn_upbLot); // 批次 
		 txv_Box = (LinearLayout)findViewById(R.id.txv_Box);
		 txv_Boxs = (LinearLayout)findViewById(R.id.txv_Boxs);
		 txv_BoxOthers = (LinearLayout)findViewById(R.id.txv_BoxOthers);
		 txv_ubpUm = (MyTextView)findViewById(R.id.txv_ubpUm); // 单位
		 etx_ubpBox = (MyEditText)findViewById(R.id.etx_ubpBox); // 整箱
		 txv_ubpUnit = (MyEditText)findViewById(R.id.txv_ubpUnit); // 每箱量
		 etx_ubpScat = (MyEditText)findViewById(R.id.etx_ubpScat); // 散量
		 txv_ubpQty = (MyTextView)findViewById(R.id.txv_ubpQty); // 数量
		 
		 txv_ubpQtyp = (MyTextView)findViewById(R.id.txv_ubpQtyp);
		 dateGrid = (MyDataGrid)findViewById(R.id.mdtg_ubplists);//dateGrid
		 domain = ApplicationDB.user.getUserDmain();
		 user = ApplicationDB.user.getUserId();
		 site = ApplicationDB.user.getUserSite();
		 listReturn = new ArrayList<Map<String,Object>>();
		 noSubmitDate(user,"scan_wkfl","0");//查询存在未提交数据
		 etx_ubpBar.addTextChangedListener(new TextWatcher(){ // 条码扫码事件
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(etx_ubpBar.getValStr() != null && !"".equals(etx_ubpBar.getValStr().trim())){
							checkBar();
						}
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
		 } );
		 spn_upbLot.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
					Lot = spn_upbLot.getValStr();
					if (onPageLoad) {
						onPageLoad = false;
					} else {
						if(scan != null && !"".equals(scan.trim())&&scan.contains("$")){
							changQtyByLot(spn_upbLot.getValStr());
						}
					}
				};
				public void onNothingSelected(AdapterView<?> parent){};
			});
		 changeBatchInfoListen();//标签为L时监听箱数，每箱，散数 是否修改

	}
	 @Override
		protected boolean onBlur(int id, View v) {
			Boolean istrue = true;
			//扫码光标离开事件相对应的代码
			if(etx_ubpFmBin.getId()==id){
				runClickFun();
				if(null != etx_ubpFmBin.getValStr() && !"".equals( etx_ubpFmBin.getValStr().toString().trim())){
					getChock(etx_ubpFmBin.getValStr(),part,Lot,domain,site,vend);
				}
			}	
			return istrue ;
		}
	 
	private void getChock(final String bin,final String part,final String lot,final String domain,final String site,final String vend){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if(etx_ubpBar.getValStr() == null || "".equals(etx_ubpBar.getValStr().trim())||!etx_ubpBar.getValStr().contains("$")){
					etx_ubpBar.setText("");
					getFocues(etx_ubpBar, true);
					return false;
				}else{
					isBar = true;
				}
				if(listReturn != null && listReturn.size() >= 1){
					int size = listReturn.size();
					partCount=size;num_lbl=size;
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					for ( int i = 0; i < size; i++) {
						String SCAN =  listReturn.get(i).get("RFLOT_SCAN")+"";
						if(SCAN.equals(etx_ubpBar.getValStr())){
							return false;
						   }
						}
					}
				return isBar;
			}
			@Override
			public Object onGetData() {
				return upPrsbiz.getStock(bin,part,lot,domain,site,vend,ref);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map = wr.getDataToMap();
					if(map!=null){
						if(StringUtil.parseFloat(map.get("XSLD_QTY_OH"))<scan_qty){
							getFocues(etx_ubpFmBin, true);
							etx_ubpFmBin.setText("");
							showErrorMsg("该源储库存量"+StringUtil.parseFloat(map.get("XSLD_QTY_OH"))+"小于总数"+scan_qty+"，请重新扫描！");
						}else{
							QTY = StringUtil.parseFloat(map.get("XSLD_QTY_OH"));
							writeScan(saveData());
							dateGrid.buildData(listReturn);
							clearEmpty();
						}
					}else{
						showSuccessMsg("未查询到库存信息!");
					}
				}
			}
		});
	}
	/** 选择批次时修改库存*/
	private void changQtyByLot(final String lot) {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return upPrsbiz.getQtyByLoc(domain,site,etx_ubpPart.getValStr(),lot,vend,fLoc,fBin);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				if (wrp.isSuccess()) {
					Map<String, Object> map = wrp.getDataToMap();
					if(StringUtil.parseFloat(map.get("XSLD_QTY_OH"))<=0){
						showErrorMsg("该批次库存量不足！当前库存："+QTY);
					}else{
						QTY = StringUtil.parseFloat(map.get("XSLD_QTY_OH"));
						statue = map.get("XSLD_STATUS")+"";
						if(listReturn.size()>0){
							for(Map<String,Object> mapdate : listReturn){
								if(scan.equals(mapdate.get("RFLOT_SCAN"))){
									mapdate.put("RFLOT_LOT", spn_upbLot.getValStr());//批次
									mapdate.put("QTY", map.get("XSLD_QTY_OH"));//库存
									QTY = StringUtil.parseFloat(map.get("XSLD_QTY_OH"));
									dateGrid.buildData(listReturn);		
									writeScan(mapdate);
								}
							}
						}
					}
				}
			}
		});
	}
	/**
	 * @author smile
	 * 扫描条码操作
	 * */
	protected void checkBar() {
		retrue = true;/*scanTrue = true;*/
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return 	true;
			}
			@Override
			public Object onGetData() {
				return upPrsbiz.anlBarcodePrs(domain, site, user,etx_ubpBar.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				boolean ispart = false;
				try {
					if (wrp.isSuccess()) {
						Map mpRturn = wrp.getDataToMap();
						if(mpRturn.get("result").toString().equals("0")){
							if(listReturn != null && listReturn.size() >= 1){
								int size = listReturn.size();
								partCount=size;num_lbl=size;
								List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
								for ( int i = 0; i < size; i++) {
									String SCAN =  listReturn.get(i).get("RFLOT_SCAN")+"";
									if(SCAN.equals(etx_ubpBar.getValStr())){
											k = i;
											retrue=false;//当前的条码被扫描了
											if("L".equals(rfptv_lbs)){//按批管理
												if(zQty==0){
													showConfirm(SCAN+"标签总量为0,删除?", isDelInfo);
												}else{
													showConfirm(SCAN+"标签已扫描,删除?", isDelInfo);
													clearEmpty();
												}
											}else{
												showConfirm(SCAN+"标签已扫描,删除?", isDelInfo);
											}
										}
									}
								}
									String[] lot = getArrayFromMap(mpRturn,"lot",";");
									if(lot.length>0){
										Lot = lot[0];
									}
									mult_pallet = StringUtil.parseInt(mpRturn.get("mult_pallet"));//托盘量
									rfqc = mpRturn.get("rfqc")+"";
									fLoc = mpRturn.get("fLoc")+"";
									fBin = mpRturn.get("fBin")+"";
									part = mpRturn.get("RFLOT_PART").toString();
									scan = mpRturn.get("RFLOT_SCAN").toString();
									rfptv_lbs = mpRturn.get("RFPTV_LBS").toString();
									rfptv_bin_group = mpRturn.get("RFPTV_BIN_GROUP").toString();//零件存储类型
									vend = mpRturn.get("RFLOT_VEND").toString();
									locbin = etx_ubpFmBin.getValStr();//源储
									statue = mpRturn.get("status")+"";
									RFLOT_NUM_LBL_NM = mpRturn.get("RFLOT_NUM_LBL")+""; 
									ref = mpRturn.get("RFLOT_REF")+"";
									createPage(mpRturn,lot);
									showMessage(mpRturn.get("msgQTY").toString());
									return;
				        }
						if(StringUtil.parseInt(mpRturn.get("qty"))>0){
								QTY =  StringUtil.parseFloat(mpRturn.get("qty"));
								String[] lot = getArrayFromMap(mpRturn,"lot",";");
								if(lot.length>0){
									Lot = lot[0];
								}
								mult_pallet = StringUtil.parseInt(mpRturn.get("mult_pallet"));//托盘量
								rfqc = mpRturn.get("rfqc")+"";
								fLoc = mpRturn.get("fLoc")+"";
								fBin = mpRturn.get("fBin")+"";
								part = mpRturn.get("RFLOT_PART").toString();
								scan = mpRturn.get("RFLOT_SCAN").toString();
								rfptv_lbs = mpRturn.get("RFPTV_LBS").toString();
								rfptv_bin_group = mpRturn.get("RFPTV_BIN_GROUP").toString();//零件存储类型
								vend = mpRturn.get("RFLOT_VEND").toString();
								locbin = mpRturn.get("FLOC").toString();//源储
								statue = mpRturn.get("status")+"";
								RFLOT_NUM_LBL_NM = mpRturn.get("RFLOT_NUM_LBL")+""; 
								ref = mpRturn.get("RFLOT_REF")+"";
								
								if(listReturn != null || listReturn.size() >= 1){
									int size = listReturn.size();
									partCount=size;num_lbl=size;
									List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
									for ( int i = 0; i < size; i++) {
										String SCAN =  listReturn.get(i).get("RFLOT_SCAN")+"";
										if(SCAN.equals(etx_ubpBar.getValStr())){
												k = i;
												retrue=false;//当前的条码被扫描了
												if("L".equals(rfptv_lbs)){//按批管理
													if(zQty==0){
														showConfirm(SCAN+"标签总量为0,删除?", isDelInfo);
													}else{
														showConfirm(SCAN+"标签已扫描,删除?", isDelInfo);
														clearEmpty();
													}
												}else{
													showConfirm(SCAN+"标签已扫描,删除?", isDelInfo);
												}
											}
										}
									}
								if(retrue){//当前的条码未被扫描
								int size = listReturn.size();
								int num=0;
									if(size >= 1){
										for (int i = 0; i < size; i++) {
											String PART = listReturn.get(i).get("RFLOT_PART")+"";
											if(PART.equals(mpRturn.get("RFLOT_PART"))){
												ispart = true;
												}else{
													num++;
													if(num==size){
														upBinStrategy();//判断上架策略
													}
												}
											}
										}else{
											partCount++;num_lbl++;
											txv_upbBox.setText(num_lbl+"");//箱数
											createPage(mpRturn,lot);
												//判读map中是否有值
												String key = part+"|"+Lot+"|"+locbin; //扫描标签的判断依据 part+lot+bin
												if(gridMap.size()>0){
													if(gridMap.containsKey(key)){
														float BinQty = StringUtil.parseInt(gridMap.get(key));
														float Qty = 0;
														if("L".equals(rfptv_lbs)){
															Qty = StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
														}else{
															Qty = StringUtil.parseFloat(mpRturn.get("RFLOT_BOX_QTY"));//当前条码的单箱量
														}
														//如果有这个零件储位，获取上架数量判断是否大于当前库存量
														if(BinQty>=Qty){
															//如果大于，减少并放回map
															gridMap.put(key,BinQty-Qty);
														}else{
															clearEmpty();
															showErrorMsg("当前库储"+mpRturn.get("FLOC")+"该零件不足，无法继续上架!");
														}
													}else{//如果不存在，放入信息
														gridMap.put(key,QTY);
													}
												}else{//如果map中没有值，传入当前库存
													gridMap.put(key,QTY);
												}
												chectQTY(lot);
										}
										if(ispart){
											partCount++;num_lbl++;
											txv_upbBox.setText(num_lbl+"");//箱数
											createPage(mpRturn,lot);
												//判读map中是否有值
												String key = part+"|"+Lot+"|"+locbin; //扫描标签的判断依据 part+lot+bin
												if(gridMap.size()>0){
													if(gridMap.containsKey(key)){
														float BinQty = StringUtil.parseInt(gridMap.get(key));
														float Qty = 0;
														if("L".equals(rfptv_lbs)){
															Qty = StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
														}else{
															Qty = StringUtil.parseFloat(mpRturn.get("RFLOT_BOX_QTY"));//当前条码的单箱量
														}
														//如果有这个零件储位，获取上架数量判断是否大于当前库存量
														if(BinQty>=Qty){
															//如果大于，减少并放回map
															gridMap.put(key,BinQty-Qty);
														}else{
															clearEmpty();
															showErrorMsg("当前库储"+mpRturn.get("FLOC")+"该零件不足，无法继续上架!");
														}
													}else{//如果不存在，放入信息
														gridMap.put(key,QTY);
													}
												}else{//如果map中没有值，传入当前库存
													gridMap.put(key,QTY);
												}
												chectQTY(lot);
											}
										}
							}else{
								clearEmpty();
								showErrorMsg("该标签库存量"+mpRturn.get("qty")+",无法上架！");
							}
						}else{
							if(wrp.getErrNbr().equals("PFT_000001")){
								errStr = wrp.getMessages();
								checkloc(etx_ubpBar.getValStr());
								return;
							}
							clearEmpty();
							getFocues(etx_ubpBar, true);
							showErrorMsg(wrp.getMessages());
						}
				} catch (Exception e) {
					txv_upbBox.setText("");//箱数
					txv_upbCount.setText("");//总数
					clearEmpty();
					getFocues(etx_ubpBar, true);
					showErrorMsg("没有相关数据,检查条码信息");
				}
			}
		});
	}
	//判断扫描是库位还是储位
	boolean isPass = true;
	protected void checkloc( final String lockey) {
		isPass = true;
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				/*if(scan_qty>QTY){
					isPass= false;
					getFocues(etx_ubpFmBin, true);
					etx_ubpFmBin.setText("");
					showErrorMsg("当前库存量"+QTY+"小于总数"+scan_qty+",请扫描变更源储！");
				}*/
				return isPass;
			}
			@Override
			public Object onGetData() {
				return upPrsbiz.checkLocBin(lockey,domain,site);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				Map<String, Object> binMp = wrp.getDataToMap();
				if (wrp.isSuccess()) {
					tlocbin= binMp.get("LOC").toString();//至仓
					etx_ubpToBin=binMp.get("BIN").toString();//至储
					RFBIN_GROUP=binMp.get("RFBIN_GROUP").toString();//储位类型
					if(listReturn != null && listReturn.size() >= 1){
						List<String> cache = new ArrayList<String>();
						int size = listReturn.size();
						  for (int i = 0; i < size; i++) {
								JSONObject obj = new JSONObject(listReturn.get(i)); 
								cache.add(obj.toString());  
								statue = listReturn.get(0).get("STATUS").toString();
							}
							jsonStr = cache.toString(); 
								loadDataBase(new IRunDataBaseListens() {
							@Override
							public boolean onValidata() {
								int size = listReturn.size();
								partCount=size;num_lbl=size;
								List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
								if(RFBIN_GROUP.toUpperCase().equals("ALL")){
									for ( int i = 0; i < size; i++) {
										String FBIN =  listReturn.get(i).get("FBIN")+"";
											if(FBIN.equals(etx_ubpBar.getValStr())){
												showErrorMsg("上架储位与标签源储有相同，不能重复上架");
												return false;
											}
										}
								}else{
									for ( int i = 0; i < size; i++) {
										    String FBIN =  listReturn.get(i).get("FBIN")+"";
										    String rfptv_bin_group =  (listReturn.get(i).get("RFPTV_BIN_GROUP")+"").toUpperCase();
											if(FBIN.equals(etx_ubpBar.getValStr())){
												showErrorMsg("上架储位与标签源储有相同，不能重复上架");
												return false;
											}
											if(!rfptv_bin_group.equals("ALL")&&!rfptv_bin_group.equals(RFBIN_GROUP)){
												showErrorMsg("储位类型被限制为只允许["+RFBIN_GROUP+"]零件上架,零件 "+listReturn.get(i).get("RFLOT_PART")+"的储位类型为[" + rfptv_bin_group+"]");
												return false;
											}
										}
								}
								return true;
							}
							@Override
							public Object onGetData() {
								//上架
								return upPrsbiz.upbinPrsSave(domain, site, jsonStr, ApplicationDB.user.getUserId(),tlocbin,etx_ubpToBin, statue, ApplicationDB.user.getUserDate().toString());
							}
							@Override
							public void onCallBrack(Object data) {
								scan_qty=0;partCount=0;num_lbl=0;
								WebResponse wrp = (WebResponse)data;
								if (wrp.isSuccess()) {
									showSuccessMsg(wrp.getMessages());
									tlocbin = "";etx_ubpToBin ="";
									listReturn.clear();
									dateGrid.clearData();
									clearEmpty();
									isBar = false;
								}
								txv_upbBox.setText("");//箱数
								txv_upbCount.setText("");//总数
							}
						});
					}else{
						showTextMessage("未检测待上架数据，请扫描标签后进行保存操作！");
					}
				}else{
					if(wrp.getErrNbr().equals("PFT_000001")){
						showErrorMsg(wrp.getMessages());
					}else{
						showErrorMsg(errStr);
					}
					clearEmpty();
					clearMsg();
					tlocbin = "";etx_ubpToBin ="";
					getFocues(etx_ubpBar, true);
				}
			}
		});
	}
	private void createPage(Map mpRturn,String[] lot){
		if("L".equals(rfptv_lbs)){//按批管理
			txv_Box.setVisibility(View.VISIBLE);
			txv_Boxs.setVisibility(View.VISIBLE);
			txv_BoxOthers.setVisibility(View.GONE);
			
			etx_ubpBox.setText(Math.floor(QTY/StringUtil.parseFloat(mpRturn.get("RFLOT_MULT_QTY")))+"");//箱数
			etx_ubpScat.setText(QTY-Math.floor(QTY/StringUtil.parseFloat(mpRturn.get("RFLOT_MULT_QTY")))+"");//散量
//			if(StringUtil.parseInt(mpRturn.get("RFLOT_SCATTER_QTY"))>0){
//				etx_ubpBox.setText(mpRturn.get("RFLOT_SCATTER_QTY")+"");//箱数
//			}else{
//				etx_ubpBox.setText(mpRturn.get("RFLOT_MULT_QTY")+"");//箱数
//			}
			etx_ubpScat.setText("0");//散量
			txv_ubpUnit.setText(mpRturn.get("RFLOT_BOX_QTY")+"");//每箱
			zQty = StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
			qty = StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
			txv_ubpQtyp.setText(zQty+"");//数量
			clearMsg();
	}else{	//按件S/箱B/托盘P管理
			if(txv_Box.getVisibility()==0&&txv_Boxs.getVisibility()==0){//0    --------   VISIBLE    可见
				txv_Box.setVisibility(View.GONE);
				txv_Boxs.setVisibility(View.GONE);
				txv_BoxOthers.setVisibility(View.VISIBLE);
			}
			float tmp_qty = StringUtil.parseFloat(StringUtil.isEmpty(mpRturn.get("RFLOT_SCATTER_QTY"), "0"));
			zQty = tmp_qty == 0 ? StringUtil.parseFloat(mpRturn.get("RFLOT_BOX_QTY")) : tmp_qty;	//mpRturn.get("RFLOT_BOX_QTY"))*StringUtil.parseFloat(mpRturn.get("RFLOT_MULT_QTY"))+StringUtil.parseFloat(mpRturn.get("RFLOT_SCATTER_QTY"));
			if(mpRturn.get("result").equals("1")){
				scan_qty = scan_qty+zQty;//总数
			}
			txv_ubpQty.setText(zQty+"");//数量
			txv_upbCount.setText(scan_qty+"");//总数
	}
	etx_ubpFmBin.setText(mpRturn.get("FLOC")+"");//源储
	etx_ubpPart.setText(mpRturn.get("RFLOT_PART")+"");//零件
	MyTextView desc=txv_ubpDesc; //获取文本
	desc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE); //设置EditText的显示方式为多行文本输入  
	desc.setGravity(Gravity.TOP);  //文本显示的位置在EditText的最上方  
	desc.setText(mpRturn.get("RFLOT_PART_DESC").toString());  
	desc.setSingleLine(false);  //改变默认的单行模式  
	txv_ubpUm.setText(mpRturn.get("RFLOT_UM")+"");//单位
	spn_upbLot.addAndClearItems(lot);//批次
	}
	//查询存在未提交数据
	public void noSubmitDate(final String user,final String table, final String status){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return upPrsbiz.getScannedInfo(domain,user,table,status);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					final List<Map<String, Object>> list = wr.getDataToList();
					if(list.size()>0){
						showConfirm("存在未提交的数据，是否清除?", new OnShowConfirmListen() { 
							@Override
							public void onConfirmClick() {   //询问框  点确定
								loadDataBase(new IRunDataBaseListens() {
									@Override
									public boolean onValidata() {
										return true;
									}
									@Override
									public Object onGetData() {
										return upPrsbiz.deleteScanned(domain,user,table,status);
									}
									@Override
									public void onCallBrack(Object data) {
										WebResponse wrp = (WebResponse)data;
										Map<String, Object> map = wrp.getDataToMap();
										if (wrp.isSuccess()) {
												showSuccessMsg("清除成功！");
												listReturn.clear();
												gridMap.clear();
												clearEmpty();
												clearMsg();
										}
									}
								});
							}
							@Override
							public void onCancelClick() {  //询问框  点取消
								String RFPTV_LBS = "";
								if(list.size()>0){
									RFPTV_LBS = list.get(0).get("RFPTV_LBS")+"";
									QTY = StringUtil.parseFloat(list.get(0).get("QTY"));//库存
									for(int i=0;i<list.size();i++){
										partCount++;num_lbl++;
										scan_qty=scan_qty+StringUtil.parseFloat(list.get(i).get("CQTY"));//总量
										list.get(i).put("RFLOT_NUM_LBL",list.size()-i);
										listReturn.add(list.get(i));
										dateGrid.buildData(listReturn);
									}
								}
								if(!RFPTV_LBS.equals("L")){
									txv_upbBox.setText(list.size()+"");//箱数
									txv_upbCount.setText(scan_qty+"");//总数
								}
							}
						});
						
					}
				}
			}
		});
	}
	/** 向页面添加按钮  */
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Save};
	}
	/** 保存按钮被点击时激发的方法 Begin  TODO*/
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if ("Y".equals(rfqc)) {
			if (!"GOOD".equals(statue)) {
				showErrorMsg("合格状态才能上架!");
				return false;
			}
		}else if(listReturn.size() < 0 && !StringUtil.isEmpty(etx_ubpBar.getValStr())&&!StringUtil.isEmpty(etx_ubpPart.getValStr())){
			showErrorMsg("扫描的数据存储到列表之后进行操作");
			return false;
		}else if(listReturn == null || listReturn.size() == 0){
			showErrorMsg("列表中没有数据,无法进行上架操作");
			return false;
		}else if(StringUtil.isEmpty(tlocbin)&&listReturn.size() > 0){//判断储位/库位是否为空
			showErrorMsg("请扫描仓库/储位码之后进行保存操作");
			getFocues(etx_ubpBar, true);
			return false;
		}
		/*else if(scan_qty>QTY){
			getFocues(etx_ubpFmBin, true);
			etx_ubpFmBin.setText("");
			showErrorMsg("当前库存量"+QTY+"小于总数"+scan_qty+",请扫描变更源储！");
			return false;
		}*/
		int size = listReturn.size();
		partCount=size;num_lbl=size;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(RFBIN_GROUP.toUpperCase().equals("ALL")){
			for ( int i = 0; i < size; i++) {
				String FBIN =  listReturn.get(i).get("FBIN")+"";
					if(FBIN.equals(etx_ubpBar.getValStr())){
						showErrorMsg("上架储位与标签源储有相同，不能重复上架");
						return false;
					}
				}
		}else{
			for ( int i = 0; i < size; i++) {
				    String FBIN =  listReturn.get(i).get("FBIN")+"";
				    String rfptv_bin_group =  (listReturn.get(i).get("RFPTV_BIN_GROUP")+"").toUpperCase();
					if(FBIN.equals(etx_ubpBar.getValStr())){
						showErrorMsg("上架储位与标签源储有相同，不能重复上架");
						return false;
					}
					if(!rfptv_bin_group.equals("ALL")&&!rfptv_bin_group.equals(RFBIN_GROUP)){
						showErrorMsg("储位类型被限制为只允许["+RFBIN_GROUP+"]零件上架,零件 "+listReturn.get(i).get("RFLOT_PART")+"的储位类型为[" + rfptv_bin_group+"]");
						return false;
					}
				}
		}
		return true;
	}
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		List<String> cache = new ArrayList<String>();
		int size = listReturn.size();
		  for (int i = 0; i < size; i++) {
				JSONObject obj = new JSONObject(listReturn.get(i)); 
				cache.add(obj.toString());  
				statue = listReturn.get(0).get("STATUS").toString();
			}
			jsonStr = cache.toString(); 
		return upPrsbiz.upbinPrsSave(domain, site, jsonStr, ApplicationDB.user.getUserId(), tlocbin,etx_ubpToBin, statue, ApplicationDB.user.getUserDate().toString());
	}
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		scan_qty=0;partCount=0;num_lbl=0;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			showSuccessMsg(wr.getMessages());
			tlocbin = "";etx_ubpToBin ="";isBar = false;
			listReturn.clear();
			gridMap.clear();
		}else{
			clearMsg();
			showErrorMsg(wr.getMessages());
		}
		dateGrid.clearData();
		clearEmpty();
		txv_upbBox.setText("");//箱数
		txv_upbCount.setText("");//总数
		getFocues(etx_ubpBar, true);

	}
	//判断当前的条码对应的件号累计量 >= 托盘量 
	private void chectQTY(String[] lot) {
		if(!"L".equals(rfptv_lbs)){
			writeScan(saveData());
			dateGrid.buildData(listReturn);
			etx_ubpBar.setText("");
		}
	}
	//判断上架策略
	private void upBinStrategy(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return upPrsbiz.getStrategy(domain,site);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map = wr.getDataToMap();
					if(0==StringUtil.parseInt(map.get("RFC_UPBIN_HPPT"))){
						showErrorMsg("策略限制不能混件上架！");
						clearEmpty();
						txv_upbBox.setText(num_lbl+"");//箱数
					}else if(1==StringUtil.parseInt(map.get("RFC_UPBIN_HPPT"))){
						partCount++;num_lbl++;
						txv_upbBox.setText(num_lbl+"");//箱数
						writeScan(saveData());
						dateGrid.buildData(listReturn);
						getFocues(etx_ubpBar, true);
						etx_ubpBar.setText("");
					}else{
						clearEmpty();
						showErrorMsg("该"+domain+"域下的地点"+site+"未设置上架策略!");
					}
				}
			}
		});
	}
	/**确认框事件*/
	private OnShowConfirmListen isDelInfo=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			deleteDate(listReturn.get(k).get("RFLOT_SCAN").toString());
			scan_qty = scan_qty - StringUtil.parseFloat(listReturn.get(k).get("CQTY"));
			txv_upbCount.setText(scan_qty+"");//总数
			listReturn.remove(k);
			int size = listReturn.size();
			txv_upbBox.setText(size+"");//箱数
			if(size>0){
				for(int i=0;i<size;i++){
					listReturn.get(i).put("RFLOT_NUM_LBL",listReturn.size()-i);
				}
				dateGrid.buildData(listReturn);
			}else{
				dateGrid.clearData();
				txv_upbBox.setText("");
				txv_upbCount.setText("");
			}
			clearEmpty();
		}
		@Override
		public void onCancelClick() {  //询问框  点取消
			if(zQty==0&&"L".equals(rfptv_lbs)){
				getFocues(etx_ubpBar, true);
			}else{
				etx_ubpBar.setText("");
				getFocues(etx_ubpBar, true);
			}
			
		}
	};
	public void deleteDate(final String scan){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return upPrsbiz.deleteScan(domain,user,"scan_wkfl","0",scan);//删除此条码
			}
			@Override
			public void onCallBrack(Object data) {
				String key = part+"|"+Lot+"|"+locbin; //扫描标签的判断依据 part+lot+bin
				gridMap.put(key, StringUtil.parseInt( gridMap.get(key))+QTY);
			}
		});
	}
	//写入扫描作业表
	private void writeScan(final Map<String,Object> map){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return upPrsbiz.createWkfl(map.get("RFLOT_NUM_LBL")+"",map.get("RFLOT_SCAN")+"",map.get("RFLOT_PART")+"",map.get("RFLOT_LOT")+"",
									map.get("CQTY")+"",map.get("QTY")+"",map.get("FBIN")+"",user,domain,site,vend,map.get("RFLOT_UM")+"",map.get("STATUS")+"",map.get("RFPTV_LBS")+"",
									map.get("RFLOT_MULT_QTY")+"",map.get("RFLOT_BOX_QTY")+"",map.get("RFLOT_SCATTER_QTY")+"");
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse)data;
					if(wr.isSuccess()){
						getFocues(etx_ubpBar, true);
					}
				}
			});
	}

	/** 把String类型的'批次'换成数组  */
	private String[] getArrayFromMap(Map<String, Object> map,String key,String splTag){
		String str = map.get(key).toString();
		if (null != str && !"".equals(str)) {
			return str.split(splTag);
		}
		return null;
	}
	Boolean etx_ubpBox_change=false,txv_ubpUnit_change=false,etx_ubpScat_change=false;
	private void changeBatchInfoListen(){//标签为L时监听箱数，每箱，散数 是否修改
		num=0;
		etx_ubpBox_change=false;txv_ubpUnit_change=false;etx_ubpScat_change=false;
		etx_ubpBox.addTextChangedListener( new TextWatcher(){ 
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,int after) {
					qty=StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				@Override
				public void afterTextChanged(Editable s) {
					loadDataBase(new IRunDataBaseListens() {
						@Override
						public boolean onValidata() {
									if(etx_ubpBox.getValStr() != null && !"".equals(etx_ubpBox.getValStr().trim())){
										etx_ubpBox_change= true;
											if(StringUtil.parseInt(etx_ubpBox.getValStr())==0){
												txv_ubpUnit.setEnabled(true);
												txv_ubpUnit.requestFocus();
												txv_ubpUnit.setFocusableInTouchMode(true);
											}else{
												txv_ubpUnit.setEnabled(false);
											}
									}
									return etx_ubpBox_change;
						}
						@Override
						public Object onGetData() {
							num=0;
							return true;
						}
						@Override
						public void onCallBrack(Object data) {
							zQty = StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
							txv_ubpQtyp.setText(zQty+"");//数量
								if(etx_ubpBox_change==true){
										saveBatchInfo();
										etx_ubpBox_change=false;
										num++;
							}
						}
					});
				}
		 });
	txv_ubpUnit.addTextChangedListener( new TextWatcher(){ 
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,int after) {
					qty=StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
					loadDataBase(new IRunDataBaseListens() {
						@Override
						public boolean onValidata() {
							if(txv_ubpUnit.getValStr() != null && !"".equals(txv_ubpUnit.getValStr().trim())){
								txv_ubpUnit_change= true;
							}
							return txv_ubpUnit_change;
						}
						@Override
						public Object onGetData() {
							return true;
						}
						@Override
						public void onCallBrack(Object data) {
						}
					});
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				@Override
				public void afterTextChanged(Editable s) {
					loadDataBase(new IRunDataBaseListens() {
						@Override
						public boolean onValidata() {
							return true;
						}
						@Override
						public Object onGetData() {
							num=0;
							return true;
						}
						@Override
						public void onCallBrack(Object data) {
							zQty = StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
							txv_ubpQtyp.setText(zQty+"");//数量
							if(txv_ubpUnit_change){
								saveBatchInfo();
								txv_ubpUnit_change=false;
								num++;
							}
						}
					});
				}
		 });
		etx_ubpScat.addTextChangedListener( new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,int after) {
					qty=StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				@Override
				public void afterTextChanged(Editable s) {
					loadDataBase(new IRunDataBaseListens() {
						@Override
						public boolean onValidata() {
									if(etx_ubpScat.getValStr() != null && !"".equals(etx_ubpScat.getValStr().trim())){
										etx_ubpScat_change= true;
									}
							return etx_ubpScat_change;
						}
						@Override
						public Object onGetData() {
							num=0;
							return true;
						}
						@Override
						public void onCallBrack(Object data) {
							zQty = StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
							txv_ubpQtyp.setText(zQty+"");//数量
							if(etx_ubpScat_change){
								saveBatchInfo();
								etx_ubpScat_change=false;
								num++;
							}
						}
					});
				}
		 });
	
	}
	private void saveBatchInfo(){
		int num=0;
		if(txv_ubpUnit.getValStr() != null && !"".equals(txv_ubpUnit.getValStr().trim())&&
				etx_ubpBox.getValStr() != null && !"".equals(etx_ubpBox.getValStr().trim())&&
						etx_ubpScat.getValStr() != null && !"".equals(etx_ubpScat.getValStr().trim())&&"L".equals(rfptv_lbs)){
			if(qty!=zQty){
				if(listReturn.size()>0){
					for(Map<String,Object> map : listReturn){
						if(scan.equals(map.get("RFLOT_SCAN"))){
							map.put("RFLOT_PART", part);//零件
							map.put("CQTY", zQty);//数量
							map.put("RFLOT_SCAN", scan);//标签
							map.put("FBIN", etx_ubpFmBin.getValStr());//源储
							map.put("RFLOT_LOT", spn_upbLot.getValStr());//批次
							map.put("RFPTV_LBS", rfptv_lbs);
							map.put("RFLOT_UM", txv_ubpUm.getValStr());
							map.put("RFLOT_VEND", vend);
							map.put("RFLOT_MULT_QTY", etx_ubpBox.getValStr());
							map.put("RFLOT_BOX_QTY", txv_ubpUnit.getValStr());
							map.put("RFLOT_SCATTER_QTY", etx_ubpScat.getValStr());
							dateGrid.buildData(listReturn);				
							writeScan(map);
						}else{
							num++;
							if(num==listReturn.size()){
								writeScan(saveData());
								dateGrid.buildData(listReturn);				
							}
						}
					}
				}else{
					writeScan(saveData());
					dateGrid.buildData(listReturn);				
				}
			}
		}
	}
	//储存dateGrid的值
	private Map<String, Object> saveData(){
		Map<String, Object> date= new HashMap<String, Object>();
		date.put("RFLOT_NUM_LBL", num_lbl);//序号
		date.put("RFLOT_NUM_LBL_NM", RFLOT_NUM_LBL_NM);//箱号
     	date.put("RFLOT_PART",part);//零件
     	date.put("CQTY", zQty);//数量
     	date.put("QTY", QTY);//库存
     	date.put("RFLOT_SCAN", etx_ubpBar.getValStr());//标签
     	date.put("FBIN", etx_ubpFmBin.getValStr());//源储
     	date.put("RFLOT_LOT", Lot);//批次
     	date.put("RFPTV_LBS", rfptv_lbs);
     	date.put("STATUS", statue);
     	date.put("RFLOT_UM", txv_ubpUm.getValStr());
     	date.put("RFLOT_VEND", vend);
     	date.put("RFPTV_BIN_GROUP", rfptv_bin_group.toUpperCase());//零件存储类型
     	date.put("RFLOT_MULT_QTY", etx_ubpBox.getValStr());
     	date.put("RFLOT_BOX_QTY", txv_ubpUnit.getValStr());
     	date.put("RFLOT_SCATTER_QTY", etx_ubpScat.getValStr());
     	date.put("ref",ref);//参考
		listReturn.add(date);
		clearMsg();
		reverseList(listReturn);
		return date;
	}
	 //实现list集合逆序排列
	private void reverseList (List<Map<String,Object>> listReturn){
		Map<String, Object> map1=new HashMap<String, Object>();
		Map<String, Object> map2=new HashMap<String, Object>();
		 for (int i = 0; i < listReturn.size() - 1; i++)
		        for (int j = 0; j < listReturn.size() - 1 - i; j++){
		        if (Integer.parseInt(listReturn.get(j).get("RFLOT_NUM_LBL").toString()) < Integer.parseInt(listReturn.get(j + 1).get("RFLOT_NUM_LBL").toString())){
		        	map1 = listReturn.get(j);
		        	map2 = listReturn.get(j+1);
		        	listReturn.set(j, map2);
		        	listReturn.set(j+1, map1);
		        }
		   }
	}

	private boolean clearEmpty(){
		etx_ubpBar.setText("");
		etx_ubpFmBin.setText("");
		etx_ubpPart.setText("");
		txv_ubpUm.setText("");
		txv_ubpDesc.setText("");
		spn_upbLot.clearItems();
		txv_ubpUnit.setText("");
		etx_ubpScat.setText("");
		etx_ubpBox.setText("");
		txv_ubpQty.setText("");
		txv_ubpQtyp.setText("");
		if("L".equals(rfptv_lbs)){
			txv_upbBox.setText("");
		}
		return true;
	}
	private boolean clearEmptys(){
		etx_ubpFmBin.setText("");
		etx_ubpPart.setText("");
		txv_ubpUm.setText("");
		txv_ubpDesc.setText("");
		txv_upbBox.setText("");
		txv_upbCount.setText("");
		spn_upbLot.clearItems();
		txv_ubpUnit.setText("");
		etx_ubpScat.setText("");
		etx_ubpBox.setText("");
		txv_ubpQty.setText("");
		txv_ubpQtyp.setText("");
		return true;
	}
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}
	private void showTextMessage(String msg) {
		txvBaseMsg = txvBaseMsg == null ? (MyTextView)findViewById(R.id.txvBaseMsg) : txvBaseMsg;
		if(txvBaseMsg != null) txvBaseMsg.setText(msg);
	}
}
