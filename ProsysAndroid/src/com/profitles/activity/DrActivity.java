package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.text.Editable;
import android.view.View;

import com.profitles.biz.DrBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;

public class DrActivity extends AppFunActivity {
	
	private DrBiz Drbiz;
	//private MyTextView txv_pxUm; 
	private MyEditText  etx_cnrtNbr,etx_cnrtVend,etx_cnrtVendName,etx_busnumber;
	private MyReadBQ etxCrnrtNbr; 
	private ApplicationDB applicationDB;
	private String jsonStr="";
	private List<Map<String , Object>> list = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> tList = new ArrayList<Map<String , Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_dr;
	}

	@Override
	protected void pageLoad() {
		  Drbiz=new DrBiz();
		  etxCrnrtNbr = (MyReadBQ) findViewById(R.id.etx_cnrtNbr);
		  //etx_cnrtNbr =(MyEditText) findViewById(R.id.etx_cnrtNbr);
		  etx_cnrtVend =(MyEditText) findViewById(R.id.etx_lapVend);
		  etx_busnumber =(MyEditText) findViewById(R.id.etx_busnumber);
		  etx_cnrtVendName=(MyEditText) findViewById(R.id.etx_lapName);
	} 
	
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_PorcNbr,etx_PorcBar);
	}

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//扫码光标离开事件相对应的代码
		if(etxCrnrtNbr.getId()==id){
			runClickFun();
			if(null != etxCrnrtNbr.getValStr() && !"".equals( etxCrnrtNbr.getValStr().toString().trim())){
				checkLapCode();
			}
		}	
		return istrue ;
	}
	
	
	
	//处理条码解析
	private void checkLapCode() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Drbiz.drCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxCrnrtNbr.getValStr(),applicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					if(null !=map.get("XRCONS_RGST_ID")&& !"".equals(map.get("XRCONS_RGST_ID"))||null !=map.get("XRCONS_ARV_DATE")&& !"".equals(map.get("XRCONS_ARV_DATE"))){
						etxCrnrtNbr.reValue();
						showMessage("此单号已完成过扫描操作!");
						getFocues(etxCrnrtNbr, true);
						istrue = false;
					}else{
						etx_cnrtVend.setText(map.get("XRCONS_VEND")+"");
						etx_busnumber.setText(map.get("XRCONS_CARNO")+"");
//						if(null !=map.get("VENDNAME")&& !"".equals(map.get("VENDNAME"))){
//							etx_cnrtVendName.setText(map.get("VENDNAME")+"");
//						}else{
//							etx_cnrtVendName.setText("");
//						}
						
						etx_cnrtVendName.setText(map.get("VENDNAME") == null ? "" : map.get("VENDNAME")+"");
						//etx_cnrtVend.setEnabled(false);
						etx_busnumber.setEnabled(false);
						etx_cnrtVendName.setEnabled(false);
					}
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etxCrnrtNbr, true);
					istrue = false;
				}
			}
		});
	}

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit};
	}	
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return Drbiz.drSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),applicationDB.user.getUserId(),etxCrnrtNbr.getValStr());
	
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxCrnrtNbr.reValue();
			etx_cnrtVend.setText("");
			etx_busnumber.setText("");
			etx_cnrtVendName.setText("");
			showMessage(wr.getMessages());
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
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
