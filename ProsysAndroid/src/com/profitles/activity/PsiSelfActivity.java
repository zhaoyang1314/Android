package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.profitles.biz.SelfiViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyLinearLayout;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

@SuppressWarnings(value = { "all" })
public class PsiSelfActivity  extends AppFunActivity{

	private String domain, site,userid,part,users, nbr;
	private SelfiViewBiz selBiz;
	private MyLinearLayout webView;
	private List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> codeList = new ArrayList<Map<String,Object>>();
	private Map<String,List<Map<String, Object>>> mapList = new HashMap<String,List<Map<String, Object>>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_psi_test;
	}

	@Override
	protected void pageLoad() {
		selBiz = new SelfiViewBiz();
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid      = ApplicationDB.user.getUserId();
		webView = (MyLinearLayout)findViewById(R.id.list);
		Intent intent = getIntent(); 
		Bundle bundle = intent.getExtras();
		part = bundle.getString("part");
		users = bundle.getString("users");
		nbr = bundle.getString("nbr");
		getItem();
	}

	private void getItem(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return selBiz.getPsiSelfItem(domain, site, userid,part, nbr);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					mapList = wr.getDataToMapList();
					dateList=mapList.get("PsiList");
					codeList=mapList.get("ProCode");
					if(!StringUtil.isEmpty(dateList)&&dateList.size()>0){
						onCreate();
					}else{
						showErrorMsg("零件"+part+"对应的自检数据为空!");
					}
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}

	protected void onCreate() {
		MyLinearLayout mainLinerLayout = (MyLinearLayout) webView;
		MyTextView functionEn = new MyTextView(this);
		functionEn.setText("备注："+setfunction().getText());
		//		MyEditText function = setfunction();
		mainLinerLayout.addView(functionEn);
		//		mainLinerLayout.addView(function);
		MyTextView REK = new MyTextView(this);
		REK.setText("说明："+(dateList.get(0).get("IQCWO_REMARK")!=null?dateList.get(0).get("IQCWO_REMARK"):""));

		for (int i = 0; i < dateList.size(); i++) {
			MyTextView qcp_titleType = new MyTextView(this);
			qcp_titleType.setText("自检项:");
			MyEditText qcp_type = new MyEditText(this);
			qcp_type.setText(dateList.get(i).get("IQCWO_ITEM")+"");
			qcp_type.setWidth(270);
			qcp_type.setReadOnly(true);
			qcp_type.setBackgroundColor(Color.TRANSPARENT);

			MyTextView TypeTool = new MyTextView(this);
			TypeTool.setText("量具:");
			MyEditText tool = new MyEditText(this);
			tool.setText(dateList.get(i).get("IQCWO_CLASS")+"");
			tool.setWidth(250);
			tool.setReadOnly(true);
			tool.setBackgroundColor(Color.TRANSPARENT);

			MyTextView qcp_titleDefault = new MyTextView(this);
			qcp_titleDefault.setText("    测试值: "+dateList.get(i).get("IQCWO_VALUE"));
			qcp_titleDefault.setWidth(280);
			MyTextView result = new MyTextView(this);
			result.setText("结果: "+dateList.get(i).get("IQCWO_RESULT"));
			MyEditText option = new MyEditText(this);
			option.setReadOnly(true);
			option.setBackgroundColor(Color.TRANSPARENT);
			option.setWidth(120);
			MyLinearLayout line=new MyLinearLayout(this);
			line.addView(qcp_titleType);
			line.addView(qcp_type);
			line.addView(TypeTool);
			line.addView(tool);
			line.addView(qcp_titleDefault);
			line.addView(result);
			line.addView(option);
			mainLinerLayout.addView(line);
		}
		mainLinerLayout.addView(REK);
	}

	private MyEditText setfunction() {
		final MyEditText function =  new MyEditText(this);
		function.setReadOnly(true);
		function.setTextSize(15);
		for (int i = 0; i < codeList.size(); i++) {
			if(codeList.get(i).get("RJP_ID")==dateList.get(0).get("IQCWO_SPACE")){
				function.setText(codeList.get(i).get("RJP_NAME")+"");
			}
		}
		return function;	
	}

	@Override
	protected void unLockNbr() {


	}

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Return};
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}


}