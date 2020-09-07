package com.profitles.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.LinearLayout;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.PmBiz;
import com.profitles.biz.SpellBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.util.StringUtil;

public class PmActivity extends AppFunActivity {
	
	private PmBiz pmbiz;
	private MyEditText  etx_sendNbr,etx_sendPart,etx_sendPartDesc,etx_sendQty,etx_sendUnit,etx_sendCus,etx_sendSn,etx_sendmodel,etx_num,etx_bnum;
	private MyReadBQ etxCrnrtNbr,etxsendSn; 
	private MySpinner  spn_pkbmodel;
	
 	private LinearLayout lltSocfmSrfd,lltSocfmPkl;
	private MyDataGrid rflot_SN; 
	
	private ApplicationDB applicationDB;
	private String jsonStr="",jsonStrremove="",jsonStrsnAcoderemove="",uuid="",type="",seq="", _qty="",_scan_qty="",_msg="";
	private  int result=0,result2=0,result3=0,result4=0;
	boolean execute=false;
	private List<Map<String , Object>> histlist = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> rList = new ArrayList<Map<String , Object>>();
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pm;
	}

	@Override
	protected void pageLoad() {
		  pmbiz=new PmBiz();
		  etxCrnrtNbr = (MyReadBQ) findViewById(R.id.etx_sendScan);
		  
		  etx_sendPart =(MyEditText) findViewById(R.id.etx_sendPart);
		  etx_sendPartDesc =(MyEditText) findViewById(R.id.etx_sendPartDesc);
		  etx_sendQty=(MyEditText) findViewById(R.id.etx_sendQty);
		  etx_sendUnit=(MyEditText) findViewById(R.id.etx_sendUnit);
		  etx_sendCus=(MyEditText) findViewById(R.id.etx_sendCus);
		  etx_sendmodel =(MyEditText) findViewById(R.id.etx_sendmodel);
		  etx_num =(MyEditText) findViewById(R.id.etx_num);
		  etx_bnum =(MyEditText) findViewById(R.id.etx_bnum);
		  rflot_SN =(MyDataGrid) findViewById(R.id.rflot_SN);

	} 
	
	
	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		
	}

	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//扫码光标离开事件相对应的代码
		if(etxCrnrtNbr.getId()==id){
			runClickFun();
			if(null != etxCrnrtNbr.getValStr() && !"".equals( etxCrnrtNbr.getValStr().toString().trim())){

				checkLapCode();
			}
		}
		//扫码光标离开事件相对应的代码
		if(etxsendSn.getId()==id){
			runClickFun();
		}	
		return istrue ;
	}
	
	
	boolean istrue=false;
	//处理托标签数据
	private void checkLapCode() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return pmbiz.spellCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxCrnrtNbr.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					    etx_sendPart.setText(map.get("RFLOT_PART")+"");
					    etx_sendPartDesc.setText(map.get("RFLOT_PART_DESC")+"");
					    etx_sendCus.setText(map.get("RFLOT_CUST_PART")+"");
					    etx_sendQty.setText(map.get("RFLOT_MULT_QTY")+"");
					    etx_sendUnit.setText(map.get("RFLOT_UM")+"");
					    
				    	etx_num.setText(map.get("RFLOT_MULT_QTY")+"");
				    	etx_bnum.setText(map.get("RFLOT_ITEM_QTY")+"");
				    	istrue=true;
					    //checkSNAll();
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etxCrnrtNbr, true);
					istrue = false;
				}
			}
		});
	}

	//托标签查询出所有关联单件标签
	private void checkSNAll() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return pmbiz.spellCheckSnList(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxCrnrtNbr.getValStr(),type);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					List<Map<String, Object>>list=(List<Map<String, Object>>) wr.getDataToList();
					rflot_SN.buildData(list);
				    if(list!=null&&list.size()>0){
				    	etx_bnum.setText(list.size()+"");
				    }else{
				    	etx_bnum.setText("0");
						istrue = false;
				    }
					istrue=true;
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etxCrnrtNbr, true);
					istrue = false;
				}
			}
		});
	}
	
	
	
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Search};
	}	

	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		return istrue=true;
	}
	
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return pmbiz.spellCheckSnList(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxCrnrtNbr.getValStr(),type);
	}
	
	@Override
	public void OnBtnSerCallBack(Object data) {
		rflot_SN.buildData(rList);
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			checkLapCode();
			if(istrue){
				List<Map<String, Object>>list=(List<Map<String, Object>>) wr.getDataToList();
				rflot_SN.buildData(list);
			}
		}else{
			showErrorMsg(wr.getMessages());
			getFocues(etxCrnrtNbr, true);
			istrue = false;
		}
	}
	
	@Override
	protected void unLockNbr() {

	}

	@Override
	public void OnBtnHelpCallBack(Object data) {
		showSuccessMsg(R.string.Cnrc_help);
		super.OnBtnHelpCallBack(data);
	}

	@Override
	public String getAppVersion() {
		return applicationDB.user.getUserDate();
	}
	
}
