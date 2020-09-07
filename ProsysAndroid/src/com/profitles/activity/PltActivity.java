package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;
import android.text.Editable;
import android.view.View;

import com.profitles.biz.PltBiz;
import com.profitles.biz.PxBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTextView;

public class PltActivity extends AppFunActivity {
	
	private PltBiz Pltbiz;
	private MyTextView txv_pltUm; 
	private MyEditText  etx_pltPart,etx_pltPartDesc,etx_pltVend,etx_pltLot,
						etx_pltQty,etx_pltSumQty,etx_pltMultQty,etx_pltPQty,etx_pltAdvBox,etx_pltSumBox;
	private MyReadBQ  etx_pltBar,etx_pltBin,etx_pltToBin,etx_pltPNbr;
	private MySpinner  spn_pltToLot;
	private MyDataGrid dtg_pltList;
	private ApplicationDB applicationDB;
	private String jsonStr="";
	private List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> tList = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_plt;
	}

	@Override
	protected void pageLoad() {
		  Pltbiz=new PltBiz();
		  etx_pltBar =(MyReadBQ) findViewById(R.id.etx_pltBar);
		  etx_pltBin =(MyReadBQ) findViewById(R.id.etx_pltBin);
		  etx_pltToBin =(MyReadBQ) findViewById(R.id.etx_pltToBin);
		  txv_pltUm =(MyTextView) findViewById(R.id.txv_pltUm);
		  etx_pltPart =(MyEditText) findViewById(R.id.etx_pltPart);
		  etx_pltPartDesc =(MyEditText) findViewById(R.id.etx_pltPartDesc);
		  etx_pltVend= (MyEditText) findViewById(R.id.etx_pltVend);
		  etx_pltLot= (MyEditText) findViewById(R.id.etx_pltLot);
		  etx_pltQty=(MyEditText) findViewById(R.id.etx_pltQty);
		  etx_pltSumQty= (MyEditText) findViewById(R.id.etx_pltSumQty);
		  etx_pltMultQty=(MyEditText) findViewById(R.id.etx_pltMultQty);
		  etx_pltPQty= (MyEditText) findViewById(R.id.etx_pltPQty);
		  etx_pltAdvBox=(MyEditText) findViewById(R.id.etx_pltAdvBox);
		  etx_pltSumBox=(MyEditText) findViewById(R.id.etx_pltSumBox);
		  etx_pltPNbr= (MyReadBQ) findViewById(R.id.etx_pltPNbr);
		  
		  dtg_pltList = (MyDataGrid)findViewById(R.id.dtg_pltList);
		  
	} 
	
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_PorcNbr,etx_PorcBar);
	}

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//单号光标离开事件相对应的代码
		if(etx_pltBar.getId()==id){
			runClickFun();
			if(null != etx_pltBar.getValStr() && !"".equals( etx_pltBar.getValStr().toString().trim())){
				if(list!=null && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map=list.get(i);
						if(etx_pltBar.getValStr().equals(map.get("SCAN"))){
							istrue = false;
							showErrorMsg(getResources().getString(R.string.Px_Scan_false));
							getFocues(etx_pltBar, true);	//光标停留
						}
					}
					if(istrue){
						checkBar();
					}
				}else{
					checkBar();
				}
				runClickFun();
			}else{
				istrue = true;
			}
		}
		
		//源储光标离开事件相对应的代码
		if(etx_pltBin.getId()==id){
			if(!"".equals(etx_pltPart.getValStr()) && !"".equals( etx_pltBin.getValStr().toString().trim())){
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map=list.get(i);
					if(etx_pltLot.getValStr().equals(map.get("LOT")) && etx_pltBar.getValStr().equals(map.get("SCAN"))){
						map.put("BIN", etx_pltBin.getValStr());
						list.set(i, map);
						dtg_pltList.buildData(list);
					}
				}
				runClickFun();
			}else{
				istrue = true;
			}		
		}
	//数量光标离开事件相对应的代码
		if(etx_pltQty.getId()==id){
			if(!"".equals(etx_pltPart.getValStr()) && !"".equals( etx_pltQty.getValStr().toString().trim())){
				if(Float.valueOf(etx_pltSumQty.getValStr()) > Float.valueOf(etx_pltMultQty.getValStr())){
					istrue = false;
					showErrorMsg(getResources().getString(R.string.Px_Qty_false));
					getFocues(etx_pltQty, true);	//光标停留
				}else if(etx_pltSumQty.getValStr().equals(etx_pltMultQty.getValStr())){
					getFocues(etx_pltToBin, true);	//光标停留
				}else{
					 etx_pltBar.reValue();
					 etx_pltPart.reValue();
					 txv_pltUm.setText("");
					 etx_pltPartDesc.reValue();
					 etx_pltVend.reValue();
					 etx_pltBin.reValue();
					 etx_pltLot.reValue();
					 etx_pltQty.reValue();
					 etx_pltMultQty.reValue();
					 getFocues(etx_pltBar, true);	//光标停留
				}
				runClickFun();
			}else{
				istrue = true;
			}		
		}
		if(etx_pltToBin.getId() == id){
			runClickFun();
		}
		return istrue;
	}
	
	
	private void checkBar() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Pltbiz.pltCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_pltBar.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					if(list!=null && list.size()>0){
						Map<String, Object> lMap=list.get(0);
						if(map.get("RFLOT_PART").equals(lMap.get("PART")) && map.get("RFLOT_LOT").equals(lMap.get("LOT"))){
							Map<String, Object> itemMap = new HashMap<String, Object>();
							itemMap.put("PART", map.get("RFLOT_PART"));
							itemMap.put("DESC", map.get("PART_DESC"));
							itemMap.put("VEND", map.get("RFLOT_VEND"));
							itemMap.put("LOT", map.get("RFLOT_LOT"));
							itemMap.put("BIN", map.get("LOCBIN"));
							itemMap.put("QTY", map.get("QTY"));
							itemMap.put("MULT_QTY", map.get("RFLOT_BOX_QTY"));
							itemMap.put("SCAN",etx_pltBar.getValStr());
							list.add(itemMap);
							if("".equals(etx_pltPQty.getValStr().trim()))	{
								etx_pltPQty.setText(map.get("PLTQTY")+"");
							}
							if("".equals(etx_pltAdvBox.getValStr().trim()))	{
								etx_pltAdvBox.setText(map.get("tjBoxQty")+"");
							}
							if("".equals(etx_pltSumBox.getValStr().trim()))	{
								etx_pltSumBox.setText("1");
							}else{
								etx_pltSumBox.setText(Integer.valueOf(etx_pltVend.getValStr())+1);
							}
							etx_pltVend.setText(map.get("RFLOT_VEND")+"");
							etx_pltPart.setText(map.get("RFLOT_PART")+"");
							etx_pltPartDesc.setText(map.get("PART_DESC")+"");
							txv_pltUm.setText(map.get("RFLOT_UM")+"");
							etx_pltBin.setText(map.get("LOCBIN")+"");
							etx_pltMultQty.setText(map.get("RFLOT_BOX_QTY")+"");
							etx_pltLot.setText(map.get("RFLOT_LOT")+"");
								
							etx_pltQty.setText(map.get("QTY")+"");
							if("".equals(etx_pltSumQty.getValStr())){
								etx_pltSumQty.setText(map.get("QTY")+"");
							}
							getFocues(etx_pltQty, true);	//光标停留
							dtg_pltList.buildData(list);
							}else{
								istrue = false;
								showErrorMsg(getResources().getString(R.string.Px_Part_false));
								getFocues(etx_pltBar, true);
							}
					}else{
						Map<String, Object> itemMap = new HashMap<String, Object>();
						itemMap.put("PART", map.get("RFLOT_PART"));
						itemMap.put("DESC", map.get("PART_DESC"));
						itemMap.put("VEND", map.get("RFLOT_VEND"));
						itemMap.put("LOT", map.get("RFLOT_LOT"));
						itemMap.put("BIN", map.get("LOCBIN"));
						itemMap.put("QTY", map.get("QTY"));
						itemMap.put("MULT_QTY", map.get("RFLOT_BOX_QTY"));
						itemMap.put("SCAN",etx_pltBar.getValStr());
						list.add(itemMap);
						etx_pltVend.setText(map.get("RFLOT_VEND")+"");
						etx_pltPart.setText(map.get("RFLOT_PART")+"");
						etx_pltPartDesc.setText(map.get("PART_DESC")+"");
						txv_pltUm.setText(map.get("RFLOT_UM")+"");
						etx_pltBin.setText(map.get("LOCBIN")+"");
						etx_pltMultQty.setText(map.get("RFLOT_BOX_QTY")+"");
						etx_pltLot.setText(map.get("RFLOT_LOT")+"");
						
						etx_pltQty.setText(map.get("QTY")+"");
						if("".equals(etx_pltSumQty.getValStr())){
							etx_pltSumQty.setText(map.get("QTY")+"");
						}
						getFocues(etx_pltQty, true);	//光标停留
						dtg_pltList.buildData(list);
					}
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					getFocues(etx_pltBar, true);
				}
			}
		});
	}
	
	@Override
	protected void onChangedAft(int id, Editable s) {
		if(etx_pltQty.getId()==id){
			if(!"".equals(etx_pltPart.getValStr()) && !"".equals( etx_pltQty.getValStr().toString().trim())){
				ArrayList<String> lList=new ArrayList<String>();;
				float sumQty=0;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map=list.get(i);
					if("0.0".equals(applicationDB.Ctrl.getString("RFC_plt_LOT", "0.0").toString())){
						lList.add(map.get("LOT")+"");
					}
					if(etx_pltLot.getValStr().equals(map.get("LOT")) && etx_pltBar.getValStr().equals(map.get("SCAN"))){
						map.put("QTY", etx_pltQty.getValStr());
						list.set(i, map);
						dtg_pltList.buildData(list);
					}
					sumQty +=Float.valueOf(map.get("QTY")+"");
				}
				if(lList.size()>0){
					String[] arrLot = (String[])lList.toArray(new String[list.size()]);
					spn_pltToLot.addAndClearItems(arrLot);
				}
				etx_pltSumQty.setText(sumQty+"");
			}
			
		}
	}
	

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit};
	}	
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {

		return istrue;
	}
	
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return null;
		/*jsonStr="";
		float sumQty=0;
		//jsonStr = JSONArray.fromObject(list).toString();
		List<String> cache = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			JSONObject obj = new JSONObject(list.get(i)); 
			cache.add(obj.toString());  
			Map<String, Object> map=list.get(i);
			sumQty+= Float.valueOf(map.get("QTY")+"");
		}
		jsonStr = cache.toString(); 
		String toLot="";
		 if("0.0".equals(applicationDB.Ctrl.getString("RFC_plt_LOT", "0.0").toString())){
			 toLot=spn_pltToLot.getValStr();
		 }else{
			 toLot=etx_pltToLot.getValStr();
		 }
		return Pxbiz.pxSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),applicationDB.user.getUserId(),jsonStr,toLot,etx_pltToBin.getValStr(),sumQty+"");
		*/
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		/*if(wr.isSuccess()){
			etx_pltBar.reValue();
			etx_pltPart.reValue();
			txv_pltUm.setText("");
			etx_pltPartDesc.reValue();
			etx_pltVend.reValue();
			etx_pltBin.reValue();
			etx_pltLot.reValue();
			etx_pltQty.reValue();
			etx_pltSumQty.reValue();
			etx_pltMultQty.reValue();
			etx_pltToBin.reValue();
			if("0.0".equals(applicationDB.Ctrl.getString("RFC_plt_LOT", "0.0").toString())){
				spn_pltToLot.clearItems();
			}else{
				etx_pltToLot.reValue();
			}
			etx_pltToLot.reValue();
			String msg=wr.getMessages();
			showSuccessMsg(msg);
			list.clear();
			dtg_pltList.buildData(list);
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
			getFocues(etx_pltBar, true);
		}*/
		
	}
	
	
	@Override
	protected void unLockNbr() {

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
