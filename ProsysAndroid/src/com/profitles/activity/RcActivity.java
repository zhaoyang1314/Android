package com.profitles.activity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.profitles.biz.RcBiz;
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
import com.profitles.framwork.util.StringUtil;

public class RcActivity extends AppFunActivity {
	
	private RcBiz Rcbiz;
	private MyEditText  etx_RcVend,etx_RcVend_Name,etx_RcPart,etx_RcPart_Desc,etx_RcUM,
			etx_RcLot,etx_RcBox,etx_RcUnit,etx_RcScat,etx_RcSum,etx_RcBoxQty,etx_RcLocBin;
	private MyReadBQ  etx_RcBar;
	private MyDataGrid mdtg_cons;
	private ApplicationDB applicationDB;
	private String lbs = "";
	private List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_rc;
	}

	@Override
	protected void pageLoad() {
		
		Rcbiz=new RcBiz();
		etx_RcBar =(MyReadBQ) findViewById(R.id.etx_RcBar);
		etx_RcVend= (MyEditText) findViewById(R.id.etx_RcVend);
		etx_RcVend_Name=(MyEditText) findViewById(R.id.etx_RcVend_Name);
		etx_RcBar =(MyReadBQ) findViewById(R.id.etx_RcBar);
		etx_RcBox =(MyEditText) findViewById(R.id.etx_RcBox);
		etx_RcScat =(MyEditText) findViewById(R.id.etx_RcScat);
		etx_RcSum= (MyEditText) findViewById(R.id.etx_RcSum);
		etx_RcPart= (MyEditText) findViewById(R.id.etx_RcPart);
		etx_RcPart_Desc=(MyEditText) findViewById(R.id.etx_RcPart_Desc);
		etx_RcUM= (MyEditText) findViewById(R.id.etx_RcUM);
		etx_RcUnit=(MyEditText) findViewById(R.id.etx_RcUnit);
		etx_RcBoxQty= (MyEditText) findViewById(R.id.etx_RcBoxQty);
		etx_RcLocBin= (MyEditText) findViewById(R.id.etx_RcLocBin);
		etx_RcLot=(MyEditText) findViewById(R.id.etx_RcLot);
		mdtg_cons = (MyDataGrid)findViewById(R.id.mdtg_cons);
		
		etx_RcBox.setBackgroundColor(Color.TRANSPARENT);
		etx_RcScat.setBackgroundColor(Color.TRANSPARENT);

		etx_RcBar.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_RcBar.getValStr() && !"".equals( etx_RcBar.getValStr().toString().trim())){
					checkRcCode();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Rcbiz.RcLoad(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					Map map = wr.getDataToMap();
					list =(List<Map<String, Object>>) map.get("list");
					mdtg_cons.buildData(list);
					etx_RcBoxQty.setText(map.get("BoxQty").toString());
					getFocues(etx_RcBar, true);
				}else{
					list = null;
					mdtg_cons.buildData(list);
					etx_RcBoxQty.reValue();
					getFocues(etx_RcBar, true);
				}
				runClickFun();
			}
		});
	} 
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_RcNbr,etx_RcBar);
	}

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;		

		return istrue;
	}	
	
	//处理条码解析
	private void checkRcCode() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Rcbiz.RcCode(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_RcBar.getValStr().toString(),applicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map map = wr.getDataToMap();
					if("1".equals(map.get("isHavaAct").toString())){
						showConfirm(wr.getMessages(), scfListenRc);
					}else{
						if("L".equals(map.get("LBS").toString())){
							lbs = "L";
							etx_RcBox.setBackgroundColor(Color.WHITE);
							etx_RcScat.setBackgroundColor(Color.WHITE);
							etx_RcVend.setText(map.get("RFLOT_VEND").toString());
							etx_RcVend_Name.setText(map.get("RFLOT_VEND_NAME").toString());
							etx_RcPart.setText(map.get("RFLOT_PART").toString());
							etx_RcPart_Desc.setText(map.get("RFLOT_PART_DESC").toString());
							etx_RcUM.setText(map.get("RFLOT_UM").toString());
							etx_RcLot.setText(map.get("RFLOT_LOT").toString());
							etx_RcUnit.setText(map.get("RFLOT_MULT_QTY").toString());
							etx_RcLocBin.setText(map.get("LOCBIN").toString());
							getFocues(etx_RcBox, true);
						}else{
							lbs = "";
							list =(List<Map<String, Object>>) map.get("list");
							mdtg_cons.buildData(list);	
							etx_RcBox.setBackgroundColor(Color.TRANSPARENT);
							etx_RcScat.setBackgroundColor(Color.TRANSPARENT);
							etx_RcBoxQty.setText(map.get("BoxQty").toString());
							etx_RcVend.setText(map.get("RFLOT_VEND").toString());
							etx_RcVend_Name.setText(map.get("RFLOT_VEND_NAME").toString());
							etx_RcPart.setText(map.get("RFLOT_PART").toString());
							etx_RcPart_Desc.setText(map.get("RFLOT_PART_DESC").toString());
							etx_RcUM.setText(map.get("RFLOT_UM").toString());
							etx_RcLot.setText(map.get("RFLOT_LOT").toString());
							etx_RcBox.setText("1");
							etx_RcUnit.setText(map.get("RFLOT_MULT_QTY").toString());
							etx_RcScat.setText(map.get("RFLOT_SCATTER_QTY").toString());
							etx_RcSum.setText(map.get("RFLOT_MULT_QTY").toString());
							etx_RcLocBin.setText(map.get("LOCBIN").toString());
							etx_RcBar.reValue();
							showMessage(wr.getMessages());
							getFocues(etx_RcBar, true);
						}
					}
				}else{
					etx_RcBar.reValue();
					showErrorMsg(wr.getMessages());
					getFocues(etx_RcBar, true);
				}
			}
		});
	}
	
	@Override
	protected void onChangedAft(int id, Editable s) {
		if(!StringUtil.isEmpty(lbs)){   //全局变量    lbs不为空说明是按批处理  需要验证箱数 散量
			//箱数光标离开事件相对应的代码
			if(etx_RcBox.getId()==id){ 
				if(null != etx_RcBox.getValStr() && !"".equals( etx_RcBox.getValStr().toString().trim()) && !"".equals(etx_RcUnit.getValStr())){
					float Box =StringUtil.parseFloat(etx_RcBox.getValStr()); 		//箱数
					float Unit =StringUtil.parseFloat(etx_RcUnit.getValStr());   	//每箱
					if("".equals(etx_RcScat.getValStr())){    //判断散量是否为空
						float Sum=(Box*Unit);
						etx_RcSum.setText(String.valueOf(Sum));
					}else{
						float Scat =StringUtil.parseFloat(etx_RcScat.getValStr());		//散量
						float Sum=(Box*Unit)+Scat;
						etx_RcSum.setText(String.valueOf(Sum));	
					}
				}else{
					istrue = true;
				}
			}
			
			//散量光标离开事件相对应的代码
			if(etx_RcScat.getId()==id){
				//先判断散量不为空  
				if(null != etx_RcScat.getValStr() && !"".equals( etx_RcScat.getValStr().toString().trim())){
					//获取散量文本框   强转Folat
					float Scat =StringUtil.parseFloat(etx_RcScat.getValStr());	
					//判断每箱量是否为空
					if("".equals(etx_RcUnit.getValStr())){  
						//判断散量是否大于等于0   
						if(Scat>=0){
							istrue = false;
							showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
						}else{
							etx_RcSum.setText(String.valueOf(Scat));	//总量
							clearMsg();
						}
					}else{     //每箱量不为空
						//获取每箱量文本值
						float Unit =StringUtil.parseFloat(etx_RcUnit.getValStr());
						//判断箱数是否为空
						if("".equals(etx_RcBox.getValStr())){  
							//散量大于等于每箱量
							if(Scat>=Unit){
								istrue = false;
								showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
								etx_RcScat.reValue();
							}else{
								etx_RcSum.setText(String.valueOf(Scat));	//总量
								clearMsg();
							}
						}else{ 
							float Box =StringUtil.parseFloat(etx_RcBox.getValStr()); 		//箱数
							float Sum=(Box*Unit)+Scat;
							if(Scat>=Unit){
								istrue = false;
								showErrorMsg(getResources().getString(R.string.cnrc_Scat_false));
								etx_RcSum.setText(String.valueOf(Box*Unit));	//总量
								etx_RcScat.reValue();
							}else{
								etx_RcSum.setText(String.valueOf(Sum));	//总量
								clearMsg();
							}
						}
					}
				}else{
					istrue = true;
				}
			}	
		}
			
	}		
	
	private OnShowConfirmListen scfListenRc =new OnShowConfirmListen() { 
		@Override
		public void onConfirmClick() {   //询问框  点确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return Rcbiz.RcCode_Dele(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_RcBar.getValStr().toString(), applicationDB.user.getUserId());
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr=(WebResponse) data;
					if(wr.isSuccess()){
						Map<String, Object> map=wr.getDataToMap();
						list =(List<Map<String, Object>>) map.get("list");
						mdtg_cons.buildData(list);
						etx_RcBoxQty.setText(map.get("BoxQty").toString());
						etx_RcBar.reValue();
						etx_RcVend.reValue();
						etx_RcVend_Name.reValue();
						etx_RcPart.reValue();
						etx_RcPart_Desc.reValue();
						etx_RcUM.reValue();
						etx_RcLot.reValue();
						etx_RcBox.reValue();
						etx_RcUnit.reValue();
						etx_RcScat.reValue();
						etx_RcSum.reValue();
						etx_RcLocBin.reValue();
						showMessage(wr.getMessages());
						getFocues(etx_RcBar, true);
					  	runClickFun();
					}else{
						etx_RcBar.reValue();
					  	showErrorMsg(wr.getMessages());
						getFocues(etx_RcBar, true);
					}
				}
			});	
		}
		
		@Override
		public void onCancelClick() {  //询问框  点取消
			etx_RcBar.reValue();
			//取消则回到该栏位位置选中该栏位值
			getFocues(etx_RcBar, true);
		}
	};		
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Save,ButtonType.Submit};
	}
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(!StringUtil.isEmpty(lbs)){
			if(!"".equals(etx_RcBar.getValStr().toString().trim()) && 
			   !"".equals(etx_RcVend.getValStr().toString().trim()) && 
			   !"".equals(etx_RcPart.getValStr().toString().trim()) && 
			   !"".equals(etx_RcBox.getValStr().toString().trim()) && 
			   !"".equals(etx_RcLocBin.getValStr().toString().trim()) &&
			   !"".equals(etx_RcSum.getValStr().toString().trim())) {
						
			}else{
				showErrorMsg("条码,供方,零件,箱数,每箱,总量,仓储不能为空!");
				istrue=false;
			}
		}else{
			istrue=false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return Rcbiz.RcSave(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),
				etx_RcBar.getValStr(),etx_RcBox.getValStr(),etx_RcScat.getValStr(),etx_RcSum.getValStr(),
				etx_RcLocBin.getValStr(),applicationDB.user.getUserId());
	}
	
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			 etx_RcBar.reValue();
			 etx_RcPart.reValue();
			 etx_RcPart_Desc.reValue();
			 etx_RcVend.reValue();
			 etx_RcVend_Name.reValue();
			 etx_RcUM.reValue();
			 etx_RcUnit.reValue();
			 etx_RcSum.reValue();
			 etx_RcScat.reValue();
			 etx_RcBox.reValue();
			 etx_RcLocBin.reValue();
			 etx_RcLot.reValue();
			 lbs = "";
			 etx_RcBox.setBackgroundColor(Color.TRANSPARENT);
			 etx_RcScat.setBackgroundColor(Color.TRANSPARENT);
			 list =(List<Map<String, Object>>) map.get("list");
			 mdtg_cons.buildData(list);
			 etx_RcBoxQty.setText(map.get("BoxQty").toString());
			 showSuccessMsg(wr.getMessages());
			 getFocues(etx_RcBar, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_RcBar, true);
		}
	}	
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(list!=null && list.size()>0){
			istrue=true;
		}else{
			showErrorMsg("不存在扫描记录，无法提交！");
			getFocues(etx_RcBar, true);
			istrue=false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return Rcbiz.RcSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),applicationDB.user.getUserId(),applicationDB.user.getUserDate());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			Map<String, Object> map=wr.getDataToMap();
			etx_RcBar.reValue();
			etx_RcVend.reValue();
			etx_RcVend_Name.reValue();
			etx_RcPart.reValue();
			etx_RcPart_Desc.reValue();
			etx_RcUM.reValue();
			etx_RcLot.reValue();
			etx_RcBox.reValue();
			etx_RcUnit.reValue();
			etx_RcScat.reValue();
			etx_RcSum.reValue();
			etx_RcBoxQty.reValue();
			etx_RcLocBin.reValue();
			list = null;
			mdtg_cons.buildData(list);
			showSuccessMsg(wr.getMessages());
			getFocues(etx_RcBar, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_RcBar, true);
		}
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
}
