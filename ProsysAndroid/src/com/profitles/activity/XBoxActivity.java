package com.profitles.activity;

import java.util.Map;

import android.text.Editable;
import android.view.View;

import com.profitles.biz.XBoxBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;


public class XBoxActivity extends AppFunActivity {
	private XBoxBiz xBoxbiz;
	private MyEditText etxXBoxPart,etxXBoxUm,etxXBoxPartDesc,etxXBoxLot,
						etxXBoxSplit,etxXBoxUnit,etxXBoxSurplus,etxXBoxSplitBox;
	private MyReadBQ  etxXBoxBar;

	private ApplicationDB applicationDB;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_xbox;
	}

	@Override
	protected void pageLoad() {
		xBoxbiz=new XBoxBiz();
		etxXBoxBar = (MyReadBQ) findViewById(R.id.etx_xBoxBar);
		etxXBoxPart = (MyEditText) findViewById(R.id.etx_xBoxPart);
		etxXBoxUm = (MyEditText) findViewById(R.id.etx_xBoxUm);
		etxXBoxPartDesc = (MyEditText) findViewById(R.id.etx_xBoxPartDesc);
		etxXBoxLot = (MyEditText) findViewById(R.id.etx_xBoxLot);
		etxXBoxSplit = (MyEditText) findViewById(R.id.etx_xBoxSplit);
		etxXBoxUnit = (MyEditText) findViewById(R.id.etx_xBoxUnit);
		etxXBoxSurplus = (MyEditText) findViewById(R.id.etx_xBoxSurplus);
		etxXBoxSplitBox = (MyEditText) findViewById(R.id.etx_xBoxSplitBox);
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
		if(etxXBoxBar.getId()==id){
			runClickFun();
			if(null != etxXBoxBar.getValStr() && !"".equals( etxXBoxBar.getValStr().toString().trim())){
				checkCnrcCode();
			}

		}
		if(etxXBoxSplit.getId()==id){
			runClickFun();
		}
		return istrue ;
	}

	//处理条码解析
	private void checkCnrcCode() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return xBoxbiz.xBoxBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxXBoxBar.getValStr().toString());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String ,Object> map = wr.getDataToMap();
					etxXBoxPart.setText(map.get("RFLOT_PART").toString());
					etxXBoxUm.setText(map.get("RFLOT_UM").toString());
					etxXBoxPartDesc.setText(map.get("RFLOT_PART_DESC").toString());
					etxXBoxLot.setText(map.get("RFLOT_LOT").toString());
					etxXBoxUnit.setText(map.get("RFLOT_BOX_QTY").toString());
					etxXBoxSurplus.setText(map.get("RFLOT_BOX_QTY").toString());
					clearMsg();
					runClickFun();
					istrue=true;
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etxXBoxBar, true);
					istrue = false;
				}
			}
		});
	}
	
	
	@Override
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ButtonType.Submit,ButtonType.Save};
	}

	@Override
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(!"".equals(etxXBoxBar.getValStr().toString().trim())){
			if(!"".equals(etxXBoxSplit.getValStr().toString().trim())){
				float Unit =StringUtil.parseFloat(etxXBoxSurplus.getValStr());   	//剩余数
				float cfs =StringUtil.parseFloat(etxXBoxSplit.getValStr());   	//拆分数
				if(cfs<=Unit){ //可以提交
					if(!"".equals(etxXBoxSplitBox.getValStr().toString().trim())){
						String SplitBox=etxXBoxSplitBox.getValStr().toString();
						etxXBoxSplitBox.setText(SplitBox+","+cfs);
					}else{
						etxXBoxSplitBox.setText(cfs+"");
					}
					float Surplus=StringUtil.parseFloat(etxXBoxSurplus.getValStr());
					float Surp=Surplus - cfs;
					etxXBoxSurplus.setText(Surp+"");
					istrue=true;
				}else{
					showErrorMsg(getResources().getString(R.string.xBox_save_false));
					getFocues(etxXBoxSplit, true);
					istrue=false;
				}
			}else{
				if(!"".equals(etxXBoxSplitBox.getValStr().toString().trim())){
					istrue=true;
				}else{
					showErrorMsg(getResources().getString(R.string.xBox_saveCfs_false));
					getFocues(etxXBoxSplit, true);
					istrue=false;
				}
			}
		}else{
			showErrorMsg(getResources().getString(R.string.xBox_saveCode_false));
			getFocues(etxXBoxBar, true);
			istrue=false;
		}
		return istrue;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return xBoxbiz.xBoxSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxXBoxBar.getValStr().toString(),etxXBoxLot.getValStr(),
				etxXBoxPart.getValStr(),etxXBoxSurplus.getValStr() ,etxXBoxSplitBox.getValStr(),applicationDB.user.getUserId());
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxXBoxBar.reValue();
			etxXBoxPart.reValue();
			etxXBoxUm.reValue();
			etxXBoxPartDesc.reValue();
			etxXBoxLot.reValue();
			etxXBoxSplit.reValue();
			etxXBoxUnit.reValue();
			etxXBoxSurplus.reValue();
			
			etxXBoxSplitBox.reValue();
			getFocues(etxXBoxBar, true);
			showSuccessMsg(wr.getMessages());
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxXBoxSplit, true);
		}
	}
	
	
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		if(!"".equals(etxXBoxBar.getValStr().toString().trim())){
			if(!"".equals(etxXBoxSplit.getValStr().toString().trim())){
				float Unit =StringUtil.parseFloat(etxXBoxSurplus.getValStr());   	//剩余数
				float cfs =StringUtil.parseFloat(etxXBoxSplit.getValStr());   	//拆分数
				if(cfs<=Unit){ //这里可以保存
					
				}else{
					showErrorMsg(getResources().getString(R.string.xBox_save_false));
					getFocues(etxXBoxSplit, true);
					istrue=false;
				}
			}else{
				showErrorMsg(getResources().getString(R.string.xBox_saveCfs_false));
				getFocues(etxXBoxSplit, true);
				istrue=false;
			}
		}else{
			showErrorMsg(getResources().getString(R.string.xBox_saveCode_false));
			getFocues(etxXBoxBar, true);
			istrue=false;
		}
		return istrue;
	}
	
	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return etxXBoxSplit.getValStr();
	}
		
	@Override
	public void OnBtnSaveCallBack(Object data) {
		if(!"".equals(etxXBoxSplitBox.getValStr().toString().trim())){
			String SplitBox=etxXBoxSplitBox.getValStr().toString();
			etxXBoxSplitBox.setText(SplitBox+","+StringUtil.parseFloat(data.toString()));
		}else{
			etxXBoxSplitBox.setText(StringUtil.parseFloat(data.toString())+"");
		}
		float Surplus=StringUtil.parseFloat(etxXBoxSurplus.getValStr());
		float Split=StringUtil.parseFloat(data);
		float Surp=Surplus - Split;
		etxXBoxSurplus.setText(Surp+"");
		etxXBoxSplit.reValue();
		getFocues(etxXBoxSplit, true);
	}
	

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}

	@Override
	protected void unLockNbr() {
		
	}
	
}



