
package com.profitles.activity;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;

import com.profitles.biz.UpBinBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class UpBinActivity extends AppFunActivity {
	
	private MyEditText etx_upbFmBin, etx_upbToLoc, etx_upbPart, 
					   etx_upbBox, etx_upbScat, etx_upbCap, etx_upbQc ;
	private MyTextView txv_upbUm, txv_upbDesc, txv_upbQoh, txv_upbUnit, txv_upbScat, txv_AdBin, txv_upbCap;
	private MySpinner spn_upbLot;//下拉框
	private MyReadBQ etx_upbBar,etx_upbToBin;
	private LinearLayout lnt_upbQc;
	private int nbtId, boxId, fmbin_id, toloc_id, tobin_id,Scat_id,qc_id, cap_id;
	//private float scat, box, boxQ, qty;
	private UpBinBiz upbiz;
	private boolean onPageLoad = true;
	private String Unit, domain, site, userID, vend, statue, lockey="", user, binCap, toLoc, fLoc, fBin,
				   rfc_iqc, adv_bin, lotMode, rfqc, fnceffdate,tray;
	
	/** 加载页面时初始化参数  */
	@Override
	protected void pageLoad() {
		upbiz = new UpBinBiz();
		nbtId = R.id.etx_upbBar;
		boxId = R.id.etx_upbBox;
		fmbin_id = R.id.etx_upbFmBin;
		toloc_id = R.id.etx_upbToLoc;
		tobin_id = R.id.etx_upbToBin;
		Scat_id = R.id.etx_upbScat;
		qc_id = R.id.etx_upbQc;
		cap_id = R.id.etx_upbCap;
		etx_upbBar = (MyReadBQ)findViewById(R.id.etx_upbBar);
		etx_upbPart = (MyEditText)findViewById(R.id.etx_upbPart);
		txv_upbQoh = (MyTextView)findViewById(R.id.txv_upbQoh);
		etx_upbBox = (MyEditText)findViewById(R.id.etx_upbBox);
		txv_upbUnit = (MyTextView)findViewById(R.id.txv_upbUnit);
		etx_upbScat = (MyEditText)findViewById(R.id.etx_upbScat);
		etx_upbToBin = (MyReadBQ)findViewById(R.id.etx_upbToBin);
		etx_upbCap = (MyEditText)findViewById(R.id.etx_upbCap);
		txv_upbCap = (MyTextView)findViewById(R.id.txv_upbCap);
		spn_upbLot = (MySpinner)findViewById(R.id.spn_upbLot);
		txv_upbDesc = (MyTextView)findViewById(R.id.txv_upbDesc);
		txv_upbUm = (MyTextView)findViewById(R.id.txv_upbUm);
		txv_upbScat = (MyTextView)findViewById(R.id.txv_upbScat);
		//txv_AdBin = (MyTextView)findViewById(R.id.txv_AdBin);
		lnt_upbQc = (LinearLayout)findViewById(R.id.lnt_upbQc);
		
		etx_upbFmBin = (MyEditText)findViewById(R.id.etx_upbFmBin);
		etx_upbFmBin.setText(ApplicationDB.LoclLoop.getString("RFLOCL_PASS", ""));
		etx_upbToLoc = (MyEditText)findViewById(R.id.etx_upbToLoc);
		etx_upbToLoc.setText(ApplicationDB.LoclLoop.getString("RFLOCL_WO", ""));
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		userID = ApplicationDB.user.getUserId();
		fnceffdate = ApplicationDB.user.getUserDate();
		binCap = ApplicationDB.Ctrl.getString("RFC_BIN_CAP", "");
		if ("1".equals(binCap)) {
			//etx_upbCap.setReadOnly(false);
		    //etx_upbScat.setText("0");
			etx_upbCap.setVisibility(View.VISIBLE);
			txv_upbCap.setVisibility(View.VISIBLE);
		}
		etx_upbToBin.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!StringUtil.isEmpty(etx_upbToBin.getValStr())){
						lockey = etx_upbToBin.getValStr();
						toLoc = etx_upbToLoc.getValStr();
						if (!"".equals(lockey)) 
						checkbin();
					}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});	
		
		spn_upbLot.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if (onPageLoad) {
					onPageLoad = false;
				} else {
					changQtyByLot(spn_upbLot.getValStr());
				}
				
			};
			public void onNothingSelected(AdapterView<?> parent){};
		});
		getFocues(etx_upbBar, true);
	}
	/** 栏位启动顺序 */
	/*@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView();
	}
*/
	/** 选择批次时修改库存*/
	private void changQtyByLot(final String lot) {
		
		loadDataBase(new IRunDataBaseListens() {
			
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return upbiz.getQtyByLoc(domain,site,etx_upbPart.getValStr(),lot,vend,fLoc,fBin);
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				if (wrp.isSuccess()) {
					txv_upbQoh.setText(wrp.getDataToMap().get("XSLD_QTY_OH")+"");
					statue = wrp.getDataToMap().get("XSLD_STATUS")+"";
				}
			}
		});
		//showShortMessage("fLoc="+fLoc+"~~~"+"fBin="+vend);
		
	}
	/** 得到Android页面的主布局  */
	@Override
	protected int getMainBodyLayout(){
		return R.layout.act_upbin;
	}
	/** 光标进入是激发的方法  */
	@Override
	protected boolean onFocus(int id, View v) {
		if (tobin_id == id) {
			if("B".equals(lotMode)&& !chectQTY()){
				getFocues(etx_upbBar, true);
				
			}
		}else if (boxId == id) {
			if ("0".equals(etx_upbBox.getValStr())) {
				etx_upbBox.setText("");
				
			}
		}else if (Scat_id == id) {
			if ("0".equals(etx_upbScat.getValStr())) {
				etx_upbScat.setText("");
			}
			runClickFun();
		}	
		return true;
	}
	/** 栏位启动顺序 */
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		// _vff.addItemView(etx_upbFmBin, etx_upbToLoc, etx_upbBar);
	}
	/** 光标离开时激发的方法  */
	@Override
	protected boolean onBlur(int id, View v) {
		if(nbtId == id){
			String upbBar = etx_upbBar.getValStr();
			if ("" .equals(upbBar) ) {
				return false;
			}
			checkBar();
		}else if (fmbin_id == id) {
			lockey = etx_upbFmBin.getValStr();
			checkloc("FmBin");
		}else if (toloc_id == id) {
				lockey = etx_upbToLoc.getValStr();
				checkloc("ToLoc");
		}else if (boxId == id) {
			if ("".equals(etx_upbBox.getValStr())) {
				etx_upbBox.setText("0");
			}
			return chectQTY();
		}else if (Scat_id == id) {
			if ("".equals(etx_upbScat.getValStr())) {
				etx_upbScat.setText("0");
			}
			return chectQTY();
		}else if (qc_id == id) {
			if("".equals(etx_upbQc.getValStr()))
				return true;
			checkUser();
		}/*else if (tobin_id == id) {
				lockey = etx_upbToBin.getValStr();
				toLoc = etx_upbToLoc.getValStr();
				if (!"".equals(lockey)) 
				checkbin();
		}*/else if (cap_id == id) {
			if (!"".equals(lockey)); 
			//checkbin();
	}
		return true;
	}
	/** 向页面添加按钮  */
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Save };
	}
	/** 保存按钮被点击时激发的方法 Begin  */
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		return checkNonEmpty();
	}
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		if(ButtonType.Save.equals(btnType)){
			return upBinSave();
		}
		return null;
	}
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wrp =  (WebResponse)data;
		if (data != null && wrp.isSuccess()) {
			etx_upbFmBin.setText(ApplicationDB.LoclLoop.getString("RFLOCL_PASS", ""));
			etx_upbToLoc.setText(ApplicationDB.LoclLoop.getString("RFLOCL_WO", ""));
			etx_upbBar.setText("");
			etx_upbPart.setText("");
			txv_upbDesc.setText("");
			txv_upbUm.setText("");
			txv_upbUnit.setText("");
			txv_upbQoh.setText("");
			spn_upbLot.clearItems();
			txv_upbScat.setText("");
			etx_upbBox.setText("");
			etx_upbScat.setText("");
			etx_upbToBin.setText("");
			etx_upbCap.setText("");
			//txv_AdBin.setText("");
			if ("1".equals(rfc_iqc)) {
				etx_upbQc.setText("");
				lnt_upbQc.setVisibility(View.GONE);
			}
			getFocues(etx_upbFmBin, true);
			showSuccessMsg("保存成功！");
		}else{
			showErrorMsg(wrp.getMessages());
		}
	}
	/** 保存按钮被点击时激发的方法  end  */
	
	/** 库位改变时触发  */
	protected void checkloc( final String locBin) {
		actPause();
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return upbiz.checkloc(lockey,domain,site);
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				if (!wrp.isSuccess()) {
					if("FmBin".equals(locBin)){
						showErrorMsg(lockey+"不存在！");
						getFocues(etx_upbFmBin, true);
					}
					if("ToLoc".equals(locBin)){
						showErrorMsg(lockey+"不存在！");
						getFocues(etx_upbToLoc, true);
					}
				}else{
					clearMsg();
					actGoon();
				}
			}
		});
	}
	/** 扫码时触发方法  */
	protected void checkBar() {
		actPause();
		loadDataBase(new IRunDataBaseListens() {
			
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return upbiz.anlBarcode(etx_upbBar.getValStr(),domain,site);
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				if (wrp.isSuccess()) {
					checkCondition(data);
					actGoon();
				}else{
					actUnPause();
					showErrorMsg(wrp.getMessages());
				}
			}
		});
	}
	
	//检查批控类型以及对应栏位赋值
	protected void checkCondition(Object data) {
		Map<String, Object> map = new HashMap<String, Object>();
		try { 
			map = ((WebResponse)data).getDataToMap();
			tray = StringUtil.isEmpty(map.get("RFLOT_TRAY"))?"":map.get("RFLOT_TRAY")+"";
			String[] lot = getArrayFromMap(map,"lot",";");
			lotMode = map.get("RFPTV_LBS")+"";
			rfc_iqc = map.get("rfc_iqc")+"";
			if ("1".equals(rfc_iqc)) {		//必须合格检验
				lnt_upbQc.setVisibility(View.VISIBLE);
				etx_upbQc = (MyEditText)findViewById(R.id.etx_upbQc);
			}
			if ("B".equals(lotMode) || "S".equals(lotMode)) {		//按箱管理
				if(StringUtil.parseInt(map.get("RFLOT_SCATTER_QTY"))>0){
					etx_upbBox.setText("0");
					etx_upbScat.setText(map.get("RFLOT_SCATTER_QTY")+"");
					txv_upbScat.setText(map.get("RFLOT_SCATTER_QTY").toString());
				}else{
				 etx_upbBox.setText("1");
				 etx_upbScat.setText("0");
				 txv_upbScat.setText(map.get("RFLOT_MULT_QTY").toString());
				}
				etx_upbBox.setReadOnly(true);
				
				//getFocues(etx_upbBox, false);
				
				etx_upbScat.setReadOnly(true);
				//getFocues(etx_upbScat, false);
				
				
				
			}else {
				etx_upbBox.setText("0");
			}
			
//			String rflotType = StringUtil.isEmpty(map.get("RFLOT_TYPE"))?"":map.get("RFLOT_TYPE")+"";
			
			vend = map.get("RFLOT_VEND")+"";
			fLoc = map.get("fLoc")+"";
			fBin = map.get("fBin")+"";
			statue = map.get("status")+"";
			Unit = map.get("RFLOT_MULT_QTY").toString();
			etx_upbFmBin.setText(map.get("FLOC")+"");
			etx_upbToLoc.setText(map.get("TLOC")+"");
			//etx_upbBar.setText(map.get("RFLOT_SCAN")+"");
			etx_upbPart.setText(map.get("RFLOT_PART")+"");
			etx_upbPart.setReadOnly(true);
			//获取文本
			MyTextView desc=txv_upbDesc;
			//设置EditText的显示方式为多行文本输入  
			desc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
			//文本显示的位置在EditText的最上方  
			desc.setGravity(Gravity.TOP);  
			desc.setText(map.get("RFLOT_PART_DESC").toString());  
			//改变默认的单行模式  
			desc.setSingleLine(false);  
			txv_upbUm.setText(map.get("RFLOT_UM")+"");
			txv_upbUnit.setText(map.get("RFLOT_MULT_QTY")+"");
			txv_upbQoh.setText(map.get("qty")+"");
			spn_upbLot.addAndClearItems(lot);
			adv_bin = map.get("ADVICE_BIN")+"";
			rfqc = map.get("rfqc")+"";
			//etx_upbScat.setText("0");
			if(StringUtil.parseInt(map.get("RFLOT_SCATTER_QTY"))>0){
				etx_upbScat.setText(map.get("RFLOT_SCATTER_QTY")+"");
			}else{
				etx_upbScat.setText("0");
			}
			if ("".equals(txv_upbScat.getValStr())) {
				if(StringUtil.parseInt(map.get("RFLOT_SCATTER_QTY"))>0){
					txv_upbScat.setText(map.get("RFLOT_SCATTER_QTY")+"");
				}else{
					txv_upbScat.setText(map.get("RFLOT_SCATTER_QTY")+"");
				}
				
			}
			if (lot.length > 1) 
				showSuccessMsg("建议储位："+adv_bin+"存在多个批次，建议最小批次："+lot[0]);
			else
				showSuccessMsg("建议储位："+adv_bin);
			//txv_AdBin.setText(map.get("ADVICE_BIN")+"");
		} catch (Exception e) {
			//getFocues(etx_upbBar, true);
			//etx_upbBar.setFocusable(true);
			showErrorMsg("条码数据异常，请检查基础数据是否完整！");
		}
		
	}
	//检查 箱数*每箱+散量 是否超过库存量 超过则返回true
	private boolean chectQTY() {
		Float qty, qoh, boxN, boxQ, scat;
		if ("".equals(etx_upbBox.getValStr())) 
			etx_upbBox.setText("0");
		
		boxN = Float.valueOf(etx_upbBox.getValStr()).floatValue();
		boxQ = Float.valueOf(Unit).floatValue();
		scat = Float.valueOf(etx_upbScat.getValStr()).floatValue();
		qty = Float.valueOf(txv_upbQoh.getValStr()).floatValue();
		qoh = boxN*boxQ+scat;
		
		if (java.lang.Math.abs(scat)>=java.lang.Math.abs(boxQ)) {
			showErrorMsg("散量不能超过每箱量！");
			return false;
		}
		/*else if(qty<java.lang.Math.abs(qoh)){
			showErrorMsg("库存不足！");
			return false;
		}*/
		txv_upbScat.setText(qoh+"");
		showSuccessMsg("建议储位："+adv_bin);
		return true;
	}

	/** 把String类型的'批次'换成数组  */
	private String[] getArrayFromMap(Map<String, Object> map,String key,String splTag){
		String str = map.get(key).toString();
		if (null != str && !"".equals(str)) {
			return str.split(splTag);
		}
		return null;
	}
	/*
	private void changeUpbQoh() {
		Float qoh =  getUpbQoh();
		txv_upbScat.setText(qoh+"");
	}
	
	private Float getUpbQoh() {
		Float boxN, boxQ, scat;
		boxN = Float.valueOf(etx_upbBox.getValStr()).floatValue();
		boxQ = Float.valueOf(Unit).floatValue();
		scat = Float.valueOf(etx_upbScat.getValStr()).floatValue();
		return boxN*boxQ+scat;
	}*/
	/**验证 检验栏位内容的准确性*/
	private void checkUser() {
		String qc = etx_upbQc.getValStr();
		try {
			if (!"".equals(qc)||null != qc) {
				String[] qcAry = qc.split("\\|");
				user = qcAry[0];
				SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
				format.setLenient(false);
				format.parse(qcAry[1]);
				loadDataBase(new IRunDataBaseListens() {
					
					@Override
					public boolean onValidata() {
						return true;
					}
					
					@Override
					public Object onGetData() {
						return upbiz.checkUser(user);
					}
					
					@Override
					public void onCallBrack(Object data) {
						WebResponse wrp = (WebResponse)data;
						if (!wrp.isSuccess()) {
							showErrorMsg("员工不存在！");
							getFocues(etx_upbQc, true);
						}else {
							showSuccessMsg("建议储位："+adv_bin);
						}
					}
				});
			}
		} catch (Exception e) {
			showErrorMsg("检验码不正确");
			getFocues(etx_upbQc, true);
		}
		

	}
	/**点击保存按钮链接数据库进行保存*/
	private WebResponse upBinSave() {
				
				WebResponse wrs = upbiz.upBinSave(domain, site, etx_upbFmBin.getValStr(), etx_upbToLoc.getValStr(), spn_upbLot.getValStr(), etx_upbPart.getValStr(),
						etx_upbToBin.getValStr(), vend,etx_upbBar.getValStr(), txv_upbScat.getValStr(), statue,txv_upbUm.getValStr(), userID, txv_upbUnit.getValStr(), txv_upbDesc.getValStr(),fnceffdate,tray);
				return wrs;
	}
	/**至储验证单件单批*/
	private void checkbin() {
		
		loadDataBase(new IRunDataBaseListens() {
			
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return upbiz.checkbin(domain, site, etx_upbToLoc.getValStr(), etx_upbToBin.getValStr(), etx_upbPart.getValStr(), vend, spn_upbLot.getValStr(), txv_upbScat.getValStr());

			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				String rMsg = wrp.getMessages();
				if (!wrp.isSuccess()) {
					showSuccessMsg(rMsg+"建议储位："+adv_bin);
					etx_upbToBin.setText("");
					getFocues(etx_upbToBin, true);
//					CapCalculation();
				}else{ 
					String[] locCap = rMsg.split("\\|");
					//toLoc = locCap[0];
					if(!toLoc.equals(locCap[0])){
						showErrorMsg("至仓下没有这个至储，至仓已被修改！"+"建议储位："+adv_bin);
						etx_upbToLoc.setText(locCap[0]);
					}
					if(locCap.length <= 1){
						getFocues(etx_upbScat, true);
					}else{
						etx_upbCap.setText(locCap[1]);
						getFocues(etx_upbScat, true);
					}
					CapCalculation();
				} 
			}
		});
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
				Float qty = StringUtil.parseFloat(etx_upbBox.getValStr())*StringUtil.parseFloat(txv_upbUnit.getValStr())+StringUtil.parseFloat(etx_upbScat.getValStr());
				return upbiz.CapCalculation(etx_upbPart.getValStr(),domain, site, etx_upbToBin.getValStr(),qty+"",txv_upbQoh.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrp = (WebResponse)data;
				Map<String, Object> map = new HashMap<String, Object>();
				map = ((WebResponse)data).getDataToMap();
				if (wrp.isSuccess()) {
					etx_upbCap.setText(map.get("CAP")+"");
				}else{
					showErrorMsg(wrp.getMessages());
				}
			}
		});
	}
	
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
		
	}
	private boolean checkNonEmpty(){
		
		if ("1".equals(binCap)&&"Y".equals(rfqc)) {
			if (!"GOOD".equals(statue)) {
				showErrorMsg("合格状态才能上架!");
				return false;
			}
		} else if("".equals(etx_upbFmBin.getValStr())){
			showErrorMsg("源储不能为空！");
			return false;
		}else if ("".equals(etx_upbBar.getValStr())) {
			showErrorMsg("扫码不能为空！");
			return false;
		}else if ("".equals(etx_upbBox.getValStr())) {
			showErrorMsg("箱数不能为空");
			return false;
		}else if ("1".equals(rfc_iqc) && "".equals(etx_upbQc.getValStr())) {
			showErrorMsg("检验不能为空！");
			return false;
		}else if ("".equals(etx_upbToBin.getValStr())) {
			showErrorMsg("至储不能为空！");
			return false;
		}else if ("0.0".equals(txv_upbQoh.getValStr())) {
			showErrorMsg("上架数量不能为0！");
			return false;
		}
		return true;
	}
	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}
}
