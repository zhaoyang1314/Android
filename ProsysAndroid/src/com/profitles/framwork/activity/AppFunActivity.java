package com.profitles.framwork.activity;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.profitles.activity.R;
import com.profitles.framwork.activity.base.BaseActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnAppFunActivityListen;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyButton;
import com.profitles.framwork.cusviews.view.MyLinearLayout;
import com.profitles.framwork.cusviews.view.MyRelativeLayout;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.pdfUtil.FileUtils;


public abstract class AppFunActivity extends BaseActivity implements OnAppFunActivityListen{

	private MyLinearLayout rltMainHead;
	private MyRelativeLayout rltMainBody;
	private MyLinearLayout lltMainTool;
	
	private Map<ButtonType, MyButton> toolBtns;
	
	private MyTextView txvTitle, txvVersion, txvUser;
	private WifiInfo wifiInfo = null;		//获得的Wifi信息
	private WifiManager wifiManager = null;	//Wifi管理器
	private Handler wifi_handler;
	private ImageView wifi_image;			//信号图片显示
	private MyTextView wifi_info;			//信号图片显示
	private int level;						//信号强度值
	private float conn = 0f ,ping_level = 0f;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			showErrorMsg("初始化页面信息异常, Error:"+e.getMessage());
		}
	}
	
	private void init() {
		setContentView(R.layout.act_base);
		//初始化基础数据
		ApplicationDB.ReloadDB(this);
		//初始化全局对象
		initViews();
		//构建导航Title
		initHead();
		//构建Body
		initBody();
		//构建Tool中Btn
		initToolBtn();
		//超类初始化
		super.pageLoad();	//内含光标验证
		//子类可继承使用
		pageLoad();
		//初始化文本光标顺序
		initViewFF();
		// 检测wifi信号强弱
		intiSignal();
		
	}
	
	protected abstract void pageLoad();
	
	private void initViews(){
		rltMainHead = (MyLinearLayout)findViewById(R.id.rltMainHead);
		rltMainBody = (MyRelativeLayout)findViewById(R.id.rltMainBody);
		lltMainTool = (MyLinearLayout)findViewById(R.id.lltMainTool);
		
		txvTitle = (MyTextView)findViewById(R.id.txvTitle);
		txvVersion = (MyTextView)findViewById(R.id.txvVersion);
		txvUser = (MyTextView)findViewById(R.id.txvUser);
		ButtonType[] btnTypes = getNeedBtn();
		toolBtns = new HashMap<ButtonType, MyButton>();
		for (int i = 0; i < btnTypes.length; i++) {
			toolBtns.put(btnTypes[i], (MyButton)findViewById(getRecouseByName("btn"+btnTypes[i], "id")));
		}
		if(!isCleanBtn()){
			toolBtns.put(ButtonType.Return, (MyButton)findViewById(R.id.btnReturn));
			toolBtns.put(ButtonType.Help, (MyButton)findViewById(R.id.btnHelp));
		}
		// 清除MES下载的大于一个月的文件
				try {
					deleteDoc();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	private void initHead(){
		txvTitle.setText(" > " + ApplicationDB.MenuName);	//菜单数据库读取
		txvVersion.setText("("+ getAppVersion() +")");
		txvUser.setText("姓名:" + ApplicationDB.user.getUserName());
	}
	
	private void initBody(){
		rltMainBody.addView(View.inflate(this, getMainBodyLayout(), null));
	}
	
	protected MyButton getToolBtn(ButtonType bt) {
		return toolBtns.get(bt);
	}
	private void initToolBtn(){
		for(Iterator<ButtonType> it = toolBtns.keySet().iterator(); it.hasNext(); ){
			final ButtonType btt = it.next();
			MyButton _btn = toolBtns.get(btt);
			_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					try{
						onClickBody(btt, v);
						/*if(btt != ButtonType.Submit && btt != ButtonType.Save){
							
						}else{
							bty = btt;
							etx_BaseFocues.setFocusable(true);
							etx_BaseFocues.setFocusableInTouchMode(true);
							etx_BaseFocues.requestFocus();
							etx_BaseFocues.setFocusable(false);
							etx_BaseFocues.setFocusableInTouchMode(false);
						}*/
					} catch (Exception e) {
						e.printStackTrace();
						showErrorMsg("操作异常, Error:"+e.getMessage());
					}
				}
			});
			_btn.setVisibility(View.VISIBLE);
		}
	}
	
	private void deleteDoc() throws ParseException{
		FileUtils fileUtils = new FileUtils();
		List<File> listFileSortByModifyTime = fileUtils.listFileSortByModifyTime("/sdcard/profit/ProfitMES");
		for (File file : listFileSortByModifyTime) {
			file.delete();
		}
	} 
	
	private void intiSignal(){
		//图片控件初始化
		wifi_image = (ImageView) findViewById(R.id.txvImage1);
		wifi_info = (MyTextView) findViewById(R.id.txvBaseMsg4);
		// 获得WifiManager
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		// 使用定时器,每隔5秒获得一次信号强度值
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				wifiInfo = wifiManager.getConnectionInfo();
				//获得信号强度值
				level = wifiInfo.getRssi();
				//根据获得的信号强度发送信息
				if (level <= 0 && level >= -50) {
					Message msg = new Message();
					msg.what = 1;
					wifi_handler.sendMessage(msg);
				} else if (level < -50 && level >= -70) {
					Message msg = new Message();
					msg.what = 2;
					wifi_handler.sendMessage(msg);
				} else if (level < -70 && level >= -80) {
					Message msg = new Message();
					msg.what = 3;
					wifi_handler.sendMessage(msg);
				} else if (level < -80 && level >= -100) {
					Message msg = new Message();
					msg.what = 4;
					wifi_handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 5;
					wifi_handler.sendMessage(msg);
				}
 
			}
 
		}, 1000, 4000);
		// 使用Handler实现UI线程与Timer线程之间的信息传递,每5秒告诉UI线程获得wifiInto
		wifi_handler = new Handler() {
 
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				// 如果收到正确的消息就获取WifiInfo，改变图片并显示信号强度
				case 1:
					wifi_image.setImageResource(R.drawable.signal2);
					wifi_info.setText(level+""); // 页面显示信号数值
					break;
				case 2:
					wifi_image.setImageResource(R.drawable.signal2);
					wifi_info.setText(level+""); // 页面显示信号数值
					break;
				case 3:
					wifi_image.setImageResource(R.drawable.signal3);
					wifi_info.setText(level+""); // 页面显示信号数值
					break;
				case 4:
					wifi_image.setImageResource(R.drawable.signal4);
					wifi_info.setText(level+""); // 页面显示信号数值
					break;
				case 5:
					wifi_image.setImageResource(R.drawable.signal5);
					wifi_info.setText(level+""); // 页面显示信号数值
					break;
				default:
					//以防万一
					wifi_image.setImageResource(R.drawable.signal5);
					wifi_info.setText(level+""); // 页面显示信号数值
				}
			}
		};
 
	
	}
	
	protected void onClickBody(final ButtonType btt, final View v){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				switch (btt) {
				case Submit:
					return validata() && OnBtnSubValidata(btt, v);
				case Save:
					return OnBtnSaveValidata(btt, v);
				case Edit:
					return OnBtnEditValidata(btt, v);
				case Help:
					return OnBtnHelpValidata(btt, v);
				case Cancel:
					return OnBtnCanlValidata(btt, v);
				case Close:
					return OnBtnClsValidata(btt, v);
				case Return:
					return OnBtnRtnValidata(btt, v);
				case Print:
					return OnBtnPntValidata(btt, v);
				case Search:
					return OnBtnSerValidata(btt, v);
				case Clear:
					return OnBtnClrValidata(btt, v);
				case Block:
					return OnBtnBloValidata(btt, v);
				default:
					return OnBtnValidata(btt, v);
				}
			}
			@Override
			public Object onGetData() {
				switch (btt) {
				case Submit:
					return OnBtnSubClick(btt, v);
				case Save:
					return OnBtnSaveClick(btt, v);
				case Edit:
					return OnBtnEditClick(btt, v);
				case Help:
					return OnBtnHelpClick(btt, v);
				case Cancel:
					return OnBtnCanlClick(btt, v);
				case Close:
					return OnBtnClsClick(btt, v);
				case Return:
					return OnBtnRtnClick(btt, v);
				case Print:
					return OnBtnPntClick(btt, v);
				case Search:
					return OnBtnSerClick(btt, v);
				case Clear:
					return OnBtnClrClick(btt, v);
				case Block:
					return OnBtnBloClick(btt, v);
				default:
					return OnBtnClick(btt, v);
				}
			}
			
			@Override
			public void onCallBrack(Object data) {
				switch (btt) {
				case Submit:
					OnBtnSubCallBack(data); break;
				case Save:
					OnBtnSaveCallBack(data); break;
				case Edit:
					OnBtnEditCallBack(data); break;
				case Help:
					OnBtnHelpCallBack(data); break;
				case Cancel:
					OnBtnCanlCallBack(data); break;
				case Close:
					OnBtnClsCallBack(data); break;
				case Return:
					OnBtnRtnCallBack(data);
					finish();
					break;
				case Print:
					OnBtnPntCallBack(data); break;
				case Search:
					OnBtnSerCallBack(data); break;
				case Clear:
					OnBtnClrCallBack(data); break;
				case Block:
					OnBtnBloCallBack(data); break;
				default:
					OnBtnCallBack(data); break;
				}
			}
		});
	}
	
	/**
	 * 获取正文内容模型
	 * @return
	 */
	protected abstract int getMainBodyLayout();
	
	/**
	 * 获取界面需要看到的按钮
	 * @return
	 */
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ ButtonType.Submit, ButtonType.Return, ButtonType.Help };
	}
	
	public boolean OnBtnValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnSubValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnEditValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnHelpValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnCanlValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnClsValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnRtnValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnPntValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnSerValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnClrValidata(ButtonType btnType, View v) { return true; }
	public boolean OnBtnBloValidata(ButtonType btnType, View v) { return true; }
	
	public Object OnBtnClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnSubClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnSaveClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnEditClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnHelpClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnCanlClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnClsClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnRtnClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnPntClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnSerClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnClrClick(ButtonType btnType, View v) { return null; }
	public Object OnBtnBloClick(ButtonType btnType, View v) { return null; }
	
	public void OnBtnCallBack(Object data){}
	public void OnBtnSubCallBack(Object data){}
	public void OnBtnSaveCallBack(Object data){}
	public void OnBtnEditCallBack(Object data){}
	public void OnBtnHelpCallBack(Object data){}
	public void OnBtnCanlCallBack(Object data){}
	public void OnBtnClsCallBack(Object data){}
	public void OnBtnRtnCallBack(Object data){}
	public void OnBtnPntCallBack(Object data){}
	public void OnBtnSerCallBack(Object data){}
	public void OnBtnClrCallBack(Object data){}
	public void OnBtnBloCallBack(Object data){}
	
	public boolean isCleanBtn(){ return false; }

	public ButtonType bty;
	
	protected void runClickFun() {
		if(bty != null){
			onClickBody(bty, null);
			bty = null;
		}
	}

	public enum ButtonType{
		Save, Submit, Print, Search, Edit, Close, Cancel, Return, Help, Clear,Block
	}
	
	@Override
	public void finish() {
		try {
			unLockNbr();
		} catch (Exception e) {
			showErrorMsg("退出时解锁异常： Error:"+e.getMessage());
		} finally{
			super.finish();
		}
	}

	/**
	 * 用于单号解锁使用
	 */
	protected abstract void unLockNbr();
	public abstract String getAppVersion();
}
