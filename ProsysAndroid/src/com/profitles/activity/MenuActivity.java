package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.profitles.biz.MenuBiz;
import com.profitles.framwork.activity.base.BaseActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyButton;
import com.profitles.framwork.cusviews.view.MyGridView;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class MenuActivity extends BaseActivity {	

	private String homeId = "20";//33
	private MenuBiz mbiz;
	private MyGridView gdvMenuItems;
	private MyButton btnMenuRtn, btnMenuHome, btnReLogin;
	private List<String> histId;
    private SharedPreferences sp;  
	private Map<String, List<Map<String,Object>>> menuCache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_menu);

		mbiz = new MenuBiz();
		histId = new ArrayList<String>();
		gdvMenuItems = (MyGridView)findViewById(R.id.gdvMenuItems);
		btnMenuRtn = (MyButton)findViewById(R.id.btnMenuRtn);
		btnMenuHome = (MyButton)findViewById(R.id.btnMenuHome);
		btnReLogin = (MyButton)findViewById(R.id.btnReLogin);
		menuCache = new HashMap<String, List<Map<String,Object>>>();
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
		pageLoad();
	}
	
	protected void pageLoad() {
		btnMenuRtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(histId.size() > 0){
					drawMenus(histId.get(histId.size() - 1));
					histId.remove(histId.size() - 1);
				}
			}
		});
		
		btnMenuHome.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				drawMenus(homeId);
			}
		});
		
		btnReLogin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				 Editor editor = sp.edit(); 
				 editor.putString("status", "false");
				 editor.commit(); 
				forwordFinish(LoginActivity.class);
			}
		});
		
		gdvMenuItems.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MyTextView itemTager = (MyTextView)view.findViewById(R.id.txvMenuItemTager);
				MyTextView itemId = (MyTextView)view.findViewById(R.id.txvMenuItemId);
				MyTextView itemName = (MyTextView)view.findViewById(R.id.txvMenuItem);
				if(!forword(itemTager.getValStr(), itemId.getValStr() + " " + itemName.getValStr())){
					drawMenus(itemId.getValStr());
					MyTextView itemParentId = (MyTextView)view.findViewById(R.id.txvMenuItemParentId);
					histId.add(itemParentId.getValStr());
				}
			}
		});
		
		drawMenus(homeId);
	}
	
	private boolean forword(String actStrName, String summary){
		if(!(StringUtil.isEmpty(actStrName) || Constants.EnptyMenuTagert.equals(actStrName))){
			try {
				ApplicationDB.MenuName = summary;
				super.forword((Class<Activity>)Class.forName(Constants.ActivityPackage+"."+actStrName));
				return true;
			} catch (Exception e) {
				showErrorMsg(e.getMessage()+":"+actStrName);
			}
		}
		return false;
	}
	
	private void drawMenus(final String menuId){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				if(!menuCache.containsKey(menuId)){
					menuCache.put(menuId, loadIco((List<Map<String,Object>>)mbiz.getMenuByUid(menuId, ApplicationDB.user.getUserId(),ApplicationDB.user.getUserDmain())));
				}
				return menuCache.get(menuId);
			}
			
			@Override
			public void onCallBrack(Object data) {
				List<Map<String, Object>> ls = (List<Map<String,Object>>)data;
				if(ls.size() == 0){
					showErrorMsg(R.string.menuNoChild); return;
				}
		        //新建适配器
		        String [] from ={"image", "name", "id", "parantid", "target"};
		        int [] to = {R.id.imgMenuItem, R.id.txvMenuItem, R.id.txvMenuItemId, R.id.txvMenuItemParentId, R.id.txvMenuItemTager};
		        SimpleAdapter sim_adapter = new SimpleAdapter(MenuActivity.this, ls, R.layout.act_menu_item, from, to);
		        //配置适配器
		        gdvMenuItems.setAdapter(sim_adapter);
			}
		});
	}
	
	private List<Map<String, Object>> loadIco(List<Map<String, Object>> data){
		 for(int i=0;i<data.size();i++){
			 Map<String, Object> map = data.get(i);
			 int image = getRecouseByName(map.get("image")+"", "drawable");
			 data.get(i).put("image", image == 0 ? R.drawable.menu_item : image);
        }
        return data;
	}
}
