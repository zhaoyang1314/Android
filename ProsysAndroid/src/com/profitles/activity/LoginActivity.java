package com.profitles.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.profitles.biz.LoginBiz;
import com.profitles.biz.SysBiz;
import com.profitles.framwork.activity.base.BaseActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.network.NetworkUtil;
import com.profitles.framwork.network.UpdNewApk;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class LoginActivity extends BaseActivity {

	private LoginBiz lbiz;
	private MyEditText etxLoginId, etxLoginPwd, edtSerLogin, edtSerBiz, dateDisplay;
	private FrameLayout tabcontent;
	private CheckBox cb_mima,cb_auto;
	private MySpinner spnDomain, spnLoc;
	private Button btnLogin, btnMore , btnDateChoose;
	private TextView txvLoginVer, txtUpding;
	private int mYear, mMonth, mDay;
	private final int DATE_DIALOG = 1; 
    private SharedPreferences sp;  
    private CheckBox rem_pw, auto_login; 
	

    //将输入的字符转换为"*"
	public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {  
		@Override
	 public CharSequence getTransformation(CharSequence source, View view) {  
	             return new PasswordCharSequence(source);  
	         }  
	 private class PasswordCharSequence implements CharSequence {  
		            private CharSequence mSource;  
		             public PasswordCharSequence(CharSequence source) {  
		                 mSource = source; // Store char sequence  
		             }  
		             public char charAt(int index) {  
		                 return '*'; // This is the important part  
		             }  
		            public int length() {  
		                return mSource.length(); // Return default  
		             }  
		             public CharSequence subSequence(int start, int end) {  
		                 return mSource.subSequence(start, end); // Return default  
		            }  
		         }  
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);

		Constants.App_Version = getAppVersionName(LoginActivity.this);
		ApplicationDB.initDB(this);
		pageLoad();
		((MyTabHost) this.findViewById(R.id.login_tbladv)).setup();
		etxLoginId = (MyEditText)findViewById(R.id.edtLoginId);
		etxLoginPwd = (MyEditText)findViewById(R.id.edtLoginPwd);
		edtSerLogin = (MyEditText)findViewById(R.id.edtSerLogin);
		edtSerLogin.setText(Constants.WebEndPoint);
		etxLoginPwd.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		edtSerBiz = (MyEditText)findViewById(R.id.edtSerBiz);
		edtSerBiz.setText(Constants.WebEndPoint);
		txvLoginVer = (TextView)findViewById(R.id.txvLoginVer);
		txvLoginVer.setText("版本：" + Constants.App_Version);
		dateDisplay = (MyEditText)findViewById(R.id.dateDisplay);
		dateDisplay.setEnabled(false);
		txtUpding = (MyTextView)findViewById(R.id.txtUpding);
		
		tabcontent = (FrameLayout)findViewById(android.R.id.tabcontent);
		
		spnDomain = (MySpinner)findViewById(R.id.login_spnDomain);
		spnLoc = (MySpinner)findViewById(R.id.login_spnLoc);

		lbiz = new LoginBiz();
		
		btnDateChoose = (Button) findViewById(R.id.dateChoose);
		btnDateChoose.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				 showDialog(DATE_DIALOG);
			}
		});
		final Calendar ca = Calendar.getInstance();
	    mYear = ca.get(Calendar.YEAR);
	    mMonth = ca.get(Calendar.MONTH);
	    mDay = ca.get(Calendar.DAY_OF_MONTH);
	    
	    cb_mima=(CheckBox)findViewById(R.id.cb_mima);
	    cb_mima.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(!isChecked){ 
					auto_login.setChecked(false);
                } 
			} 
	    	
	    });
	    cb_auto=(CheckBox)findViewById(R.id.cb_auto);
	    cb_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){ 
					rem_pw.setChecked(true);
					auto_login.setChecked(true);
                }else{ 
                	auto_login.setChecked(false);
                } 
			} 
	    	
	    });
	    
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						boolean istuer = validata();
						if(dateDisplay.getValStr().trim().equals("")){
							Toast.makeText(getApplicationContext(), "请选择业务日期!", Toast.LENGTH_LONG).show();
							istuer = false;
						}
						return istuer;
					}
					@Override
					public Object onGetData() {
						lbiz.initUserInfo(etxLoginId.getValStr());
						Object tag = lbiz.login(etxLoginId.getValStr(), etxLoginPwd.getValStr(), spnDomain.getValStr(), spnLoc.getValStr(),dateDisplay.getValStr());
						if("2".equals(tag+"")){
							if(ApplicationDB.user != null){
								ApplicationDB.user.setMac(NetworkUtil.getMacFromWifi(LoginActivity.this));
							}
							//Load AppactionDB Begin
							lbiz.initSysCtrl(spnDomain.getValStr(), ApplicationDB.user.getUserSite());
							//Load AppactionDB End
							lbiz.initLoclLoop(spnLoc.getValStr(), spnDomain.getValStr(), ApplicationDB.user.getUserSite());
							lbiz.initWocCtrl(spnDomain.getValStr(), ApplicationDB.user.getUserSite());;
							
							Constants.WebEndPoint = edtSerLogin.getValStr();
						}
						return tag;
					}
					@Override
					public void onCallBrack(Object data) {
						if("2".equals(data+"")){
		                      Editor editor = sp.edit(); 
							if(rem_pw.isChecked()){  
								  //记住用户名、密码   
								  editor.putString("USER_NAME", etxLoginId.getValStr());  
		                      	  editor.putString("PASSWORD",etxLoginPwd.getValStr()); 
		                          editor.putString("LOCS",spnLoc.getValStr());  
		                      	  editor.putString("DOMAIN",spnDomain.getValStr());  
		                          editor.putString("FNCDATE",dateDisplay.getValStr());   
		                    }else{
			                      editor.putString("USER_NAME", "");  
			                      editor.putString("PASSWORD",""); 
			                      editor.putString("LOCS","");  
			                      editor.putString("DOMAIN","");  
			                      editor.putString("FNCDATE","");  
		                    }  
							if(auto_login.isChecked()){
								editor.putString("status", "true");
							}
							editor.putString("pw_status", rem_pw.isChecked()+"");
							editor.putString("al_status", auto_login.isChecked()+"");  
		                    editor.commit(); 
							forwordFinish(MenuActivity.class);
						}else if("4".equals(data+"")){
							showErrorMsg(getResources().getString(R.string.loginLocErrorMsg));
						}else{
							showErrorMsg(getResources().getString(R.string.loginErrorMsg));
						}
					}
				});
			}
		});
		
		btnMore = (Button)findViewById(R.id.btnMore);
		btnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(tabcontent != null) tabcontent.setVisibility(View.VISIBLE);
			}
		});
		
		initViewFF();

		//更新检查最新版本
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return new SysBiz().getUpdNewVer();
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				String newVer = wr.getString("PFT_ALES_VERSION", "");
				final String path =  Constants.WebUrl + "/" + wr.getString("PFT_ALES_URL", "") + "/" + wr.getString("PFT_ALES_NAME", "");
				if(!StringUtil.isEmpty(newVer) && newVer.compareTo(Constants.App_Version) > 0){
					showConfirm("发现新版本: "+newVer+"，当前版本："+ Constants.App_Version +"确认是否更新？",new OnShowConfirmListen(){
						@Override
						public void onConfirmClick() {
							new UpdNewApk(LoginActivity.this).updBegin(path);
							txtUpding.setVisibility(View.VISIBLE);
						}
						@Override
						public void onCancelClick() {
							
						}
					});
				}
			}
		});
		
		//自动登录 记住密码
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
        rem_pw = (CheckBox) findViewById(R.id.cb_mima);  
        auto_login = (CheckBox) findViewById(R.id.cb_auto);  
        //设置默认是记录密码状态   
        auto_login.setChecked(true);
    	//设置默认是自动登录状态  
        rem_pw.setChecked(true); 
        if(sp.getString("pw_status", "").equals("true")){
        	if(sp.getString("al_status", "").equals("true") && !sp.getString("status", "").equals("false")){
        		SignIn();
        	}
        }else{
        	rem_pw.setChecked(false);
        }
        if(!sp.getString("al_status", "").equals("true")){
        	auto_login.setChecked(false);
        }
        
    	etxLoginId.setText(sp.getString("USER_NAME", ""));  
    	etxLoginPwd.setText(sp.getString("PASSWORD", ""));  
    	if(!StringUtil.isEmpty(sp.getString("LOCS", ""))){
    		String[] locs = new String[]{sp.getString("LOCS", "")}; 
        	spnLoc.addAndClearItems((String[]) locs);
    	}
    	if(!StringUtil.isEmpty(sp.getString("DOMAIN", ""))){
    		String[] dom = new String[]{sp.getString("DOMAIN", "")}; 
        	spnDomain.addAndClearItems(dom);
    	}
    	dateDisplay.setText(sp.getString("FNCDATE", "")); 
	}

	/** 
	 * 返回当前程序版本名 
	 */  
	public static String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {  
	        // ---get the package info---  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	        Log.e("VersionInfo", "Exception", e);  
	    }  
	    return versionName;  
	}  
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		// _vff.addItemView(etxLoginId, etxLoginPwd);
	}

	@Override
	protected boolean onBlur(int id, View v) {
		if(id == etxLoginId.getId()){
			actPause();
			loadDataBase(new IRunDataBaseListens() {
				@Override
				public boolean onValidata() {
					return !StringUtil.isEmpty(etxLoginId.getValStr());
				}
				
				@Override
				public Object onGetData() {
					return lbiz.initUserInfo(etxLoginId.getValStr());
				}
				
				@Override
				public void onCallBrack(Object data) {
					WebResponse wr = (WebResponse)data;
					Map<String,Object> map = new HashMap<String, Object>();
					map =((WebResponse) data).getDataToMap();
					if(StringUtil.isEmpty(map.get("date"))){
						Toast.makeText(getApplicationContext(), wr.getMessages(), Toast.LENGTH_LONG).show();
					}else{
						dateDisplay.setText(map.get("date")+"");
					}
					loadSpinner();
					actGoon();
					
				}
			});
		}
		return true;
	}
	
	private void loadSpinner(){
		if(ApplicationDB.user != null){
			//** Reload Domain
			spnDomain.addAndClearItems(ApplicationDB.user.getDomains());
			//** Reload Site
			spnLoc.addAndClearItems(ApplicationDB.user.getLocs());
		}
	}
	
	protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }
	
	public void display() {
	        dateDisplay.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
	}

	private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
		
	        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
	            mYear = year;
	            mMonth = monthOfYear;
	            mDay = dayOfMonth;
	            getinfor(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
	        }
	};
	
	public void getinfor(final String fnceffdate) {
		loadDataBase(new IRunDataBaseListens() {
			public boolean onValidata() {
				return true;
			}
			
			public Object onGetData() {
				return lbiz.getinfor(spnDomain.getValStr(), spnLoc.getValStr(), fnceffdate,"");
			}
			
			public void onCallBrack(Object data) {
				WebResponse web = (WebResponse) data;
				if(!web.isSuccess()){
					showErrorMsg(web.getMessages());
				}else{
					display();
				}
			}
		});
	}
	
	public void SignIn(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				lbiz.initUserInfo(etxLoginId.getValStr());
				Object tag = lbiz.login(etxLoginId.getValStr(), etxLoginPwd.getValStr(), spnDomain.getValStr(), spnLoc.getValStr(),dateDisplay.getValStr());
				if("2".equals(tag+"")){
					if(ApplicationDB.user != null){
						ApplicationDB.user.setMac(NetworkUtil.getMacFromWifi(LoginActivity.this));
					}
					//Load AppactionDB Begin
					lbiz.initSysCtrl(spnDomain.getValStr(), ApplicationDB.user.getUserSite());
					//Load AppactionDB End
					lbiz.initLoclLoop(spnLoc.getValStr(), spnDomain.getValStr(), ApplicationDB.user.getUserSite());
					lbiz.initWocCtrl(spnDomain.getValStr(), ApplicationDB.user.getUserSite());;
					
					Constants.WebEndPoint = edtSerLogin.getValStr();
				}
				return tag;
			}
			@Override
			public void onCallBrack(Object data) {
				if("2".equals(data+"")){
                      Editor editor = sp.edit(); 
					if(rem_pw.isChecked()){  
						  //记住用户名、密码   
						  editor.putString("USER_NAME", etxLoginId.getValStr());  
                      	  editor.putString("PASSWORD",etxLoginPwd.getValStr()); 
                          editor.putString("LOCS",spnLoc.getValStr());  
                      	  editor.putString("DOMAIN",spnDomain.getValStr());  
                          editor.putString("FNCDATE",dateDisplay.getValStr());   
                    }else{
	                      editor.putString("USER_NAME", "");  
	                      editor.putString("PASSWORD",""); 
	                      editor.putString("LOCS","");  
	                      editor.putString("DOMAIN","");  
	                      editor.putString("FNCDATE","");  
                    }  
					if(auto_login.isChecked()){
						editor.putString("status", "true");
					}
					editor.putString("pw_status", rem_pw.isChecked()+"");
                    editor.putString("al_status", auto_login.isChecked()+"");  
					editor.commit(); 
					forwordFinish(MenuActivity.class);
				}else if("4".equals(data+"")){
					showErrorMsg(getResources().getString(R.string.loginLocErrorMsg));
				}else{
					showErrorMsg(getResources().getString(R.string.loginErrorMsg));
				}
			}
		});
	}
}

