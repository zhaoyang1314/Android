package com.profitles.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.fpsBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class FpsActivity extends AppFunActivity {

	private MyReadBQ actBar,actNbr;
	private MyEditText actPart,actUm,actDesc,actLot,actQty,actLocBin,actUnit,actDate,actShift,actBox,actScat;
	private fpsBiz fpsBiz;
	private MyDataGrid dtg;
	private ApplicationDB applicationDB;
	private String domain, site,userid,locbin,vend,tiaoma = "",lbs = "",cons_nbr = "",fnceffdate,rfc_is_nbr_fps,amount="";
	private boolean isflgn = true;
	private List<Map<String,Object>> nbrlist= new ArrayList<Map<String,Object>>();

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_fps;
	}

	@Override
	protected void pageLoad() {
		fpsBiz = new fpsBiz();
		actNbr		= (MyReadBQ)   findViewById(R.id.etx_fpsNbr);
		actBar		= (MyReadBQ)   findViewById(R.id.etx_fpsBar);
		actBar.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				clearMsg();
				if(!actBar.getValStr().equals("")){
					BlurBar();
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actLocBin	= (MyEditText) findViewById(R.id.etx_fpsLocBin);
		actPart     = (MyEditText) findViewById(R.id.etx_fpsPart);
		actDesc     = (MyEditText) findViewById(R.id.etx_fpsDesc);
		actUm      	= (MyEditText) findViewById(R.id.etx_fpsUm);
		actLot      = (MyEditText) findViewById(R.id.etx_fpsLot);
		actQty     	= (MyEditText) findViewById(R.id.etx_fpsQty);
		actUnit     = (MyEditText) findViewById(R.id.etx_fpsUnit);
		actDate     = (MyEditText) findViewById(R.id.etx_fpsDate);
		actShift    = (MyEditText) findViewById(R.id.etx_fpsShift);
		actBox		= (MyEditText) findViewById(R.id.etx_fpsBox);
		actBox.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actBox.getValStr().equals("")){
					Calculation();
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		actScat		= (MyEditText) findViewById(R.id.etx_fpsScat);
		actScat.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!actScat.getValStr().equals("")){
					Calculation();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		dtg 		= (MyDataGrid) findViewById(R.id.mdtg_fsp);
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid		= ApplicationDB.user.getUserId();
		fnceffdate  = ApplicationDB.user.getUserDate();
		
		Date da = new Date();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		String date = sim.format(da);
		actDate.setText(date);
		getFocues(actBar, true);	//光标停留

		rfc_is_nbr_fps = applicationDB.Ctrl.getString("RFC_IS_NBR_FPS", "0.0").toString();
		if(StringUtil.parseFloat(rfc_is_nbr_fps) == 0){
			actNbr.setReadOnly(true);
			actLocBin.setReadOnly(false);
		}else{
			actNbr.setReadOnly(false);
			actLocBin.setReadOnly(true);
		}
	}

	@Override
	protected void unLockNbr() {
	}
	
	//计算 总数 = 单包量 * 箱数 + 散量
	public void Calculation(){
		if(actBox.getValStr().equals("")){
			actBox.setText("0");
		}
		if(actScat.getValStr().equals("")){
			actScat.setText("0");
		}
		
		float zs = StringUtil.parseFloat(actBox.getValStr()) * 
				StringUtil.parseFloat(actUnit.getValStr()) +  StringUtil.parseFloat(actScat.getValStr());
		actQty.setText(zs+"");
	}
		
	// 源容相关文本禁用
	public void yEnabledFalse() {
		actBox.setEnabled(false);
		actScat.setEnabled(false);
	}

	// 源容相关文本激活
	public void yEnabledTrue() {
		actBox.setEnabled(true);
		actScat.setEnabled(true);
	}
	

	boolean isSchBarSuc = false;
	protected boolean onBlur(int id, View v) {
		if(R.id.etx_fpsBar == id && isSchBarSuc){
			runClickFun();
		}else if(R.id.etx_fpsBox == id && isSchBarSuc){
			if(actBox.getValStr().equals("")){
				actBox.setText("0");
			}
			runClickFun();
		}else if(R.id.etx_fpsScat == id && isSchBarSuc){
			if(actScat.getValStr().equals("")){
				actScat.setText("0");
			}
			runClickFun();
		}else if(R.id.etx_fpsShift == id && isSchBarSuc){
			runClickFun();
		}else if(R.id.etx_fpsNbr == id ){
			if(!actNbr.getValStr().equals("")){
				BlurNbr();
			}
			if(isSchBarSuc){
				runClickFun();
			}
		}
		return true;
	}
	/*
	 * 单号
	 */
	public void BlurNbr(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return fpsBiz.fps_nbr(domain, site , actNbr.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if (!wrs.isSuccess()) {
					actNbr.setText("");
					getFocues(actNbr, true);	//光标停留
					showErrorMsg(wrs.getMessages());
					isSchBarSuc = false;
				}
			}
		});
	}
	
	/*
	 * 扫码
	 */
	public void BlurBar(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				String[] ss =  tiaoma.split(";");
				for (int i = 0; i < ss.length; i++) {
					if(ss[i].equals(actBar.getValStr())){
						actBar.setText("");
						showErrorMsg("条码已经存在明细中!");
						return false;
					}
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return fpsBiz.fps_scan(domain, site , actBar.getValStr(),actNbr.getValStr(),rfc_is_nbr_fps);
			}

			@Override
			public void onCallBrack(Object data) {
					WebResponse wrs = (WebResponse) data;
					if (wrs.isSuccess()) {
						Map<String, Object> map = new HashMap<String, Object>();
						map=wrs.getDataToMap();
						if(lbs.equals("L")||lbs.equals("")){
							if(map.get("RFPTV_LBS").equals("L")){
								tiaoma = map.get("RFLOT_SCAN")+";";
								nbrlist.clear(); 
								yEnabledTrue();
								isflgn = true;
							}else if(!map.get("RFPTV_LBS").equals("L")&&lbs.equals("L")){
								showErrorMsg("该条码批控类型和上个条码不一致无法累计提交.");
								isflgn = false;
							}else{
								tiaoma = tiaoma + map.get("RFLOT_SCAN")+";";
								yEnabledFalse();
								isflgn = true;
							}
						}else{
							if(map.get("RFPTV_LBS").equals("L")&&!lbs.equals("L")){
								showErrorMsg("该条码批控类型和上个条码不一致无法累计提交.");
								isflgn = false;
							}else{
								tiaoma = tiaoma + map.get("RFLOT_SCAN")+";";
								yEnabledFalse();
								isflgn = true;
							}
						}
						if(isflgn){
							actPart.setText(map.get("RFLOT_PART")+"");
							actDesc.setText(map.get("RFLOT_PART_DESC")+"");
							actUm.setText(map.get("RFLOT_UM")+"");
							actLot.setText(map.get("RFLOT_LOT")+"");
							actUnit.setText(map.get("RFLOT_MULT_QTY")+"");
							
							actLocBin.setText(map.get("LOC")+"");
							actScat.setText(map.get("RFLOT_SCATTER_QTY")+"");
							if(StringUtil.parseInt(map.get("RFLOT_SCATTER_QTY"))>0){
								actBox.setText("0");
								actQty.setText(map.get("RFPTV_MULT_BOX")+"");
							}else{
								actBox.setText("1");
								actQty.setText(map.get("QTY")+"");
							}
							locbin = map.get("LOC")+"";
							map.put("DATE", actDate.getValStr());
							map.put("SHIFT", actShift.getValStr());
							vend = map.get("RFLOT_VEND")+"";
							lbs = map.get("RFPTV_LBS")+"";
							nbrlist.add(map);
							dtg.buildData(nbrlist);
							isSchBarSuc = true;
							cons_nbr = map.get("RFLOT_CONS_NBR")+"";
							
						}else{
							actBar.setText("");
							getFocues(actBar, true);	//光标停留
							isSchBarSuc = false;
						}
					} else {
						actBar.setText("");
						dtg.buildData(nbrlist);
						getFocues(actBar, true);	//光标停留
						showErrorMsg(wrs.getMessages());
						isSchBarSuc = false;
					}
			}
		});
	}
	
	// 页面添加按钮
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[] { ButtonType.Submit};
	}
	
	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		
		return true;
	}

	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return super.OnBtnSerClick(btnType, v);
	}

	@Override
	public void OnBtnSerCallBack(Object data) {
		if(!"".equals(actBar.getValStr())){
			BlurBar();
		}
	
	}
	

	// 提交按钮
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(actQty.getValStr().equals("0")||actQty.getValStr().equals("0.0")){
			showErrorMsg("总数量不能为零！");
			getFocues(actBox, true);	//光标停留
			return false;
		}
		return true;
	}

	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return fpsBiz.fps_submit(domain, site, userid,locbin,tiaoma,vend,lbs,actQty.getValStr(),actNbr.getValStr(),cons_nbr,fnceffdate,rfc_is_nbr_fps);
	}

	public void OnBtnSubCallBack(Object data) {
		WebResponse wrs = (WebResponse) data;
		if (wrs.isSuccess()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map=wrs.getDataToMap();
			if(StringUtil.parseFloat(rfc_is_nbr_fps)!=0){
				if(map.get("STATUS").equals("J0.WAREHOUSED")){
					actNbr.setText("");
				}
			}
			actBar.setText("");
			actPart.setText("");
			actDesc.setText("");
			actUm.setText("");
			actLot.setText("");
			actQty.setText("");
			actUnit.setText("");
			actLocBin.setText("");
			actBox.setText("");
			actScat.setText("");
			nbrlist.clear(); 
			dtg.buildData(nbrlist);
			tiaoma = "";
			lbs = "";
			getFocues(actBar, true);	//光标停留
			showSuccessMsg(wrs.getMessages());
		} else {
			showErrorMsg(wrs.getMessages());
		}
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
