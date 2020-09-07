package com.profitles.activity;

import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.profitles.biz.UpStatusBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

public class UpStatusActivity extends AppFunActivity {
	
	private UpStatusBiz Upiz;
	private MyReadBQ  etx_scancBar,etx_cancu;
	private MyEditText etx_scanStatus,etxLocFpsBox,etx_scanType; 
	private ApplicationDB applicationDB;
	private CheckBox cb_close, cb_open,cb_plcz; 
	private String type = "";
	private String isPLcz =""; //是否批量操作
	private int boxqty = 0 , resqty=0  ;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_upstatus;
	}

	@Override
	protected void pageLoad() {
		Upiz=new UpStatusBiz();
		  etx_scancBar =(MyReadBQ) findViewById(R.id.etx_scancBar); //条码
		  etx_cancu =(MyReadBQ) findViewById(R.id.etx_cancu);        //仓储
		  etx_scanStatus  = (MyEditText) findViewById(R.id.etx_scanStatus); //条码状态
		  cb_close=(CheckBox)findViewById(R.id.cb_close); //关闭
		  cb_open=(CheckBox)findViewById(R.id.cb_open);  //开启
		//  cb_plcz = (CheckBox) findViewById(R.id.etx_plcz); //批量操作
		  etxLocFpsBox= (MyEditText) findViewById(R.id.etx_locFpsBox); //已扫描数量
		  etx_scanType = (MyEditText) findViewById(R.id.etx_scanType); //是否存在
		  etx_scancBar.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!StringUtil.isEmpty(etx_scancBar.getValStr())){
						try {
							String scan = etx_scancBar.getValStr().toString();
							/*String PART,SHNSCAN;
							String[] split = scan.split("\\+");
							PART = split[0];
							SHNSCAN = split[5];
							String[] splitSHNS = SHNSCAN.split("[$]");
							if(splitSHNS.length ==9 ){
									for (int i = 1; i < splitSHNS.length; i++) {
										 if (splitSHNS[i].toUpperCase().substring(0,1).equals("P")){
											 PART = splitSHNS[i].substring(1).toString();//零件编码(去掉‘P'标记)
						                 }
									}
							 }*/
							String PART = "";
							String[] scans = scan.split("[$]");
							for (int i = 1; i < scans.length; i++) {
								 if (scans[i].startsWith("P")){
									 PART = scans[i].substring(1).toString();//零件编码(去掉‘P'标记)
				                 }
							}
							isCzBar();
						} catch (Exception e) {
							showErrorMsg("条码格式不正确，请重新扫码");
							etx_scancBar.reValue();
							getFocues(etx_scancBar, true);
						}
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				public void afterTextChanged(Editable s) {
				}
			});
		  
		  
		  cb_close.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ //关闭事件

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if(isChecked){
						cb_close.setChecked(true);
						cb_open.setChecked(false);
						type = "close";
					}else{
						cb_close.setChecked(false);
					}
					
				} 
		    	
		    });
		  
		  cb_open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ //开启事件

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if(isChecked){
						cb_close.setChecked(false);
						cb_open.setChecked(true);
						type = "open";
					}else {
						cb_open.setChecked(false);
					}
				} 
		    	
		    });
		  /*cb_plcz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ //开启事件

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if(isChecked){
						isPLcz = "PL"; //批量操作
					}else {
						isPLcz = "NPL";//不批量操作
					}
				} 
		    	
		    });*/
		  
	} 
	
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_PorcNbr,etx_PorcBar);
	}
	

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		
		return istrue;
	}
	private void isCzBar() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
			if(!cb_open.isChecked() && (!cb_close.isChecked())){
				showErrorMsg("请选择对应的操作状态");
				etx_scancBar.reValue();
				return false;
			}	
			if(StringUtil.isEmpty(etx_scancBar.getValStr())){
				showErrorMsg("标签不能为空");
				return false;
			}
				return true;
			}
			@Override
			public Object onGetData() {
				return Upiz.isCzBar(ApplicationDB.user.getUserDmain(),  etx_scancBar.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
				Map<String, Object> map=wr.getDataToMap();
				if(!StringUtil.isEmpty(map)){
					etx_cancu.setText(map.get("loc")+"");
				}
				getFocues(etx_cancu, true);
				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					etx_scancBar.setText("");
					etx_cancu.reValue();
					getFocues(etx_scancBar, true);
				}
			}
		});
	}	
	

	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Submit,ButtonType.Help};
	}

	@Override
	public Object OnBtnHelpClick(ButtonType btnType, View v) {
		showSuccessMsg(R.string.help_ok);
		return  null ;
	}

	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!cb_open.isChecked() && (!cb_close.isChecked())){
			showErrorMsg("请选择对应的操作状态");
			etx_scancBar.reValue();
			return false;
		}
		if(StringUtil.isEmpty(etx_scancBar.getValStr())){
			showErrorMsg("标签不能为空");
			return false;
		}
		if(StringUtil.isEmpty(etx_cancu.getValStr()+"")){
			showErrorMsg("仓储不能为空");
			return false;
		}
		return true;
	}
	
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return Upiz.upStatus(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite(), etx_scancBar.getValStr(),type,ApplicationDB.user.getUserName(),etx_cancu.getValStr(),isPLcz,ApplicationDB.user.getUserId());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse) data;
		if(wr.isSuccess()){
		Map<String, Object> map=wr.getDataToMap();
		etx_scanStatus.setText(map.get("RFLOT_SCAN_STATUS")+"");
		boxqty += 1;
		etx_scancBar.setText("");
		etx_cancu.reValue();
		String valueOf = String.valueOf(boxqty);
		etxLocFpsBox.setText(valueOf);
		etx_scanType.setText(map.get("RFLOT_SCAN_IS")+"");
		showSuccessMsg(wr.getMessages());
		getFocues(etx_scancBar, true);
		}else{
			istrue = false;
			showErrorMsg(wr.getMessages());
			// etx_scancBar.setText("");
			// etx_cancu.reValue();
			getFocues(etx_scancBar, true);
		}
		
	}
	
	
	@Override
	protected void unLockNbr() {

	}


	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
	
}
