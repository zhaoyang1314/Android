package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.profitles.biz.HandoverItemBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

@SuppressLint("NewApi")
public class HandoverItemActivity extends AppFunActivity {
	private ApplicationDB applicationDB;
	private List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
	private LinearLayout mytable1 ; 	
	private HandoverItemBiz biz;
	private Button  btnQr;

	Map<String, Object> itemmap = new HashMap<String, Object>();
	List<Map<String, Object>> itemList1 = new ArrayList<Map<String,Object>>();
	List<Map<String, Object>> itemList2 = new ArrayList<Map<String,Object>>();
	private String jsonStr="";
	private String XSWO_UKEY="";
	//接收从上一个页面来的生产线
	String line="";
	String opin="";
	String count="0";
	@Override
	protected int getMainBodyLayout() {	
		return R.layout.act_handover_item;	
	}
	@Override
	protected void pageLoad() {
		
		Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
		Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据  
		line = bundle.getString("LINE");//getString()返回指定key的值
		//判断是否由计划产线界面跳转
		if(!StringUtil.isEmpty(bundle.getString("COUNT"))){
			count=bundle.getString("COUNT");
		}
		if(!StringUtil.isEmpty(bundle.getString("XSWO_UKEY"))){
			XSWO_UKEY=bundle.getString("XSWO_UKEY");
		}
		btnQr = (Button) findViewById(R.id.btnQr); // 确认
		mytable1=(LinearLayout) findViewById(R.id.MyTable);
		biz = new HandoverItemBiz();
		if(!count.equals("0")){
			getItemValue();
		}else{
		getLinePage();
		}
		/**
		 * 确认按钮事件
		 */
		btnQr.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						// TODO Auto-generated method stub
						return true;
					}
					@Override
					public Object onGetData() {
						if(count.equals("0")){
							jsonStr="";
							//itemmap参数转json
							List<String> cache = new ArrayList<String>();
							JSONObject obj = new JSONObject(itemList1.get(0)); 
							cache.add(obj.toString());  
							jsonStr = cache.toString(); 
							return biz.confirmButton(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), 
									applicationDB.user.getUserId(),line,jsonStr,XSWO_UKEY);
						}else{
							return biz.updateOpin(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), 
									line,opin);
						}
					}
					@Override
					public void onCallBrack(Object data) {
						WebResponse wr = (WebResponse)data;
						if(wr.isSuccess()){
							showMessage(wr.getMessages());
							if(count.equals("0")){
								Intent intent = new Intent(HandoverItemActivity.this, LineWoListViewActivity.class);
								startActivity(intent);
							}else{
								//跳转下一个页面
								Intent intent = new Intent(HandoverItemActivity.this,  WoListViewActivity.class);		
								Bundle bundle = new Bundle();
								bundle.putString("LINE", line);
								intent.putExtras(bundle);
								startActivity(intent);	
							}
							
						}else{
							showErrorMsg(wr.getMessages());
						}					
					}
				});	
			}
		});
	}

	//获得交接项结果
	private void getItemValue(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public Object onGetData() {
				return biz.getItemValue(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),line);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;		
				if(wrs.isSuccess()){
					//获得的数据库交接项信息
					itemList2 = wrs.getDataToList();
					getLinePage();
				}else{
					showErrorMsg(wrs.getMessages());
					
				}
			}		
		});	 
	}

	// 获得交接项
	private void getLinePage(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public Object onGetData() {
				return biz.handoverItem(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),line);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;		
				if(wrs.isSuccess()){
					//获得的数据库交接项信息
					itemList = wrs.getDataToList();
					onCreate();
				}else{
					showErrorMsg(wrs.getMessages());
					Intent intent = new Intent(HandoverItemActivity.this,  WoListViewActivity.class);		
					Bundle bundle = new Bundle();
					bundle.putString("LINE", line);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}		
		});	 
	}

	protected void onCreate() {
		LinearLayout mytable = (LinearLayout)mytable1;
		mytable.removeAllViews();
		final MyEditText editText1 = new MyEditText(this);
		MyTextView tvn1 = new MyTextView(this);
		for (int i = 0; i < itemList.size(); i++) {
			// 1.集合中每有一条元素，就new一个控件
			LinearLayout mytable1=new LinearLayout(this);
			final int cout=i;
			final MyEditText editText = new MyEditText(this);
			final RadioGroup radiogroup= new RadioGroup(this);
			final RadioButton radiobutton=new RadioButton(this);
			final RadioButton radiobutton2=new RadioButton(this);	
			MyTextView tvn = new MyTextView(this);
			// 2.把信息设置为文本框的内容 
			//序号为两位数时调整布局宽度
			if(i>=9){
				String count=String.valueOf(i+1);
				tvn.setText(count + "   ");
				tvn.setHeight(150);
				tvn.setTextSize(20);
			}else{
				String count=String.valueOf(i+1);
				tvn.setText(count + "     ");
				tvn.setHeight(150);
				tvn.setTextSize(20);
			}
			// 3.把textView设置为线性布局的子节点
			mytable1.addView(tvn);
			MyTextView tv = new MyTextView(this);
			// 2.把信息设置为文本框的内容
			tv.setText(itemList.get(i).get("SHT_ITEM") + "  ");
			tv.setHeight(100);
			tv.setTextSize(20);
			// 3.把textView设置为线性布局的子节点
			mytable1.addView(tv);
			final String itemName=tv.getValStr().trim();
			//文本内容存map
			itemmap.put(itemName, "");
			try {
				//字符类型为逻辑型 加单选框控件
				if (itemList.get(i).get("SHT_DATATYPE").toString().trim()
						.equals("40")) {
					radiobutton.setText("是");
					radiobutton.setId(1);
					radiobutton.setTranslationY(-30);
					radiobutton2.setText("否");
					radiobutton2.setId(2);
					radiobutton2.setTranslationY(-30);
					radiobutton.setHeight(150);
					radiobutton2.setHeight(150);
					radiogroup.addView(radiobutton, 0);
					radiogroup.addView(radiobutton2, 1);
					if(!itemList2.isEmpty()){
						for(int j = 0; j < itemList2.size(); j++){
							if((itemList2.get(j).get("CHASHIFT_NAME")).equals(itemList.get(i).get("SHT_ITEM"))){
								radiogroup.check(Integer.parseInt((String)itemList2.get(j).get("CHASHIFT_VALUE")));
								break;
							}
						}	
					}
					//禁用单选框
					if(count.equals("0")||count.equals("")){
						radiogroup.getChildAt(0).setEnabled(true);
						radiogroup.getChildAt(1).setEnabled(true);
					}else{
						radiogroup.getChildAt(0).setEnabled(false);
						radiogroup.getChildAt(1).setEnabled(false);
					}
					//单选框横向布局
					radiogroup.setOrientation(LinearLayout.HORIZONTAL);
					mytable1.addView(radiogroup);
					//选择框组合监听
					radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener()  {
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							if(checkedId==radiobutton.getId()){
								String count=String.valueOf(checkedId);	
								itemmap.put(itemName, count);	
							}else{
								String count=String.valueOf(checkedId);						 						
								itemmap.put(itemName, count);	
							}
						}
					}); 					
				} else {
					//字符为其他类型加入手输入框
					editText.setTextSize(20);
					editText.setWidth(600);
					editText.setHeight(100);
					editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)}); 
					mytable1.addView(editText);
					//禁用控件判断
					if(count.equals("0")){
						editText.setEnabled(true);
					}else{
						editText.setEnabled(false);
						
					}
					if(!itemList2.isEmpty()){
						for(int j = 0; j < itemList2.size(); j++){
							if((itemList2.get(j).get("CHASHIFT_NAME")).equals(itemList.get(i).get("SHT_ITEM"))){
								editText.setText((String)itemList2.get(j).get("CHASHIFT_VALUE"));	
								break;
							}
						}	
				    }
					editText.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
							//输入内容之前你想做什么	
						}
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							//输入的时候你想做什么
						}
						@Override
						public void afterTextChanged(Editable s) {
							itemmap.put(itemName, editText.getValStr());
						}
					});			
				}
				//子节点横向布局
				mytable1.setOrientation(LinearLayout.HORIZONTAL);			
				//将所有子节点放入一个新的LinearLayout
				mytable.addView(mytable1);
			} catch (Exception e) {
				showErrorMsg(e.getMessage());
			}
		}
		LinearLayout mytable2=new LinearLayout(this);
		tvn1.setText("意见" + "  ");
		tvn1.setHeight(100);
		tvn1.setTextSize(20);
		editText1.setTextSize(20);
		editText1.setWidth(600);
		editText1.setHeight(100);
		mytable2.addView(tvn1);
		mytable2.addView(editText1);
		try {
			mytable.addView(mytable2);
			itemmap.put("意见", editText1.getValStr());
			if(count.equals("0")){
				editText1.setEnabled(false);
				opin=editText1.getValStr();
			}else{
				editText1.setEnabled(true);
			}
			editText1.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					
					//输入内容之前你想做什么	
				}
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					//输入的时候你想做什么
				}
				@Override
				public void afterTextChanged(Editable s) {
						opin=editText1.getValStr();
				}
			});
			
			if(!itemList2.isEmpty()){
				for(int j = 0; j < itemList2.size(); j++){
					if(itemList2.get(j).get("CHASHIFT_NAME").toString().equals("意见")){
						editText1.setText((String)itemList2.get(j).get("CHASHIFT_VALUE"));
						break;
					}
				}	
				opin=editText1.getValStr();
		    }
		} catch (Exception e) {
			showErrorMsg(e.getMessage());
		}
		
		itemList1.add(itemmap);
	}
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Return, ButtonType.Help};
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
