package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.text.Editable;
import android.view.View;

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

public class PxActivity extends AppFunActivity {
	
	private PxBiz Pxbiz;
	private MyTextView txv_pxUm; 
	private MyEditText  etx_pxPart,etx_pxPartDesc,etx_pxVend,etx_pxLot,
						etx_pxQty,etx_pxSumQty,etx_pxMultQty;
	private MyReadBQ  etx_pxBar,etx_pxBin,etx_pxToBin,etx_pxToLot;
	private MySpinner  spn_pxToLot;
	private MyDataGrid dtg_pxList;
	private ApplicationDB applicationDB;
	private String jsonStr="";
	private List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> tList = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_px;
	}

	@Override
	protected void pageLoad() {
		  Pxbiz=new PxBiz();
		  etx_pxBar =(MyReadBQ) findViewById(R.id.etx_pxBar);
		  etx_pxBin =(MyReadBQ) findViewById(R.id.etx_pxBin);
		  etx_pxToBin =(MyReadBQ) findViewById(R.id.etx_pxToBin);
		  txv_pxUm =(MyTextView) findViewById(R.id.txv_pxUm);
		  etx_pxPart =(MyEditText) findViewById(R.id.etx_pxPart);
		  etx_pxPartDesc =(MyEditText) findViewById(R.id.etx_pxPartDesc);
		  etx_pxVend= (MyEditText) findViewById(R.id.etx_pxVend);
		  etx_pxLot= (MyEditText) findViewById(R.id.etx_pxLot);
		  etx_pxQty=(MyEditText) findViewById(R.id.etx_pxQty);
		  etx_pxSumQty= (MyEditText) findViewById(R.id.etx_pxSumQty);
		  etx_pxMultQty=(MyEditText) findViewById(R.id.etx_pxMultQty);
		  etx_pxToLot= (MyReadBQ) findViewById(R.id.etx_pxToLot);
		  spn_pxToLot= (MySpinner) findViewById(R.id.spn_pxToLot);
		  etx_pxToBin.setText("10000-1");
		  
		  dtg_pxList = (MyDataGrid)findViewById(R.id.dtg_pxList);
		  
		  if("0.0".equals(applicationDB.Ctrl.getString("RFC_PX_LOT", "0.0").toString())){
			  	etx_pxToLot.setVisibility(View.GONE);
			}else{
				spn_pxToLot.setVisibility(View.GONE);
			}
		  
		  /*spn_pxToLot.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
						checkPxToLot();
				};
				public void onNothingSelected(AdapterView<?> parent){};
			});*/
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
		if(etx_pxBar.getId()==id){
			runClickFun();
			if(null != etx_pxBar.getValStr() && !"".equals( etx_pxBar.getValStr().toString().trim())){
				if(list!=null && list.size()>0){
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> map=list.get(i);
						if(etx_pxBar.getValStr().equals(map.get("SCAN"))){
							istrue = false;
							showErrorMsg(getResources().getString(R.string.Px_Scan_false));
							getFocues(etx_pxBar, true);	//光标停留
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
		if(etx_pxBin.getId()==id){
			if(!"".equals(etx_pxPart.getValStr()) && !"".equals( etx_pxBin.getValStr().toString().trim())){
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map=list.get(i);
					if(etx_pxLot.getValStr().equals(map.get("LOT")) && etx_pxBar.getValStr().equals(map.get("SCAN"))){
						map.put("BIN", etx_pxBin.getValStr());
						list.set(i, map);
						dtg_pxList.buildData(list);
					}
				}
				runClickFun();
			}else{
				istrue = true;
			}		
		}
	//数量光标离开事件相对应的代码
		if(etx_pxQty.getId()==id){
			if(!"".equals(etx_pxPart.getValStr()) && !"".equals( etx_pxQty.getValStr().toString().trim())){
				if(Float.valueOf(etx_pxSumQty.getValStr()) > Float.valueOf(etx_pxMultQty.getValStr())){
					istrue = false;
					showErrorMsg(getResources().getString(R.string.Px_Qty_false));
					getFocues(etx_pxQty, true);	//光标停留
				}else if(etx_pxSumQty.getValStr().equals(etx_pxMultQty.getValStr())){
					getFocues(etx_pxToBin, true);	//光标停留
				}else{
					 etx_pxBar.reValue();
					 etx_pxPart.reValue();
					 txv_pxUm.setText("");
					 etx_pxPartDesc.reValue();
					 etx_pxVend.reValue();
					 etx_pxBin.reValue();
					 etx_pxLot.reValue();
					 etx_pxQty.reValue();
					 etx_pxMultQty.reValue();
					 getFocues(etx_pxBar, true);	//光标停留
				}
				runClickFun();
			}else{
				istrue = true;
			}		
		}
		if(etx_pxToBin.getId() == id){
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
				return Pxbiz.pxCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_pxBar.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					if(list!=null && list.size()>0){
						Map<String, Object> lMap=list.get(0);
						if(map.get("RFLOT_PART").equals(lMap.get("PART"))){
							Map<String, Object> itemMap = new HashMap<String, Object>();
							itemMap.put("PART", map.get("RFLOT_PART"));
							itemMap.put("DESC", map.get("PART_DESC"));
							itemMap.put("VEND", map.get("RFLOT_VEND"));
							itemMap.put("LOT", map.get("RFLOT_LOT"));
							itemMap.put("BIN", map.get("LOCBIN"));
							itemMap.put("QTY", map.get("QTY"));
							itemMap.put("MULT_QTY", map.get("RFLOT_BOX_QTY"));
							itemMap.put("SCAN",etx_pxBar.getValStr());
							list.add(itemMap);
								
							etx_pxVend.setText(map.get("RFLOT_VEND")+"");
							etx_pxPart.setText(map.get("RFLOT_PART")+"");
							etx_pxPartDesc.setText(map.get("PART_DESC")+"");
							txv_pxUm.setText(map.get("RFLOT_UM")+"");
							etx_pxBin.setText(map.get("LOCBIN")+"");
							etx_pxMultQty.setText(map.get("RFLOT_BOX_QTY")+"");
							etx_pxLot.setText(map.get("RFLOT_LOT")+"");
								
							etx_pxQty.setText(map.get("QTY")+"");
							if("".equals(etx_pxSumQty.getValStr())){
								etx_pxSumQty.setText(map.get("QTY")+"");
							}
							getFocues(etx_pxQty, true);	//光标停留
							dtg_pxList.buildData(list);
							}else{
								istrue = false;
								showErrorMsg(getResources().getString(R.string.Px_Part_false));
								getFocues(etx_pxBar, true);
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
						itemMap.put("SCAN",etx_pxBar.getValStr());
						list.add(itemMap);
						etx_pxVend.setText(map.get("RFLOT_VEND")+"");
						etx_pxPart.setText(map.get("RFLOT_PART")+"");
						etx_pxPartDesc.setText(map.get("PART_DESC")+"");
						txv_pxUm.setText(map.get("RFLOT_UM")+"");
						etx_pxBin.setText(map.get("LOCBIN")+"");
						etx_pxMultQty.setText(map.get("RFLOT_BOX_QTY")+"");
						etx_pxLot.setText(map.get("RFLOT_LOT")+"");
						
						etx_pxQty.setText(map.get("QTY")+"");
						if("".equals(etx_pxSumQty.getValStr())){
							etx_pxSumQty.setText(map.get("QTY")+"");
						}
						getFocues(etx_pxQty, true);	//光标停留
						dtg_pxList.buildData(list);
					}
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					getFocues(etx_pxBar, true);
				}
			}
		});
	}
	
	@Override
	protected void onChangedAft(int id, Editable s) {
		if(etx_pxQty.getId()==id){
			if(!"".equals(etx_pxPart.getValStr()) && !"".equals( etx_pxQty.getValStr().toString().trim())){
				ArrayList<String> lList=new ArrayList<String>();;
				float sumQty=0;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map=list.get(i);
					if("0.0".equals(applicationDB.Ctrl.getString("RFC_PX_LOT", "0.0").toString())){
						lList.add(map.get("LOT")+"");
					}
					if(etx_pxLot.getValStr().equals(map.get("LOT")) && etx_pxBar.getValStr().equals(map.get("SCAN"))){
						map.put("QTY", etx_pxQty.getValStr());
						list.set(i, map);
						dtg_pxList.buildData(list);
					}
					sumQty +=Float.valueOf(map.get("QTY")+"");
				}
				if(lList.size()>0){
					String[] arrLot = (String[])lList.toArray(new String[list.size()]);
					spn_pxToLot.addAndClearItems(arrLot);
				}
				etx_pxSumQty.setText(sumQty+"");
			}
			
		}
	}
	

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit};
	}	
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		String toLot="";
		 if("0.0".equals(applicationDB.Ctrl.getString("RFC_PX_LOT", "0.0").toString())){
			 toLot=spn_pxToLot.getValStr();
		 }else{
			 toLot=etx_pxToLot.getValStr();
		 }
		if(!"".equals(toLot) && !"".equals(etx_pxToBin.getValStr())){
			
		}else{
			showErrorMsg(getResources().getString(R.string.Px_Sub_false));
			istrue=false;
		}
		return istrue;
	}
	
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		jsonStr="";
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
		 if("0.0".equals(applicationDB.Ctrl.getString("RFC_PX_LOT", "0.0").toString())){
			 toLot=spn_pxToLot.getValStr();
		 }else{
			 toLot=etx_pxToLot.getValStr();
		 }
		return Pxbiz.pxSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),applicationDB.user.getUserId(),jsonStr,toLot,etx_pxToBin.getValStr(),sumQty+"",applicationDB.user.getUserDate());
	
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etx_pxBar.reValue();
			etx_pxPart.reValue();
			txv_pxUm.setText("");
			etx_pxPartDesc.reValue();
			etx_pxVend.reValue();
			etx_pxBin.reValue();
			etx_pxLot.reValue();
			etx_pxQty.reValue();
			etx_pxSumQty.reValue();
			etx_pxMultQty.reValue();
			etx_pxToBin.reValue();
			if("0.0".equals(applicationDB.Ctrl.getString("RFC_PX_LOT", "0.0").toString())){
				spn_pxToLot.clearItems();
			}else{
				etx_pxToLot.reValue();
			}
			etx_pxToLot.reValue();
			String msg=wr.getMessages();
			showSuccessMsg(msg);
			list.clear();
			dtg_pxList.buildData(list);
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
			getFocues(etx_pxBar, true);
		}
		
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
