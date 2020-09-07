package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;





import com.profitles.biz.HandoverItemBiz;
import com.profitles.biz.LineWoListViewBiz;
import com.profitles.biz.WoListViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyButton;
import com.profitles.framwork.cusviews.view.MyViewGroup;
import com.profitles.framwork.util.StringUtil;


public class LineWoListViewActivity extends AppFunActivity {
	private HandoverItemBiz biz1;
	private WoListViewBiz woBiz;
	private LinearLayout mytable;
	private ApplicationDB applicationDB;
	private List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> dateList2 = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> dateList1 = new ArrayList<Map<String,Object>>();
	List<Map<String, Object>> itemList2 = new ArrayList<Map<String,Object>>();
	private LineWoListViewBiz biz;
    private String count="0";
    private String XSWC_MJCJ="";//当前产线车间是否为免检车间
    int count1=0;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_linepage;
	}
	@Override
	protected void pageLoad() {
		woBiz=new WoListViewBiz();
		biz1 = new HandoverItemBiz();
		biz = new LineWoListViewBiz();
		mytable=(LinearLayout) findViewById(R.id.MyTable2);
		getLinePage();
		
	}
	//获得开工记录
	 private void  getRecord(final String line){
	  loadDataBase(new IRunDataBaseListens() {
	    public boolean onValidata() {
		return true;
	    }
	    @Override
	    public Object onGetData() {
		return woBiz.getLineState(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),line);
	    }
	    public void onCallBrack(Object data) {
		// 数据是以WebResponse对象传过来的，后台发送消息和数据
		WebResponse wrs = (WebResponse) data;
		 if(wrs.isSuccess()){
	       dateList1 = wrs.getDataToList();
	       if(count.equals("0") || count.equals("[0]")){
	    	   if((dateList1 != null && dateList1.size() >0 ) && dateList.size()>0){
	    		   for  (int i=0; i< dateList1.size(); i++) {  
	    			   if(!StringUtil.isEmpty(dateList1.get(i).get("RFQCLOT_STATUS")) && "YES".equals((String)dateList1.get(i).get("RFQCLOT_STATUS"))){
	    				// 跳转到下一个 页面
	    				   Intent intent = new Intent(LineWoListViewActivity.this, FinshOPActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("XSWO_NBR", (String)dateList1.get(i).get("RFQCLOT_WO_NBR"));
							bundle.putString("XSWO_UKEY", (String)dateList1.get(i).get("RFQCLOT_WO_UKEY"));
							intent.putExtras(bundle);
							startActivity(intent);
							break;
	    			   }else{
	    				// 跳转到下一个 页面
	   					Intent intent = new Intent(LineWoListViewActivity.this, WoListViewActivity.class);	
	   					Bundle bundle = new Bundle();
	   					bundle.putString("LINE", line);
	   					bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
	   					intent.putExtras(bundle);
	   					startActivity(intent);
	    			   }
	    		   }
				}else{
					Intent intent = new Intent(LineWoListViewActivity.this, WoListViewActivity.class);	
   					Bundle bundle = new Bundle();
   					bundle.putString("LINE", line);
   					bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
   					intent.putExtras(bundle);
   					startActivity(intent);
				}
	       }else{
					getItemValue(line);
				}
		 
         }else{
	       showErrorMsg(wrs.getMessages());
         }
	    }
	 });	
	}
      // 获得生产线数据
	private void getLinePage(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public Object onGetData() {
				return biz.LineButton(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;		
				if(wrs.isSuccess()){
					dateList = wrs.getDataToList();
					if(!StringUtil.isEmpty(dateList)&&dateList.size()>0){
						XSWC_MJCJ=dateList.get(0).get("XSWC_MJCJ")+"";
						setValue();
					}
				}else{
					showErrorMsg(wrs.getMessages());
				}
			}		
		});	 
	}
	
	
	 
	// 获得交接项记录
	 private void  getCount(final String line){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					// TODO Auto-generated method stub
					return true;
				}
				@Override
				public Object onGetData() {
					return biz.getCount(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),line);
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wrs = (WebResponse) data;		
					if(wrs.isSuccess()){
                       count=wrs.getWData().toString();
                    getRecord(line);
					}else{
						showErrorMsg(wrs.getMessages());
					}
				}		
			});	 
		}	
	  @Override
     	protected void unLockNbr() {
	    }
	@SuppressLint("NewApi")
	public void setValue(){	
		  DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);
		  int width = dm.widthPixels;
		  int height=dm.heightPixels;
		  LinearLayout mytable2 = (LinearLayout)mytable;
		  
		 //这里创建按钮，每行放置2个按钮  
		  if(dateList.size()>0){
			  if(dateList.size()==1){
				  count1=2;
			  }else{
				  count1=dateList.size();
			  }
			  for (int i=0; i< count1-1; i++) {  
				  LinearLayout mytable1=new LinearLayout(this);
				  Button but=new Button(this);
				  Button but2=new Button(this);
			        //将LIST数据填充到按钮中
			        but.setText(dateList.get(i).get("XRUL_LINE")+""); 
			        but.setTextSize(width/40);
			        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width/2-90,height/3-60);	 
				    lp.setMargins(30, 30, 30, 30);
			        mytable1.addView(but,lp);
			        if(dateList.size()!=1){
			        	but2.setText(dateList.get(i+1).get("XRUL_LINE")+""); 
				        but2.setTextSize(width/40);  
				        mytable1.addView(but2,lp);
				        final String line1=but2.getText().toString();
					     final int ct=i+1;
					     but2.setOnClickListener(new Button.OnClickListener() {
								@Override
							   public void onClick(View v) {
									getCount(line1);
							 }
					     });
			        }
			        mytable1.setOrientation(LinearLayout.HORIZONTAL);
                     //将按钮放入layout组件
			        //为按钮设置一个.标记，来确认是按下了哪一个按钮
			         final String line=but.getText().toString(); 
					 final int cut=i;
				     but.setOnClickListener(new Button.OnClickListener() {
								@Override
							   public void onClick(View v) {
									getCount(line);
							 }
					 });
				     mytable2.addView(mytable1);
				     i=i+1;
			  }
			   
	 }
	}
	//获得交接项结果
		private void getItemValue(final String line){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					// TODO Auto-generated method stub
					return true;
				}
				@Override
				public Object onGetData() {
					return biz1.getItemValue(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),line);
				}
				@Override
				public void onCallBrack(Object data) {
					WebResponse wrs = (WebResponse) data;		
					if(wrs.isSuccess()){
						//获得的数据库交接项信息
						itemList2 = wrs.getDataToList();
						for (int j = 0; j < itemList2.size(); j++) {
							if(itemList2.get(j).get("CHASHIFT_NAME").toString().equals("意见") && itemList2.get(j).get("CHASHIFT_STATUS").equals("10")){
								if(!StringUtil.isEmpty(itemList2.get(j).get("CHASHIFT_VALUE"))){
									 if(!StringUtil.isEmpty(dateList1)&&dateList.size()>0){
							    		   for  (int i=0; i< dateList1.size(); i++) {  
							    			   if(!StringUtil.isEmpty(dateList1.get(i).get("RFQCLOT_STATUS")) && "YES".equals((String)dateList1.get(i).get("RFQCLOT_STATUS"))){
							    				// 跳转到下一个 页面
							    				   Intent intent = new Intent(LineWoListViewActivity.this, FinshOPActivity.class);
													Bundle bundle = new Bundle();
													bundle.putString("XSWO_NBR", (String)dateList1.get(i).get("RFQCLOT_WO_NBR"));
													bundle.putString("XSWO_UKEY", (String)dateList1.get(i).get("RFQCLOT_WO_UKEY"));
													bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
													intent.putExtras(bundle);
													startActivity(intent);
													break;
							    			   }else{
							    				// 跳转到下一个 页面
							   					Intent intent = new Intent(LineWoListViewActivity.this, WoListViewActivity.class);	
							   					Bundle bundle = new Bundle();
							   					bundle.putString("LINE", line);
							   					bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
							   					intent.putExtras(bundle);
							   					startActivity(intent);
							    			   }
							    		   }
									 }
									 break;
								}else{
									// 跳转到下一个 页面
									if(XSWC_MJCJ.equals("1")){
										// 跳转到下一个 页面
										if(!StringUtil.isEmpty(dateList1)&&dateList.size()>0){
											 for  (int i=0; i< dateList1.size(); i++) {  
								    			   if(!StringUtil.isEmpty(dateList1.get(i).get("RFQCLOT_STATUS")) && "YES".equals((String)dateList1.get(i).get("RFQCLOT_STATUS"))){
								    				// 跳转到下一个 页面
								    				   Intent intent = new Intent(LineWoListViewActivity.this, FinshOPActivity.class);
														Bundle bundle = new Bundle();
														bundle.putString("XSWO_NBR", (String)dateList1.get(i).get("RFQCLOT_WO_NBR"));
														bundle.putString("XSWO_UKEY", (String)dateList1.get(i).get("RFQCLOT_WO_UKEY"));
														bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
														intent.putExtras(bundle);
														startActivity(intent);
														break;
								    			   }else{
								    				// 跳转到下一个 页面
								   					Intent intent = new Intent(LineWoListViewActivity.this, WoListViewActivity.class);	
								   					Bundle bundle = new Bundle();
								   					bundle.putString("LINE", line);
								   					bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
								   					intent.putExtras(bundle);
								   					startActivity(intent);
								    			   }
								    		   }
										}
									}else{
										getWoInfo(line);
									}
								}
								break;
							}
						}
					}else{
						showErrorMsg(wrs.getMessages());
						
					}
				}		
			});	 
		}
		@Override
		public boolean OnBtnRtnValidata(ButtonType btnType, View v) {
			return true;
		}

		@Override
		public Object OnBtnRtnClick(ButtonType btnType, View v) {
			return null;
		}
		@Override
		public void OnBtnRtnCallBack(Object data) {
			Intent intent = new Intent(LineWoListViewActivity.this, MenuActivity.class);
			startActivity(intent);	
		}
		
		
		private void getWoInfo(final String line){
			
			loadDataBase(new IRunDataBaseListens() {

				@Override
				public boolean onValidata() {
					return true;
				}

				@Override
				public Object onGetData() {
					return woBiz.seaWoInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(), line);
				}

				// 回调函数
				@Override
				public void onCallBrack(Object data) {
					WebResponse wrs = (WebResponse) data;
					if(wrs.isSuccess()){
						Intent intent = new Intent(LineWoListViewActivity.this, HandoverItemActivity.class);	
						Bundle bundle = new Bundle();
						bundle.putString("LINE", line);
						bundle.putString("COUNT", count);
						intent.putExtras(bundle);
						startActivity(intent);
					}else{
						/**
						 * 点击产线  要是该产线无生产计划提示后点击确认才能返回原页面   Jack  2019-08-23
						 */
						showConfirmClickCon(wrs.getMessages(),initalert);
						/**
						 * end
						 */
					}
				}
			});	
		}
		private OnShowConfirmListen initalert=new OnShowConfirmListen() {
			@Override
			public void onConfirmClick() {   //点击确定
				Intent intent = new Intent(LineWoListViewActivity.this, LineWoListViewActivity.class);	
				startActivity(intent);
			}
			@Override
			public void onCancelClick() {    //点击取消
				Intent intent = new Intent(LineWoListViewActivity.this, LineWoListViewActivity.class);	
				startActivity(intent);
			}
		};

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Return, ButtonType.Help};
	}	
	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}
}
