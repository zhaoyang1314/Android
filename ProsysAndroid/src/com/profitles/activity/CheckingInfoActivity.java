package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.profitles.biz.CheckInfoBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.util.StringUtil;
@SuppressLint("NewApi")
public class CheckingInfoActivity extends AppFunActivity {
	private ApplicationDB applicationDB;
	private List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
	private CheckInfoBiz biz;
	private String nbr, WO_NBR, UKEY, txvWkLine;
	private MyDataGrid dtg;
	private Map<String, Object> datas;
	private String qty,tiaoma,OPERENVNM;    //数量   条码   环境
	Map<String, Object> itemmap = new HashMap<String, Object>();
	List<Map<String, Object>> itemList1 = new ArrayList<Map<String,Object>>();
	String state ="0"; //状态=未检 
	private String XSWC_MJCJ="";//当前产线车间
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_checking_view;
	}

	@Override
	protected void pageLoad() {	
		dtg = (MyDataGrid) findViewById(R.id.mdtg_chk1);
		biz = new CheckInfoBiz();
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
		Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
		WO_NBR=bundle.getString("XSWO_NBR");//getString()返回指定key的值  
		UKEY=bundle.getString("XSWO_UKEY");//getString()返回指定key的值  
		txvWkLine=bundle.getString("txvWkLine"); // 产线
		tiaoma = bundle.getString("TIAOMA");    //条码
		qty = bundle.getString("QTY");      //数量
		OPERENVNM=bundle.getString("OPERENVNM");     //环境
		if(!StringUtil.isEmpty(bundle.getString("XSWC_MJCJ"))){
			XSWC_MJCJ=bundle.getString("XSWC_MJCJ");
		}
		getLinePage();	
	}
	
	// 获得未检信息
	private void getLinePage(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public Object onGetData() {
				return biz.getCheckInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),state,txvWkLine);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;		
				if(wrs.isSuccess()){
					//获得的数据库交接项信息
					itemList = wrs.getDataToList();
					
						
						dtg.buildData(itemList);
						setItemListyener();
					onCreate();
				}else{
					showErrorMsg(wrs.getMessages());
				}
			}		
		});	 
	}
	private void setItemListyener() {
		dtg.setOnMyDataGridListener(new OnMyDataGridListener() {
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowDatas) {
				return false;
			}
			public void onItemClick(View view, Object val,  int rowIndex, int colIndex ,Map<String, Object> rowData) {
				datas = rowData;
				//view.setBackgroundColor(Color.YELLOW);
				AlertDialog alert=new AlertDialog.Builder(CheckingInfoActivity.this).create();
				alert.setTitle("SN:"+ StringUtil.isEmpty(rowData.get("QAT_SN")+"", "")); 
				alert.setMessage("零件:"+ StringUtil.isEmpty(rowData.get("QAT_OP_PART")+"", "")+"\r\n"+"日期:"+ StringUtil.isEmpty(rowData.get("QAT_CSP_DATE")+"", ""));
				
				alert.setButton(DialogInterface.BUTTON_POSITIVE,"查自检", new DialogInterface.OnClickListener() {     
			          @Override
			          public void onClick(DialogInterface arg0, int arg1) {
			        	 Intent i = new Intent(CheckingInfoActivity.this , PsiSelfActivity.class);
						 Bundle bundle = new Bundle();
						 bundle.putString("part", StringUtil.isEmpty(datas.get("QAT_OP_PART")+"", ""));
						 bundle.putString("nbr", StringUtil.isEmpty(datas.get("QAT_NBR")+"", ""));
						 i.putExtras(bundle); 
						 startActivity(i); 
			          } 
			      });
				
				alert.show();
			}
		});
		
	}

	
	
	protected void onCreate() {
		//序号存入map
		for (int i = 0; i < itemList.size(); i++) {
			itemmap.put(String.valueOf(i), itemList.get(i).get("QAT_NBR").toString());
		}
		itemList1.add(itemmap);
	}

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Return, ButtonType.Help};
	}	
	
	@Override
	public boolean OnBtnRtnValidata(ButtonType btnType, View v) {
		return true;
	}

	@Override
	public Object OnBtnRtnClick(ButtonType btnType, View v) {
		//序号map转json转string
		nbr="";
		List<String> cache = new ArrayList<String>();
		JSONObject obj = new JSONObject(itemList1.get(0)); 
		cache.add(obj.toString());  
		nbr = cache.toString(); 
		return biz.updateCheckState(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),nbr,state);
	}
	@Override
	public void OnBtnRtnCallBack(Object data) {
		//刷新主页面
		Intent intent = new Intent(CheckingInfoActivity.this, FinshOPActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("XSWO_NBR", WO_NBR);  
		bundle.putString("XSWO_UKEY", UKEY);  
		bundle.putString("RETURNTIAOMA", UKEY);
		bundle.putString("TURNTIAOMA", tiaoma);
		bundle.putString("SPACE", OPERENVNM);
		bundle.putString("TURNQTY", qty);
		bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
		intent.putExtras(bundle);
		startActivity(intent);	
	}
	
	
	
	
	
	
	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
	}

	
}
