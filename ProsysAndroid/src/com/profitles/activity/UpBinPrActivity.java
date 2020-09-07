
package com.profitles.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;






import org.json.JSONArray;
import org.json.JSONObject;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.UpBinPrBiz;
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
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class UpBinPrActivity extends AppFunActivity {
	 
	/**初始化数据*/
	private UpBinPrBiz upPrbiz;
	private MyEditText   etx_ubpCap ,etx_ubpScat , etx_ubpBox ;
	private MyTextView  etx_ubpPart ,txv_ubpQoh , txv_ubpUnit   , txv_ubpDesc , txv_ubpUm,txv_ubpQty;
	private MySpinner spn_ubpLot;//下拉框
	private MyReadBQ etx_ubpBar , etx_ubpFmBin , etx_ubpToLoc , etx_ubpToBin;
	private LinearLayout lnt_upbQc;
	private MyDataGrid dateGrid;
	private int nbtId;
	private int k	 = 0 ;
	private float zQty;
	private String Unit, domain, site, userID, vend, statue, lockey="", user, binCap, toLoc, fLoc, fBin,
	rfc_iqc, adv_bin, lotMode, rfqc, fnceffdate,tray ,locbin = "" , tlocbin = "" , num_lbl = "";
	private List<Map<String,Object>> listReturn = new ArrayList<Map<String,Object>>();
	private  Boolean isTrue = false ,isSearch = false ,isLoc = false;
	private String jsonStr="";
	/** 得到Android页面的主布局  */
	@Override
	protected int getMainBodyLayout(){
		return R.layout.act_upbinpr;
	}
	/** 加载页面时初始化参数  */
	@Override
	protected void pageLoad() {
		upPrbiz = new UpBinPrBiz();
		
		
		 etx_ubpBar =(MyReadBQ) findViewById(R.id.etx_ubpBar); // 条码
		 etx_ubpFmBin =(MyReadBQ) findViewById(R.id.etx_ubpFmBin); // 源储
		 etx_ubpToLoc =(MyReadBQ) findViewById(R.id.etx_ubpToLoc); // 至仓
		 etx_ubpToBin =(MyReadBQ) findViewById(R.id.etx_ubpToBin); // 至储
		 
		 etx_ubpPart = (MyTextView)findViewById(R.id.etx_ubpPart); // 零件
		 txv_ubpDesc = (MyTextView)findViewById(R.id.txv_ubpDesc); // 零件描述
		 etx_ubpCap = (MyEditText)findViewById(R.id.etx_ubpCap); // 负荷
		 spn_ubpLot = (MySpinner)findViewById(R.id.spn_ubpLot); // 批次 
		 
		 txv_ubpUm = (MyTextView)findViewById(R.id.txv_ubpUm); // 单位
		 txv_ubpQoh = (MyTextView)findViewById(R.id.txv_ubpQoh); // 库存
		 etx_ubpBox = (MyEditText)findViewById(R.id.etx_ubpBox); // 箱数
		 txv_ubpUnit = (MyTextView)findViewById(R.id.txv_ubpUnit); // 每箱量
		 etx_ubpScat = (MyEditText)findViewById(R.id.etx_ubpScat); // 散量
		 txv_ubpQty = (MyTextView)findViewById(R.id.txv_ubpQty); // 数量
		 dateGrid = (MyDataGrid)findViewById(R.id.mdtg_ubplist);
		 domain = ApplicationDB.user.getUserDmain();
		 site = ApplicationDB.user.getUserSite();

		 etx_ubpBar.addTextChangedListener( new TextWatcher(){ // 条码扫码事件
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if( etx_ubpBar.getValStr() != null && !"".equals(etx_ubpBar.getValStr().trim())){
					checkBar();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
			 
		 } );
		 
		 
		 spn_ubpLot.setOnItemSelectedListener(new OnItemSelectedListener() { // 批次
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
					
				};
				public void onNothingSelected(AdapterView<?> parent){
					
				};
			});
		 
		 
		 etx_ubpToBin.addTextChangedListener(new TextWatcher() { // 至储
				public void onTextChanged(CharSequence s, int start, int before, int count) {
								if(!StringUtil.isEmpty(etx_ubpToBin.getValStr())){
									checkbin();
								}
				}
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				public void afterTextChanged(Editable s) {
				}
			});
		 
		/* etx_ubpToLoc.addTextChangedListener(new TextWatcher() { // 至仓
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
				public void afterTextChanged(Editable s) {
				}
			});
		 
		 etx_ubpFmBin.addTextChangedListener(new TextWatcher() { // 源储
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
				public void afterTextChanged(Editable s) {
				}
			});*/
		 
	}
	
	/** 光标离开时激发的方法  
	 * 
	 * 
	 * 
	 * 
	 * */
	@Override
	protected boolean onBlur(int id, View v) {
		 if (R.id.etx_ubpFmBin == id) { 
			 if(!StringUtil.isEmpty(etx_ubpFmBin.getValStr())){
					lockey = etx_ubpFmBin.getValStr();
					isTrue = true;
					checkloc(lockey);
					getFocues(etx_ubpToLoc, true);
				}
		}
		 if(R.id.etx_ubpToLoc == id){
			 if(!StringUtil.isEmpty(etx_ubpToLoc.getValStr())){
					lockey = etx_ubpToLoc.getValStr();
					isLoc = true;
					checkloc(lockey);
					getFocues(etx_ubpToBin, true);
				}
		}
		 /**
		  * TODO
		  */
		 if(R.id.etx_ubpToBin == id){
			  if(listReturn.size() > 0 && !StringUtil.isEmpty(etx_ubpBar.getValStr()) && !StringUtil.isEmpty(etx_ubpToBin.getValStr())){
					saveInfo();
				}else
			 if(StringUtil.isEmpty(etx_ubpToBin.getValStr()) && checkNonEmpty()){
					 saveInfo();
				}
			  if(!StringUtil.isEmpty(etx_ubpToBin.getValStr()) && checkNonEmpty()){
				  saveInfo();
			  }
			 getFocues(etx_ubpBar, true);
		 }
		return true;
	}
	
	/**
	 * @author wade
	 * 扫描条码操作
	 * */
	
	protected void checkBar() {
		loadDataBase(new IRunDataBaseListens() {
			
			@Override
			public boolean onValidata() {
				if(listReturn != null || listReturn.size() >= 1){
					for ( int i = 0; i < listReturn.size(); i++) {
					String SCAN =  listReturn.get(i).get("RFLOT_SCAN")+"";
						if(SCAN.equals(etx_ubpBar.getValStr())){
					     	k = i;
							showConfirm(SCAN+"条码已存在扫描记录,是否删除?", isDelInfo);
							return false;
						}
					}
					
				}
					return true;
				
			}
			@Override
			public Object onGetData() {
				return upPrbiz.anlBarcodePr(domain, site, etx_ubpBar.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				try {
				if (wrp.isSuccess()) {
					Map mpRturn = wrp.getDataToMap();
					
					tray = StringUtil.isEmpty(mpRturn.get("RFLOT_TRAY"))?"":mpRturn.get("RFLOT_TRAY")+"";
					String[] lot = getArrayFromMap(mpRturn,"lot",";");
					lotMode = mpRturn.get("RFPTV_LBS")+"";
					rfc_iqc = mpRturn.get("rfc_iqc")+"";
					if ("1".equals(rfc_iqc)) {		//必须合格检验
						lnt_upbQc.setVisibility(View.VISIBLE);
//						txv_ubpQty = (MyEditText)findViewById(R.id.etx_upbQc);
					}
					if ("B".equals(lotMode) || "S".equals(lotMode)) {		//按箱管理
						if(StringUtil.parseInt(mpRturn.get("RFLOT_SCATTER_QTY"))>0){
							etx_ubpBox.setText("0");
							etx_ubpScat.setText(mpRturn.get("RFLOT_SCATTER_QTY")+"");
							etx_ubpScat.setText(mpRturn.get("RFLOT_SCATTER_QTY").toString());
						}else{
							etx_ubpBox.setText("1");
							etx_ubpScat.setText("0");
							etx_ubpScat.setText(mpRturn.get("RFLOT_MULT_QTY").toString());
						}
						etx_ubpBox.setReadOnly(true);
						etx_ubpScat.setReadOnly(true);
					}else {
						etx_ubpBox.setText("0");
						etx_ubpBox.setReadOnly(false);
						//需要打开此项开关才可以获取焦点
						etx_ubpBox.setFocusableInTouchMode(true);
						etx_ubpScat.setReadOnly(false);
						//需要打开此项开关才可以获取焦点
						etx_ubpScat.setFocusableInTouchMode(true);
						getFocues(etx_ubpBar, true);
					}
					vend = mpRturn.get("RFLOT_VEND")+"";
					fLoc = mpRturn.get("RFLOT_LOC")+"";
					fBin = mpRturn.get("RFLOT_BIN")+"";
					locbin = StringUtil.isEmpty(fBin,fLoc);
					statue = mpRturn.get("status")+"";
					Unit = mpRturn.get("RFLOT_MULT_QTY").toString();
					num_lbl = mpRturn.get("RFLOT_NUM_LBL")+"";
					etx_ubpFmBin.setText(StringUtil.isEmpty(StringUtil.isEmpty(fBin,fLoc),""));
					etx_ubpToLoc.setText(mpRturn.get("TLOC")+"");
					tlocbin = mpRturn.get("TLOC")+"";
					etx_ubpPart.setText(mpRturn.get("RFLOT_PART")+"");
					//获取文本
					MyTextView desc=txv_ubpDesc;
					//设置EditText的显示方式为多行文本输入  
					desc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
					//文本显示的位置在EditText的最上方  
					desc.setGravity(Gravity.TOP);  
					desc.setText(mpRturn.get("RFLOT_PART_DESC").toString());  
					//改变默认的单行模式   txv_ubpQty
					desc.setSingleLine(false);  
					txv_ubpUm.setText(mpRturn.get("RFLOT_UM")+"");
					txv_ubpUnit.setText(mpRturn.get("RFLOT_BOX_QTY")+"");
					txv_ubpQoh.setText(mpRturn.get("qty")+"");
					vend = mpRturn.get("RFLOT_VEND")+"";
					toLoc = etx_ubpToLoc.getValStr();
					spn_ubpLot.addAndClearItems(lot);
					adv_bin = mpRturn.get("ADVICE_BIN")+"";
					rfqc = mpRturn.get("rfqc")+"";
					//etx_upbScat.setText("0");
					if(StringUtil.parseInt(mpRturn.get("RFLOT_SCATTER_QTY"))>0){
						etx_ubpScat.setText(mpRturn.get("RFLOT_SCATTER_QTY")+"");
					}else{
						etx_ubpScat.setText("0");
					}
					if ("".equals(etx_ubpScat.getValStr())) {
						if(StringUtil.parseInt(mpRturn.get("RFLOT_SCATTER_QTY"))>0){
							etx_ubpScat.setText(mpRturn.get("RFLOT_SCATTER_QTY")+"");
						}else{
							etx_ubpScat.setText(mpRturn.get("RFLOT_SCATTER_QTY")+"");
						}
					}
					 zQty =	 StringUtil.parseFloat(txv_ubpUnit.getValStr())*StringUtil.parseFloat(etx_ubpBox.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
					txv_ubpQty.setText(zQty+"");
					if (lot.length > 1) 
						showSuccessMsg("建议储位："+adv_bin+"存在多个批次，建议最小批次："+lot[0]);
					else
						showSuccessMsg("建议储位："+adv_bin);
					isSearch = true;
					actGoon();
					if(StringUtil.isEmpty(etx_ubpFmBin.getValStr())){
						getFocues(etx_ubpFmBin, true);
					}else if(StringUtil.isEmpty(etx_ubpToLoc.getValStr())){
						getFocues(etx_ubpToLoc, true);
					}else{
						getFocues(etx_ubpToBin, true);
					}
					
				}else{
					etx_ubpBar.setText("");
					getFocues(etx_ubpBar, true);
					showErrorMsg(wrp.getMessages());
				}
				} catch (Exception e) {
					etx_ubpBar.setText("");
					getFocues(etx_ubpBar, true);
					showErrorMsg("没有相关数据,检查条码信息");
				}
			}
		});
	}
	
	/** 库位改变时触发  */
	protected void checkloc( final String lockey) {
		actPause();
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return upPrbiz.checkloc(lockey,domain,site);
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				Map<String, Object> binMp = wrp.getDataToMap();
				if (!wrp.isSuccess()) {
					showErrorMsg("扫描的仓储"+lockey+"不存在");
					if(isTrue){
						etx_ubpFmBin.setText(locbin);
						isTrue = false;
					}
					if(isLoc){
						etx_ubpToLoc.setText(tlocbin);
						isLoc = false;
					}
				}else{
					locbin = StringUtil.isEmpty(binMp.get("BIN")+"", binMp.get("LOC")+"");
					clearMsg();
					actGoon();
				}
			}
		});
	}
	
	/**储位为空时，进行界面保存数据*/
	private void saveInfo(){
	Map<String, Object> mpInfo = new HashMap<String, Object>();
     	mpInfo.put("RFLOT_NUM_LBL", num_lbl);
     	if(locbin==""){
     		mpInfo.put("fBin", etx_ubpFmBin.getValStr());
     	}else{
     		mpInfo.put("fBin", locbin);
     	}
     	mpInfo.put("TLOC", tlocbin);
     	mpInfo.put("RFLOT_PART", etx_ubpPart.getValStr());
     	mpInfo.put("RFLOT_LOT", spn_ubpLot.getValStr());
     	mpInfo.put("CQTY", zQty);
     	mpInfo.put("RFLOT_SCAN", etx_ubpBar.getValStr());
     	mpInfo.put("RFLOT_UM", txv_ubpUm.getValStr()); 
     	mpInfo.put("RFLOT_VEND", vend);
		listReturn.add(mpInfo);
		dateGrid.buildData(listReturn);
		clearEmpty();
	}
	
	/**确认框事件*/
	private OnShowConfirmListen isDelInfo=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			listReturn.remove(k);
			if(listReturn == null || listReturn.size() == 0){
				dateGrid.clearData();
			}else{
				dateGrid.buildData(listReturn);
			}
			etx_ubpBar.setText("");
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			etx_ubpBar.setText("");
			getFocues(etx_ubpBar, true);
		}
	};
	
	//检查 箱数*每箱+散量 是否超过库存量 超过则返回true
		private boolean chectQTY() {
			Float qty, qoh, boxN, boxQ, scat;
			if ("".equals(etx_ubpBox.getValStr())) 
				etx_ubpBox.setText("0");
			
			boxN = Float.valueOf(etx_ubpBox.getValStr()).floatValue();
			boxQ = Float.valueOf(Unit).floatValue();
			scat = Float.valueOf(etx_ubpScat.getValStr()).floatValue();
			qty = Float.valueOf(txv_ubpQoh.getValStr()).floatValue();
			qoh = boxN*boxQ+scat;
			
			if (java.lang.Math.abs(scat)>=java.lang.Math.abs(boxQ)) {
				showErrorMsg("散量不能超过每箱量！");
				return false;
			}
			/*else if(qty<java.lang.Math.abs(qoh)){
				showErrorMsg("库存不足！");
				return false;
			}*/
			etx_ubpScat.setText(qoh+"");
			showSuccessMsg("建议储位："+adv_bin);
			return true;
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
				return upPrbiz.getQtyByLoc(domain,site,etx_ubpPart.getValStr(),lot,vend,fLoc,fBin);
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				if (wrp.isSuccess()) {
					txv_ubpQoh.setText(wrp.getDataToMap().get("XSLD_QTY_OH")+"");
					statue = wrp.getDataToMap().get("XSLD_STATUS")+"";
				}
			}
		});
		//showShortMessage("fLoc="+fLoc+"~~~"+"fBin="+vend);
		
	}
	
	/** 把String类型的'批次'换成数组  */
	private String[] getArrayFromMap(Map<String, Object> map,String key,String splTag){
		String str = map.get(key).toString();
		if (null != str && !"".equals(str)) {
			return str.split(splTag);
		}
		return null;
	}
	
	
	//负荷率
	private void CapCalculation(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				Float qty = StringUtil.parseFloat(etx_ubpBox.getValStr())*StringUtil.parseFloat(txv_ubpUnit.getValStr())+StringUtil.parseFloat(etx_ubpScat.getValStr());
				return upPrbiz.CapCalculation(etx_ubpPart.getValStr(),domain, site, etx_ubpToBin.getValStr(),qty+"",txv_ubpQoh.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				Map<String, Object> map = new HashMap<String, Object>();
				map = ((WebResponse)data).getDataToMap();
				if (wrp.isSuccess()) {
					etx_ubpCap.setText(map.get("CAP")+"");
				}else{
					showErrorMsg(wrp.getMessages());
				}
			}
		});
	}
	
	
	/**至储验证单件单批*/
	private void checkbin() {
		
		loadDataBase(new IRunDataBaseListens() {
			
			@Override
			public boolean onValidata() {
				adv_bin="";
				return true;
			}
			
			@Override
			public Object onGetData() {
				return upPrbiz.checkbin(domain, site, etx_ubpToLoc.getValStr(), etx_ubpToBin.getValStr(), etx_ubpPart.getValStr(), vend, spn_ubpLot.getValStr(), etx_ubpScat.getValStr());

			}
			/**
			 * TODO
			 */
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				String rMsg = wrp.getMessages();
				if (!wrp.isSuccess()) {
					getFocues(etx_ubpToBin, true);
					CapCalculation();
					clearMsg();
				}else{ 
					String[] locCap = rMsg.split("\\|");
				    String	toLoc2 = locCap[0];
					if(!toLoc.equals(locCap[0])){
						if(adv_bin==null||adv_bin==""){
							showErrorMsg("至仓下没有这个至储，至仓已被修改！");
						}else{
							showErrorMsg("至仓下没有这个至储，至仓已被修改！"+"建议储位："+adv_bin);
						}
						etx_ubpToLoc.setText(locCap[0]);
						tlocbin = locCap[0];
						getFocues(etx_ubpToBin, true);
					}
					if(locCap.length <= 1){
						getFocues(etx_ubpToBin, true);
					}else{
						etx_ubpCap.setText(locCap[1]);
						tlocbin = locCap[1];
						getFocues(etx_ubpToBin, true);
					}
					CapCalculation();
				} 
			}
		});
	}
	
	/** 向页面添加按钮  */
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Save };
	}
	/** 保存按钮被点击时激发的方法 Begin  TODO*/
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(listReturn.size() == 0 && !StringUtil.isEmpty(etx_ubpToBin.getValStr())){
			Map<String,Object> mpInfo = new HashMap<String, Object>();
			mpInfo.put("RFLOT_NUM_LBL", num_lbl);
	     	mpInfo.put("fBin", locbin);
	     	mpInfo.put("TLOC", tlocbin);
	     	mpInfo.put("RFLOT_PART",etx_ubpPart.getValStr());
	     	mpInfo.put("RFLOT_LOT", spn_ubpLot.getValStr());
	     	mpInfo.put("CQTY", zQty);
	     	mpInfo.put("RFLOT_SCAN", etx_ubpBar.getValStr());
	     	mpInfo.put("RFLOT_UM", txv_ubpUm.getValStr()); 
	     	mpInfo.put("RFLOT_VEND", vend);
			listReturn.add(mpInfo);
			return true;
		}else if(listReturn.size() > 0 && !StringUtil.isEmpty(etx_ubpBar.getValStr()) ){
			showErrorMsg("扫描的数据存储到列表之后进行操作");
			return false;
		}
		else
		if(listReturn == null || listReturn.size() == 0){
			showErrorMsg("列表中没有数据,无法进行上架操作");
			return false;
		}else
		if(StringUtil.isEmpty(etx_ubpToBin.getValStr())){
			showErrorMsg("请扫描至储之后进行保存操作");
			return false;
		}
		return true;
	}
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		List<String> cache = new ArrayList<String>();
		  for (int i = 0; i < listReturn.size(); i++) {
				JSONObject obj = new JSONObject(listReturn.get(i)); 
				cache.add(obj.toString());  
				Map<String, Object> map=listReturn.get(i);
			}
			jsonStr = cache.toString(); 
		return upPrbiz.upBinPrSave(domain, site, jsonStr, ApplicationDB.user.getUserId(), etx_ubpToBin.getValStr(), statue, ApplicationDB.user.getUserDate().toString());
	}
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			showSuccessMsg(wr.getMessages());
			listReturn.clear();
			dateGrid.clearData();
			clearEmpty();
			clearMsg();
			getFocues(etx_ubpBar, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_ubpBar, true);
		}
	}
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
		
	}
	private boolean checkNonEmpty(){
		if ("".equals(etx_ubpBar.getValStr())) {
			return false;
		}else{
			if ("1".equals(binCap)&&"Y".equals(rfqc)) {
				if (!"GOOD".equals(statue)) {
					showErrorMsg("合格状态才能上架!");
					return false;
				}
			} else if("".equals(etx_ubpFmBin.getValStr())){
				showErrorMsg("源储不能为空！");
				return false;
			}else if ("".equals(etx_ubpBar.getValStr())) {
				showErrorMsg("扫码不能为空！");
				return false;
			}else if ("".equals(etx_ubpBox.getValStr())) {
				showErrorMsg("箱数不能为空");
				return false;
			}else if ("0.0".equals(txv_ubpQty.getValStr())) {
				showErrorMsg("上架数量不能为0！");
				return false;
			}
			return true;
		}
	}
	private boolean clearEmpty(){
		etx_ubpBar.setText("");
		etx_ubpFmBin.setText("");
		etx_ubpToLoc.setText("");
		
		etx_ubpToBin.setText("");
		etx_ubpPart.setText("");
		txv_ubpDesc.setText("");
		
		etx_ubpCap.setText("");
		txv_ubpUm.setText("");
		txv_ubpUnit.setText("");
		etx_ubpScat.setText("");
		spn_ubpLot.clearItems();
		txv_ubpQoh.setText("");
		etx_ubpBox.setText("");
		txv_ubpQty.setText("");
		return true;
	}
	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}
}
