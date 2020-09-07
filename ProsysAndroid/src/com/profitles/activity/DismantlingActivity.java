package com.profitles.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.DisBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.ViewFocusForward;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.util.StringUtil;

public class DismantlingActivity extends AppFunActivity {
	
	private DisBiz disBiz;
	//private MyTextView txv_pxUm; 
	private MyEditText  etx_sendPart,etx_sendPartDesc,etx_sendQty,etx_sendUnit,etx_sendCus,etx_sendmodel,etx_num,etx_bnum;
	private MyReadBQ etxCrnrtNbr,etxsendSn; 
	private MyTabHost tblSocfmAdv;
	private MyDataGrid rflot_SnList,gdvAddARemove; 
	private String type,seq,domain,site,userid,date,scanType,DelAll,qty, Edition;
	private StringBuffer strScan = new StringBuffer("");
	private List<Map<String,Object>> TrayList = new ArrayList<Map<String,Object>>();//托盘信息
	private List<Map<String,Object>> DisList = new ArrayList<Map<String,Object>>(); //拼拆信息
	private boolean istrue = false;
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_dis;
	}

	@Override
	protected void pageLoad() {
		  disBiz=new DisBiz();
		  domain = ApplicationDB.user.getUserDmain();
		  site = ApplicationDB.user.getUserSite();  
		  userid = ApplicationDB.user.getUserId();  
		  date = ApplicationDB.user.getUserDate(); 
		  etxCrnrtNbr = (MyReadBQ) findViewById(R.id.etx_sendScan);
		  etxCrnrtNbr.addTextChangedListener(new TextWatcher() { //托标签改变事件
			  
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!StringUtil.isEmpty(etxCrnrtNbr.getValStr())){
						CrnrtNbrChange();
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				public void afterTextChanged(Editable s) {
				}
		  });
		  etxsendSn = (MyReadBQ) findViewById(R.id.etx_sendSn);
		  etxsendSn.addTextChangedListener(new TextWatcher() { //单件标签改变事件
			  
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!StringUtil.isEmpty(etxsendSn.getValStr())){
						SnchChange();
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				public void afterTextChanged(Editable s) {
				}
			  });
		  etx_sendPart =(MyEditText) findViewById(R.id.etx_sendPart);
		  etx_sendPartDesc =(MyEditText) findViewById(R.id.etx_sendPartDesc);
		  etx_sendQty=(MyEditText) findViewById(R.id.etx_sendQty);
		  etx_sendUnit=(MyEditText) findViewById(R.id.etx_sendUnit);
		  etx_sendCus=(MyEditText) findViewById(R.id.etx_sendCus);
		  etx_sendmodel =(MyEditText) findViewById(R.id.etx_sendmodel);
		  etx_num =(MyEditText) findViewById(R.id.etx_num);
		  etx_bnum =(MyEditText) findViewById(R.id.etx_bnum);
		  
		  tblSocfmAdv= ((MyTabHost) this.findViewById(R.id.tbl_socfmAdv));
		  tblSocfmAdv.setup();
		  rflot_SnList = (MyDataGrid)findViewById(R.id.rflot_SN);
		  gdvAddARemove=(MyDataGrid)findViewById(R.id.gdvAddARemove);
	} 
	
	protected boolean onBlur(int id, View v) {
		//扫码光标离开事件相对应的代码
		if(etxCrnrtNbr.getId()==id){
			runClickFun();
		}
		//扫码光标离开事件相对应的代码
		if(etxsendSn.getId()==id){
			runClickFun();
		}	
		return true ;
	}
	
	/**
	 * 扫描托标签
	 */
	private void CrnrtNbrChange() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				clearMsg();
				return true;
			}
			@Override
			public Object onGetData() {
				return disBiz.DisCheckBar(domain, site,etxCrnrtNbr.getValStr());
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
				    type=StringUtil.isEmpty(map.get("RFLOT_SCAN_STATUS"))?"":map.get("RFLOT_SCAN_STATUS")+"";
				    BigDecimal bd=new BigDecimal(map.get("RFLOT_SEQ")+"");
				    seq = bd.toPlainString(); 
				    Edition = StringUtil.isEmpty(map.get("RFLOT_PART_REV")+"", "");
				    scanType = map.get("RFLOT_TYPE")+"";
				    if("A".equals(type)){
				    	etx_sendmodel.setText("拼托");
				    }else{
				    	etx_sendmodel.setText("拆托");
				    }
				    if(StringUtil.parseFloat(map.get("RFLOT_SCATTER_QTY"))>0){
				        etx_num.setText(map.get("RFLOT_SCATTER_QTY")+"");  //标签数量
				    }else{
				    	etx_num.setText(map.get("RFLOT_MULT_QTY")+"");
				    }
				    querySnAll();
				  
				}else{
					clear();
					showErrorMsg(wr.getMessages());
					getFocues(etxCrnrtNbr, true);
				}
			}
		});
	}
	
	/**
	 * 查询当前托标签下的单件标签
	 */
	private void querySnAll() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return disBiz.DisCheckSnList(domain, site,etxCrnrtNbr.getValStr(),type);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					List<Map<String, Object>>list=(List<Map<String, Object>>) wr.getDataToList();
					TrayList = list;
					rflot_SnList.buildData(list);
					float pkg_qty = 0f;
					for (int i = 0; i < list.size(); i++) {
						pkg_qty = pkg_qty + StringUtil.parseFloat(list.get(i).get("RFLOTD_MFG_PKG_QTY"));
					}
			        etx_bnum.setText(pkg_qty+"");	   //已扫描数量
			        
			        istrue = true;
				}
				  getFocues(etxsendSn, true);
			}
		});
	}
	
	/**
	 * 扫描单件标签按照当前拼拆托类型进行显示数据的增加或者删除
	 */
	private void SnchChange(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return Dismantling();
			}
			@Override
			public Object onGetData() {
				return disBiz.DisOperation(userid, domain, site, etxCrnrtNbr.getValStr(), etxsendSn.getValStr(), etx_sendCus.getValStr(), scanType, type, seq,etx_num.getValStr(),etx_bnum.getValStr(), Edition);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map=wr.getDataToMap();
					String SnType = map.get("RFLOT_TYPE")+"";
					DisList.add(map);
					gdvAddARemove.buildData(DisList); //将当前扫描单件信息放到 拼拆信息 栏位中显示
					//根据当前类型判断增加或者删除显示当前扫描的单件数据
					if(type.equals("A")){
						TrayList.add(map);
						rflot_SnList.buildData(TrayList);
						if(SnType.equals("B")){
							etx_bnum.setText(StringUtil.parseFloat(map.get("RFLOTD_MFG_PKG_QTY")) * TrayList.size() +""); //刷新已扫数量
						}else{
							etx_bnum.setText(TrayList.size() > 0 ? TrayList.size()+"" : 0+""); //刷新已扫数量
						}
					}else{				 
						for (int i = 0; i < TrayList.size(); i++) {
							if(map.get("RFLOT_SCAN").equals(TrayList.get(i).get("D_SCAN"))){
								TrayList.remove(i);
								strScan.append("'"+map.get("RFLOT_SCAN")+"',");
							}
						}
						rflot_SnList.buildData(TrayList);
						if(SnType.equals("B")){
							etx_bnum.setText(StringUtil.parseFloat(map.get("RFLOTD_MFG_PKG_QTY")) * TrayList.size() +""); //刷新已扫数量
						}else{
							etx_bnum.setText(DisList.size() > 0 ? DisList.size()+"" : 0+""); //刷新已扫数量
						}
					}
					etxsendSn.reValue();
					qty = StringUtil.parseFloat(map.get("RFLOTD_MFG_PKG_QTY"))+"";
					istrue = true;
					getFocues(etxsendSn, true);
				}else{
					etxsendSn.reValue();
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	/**
	 * 拆托或拼托
	 */
	private boolean Dismantling(){
		if(type.equals("A")){
			if(StringUtil.parseInt(etx_bnum.getValStr()) + 1 > StringUtil.parseInt(etx_num.getValStr())){
				showErrorMsg("当前托标签已拼满!");
				etxsendSn.reValue();
				return false;
			}
		}
		int del = 0;
		for (int i = 0; i < TrayList.size(); i++) {
			if(etxsendSn.getValStr().equals(TrayList.get(i).get("D_SCAN"))){
				if(type.equals("A")){
					showErrorMsg("当前单件标签已存在托盘信息中!");
					etxsendSn.reValue();
					return false;
				}
			}else{
				del++;
			}
		}
		if(type.equals("")){
			if(del == TrayList.size()){
				showErrorMsg("当前单件标签不存在托盘信息中!");
				etxsendSn.reValue();
				return false;
			}
		}
//		if(type.equals("A")){
//			if((StringUtil.parseInt(etx_bnum.getValStr())+1) == StringUtil.parseInt(etx_num.getValStr())){
//				DelAll = "DelAll";
//			}
//		}
		return true;
	}	
	
	/**
	 * 提交
	 */
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		if(StringUtil.isEmpty(etxCrnrtNbr.getValStr())){
			return false;
		}else if(istrue != true){
			return false;
		}
		if(type.equals("")){
			if(DisList.size() == 0){
				showErrorMsg("请扫描单件之后再做操作");
				return false;
			}
			if(TrayList.size() == 0){
				DelAll = "DelAll";
			}
		}
		return true;
	}

	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return disBiz.DisInforSub(userid,domain, site,seq, etxCrnrtNbr.getValStr(),etx_sendQty.getValStr(), qty,strScan.toString(),type,DelAll,etx_bnum.getValStr());
	}

	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			clear();
			showMessage(wr.getMessages());
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
		}
	}
	
	//清空
	public void clear(){
		strScan = new StringBuffer("");
		etxCrnrtNbr.reValue();
		etxsendSn.reValue();
		etx_sendPart.setText("");  
		etx_sendPartDesc.setText("");
		etx_sendQty.setText("");  
		etx_sendUnit.setText("");
		etx_sendCus.setText("");  
		etx_sendmodel.setText("");
		etx_num.setText("");
		etx_bnum.setText("");
		TrayList = new ArrayList<Map<String,Object>>();
		DisList = new ArrayList<Map<String,Object>>();
		rflot_SnList.buildData(TrayList); 
		gdvAddARemove.buildData(TrayList);
		qty = "";
		seq = "";
		type = "";
		DelAll = "";
		scanType = "";
		getFocues(etxCrnrtNbr, true);
	}

	@Override
	protected void initViewFocusForward(ViewFocusForward _vff) {
		
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
		return ApplicationDB.user.getUserDate();
	}
	
}
