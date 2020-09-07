
package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.zxing.common.StringUtils;
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
import com.profitles.framwork.util.StringUtil;

public class NewPxActivity extends AppFunActivity {
	
	private PxBiz Pxbiz;
	private MyTextView txv_pxUm,txv_pxNbl; 
	private MyEditText  etx_pxPart,etx_pxPartDesc,etx_pxVend,etx_pxLot,
						etx_pxQty,etx_pxSumQty,etx_pxMultQty,etx_yuQty;
	private MyReadBQ  etx_pxBar;
	private MySpinner  spn_pxToLot;
	private MyDataGrid dtg_pxList;
	private ApplicationDB applicationDB;
	private String jsonStr="";
	private List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> tList = new ArrayList<Map<String , Object>>();
	private float pxMultQty = 0,QtyZ = 0; //拆分量
	private String sumQty="", allQty = "" ,bin ="",cust_part = "",PO_NBR = "";
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_newpx;
	}

	@Override
	protected void pageLoad() {
		  Pxbiz=new PxBiz();
		  etx_pxBar =(MyReadBQ) findViewById(R.id.etx_pxBar);  //条码
		  txv_pxUm =(MyTextView) findViewById(R.id.txv_pxUm);
		  etx_pxPart =(MyEditText) findViewById(R.id.etx_pxPart); //零件
		  etx_pxPartDesc =(MyEditText) findViewById(R.id.etx_pxPartDesc); //零件描述
		  etx_pxVend= (MyEditText) findViewById(R.id.etx_pxVend); //供应商
		  etx_pxLot= (MyEditText) findViewById(R.id.etx_pxLot); //批次
		  etx_pxQty=(MyEditText) findViewById(R.id.etx_pxQty);  //扫描总数量
		  etx_pxSumQty= (MyEditText) findViewById(R.id.etx_pxSumQty); //拆分量
		  etx_pxMultQty=(MyEditText) findViewById(R.id.etx_pxMultQty); //已拆量 
		  etx_yuQty = (MyEditText) findViewById(R.id.etx_pxolyQty);//余量 
		  txv_pxNbl =(MyTextView) findViewById(R.id.txv_pxNbl); //箱号
		//  spn_pxToLot= (MySpinner) findViewById(R.id.spn_pxToLot);  
		  dtg_pxList = (MyDataGrid)findViewById(R.id.dtg_pxList);
		  etx_pxBar.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					clearMsg();
					if(!StringUtil.isEmpty(etx_pxBar.getValStr())){
						checkBar();
					}
				}

				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}

				public void afterTextChanged(Editable s) {
				}
			});
		
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
				boolean falg = false;
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					if(list!=null && list.size()>0){
						
						for(int j=0;j<list.size();j++){
							if(map.get("RFLOT_SCAN").equals(list.get(j).get("SCAN"))){
								falg = false;
								showErrorMsg("条码已存在列表,请重新扫码");
								etx_pxBar.setText("");
								getFocues(etx_pxBar, true);
								break;
							}else{
								falg = true;
							}
						}
						Map<String, Object> lMap=list.get(0);
						if(falg == true){
							if(!map.get("RFLOT_SCAN").equals(lMap.get("SCAN"))){
								if(map.get("RFLOT_PART").equals(lMap.get("PART"))&& map.get("RFLOT_LOT").equals(lMap.get("LOT"))&& map.get("LOCBIN").equals(lMap.get("BIN"))){
									Map<String, Object> itemMap = new HashMap<String, Object>();
									itemMap.put("SEQ_TWO", map.get("RFLOT_SEQ_TWO"));
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
									etx_pxLot.setText(map.get("RFLOT_LOT")+"");
									txv_pxNbl.setText(map.get("RFLOT_NUM_LBL")+"");
									etx_pxBar.setText("");
									bin = map.get("LOCBIN")+"";
									PO_NBR = map.get("RFLOT_CONS_NBR")+"";
									cust_part = map.get("RFLOT_CUST_PART")+"";
								    QtyZ =StringUtil.parseFloat(etx_pxQty.getValStr())+StringUtil.parseFloat(map.get("QTY")+"");
									etx_pxQty.setText(QtyZ+"");
									getFocues(etx_pxBar, true);	//光标停留
									dtg_pxList.buildData(list);
									}else{
										showErrorMsg(getResources().getString(R.string.Px_Part_false));
										etx_pxBar.setText("");
										getFocues(etx_pxBar, true);
									}
								}
						}
					}else{
						Map<String, Object> itemMap = new HashMap<String, Object>();
						itemMap.put("SEQ_TWO", map.get("RFLOT_SEQ_TWO"));
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
						etx_pxLot.setText(map.get("RFLOT_LOT")+"");
						txv_pxNbl.setText(map.get("RFLOT_NUM_LBL")+"");
						etx_pxBar.setText("");
						etx_pxQty.setText(map.get("QTY")+"");
						bin = map.get("LOCBIN")+"";
						cust_part = map.get("RFLOT_CUST_PART")+"";
						PO_NBR = map.get("RFLOT_CONS_NBR")+"";
						getFocues(etx_pxBar, true);	//光标停留
						dtg_pxList.buildData(list);
					}
				}else{
					
					showErrorMsg(wr.getMessages());
					etx_pxBar.setText("");
					getFocues(etx_pxBar, true);
				}
			}
		});
	}
	
	
	
	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//扫码光标离开事件相对应的代码
		if(etx_pxSumQty.getId()==id){
			runClickFun();
			if(!"".equals(etx_pxPart.getValStr()) && !"".equals( etx_pxQty.getValStr().toString().trim()) &&  !"".equals( etx_pxSumQty.getValStr().toString().trim())){
				ArrayList<String> lList=new ArrayList<String>();;
				float marginQty = 0; //余量
				 pxMultQty += StringUtil.parseFloat(etx_pxSumQty.getValStr()); //拆分量 StringUtil.parseFloat(etx_pxSumQty.getValStr())
				marginQty =  StringUtil.parseFloat(etx_pxQty.getValStr()) -pxMultQty ;
				if(etx_pxSumQty.getValStr().equals("0")|| etx_pxSumQty.getValStr().equals("0.0")){
					etx_pxMultQty.setText("");
					etx_pxSumQty.setText("");
					sumQty = "";
					allQty = "";
					marginQty=0;
					pxMultQty = 0;
					etx_yuQty.setText("");
				}
				if( marginQty < 0){
					showErrorMsg("拆分量+已拆分量不能大于扫描总量");
					etx_pxMultQty.setText("");
					etx_pxSumQty.setText("");
					sumQty = "";
					allQty = "";
					marginQty=0;
					etx_yuQty.setText("");
					pxMultQty = 0;
					istrue = false;
				}else{
				sumQty += etx_pxSumQty.getValStr()+",";
				if(marginQty != 0 || marginQty != 0.0){
				allQty = sumQty+marginQty;
				}else{
					allQty = sumQty;
				}
				etx_pxMultQty.setText(sumQty);
				etx_yuQty.setText(marginQty+"");
				etx_pxSumQty.setText("");
				}
			}
		}
		return istrue ;
	}

	
//	@Override
/*	protected void onChangedAft(int id, Editable s) {
		if(etx_pxSumQty.getId()==id){
			if(!"".equals(etx_pxPart.getValStr()) && !"".equals( etx_pxQty.getValStr().toString().trim()) &&  !"".equals( etx_pxSumQty.getValStr().toString().trim())){
				ArrayList<String> lList=new ArrayList<String>();;
				float marginQty = 0; //余量
				 pxMultQty += StringUtil.parseFloat(etx_pxSumQty.getValStr()); //拆分量 StringUtil.parseFloat(etx_pxSumQty.getValStr())
				marginQty =  StringUtil.parseFloat(etx_pxQty.getValStr()) -pxMultQty ;
				if( marginQty < 0){
					showErrorMsg("拆分量+已拆分量不能大于扫描总量");
					etx_pxSumQty.setText("");
				}else{
				sumQty += etx_pxSumQty.getValStr()+",";
				allQty = sumQty+marginQty;
			//	sumQty=	sumQty.substring(0, sumQty.length()-1);
				etx_pxMultQty.setText(sumQty);
				etx_yuQty.setText(marginQty+"");
				etx_pxSumQty.setText("");
				}
			}
			
		}
	}
	*/

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit};
	}	
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(list == null || list.size() ==0){
			showErrorMsg("请扫描数据后进行操作");
			return false ;
		}
		if(StringUtil.isEmpty(etx_pxMultQty.getValStr()) || !StringUtil.isEmpty(etx_pxSumQty.getValStr())){
			showErrorMsg("提交失败:拆拼量有数值或已拆拼量为空");
			return false;
		}
		if(istrue == false){
			showErrorMsg("请填写正确数据后提交,或者退出界面重新操作");
			return false ;
		}
		if(!StringUtil.isEmpty(etx_pxSumQty.getValStr())){
			showErrorMsg("请填写正确拆分量后提交");
			return false ;
		}
		return true;
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
		
		return Pxbiz.newPxSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(), jsonStr, etx_pxLot.getValStr(),allQty,etx_pxQty.getValStr()
				,applicationDB.user.getUserDate(),bin,txv_pxUm.getValStr(),cust_part,PO_NBR);
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
			etx_pxLot.reValue();
			etx_pxQty.reValue();
			etx_pxSumQty.reValue();
			etx_pxMultQty.reValue();
			QtyZ = 0;
			String msg=wr.getMessages();
			showSuccessMsg(msg);
			list.clear();
			etx_yuQty.setText("");
			getFocues(etx_pxBar, true);
			dtg_pxList.buildData(list);
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
			etx_yuQty.setText("");
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

