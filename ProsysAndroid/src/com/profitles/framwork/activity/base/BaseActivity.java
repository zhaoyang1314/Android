package com.profitles.framwork.activity.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.profitles.activity.R;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.base.rdb.RunDataBase;
import com.profitles.framwork.activity.forword.ActForwordMgr;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.cusviews.view.bean.ValidataBean;
import com.profitles.framwork.cusviews.view.interfaces.IMyBaseView;
import com.profitles.framwork.cusviews.view.interfaces.IMyEditText;
import com.profitles.framwork.music.SoundPlayer;
import com.profitles.framwork.read.ReadSpackingBase;
import com.profitles.framwork.read.unonline.UnLReadSpacking;
import com.profitles.framwork.util.StringUtil;
import com.profitles.framwork.util.Constants;

public class BaseActivity extends Activity {
	
	private ActForwordMgr afm = new ActForwordMgr();
	private MyTextView txvBaseMsg;
	private ViewFocusForward vff;
	protected SoundPlayer splayer;
	protected MyEditText etx_BaseFocues;
	private ReadSpackingBase rsk;
	
	protected int getRecouseByName(String name, String type){
		 return getResources().getIdentifier(name, type, this.getPackageName());
	}
	
	private void showShortMessage(int msgId) {
		showShortMessage(getResources().getString(msgId));
	}
	
	private void showShortMessage(String msg) {
		Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	private void showTextMessage(int msgId) {
		showTextMessage(getResources().getString(msgId));
	}

	private void showTextMessage(String msg) {
		txvBaseMsg = txvBaseMsg == null ? (MyTextView)findViewById(R.id.txvBaseMsg) : txvBaseMsg;
		if(txvBaseMsg != null) txvBaseMsg.setText(msg);
	}
	
	protected void showErrorMsg(int msgId) {
//		showShortMessage(msgId);
//		showTextMessage(msgId);
		showWaring(getResources().getString(msgId), null);
		runError();
	}
	
	protected void showErrorMsg(String msg) {
//		showShortMessage(msg);
//		showTextMessage(msg);
		/*if("".equals(msg)){
			msg="响应超时，请重新尝试";
		}*/
		showWaring(msg, null);
		runError();
	}
	
	public ReadSpackingBase getRsk() {
		//new ReadSpacking(this)
		rsk = rsk == null ? new UnLReadSpacking(this) : rsk;
		return rsk;
	}

	protected void showMessage(int msgId) {
		showShortMessage(msgId);
		showTextMessage(msgId);
	}
	
	protected void showMessage(String msg) {
		showShortMessage(msg);
		showTextMessage(msg);
	}
	protected void showMessage1(String msg) {
		showShortMessage(msg);
	
	}
	protected void showWarningMsg(int msgId) {
		showShortMessage(msgId);
		showTextMessage(msgId);
		runWarning();
	}
	
	protected void showWarningMsg(String msg) {
		showShortMessage(msg);
		showTextMessage(msg);
		runWarning();
	}
	
	protected void showSuccessMsg(int msgId) {
		showShortMessage(msgId);
		showTextMessage(msgId);
		runSuccess();
	}
	
	protected void showSuccessMsg(String msg) {
		showShortMessage(msg);
		showTextMessage(msg);
		runSuccess();
	}
	
	protected void clearMsg() {
		showTextMessage("");
	}
	
	private SoundPlayer getSPlayer(){
		splayer = splayer == null ? new SoundPlayer(getBaseContext()) : splayer;
		return splayer;
	}
	
	protected void runSuccess(){
		getSPlayer().success();
	}
	
	protected void runError(){
		getSPlayer().error();
	}
	
	protected void runWarning(){
		getSPlayer().warning();
	}
	
	protected void showConfirm(String msg, final OnShowConfirmListen oscfListen){
		// SN006 语音提示修改 2019-07-09 By Brenna 
		if(Constants.isReadMes){
			getRsk().spacking(msg);
		}else{
			getRsk().spacking(StringUtil.isEmpty(Constants.WaringMes, "系统提示"));
		}
		runWarning();
		new AlertDialog.Builder(this)
		.setTitle("系统提示")
		.setMessage(msg)
		.setIcon(R.drawable.sys_dialog_waring)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				oscfListen.onConfirmClick();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				oscfListen.onCancelClick();
			}
		}).show().setCanceledOnTouchOutside(false);
	}
	
	protected void showConfirmClickCon(String msg, final OnShowConfirmListen oscfListen){
		// SN006 语音提示修改 2019-07-09 By Brenna 
		if(Constants.isReadMes){
			getRsk().spacking(msg);
		}else{
			getRsk().spacking(StringUtil.isEmpty(Constants.WaringMes, "系统提示"));
		}
		runWarning();
		new AlertDialog.Builder(this)
		.setTitle("系统提示")
		.setMessage(msg)
		.setIcon(R.drawable.sys_dialog_waring)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				oscfListen.onConfirmClick();
			}
		}).show().setCanceledOnTouchOutside(false);
		
	}
	
	private void showWaring(String msg, DialogInterface.OnClickListener click){
		// SN006 语音提示修改 2019-07-09 By Brenna 
		if(Constants.isReadMes){
			getRsk().spacking(msg);
		}else{
			getRsk().spacking(StringUtil.isEmpty(Constants.ErrorMes, "错误提示"));
		}
		new AlertDialog.Builder(this)
			.setTitle("错误提示")
			.setMessage(msg)
			.setIcon(R.drawable.sys_dialog_error)
			.setNegativeButton("确定", click)
			.show();
	}
	
	protected void loadDataBase(IRunDataBaseListens _rdbl){
		if(_rdbl.onValidata())
			new RunDataBase(_rdbl, ProgressDialog.show(this, null, "处理中，请稍后...", true))
				.start();
	}
	
	protected void forword(Class<? extends Activity> to) {
		afm.forword(this, to);
	}
	
	protected void forwordFinish(Class<? extends Activity> to) {
		afm.forwordFinish(this, to);
	}
	
	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getRsk().destroy();
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, R.string.doubleClickExit, Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
		} else {
			afm.exit();
			finish();
			System.exit(0);
		}
	}
	private boolean isLockFocusEvent = false;
	protected void pageLoad() {
		etx_BaseFocues = (MyEditText)findViewById(R.id.etx_BaseFocues);
		List<View> ls = getAllChildViews();
		for(int i = 0; i < ls.size(); i++){
			if (ls.get(i) instanceof IMyBaseView) {
				final IMyBaseView mbv = ((IMyBaseView)ls.get(i));
				final ValidataBean vb = mbv.getVdbean();
				if(ls.get(i) instanceof IMyEditText){
					IMyEditText ext = (IMyEditText)ls.get(i);
					final int id = ext.getId();
					ext.setOnFocusChangeListener(new OnFocusChangeListener(){
						@Override
						public void onFocusChange(final View v, boolean hasFocus) {
							try {
								if(!isLockFocusEvent && (etx_BaseFocues == null || (etx_BaseFocues != null && v.getId() != etx_BaseFocues.getId()))){
									if(hasFocus){
										onFocus(v.getId(), v);
										if(vff != null && vff.isLesActIndex(v)){
											vff.unActByIndex(vff.getIndexByView(v));
											getFocues(v, true);
										}
									}
									if(!hasFocus){
										if(vb.isBlurRequired()){
											//** 验证必填
											StringBuffer _msg = new StringBuffer(validRequired(mbv, vb));
											//** 正则验证
											_msg.append(validRegex(mbv, vb));
											if(!StringUtil.isEmpty(_msg) || !onBlur(v.getId(), v)){
												if(!StringUtil.isEmpty(_msg.toString())) showShortMessage(_msg.toString());
												getFocues(v, true);
											}else{
												actNextTView();
											}
										}else{
											if(onBlur(v.getId(), v))
												actNextTView();
											else
												getFocues(v, true);
										}
									}
								}else isLockFocusEvent = false;
							} catch (Exception e) {
								e.printStackTrace();
								showShortMessage("光标事件异常, Error:"+e.getMessage());
							}
						}
					});
					
					ext.addTextChangedListener(new TextWatcher() {
						private int voiwId = id;
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							try {
								onChanged(voiwId, s, start, before, count);
							} catch (Exception e) {
								e.printStackTrace();
								showShortMessage("onTextChanged事件异常, Error:"+e.getMessage());
							}
						}
						
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
							try {
								onChangedBef(voiwId, s, start, count, after);
							} catch (Exception e) {
								e.printStackTrace();
								showShortMessage("onChangedBef事件异常, Error:"+e.getMessage());
							}
						}
						
						@Override
						public void afterTextChanged(Editable s) {
							try {
								onChangedAft(voiwId, s);
							} catch (Exception e) {
								e.printStackTrace();
								showShortMessage("afterTextChanged事件异常, Error:"+e.getMessage());
							}
						}
					});
				}
			}
		}
	}
	protected void onChanged(int id, CharSequence s, int start, int before, int count){
	}
	protected void onChangedBef(int id, CharSequence s, int start, int count, int after){
	}
	protected void onChangedAft(int id, Editable s){
	}
	
	protected void getFocues(final View v, final boolean _isLockFocusEvent){
		new Handler() {
			public void handleMessage(
					Message msg) {
				if(v != null && !v.hasFocus()){
					isLockFocusEvent = _isLockFocusEvent;
					v.requestFocus();
				}
				super.handleMessage(msg);
			}
		}.sendEmptyMessage(0); 
	}

	protected void actNextTView(){
		if(vff != null) getFocues(vff.actNext(), true);
	}
	
	protected void actByIndexTView(int index){
		if(vff != null) getFocues(vff.actByIndex(index), true);
	}
	
	protected void actPause(){
		vff.pause();
	}
	
	protected void actUnPause(){
		getFocues(vff.unPause(), true);
	}
	
	protected void actGoon(){
		vff.goon();
	}
	
	protected void initViewFF(){
		initViewFocusForward(vff = vff == null ? new ViewFocusForward() : vff);
		if (vff != null) {
			vff.setup();
		}
	}
	
	//验证
	protected boolean validata(){
		List<View> ls = getAllChildViews();
		StringBuffer msg = new StringBuffer();
		for(int i = 0; i < ls.size(); i++){
			if (ls.get(i) instanceof IMyBaseView) {
				IMyBaseView mbv = ((IMyBaseView)ls.get(i));
				ValidataBean vb = mbv.getVdbean();
				//** 验证必填
				msg.append(validRequired(mbv, vb));
				
				//** 正则验证
				msg.append(validRegex(mbv, vb));
			}
		}
		String _msg = msg.toString();
		if(!StringUtil.isEmpty(_msg)){
			showShortMessage(StringUtil.remLastLine(_msg));
			return false;
		}
		return true;
	}
	
	private String validRequired(IMyBaseView mbv, ValidataBean vb){
		if(vb.isRequired() && StringUtil.isEmpty(mbv.getValStr())){
			return StringUtil.isEmpty(vb.getReqAltMsg(), ("Message of " + mbv.getId()+ "'s view is Null!")) + "\n";
		}
		return "";
	}
	
	private String validRegex(IMyBaseView mbv, ValidataBean vb){
		if(!StringUtil.isEmpty(vb.getRegValidata()) && Pattern.matches(vb.getRegValidata(), mbv.getValStr())){
			return StringUtil.isEmpty(vb.getRegAltMsg(), ("Message of " + mbv.getId()+ "'s view is invalid!")) + "\n";
		}
		return "";
	}
	
	
	/** 获取所有View **/
	private List<View> getAllChildViews() {
		View view = this.getWindow().getDecorView();
		return getAllChildViews(view);

	}

	private List<View> getAllChildViews(View view) {
		List<View> allchildren = new ArrayList<View>();
		if (view instanceof ViewGroup) {
			ViewGroup vp = (ViewGroup) view;
			for (int i = 0; i < vp.getChildCount(); i++) {
				View viewchild = vp.getChildAt(i);
				allchildren.add(viewchild);
				allchildren.addAll(getAllChildViews(viewchild));
			}
		}
		return allchildren;
	}
	/** 获取所有View End **/
	
	protected boolean onFocus(int id, View v){
		return true;
	}
	/**
	 * 返回False则不允许离开当前文本框
	 * @param id
	 * @param v
	 * @return
	 */
	protected boolean onBlur(int id, View v){ 
		return true;
	}
	
	protected void initViewFocusForward(ViewFocusForward _vff){
		
	}	
	
	private Map<Integer, MyReadBQ> rbqMap;
	
	public void addReadBQItem(MyReadBQ item){
		rbqMap = rbqMap == null ? new HashMap<Integer, MyReadBQ>() : rbqMap;
		rbqMap.put(item.getId(), item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(rbqMap != null && rbqMap.containsKey(requestCode) && resultCode == RESULT_OK){
			String result = data.getStringExtra("result");
			if(!StringUtil.isEmpty(result))
				rbqMap.get(requestCode).setText(result);
	    }
	}
}
