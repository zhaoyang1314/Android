package com.profitles.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.profitles.biz.PorcBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyReadBQ;

public class ScanCActivity extends AppFunActivity {
	
	private PorcBiz Pbiz;
	private MyReadBQ  etx_scancBar;
	private ApplicationDB applicationDB;

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_scanc;
	}

	@Override
	protected void pageLoad() {
		  Pbiz=new PorcBiz();
		  etx_scancBar =(MyReadBQ) findViewById(R.id.etx_scancBar);
		  
		  etx_scancBar.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(null != etx_scancBar.getValStr() && !"".equals( etx_scancBar.getValStr().toString().trim())){
						clearMsg();
						checkBar();
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				public void afterTextChanged(Editable s) {
				}
			});

	} 
	
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		//_vff.addItemView(etx_PorcNbr,etx_PorcBar);
	}
	

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		if(etx_scancBar.getId()==id){
			runClickFun();
		}
		
		return istrue;
	}
	
	private void checkBar() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Pbiz.checkScanCBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_scancBar.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){

				}else{
					istrue = false;
					showErrorMsg(wr.getMessages());
					etx_scancBar.setText("");
					getFocues(etx_scancBar, true);
				}
			}
		});
	}
	
	

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit};
	}	
	
	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(null != etx_scancBar.getValStr() && !"".equals( etx_scancBar.getValStr().toString().trim())){
			
		}else{
			showErrorMsg(getResources().getString(R.string.ScanC_Sub_false));
			istrue=false;
		}
		return istrue;
	}
	
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return Pbiz.checkScanCSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etx_scancBar.getValStr());
	
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etx_scancBar.reValue();
			getFocues(etx_scancBar, true);
			showSuccessMsg(wr.getMessages());
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
			getFocues(etx_scancBar, true);
		}
		
	}
	
	
	@Override
	protected void unLockNbr() {

	}


	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
	
}
