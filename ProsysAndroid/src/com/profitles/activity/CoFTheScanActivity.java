package com.profitles.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.CoFTheScanBiz;
import com.profitles.biz.LoginBiz;
import com.profitles.biz.TrBiz;
import com.profitles.biz.fpsBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyButton;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.network.NetworkUtil;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

import android.R.integer;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class CoFTheScanActivity extends AppFunActivity {

	private MyReadBQ txvBar;
	private MyEditText txvDate, txvWkSh, nbr, txvShift, txvPart,txvPartDesc,
			CuntNbr, txvCPart, dateDisplay;
	private CoFTheScanBiz CoFTheScanBiz;
	private LoginBiz lbiz;
	private MySpinner opDesc,txvWkShift;
	private MyDataGrid dtg;
	private ApplicationDB applicationDB;
	private String domain, site, userid, locbin, vend, tiaoma , 
			 fnceffdate, amount,tmp_qclot_wo_ukey,shift;
	private boolean isflgn = true;
	private boolean isSchBarSuc = true,isSchBar = true,IS_GRENZEB = true;
	private List<Map<String, Object>> nbrlist = new ArrayList<Map<String, Object>>();
	private boolean onPageLoad = true;
	private Button PassButton, NGButton, dateChoose;
	private String buttonType1 = "PASS";
	private String buttonType2 = "NG";
	private OnShowConfirmListen confirmIsNo;
	private static	long lastClick;  
	private int mYear, mMonth, mDay;
	private final int DATE_DIALOG = 1;

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_cofthescanbiz;
	}

	@Override
	protected void pageLoad() {
		CoFTheScanBiz = new CoFTheScanBiz();
		LoginBiz lbiz = new LoginBiz();
		txvBar = (MyReadBQ) findViewById(R.id.etx_cofBar); // 扫码etx_cofDate
		txvPart = (MyEditText) findViewById(R.id.etx_cofPart); // 零件 etx_sendPartDesc
		txvPartDesc = (MyEditText) findViewById(R.id.etx_sendPartDesc);
		nbr = (MyEditText) findViewById(R.id.etx_cofnbr);// 加工单
		txvWkSh = (MyEditText) findViewById(R.id.etx_cofWkSh);// 车间
		txvWkShift = (MySpinner) findViewById(R.id.etx_cofWkShift); // 产线
		txvShift = (MyEditText) findViewById(R.id.etx_cofTxvShift1); // 班次
		opDesc = (MySpinner) findViewById(R.id.ext_cofspinner); // 工序
		dateDisplay = (MyEditText) findViewById(R.id.etx_cofDate); // 时间
		dateDisplay.setEnabled(false);
		txvCPart = (MyEditText) findViewById(R.id.etx_cofCPart); // 客件
		CuntNbr = (MyEditText) findViewById(R.id.etx_Nbr); // 数量
		//dateChoose = (Button) findViewById(R.id.dateChoose);
		PassButton = (Button) findViewById(R.id.button); // 按钮
	//	NGButton = (Button) findViewById(R.id.button2); // 按钮
		domain = ApplicationDB.user.getUserDmain();
		site = ApplicationDB.user.getUserSite();
		userid = ApplicationDB.user.getUserId();
		fnceffdate = ApplicationDB.user.getUserDate();
		getLine();
		

		// 工序下拉框
		opDesc.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
			
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		
		
		txvWkShift.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (txvWkShift.getValStr() != null && !"".equals(txvWkShift.getValStr().toString().trim())&& isflgn == false && IS_GRENZEB) {
					getWoRecord(txvWkShift.getValStr());
				}
					
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		

		// 扫码
		txvBar.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				clearMsg();
				if (!txvBar.getValStr().equals("")) {
					BlurBar();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});

		// pass按钮
			PassButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						// TODO Auto-generated method stub
						// 防止网络重新请求
						if (System.currentTimeMillis() - lastClick <= 1000) {
							showErrorMsg("不可重复提交数据");
							getFocues(txvBar, true); // 光标停留
							return false;
						}
						lastClick = System.currentTimeMillis();
						
						if (txvBar.getValStr().equals("")) {
							showErrorMsg("扫码栏位为空!");
							getFocues(txvBar, true); // 光标停留
							return false;
						}else
						if (CuntNbr.getValStr() == null || "".equals(CuntNbr.getValStr().trim())) {
							showErrorMsg("请输入数量!");
							getFocues(txvBar, true); // 光标停留
							return false;

						}
						return true;
					}

					public Object onGetData() {
						// TODO Auto-generated method stub
						return CoFTheScanBiz.cof_submit(domain, site, userid,txvBar.getValStr(), nbr.getValStr(),opDesc.getValStr(), buttonType1,
								CuntNbr.getValStr(), txvPart.getValStr(),shift,tmp_qclot_wo_ukey,txvWkSh.getValStr(),txvWkShift.getValStr(),
								dateDisplay.getValStr(),txvPartDesc.getValStr()
								
								);
					}


					@Override
					public void onCallBrack(Object data) {
						// TODO Auto-generated method stub
						WebResponse wrs = (WebResponse) data;
						try {
							if (wrs.isSuccess()) {
								 Map<String, Object> mapConfirm = wrs.getDataToMap();
								Map<String, Object> map = new HashMap<String, Object>();
								map = mapConfirm;
								getFocues(txvBar, true); // 光标停留
								showSuccessMsg(wrs.getMessages());
								txvBar.setText("");
								CuntNbr.setText("");
								tmp_qclot_wo_ukey = mapConfirm.get("UKEY").toString();
								if(!IS_GRENZEB){
									txvPart.setText("");// 零件
									txvPartDesc.setText("");
									dateDisplay.setText("");// 日期
									nbr.setText(""); // 加工单
									txvWkSh.setText(""); // 车间
									txvShift.setText(""); // 班次
									txvWkShift.clearItems();// 产线
									shift = "";
									tmp_qclot_wo_ukey =""; //唯一标示
									opDesc.clearItems();;
								    CuntNbr.setText("");
								    txvBar.setText("");
								}
							
							} else {
								if(IS_GRENZEB){
									txvBar.setText("");
									CuntNbr.setText("");
								}else{
									txvPart.setText("");// 零件
									txvPartDesc.setText("");
									dateDisplay.setText("");// 日期
									nbr.setText(""); // 加工单
									txvWkSh.setText(""); // 车间
									txvShift.setText(""); // 班次
									txvWkShift.clearItems();// 产线
									shift = "";
									tmp_qclot_wo_ukey =""; //唯一标示
									opDesc.clearItems();;
								    CuntNbr.setText("");
								    txvBar.setText("");
								}
								getFocues(txvBar, true); // 光标停留
								showErrorMsg(wrs.getMessages());
							}
							
						} catch (Exception e) {
							// TODO: handle exception
							showErrorMsg(e.getMessage());	
						}
				
					}

				});
			}

		});
		
			 OnShowConfirmListen confirmIsNo =new OnShowConfirmListen() { 
				@Override
				public void onConfirmClick() {   //询问框  点确定
					//确定(用户选择了继续)则记录异常操作日志中rfifo_log中
					loadDataBase(new IRunDataBaseListens() {
						@Override
						public boolean onValidata() {
							return true;
						}
						@Override
						public Object onGetData() {
							return CoFTheScanBiz.cofConfirm(domain, userid, site, tmp_qclot_wo_ukey, CuntNbr.getValStr(), txvPart.getValStr(), txvBar.getValStr(), nbr.getValStr());
						}
						@Override
						public void onCallBrack(Object data) {
							WebResponse wrConfirm=(WebResponse) data;
							if (wrConfirm.isSuccess()) {
								showSuccessMsg(wrConfirm.getMessages());
								txvBar.setText("");
								CuntNbr.setText("");
							}else {
								txvBar.setText("");
								CuntNbr.setText("");
								getFocues(txvBar, true); // 光标停留
								showErrorMsg(wrConfirm.getMessages());
							}
							
						}
					});	
					
				}
				
				@Override
				public void onCancelClick() {  //询问框  点取消
					//取消则回到该栏位位置选中该栏位值
					getFocues(txvBar, true);
					//istrue=false;
				}
			};
			
			

		// ng按钮
	/*			NGButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadDataBase(new IRunDataBaseListens() {

					@Override
					public boolean onValidata() {
						// TODO Auto-generated method stub
						if (txvBar.getValStr().equals("")) {
							showErrorMsg("请扫完条码后进行操作!");
							return false;
						}
						if (CuntNbr.getValStr() == null || "".equals(CuntNbr.getValStr().trim())) {
							showErrorMsg("请输入数量!");
							getFocues(txvBar, true); // 光标停留
							return false;

						}

						return true;
					}

					@Override
					public Object onGetData() {
						// domain,site,userid,scan,nbr,desc,type,num,part,shift,woukey,wkctr,line,time

						return CoFTheScanBiz.cof_submit(domain, site, userid,
								txvBar.getValStr(), nbr.getValStr(),
								opDesc.getValStr(), buttonType2,
								CuntNbr.getValStr(), txvPart.getValStr(),
								txvShift.getValStr(),tmp_qclot_wo_ukey,
								txvWkSh.getValStr(),
								txvWkShift.getValStr(),
								dateDisplay.getValStr()
								);
					}

					@Override
					public void onCallBrack(Object data) {
						// TODO Auto-generated method stub
						WebResponse wrs = (WebResponse) data;
						if (wrs.isSuccess()) {
							Map<String, Object> map = new HashMap<String, Object>();
							map = wrs.getDataToMap();
							getFocues(txvBar, true); // 光标停留
							showSuccessMsg(wrs.getMessages());
							txvBar.setText("");
							CuntNbr.setText("");
						} else {
							txvBar.setText("");
							CuntNbr.setText("");
							getFocues(txvBar, true); // 光标停留
							showErrorMsg(wrs.getMessages());
						}
					}

				});

			}

		});
*/
	}

	
	/*
	 * 扫码
	 */

	public void BlurBar() {
 		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				if (StringUtil.isEmpty(opDesc.getValStr()) && IS_GRENZEB) {
					showErrorMsg("请选择工序后操作!");
					txvBar.setText("");
					return false;
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return CoFTheScanBiz.cof_scan(domain, site, userid,
						txvBar.getValStr(), nbr.getValStr(), opDesc.getValStr(),
						txvPart.getValStr(), txvWkShift.getValStr(),
						txvWkSh.getValStr(),txvCPart.getValStr(),tmp_qclot_wo_ukey);
			}

			@Override
			public void onCallBrack(Object data) {
				try {
					
				WebResponse wrs  = (WebResponse) data;
				if (wrs.isSuccess()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map = wrs.getDataToMap();
					if(!map.get("PFTWOC_IS_START").equals("1")){

						txvPart.setText(map.get("part") + "");// 零件
						txvPartDesc.setText(map.get("RFLOT_PART_DESC") + "");
						dateDisplay.setText(map.get("RFQCLOT_DATE") + "");// 日期
						nbr.setText(map.get("RFQCLOT_WO_NBR") + ""); // 加工单
						txvWkSh.setText(map.get("RFQCLOT_WKCTR") + ""); // 车间
						txvShift.setText(map.get("SHIFT")+ ""); // 班次
						txvWkShift.addItemW(map.get("XSWO_LINE")+"",map.get("XSWO_LINE")+"");// 产线
						shift = map.get("XSWO_SHIFT")+"";
						tmp_qclot_wo_ukey =map.get("RFQCLOT_WO_UKEY")+ ""; //唯一标示
						opDesc.addItem(map.get("XSWO_OP")+"", map.get("XSWO_OP")+"");
						if (!map.get("QTY").equals("0")) {
							CuntNbr.setText(map.get("QTY").toString());
							CuntNbr.setCursorVisible(false);
							CuntNbr.setFocusable(false);
							CuntNbr.setFocusableInTouchMode(false);
						}
					}else{
						String scan = map.get("scan") + "";
						int parseInt = scan.length();
						String part = map.get("part") + "";
						String ukey = map.get("ukey")+"";
						String CUST_PART = map.get("CUST_PART") + "";
						if (!map.get("QTY").equals("0")) {
							CuntNbr.setText(map.get("QTY").toString());
							CuntNbr.setCursorVisible(false);
							CuntNbr.setFocusable(false);
							CuntNbr.setFocusableInTouchMode(false);
						}
						tmp_qclot_wo_ukey = ukey;
						showMessage1(wrs.getMessages());
						txvCPart.setText(StringUtil.isEmpty(CUST_PART, ""));
						
					}
						getFocues(CuntNbr, true);// 光标停留
						isSchBarSuc = true;
				} else {
					showErrorMsg(wrs.getMessages());
					txvBar.setText("");
					getFocues(txvBar, true); // 光标停留
					isSchBarSuc = false;
				 }
				} catch (Exception e) {
					// TODO: handle exception
					showErrorMsg(e.getMessage());
				}
			}
		});
	}
   
	
	/**
	 * 多条工序情况下，选择某条工序
	 */
	public void LotChange() {     
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {

				return CoFTheScanBiz.cof_getOp(domain, site, userid,nbr.getValStr(),txvPart.getValStr(),tmp_qclot_wo_ukey);
			}

			@Override
			public void onCallBrack(Object data) {// 下拉框赋值
				WebResponse m = (WebResponse) data;
				if (m.isSuccess()) {
					if(opDesc.getValStr()!=null)
					    opDesc.clearItems();
					Map<String ,Object> map = m.getDataToMap();
					List<Map<String , Object>> list  = (List<Map<String, Object>>) map.get("LIST");
					if(list.size() == 1){
						opDesc.addItem(StringUtil.isEmpty(list.get(0).get("XSWR_DESC")+"",list.get(0).get("XSWR_OP")+""),list.get(0).get("XSWR_OP")+"");
					}else{
					opDesc.addItem("请选工序","");
					List oplist = new ArrayList();
					for (int i = 0; i < list.size(); i++) {
						Map sMap = list.get(i);
						opDesc.addItem(StringUtil.isEmpty(sMap.get("XSWR_DESC")+"",sMap.get("XSWR_OP")+""),sMap.get("XSWR_OP")+"");
					}
				  }
				} else {
					showErrorMsg(m.getMessages());
					getFocues(txvBar, true); // 光标停留
					
				}

			}

		});
	}

	/**
	 * 获取生产线
	 * */
	
	public void getLine() {
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {

				return CoFTheScanBiz.cofGetLine(domain, site, userid);
						
			}

			@Override
			public void onCallBrack(Object data) {// 下拉框赋值
				WebResponse m = (WebResponse) data;
				if (m.isSuccess()) {
					List<Map<String, Object>> lineList = m.getDataToList();
					List<String> selList = new ArrayList<String>();
					String biaoshi = StringUtil.isEmpty(lineList.get(0).get("biaoshi"), "2");
				 if(!biaoshi.equals("1")){
				    //将list集合转化成String类型的数组,因为批次是下拉框所有要求放数组类型
					//手动输入零件获取到的批次可能有多条
					if(lineList.size() == 1){
						selList.add(lineList.get(0).get("XRUL_LINE")+"");
						String[] ub = selList.toArray(new String[selList.size()]);
						txvWkShift.addItems(ub);
						getWoRecord(ub[0]);
						
					}else{
						txvWkShift.addItem("请选择生产线","");
						for (int i = 0; i < lineList.size(); i++) {
							selList.add(lineList.get(i).get("XRUL_LINE")+"");
						}
						String[] ub = selList.toArray(new String[selList.size()]); 
						txvWkShift.addItems(ub);
						if (ub.length == 1) {
							getWoRecord(ub[0]);
						}
						isflgn = false;
					}
				 }else{
					 txvWkShift.addItem("",""); 
					 IS_GRENZEB = false;
				 }
					
				} else {
					showErrorMsg(m.getMessages());
				
					getFocues(txvBar, true); // 光标停留
					
					
				}

			}

		});
	}
	/**
	 * 进行生产线是否有开工记录验证cofGetRecord
	 * 
	 * */
	public void getWoRecord(final String wkShift) {
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {

				return CoFTheScanBiz.cofGetRecord(domain, site, userid, wkShift);
			}
			@Override
			public void onCallBrack(Object data) {// 下拉框赋值
				WebResponse m = (WebResponse) data;
				if (m.isSuccess()) {
					List<Map<String, Object>> dataToList = m.getDataToList();
					
					txvPart.setText(dataToList.get(0).get("RFQCLOT_PART") + "");// 零件
					txvPartDesc.setText(dataToList.get(0).get("PART_NM") + "");
					dateDisplay.setText(dataToList.get(0).get("RFQCLOT_DATE") + "");// 日期
					nbr.setText(dataToList.get(0).get("RFQCLOT_WO_NBR") + ""); // 加工单
					txvWkSh.setText(dataToList.get(0).get("RFQCLOT_WKCTR") + ""); // 车间
					txvShift.setText(dataToList.get(0).get("SHIFT")+ ""); // 班次
					shift = dataToList.get(0).get("RFQCLOT_SHIFT")+ "";
					tmp_qclot_wo_ukey =dataToList.get(0).get("RFQCLOT_WO_UKEY")+ ""; //唯一标示
					String qclotFPass = dataToList.get(0).get("RFQCLOT_F_PASS")+ "";
						LotChange();
					getFocues(txvBar, true); // 光标停留	
					
				  } else {
					showErrorMsg(m.getMessages());
					txvPart.setText("");// 零件
					dateDisplay.setText("");// 日期
					nbr.setText(""); // 加工单
					txvWkSh.setText(""); // 车间
					txvShift.setText(""); // 班次
					shift="";
					txvPartDesc.setText("");
					txvCPart.setText("");
					opDesc.clearItems();
					getFocues(txvBar, true); // 光标停留
					
				}

			}

		});
	}
	/**
	 * 查找产品是否按序管理
	 * */
	public void getManageType() {
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {

				return CoFTheScanBiz.cofManageType(domain, site,txvPart.getValStr() );
						
			}

			@Override
			public void onCallBrack(Object data) {// 根据返回的值给扫描栏赋值
				WebResponse m = (WebResponse) data;
				if (m.isSuccess()) {
					List<Map<String, Object>> ptvLbsList = m.getDataToList();
					String lbsType = ptvLbsList.get(0).get("RFPTV_LBS")+"";
					if(lbsType.equals("S")){
						
					}
					
				} else {
					showErrorMsg(m.getMessages());
				
					getFocues(txvBar, true); // 光标停留
					
					
				}

			}

		});
	}
	
	
	
	
	@Override
	protected void unLockNbr() {
	}

	private boolean notNull() {
		// 参数非空验证
		return true;
	}

	// 页面添加按钮
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[] { ButtonType.Return };
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
		Intent intent = new Intent(CoFTheScanActivity.this, MenuActivity.class);
		startActivity(intent);	
	}
	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return ApplicationDB.user.getUserDate();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG:
			return new DatePickerDialog(this, mdateListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	public void display() {
		dateDisplay.setText(new StringBuffer().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).append(" "));
	}

	private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			getinfor(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
		}
	};

	public void getinfor(final String fnceffdate) {
		display();
	}

}
