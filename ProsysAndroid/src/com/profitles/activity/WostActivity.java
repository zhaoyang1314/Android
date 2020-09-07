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
import com.profitles.biz.WostBiz;
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
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class WostActivity extends AppFunActivity {

	private WostBiz Wostbiz;
	private MyEditText  etx_wostWoNbr,etx_wostShift,etx_wostPart,
			etx_wostPartDesc,etx_wostLot,etx_wostWkctr,etx_wostLine,
			etx_wostLocBin,etx_wostWoDate;
	private MyTextView  txv_wostYjScan,txv_wostWmBox,txv_wostLocBin;
	private MyReadBQ  etx_wostScan,etx_wostYjScan,etx_wostWmBox;
	private ApplicationDB applicationDB;
	private String cPart = "",uKey = "",shift = "";
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_wost;
	}

	@Override
	protected void pageLoad() {
		Wostbiz=new WostBiz();
		etx_wostScan = (MyReadBQ) findViewById(R.id.etx_wostScan);
		etx_wostYjScan = (MyReadBQ) findViewById(R.id.etx_wostYjScan);
		etx_wostWmBox = (MyReadBQ) findViewById(R.id.etx_wostWmBox);
		
		etx_wostWoDate = (MyEditText) findViewById(R.id.etx_wostWoDate);
		etx_wostWoNbr = (MyEditText) findViewById(R.id.etx_wostWoNbr);
		etx_wostShift = (MyEditText) findViewById(R.id.etx_wostShift);
		etx_wostPart = (MyEditText) findViewById(R.id.etx_wostPart);
		etx_wostPartDesc = (MyEditText) findViewById(R.id.etx_wostPartDesc);
		etx_wostLot = (MyEditText) findViewById(R.id.etx_wostLot);
		etx_wostWkctr = (MyEditText) findViewById(R.id.etx_wostWkctr);
		etx_wostLine = (MyEditText) findViewById(R.id.etx_wostLine);
		etx_wostLocBin = (MyEditText) findViewById(R.id.etx_wostLocBin);
		
		txv_wostYjScan=(MyTextView) findViewById(R.id.txv_wostYjScan);
		txv_wostWmBox=(MyTextView) findViewById(R.id.txv_wostWmBox);
		txv_wostLocBin=(MyTextView) findViewById(R.id.txv_wostLocBin);
		
		if(applicationDB.WoC != null){
			String sample = applicationDB.WoC.getString("PFTWOC_SAMPLE","0");
			String noFull = applicationDB.WoC.getString("PFTWOC_NO_FULL","0");
			if("1".equals(sample)){
				txv_wostYjScan.setVisibility(View.VISIBLE);
				etx_wostYjScan.setVisibility(View.VISIBLE); //生产策略为YES 显示etx_wostYjScan 		样件标签
			}else{
				txv_wostYjScan.setVisibility(View.GONE);
				etx_wostYjScan.setVisibility(View.GONE);
			}
			if("1".equals(noFull)){
				txv_wostWmBox.setVisibility(View.VISIBLE);
				txv_wostLocBin.setVisibility(View.VISIBLE);
				etx_wostWmBox.setVisibility(View.VISIBLE);
				etx_wostLocBin.setVisibility(View.VISIBLE);
			}else{
				txv_wostWmBox.setVisibility(View.GONE);
				txv_wostLocBin.setVisibility(View.GONE);
				etx_wostWmBox.setVisibility(View.GONE);
				etx_wostLocBin.setVisibility(View.GONE);
			}
		}else{
			txv_wostYjScan.setVisibility(View.GONE); //暂时隐藏etx_wostYjScan 		    样件标签
			txv_wostWmBox.setVisibility(View.GONE); //暂时隐藏etx_wostWmBox        上次未满箱号
			txv_wostLocBin.setVisibility(View.GONE); //暂时隐藏etx_wostLocBin       仓储
			etx_wostYjScan.setVisibility(View.GONE); //暂时隐藏etx_wostYjScan 		    样件标签
			etx_wostWmBox.setVisibility(View.GONE); //暂时隐藏etx_wostWmBox        上次未满箱号
			etx_wostLocBin.setVisibility(View.GONE); //暂时隐藏etx_wostLocBin       仓储
		}
		
		etx_wostScan.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_wostScan.getValStr() && !"".equals( etx_wostScan.getValStr().toString().trim())){
					checkWostScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
	} 
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_PftporcNbr,etx_PftporcBar);
	}

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		if(etx_wostScan.getId()==id){
			runClickFun();
			if(null != etx_wostScan.getValStr() && !"".equals( etx_wostScan.getValStr().toString().trim())){
				//checkWostScan();
				runClickFun();
			}else{
				istrue = true;
			}
		}
		
		return istrue;
	}
	
	//处理条码解析
	private void checkWostScan() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Wostbiz.wostScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_wostScan.getValStr().toString(),applicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					etx_wostWoNbr.setText(map.get("NO")+"");
					etx_wostShift.setText(map.get("SHIFT")+"");
					shift = map.get("ID")+"";
					etx_wostPart.setText(map.get("PART")+"");
					etx_wostPartDesc.setText(map.get("DESC")+"");
					etx_wostLot.setText(map.get("LOT")+"");
					etx_wostWkctr.setText(map.get("WORKSHOP")+"");
					etx_wostLine.setText(map.get("LINE")+"");
					etx_wostWoDate.setText(map.get("DATE")+"");
					cPart = map.get("CUST_PART")+"";
					uKey = map.get("UKEY")+"";
					runClickFun();
					istrue=true;
				}else{
					etx_wostWoNbr.reValue();
					etx_wostShift.reValue();
					shift = "";
					etx_wostPart.reValue();
					etx_wostPartDesc.reValue();
					etx_wostLot.reValue();
					etx_wostWkctr.reValue();
					etx_wostLine.reValue();
					etx_wostWoDate.reValue();
					cPart = "";
					uKey = "";
					showErrorMsg(wr.getMessages());
					getFocues(etx_wostScan, true);
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
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals(etx_wostScan.getValStr().toString().trim()) && !"".equals(etx_wostPart.getValStr().toString().trim())
				&& !"".equals(etx_wostWkctr.getValStr().toString().trim()) && !"".equals(etx_wostLine.getValStr().toString().trim())){
			
		}else{
			showErrorMsg("请先扫描正确的首件标签");
			istrue=false;
		}	
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return Wostbiz.wostSub(applicationDB.user.getUserDmain(), 
								applicationDB.user.getUserSite(),
								applicationDB.user.getUserId(),
								etx_wostScan.getValStr(),
								etx_wostWoNbr.getValStr(),
								shift,
								etx_wostPart.getValStr(),
								etx_wostLot.getValStr(),
								etx_wostWkctr.getValStr(),
								etx_wostLine.getValStr(),
								cPart,
								uKey,
								etx_wostWoDate.getValStr());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etx_wostScan.reValue();
			etx_wostWoNbr.reValue();
			etx_wostShift.reValue();
			shift = "";
			etx_wostPart.reValue();
			etx_wostPartDesc.reValue();
			etx_wostLot.reValue();
			etx_wostWkctr.reValue();
			etx_wostLine.reValue();
			cPart = "";
			uKey = "";
			etx_wostWoDate.reValue();
			showSuccessMsg(wr.getMessages());
			getFocues(etx_wostScan, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_wostScan, true);
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
