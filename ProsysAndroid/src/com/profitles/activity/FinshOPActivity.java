package com.profitles.activity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import com.profitles.biz.FinshOpScanBiz;
import com.profitles.biz.LineWoListViewBiz;
import com.profitles.biz.WoListViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.log.LogFile;
import com.profitles.framwork.pdfUtil.FileUtils;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;
import com.readystatesoftware.viewbadger.BadgeView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsSatellite;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class FinshOPActivity extends AppFunActivity {

	private MyReadBQ txvBar;
	private MyEditText txvWkLine, txvPart,txvPartDesc,
	        txvEdtion, txvPlanQty, txvFinQty, qty, workSpace, txvCustPart, txvWonbr, txvDate, txvSn;
	private ApplicationDB applicationDB;
	private Button buttonSpace, buttonPlan, buttonOnCheck, buttonOrdCheck,
	        buttonFinsh, buttonDraw, buttonCheckPlan, buttonInstruction,buttonShifiDuty;
	private MySpinner opDesc;
	private Handler handler;
	private String WO_NBR, UKEY, LBS, CUST_PART, PLANDATE, INBROUND = "",XSLN_SUB_NUM, COUNT_NUM;   //XSLN_SUB_NUM 最少提交次数
	private FinshOpScanBiz biz;
	private String count; //已检数量
	private String count1; //在检数量
	private String state;
	private String lineOP;   //工艺流程维护表  中 的工艺流程对应的工序
//	private Timer timer;   //定时器
	private Handler TimerHandler;
	private Handler linkHander;
	private int FileLength, X, result;
    private int DownedFileLength=0;
    private ProgressBar progressBar;
    private TextView tv;
	private ProgressDialog mDialog;
	BadgeView badge = null;
	BadgeView badge1 = null;
	//1.记录时间    2.计时完工定义频率(完成该数量跳转)  3.当前时间  (毫秒)    4.比较时间   5.完成指定数量的时间
//	private long superposition,jumpFrequency,currentTime,comparingTime,fixedTime;   
//	private float piecesPerHour,piecesPerMinute;   //1.每小时多少件     2.每分钟多少件  
//	private int frequency,monitoringTime=60,startTime;    //1.计时完工定义频率(完成该数量跳转)     2.监听时间(秒为单位)固定60秒监听一次      3.班次开始时间
//	private int year,month,day,hourOfDay,minute,second;  //年  月  日   小时   分钟  秒钟
//	private Calendar calendar;
//	private String stringLong;  //用与类型转换
//	private Map<String, Object> timemap;    //计时完工时间点(时间点集合)
	private WoListViewBiz biz1;
	private LineWoListViewBiz biz2;
	String j="0";//开工记录
	private String XSWC_MJCJ="", XSLN_IS_SUB = "", XSLN_MJCX="";//免检车间
	private float all_qty = 0f;
	private String USERID;
	private String PATH_ADDRESS, XSPT_PATH_IP, XSPT_PATH_GUIDANCE_IP, DETECTION_TYPE_IP, SERVER_FILE; // 局域网服务器地址:图纸 , checkplan, 工艺指导书 , 三坐标报告, 服务器上文件夹
	private String PART_DRAW, XSRO_PATH, XSPT_PATH_GUIDANCE, SERVER_DOWNLOADS; // 文件名称:图纸 , checkplan, 工艺指导书 ,是否服务器下载
	private String[] POINT = Constants.WebEndPoint.split("/");
	private String SERVER_POINT = "http://"+POINT[2],  XSRO_PATH_GUIDANCE_NM;

    private List<Map<String, Object>> dateList2 = new ArrayList<Map<String,Object>>();
    private List<Map<String , Object>> list = new ArrayList<Map<String,Object>>();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_finshop;
	}
	
	Runnable myTimerRun=new Runnable()                //创建一个runnable对象
	{              
	        @Override
	        public void run()
	        {
	        	 try {
	        		 getCountChecked(); //获取已检数据
				      getCountChecking();//获取在检数据
	                  TimerHandler.postDelayed(this, 1000*3*60);      //再次调用myTimerRun对象，实现三分钟一次的定时器操作
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	};

	//跳转页面
	protected void jumpPages(){
		Intent intent = new Intent(FinshOPActivity.this,
				PsiActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("PART", txvPart.getValStr());
		bundle.putString("XSWO_NBR", WO_NBR);
		bundle.putString("XSWO_LINE", txvWkLine.getValStr());
		bundle.putString("XSWO_OP", opDesc.getValStr());
		bundle.putString("XSWO_UKEY", UKEY);
		bundle.putString("TXVPARTDESC", txvPartDesc.getValStr());
		bundle.putString("QTY", qty.getValStr());          //数量
		bundle.putString("TIAOMA", txvBar.getValStr());    //条码
		bundle.putString("CUSTPART", txvBar.getValStr());
		bundle.putString("EDITION", txvEdtion.getValStr());
		bundle.putString("OPERENVNM", workSpace.getValStr());   //环境
		bundle.putString("SN",txvSn.getValStr());
		intent.putExtras(bundle);
//		timer.cancel(); //关闭定时完工  (关闭线程,进入生产自检不需要此操作了,返回时再开启)
		startActivity(intent);
	}
	
	
	 /**
     * 开启定时器
     * 2019-07-31
     * Jack
     * piecesPerHour每小时多少件       frequency(完成该数量跳转)
     * monitoringTime监听时间(多久监听一次)  秒为单位
     */
	/*protected void getTime(float piecesPerHour,int frequency,int monitoringTime,int startTime) {
		timer=new Timer();
		timemap=new HashMap<String,Object>();
		piecesPerMinute=60/piecesPerHour;       //获取一件完成时间
		fixedTime=(long)StringUtil.parseInt(piecesPerMinute)*frequency;     //完成指定的数量所需要的时间
		jumpFrequency=fixedTime*60*1000;         //计时完工定义时间(单位毫秒)

		Calendar currentCalendar = Calendar.getInstance();
		int currentyear=currentCalendar.get(Calendar.YEAR);
		int currentmonth=currentCalendar.get(Calendar.MONTH) + 1; 
		int currentday=currentCalendar.get(Calendar.DATE);
		currentCalendar.set(currentyear, currentmonth, currentday, startTime, 0, 0);
		superposition=currentCalendar.getTimeInMillis();
		String string=superposition+"";
		string=string.substring(0,string.length()-4)+"0000";   //类型转换  //将毫秒位替换成0000
		superposition=Long.parseLong(string);
		for (int i = 0; i < 86400000/jumpFrequency; i++) {
			superposition+=jumpFrequency;
			timemap.put("frequency"+i, superposition);
		}
//		final long monitoring=(long)monitoringTime;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				calendar = Calendar.getInstance();
				year=calendar.get(Calendar.YEAR);
				month=calendar.get(Calendar.MONTH) + 1;
				day=calendar.get(Calendar.DATE);
				hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
				minute=calendar.get(Calendar.MINUTE);
//				second=calendar.get(Calendar.SECOND);
				calendar.set(year, month, day, hourOfDay, minute, 0);
				currentTime=calendar.getTimeInMillis();
				stringLong=currentTime+"";
				stringLong=stringLong.substring(0,stringLong.length()-4)+"0000";   //类型转换  //将毫秒位替换成0000
				currentTime=Long.parseLong(stringLong);
				if(timemap.values().contains(currentTime) && (INBROUND.equals("Y")||INBROUND.equals("1")) && !StringUtil.isEmpty(txvSn.getValStr())){
					jumpPages();
				}
//				for (int i = 0; i < timemap.size(); i++) {
//					comparingTime=(long) timemap.get("frequency"+i);
//					if(currentTime>=comparingTime&&currentTime<comparingTime+monitoring){   //判断当前时间是否在跳转时间点的范围内
//						jumpPages();
//					}
//				}
			}
		},1000*monitoringTime,1000*monitoringTime);
	}
*/
	@Override
	protected void pageLoad() {
		TimerHandler=new Handler();  
		biz = new FinshOpScanBiz();
		biz1=new WoListViewBiz();
		biz2 = new LineWoListViewBiz();
		txvBar =(MyReadBQ) findViewById(R.id.ext_scan);
		txvWkLine =(MyEditText) findViewById(R.id.etx_shift); // 产线
		opDesc =(MySpinner) findViewById(R.id.ext_cofspinner); // 工序
		txvPart =(MyEditText) findViewById(R.id.etx_part); // 零件
		txvPartDesc =(MyEditText) findViewById(R.id.etx_partDesc); // 描述
		txvEdtion =(MyEditText) findViewById(R.id.etx_Edtion); // 版本号
		txvPlanQty =(MyEditText) findViewById(R.id.etx_planqty); // 计划数
		txvFinQty =(MyEditText) findViewById(R.id.etx_finqty); // 完工数
		qty =(MyEditText) findViewById(R.id.etx_scanQty); // 数量
		workSpace =(MyEditText) findViewById(R.id.ext_spaceSpinner); // 备注
		txvCustPart =(MyEditText) findViewById(R.id.etx_Custpart); // 客户件号
		txvWonbr =(MyEditText) findViewById(R.id.etx_Wonbr); // 加工单号
		txvDate =(MyEditText) findViewById(R.id.etx_WoDate); // 日期
		txvSn =(MyEditText) findViewById(R.id.ext_sn); // SN码

		buttonSpace = (Button) findViewById(R.id.buttonSpace); // 主界面
		buttonPlan = (Button) findViewById(R.id.buttonPlan); // 主计划
		buttonOnCheck = (Button) findViewById(R.id.buttonOnCheck); // 在检
		buttonOrdCheck = (Button) findViewById(R.id.buttonOrdCheck); // 已检
		buttonFinsh = (Button) findViewById(R.id.buttonFinsh); // 下线
		buttonDraw = (Button) findViewById(R.id.buttonDraw); // 图纸
		buttonCheckPlan = (Button) findViewById(R.id.buttonCheckPlan); // 检验计划
		buttonInstruction = (Button) findViewById(R.id.buttonInstruction); // 指导书
		buttonShifiDuty = (Button) findViewById(R.id.buttonShifiDuty); // 交接班
		
		USERID = applicationDB.user.getUserId();
		// 获取主计划界面传来的值
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
		Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
		WO_NBR=bundle.getString("XSWO_NBR");//getString()返回指定key的值          加工单
		UKEY=bundle.getString("XSWO_UKEY");//getString()返回指定key的值         加工单ID
		PLANDATE = bundle.getString("PLANDATE");
		workSpace.setText(bundle.getString("SPACE"));
//		txvBar.setText(bundle.getString("TURNTIAOMA"));
		qty.setText(bundle.getString("TURNQTY"));
		LotChange(); // 加载工序
		/*if(!StringUtil.isEmpty(bundle.getString("XSWC_MJCJ"))){
			XSWC_MJCJ=bundle.getString("XSWC_MJCJ");
		}
		if(!StringUtil.isEmpty(bundle.getString("XSLN_MJCX"))){
			XSLN_MJCX=bundle.getString("XSLN_MJCX");
		}
		if(!StringUtil.isEmpty(bundle.getString("INBROUND"))){
			INBROUND=bundle.getString("INBROUND");
		}*/
		onLoadPlanInfo();
		      
		       TimerHandler.postDelayed(myTimerRun, 1000*3*60);                //启动定时器
		/**
		 * 扫码监听事件
		 * */
		txvBar.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!txvBar.getValStr().equals("") && "S".equals(LBS) && txvBar.getValStr().length() == 37){
					searchScanInfo();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		
		// 工序下拉框
		opDesc.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						if(!StringUtil.isEmpty(opDesc.getValStr())){
							getOpIsRemark(opDesc.getValStr());
						}
					}
					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
		});

		/**
		 * 主界面按钮事件
		 * */
		buttonSpace.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public Object onGetData() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void onCallBrack(Object data) {
						// TODO Auto-generated method stub

					}
				});	
			}

		} );
		/**
		 * 主计划按钮事件
		 * */
		buttonPlan.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				TimerHandler.removeCallbacks(myTimerRun);
				Intent intent = new Intent(FinshOPActivity.this, WoListViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("LINE", txvWkLine.getValStr()); 
				bundle.putString("XSWC_MJCJ", XSWC_MJCJ);  
				intent.putExtras(bundle);
//				if(timer!=null){
//					timer.cancel();   //关闭计时完工
//				}
				startActivity(intent);
			}
		} );
		/**
		 * 在检按钮事件
		 * */
		buttonOnCheck.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) { // 点击跳转到在检明细界面查看
				TimerHandler.removeCallbacks(myTimerRun);
				Intent intent = new Intent(FinshOPActivity.this, CheckingInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("XSWO_NBR", WO_NBR);  
				bundle.putString("XSWO_UKEY", UKEY); 
				bundle.putString("QTY", qty.getValStr());        //数量
				bundle.putString("txvWkLine", txvWkLine.getValStr());
				bundle.putString("OPERENVNM", workSpace.getValStr());   //环境
				if(!StringUtil.isEmpty(txvBar.getValStr())){            //条码
					CUST_PART = txvBar.getValStr().substring(4,10);
					bundle.putString("TIAOMA", txvBar.getValStr());
				}
				bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
				intent.putExtras(bundle);
//				if(timer!=null){
//					timer.cancel();   //关闭计时完工
//				}
				startActivity(intent);
			}
		} );
		/**
		 * 已检按钮事件
		 * */
		buttonOrdCheck.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {//点击跳转已检信息
				TimerHandler.removeCallbacks(myTimerRun);
				Intent intent = new Intent(FinshOPActivity.this, CheckedInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("XSWO_NBR", WO_NBR);  
				bundle.putString("XSWO_UKEY", UKEY); 
				bundle.putString("txvWkLine", txvWkLine.getValStr());
				bundle.putString("QTY", qty.getValStr());        //数量
				bundle.putString("DETECTION_TYPE_IP", DETECTION_TYPE_IP);
				bundle.putString("SERVER_DOWNLOADS", SERVER_DOWNLOADS);
				bundle.putString("SERVER_POINT", SERVER_POINT);
				if(!StringUtil.isEmpty(workSpace.getValStr())){
					bundle.putString("OPERENVNM", workSpace.getValStr());   //环境
				}
				if(!StringUtil.isEmpty(txvBar.getValStr())){           //条码
					CUST_PART = txvBar.getValStr().substring(4,10);
					bundle.putString("TIAOMA", txvBar.getValStr());
				}
				bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
				intent.putExtras(bundle);
//				if(timer!=null){
//					timer.cancel();   //关闭计时完工
//				}
				startActivity(intent);	
			}
		} );
		
		/**
		 * 下线按钮事件
		 * */
		buttonFinsh.setOnClickListener(new OnClickListener(){ // 下线按钮需要执行和自检报告相同功能
			@Override
			public void onClick(View v) {
				if(!"S".equals(LBS)){
					txvSn.setText(txvBar.getValStr());
				}
				if("S".equals(LBS) && StringUtil.isEmpty(txvBar.getValStr())){
					showErrorMsg("请扫描标签后进行下线操作");
				}else
				if("S".equals(LBS) && txvBar.getValStr().length() != 37){
					showConfirmClickCon("SN码不能为空", confirm);
				}else
				if(StringUtil.isEmpty(qty.getValStr()) || StringUtil.isEmpty(txvBar.getValStr())){
					showErrorMsg("请填写序列码或者填写数量之后进行下线操作");
				}else{
					if(!StringUtil.isEmpty(XSLN_SUB_NUM)){
						int integer=StringUtil.parseInt(txvPlanQty.getValStr())/StringUtil.parseInt(XSLN_SUB_NUM);
						float decimal=StringUtil.parseFloat(txvPlanQty.getValStr())/StringUtil.parseFloat(XSLN_SUB_NUM);
						if(StringUtil.parseInt(qty.getValStr())>(integer<decimal?integer+1:integer)){
							showErrorMsg("该产线每次下线的数量不能超过"+(integer<decimal?integer+1:integer));
							return;
						}
					}
					float pass_qty = StringUtil.parseFloat(qty.getValStr());
					float plan_qty = StringUtil.parseFloat(txvPlanQty.getValStr());
					float fin_qty = StringUtil.parseFloat(txvFinQty.getValStr());
					if(!"S".equals(LBS) && txvBar.getValStr().length() == 0){
						showErrorMsg("按批生产物料,序列号必输");
						}/*else if(pass_qty > plan_qty - fin_qty){
							showErrorMsg("下线量["+pass_qty+"]不能大于计划量["+plan_qty+"]减去已完工量["+fin_qty+"]");
						}*/else{
							if(!StringUtil.isEmpty(XSWC_MJCJ)){
								if(XSWC_MJCJ.equals("1")){
									wipCompletion();
								}/*else if (XSLN_MJCX.equals("1")){
									wipCompletion();
								}*/else if(XSLN_MJCX.equals("1") && !(INBROUND.equalsIgnoreCase("Y")||INBROUND.equalsIgnoreCase("1"))){
									// 进行入库操作,执行完工的方法
									wipCompletion();
								} else{
										TimerHandler.removeCallbacks(myTimerRun);
										Intent intent = new Intent(FinshOPActivity.this,PsiActivity.class);
										Bundle bundle = new Bundle();
										bundle.putString("PART", txvPart.getValStr());  
										bundle.putString("XSWO_NBR", WO_NBR);
										bundle.putString("XSWO_LINE", txvWkLine.getValStr());
										bundle.putString("XSWO_OP", opDesc.getValStr());
										bundle.putString("XSWO_UKEY", UKEY);
										bundle.putString("CUSTPART", txvCustPart.getValStr());
										bundle.putString("TXVPARTDESC", txvPartDesc.getValStr());
										bundle.putString("QTY", qty.getValStr());        //数量
										bundle.putString("EDITION", txvEdtion.getValStr());
										bundle.putString("OPERENVNM", workSpace.getValStr());   //环境
										bundle.putString("SN",txvSn.getValStr());
//										bundle.putString("INBROUND",INBROUND); // 是否入库
//										bundle.putString("XSLN_MJCX",XSLN_MJCX); // 是否全检
//										bundle.putString("XSWC_MJCJ",XSWC_MJCJ); // 是否全检
										
										if(!StringUtil.isEmpty(txvBar.getValStr())){         //条码
											if(txvBar.getValStr().length() == 37){
												CUST_PART = txvBar.getValStr().substring(4,10);
											}
											bundle.putString("TIAOMA", txvBar.getValStr());
										}
										intent.putExtras(bundle);
//										if(timer!=null){
//											timer.cancel();   //关闭计时完工
//										}
										startActivity(intent);
								}
							}else{
								if(INBROUND.equalsIgnoreCase("Y")||INBROUND.equalsIgnoreCase("1")){
									TimerHandler.removeCallbacks(myTimerRun);
									Intent intent = new Intent(FinshOPActivity.this,PsiActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("PART", txvPart.getValStr());  
									bundle.putString("XSWO_NBR", WO_NBR);
									bundle.putString("XSWO_LINE", txvWkLine.getValStr());
									bundle.putString("XSWO_OP", opDesc.getValStr());
									bundle.putString("XSWO_UKEY", UKEY);
									bundle.putString("TXVPARTDESC", txvPartDesc.getValStr());
									bundle.putString("CUSTPART", txvCustPart.getValStr());
									bundle.putString("QTY", qty.getValStr());        //数量
									bundle.putString("EDITION", txvEdtion.getValStr());
									bundle.putString("OPERENVNM", workSpace.getValStr());   //环境
									bundle.putString("SN",txvSn.getValStr());
//									bundle.putString("INBROUND",INBROUND); // 是否入库
//									bundle.putString("XSLN_MJCX",XSLN_MJCX); // 是否全检
//									bundle.putString("XSWC_MJCJ",XSWC_MJCJ); // 是否全检
									if(!StringUtil.isEmpty(txvBar.getValStr()) && "S".equals(LBS)){         //条码
										CUST_PART = txvBar.getValStr().substring(4,10);
										bundle.putString("TIAOMA", txvBar.getValStr());
									}else{
										bundle.putString("TIAOMA", txvBar.getValStr());
									}
									intent.putExtras(bundle);
//									if(timer!=null){
//										timer.cancel();   //关闭计时完工
//									}
									startActivity(intent);
								}else{
									// 进行入库操作,执行完工的方法
									wipCompletion();
								}
							}
					 }
				}
				
			}
		} );
		/**
		 * 图纸按钮事件
		 * */
		buttonDraw.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						return true;
					}
					@Override
					public Object onGetData() {
						return biz.getPartDraw(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(),txvPart.getValStr());
					}
					@SuppressWarnings("resource")
					@Override
					public void onCallBrack(Object data) {
						WebResponse wrb = (WebResponse) data;
						if (wrb.isSuccess()) {
							Map<String, Object> dataToMap = wrb.getDataToMap();
							String PART_DRAW = dataToMap.get("PART_DRAW") + "";
							String PATH = dataToMap.get("PATH") + "";   //获取保存的时候的文件地址
							FileUtils fileUtils = new FileUtils();
							String[] split = PART_DRAW.split("/");
							String	PART_DRAW_NM = split[4]; 
							boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/ProfitMES",PART_DRAW_NM);
							if(!fileIsExists){
								if("NO".equals(SERVER_DOWNLOADS)){
									downLoad(PART_DRAW_NM , "DRAWING", PATH_ADDRESS);
								}else{
									
									downLoadUrl(SERVER_POINT,PART_DRAW_NM, "/sdcard/profit/ProfitMES", PART_DRAW);
								}
							}else{
								Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/ProfitMES"+"/"+PART_DRAW_NM);
								startActivity(pdfFileIntent);
							}
							/*Intent intent = new Intent(FinshOPActivity.this,
									DrawPdfActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("PART_DRAW", PART_DRAW);
							bundle.putString("PATH", PATH);
							intent.putExtras(bundle);
							startActivity(intent);*/
						} else {
							showMessage(wrb.getMessages());
						}
					}

				});
			}
		} );

		/**
		 * 检验计划按钮事件  checkPlan
		 * */
		buttonCheckPlan.setOnClickListener(new OnClickListener(){
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
						return biz.getTechnologyDraw(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(),txvPart.getValStr());   //获取到图纸保存路径
					}

					@Override
					public void onCallBrack(Object data) {
						WebResponse wrb = (WebResponse) data;
						if (wrb.isSuccess()) {
							Map<String, Object> dataToMap = wrb.getDataToMap();
							String XSRO_PATH = dataToMap.get("XSPT_PATH") + "";
							String PATH = dataToMap.get("PATH")+"";  //获取文件保存地址
								String[] split = XSRO_PATH.split("/");
							String	XSRO_PATH_NM = split[4]; 
							FileUtils fileUtils = new FileUtils();
							boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/ProfitMES",XSRO_PATH_NM);
							if(!fileIsExists){
								if("NO".equals(SERVER_DOWNLOADS)){
								downLoad(XSRO_PATH_NM, "CHECKPLAN", XSPT_PATH_IP);
								}else{
									downLoadUrl(SERVER_POINT, XSRO_PATH_NM, "/sdcard/profit/ProfitMES",XSRO_PATH);
								}
							}else{
								Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/ProfitMES"+"/"+XSRO_PATH_NM);
								startActivity(pdfFileIntent);
							}
							/*Intent intent = new Intent(FinshOPActivity.this,DrawPdfActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("PART_DRAW", XSRO_PATH);
							bundle.putString("PATH", PATH);
							intent.putExtras(bundle);
							startActivity(intent);*/
						} else {
							showMessage(wrb.getMessages());
						}
					}

				});	
			}

		} );
		/**
		 * 指导书按钮事件  guidance
		 * */
		buttonInstruction.setOnClickListener(new OnClickListener(){
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
						return biz.getGuidanceDraw(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(),txvPart.getValStr());
					}

					@Override
					public void onCallBrack(Object data) {
						WebResponse wrb = (WebResponse) data;
						if (wrb.isSuccess()) {
							Map<String, Object> dataToMap = wrb.getDataToMap();
							String XSRO_PATH_GUIDANCE = dataToMap.get("XSPT_PATH_GUIDANCE") + "";
							String PATH = dataToMap.get("PATH") +"";  //获取文件保存地址
							FileUtils fileUtils = new FileUtils();
							String[] split = XSRO_PATH_GUIDANCE.split("/");
							 XSRO_PATH_GUIDANCE_NM = split[4]; 
							if("NO".equals(SERVER_DOWNLOADS)){
							}
							boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/ProfitMES",XSRO_PATH_GUIDANCE_NM);
							if(!fileIsExists){
								if("NO".equals(SERVER_DOWNLOADS)){
									downLoad(XSRO_PATH_GUIDANCE_NM, "GUIDANCEBOOK", XSPT_PATH_GUIDANCE_IP);
									}else{
									downLoadUrl(SERVER_POINT, XSRO_PATH_GUIDANCE_NM, "/sdcard/profit/ProfitMES",XSRO_PATH_GUIDANCE);
									}
							}else{
								Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/ProfitMES"+"/"+XSRO_PATH_GUIDANCE_NM);
								startActivity(pdfFileIntent);
							}
							/*Intent intent = new Intent(FinshOPActivity.this,
									DrawPdfActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("PART_DRAW", XSRO_PATH_GUIDANCE);
							bundle.putString("PATH", PATH);
							intent.putExtras(bundle);
							startActivity(intent);*/
						} else {
							showMessage(wrb.getMessages());
						}
					}
				});	
			}

		} );
		/**
		 * 自检报告按钮事件
		 * */
		buttonShifiDuty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("S".equals(LBS) && StringUtil.isEmpty(txvBar.getValStr())) {
					showErrorMsg("请扫描标签后进行下线操作");
				} else if (StringUtil.isEmpty(qty.getValStr())) {
					showErrorMsg("请扫描标签或者填写数量之后进行下线操作");
				} else {
					Intent intent = new Intent(FinshOPActivity.this,
							PsiActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("PART", txvPart.getValStr());
					bundle.putString("XSWO_NBR", WO_NBR);
					bundle.putString("XSWO_LINE", txvWkLine.getValStr());
					bundle.putString("XSWO_OP", opDesc.getValStr());
					bundle.putString("XSWO_UKEY", UKEY);
					bundle.putString("TXVPARTDESC", txvPartDesc.getValStr());
					bundle.putString("QTY", qty.getValStr());     //数量
					bundle.putString("TIAOMA", txvBar.getValStr());      //条码
					bundle.putString("CUSTPART", txvBar.getValStr());     
					bundle.putString("OPERENVNM", workSpace.getValStr());   //环境
					bundle.putString("EDITION", txvEdtion.getValStr());
					intent.putExtras(bundle);
//					if(timer!=null){
//						timer.cancel();   //关闭计时完工
//					}
					startActivity(intent);
				}
			}

		});
		
	}
	
	
	 /**
     * 从局域网的共享服务器上面下载文件到本地
     * 
     * */
	
  final int SUCCESS=1;
  final int FAIL=0;
	private void downLoad(final String name, final String department, final String addrss) {
		new Thread(new Runnable() {
            @Override
            public void run() {
			    	InputStream in = null ; 
			    	 Message msg = new Message();
					try {  
					    //创建远程文件对象  smb://用户名:密码@ip地址/共享的路径/...
					    String remotePhotoUrl = "smb://"+ addrss + name;  
					    SmbFile remoteFile = new SmbFile(remotePhotoUrl);  
					    try {
							remoteFile.connect(); //尝试连接  
						} catch (Error e) {
							e.printStackTrace();
							msg.what = 0;
							linkHander.sendMessage(msg);
						}
					    FileUtils fileUtils = new FileUtils();
					   boolean copyRemoteFile = fileUtils.copyRemoteFile(remoteFile, "/sdcard/profit/"+applicationDB.user.getUserId()+"/"+department+"/");
					   if(!copyRemoteFile){
						  msg.what = 2;
						linkHander.sendMessage(msg);
					   }else{
							msg.what = 1;
							linkHander.sendMessage(msg);
						}
					    
					}  
					catch (Exception e) {  
						msg.what = 0;
						linkHander.sendMessage(msg);
					}  
					finally {  
					    try {  
					        if(in != null) in.close();  
					    }  
					    catch (Exception e) {
					    	
					    }  
					}
             }
           }).start();
    	   linkHander =new Handler(){
    	        @Override
    	        public void handleMessage(Message msg) {
    	            super.handleMessage(msg);
    	            switch (msg.what) {
    	                case 1:
    	                	try {
		                		Thread.sleep(10000);
		                	} catch (InterruptedException e) {
		                		// TODO Auto-generated catch block
		                		e.printStackTrace();
		                	}
    	                	FileUtils fileUtils = new FileUtils();
    	                	Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/"+applicationDB.user.getUserId()+"/"+department+"/"+ name);
							startActivity(pdfFileIntent);
    	                    break;
    	                case 0:
    	                    Toast.makeText(FinshOPActivity.this, "连接共享服务器失败", Toast.LENGTH_SHORT).show();
    	                    break;
    	                case 2:
    	                    Toast.makeText(FinshOPActivity.this, "拷贝远程文件到本地目录失败:文件不存在", Toast.LENGTH_SHORT).show();
    	                    break;
    	                default:
    	                    super.handleMessage(msg);
    	            }
    	        }
    	    };
		
	}


	 /**
     * 从服务器下载文件
     * @param path 下载文件的地址
     * @param FileName 文件名字
     */
    @SuppressWarnings("deprecation")
	@SuppressLint("HandlerLeak")
	private  void downLoadUrl(final String point, final String name , final String localDirectory , final String serverfilepath) {
    			final FileUtils fileUtils = new FileUtils();
	      		mDialog = new ProgressDialog(FinshOPActivity.this);
	      		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置风格为圆形进度条
	      		mDialog.setMax(100);
	      		mDialog.setMessage("文件下载中....");
	      		mDialog.setIndeterminate(false);//设置进度条是否为不明确
	      		mDialog.setCancelable(false);//设置进度条是否可以按退回键取消
	      		mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
	      			@Override
	      			public void onDismiss(DialogInterface dialog) {
	      				// TODO Auto-generated method stub
	      				mDialog=null;
	      			}
	      		});
	      		mDialog.setButton("终止下载", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							if(mDialog!=null){
						    	mDialog.dismiss();
						    	mDialog=null;
						    	}
						}
						
	      		});
	      		
    	 new Thread(new Runnable() {
             @Override
             public void run() {
				FileOutputStream fileOutputStream = null;//文件输出流
				HttpURLConnection con = null;
				 Message msg = new Message();
				try {
	             	URL url = new URL(point+"/URL/fileupload"+ serverfilepath);
	             	con = (HttpURLConnection) url.openConnection();
	             	con.setReadTimeout(5000);
	             	con.setConnectTimeout(5000);
	             	con.setRequestProperty("Charset", "UTF-8");
	             	con.setRequestMethod("GET");
	             	File[] localFiles = new File(localDirectory).listFiles();
	                  if (null == localFiles) {
	                      // 目录不存在的话,就创建目录
	                      new File(localDirectory).mkdirs();
	                  }
	                  File path1 = new File(localDirectory);
	                  FileLength = con.getContentLength();
	                  DownedFileLength=0;
	                 int f = 0;
	                 if (con.getResponseCode() == 200) {
	                     InputStream is = con.getInputStream();//获取输入流
	                     // 暂时注释文档验证功能，
						/*boolean check = fileUtils.check(point+"/URL/fileupload"+ serverfilepath); // 判断服务器上面的文件是否损坏
	                     if(!check){
	                    	 Message _msg3 = new Message();
	                    	 mDialog.cancel();
						     mDialog=null;
	                    	 _msg3.what = -1;
	                    	 linkHander.sendMessage(_msg3);
	                     }else*/
	                     if (is != null) {
	                    	 File createTempFile = File.createTempFile(name, ".pft",path1);
	                    	  fileOutputStream =  new FileOutputStream(createTempFile);
	                         byte[] buf = new byte[1024];
	                         int ch;
	                         while ((ch = is.read(buf)) != -1) {
	                             fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
	                             DownedFileLength += ch;
	                            	Message _msg1 = new Message();
	                            	_msg1.what = 3;
	                            	linkHander.sendMessage(_msg1);
	                            if(mDialog != null)	{
	                            	mDialog.setProgress(result);
	                            }
	                         }
	                         File newFile = new File("/"+localDirectory+"/"+ name);
	                         boolean renameTo = createTempFile.renameTo(newFile);
	                     }
	                     if(mDialog != null){
	                    	 mDialog.cancel();
	                    	 msg.what = 1;
	                    	 linkHander.sendMessage(msg);
	                     }
	                     if (fileOutputStream != null) {
	                         fileOutputStream.flush();
	                         fileOutputStream.close();
	                     }
	                 }else{
	                	 mDialog.cancel();
	                	 msg.what = 0;
	                     linkHander.sendMessage(msg);
	                 }
				}catch(Exception e){
					mDialog.cancel();
					e.getMessage();
					 msg.what = 2;
                     linkHander.sendMessage(msg);
				}
             }
             }).start();
    	 mDialog.show();
		        linkHander =new Handler(){
			        @Override
			        public void handleMessage(Message msg) {
			            super.handleMessage(msg);
			            switch (msg.what) {
				            case 0:
				            	Toast.makeText(FinshOPActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
				            	break;
			                case 1:
			                	try {
			                		Intent pdfFileIntent = fileUtils.getPdfFileIntent(localDirectory+ "/" + name);
			                		startActivity(pdfFileIntent);
			                	} catch (Exception e) {
			                		Toast.makeText(FinshOPActivity.this, "文件下载未完成,无法打开,转为后台下载", Toast.LENGTH_SHORT).show();
			                		e.printStackTrace();
			                	}
			                    break;
			                case -1:
				            	Toast.makeText(FinshOPActivity.this, "服务器文件损坏,无法进行下载,请确认服务器文件是否有效", Toast.LENGTH_SHORT).show();
				            	break;
			                case 2:
			                    Toast.makeText(FinshOPActivity.this, "从服务器下载文件异常", Toast.LENGTH_SHORT).show();
			                    break;
			                case 3:
			                     result=DownedFileLength*100/FileLength;
			                    break;
			                default:
			                    super.handleMessage(msg);
			            }
			        }
			    };
    }
   
	
	/**
	 * 下线入库事件
	 * */
	private void wipCompletion(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return biz.finshOPMethod(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),applicationDB.user.getUserId(),
						txvBar.getValStr(),WO_NBR,txvPart.getValStr(),UKEY,applicationDB.user.getUserDate(),qty.getValStr(), opDesc.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;		
				if(wrs.isSuccess()){
					if(StringUtil.parseInt(COUNT_NUM) != 0){
						txvBar.setText("");
						all_qty  = all_qty + StringUtil.parseFloat(qty.getValStr());
						txvFinQty.setText(all_qty+"");
						getFocues(txvBar, true);
						showMessage1(wrs.getMessages());
					}/*else
					if (!("1".equals(XSLN_MJCX) && !("1".equals(INBROUND)))){
						txvBar.setText("");
						all_qty  = all_qty + StringUtil.parseFloat(qty.getValStr());
						txvFinQty.setText(all_qty+"");
						getFocues(txvBar, true);
						showMessage1(wrs.getMessages());
					}else if("1".equals(XSLN_MJCX) && list.size() == 1){
						txvBar.setText("");
						all_qty  = all_qty + StringUtil.parseFloat(qty.getValStr());
						txvFinQty.setText(all_qty+"");
						getFocues(txvBar, true);
						showMessage1(wrs.getMessages());
					}*/else{
						getFocues(txvBar, true);
						showConfirmClickCon(wrs.getMessages(), confirm);
					}
				}else{
//					txvBar.setText("");
					getFocues(txvBar, true);
					showConfirmClickCon(wrs.getMessages(), confirm);
				}
			}		
		});
	}
	private void getCountChecked(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public Object onGetData() {
				state="1";
				return biz.getCountCheckinfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),state, txvWkLine.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;		
				if(wrs.isSuccess()){
					count=wrs.getWData().toString();
					getSearchMessage1();
				}else{
					showErrorMsg(wrs.getMessages());
				}
			}		
		});
	}
	
	@Override
	protected boolean onBlur(int id, View v) {
		
		if(!"S".equals(LBS)){
			if(txvBar.getId() == id){
					if(txvBar.getValStr().length() != 7){
						showConfirmClickCon("按批管理物料的序列码长度为7", confirm);
//						txvBar.setText("");
						return false;
					}
				}
			txvSn.setText(txvBar.getValStr());
		}/*else{
			if(txvBar.getId() == id){
				if(!txvBar.getValStr().equals("")){
					searchScanInfo();
				}
			}
		}*/
		return true;
		
	};
	

	int k = 0;
	private void getCountChecking(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public Object onGetData() {
				state="0";
				return biz.getCountCheckinfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),state, txvWkLine.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;		
				if(wrs.isSuccess()){
					count1=wrs.getWData().toString();
					if(!StringUtil.isEmpty(badge1)){
						badge1.setVisibility(View.GONE);
		 		        }
					getSearchMessage();
				}else{
					showErrorMsg(wrs.getMessages());
				}
			}
		});	 
	}
	private  void getSearchMessage1(){
		 View btn1 = new View(this);
        btn1=buttonOrdCheck;
        badge = new BadgeView(this, btn1);
        badge.setTextSize(8);
        badge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT); 
        if(!count.equals("0")){
	    	 badge.setText(count);
	    	 badge.show(); 
	    }else{
	    	badge.hide();
	    }
        //buttonOrdCheck.setWidth(buttonPlan.getWidth() - 20);
	}
	private  void getSearchMessage(){
//		View btn = new View(this);
//	    btn = buttonOnCheck;
	    badge1 = new BadgeView(this, buttonOnCheck);
	    badge1.setTextSize(8);
	    badge1.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT); 
	    if(!count1.equals("0")){
	    	 badge1.setText(count1);
	    	 badge1.show(); 
	    }else{
	    	badge1.hide();
	    }
	    //buttonOnCheck.setWidth(buttonPlan.getWidth() - 20);
	}

	/**
	 * 点击主计划,进入主界面,加载信息
	 * 2019/07/01
	 * wade
	 * */

	private void onLoadPlanInfo(){

		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() { // 加载后台数据
				return biz.seaPlanInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(), WO_NBR, UKEY); 
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					if(!StringUtil.isEmpty(XSWC_MJCJ)){
						if(XSWC_MJCJ.equals("1")){
							// 禁用自检
							buttonShifiDuty.setEnabled(false);
						}else{
							buttonShifiDuty.setEnabled(true);
						}
					}else{
						buttonShifiDuty.setEnabled(true);
					}
					Map<String, Object> dataToMap = wrs.getDataToMap();
					txvWkLine.setText(dataToMap.get("XSWO_LINE")+"");
					txvPart.setText(dataToMap.get("XSWO_PART")+"");
					txvPartDesc.setText(dataToMap.get("PART_NM")+"");
					txvEdtion.setText(dataToMap.get("XSWO_REV")+"");
					txvPlanQty.setText(dataToMap.get("XSWO_QTY_ORD")+"");
					txvFinQty.setText(dataToMap.get("XSWO_QTY_COMP")+"");
					XSLN_IS_SUB = dataToMap.get("XSLN_IS_SUB")+"";
					XSLN_MJCX = dataToMap.get("XSLN_MJCX") +"";
					XSWC_MJCJ = dataToMap.get("XSWO_MJCJ") +"";
					XSLN_SUB_NUM = dataToMap.get("XSLN_SUB_NUM")+"";
					
					PART_DRAW = 	dataToMap.get("PART_DRAW")+""; // 图纸
					XSRO_PATH = 	dataToMap.get("XSPT_PATH")+""; // checkplan
					XSPT_PATH_GUIDANCE = 	dataToMap.get("XSPT_PATH_GUIDANCE")+""; // 工艺指导书
					
					SERVER_DOWNLOADS = dataToMap.get("SERVER_DOWNLOADS")+"";
					if("NO".equals(SERVER_DOWNLOADS)){
						PATH_ADDRESS = 	dataToMap.get("PATH_ADDRESS")+""; // 图纸
						XSPT_PATH_IP =  dataToMap.get("XSPT_PATH_IP")+""; // checkplan
						XSPT_PATH_GUIDANCE_IP =  dataToMap.get("XSPT_PATH_GUIDANCE_IP")+""; // 工艺指导书
						DETECTION_TYPE_IP = dataToMap.get("DETECTION_TYPE_IP")+""; // 工艺指导书
					    onloadPDF(); // 共享服务器局域网下载文件
					}else{
						SERVER_FILE = dataToMap.get("PATH")+""; // 服务器上的存储地址文件夹
						onloadPDFURL(); // 阿里云服务器端下载文件
					}
					
					all_qty = StringUtil.parseFloat(dataToMap.get("XSWO_QTY_COMP")+"");
					if(XSWC_MJCJ.equals("1")  || "1".equals(XSLN_IS_SUB) || "0".equals(INBROUND)){
					}
					if(StringUtil.isEmpty(PLANDATE)){
						txvDate.setText(dataToMap.get("XSWO_REL_DATE")+"");
					}else{
						txvDate.setText(PLANDATE);
					} 
					txvWonbr.setText(WO_NBR);
//					piecesPerHour=StringUtil.parseFloat(dataToMap.get("PIECESPERHOUS"));   //每小时多少件
//					frequency=StringUtil.parseInt(dataToMap.get("PFTWOC_TCF"));     //计时完工跳转频率(完成该数量跳转)
//					startTime=StringUtil.parseInt(dataToMap.get("PFTWOC_STOS"));   //班次开始时间
					LBS = dataToMap.get("LBS")+"";
					/**
					 * 取消计时完工
					 */
					if(!"S".equals(LBS)){
						txvCustPart.setText(dataToMap.get("XSWO_CUST_PART")+"");
////						txvBar.setVisibility(View.GONE);
						getFocues(txvBar, true);
//						if(frequency>0){
//							/**
//							 * PIECESPERHOUS 每小时多少件
//							 * frequency 跳转频率 
//							 * monitoringTime监听的频率  秒为单位
//							 * startTime   班次开始时间
//							 */
//							getTime(piecesPerHour,frequency,monitoringTime,startTime);         
//						}else{
//							showMessage(wrs.getMessages());
//						}
					}else{
						qty.setText("1");
						qty.setFocusableInTouchMode(false);
						getFocues(txvBar, true);
					}
//					getLineIsMJ();
					 getCountChecked(); //获取已检数据
				     getCountChecking();//获取在检数据
				 
				}else{
					showErrorMsg(wrs.getMessages());
				}
			}
		});	
	}
	/**
	 * 进行加载图纸使用
	 * */
	public void onloadPDF(){
		if(!StringUtil.isEmpty(PART_DRAW) && !StringUtil.isEmpty(PATH_ADDRESS)){
			FileUtils fileUtils = new FileUtils();
			boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/"+applicationDB.user.getUserId()+"/DRAWING",PART_DRAW);
			if(!fileIsExists){
				fileUtils.downLoad(PART_DRAW , "DRAWING",PATH_ADDRESS);
			}
		}
		if(!StringUtil.isEmpty(XSRO_PATH) && !StringUtil.isEmpty(XSPT_PATH_IP)){
			FileUtils fileUtils = new FileUtils();
			boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/"+applicationDB.user.getUserId()+"/CHECKPLAN",XSRO_PATH);
			if(!fileIsExists){
				fileUtils.downLoad(XSRO_PATH, "CHECKPLAN", XSPT_PATH_IP);
			}
		}
		if(!StringUtil.isEmpty(XSPT_PATH_GUIDANCE) && !StringUtil.isEmpty(XSPT_PATH_GUIDANCE_IP)){
			FileUtils fileUtils = new FileUtils();
			boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/"+applicationDB.user.getUserId()+"/GUIDANCEBOOK",XSPT_PATH_GUIDANCE);
			if(!fileIsExists){
				fileUtils.downLoad(XSPT_PATH_GUIDANCE, "GUIDANCEBOOK", XSPT_PATH_GUIDANCE_IP);
			}
		}
		
	}
	
	/**
	 * 进行加载各个图纸
	 * */
	String localDirectory = "/sdcard/profit/ProfitMES";
	public void onloadPDFURL(){
		if(!StringUtil.isEmpty(PART_DRAW) && !StringUtil.isEmpty(SERVER_FILE)){
			FileUtils fileUtils = new FileUtils();
			String[] split = PART_DRAW.split("/");
			boolean fileIsExists = fileUtils.fileIsExists(localDirectory+"/",split[4]);
			if(!fileIsExists){
				fileUtils.downLoadUrl(SERVER_POINT ,split[4], localDirectory, PART_DRAW);
			}
		}
		if(!StringUtil.isEmpty(XSRO_PATH) && !StringUtil.isEmpty(SERVER_FILE)){
			FileUtils fileUtils = new FileUtils();
			String[] split = XSRO_PATH.split("/");
			boolean fileIsExists = fileUtils.fileIsExists(localDirectory+"/",split[4]);
			if(!fileIsExists){
				fileUtils.downLoadUrl(SERVER_POINT, split[4], localDirectory, XSRO_PATH);
			}
		}
		if(!StringUtil.isEmpty(XSPT_PATH_GUIDANCE) && !StringUtil.isEmpty(SERVER_FILE)){
			FileUtils fileUtils = new FileUtils();
			String[] split = XSPT_PATH_GUIDANCE.split("/");
			boolean fileIsExists = fileUtils.fileIsExists(localDirectory+"/",split[4]);
			if(!fileIsExists){
				fileUtils.downLoadUrl(SERVER_POINT, split[4], localDirectory, XSPT_PATH_GUIDANCE);
			}
		}
		
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

				return biz.cof_getOp(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(),WO_NBR,txvPart.getValStr(),UKEY);
			}

			@Override
			public void onCallBrack(Object data) {// 下拉框赋值
				WebResponse m = (WebResponse) data;
				if (m.isSuccess()) {
//					if(opDesc.getValStr()!=null)
				    opDesc.clearItems();
					Map<String ,Object> map = m.getDataToMap();
					 list  = (List<Map<String, Object>>) map.get("LIST");
					if(list.size() == 1){
						opDesc.addItem(StringUtil.isEmpty(list.get(0).get("XSWR_DESC")+"",list.get(0).get("XSWR_OP")+""),list.get(0).get("XSWR_OP")+"");
//						getOpIsRemark(opDesc.getValStr());
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
	// 查询工序是不是入库工序
	private void getOpIsRemark(final String op){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.opIsNotRemark(applicationDB.user.getUserDmain(), op, UKEY);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse web = (WebResponse) data;
				if(web.isSuccess()){
					Map<String, Object> dataToMap = web.getDataToMap();
					INBROUND = dataToMap.get("INBROUND") +"";
					COUNT_NUM = dataToMap.get("COUNT_NUM") +"";
				}
			}
			
		});
	}
	
	//查询产线是否免检
	private void getLineIsMJ(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.seaCxInfo(applicationDB.user.getUserDmain(),  applicationDB.user.getUserSite(), txvWkLine.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse web = (WebResponse) data;
				if(web.isSuccess()){
					Map<String, Object> dataToMap = web.getDataToMap();
					XSLN_MJCX = dataToMap.get("XSLN_MJCX") +"";
				}
			}
			
		});
	}
	
	/**
	 * 扫描标签查询信息
	 * */
	void searchScanInfo(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if(StringUtil.isEmpty(opDesc.getValStr())){
					showConfirmClickCon("选择工序后进行扫描", confirm);
//					txvBar.setText("");
					return false;
				}
				if(!StringUtil.isEmpty(txvEdtion.getValStr()) && txvBar.getValStr().length() >=37){
					String substring = txvBar.getValStr().substring(17, 19);
					if(!txvEdtion.getValStr().equals(substring)){
						txvBar.setText("");
						showMessage("标签的版本与零件最新版本不同,不允许扫描");
						return false;
					}
				}
				return true;
			}

			@Override
			public Object onGetData() {
				return biz.seaScanInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(),
						txvBar.getValStr(), opDesc.getValStr(), txvWkLine.getValStr(), UKEY, txvPart.getValStr());
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					CUST_PART = txvBar.getValStr().substring(4,10);
					txvCustPart.setText(CUST_PART);
					String substring = txvBar.getValStr().substring(19, 25);
					txvSn.setText(substring);
					
					if("1".equals(XSLN_IS_SUB)){
						// 进行入库操作,执行完工的方法
						wipCompletion();
					}
					/*if(!StringUtil.isEmpty(XSWC_MJCJ)){
						if(XSWC_MJCJ.equals("1")){
							// 进行入库操作,执行完工的方法
							wipCompletion();
						}else{
							Intent intent = new Intent(FinshOPActivity.this, PsiActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("PART", txvPart.getValStr());  
							bundle.putString("XSWO_NBR", WO_NBR);
							bundle.putString("XSWO_LINE", txvWkLine.getValStr());
							bundle.putString("XSWO_OP", opDesc.getValStr());
							bundle.putString("XSWO_UKEY", UKEY);
							bundle.putString("TXVPARTDESC", txvPartDesc.getValStr());
							bundle.putString("QTY", qty.getValStr());    //数量
							bundle.putString("TIAOMA", txvBar.getValStr());   //条码
							bundle.putString("OPERENVNM", workSpace.getValStr());   //环境
							bundle.putString("CUSTPART", CUST_PART);
							bundle.putString("EDITION", txvEdtion.getValStr());
							intent.putExtras(bundle);
							if(timer!=null){
								timer.cancel(); //关闭定时完工
							}
							startActivity(intent);
						}
					}else{
						Intent intent = new Intent(FinshOPActivity.this, PsiActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("PART", txvPart.getValStr());  
						bundle.putString("XSWO_NBR", WO_NBR);
						bundle.putString("XSWO_LINE", txvWkLine.getValStr());
						bundle.putString("XSWO_OP", opDesc.getValStr());
						bundle.putString("XSWO_UKEY", UKEY);
						bundle.putString("TXVPARTDESC", txvPartDesc.getValStr());
						bundle.putString("QTY", qty.getValStr());    //数量
						bundle.putString("TIAOMA", txvBar.getValStr());   //条码
						bundle.putString("OPERENVNM", workSpace.getValStr());   //环境
						bundle.putString("CUSTPART", CUST_PART);
						bundle.putString("EDITION", txvEdtion.getValStr());
						intent.putExtras(bundle);
						if(timer!=null){
							timer.cancel(); //关闭定时完工
						}
						startActivity(intent);
					}*/
				}else{
					showConfirmClickCon(wr.getMessages(), confirm);
					getFocues(txvBar, true);
				}
			}
		});
	}
	private OnShowConfirmListen confirm=new OnShowConfirmListen() {
		@Override
		public void onConfirmClick() {   //点击确定
			txvBar.setText("");
		}
		@Override
		public void onCancelClick() {    //点击取消
			
		}
	};

	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
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
		TimerHandler.removeCallbacks(myTimerRun);
//		if(timer!=null){
//			timer.cancel(); //关闭定时完工
//		}
		return null;
	}
	@Override
	public void OnBtnRtnCallBack(Object data) {
		Intent intent = new Intent(FinshOPActivity.this, WoListViewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("LINE", txvWkLine.getValStr());
		bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}
    
}
