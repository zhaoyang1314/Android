package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.SelfiViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyCheckBox;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyLinearLayout;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyRelativeLayout;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;
import com.profitles.framwork.view.Good;
import com.profitles.framwork.view.Goods;
import com.profitles.framwork.view.TableAdapter;
import com.profitles.framwork.view.TableAdapters;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

@SuppressWarnings(value = { "all" })
public class SelfInsertActivity extends AppFunActivity {

	private String domain, site, userid, part, cust, dates, desc, users, nbr, scan, sn;

	private MyEditText etxpart, etxuser, extdate, etxcustpart, etxdesc,extoutslt,extsizeslt,extslt, extsn;

	private MySpinner extresult, spn_RScansType, extfisttype;

	private MyReadBQ etxscan;
	
	private CheckBox Blockbox;
	
	private SelfiViewBiz selBiz;
	private List<Map<String , Object>> tList = new ArrayList<Map<String,Object>>();
	private List<Map<String , Object>> CheckList = new ArrayList<Map<String,Object>>();
	private Map sMap = new HashMap();
	
	private String falg="0";
	@Override
	protected void pageLoad() {
		selBiz = new SelfiViewBiz();
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		userid = ApplicationDB.user.getUserId();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		part = bundle.getString("part");
		users = bundle.getString("users");
		dates = bundle.getString("dates");
		cust = bundle.getString("cust");
		desc = bundle.getString("desc");
		nbr =  bundle.getString("nbr");
		scan = bundle.getString("scan");
		sn = bundle.getString("sn");
		extresult = (MySpinner) findViewById(R.id.txv_ck_type);
		spn_RScansType = (MySpinner) findViewById(R.id.txv_Rscantype);
		extoutslt = (MyEditText) findViewById(R.id.ext_out_slt);
		extsizeslt = (MyEditText) findViewById(R.id.ext_size_slt);
		extslt = (MyEditText) findViewById(R.id.ext_slt);
		extsn = (MyEditText) findViewById(R.id.ext_csp_sn);
		extfisttype = (MySpinner) findViewById(R.id.ext_fist_type); 
		Blockbox = (CheckBox) findViewById(R.id.checkBlock); 
//		extresult.addItem("来料检验", "IQC");
		extresult.addItem("过程检验", "PQC");
//		extresult.addItem("出货检验", "OQC");
		etxscan = (MyReadBQ) findViewById(R.id.etx_scan);
		/*extfisttype.addItem("G3", "G3");
		extfisttype.addItem("ACCRUA", "ACCRUA");
		extfisttype.addItem("外观检测", "外观检测");*/
		setContent();
		getCheckType();
		etxscan.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!etxscan.getValStr().equals("")) {
					getScan();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		extresult.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(!StringUtil.isEmpty(extresult.getValStr())){
					getRscanType();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		spn_RScansType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		extfisttype.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		Blockbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(etxscan.getValStr().length() < 37){
					showErrorMsg("非按序管理物料不能进行此操作");
					Blockbox.setChecked(false);
				}
				if(Blockbox.isChecked()){
					falg = "1";
					showMessage("选中将进行标签BLOCK操作");
				}else{
					falg = "0";
				}
			} //开启事件
			});

	}

	private void setContent() {

		etxpart = (MyEditText) findViewById(R.id.etx_txv_part);
		etxpart.setText(part);
		etxuser = (MyEditText) findViewById(R.id.ext_csp_user);
		etxuser.setText(users);
		extdate = (MyEditText) findViewById(R.id.ext_date);
		extdate.setText(dates);
		etxcustpart = (MyEditText) findViewById(R.id.etx_cust_part);
		etxcustpart.setText(cust);
		etxdesc = (MyEditText) findViewById(R.id.etx_part_desc);
		etxdesc.setText(desc);
		etxscan.setText(scan);
		extsn.setText(sn);
	}

	private void getRscanType(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return selBiz.getRscanType(domain, extresult.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					if(tList.size() > 0) tList.clear();
					if(!StringUtil.isEmpty(spn_RScansType.getValStr())){
						spn_RScansType.clearItems();
					}
					spn_RScansType.addItem("请选择拆分类型","0");
					Map<String ,Object> map = wr.getDataToMap();
					tList = (List<Map<String, Object>>) map.get("LIST");
					for (int i = 0; i < tList.size(); i++) {
						 sMap = tList.get(i);
						spn_RScansType.addItem(sMap.get("NAME")+"",sMap.get("CODE")+"");
					}
					spn_RScansType.setSelection(1,true);
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	// 加载监测机型
	private void getCheckType(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return selBiz.getCheckType(domain);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					if(CheckList.size() > 0) CheckList.clear();
					if(!StringUtil.isEmpty(extfisttype.getValStr())){
						extfisttype.clearItems();
					}
					extfisttype.addItem("请选择检测机型","0");
					Map<String ,Object> map = wr.getDataToMap();
					CheckList = (List<Map<String, Object>>) map.get("LIST");
					for (int i = 0; i < CheckList.size(); i++) {
						 sMap = CheckList.get(i);
						 extfisttype.addItem(sMap.get("NAME")+"",sMap.get("CODE")+"");
					}
					extfisttype.setSelection(1,true);
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	
	@Override
	protected int getMainBodyLayout() {

		return R.layout.act_psi_item_test;
	}

	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub

	}
	
	private void getScan(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return selBiz.getSelfScan(domain, site, userid,etxscan.getValStr().trim());
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
				}else{
					etxscan.reValue();
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[] { ButtonType.Save, ButtonType.Return,
				ButtonType.Help };
	}
	
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		/*if(StringUtil.isEmpty(etxscan.getValStr())){
			showErrorMsg("请扫描条码后操作");
			return false;
		}*/
		return true;
	}
	
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return selBiz.getSelfSave(domain, site, userid,ApplicationDB.user.getUserDate(),etxscan.getValStr().trim(), part, users,dates, cust, desc, extoutslt.getValStr().trim(), extsizeslt.getValStr().trim()
				, extslt.getValStr().trim(), spn_RScansType.getValStr().trim(),nbr,extfisttype.getValStr().trim(), extresult.getValStr(), sn, falg);
	}
	
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			etxscan.reValue();
			etxpart.setText("");
			etxuser.setText("");
			extdate.setText("");
			etxcustpart.setText("");
			extoutslt.setText("");
			extsizeslt.setText("");
			extslt.setText("");
			etxdesc.setText("");
			extsn.setText("");
			showSuccessMsg(wr.getMessages());
			Intent intent = new Intent(SelfInsertActivity.this, SelfActivity.class);
				startActivity(intent);	
		}else{
			showErrorMsg(wr.getMessages());
		}
	}
	
	public Object OnBtnRtnClick(ButtonType btnType, View v){
		Intent intent = new Intent(SelfInsertActivity.this, SelfActivity.class);
		startActivity(intent);
		return null;
	}
	
	@Override
	public String getAppVersion() {

		return ApplicationDB.user.getUserDate();
	}

}