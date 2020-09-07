package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;

import com.profitles.biz.SortPrintBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.Constants;

@SuppressLint("HandlerLeak")
public class SortPrintActivity extends AppFunActivity {
	private SortPrintBiz sortBiz;
	@SuppressWarnings("unused")
	private MyTextView txvsortNbr;
 	private MyEditText etxsortNbr;
	private MyTabHost tblsortAdv;
 	@SuppressWarnings("unused")
	private LinearLayout lltsortso,lltsortPkl,lltsortSrfd;
	private MyDataGrid gdvsortso,gdvsortPkl,gdvsortSrfd; 
	private List<Map<String , Object>> srfdList = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> pklList = new ArrayList<Map<String , Object>>();
	private ApplicationDB applicationDB;
	private String domain , site ;
	private View vi;
	private int checkRowIndex;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_sortprint;
	}

	@SuppressWarnings("rawtypes")
	@Override 
	protected void pageLoad() {
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		tblsortAdv= ((MyTabHost) this.findViewById(R.id.tbl_sortAdv));
		tblsortAdv.setup();
		tblsortAdv.setOnTabChangedListener(new OnTabChangeListener() {
			@Override	
			public void onTabChanged(String tabId) {
				if(tabId.equals("GetSortSrfd")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							gdvsortSrfd.buildData(srfdList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}else if(tabId.equals("GetSortPkl")){
					new Handler() {
						public void handleMessage(
								Message msg) {
							gdvsortPkl.buildData(pklList);
							super.handleMessage(msg);
						}
					}.sendEmptyMessage(0); 
				}
			}
		});
		sortBiz = new SortPrintBiz();
		etxsortNbr = (MyEditText) findViewById(R.id.etx_sortNbr);
		etxsortNbr.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { 
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
//				if(null != etxsortNbr.getValStr() && !"".equals( etxsortNbr.getValStr().toString().trim())){
//					checkNbr();
//				} 
			}
			public void afterTextChanged(Editable s) {
				if(null != etxsortNbr.getValStr() && !"".equals( etxsortNbr.getValStr().toString().trim())){
					checkNbr();
				} 
			}
			
		});
		txvsortNbr = (MyTextView)findViewById(R.id.txv_sortNbr);
		lltsortso = (LinearLayout) findViewById(R.id.llt_sortso) ;
		lltsortSrfd = (LinearLayout) findViewById(R.id.llt_sortSrfd) ; 
		lltsortPkl = (LinearLayout) findViewById(R.id.llt_sortPkl) ;  

		gdvsortPkl = (MyDataGrid) findViewById(R.id.gdv_sortPkl); 
		gdvsortSrfd = (MyDataGrid) findViewById(R.id.gdv_sortSrfd) ;
		gdvsortso = (MyDataGrid) findViewById(R.id.gdv_sortso) ;
		reBindPkList();//加载销售单信息
		gdvsortso.setOnMyDataGridListener(new OnMyDataGridListener() {
			@Override
			public void onItemClick(View view, Object val, int rowIndex,
				int colIndex, Map<String, Object> rowData) {
					if(rowIndex == 0) return;
					if(vi != null){
						vi.setBackgroundColor(checkRowIndex == 0 ? Color.TRANSPARENT :
							(checkRowIndex % 2 == 0 ? Color.TRANSPARENT : Color.parseColor(Constants.CHECK_ROW_COLOR)) );
//						vi.setBackgroundColor(Color.TRANSPARENT);//清空上次点击行颜色
					}
					vi  = (View) view.getParent();		//保存获取到行View
					checkRowIndex = rowIndex;
					View vv = (View) view.getParent();		//获取到行View
					vv.setBackgroundColor(Color.YELLOW);	//更改选中行的背景色
					etxsortNbr.setText(rowData.get("RFSRF_NBR")+"");
			}

			@Override
			public boolean onItemLongClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
				return false;
			}

		});
		map = new HashMap();
	}
  
	@Override
	protected boolean onFocus(int id, View v) {
		return super.onFocus(id, v);
	}
	boolean istrue = false; 
	@SuppressWarnings("rawtypes")
	Map map = new HashMap();
	@Override
	protected boolean onBlur(int id, View v) { 
		// 单号
		if(etxsortNbr.getId() == id ){
			checkNbr(); 
		} 
		return istrue ;
	}
	
	private void checkNbr() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if(!"".equals(etxsortNbr.getValStr().toString().trim())){
					return istrue = true;
				}else{
					showErrorMsg(getResources().getString(R.string.pk_sub_false));
					getFocues(etxsortNbr, true);
					return  istrue = false; 
				}
			}
			@SuppressWarnings("static-access")
			@Override
			public Object onGetData() {
				return sortBiz.getIsPrint(applicationDB.user.getUserDmain(),
						applicationDB.user.getUserSite() ,
						etxsortNbr.getValStr().toString() , 
						applicationDB.user.getUserId() , 
						applicationDB.user.getMac());
			}
			@SuppressWarnings("unchecked")
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					map = wr.getDataToMap();
					if(map!=null){
						//带回分拣和发货详细信息
						setChangeTable(map);
						istrue = true;
					}else{
//						getResources().getString(R.string.pk_getfalse)
						etxsortNbr.reValue();
						showErrorMsg("当前销售通知单无信息返回");
						istrue = false;
					}
				}else{
					istrue = false;
					etxsortNbr.reValue();
					showErrorMsg(wr.getMessages());
					getFocues(etxsortNbr, true);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void setChangeTable(Map<String, Object> map) {
//		reBindPkList();
		srfdList =  (List<Map<String, Object>>) map.get("detList"); 
		pklList =  (List<Map<String, Object>>) map.get("pkllist");
		gdvsortPkl.buildData(srfdList);
		gdvsortSrfd.buildData(pklList);
	}
	
	  
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Print,ButtonType.Return,ButtonType.Help};
	}
	
	@Override
	public Object OnBtnHelpClick(ButtonType btnType, View v) {
		showSuccessMsg(R.string.help_ok);
		return  null ;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public Object OnBtnPntClick(ButtonType btnType, View v) {
		if(etxsortNbr.getValStr() != null || etxsortNbr.getValStr() != ""){
			return sortBiz.ptAdConfirm(domain, site, 
					etxsortNbr.getValStr(),
					applicationDB.user.getUserId() , 
					applicationDB.user.getMac(),
					"SortPrintActivity",
					getAppVersion(),
					applicationDB.Ctrl.getString("RFC_PO_QAD","0"),
					applicationDB.user.getUserDate()
					);
		}else{
			return null;
		}
	}
	
	@Override
	public void OnBtnPntCallBack(Object data) {
		//处理打印后返回的信息
		WebResponse wrs = (WebResponse) data;
		if (wrs.isSuccess()) {
			reBindPkList();
			srfdList =   new ArrayList<Map<String, Object>>() ; 
			pklList =  new ArrayList<Map<String, Object>>() ; 
			gdvsortPkl.buildData(srfdList);
			gdvsortSrfd.buildData(pklList);
			etxsortNbr.reValue();
			getFocues(etxsortNbr, true);
			showSuccessMsg(wrs.getMessages());
		}else{
			showErrorMsg(wrs.getMessages());
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
	
	//解锁
	@Override
	protected void unLockNbr() {

	}

	// 获取销售单列表
		private void reBindPkList(){
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return true;
				}
				
				@Override
				public Object onGetData() {
					return sortBiz.getSortList(ApplicationDB.user.getUserDmain(), ApplicationDB.user.getUserSite(), ApplicationDB.user.getUserId());
				}
				
				@SuppressWarnings("unchecked")
				@Override
				public void onCallBrack(Object data) {
						gdvsortso.buildData((List<Map<String,Object>>)data);
				}
			});
		}
	
//	private OnShowConfirmListen scfListenSocfmNbr=new OnShowConfirmListen() { 
//		@Override
//			loadDataBase(new IRunDataBaseListens() {
//				@Override
//				public boolean onValidata() {
//					return true;
//				}
//				@Override
//				public Object onGetData() {
//					 return  sortBiz.getIsPrint(applicationDB.user.getUserDmain() , 
//								applicationDB.user.getUserSite() ,
//								etxsortNbr.getValStr().toString() , 
//								applicationDB.user.getUserId() , 
//								applicationDB.user.getMac());
//				}
//				@Override
//				public void onCallBrack(Object data) {
//					setChangeTable(map);
//					istrue = true;
//				}
//			});	
//	};
}
