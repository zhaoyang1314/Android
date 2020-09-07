package com.profitles.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.PftporcBiz;
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

public class PftporcActivity extends AppFunActivity {

	
	private PftporcBiz Pftporcbiz;
	private MyEditText  etx_PftporcVend,etx_PftporcVend_Name,etx_PftporcPart,
				etx_PftporcPart_Desc,etx_PftporcUM,etx_PftporcLot,etx_PftporcNLot,
				etx_PftporcBox,etx_PftporcUnit,etx_PftporcScat,etx_PftporcSum;
	private MyReadBQ  etx_PftporcCnNbr,etx_PftporcBar,etx_PftporcLocBin;
	private MyDataGrid mdtg_cons;
	private ApplicationDB applicationDB;
	private String lbs = "";
	private List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pftporc;
	}

	@Override
	protected void pageLoad() {
		
		Pftporcbiz=new PftporcBiz();
		etx_PftporcCnNbr =(MyReadBQ) findViewById(R.id.etx_PftporcCnNbr);
		etx_PftporcVend= (MyEditText) findViewById(R.id.etx_PftporcVend);
		etx_PftporcVend_Name=(MyEditText) findViewById(R.id.etx_PftporcVend_Name);
		etx_PftporcBar =(MyReadBQ) findViewById(R.id.etx_PftporcBar);
		etx_PftporcBox =(MyEditText) findViewById(R.id.etx_PftporcBox);
		etx_PftporcScat =(MyEditText) findViewById(R.id.etx_PftporcScat);
		etx_PftporcSum= (MyEditText) findViewById(R.id.etx_PftporcSum);
		etx_PftporcPart= (MyEditText) findViewById(R.id.etx_PftporcPart);
		etx_PftporcPart_Desc=(MyEditText) findViewById(R.id.etx_PftporcPart_Desc);
		etx_PftporcUM= (MyEditText) findViewById(R.id.etx_PftporcUM);
		etx_PftporcUnit=(MyEditText) findViewById(R.id.etx_PftporcUnit);
		etx_PftporcLocBin= (MyReadBQ) findViewById(R.id.etx_PftporcLocBin);
		etx_PftporcLot=(MyEditText) findViewById(R.id.etx_PftporcLot);
		etx_PftporcNLot=(MyEditText) findViewById(R.id.etx_PftporcNLot);
		
		
		mdtg_cons = (MyDataGrid)findViewById(R.id.mdtg_cons);
		etx_PftporcBar.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_PftporcBar.getValStr() && !"".equals( etx_PftporcBar.getValStr().toString().trim()) && !"".equals(etx_PftporcVend.getValStr().toString().trim())){
					checkPftporcCode();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		etx_PftporcLocBin.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_PftporcLocBin.getValStr() && !"".equals( etx_PftporcLocBin.getValStr().toString().trim())){
					checkPftPorcLocBin();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		etx_PftporcCnNbr.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_PftporcCnNbr.getValStr() && !"".equals( etx_PftporcCnNbr.getValStr().toString().trim())){
					checkPftporcCnNbr();
					runClickFun();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
	} 
	
	
	
	/*//从采购策略获取是否开启送货单输入
	PRIVATE VOID UPDATECNNBR(BOOLEAN FLAG) { 
		// TRUE 需要验证送货单   FALSE 不需要验证送货单
		ETX_PFTPORCCNNBR.SETVISIBILITY(FLAG ? VIEW.VISIBLE : VIEW.GONE);
		ETX_PFTPORCCNNBR_GONE.SETVISIBILITY(FLAG ? VIEW.GONE : VIEW.VISIBLE);
	}*/
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_PftporcNbr,etx_PftporcBar);
	}

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//送货单号光标离开事件相对应的代码
		if(etx_PftporcCnNbr.getId()==id){
			runClickFun();
			if(null != etx_PftporcCnNbr.getValStr() && !"".equals( etx_PftporcCnNbr.getValStr().toString().trim())){
				checkPftporcCnNbr();
				runClickFun();
			}else{
				istrue = true;
			}
		}
		
		//扫码光标离开事件相对应的代码
		if(etx_PftporcBar.getId()==id){
			runClickFun();
			if(null != etx_PftporcBar.getValStr() && !"".equals( etx_PftporcBar.getValStr().toString().trim()) && !"".equals(etx_PftporcVend.getValStr().toString().trim())){
				checkPftporcCode();
				runClickFun();
			}else{
				 etx_PftporcPart.reValue();
				 etx_PftporcPart_Desc.reValue();
				 etx_PftporcUM.reValue();
				 etx_PftporcUnit.reValue();
				 etx_PftporcBox.reValue();
				 etx_PftporcScat.reValue();
				 etx_PftporcSum.reValue();
				 etx_PftporcLocBin.reValue();
				 etx_PftporcLot.reValue();
				 etx_PftporcNLot.reValue();
				clearMsg();
				istrue = true;
			}

		}
		//仓储光标离开事件相对应的代码
		if(etx_PftporcLocBin.getId()==id){
			if(null != etx_PftporcLocBin.getValStr() && !"".equals( etx_PftporcLocBin.getValStr().toString().trim())){
				checkPftPorcLocBin();
				runClickFun();
			}else{
				istrue = true;
			}		
		}
		return istrue;
	}
	
	//处理单号校验
	private void checkPftporcCnNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Pftporcbiz.pftPorcCnNbr(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_PftporcCnNbr.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					etx_PftporcVend.setText(map.get("XRCONS_VEND")+"");
					etx_PftporcVend_Name.setText(map.get("VEND_NAME")+"");
					list =(List<Map<String, Object>>) map.get("List");
					mdtg_cons.buildData(list);
					if(StringUtil.isEmpty(map.get("WKFL")+"")){
						getFocues(etx_PftporcBar, true);
					}else{
						showConfirm("该单已经在收货记录,继续收?"+"\n"+"ps:注意取消会删除收货记录", scfListenPorcNbr);
					}
					runClickFun();
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					list = null;
					mdtg_cons.buildData(list);
				}
			}
		});
	}
	
	private OnShowConfirmListen scfListenPorcNbr=new OnShowConfirmListen() {   
		@Override
		public void onConfirmClick() {
			getFocues(etx_PftporcBar, true);
		}
		
		@Override
		public void onCancelClick() {
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return Pftporcbiz.pftPorcCnNbrBydele(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), 
							etx_PftporcCnNbr.getValStr());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String ,Object> map = wr.getDataToMap();
						list =(List<Map<String, Object>>) map.get("List");
						mdtg_cons.buildData(list);
						getFocues(etx_PftporcBar, true);
						showMessage(wr.getMessages());
					}else{
						istrue = false;
						showErrorMsg(wr.getMessages());
					}
				}
			});	
		}
	};	
	
	//处理条码解析
	private void checkPftporcCode() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Pftporcbiz.pftPorcCode(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_PftporcBar.getValStr().toString(), etx_PftporcCnNbr.getValStr().toString(),etx_PftporcVend.getValStr().toString(),applicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					list =(List<Map<String, Object>>) map.get("List");
					mdtg_cons.buildData(list);
					String pDesc= map.get("RFLOT_PART_DESC")+"";
					String um= map.get("RFLOT_UM")+"";
						if("L".equals(map.get("RFPTV_LBS"))){
							lbs = "L";
							etx_PftporcPart.setText(map.get("RFLOT_PART").toString());
							if(!"null".equals(pDesc) && !"".equals(pDesc)){
								etx_PftporcPart_Desc.setText(map.get("RFLOT_PART_DESC").toString());
							}else{
								etx_PftporcPart_Desc.setText("");
							}
							if(!"null".equals(um) && !"".equals(um)){
								etx_PftporcUM.setText(map.get("RFLOT_UM").toString());
							}else{
								etx_PftporcUM.setText("");
							}
							if(StringUtil.isEmpty(map.get("NLOT")+"")){
								etx_PftporcLot.setText(map.get("LOT").toString()); 
							}else{
								etx_PftporcNLot.setText(map.get("NLOT").toString());
							}
							etx_PftporcLocBin.setText(map.get("LocBin").toString());
							etx_PftporcUnit.setText(map.get("RFLOT_MULT_QTY").toString()); //每箱
							etx_PftporcBox.setEnabled(true);
							etx_PftporcScat.setEnabled(true);
							getFocues(etx_PftporcBox, true);
						}else{
							lbs = "";
							etx_PftporcPart.setText(map.get("RFLOT_PART").toString());
							if(!"null".equals(pDesc) && !"".equals(pDesc)){
								etx_PftporcPart_Desc.setText(map.get("RFLOT_PART_DESC").toString());
							}else{
								etx_PftporcPart_Desc.setText("");
							}
							if(!"null".equals(um) && !"".equals(um)){
								etx_PftporcUM.setText(map.get("RFLOT_UM").toString());
							}else{
								etx_PftporcUM.setText("");
							}
							etx_PftporcLocBin.setText(map.get("LocBin").toString());
							if(StringUtil.isEmpty(map.get("NLOT")+"")){
								etx_PftporcLot.setText(map.get("LOT").toString()); 
							}else{
								etx_PftporcNLot.setText(map.get("NLOT").toString());
							}
							etx_PftporcUnit.setText(map.get("RFLOT_MULT_QTY").toString()); //每箱
							etx_PftporcBox.setText("1");  //箱数
							float scat = Float.parseFloat(map.get("RFLOT_SCATTER_QTY")+"");
							if(scat>0){  //散量     总数等于散量
								etx_PftporcScat.setText(map.get("RFLOT_SCATTER_QTY").toString()); //散量
								etx_PftporcSum.setText(map.get("RFLOT_SCATTER_QTY").toString());  //总数
							}else{ //没有散量    总数等于每箱
								etx_PftporcScat.setText("0"); //散量
								etx_PftporcSum.setText(map.get("RFLOT_MULT_QTY").toString());  //总数   
							}
							etx_PftporcBox.setEnabled(false);
							etx_PftporcScat.setEnabled(false);
							getFocues(etx_PftporcLocBin, true);
						}
						runClickFun();
						istrue=true;
					}else{
						 etx_PftporcPart.reValue();
						 etx_PftporcPart_Desc.reValue();
						 etx_PftporcUM.reValue();
						 etx_PftporcUnit.reValue();
						 etx_PftporcBox.reValue();
						 etx_PftporcScat.reValue();
						 etx_PftporcSum.reValue();
						 etx_PftporcLocBin.reValue();
						 etx_PftporcLot.reValue();
						 etx_PftporcNLot.reValue();
						 etx_PftporcBar.reValue();
						 showErrorMsg(wr.getMessages());
						 getFocues(etx_PftporcBar, true);
						 istrue = false;
				}
			}
		});
	}
	
	private void checkPftPorcLocBin(){
		loadDataBase(new IRunDataBaseListens() {  
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Pftporcbiz.pftPorcLocBin(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_PftporcLocBin.getValStr());
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
					showErrorMsg(wr.getMessages());
					etx_PftporcLocBin.reValue();
					getFocues(etx_PftporcLocBin, true);
				}
			}
		});	
	}	
	
	@Override
	protected void onChangedAft(int id, Editable s) {
		if(!StringUtil.isEmpty(lbs)){   //全局变量    lbs不为空说明是按批处理  需要验证箱数 散量
			//箱数光标离开事件相对应的代码
			if(etx_PftporcBox.getId()==id){ 
				if(null != etx_PftporcBox.getValStr() && !"".equals( etx_PftporcBox.getValStr().toString().trim()) && !"".equals(etx_PftporcUnit.getValStr())){
					float Box =StringUtil.parseFloat(etx_PftporcBox.getValStr()); 		//箱数
					float Unit =StringUtil.parseFloat(etx_PftporcUnit.getValStr());   	//每箱
					if("".equals(etx_PftporcCnNbr.getValStr())){  //判断单号是否为空
						if("".equals(etx_PftporcScat.getValStr())){    //判断散量是否为空
							float Sum=(Box*Unit);
							etx_PftporcSum.setText(String.valueOf(Sum));
						}else{
							float Scat =StringUtil.parseFloat(etx_PftporcScat.getValStr());		//散量
							float Sum=(Box*Unit)+Scat;
							etx_PftporcSum.setText(String.valueOf(Sum));	
						}
					}else{
						if("".equals(etx_PftporcScat.getValStr())){    //判断散量是否为空
							float Sum=(Box*Unit);
							etx_PftporcSum.setText(String.valueOf(Sum));
						}else{
							float Scat =StringUtil.parseFloat(etx_PftporcScat.getValStr());		//散量
							float Sum=(Box*Unit)+Scat;
							etx_PftporcSum.setText(String.valueOf(Sum));	
						}
					}
					
				}else{
					istrue = true;
				}

			}
			
			
			//散量光标离开事件相对应的代码
			if(etx_PftporcScat.getId()==id){
				//先判断散量不为空  
				if(null != etx_PftporcScat.getValStr() && !"".equals( etx_PftporcScat.getValStr().toString().trim())){
					//获取散量文本框   强转Folat
					float Scat =StringUtil.parseFloat(etx_PftporcScat.getValStr());	
					//判断每箱量是否为空
					if("".equals(etx_PftporcUnit.getValStr())){  
						//判断散量是否大于等于0   
						if(Scat>=0){
							istrue = false;
							showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
						}else{
							etx_PftporcSum.setText(String.valueOf(Scat));	//总量
							clearMsg();
						}
					}else{     //每箱量不为空
						//获取每箱量文本值
						float Unit =StringUtil.parseFloat(etx_PftporcUnit.getValStr());
						//判断箱数是否为空
						if("".equals(etx_PftporcBox.getValStr())){  
							//散量大于等于每箱量
							if(Scat>=Unit){
								istrue = false;
								showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
								etx_PftporcScat.reValue();
							}else{
								etx_PftporcSum.setText(String.valueOf(Scat));	//总量
								clearMsg();
							}
						}else{   //箱数不为空
							//箱数 /每箱量/散量 都 不为空 判断单号是否为空  
							if("".equals(etx_PftporcCnNbr.getValStr())){  
								float Box =StringUtil.parseFloat(etx_PftporcBox.getValStr()); 		//箱数
								float Sum=(Box*Unit)+Scat;
								if(Scat>=Unit){
									istrue = false;
									showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
									etx_PftporcScat.reValue();
								}else{
									etx_PftporcSum.setText(String.valueOf(Sum));	//总量
									clearMsg();
								}
							}else{  //单号不为空     
								float Box =StringUtil.parseFloat(etx_PftporcBox.getValStr()); 		//箱数
								float Sum=(Box*Unit)+Scat;
								if(Scat>=Unit){
									istrue = false;
									showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
								}else{
									etx_PftporcSum.setText(String.valueOf(Sum));	//总量
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
			
	}	
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Save,ButtonType.Submit};
	}	
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(!"".equals(etx_PftporcCnNbr.getValStr().toString().trim()) && 
		   !"".equals(etx_PftporcBar.getValStr().toString().trim()) && 
		   !"".equals(etx_PftporcVend.getValStr().toString().trim()) && 
		   !"".equals(etx_PftporcPart.getValStr().toString().trim()) && 
		   !"".equals(etx_PftporcBox.getValStr().toString().trim()) && 
		   !"".equals(etx_PftporcLocBin.getValStr().toString().trim()) &&
		   !"".equals(etx_PftporcSum.getValStr().toString().trim())) {
			
			float sumQty = Float.parseFloat(etx_PftporcSum.getValStr());
			float shipQty = 0f;
			float rfQty = 0f;
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map=list.get(i);
				if(etx_PftporcPart.getValStr().toUpperCase().equals(map.get("XRCONSD_PART").toString().toUpperCase())){
					shipQty = Float.parseFloat(map.get("XRCONSD_QTY_SHIP")+"");
					rfQty = Float.parseFloat(map.get("RF_QTY")+"");
				}
			}
			if(sumQty + rfQty <= shipQty){ // 本次扫描总量 + 已扫描量 <= 送货量才能保存
				
			}else{
				showErrorMsg("本次扫描量加上已扫描量不能超过送货量");
				istrue=false;
			}
		}else{
			showErrorMsg(getResources().getString(R.string.Pftporc_consSave_false));
			istrue=false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return Pftporcbiz.PftporcSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_PftporcCnNbr.getValStr(),etx_PftporcVend.getValStr(),
				etx_PftporcBar.getValStr(),etx_PftporcPart.getValStr(),StringUtil.isEmpty(etx_PftporcLot.getValStr()) ? etx_PftporcNLot.getValStr() : etx_PftporcLot.getValStr(),
				etx_PftporcUnit.getValStr(),etx_PftporcScat.getValStr(),etx_PftporcSum.getValStr(),etx_PftporcLocBin.getValStr(),applicationDB.user.getUserId());
	}
	
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			 etx_PftporcBar.reValue();
			 etx_PftporcPart.reValue();
			 etx_PftporcPart_Desc.reValue();
			 etx_PftporcUM.reValue();
			 etx_PftporcUnit.reValue();
			 etx_PftporcSum.reValue();
			 etx_PftporcScat.reValue();
			 etx_PftporcBox.reValue();
			 etx_PftporcLocBin.reValue();
			 etx_PftporcNLot.reValue();
			 etx_PftporcLot.reValue();
			list =(List<Map<String, Object>>) map.get("List");
			mdtg_cons.buildData(list);
			showSuccessMsg(wr.getMessages());
			getFocues(etx_PftporcBar, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_PftporcBar, true);
		}
	}
	
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals(etx_PftporcCnNbr.getValStr().toString().trim()) && !"".equals(etx_PftporcVend.getValStr().toString().trim())){
			if(list!=null && list.size()>0){
				float shipQty = 0f;
				float rfQty = 0f;
				float sumQty = 0f;
				String part = "";
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map=list.get(i);
					shipQty=StringUtil.parseFloat(map.get("XRCONSD_QTY_SHIP")+"");
					rfQty=StringUtil.parseFloat(map.get("RF_QTY")+"");
					sumQty +=StringUtil.parseFloat(map.get("RF_QTY")+"");
					if(shipQty != rfQty){
						part += map.get("XRCONSD_PART")+"\n";
					}
					
				}
				if(sumQty > 0){ //如果sumQty等于0  说明不存在扫描记录     不允许提交
					if(StringUtil.isEmpty(part)){  //送货量   扫描量都相等  直接提交
						
					}else{
						istrue = false;
						showConfirm("送货单零件："+part+" 没有收货完成是否提交?", scfListenpftPorcSub);
					}
				}else{
					showErrorMsg(getResources().getString(R.string.pftporc_sub_false2)); //此送货单没有收货记录不能提交
					getFocues(etx_PftporcCnNbr, true);
					istrue=false;
				}
			}
		}else{
			showErrorMsg(getResources().getString(R.string.pftporc_sub_false));
			getFocues(etx_PftporcCnNbr, true);
			istrue=false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return Pftporcbiz.PftporcSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_PftporcCnNbr.getValStr(),
				etx_PftporcVend.getValStr(),applicationDB.user.getUserId(),applicationDB.user.getUserDate(),"","");
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			etx_PftporcCnNbr.reValue();
			etx_PftporcBar.reValue();
			etx_PftporcVend.reValue();
			etx_PftporcVend_Name.reValue();
			etx_PftporcPart.reValue();
			etx_PftporcPart_Desc.reValue();
			etx_PftporcUM.reValue();
			etx_PftporcLot.reValue();
			etx_PftporcBox.reValue();
			etx_PftporcUnit.reValue();
			etx_PftporcScat.reValue();
			etx_PftporcSum.reValue();
			etx_PftporcLocBin.reValue();
			etx_PftporcNLot.reValue();
			list = null;
			mdtg_cons.buildData(list);
			showSuccessMsg(wr.getMessages());
			getFocues(etx_PftporcCnNbr, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_PftporcBar, true);
		}
	}

	private OnShowConfirmListen scfListenpftPorcSub=new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			initview();
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			getFocues(etx_PftporcBar, true);
		}
	};
	
	
	private View view; 
	private AlertDialog selfdialog;  
	@SuppressWarnings("deprecation")
	public void initview() {
		LayoutInflater inflater = (LayoutInflater) getApplicationContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.act_porc_apph, null);

		final EditText username = (EditText) view.findViewById(R.id.txt_username);
		final EditText password = (EditText) view.findViewById(R.id.txt_password);
		final EditText reson = (EditText) view.findViewById(R.id.txt_reson);
		password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		
		AlertDialog.Builder ad = new AlertDialog.Builder(PftporcActivity.this);
		ad.setView(view);
		ad.setTitle("审批账号登陆");
		selfdialog = ad.create();
		selfdialog.setButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 注意此处是通过反射，修改源代码类中的字段mShowing为true，系统会认为对话框打开  
				try {
					Field field = dialog.getClass().getSuperclass()  
					        .getDeclaredField("mShowing");
					field.setAccessible(true);  
                    field.set(dialog, false); //false为锁定弹出框为,true为非锁定弹出框.
				} catch (Exception e) {
				} 
				
				if(username.getText().toString().trim().equals("")){
					Toast.makeText(PftporcActivity.this, "账号不能为空!",  
	                          Toast.LENGTH_SHORT).show();  
				}else if(password.getText().toString().trim().equals("")){
					Toast.makeText(PftporcActivity.this, "密码不能为空!",  
	                          Toast.LENGTH_SHORT).show();  
				}else if(reson.getText().toString().trim().equals("")){
					Toast.makeText(PftporcActivity.this, "理由不能为空!",  
	                          Toast.LENGTH_SHORT).show();  
				}else if(username.getText().toString().trim().equals(ApplicationDB.user.getUserId())){
					Toast.makeText(PftporcActivity.this, "审批账号不能与当前登录账号一致!",  
	                          Toast.LENGTH_SHORT).show();
				}else{
					UserAuthentication(username.getText().toString(),password.getText().toString(),reson.getText().toString(),dialog);
				}
			}

		});
		selfdialog.setButton2("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Field field = dialog.getClass().getSuperclass()  
					        .getDeclaredField("mShowing");
					field.setAccessible(true);  
                    field.set(dialog, true);
				} catch (Exception e) {
			}  
                dialog.dismiss(); 
				getFocues(etx_PftporcBar, true);
			}
		});
		selfdialog.show(); //显示创建的弹出框
		selfdialog.setCancelable(false); //设置弹出框(dialog)是否为模态，false表示模态，true表示非模态 
	}
		

	// 将输入的字符转换为"*"
	public class AsteriskPasswordTransformationMethod extends
			PasswordTransformationMethod {
		@Override
		public CharSequence getTransformation(CharSequence source, View view) {
			return new PasswordCharSequence(source);
		}

		private class PasswordCharSequence implements CharSequence {
			private CharSequence mSource;

			public PasswordCharSequence(CharSequence source) {
				mSource = source; // Store char sequence
			}

			public char charAt(int index) {
				return '*'; // This is the important part
			}

			public int length() {
				return mSource.length(); // Return default
			}

			public CharSequence subSequence(int start, int end) {
				return mSource.subSequence(start, end); // Return default
			}
		}
	}
	
	/**
	 * 审批用户验证
	 */
	public void UserAuthentication(final String user ,final String password,final String reson,final DialogInterface dialog){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return Pftporcbiz.porcUserAuthentication(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),user, password);
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(!wrs.isSuccess()){
					Toast.makeText(PftporcActivity.this, wrs.getMessages(),  
	                          Toast.LENGTH_SHORT).show();
				}else{
					try {
						Field ff = dialog.getClass().getSuperclass()  
						        .getDeclaredField("mShowing");
						ff.setAccessible(true);  
						ff.set(dialog, true);	//解锁弹出框
					} catch (Exception e) {
						e.printStackTrace();
					}
					dialog.dismiss();	//关闭弹出框
					getFocues(etx_PftporcBar, true);
					Submit(user,reson);
				}
			}
		});
	}
	
	public void Submit(final String apphUser,final String reson) {   //询问框  点确定
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Pftporcbiz.PftporcSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_PftporcCnNbr.getValStr(),
						etx_PftporcVend.getValStr(),applicationDB.user.getUserId(),applicationDB.user.getUserDate(),apphUser,reson);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					etx_PftporcCnNbr.reValue();
					etx_PftporcBar.reValue();
					etx_PftporcVend.reValue();
					etx_PftporcVend_Name.reValue();
					etx_PftporcPart.reValue();
					etx_PftporcPart_Desc.reValue();
					etx_PftporcUM.reValue();
					etx_PftporcLot.reValue();
					etx_PftporcBox.reValue();
					etx_PftporcUnit.reValue();
					etx_PftporcScat.reValue();
					etx_PftporcSum.reValue();
					etx_PftporcLocBin.reValue();
					etx_PftporcNLot.reValue();
					list = null;
					mdtg_cons.buildData(list);
					showSuccessMsg(wr.getMessages());
					getFocues(etx_PftporcCnNbr, true);
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etx_PftporcBar, true);
				}
			}
		});	
	}
	
	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
}
