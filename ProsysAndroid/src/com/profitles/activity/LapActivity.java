package com.profitles.activity;

import java.util.Map;

import android.view.View;

import com.profitles.biz.LapBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;


public class LapActivity extends AppFunActivity {
	private LapBiz lapBiz;
	private MyEditText etxLapPart,etxLapLot,etxLapUnit,
					etxLapVend,etxLapPo,etxLapPoLine;
	private MyReadBQ  etxLapBar;

	private ApplicationDB applicationDB;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_lap;
	}

	@Override
	protected void pageLoad() {
		lapBiz = new LapBiz();
		etxLapBar = (MyReadBQ) findViewById(R.id.etx_lapBar);
		etxLapPart = (MyEditText) findViewById(R.id.etx_lapPart);
		etxLapLot = (MyEditText) findViewById(R.id.etx_lapLot);
		etxLapUnit = (MyEditText) findViewById(R.id.etx_lapUnit);
		etxLapVend = (MyEditText) findViewById(R.id.etx_lapVend);
		etxLapPo = (MyEditText) findViewById(R.id.etx_lapPo);
		etxLapPoLine = (MyEditText) findViewById(R.id.etx_lapPoLine);
	}


	@Override
	protected boolean onFocus(int id, View v) {
		return super.onFocus(id, v);
	}

	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//扫码光标离开事件相对应的代码
		if(etxLapBar.getId()==id){
			runClickFun();
			if(null != etxLapBar.getValStr() && !"".equals( etxLapBar.getValStr().toString().trim())){
				checkLapCode();
			}else{
				etxLapPart.setReadOnly(false);
				etxLapLot.setReadOnly(false);
				etxLapUnit.setReadOnly(false);
				etxLapVend.setReadOnly(false);
				etxLapPo.setReadOnly(false);
				etxLapPoLine.setReadOnly(false);
				etxLapPart.reValue();
				etxLapLot.reValue();
				etxLapUnit.reValue();
				etxLapVend.reValue();
				etxLapPo.reValue();
				etxLapPoLine.reValue();
				clearMsg();
				istrue = true;
			}
			
		}
		if(etxLapPart.getId()==id){
			runClickFun();
		}
		if(etxLapLot.getId()==id){
			runClickFun();
		}	
		if(etxLapUnit.getId()==id){
			runClickFun();
		}	
		if(etxLapVend.getId()==id){
			runClickFun();
		}	
		if(etxLapPo.getId()==id){
			runClickFun();
		}
		if(etxLapPoLine.getId()==id){
			runClickFun();
		}	
		return istrue ;
	}

	//处理条码解析
	private void checkLapCode() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return lapBiz.lapBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxLapBar.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					etxLapPart.setText(map.get("Part").toString());
					etxLapLot.setText(map.get("Lot").toString());
					etxLapUnit.setText(map.get("Unit").toString());
					etxLapVend.setText(map.get("Vend").toString());
					etxLapPo.setText(map.get("Po_no").toString());
					etxLapPoLine.setText(map.get("Po_line").toString());
					etxLapPart.setReadOnly(true);
					etxLapLot.setReadOnly(true);
					etxLapUnit.setReadOnly(true);
					etxLapVend.setReadOnly(true);
					etxLapPo.setReadOnly(true);
					etxLapPoLine.setReadOnly(true);
					clearMsg();
					runClickFun();
					istrue=true;
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etxLapBar, true);
					istrue = false;
				}
			}
		});
	}
	
	
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Submit};
	}

	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals( etxLapPart.getValStr().toString().trim()) && !"".equals( etxLapLot.getValStr().toString().trim()) 
			&& !"".equals( etxLapUnit.getValStr().toString().trim()) && !"".equals( etxLapVend.getValStr().toString().trim())
			&& !"".equals( etxLapPo.getValStr().toString().trim()) && !"".equals( etxLapPoLine.getValStr().toString().trim())){
		}else{
			showErrorMsg(getResources().getString(R.string.lap_consSub_false));
			getFocues(etxLapBar, true);
			istrue = false;
		}
		return istrue;
	}
	

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		if(!"".equals( etxLapBar.getValStr().toString().trim())){
			return lapBiz.lapSubByBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxLapBar.getValStr(), applicationDB.user.getUserId());
		}else{
			return lapBiz.lapSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxLapPart.getValStr(),
					etxLapLot.getValStr(),etxLapUnit.getValStr(),etxLapVend.getValStr(),etxLapPo.getValStr(),etxLapPoLine.getValStr(),applicationDB.user.getUserId());
		}
		
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxLapPart.setReadOnly(false);
			etxLapLot.setReadOnly(false);
			etxLapUnit.setReadOnly(false);
			etxLapVend.setReadOnly(false);
			etxLapPo.setReadOnly(false);
			etxLapPoLine.setReadOnly(false);
			etxLapPart.reValue();
			etxLapLot.reValue();
			etxLapUnit.reValue();
			etxLapVend.reValue();
			etxLapPo.reValue();
			etxLapPoLine.reValue();
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxLapBar, true);
		}
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
}



