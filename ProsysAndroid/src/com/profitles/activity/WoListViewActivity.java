package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemSelectedListener;

import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.biz.CoFTheScanBiz;
import com.profitles.biz.LineWoListViewBiz;
import com.profitles.biz.WoListViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;
/**
 * 主计划页面
 * @author by Leo
 * @date 2019/7/8
 * */
@SuppressWarnings(value = { "all" })
public class WoListViewActivity extends AppFunActivity {
	// 获取页面View对象
	private MyDataGrid webView ;
	private ApplicationDB applicationDB;
	private Button buttonBegin, buttonEnd, buttonNext;
	private List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> dateList2 = new ArrayList<Map<String,Object>>();
	private WoListViewBiz biz;
	private LineWoListViewBiz biz1;
	private View backRow;
	private int checkRowIndex;
	private String domain, site, userid;
	private String XSWO_UKEY,XSWO_SHIFT,XSWO_NBR,XSWO_PART,
		XSWO_REL_DATE,XSWO_OP,XSWO_REV,XSWO_PART_NM,XSWO_QTY_ORD,XSWO_QTY_COMP, LINE;
    private String XSWC_MJCJ="";//免检车间

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_wolistview;
	}
	@Override
	protected void pageLoad() {
		// TODO Auto-generated method stub
		biz = new WoListViewBiz();
		biz1 = new LineWoListViewBiz();
		buttonBegin = (Button) findViewById(R.id.buttonBegin); // 开工
		buttonEnd = (Button) findViewById(R.id.buttonEnd);	   // 完工
		buttonNext = (Button) findViewById(R.id.buttonNext);   // 下一步
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		userid = ApplicationDB.user.getUserId();

		webView = (MyDataGrid)findViewById(R.id.mdtg_woinfolist);
		Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
	    Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据  
	    LINE = bundle.getString("LINE");//getString()返回指定key的值  
	    if(!StringUtil.isEmpty(bundle.getString("XSWC_MJCJ"))){
	    	XSWC_MJCJ=bundle.getString("XSWC_MJCJ");
		}
//	    LINE = "Agma1";
		getWoInfo();
		webView.setOnMyDataGridListener(new OnMyDataGridListener() {
			// 点击单元格事件
			@Override
			public void onItemClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
				clearMsg();
				// 获取加工单信息
				XSWO_UKEY = rowData.get("XSWO_UKEY") +""; 	  // 加工单ID
				XSWO_NBR = rowData.get("XSWO_NBR") +"";	  	  // 加工单
				XSWO_SHIFT = rowData.get("XSWO_SHIFT") +"";	  // 班次
				XSWO_PART = rowData.get("XSWO_PART") +"";	  // 物料号
				XSWO_REL_DATE = rowData.get("XSWO_REL_DATE") +""; // 计划时间
//				XSWO_OP = rowData.get("XSWO_OP") +""; // 工序
				XSWO_REV = rowData.get("XSWO_REV") +""; // 版本号
				XSWO_PART_NM = rowData.get("XSWO_PART_NM") +""; // 物料描述
				XSWO_QTY_ORD = rowData.get("XSWO_QTY_ORD") +""; // 计划数量
				XSWO_QTY_COMP = rowData.get("XSWO_QTY_COMP") +""; // 完工数量
				// TODO Auto-generated method stub
				if(rowIndex == 0) return;
//		    	Map<String, Object> map = dtg.getRowDataByKey(rowIndex-1);
				if(backRow != null){
					// backRow.setBackgroundColor(Color.TRANSPARENT);//清空上次点击行颜色
					backRow.setBackgroundColor(checkRowIndex == 0  ? Color.TRANSPARENT : 
						(checkRowIndex%2 == 0 ? Color.TRANSPARENT: Color.parseColor(Constants.CHECK_ROW_COLOR)) );//清空上次点击行颜色
				}
				backRow  = (View) view.getParent();		//保存获取到行View
				checkRowIndex = rowIndex;
				View vv = (View) view.getParent();		//获取到行View
				vv.setBackgroundColor(Color.YELLOW);	//更改选中行的背景色
				//checkLine(rowData);
			}

			// 长按单元格事件
			@Override
			public boolean onItemLongClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
				return false;
			}
		});
		
		/**
		 * 开工按钮事件
		 * */
		buttonBegin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				loadDataBase(new IRunDataBaseListens() {
					
					// 数据校验，数据有效则进入后续步骤
					@Override
					public boolean onValidata() {
						clearMsg();
						// checkRowIndex用于判断是否选中一行
						if(checkRowIndex == 0){
							showErrorMsg("请选择一行进行操作");
							return false;
						}
						return true;
					}

					// 向后台发送请求
					@Override
					public Object onGetData() {
						// 调用处理类
						return biz.cofGetLineById(applicationDB.user.getUserDmain(), 
								applicationDB.user.getUserSite(),
								applicationDB.user.getUserId(),
								XSWO_UKEY,
								XSWO_NBR,
								XSWO_PART,
								XSWO_REL_DATE,
								XSWO_OP,
								XSWO_REV,
								XSWO_PART_NM,
								XSWO_QTY_ORD,
								XSWO_QTY_COMP,
								LINE);
					}

					// 回调函数，获取后台的数据
					@Override
					public void onCallBrack(Object data) {
						// 数据是以WebResponse对象传过来的，后台发送消息和数据
						WebResponse wrs = (WebResponse) data;
						if(wrs.isSuccess()){
							// 开工
							showMessage(wrs.getMessages());
						}else{
							// 失败则报错
							showErrorMsg(wrs.getMessages());
						}
					}
					
				});	
			}
			
		} );
		/**
		 * 完工按钮事件
		 * */
		buttonEnd.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						clearMsg();
						// 不为true,则不能进入后面的代码
						if(checkRowIndex == 0){
							showErrorMsg("请选择一行进行操作");
							return false;
						}
						float PQ=StringUtil.parseFloat(XSWO_QTY_ORD);
						float QC=StringUtil.parseFloat(XSWO_QTY_COMP);
						if(PQ!=QC){  //计划数量需要等于完工数
							showConfirm("计划完成数量["+PQ+"],与实际完成数量["+QC+"]不一致,是否继续完工操作?",comfirmalert);
							return false;
						}else{
							return true;
						}
					}
					@Override
					public Object onGetData() {
						return biz.cofGetLineIsBg(XSWO_NBR,XSWO_UKEY,applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(),LINE);
					}
					@Override
					public void onCallBrack(Object data) {
						WebResponse wrs = (WebResponse) data;
						if(wrs.isSuccess()){
							// 执行完工操作
							EndRfqclot();
						}else{
							showErrorMsg(wrs.getMessages());
						}
					}
				});	
			}
			
		} );
		
		
		/**
		 * 下一步按钮事件
		 * */
		buttonNext.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				loadDataBase(new IRunDataBaseListens() {

					@Override
					public boolean onValidata() {
						clearMsg();
						if(checkRowIndex == 0){
							showErrorMsg("请选择一行进行操作");
							return false;
						}
						return true;
					}

					@Override
					public Object onGetData() {
						// TODO Auto-generated method stub
						return biz.cofGetLineIsBg(XSWO_NBR,XSWO_UKEY,applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(),LINE);
					}

					@Override
					public void onCallBrack(Object data) {
						WebResponse wrs = (WebResponse) data;
						if(wrs.isSuccess()){
							// 加工单开工，跳转到工作台界面
							Intent intent = new Intent(WoListViewActivity.this, FinshOPActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("XSWO_NBR", XSWO_NBR);
							bundle.putString("XSWO_UKEY", XSWO_UKEY);
							bundle.putString("SPACE", "");
							bundle.putString("PLANDATE", XSWO_REL_DATE);
							bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
							bundle.putString("XSWO_SHIFT",XSWO_SHIFT);
							intent.putExtras(bundle);
							startActivity(intent);
						}else{
							showErrorMsg(wrs.getMessages());
						}
					}
					
				});	
			}
			
		} );
		
	}
	
	private OnShowConfirmListen comfirmalert=new OnShowConfirmListen() {
		@Override
		public void onConfirmClick() {   //点击确定
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				@Override
				public Object onGetData() {
					return biz.cofGetLineIsBg(XSWO_NBR,XSWO_UKEY,applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(),LINE);
				}

				@Override
				public void onCallBrack(Object data) {
					WebResponse wrs = (WebResponse) data;
					if(wrs.isSuccess()){
						// 执行完工操作
						EndRfqclot();
					}else{
						showErrorMsg(wrs.getMessages());
					}
				}
			});	
		}
		@Override
		public void onCancelClick() {    //点击取消
			// TODO Auto-generated method stub
			//不做操作
		}
	};
	
	// 完工，修改加工单和生产线状态
	private void EndRfqclot(){ 
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				//clearMsg();
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.updateRfqclot(applicationDB.user.getUserDmain(), 
						applicationDB.user.getUserId(),
						applicationDB.user.getUserSite(),
						XSWO_UKEY);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
						if(!StringUtil.isEmpty(XSWC_MJCJ)){
							if(XSWC_MJCJ.equals("1")){
								Intent intent = new Intent(WoListViewActivity.this, LineWoListViewActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("XSWO_NBR", XSWO_NBR);
								bundle.putString("XSWO_UKEY", XSWO_UKEY);
								bundle.putString("SPACE", "");
								bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
								intent.putExtras(bundle);
								startActivity(intent);
							}else{
								Intent intent = new Intent(WoListViewActivity.this,
										HandoverItemActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("LINE", LINE);
								bundle.putString("XSWO_UKEY", XSWO_UKEY);
								intent.putExtras(bundle);
								startActivity(intent);
//								showMessage(wrs.getMessages());
								// 删除页面开工记录
//								clearData(checkRowIndex);
								// 清除选中的列表行标
								checkRowIndex = 0;
							}
						}else{
							Intent intent = new Intent(WoListViewActivity.this,
									HandoverItemActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("LINE", LINE);
							bundle.putString("XSWO_UKEY", XSWO_UKEY);
							intent.putExtras(bundle);
							startActivity(intent);
//							showMessage(wrs.getMessages());
							// 删除页面开工记录
//							clearData(checkRowIndex);
							// 清除选中的列表行标
							checkRowIndex = 0;
						}
				}else{
					showErrorMsg(wrs.getMessages());
				}
			}
			
		});	
	}
	
	// 初始化页面数据
	private void getWoInfo(){
		
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.seaWoInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(), LINE);
			}

			// 回调函数
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					dateList = wrs.getDataToList();
					XSWC_MJCJ = dateList.get(0).get("XSWC_MJCJ")+"";
					// 显示某行某列的数据
					//showErrorMsg(webView.getItemDataByRC(2, 2)+"");
					webView.buildData(dateList);
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
			Intent intent = new Intent(WoListViewActivity.this, LineWoListViewActivity.class);	
			startActivity(intent);
		}
		@Override
		public void onCancelClick() {    //点击取消
			Intent intent = new Intent(WoListViewActivity.this, LineWoListViewActivity.class);	
			startActivity(intent);
		}
	};
	
	
	
	
	// 删除开工成功记录
	private void clearData(int checkRowIndex){
		// 删除开工记录
		if(checkRowIndex == 0) return;
		dateList.remove(checkRowIndex - 1);
		// gridlist重新加载数据
		webView.buildData(dateList);
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
		Intent intent = new Intent(WoListViewActivity.this, LineWoListViewActivity.class);
		startActivity(intent);	
	}
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Return,ButtonType.Help};
	}	
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
	}
	
	// 获取时间
	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}
}