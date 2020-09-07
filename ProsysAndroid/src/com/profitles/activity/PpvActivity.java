package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;

import com.profitles.biz.PpvBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;

public class PpvActivity extends AppFunActivity {

	private MyEditText yContainer, yUnit, yBox, yScat, zContainer, zUnit, zBox,
			zScat;
	private MyTextView MiaoShu, DanWei, txvPart, txvVend, txvLot;
	private MyReadBQ actBar;
	private MyDataGrid Splitfront, Aftersplit;
	private String domain, site, userId,fnceffdate;
	private PpvBiz ppvBiz ;
	private int bcount = 0;
	private String Package = "";
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_ppv;
	}

	@Override
	protected void pageLoad() {
		ppvBiz =new PpvBiz();
		actBar = (MyReadBQ) findViewById(R.id.ext_ppvBar);
		MiaoShu = (MyTextView) findViewById(R.id.txv_ppvMs);
		DanWei = (MyTextView) findViewById(R.id.txv_ppvDw);
		txvPart = (MyTextView) findViewById(R.id.txv_ppvParts);
		txvVend = (MyTextView) findViewById(R.id.txv_ppvVend);
		txvLot = (MyTextView) findViewById(R.id.txv_ppvLot);
		yContainer = (MyEditText) findViewById(R.id.ext_ppvyContainer);
		yUnit = (MyEditText) findViewById(R.id.ext_ppvyUnit);
		yBox = (MyEditText) findViewById(R.id.ext_ppvyBox);
		yScat = (MyEditText) findViewById(R.id.ext_ppvyScat);
		zContainer = (MyEditText) findViewById(R.id.ext_ppvzContainer);
		zUnit = (MyEditText) findViewById(R.id.ext_ppvzUnit);
		zBox = (MyEditText) findViewById(R.id.ext_ppvzBox);
		zScat = (MyEditText) findViewById(R.id.ext_ppvzScat);
		Splitfront = (MyDataGrid) findViewById(R.id.mdtg_Splitfront);
		Aftersplit = (MyDataGrid) findViewById(R.id.mdtg_Aftersplit);
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		userId =  ApplicationDB.user.getUserId();
		fnceffdate = ApplicationDB.user.getUserDate();
	}

	// 源容相关文本禁用
	public void yEnabledFalse() {
		yBox.setEnabled(false);
		yScat.setEnabled(false);
		yContainer.setEnabled(false);
		yUnit.setEnabled(false);
	}

	// 源容相关文本激活
	public void yEnabledTrue() {
		yBox.setEnabled(true);
		yScat.setEnabled(true);
		yContainer.setEnabled(true);
		yUnit.setEnabled(true);
	}

	// 鼠标进入事件
	protected boolean onFocus(int id, View v) {
		if (R.id.ext_ppvzContainer == id) {
			if (!"".equals(yContainer.getValStr())) {
				yEnabledFalse();
				yScat.setEnabled(true);
				if("1".equals(Package)){
					yScat.setEnabled(false);
				}
			}
			if(!"0".equals(yScat.getValStr())&&!"".equals(yScat.getValStr())){
				yScat.setEnabled(false);
				if("1".equals(Package)){
					yScat.setEnabled(true);
					Package = "";
				}
			}
		}
		if (R.id.ext_ppvBar == id) {
			txvPart.setText("");
			MiaoShu.setText("");
			DanWei.setText("");
			txvVend.setText("");
			txvLot.setText("");
			yContainer.setText("");
			yUnit.setText("");
			yBox.setText("");
			yScat.setText("");
			yEnabledTrue();
		}
		return true;
	}

	// 鼠标离开事件
	protected boolean onBlur(int id, View v) {
		if (R.id.ext_ppvBar == id) {
			if (!"".equals(actBar.getValStr())) {
				String clean = "";
				if(mMap != null){
					for (int i = 0; i < mMap.size(); i++) {
						if(mMap.get(i).get("Scan").toString().equals(actBar.getValStr())){
							mMap.remove(i);
							Splitfront.buildData(mMap);
							bcount =bcount-1;
							if(i == 0){
								actBar.setText("");
								Clean();
								getFocues(actBar, true);
							}else{
								actBar.setText(mMap.get(i-1).get("Scan")+"");
								yContainer.setText(mMap.get(i-1).get("yContainer")+"");
								yBox.setText(mMap.get(i-1).get("yBox")+"");
								yUnit.setText(mMap.get(i-1).get("yUnit")+"");
								yScat.setText(mMap.get(i-1).get("yScat")+"");
								txvPart.setText(mMap.get(i-1).get("PART")+"");
								MiaoShu.setText(mMap.get(i-1).get("MiaoShu")+"");
								DanWei.setText(mMap.get(i-1).get("DanWei")+"");
								txvVend.setText(mMap.get(i-1).get("txvVend")+"");
								txvLot.setText(mMap.get(i-1).get("txvLot")+"");
							}
							showErrorMsg("当前扫码在拆分前页签中存在默认帮您清空当前条码数据!");
							clean = "clean";
							break;
						}
					}
					if(!clean.equals("clean")){
						ppvBarNotNull();
					}
				}
			} else {
				showErrorMsg("扫码不能为空！");
				getFocues(actBar, true);
			}
		}
		if (R.id.ext_ppvzContainer == id) {
			if (!"".equals(zContainer.getValStr())) {
				zContainerNotNull();
			} else {
				yEnabledTrue();
				zUnit.setText("");
				zBox.setText("");
				zScat.setText("");
				bcount = bcount - 1;
				mMap.clear();
				Splitfront.buildData(mMap);
				Aftersplit.buildData(mMap);
			}
			runClickFun();
		}
		if (R.id.ext_ppvyBox == id) {
			if (!"".equals(yBox.getValStr())) {
				SplitFront("");
			}
		}
		if (R.id.ext_ppvyScat == id) {
			if (!"".equals(yScat.getValStr())) {
				SplitFront("");
			}
		}
		if (R.id.ext_ppvzBox == id) {
			if (!"".equals(zBox.getValStr()) && !"0".equals(zBox.getValStr())) {
				CalculationScat();
			}
			runClickFun();
		}
		if (R.id.ext_ppvzScat == id) {
			runClickFun();
		}
		if (R.id.ext_ppvzUnit == id) {
			runClickFun();
		}
		return true;
	}

	// 扫码失焦时触发事件
	public void ppvBarNotNull() {
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return ppvBiz.ppv_scancode(domain, site, actBar.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if (wrs.isSuccess()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map = wrs.getDataToMap();
					txvPart.setText(map.get("RFLOT_PART") + "");
					MiaoShu.setText(map.get("RFLOT_PART_DESC") + "");
					DanWei.setText(map.get("UM") + "");
					txvVend.setText(map.get("RFLOT_VEND") + "");
					txvLot.setText(map.get("RFLOT_LOT") + "");
					yUnit.setText(map.get("RFLOT_BOX_QTY") + "");
					yContainer.setText(map.get("BOXID") + "");
					yBox.setText("1");
					yScat.setText("0");
					String by = "";
					if(!map.get("SCATTER").toString().equals("")&&!map.get("SCATTER").toString().equals("0")){
						yUnit.setText(map.get("SCATTER") + "");
						Package = "1";
						SplitFront("B");
						yEnabledFalse();
						by = "1";
					}
					if(!"1".equals(by)){
						if (map.get("LBS").toString().equals("B")) {
							yEnabledFalse();
							SplitFront("B");
						}else{
							getFocues(yBox, true);
						}
					}
					
				} else {
					Clean();
					getFocues(actBar, true);
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}

	private List<Map<String, Object>> mMap = new ArrayList<Map<String, Object>>();
	// 输入源容箱数,散量失焦时或零件批号类型为按箱时执行的事件(在拆分前添加一条记录)
	public void SplitFront(String b) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PART", txvPart.getValStr());
		map.put("MiaoShu", MiaoShu.getValStr());
		map.put("DanWei", DanWei.getValStr());
		map.put("txvVend", txvVend.getValStr());
		map.put("txvLot", txvLot.getValStr());
		map.put("yContainer", yContainer.getValStr());
		map.put("yUnit", yUnit.getValStr());
		map.put("yBox", yBox.getValStr());
		map.put("yScat", yScat.getValStr());
		map.put("Scan", actBar.getValStr());
		if(!"".equals(b)){
			bcount += 1;
			if("1".equals(Package)){
				getFocues(zContainer, true);
			}else{
				getFocues(yContainer, true);
			}
			
			
		}else{
			bcount = 1;
			getFocues(zContainer, true);
		}
		mMap.add(map);
		Splitfront.buildData(mMap);
	}

	// 至容失焦时触发事件
	public void zContainerNotNull() {
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if (zContainer.getValStr().equals(yContainer.getValStr())) {
					showErrorMsg("至容与源容不能一致,请您重新输入!");
					return false;
				} else {
					return true;
				}
			}

			@Override
			public Object onGetData() {
				return ppvBiz.ppv_zContainer(domain, txvPart.getValStr(),
						txvVend.getValStr(), zContainer.getValStr(),
						yBox.getValStr(), yUnit.getValStr(), yScat.getValStr(),bcount+"");
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if (wrs.isSuccess()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map = wrs.getDataToMap();
					zBox.setText(map.get("zBox") + "");
					zScat.setText(map.get("zScat") + "");
					zUnit.setText(map.get("RFPPV_MULT") + "");
					showConfirm(
							"拆分" + map.get("zBox") + "整箱及" + map.get("zScat")
									+ "个散量是否确定?", QueRen);
				} else {
					getFocues(zContainer, true);
					showErrorMsg(wrs.getMessages());
				}
			}

		});
	}

	// 至容失焦后提示拆分整箱量及散量是否确定拆分后数据执行的事件(在拆分后添加一条记录)
	private OnShowConfirmListen QueRen = new OnShowConfirmListen() {

		@Override
		public void onConfirmClick() {
			List<Map<String, Object>> lMap = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PART", txvPart.getValStr());
			map.put("MiaoShu", MiaoShu.getValStr());
			map.put("DanWei", DanWei.getValStr());
			map.put("txvVend", txvVend.getValStr());
			map.put("txvLot", txvLot.getValStr());
			map.put("zContainer", zContainer.getValStr());
			map.put("zUnit", zUnit.getValStr());
			map.put("zBox", zBox.getValStr());
			map.put("zScat", zScat.getValStr());
			lMap.add(map);
			Aftersplit.buildData(lMap);
			getFocues(zScat, true);
		}
	

		@Override
		public void onCancelClick() {
			zContainer.setText("");
			zUnit.setText("");
			zBox.setText("");
			zScat.setText("");
			yEnabledTrue();
			getFocues(yBox, true);
		}

	};

	// 修改至容箱数自动计算出至容散量
	public boolean CalculationScat() {
		Float Total = Float.valueOf(yBox.getValStr())*bcount
				* Float.valueOf(yUnit.getValStr())
				+ Float.valueOf(yScat.getValStr());
		Float scat = Total - Float.valueOf(zBox.getValStr())*Float.valueOf(zUnit.getValStr());
		if(scat > Float.valueOf(yUnit.getValStr())){
			showErrorMsg("自动计算出散量超出源容每箱量,请重新输入合适的箱数!");
			this.zBox.setText("");
			this.zScat.setText("0.0");
			getFocues(this.zBox, true);
			return false;
		}else if(scat < 0){
			showErrorMsg("自动计算出的散量已存在负数,请重新输入合适的箱数!");
			this.zBox.setText("");
			this.zScat.setText("0.0");
			getFocues(this.zBox, true);
			return false;
		}else{
			showConfirm(
					"拆分" + zBox.getValStr() + "整箱及" + scat
							+ "个散量是否确定?", QueRen);
			zScat.setText(scat+"");
			getFocues(this.zScat, true);
			return true;
		}
	}

	// 提交按钮
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		JudgeIsEmpty();
		return true;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		String yCount = Float.valueOf(yBox.getValStr())*Float.valueOf(bcount)+"";
		if(!"0".equals(yScat.getValStr())){
			yCount = Float.valueOf(yBox.getValStr()) + 1 +"";
		}
		if(Float.valueOf(zScat.getValStr()) > Float.valueOf(zUnit.getValStr())){
			yCount = Float.valueOf(yCount) - 1 +"";
		}
		return ppvBiz.ppv_Commit(domain, site, userId ,yContainer.getValStr(),zContainer.getValStr(), zBox.getValStr(),
				zUnit.getValStr(), zScat.getValStr(), actBar.getValStr(),yCount,fnceffdate);
	}

	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse web = (WebResponse) data;
		if (web.isSuccess()) {
			yEnabledTrue();
			zContainer.setText("");
			zUnit.setText("");
			zBox.setText("");
			zScat.setText("");
			actBar.setText("");
			bcount = 0;
			Clean();
			mMap.clear();
			Splitfront.buildData(mMap);
			Aftersplit.buildData(mMap);
			showSuccessMsg(web.getMessages());
		} else {
			showErrorMsg(web.getMessages());
		}
	}
	
	public void Clean(){
		txvPart.setText("");
		MiaoShu.setText("");
		DanWei.setText("");
		txvVend.setText("");
		txvLot.setText("");
		yContainer.setText("");
		yUnit.setText("");
		yBox.setText("");
		yScat.setText("");
	}
	
	public boolean JudgeIsEmpty(){
		if("".equals(actBar.getValStr())){
			showErrorMsg("扫码不能为空!");
			return false;
		}else if("".equals(yBox.getValStr())){
			showErrorMsg("源容相关信息不能为空!");
			return false;
		}else if("".equals(zContainer.getValStr())){
			showErrorMsg("至容不能为空!");
			return false;
		}else if("".equals(zBox.getValStr())){
			showErrorMsg("至容箱数不能为空!");
			return false;
		}else if("".equals(zUnit.getValStr())){
			showErrorMsg("至容每箱量不能为空!");
			return false;
		}else if("".equals(zScat.getValStr())){
			showErrorMsg("至容散量不能为空!");
			return false;
		}else if(Float.valueOf(zScat.getValStr()) > Float.valueOf(yUnit.getValStr())){
			showErrorMsg("至容散量不允许超出源容每箱量!");
			return false;
		}
		return true;
	}

	@Override
	protected void unLockNbr() {
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}

}
