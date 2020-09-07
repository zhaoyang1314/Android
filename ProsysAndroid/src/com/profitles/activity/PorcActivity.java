package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost.OnTabChangeListener;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.PorcBiz;
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

public class PorcActivity extends AppFunActivity {
	
	private PorcBiz Porcbiz;
	private MyEditText  etx_PorcBox,etx_PorcPart_Desc,
						etx_PorcScat,etx_PorcSum,etx_PorcUM,
						etx_PorcUnit,etx_PorcVend,etx_PorcVend_Name,etx_PorcLot2;
	private MyReadBQ  etx_PorcNbr,etx_PorcCnNbr,etx_PorcPart,etx_PorcLocBin,etx_PorcLot;
	private MyTabHost tbh_PorcOne;
	private MyDataGrid dtg_PorcDetail , dtg_PorcBoxQuery;
	private ApplicationDB applicationDB;
	private List<Map<String , Object>> trList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> trBoxList = new ArrayList<Map<String , Object>>();
	private String isTrue = "";
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_porc;
	}

	@Override
	protected void pageLoad() {
		tbh_PorcOne=((MyTabHost) this.findViewById(R.id.tbh_PorcOne));
		tbh_PorcOne.setup();
		tbh_PorcOne.setOnTabChangedListener(new OnTabChangeListener() {
			@Override	
			public void onTabChanged(String tabId) {
				if(tabId.equals("BoxQuery")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							dtg_PorcBoxQuery.buildData(trBoxList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}
			}
		});
		
		  Porcbiz=new PorcBiz();
		  etx_PorcNbr =(MyReadBQ) findViewById(R.id.etx_PorcNbr);
		  etx_PorcCnNbr =(MyReadBQ) findViewById(R.id.etx_PorcCnNbr);
		  etx_PorcBox =(MyEditText) findViewById(R.id.etx_PorcBox);
		  etx_PorcScat =(MyEditText) findViewById(R.id.etx_PorcScat);
		  etx_PorcSum= (MyEditText) findViewById(R.id.etx_PorcSum);
		  etx_PorcVend= (MyEditText) findViewById(R.id.etx_PorcVend);
		  etx_PorcVend_Name=(MyEditText) findViewById(R.id.etx_PorcVend_Name);
		  etx_PorcPart= (MyReadBQ) findViewById(R.id.etx_PorcPart);
		  etx_PorcPart_Desc=(MyEditText) findViewById(R.id.etx_PorcPart_Desc);
		  etx_PorcUM= (MyEditText) findViewById(R.id.etx_PorcUM);
		  etx_PorcUnit=(MyEditText) findViewById(R.id.etx_PorcUnit);
		  etx_PorcLocBin= (MyReadBQ) findViewById(R.id.etx_PorcLocBin);
		  etx_PorcLot= (MyReadBQ) findViewById(R.id.etx_PorcLot);
		  etx_PorcLot2= (MyEditText) findViewById(R.id.etx_PorcLot2);
		dtg_PorcDetail = (MyDataGrid)findViewById(R.id.dtg_PorcDetail);
		
		dtg_PorcBoxQuery = (MyDataGrid)findViewById(R.id.dtg_PorcBoxQuery);
		
		etx_PorcLot.setVisibility(View.VISIBLE);
		etx_PorcLot2.setVisibility(View.GONE);
		isTrue = applicationDB.Ctrl.getString("RFC_PO_QAD", "0.0").toString();
		if("0.0".equals(applicationDB.Ctrl.getString("RFC_PO_QAD", "0.0").toString())){
			etx_PorcVend.setReadOnly(false);
		}else{
			etx_PorcVend.setReadOnly(true);
		}
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
		if(etx_PorcNbr.getId()==id){
			runClickFun();
			if(null != etx_PorcNbr.getValStr() && !"".equals( etx_PorcNbr.getValStr().toString().trim())){
				if("0.0".equals(applicationDB.Ctrl.getString("RFC_PO_QAD", "0.0").toString())){
					runClickFun();
				}else{
					checkPorcNbr();
					runClickFun();
				}
				
			}else{
				istrue = true;
			}
		}
		//供方光标离开事件相对应的代码
		if(etx_PorcVend.getId()==id){
			if(null != etx_PorcVend.getValStr() && !"".equals( etx_PorcVend.getValStr().toString().trim())){
				checkPorcVend();
				runClickFun();
			}else{
				istrue = true;
			}		
		}
		
		//零件光标离开事件相对应的代码
		if(etx_PorcPart.getId()==id){
			if(null != etx_PorcPart.getValStr() && !"".equals( etx_PorcPart.getValStr().toString().trim())){
				checkPorcPart();
				runClickFun();
			}else{
				istrue = true;
			}		
		}
		
		//仓储光标离开事件相对应的代码
		if(etx_PorcLocBin.getId()==id){
			if(null != etx_PorcLocBin.getValStr() && !"".equals( etx_PorcLocBin.getValStr().toString().trim())){
				checkPorcLocBin();
				runClickFun();
			}else{
				istrue = true;
			}		
		}
		if(etx_PorcScat.getId() == id){
			runClickFun();
		}
		if(etx_PorcBox.getId() == id){
			runClickFun();
		}
		if(etx_PorcLot.getId() == id){
			/*if(null != etx_PorcLot.getValStr() && !"".equals( etx_PorcLot.getValStr().toString().trim())){
				checkPorcLot();
				runClickFun();
				
			}else{
				istrue = true;
			}	*/
			runClickFun();
		}
		if(etx_PorcCnNbr.getId() == id){
			runClickFun();
		}
		if(etx_PorcUnit.getId() == id){
			if(null != etx_PorcUnit.getValStr() && !"".equals( etx_PorcUnit.getValStr().toString().trim())){
				if(null != etx_PorcBox.getValStr() && !"".equals( etx_PorcBox.getValStr().toString().trim())){
					float Box =StringUtil.parseFloat(etx_PorcBox.getValStr()); 		//箱数
					float Unit =StringUtil.parseFloat(etx_PorcUnit.getValStr());   	//每箱
					if("".equals(etx_PorcScat.getValStr())){    //判断散量是否为空
						float Sum=(Box*Unit);
						etx_PorcSum.setText(String.valueOf(Sum));
					}else{
						float Scat =StringUtil.parseFloat(etx_PorcScat.getValStr());		//散量
						float Sum=(Box*Unit)+Scat;
						etx_PorcSum.setText(String.valueOf(Sum));	
					}

				}
				runClickFun();
			}else{
				istrue = true;
			}	
		}
		return istrue;
	}
	

	//同步QAD采购单号
	private void checkPorcNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Porcbiz.porcNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_PorcNbr.getValStr(),etx_PorcCnNbr.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					etx_PorcVend.setText(map.get("poVend")+"");
					etx_PorcVend_Name.setText(map.get("cnVend")+"");
					if(map.get("trList") != null){
						trList=(List<Map<String, Object>>) map.get("trList");
						dtg_PorcDetail.buildData(trList);
					}else if(map.get("trBoxList") != null){
						trBoxList=(List<Map<String, Object>>) map.get("trBoxList");
						dtg_PorcBoxQuery.buildData(trBoxList);
					}
				}else{
					istrue = false;
					if(!StringUtil.isEmpty(wr.getDataToMap())){
						etx_PorcVend.setText(wr.getDataToMap().get("poVend").toString());
					}else{
						etx_PorcVend.reValue();
						etx_PorcVend_Name.reValue();
						dtg_PorcDetail.buildData(trList);
						dtg_PorcBoxQuery.buildData(trBoxList);
						getFocues(etx_PorcNbr, true);
					}
					showErrorMsg(wr.getMessages());
				}
			
			}
		});
	}
	
	private void checkPorcVend(){
		loadDataBase(new IRunDataBaseListens() {    
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Porcbiz.porcVend(applicationDB.user.getUserDmain(), etx_PorcVend.getValStr());	
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					String vend_Desc=(String) wr.getWData();
					etx_PorcVend_Name.setText(vend_Desc);
					clearMsg();
					istrue = true;
					runClickFun();
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etx_PorcVend, true);
				}
			}
		});				
	}
	
	private void checkPorcPart(){
		loadDataBase(new IRunDataBaseListens() {    
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Porcbiz.porcPart(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(), etx_PorcPart.getValStr(),etx_PorcVend.getValStr(),
						etx_PorcNbr.getValStr(),etx_PorcCnNbr.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					//获取文本
					MyEditText desc=(MyEditText) findViewById(R.id.etx_PorcPart_Desc);
					//设置EditText的显示方式为多行文本输入  
					desc.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
					//文本显示的位置在EditText的最上方  
					desc.setGravity(Gravity.TOP);  
					desc.setText(map.get("PART_DESC").toString());  
					//改变默认的单行模式  
					desc.setSingleLine(false);  
					etx_PorcUM.setText(map.get("XSPT_UM").toString());
					etx_PorcUnit.setText(map.get("RFPTV_MULT_BOX").toString());
					etx_PorcLocBin.setText(map.get("LocBin").toString());
					if("PFT_AUTO_LOT".equals(map.get("RFPTV_LOT_MODE").toString())){
						etx_PorcLot2.setVisibility(View.VISIBLE);
						etx_PorcLot.setVisibility(View.GONE);
					}
					clearMsg();
					istrue = true;
					runClickFun();
				}else{
					showErrorMsg(wr.getMessages());
					etx_PorcPart_Desc.reValue();
					etx_PorcUM.reValue();
					etx_PorcUnit.reValue();
					etx_PorcLocBin.reValue();
					getFocues(etx_PorcPart, true);
				}
			}
		});				
	}
	private void checkPorcLocBin(){
		loadDataBase(new IRunDataBaseListens() {  
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Porcbiz.porcLocBin(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_PorcLocBin.getValStr());
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
					istrue = false;
					showErrorMsg(getResources().getString(R.string.pk_getfalse));
					getFocues(etx_PorcLocBin, true);
				}
			}
		});	
	}
	
	private void checkPorcLot(){
		loadDataBase(new IRunDataBaseListens() {  
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Porcbiz.porcLot(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_PorcNbr.getValStr(),etx_PorcCnNbr.getValStr(),
						etx_PorcPart.getValStr(),etx_PorcLot.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					runClickFun();
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					getFocues(etx_PorcLot, true);
				}
			}
		});	
	}
	
	@Override
	protected void onChangedAft(int id, Editable s) {
		//箱数光标离开事件相对应的代码
		if(etx_PorcBox.getId()==id){  	
			if(null != etx_PorcBox.getValStr() && !"".equals( etx_PorcBox.getValStr().toString().trim()) && !"".equals(etx_PorcUnit.getValStr())){
				float Box =StringUtil.parseFloat(etx_PorcBox.getValStr()); 		//箱数
				float Unit =StringUtil.parseFloat(etx_PorcUnit.getValStr());   	//每箱
				if("".equals(etx_PorcNbr.getValStr())){  //判断单号是否为空
					if("".equals(etx_PorcScat.getValStr())){    //判断散量是否为空
						float Sum=(Box*Unit);
						etx_PorcSum.setText(String.valueOf(Sum));
					}else{
						float Scat =StringUtil.parseFloat(etx_PorcScat.getValStr());		//散量
						float Sum=(Box*Unit)+Scat;
						etx_PorcSum.setText(String.valueOf(Sum));	
					}
				}else{
					if("".equals(etx_PorcScat.getValStr())){    //判断散量是否为空
						float Sum=(Box*Unit);
						etx_PorcSum.setText(String.valueOf(Sum));
					}else{
						float Scat =StringUtil.parseFloat(etx_PorcScat.getValStr());		//散量
						float Sum=(Box*Unit)+Scat;
						etx_PorcSum.setText(String.valueOf(Sum));	
					}
				}
				
			}else{
				istrue = true;
			}

		}
		
		
		//散量光标离开事件相对应的代码
		if(etx_PorcScat.getId()==id){
			//先判断散量不为空  
			if(null != etx_PorcScat.getValStr() && !"".equals( etx_PorcScat.getValStr().toString().trim())){
				//获取散量文本框   强转Folat
				float Scat =StringUtil.parseFloat(etx_PorcScat.getValStr());	
				//判断每箱量是否为空
				if("".equals(etx_PorcUnit.getValStr())){  
					//判断散量是否大于等于0   
					if(Scat>=0){
						istrue = false;
						showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
					}else{
						etx_PorcSum.setText(String.valueOf(Scat));	//总量
						clearMsg();
					}
				}else{     //每箱量不为空
					//获取每箱量文本值
					float Unit =StringUtil.parseFloat(etx_PorcUnit.getValStr());
					//判断箱数是否为空
					if("".equals(etx_PorcBox.getValStr())){  
						//散量大于等于每箱量
						if(Scat>=Unit){
							istrue = false;
							showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
						}else{
							etx_PorcSum.setText(String.valueOf(Scat));	//总量
							clearMsg();
						}
					}else{   //箱数不为空
						//箱数 /每箱量/散量 都 不为空 判断单号是否为空  
						if("".equals(etx_PorcNbr.getValStr())){  
							float Box =StringUtil.parseFloat(etx_PorcBox.getValStr()); 		//箱数
							float Sum=(Box*Unit)+Scat;
							if(Scat>=Unit){
								istrue = false;
								showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
							}else{
								etx_PorcSum.setText(String.valueOf(Sum));	//总量
								clearMsg();
							}
						}else{  //单号不为空     
							float Box =StringUtil.parseFloat(etx_PorcBox.getValStr()); 		//箱数
							float Sum=(Box*Unit)+Scat;
							if(Scat>=Unit){
								istrue = false;
								showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
							}else{
								etx_PorcSum.setText(String.valueOf(Sum));	//总量
								clearMsg();
							}
								
						}
					}
				}
			}else{
				istrue = true;
			}
		}		
	}
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Save,ButtonType.Help};
	}	
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(!"".equals(etx_PorcVend.getValStr().toString().trim()) && !"".equals(etx_PorcPart.getValStr().toString().trim()) 
				&& !"".equals(etx_PorcLocBin.getValStr().toString().trim()) && !"0.0".equals(etx_PorcSum.getValStr().toString().trim()) 
				&& !"".equals(etx_PorcSum.getValStr().toString().trim())
				&& !"".equals(etx_PorcUnit.getValStr().toString().trim()) ){
		
		}else{
			showErrorMsg(getResources().getString(R.string.Porc_consSave_false));
			istrue=false;
		}
		if("".equals(etx_PorcLot.getValStr().toString().trim())&&"".equals(etx_PorcLot2.getValStr().toString().trim())){
			showErrorMsg("批号必填!");
			istrue=false;
		}
		if(!(isTrue.equals("0.0"))){
			if(etx_PorcNbr.getValStr().toString().trim().equals("")){
				showErrorMsg("单号必填!");
				istrue=false;
			}
			
		}
		return istrue;
	}

	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return Porcbiz.porcSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_PorcLocBin.getValStr(),
				etx_PorcPart.getValStr(), etx_PorcVend.getValStr(),etx_PorcSum.getValStr(), etx_PorcUM.getValStr(),etx_PorcNbr.getValStr(),
				"CnrcActivity", applicationDB.user.getUserId(), "maxnumber", "3", "5",etx_PorcLot.getValStr(),applicationDB.Ctrl.getString("RFC_PO_QAD", "0.0").toString(),
				etx_PorcCnNbr.getValStr(),etx_PorcUnit.getValStr(),applicationDB.user.getUserDate());
	}
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			//if("".equals(etx_PorcNbr.getValStr().toString())){
				//etx_PorcNbr.setText(map.get("PorcNbr").toString());
			//}
			//if("".equals(etx_PorcLot.getValStr().toString())){
				//etx_PorcLot.setText(map.get("PorcLot").toString());
			//}
			 etx_PorcPart.reValue();
			 etx_PorcPart_Desc.reValue();
			 etx_PorcUM.reValue();
			 etx_PorcUnit.reValue();
			 etx_PorcSum.reValue();
			 etx_PorcScat.reValue();
			 etx_PorcBox.reValue();
			 etx_PorcLocBin.reValue();
			 etx_PorcLot.reValue();
			 etx_PorcPart.setReadOnly(false);
			 etx_PorcVend.setReadOnly(false);
			 etx_PorcLot.setVisibility(View.VISIBLE);
			 etx_PorcLot2.setVisibility(View.GONE);
			String msg=wr.getMessages();
			showSuccessMsg(msg);
			trList=(List<Map<String, Object>>) map.get("trList");
			trBoxList=(List<Map<String, Object>>) map.get("trBoxList");
			//dtg_PorcDetail.clearData();
			//dtg_PorcBoxQuery.clearData();
			dtg_PorcDetail.buildData(trList);
			getFocues(etx_PorcPart, true);
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
		}
		
		super.OnBtnSaveCallBack(data);
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
