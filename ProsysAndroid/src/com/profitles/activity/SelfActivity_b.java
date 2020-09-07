package com.profitles.activity;
import java.util.ArrayList;
import java.util.List;


import java.util.Map;

import com.profitles.biz.SelfiViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyLinearLayout;
import com.profitles.framwork.cusviews.view.MyRelativeLayout;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;
import com.profitles.framwork.view.Good;
import com.profitles.framwork.view.Goods;
import com.profitles.framwork.view.TableAdapter;
import com.profitles.framwork.view.TableAdapters;

import java.util.ArrayList;    

import android.app.Activity;    
import android.graphics.Color;
import android.os.Bundle;    
import android.view.View;    
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;    
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;    
import android.widget.LinearLayout.LayoutParams;    
import android.widget.Toast;
@SuppressWarnings(value = { "all" })
public class SelfActivity_b extends AppFunActivity {
	private SelfiViewBiz selBiz;
	List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
	List<Map<String, Object>> itemLists = new ArrayList<Map<String,Object>>();
	//private  ListView lv;
	private String domain, site,userid;
	
	private MyRelativeLayout rltMainBody;

	
	@Override
	protected void pageLoad() {
		selBiz = new SelfiViewBiz();
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid      = ApplicationDB.user.getUserId();
		getItem();
	}


	@Override
	protected int getMainBodyLayout() {
        return R.layout.act_self;
	}

	@Override
	protected void unLockNbr() {
		
	}

	@Override
	public String getAppVersion() {
		//return ApplicationDB.user.getUserDate();
		return null;
	}
	private void getItem(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return selBiz.getSelfItem(domain, site,userid);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					itemList = wr.getDataToList();
					if(!StringUtil.isEmpty(itemList)&&itemList.size()>0){
						setXml(itemList);
					}
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	private void getItems(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return selBiz.getSelfItems(domain, site,userid);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					itemLists = wr.getDataToList();
					if(!StringUtil.isEmpty(itemLists)&&itemLists.size()>0){
						setXmls(itemLists);
					}
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	private void setXml(List<Map<String, Object>> itemList) {
		/*setContentView(R.layout.act_self);    
        lv = (ListView) this.findViewById(R.id.ListView01);    
        ArrayList<TableRow> table = new ArrayList<TableRow>();
        TableCell[] heads = new TableCell[2];
        int wid = this.getWindowManager().getDefaultDisplay().getWidth()/heads.length;
        heads[0] = new TableCell("待检",wid + 8 * 0,LayoutParams.FILL_PARENT,TableCell.STRING);
        heads[0] = new TableCell("已检",wid + 8 * 1,LayoutParams.FILL_PARENT,TableCell.STRING);
        TableCell[] titles = new TableCell[10];
        int width = this.getWindowManager().getDefaultDisplay().getWidth()/titles.length;    
        titles[0] = new TableCell("序号",width + 8 * 0,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[1] = new TableCell("送检日期",width + 8 * 1,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[2] = new TableCell("送检时间",width + 8 * 2,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[3] = new TableCell("班次",width + 8 * 3,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[4] = new TableCell("送检人",width + 8 * 4,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[5] = new TableCell("物料号",width + 8 * 5,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[6] = new TableCell("客户件号",width + 8 * 6,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[7] = new TableCell("版本号",width + 8 * 7,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[8] = new TableCell("物料描述",width + 8 * 8,LayoutParams.FILL_PARENT,TableCell.STRING);
        titles[9] = new TableCell("类型",width + 8 * 9,LayoutParams.FILL_PARENT,TableCell.STRING);
        table.add(new TableRow(titles));  
        // 每行的数据    
        TableCell[] cells = new TableCell[10];
        for (int i = 0; i < cells.length; i++) {    
            cells[i] = new TableCell("No." + String.valueOf(i),    
                                    titles[i].width,     
                                    LayoutParams.FILL_PARENT,     
                                    TableCell.STRING);   
        }   
        // 把表格的行添加到表格    
        //for (int i = 0; i < 12; i++)    
            table.add(new TableRow(cells));  
        TableAdapter tableAdapter = new TableAdapter(this, table);    
        lv.setAdapter(tableAdapter);    
        lv.setOnItemClickListener(new ItemClickEvent());*/
		setContentView(R.layout.act_self);
		ListView tableListView = (ListView) findViewById(R.id.list);
		List<Goods> list = new ArrayList<Goods>();
		for (int i = 0; i < itemList.size(); i++) {    
			Map<String, Object> map = itemList.get(i);
			list.add(new Goods(
					i+"",
					StringUtil.isEmpty(map.get("QAT_CSP_DATE")+"", ""),
					StringUtil.isEmpty(map.get("QAT_CSP_TIME")+"", ""),
					StringUtil.isEmpty(map.get("QAT_OP_SHIFT")+"", ""),
					StringUtil.isEmpty(map.get("QAT_CSP_USER")+"", ""),
					StringUtil.isEmpty(map.get("QAT_OP_PART")+"", ""),
					StringUtil.isEmpty(map.get("QAT_CUST_PART")+"", ""),
					StringUtil.isEmpty(map.get("QAT_REV")+"", ""),
					StringUtil.isEmpty(map.get("QAT_OP_PART_DESC")+"", ""),
					StringUtil.isEmpty(map.get("QAT_TYPE")+"", "")
					)
					);
			
        } 		
		
		TableAdapters adapter = new TableAdapters(this, list);
		
		tableListView.setAdapter(adapter);
		
		tableListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				showErrorMsg("a");
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				showErrorMsg("aa");
			}
		});
		findViewById(R.id.text_ycheck).setOnClickListener(new y_onclick());
		findViewById(R.id.text_fcheck).setOnClickListener(new f_onclick());;
	}
	
	
	
	private void setXmls(List<Map<String, Object>> itemLists) {
		setContentView(R.layout.act_self_check);
		ListView tableListView = (ListView) findViewById(R.id.list1);
		List<Good> item_list = new ArrayList<Good>();
		for (int i = 0; i < itemLists.size(); i++) {    
			Map<String, Object> map = itemLists.get(i);
			item_list.add(new Good(
					i+"",
					StringUtil.isEmpty(map.get("QAT_CSP_DATE")+"", ""),
					StringUtil.isEmpty(map.get("QAT_CSP_TIME")+"", ""),
					StringUtil.isEmpty(map.get("QAT_OP_SHIFT")+"", ""),
					StringUtil.isEmpty(map.get("QAT_CSP_USER")+"", ""),
					StringUtil.isEmpty(map.get("QAT_OP_PART")+"", ""),
					StringUtil.isEmpty(map.get("QAT_CUST_PART")+"", ""),
					StringUtil.isEmpty(map.get("QAT_REV")+"", ""),
					StringUtil.isEmpty(map.get("QAT_OP_PART_DESC")+"", ""),
					StringUtil.isEmpty(map.get("QAT_TYPE")+"", ""),
					StringUtil.isEmpty(map.get("QAT_OUT_SLT")+"", ""),
					StringUtil.isEmpty(map.get("QAT_SIZE_SLT")+"", ""),
					StringUtil.isEmpty(map.get("QAT_JDG_SLT")+"", ""),
					StringUtil.isEmpty(map.get("QAT_SLT")+"", ""),
					StringUtil.isEmpty(map.get("QAT_CHK_TIME")+"", ""),
					StringUtil.isEmpty(map.get("QAT_CSP_USER")+"", "")
					)
					);
			
        } 		
		
		TableAdapter app = new TableAdapter(this, item_list);
		tableListView.setAdapter(app);
		findViewById(R.id.text_ycheck).setOnClickListener(new y_onclick());
		findViewById(R.id.text_fcheck).setOnClickListener(new f_onclick());
		
	}
	

	
	class ItemClickEvent implements AdapterView.OnItemClickListener {    
        @Override    
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,    
                long arg3) {   
        	showErrorMsg("aaa");
            Toast.makeText(SelfActivity_b.this, "选中第"+String.valueOf(arg2)+"行", 500).show();    
        }    
    }
	
	class y_onclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showErrorMsg("已检页面初始化成功");
			getItems();
		}    
         
    }
	
	class f_onclick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showErrorMsg("待检页面初始化成功");
			pageLoad();
		}    
         
    }
}