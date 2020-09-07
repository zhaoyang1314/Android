package com.profitles.activity;
import java.util.Map;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import com.profitles.biz.WoQcOkBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;

public class WoQcOkActivity extends AppFunActivity {

	private WoQcOkBiz woQcOkbiz;
	private MyEditText  etx_woQcOkWoNbr,etx_woQcOkShift,etx_woQcOkPart,
			etx_woQcOkPartDesc,etx_woQcOkOp,etx_woQcOkWkctr,
			etx_woQcOkLine,etx_woQcOkWoDate,etx_woQcOkQty;
	private MyReadBQ  etx_woQcOkScan;
	private ApplicationDB applicationDB;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_woqcok;
	}

	@Override
	protected void pageLoad() {
		woQcOkbiz=new WoQcOkBiz();
		etx_woQcOkScan = (MyReadBQ) findViewById(R.id.etx_woQcOkScan);
		etx_woQcOkWoDate = (MyEditText) findViewById(R.id.etx_woQcOkWoDate);
		etx_woQcOkWoNbr = (MyEditText) findViewById(R.id.etx_woQcOkWoNbr);
		etx_woQcOkShift = (MyEditText) findViewById(R.id.etx_woQcOkShift);
		etx_woQcOkPart = (MyEditText) findViewById(R.id.etx_woQcOkPart);
		etx_woQcOkPartDesc = (MyEditText) findViewById(R.id.etx_woQcOkPartDesc);
		etx_woQcOkOp = (MyEditText) findViewById(R.id.etx_woQcOkOp);
		etx_woQcOkWkctr = (MyEditText) findViewById(R.id.etx_woQcOkWkctr);
		etx_woQcOkLine = (MyEditText) findViewById(R.id.etx_woQcOkLine);
		etx_woQcOkQty = (MyEditText) findViewById(R.id.etx_woQcOkQty);
		
		etx_woQcOkScan.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null != etx_woQcOkScan.getValStr() && !"".equals( etx_woQcOkScan.getValStr().toString().trim())){
					checkwoQcOkScan();
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
		//_vff.addItemView(etx_PftporcNbr,etx_PftporcBar);
	}

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		if(etx_woQcOkScan.getId()==id){
			runClickFun();
			if(null != etx_woQcOkScan.getValStr() && !"".equals( etx_woQcOkScan.getValStr().toString().trim())){
				checkwoQcOkScan();
				runClickFun();
			}else{
				istrue = true;
			}
		}
		if(etx_woQcOkQty.getId()==id){
			runClickFun();
		}
		return istrue;
	}
	
	//处理条码解析
	private void checkwoQcOkScan() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return woQcOkbiz.woQcOkScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_woQcOkScan.getValStr().toString(),applicationDB.user.getUserId());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					etx_woQcOkWoDate.setText(map.get("RFLOT_DATE")+"");
					etx_woQcOkShift.setText(map.get("SHIFT")+"");
					etx_woQcOkWkctr.setText(map.get("RFLOT_WORKSHOP")+"");
					etx_woQcOkLine.setText(map.get("RFLOT_SO")+"");
					etx_woQcOkWoNbr.setText(map.get("RFLOT_WOID")+"");
					etx_woQcOkPart.setText(map.get("RFLOT_PART")+"");
					etx_woQcOkPartDesc.setText(map.get("RFLOT_PART_DESC")+"");
					etx_woQcOkOp.setText(map.get("wrDesc")+"");
					etx_woQcOkQty.setText(map.get("RFLOT_SHIP_QTY")+"");
					getFocues(etx_woQcOkScan, true);
					runClickFun();
					istrue=true;
				}else{
					showErrorMsg(wr.getMessages());
					etx_woQcOkScan.reValue();
					etx_woQcOkWoDate.reValue();
					etx_woQcOkShift.reValue();
					etx_woQcOkWkctr.reValue();
					etx_woQcOkLine.reValue();
					etx_woQcOkWoNbr.reValue();
					etx_woQcOkPart.reValue();
					etx_woQcOkPartDesc.reValue();
					etx_woQcOkOp.reValue();
					etx_woQcOkQty.reValue();
					getFocues(etx_woQcOkScan, true);
					istrue = false;
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
		if(!"".equals(etx_woQcOkScan.getValStr().toString().trim()) && !"".equals(etx_woQcOkPart.getValStr().toString().trim()) &&
				!"".equals(etx_woQcOkQty.getValStr().toString().trim())){
			
		}else{
			showErrorMsg("请先扫描正确的标签");
			istrue=false;
		}	
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return woQcOkbiz.woQcOkSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_woQcOkScan.getValStr().toString(),applicationDB.user.getUserId());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etx_woQcOkScan.reValue();
			etx_woQcOkWoDate.reValue();
			etx_woQcOkShift.reValue();
			etx_woQcOkWkctr.reValue();
			etx_woQcOkLine.reValue();
			etx_woQcOkWoNbr.reValue();
			etx_woQcOkPart.reValue();
			etx_woQcOkPartDesc.reValue();
			etx_woQcOkOp.reValue();
			etx_woQcOkQty.reValue();
			showSuccessMsg(wr.getMessages());
			getFocues(etx_woQcOkScan, true);
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etx_woQcOkScan, true);
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
