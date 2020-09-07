package com.profitles.activity;

import java.util.List;
import java.util.Map;

import android.view.View;

import com.profitles.biz.RecehsBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyDateView;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.StringUtil;

public class RevhsActivity extends AppFunActivity {


	private RecehsBiz pbiz;

	private MyReadBQ  etxRece,etxRecenbr,etxSendnbr,etxReceventor,etxEcepart;
	private MyDataGrid dtgRecehist;
	private MyDateView dtvReceDate;
	private ApplicationDB applicationDB;
	
	@Override
	protected void pageLoad() {
		pbiz = new RecehsBiz();
	
		etxEcepart = (MyReadBQ) findViewById(R.id.etx_recepart);
		etxReceventor = (MyReadBQ) findViewById(R.id.etx_receventor);
		
		etxRece = (MyReadBQ)findViewById(R.id.etx_rece);
		etxRecenbr = (MyReadBQ)findViewById(R.id.etx_recenbr);
		etxSendnbr = (MyReadBQ)findViewById(R.id.etx_sendnbr);
		//日期
		dtvReceDate =  (MyDateView) findViewById(R.id.dtv_rece_date);
		
		dtgRecehist = (MyDataGrid)findViewById(R.id.dtg_Recehist);
		runClickFun();
	}

	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_revhs;
	}
	
	boolean istrue = true ;
	@Override
	protected boolean onBlur(int id, View v) {
		if(etxEcepart.getId() == id){
			runClickFun();
		}
		if(etxReceventor.getId()==id){
			runClickFun();
		}
		if(etxRece.getId() == id){
			runClickFun();
		}
		if(etxRecenbr.getId() == id){
			runClickFun();
		}
		
		if(dtvReceDate.getId() == id){
			runClickFun();
		}
		return istrue ;
	}
	
	protected ButtonType[] getNeedBtn() {
		return new ButtonType[]{ ButtonType.Search};
	}

	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		if(!"".equals(dtvReceDate.getValStr().toString().trim())){
			if(!"".equals(etxEcepart.getValStr().toString().trim()) || !"".equals(etxReceventor.getValStr().toString().trim()) ||!"".equals(etxSendnbr.getValStr().toString().trim()) ||
					!"".equals(etxRece.getValStr().toString().trim()) || !"".equals(etxRecenbr.getValStr().toString().trim()) ){
				
			}
		}else{
			istrue = false;
			showErrorMsg(getResources().getString(R.string.Rece_false));
			getFocues(dtvReceDate, true);
		}
		return istrue;
	}
	
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return pbiz.getConsInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etxEcepart.getValStr(), etxReceventor.getValStr(), etxRece.getValStr(), etxRecenbr.getValStr(),dtvReceDate.getValStr(),etxSendnbr.getValStr()).getDataToList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void OnBtnSerCallBack(Object data) {
		//返回line  有.0的处理
		List<Map<String, Object>> list  = (List<Map<String, Object>>)data;
		for (int i = 0; i < list.size(); i++) {
			int a =   StringUtil.parseInt(list.get(i).get("XSPRH_LINE"));	
			int b =  StringUtil.parseInt(list.get(i).get("XSPRH_RCVD"));
			if (a==-1) {
				list.get(i).put("XSPRH_LINE", 0);
				list.get(i).put("XSPRH_RCVD", b);
			}else if(b==-1){
				list.get(i).put("XSPRH_LINE", a);
				list.get(i).put("XSPRH_RCVD", 0);
			}else{
				list.get(i).put("XSPRH_LINE", a);
				list.get(i).put("XSPRH_RCVD", b);
			}
		}
		dtgRecehist.buildData(list);
	}

	@Override
	protected void unLockNbr() {
		
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
}