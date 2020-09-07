package com.profitles.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.LinearLayout;

import com.google.zxing.common.StringUtils;
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

public class SpellActivity extends AppFunActivity {
	
	private SpellBiz Spellbiz;
	//private MyTextView txv_pxUm; 
	private MyEditText  etx_sendNbr,etx_sendPart,etx_sendPartDesc,etx_sendQty,etx_sendUnit,etx_sendCus,etx_sendSn,etx_sendmodel,etx_num,etx_bnum;
	private MyReadBQ etxCrnrtNbr,etxsendSn; 
	private MySpinner  spn_pkbmodel;

	
	private MyTabHost tblSocfmAdv;
 	private LinearLayout lltSocfmSrfd,lltSocfmPkl;
	private MyDataGrid rflot_SnList,gdvAddARemove; 
	
	private ApplicationDB applicationDB;
	private String jsonStr="",jsonStrremove="",jsonStrsnAcoderemove="",uuid="",type="",seq="", _qty="",_scan_qty="",_msg="";
	private  int result=0,result2=0,result3=0,result4=0;
	boolean execute=false;
	private List<Map<String , Object>> histlist = new ArrayList<Map<String , Object>>();
	private List<Map<String , Object>> removeList = new ArrayList<Map<String , Object>>();

	private List<Map<String , Object>> rList = new ArrayList<Map<String , Object>>();
	
	
	private List<Map<String , Object>> SnACodeList = new ArrayList<Map<String , Object>>();  /* 记录单件标签对应的托标签*/
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_spell_down;
	}

	@Override
	protected void pageLoad() {
		  Spellbiz=new SpellBiz();
		  etxCrnrtNbr = (MyReadBQ) findViewById(R.id.etx_sendScan);
		  etxsendSn = (MyReadBQ) findViewById(R.id.etx_sendSn);
		  etx_sendPart =(MyEditText) findViewById(R.id.etx_sendPart);
		  etx_sendPartDesc =(MyEditText) findViewById(R.id.etx_sendPartDesc);
		  etx_sendQty=(MyEditText) findViewById(R.id.etx_sendQty);
		  etx_sendUnit=(MyEditText) findViewById(R.id.etx_sendUnit);
		  etx_sendCus=(MyEditText) findViewById(R.id.etx_sendCus);
		  //spn_pkbmodel=(MySpinner) findViewById(R.id.spn_pkbmodel);
		  etx_sendmodel =(MyEditText) findViewById(R.id.etx_sendmodel);
		  
		  etx_num =(MyEditText) findViewById(R.id.etx_num);
		  etx_bnum =(MyEditText) findViewById(R.id.etx_bnum);
		  
		  
		  
			tblSocfmAdv= ((MyTabHost) this.findViewById(R.id.tbl_socfmAdv));
			tblSocfmAdv.setup();
			tblSocfmAdv.setOnTabChangedListener(new OnTabChangeListener() {
				@Override	
				public void onTabChanged(String tabId) {
					if(tabId.equals("GetSocfmSrfd")){
						new Handler() {
							public void handleMessage(
									Message msg) {
								rflot_SnList.buildData(histlist);
								super.handleMessage(msg);
							}
						}.sendEmptyMessage(0); 
					}else if(tabId.equals("GetSocfmPkl")){
						new Handler() {
							public void handleMessage(Message msg) {
								gdvAddARemove.buildData(SnACodeList);

								super.handleMessage(msg);
							}
						}.sendEmptyMessage(0); 
					}
				}
			});
		   rflot_SnList = (MyDataGrid)findViewById(R.id.rflot_SN);
		   gdvAddARemove=(MyDataGrid)findViewById(R.id.gdvAddARemove);
		   etxsendSn.addTextChangedListener(new TextWatcher() {
				
		   public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!etxsendSn.getValStr().equals("")){
						goon=true;
						boolean isok=false;
					    if("A".equals(type)){
					    	if(StringUtil.parseInt(_scan_qty)>0){
					    		if(StringUtil.parseInt(result)==0){
					    			if((StringUtil.parseInt(etx_bnum.getValStr()))<StringUtil.parseInt(etx_num.getValStr())){
					    				isok=true;
					    			}
					    		}
					    		if(isok){
									if(histlist.size()>0){
							  			for (int i = 0; i < histlist.size(); i++) {
							  				Map<String, Object> itemMap = new HashMap<String, Object>();
							  				itemMap=histlist.get(i);
							  				if(itemMap.get("RFLOTD_SN").equals(etxsendSn.getValStr().toString().trim())){
												showErrorMsg("扫描的["+etxsendSn.getValStr()+"]单件标签已存在!");
												getFocues(etxsendSn, true);
												etxsendSn.reValue();
												goon=false;
												break;
							  				}
							  			} 
									}
									if(goon){
									  histlistRemoveSn();	
									}
					    		}else{
									showErrorMsg("此托标签已完成单件标签的扫描,单件标签个数不能大于散包量!");
									getFocues(etxsendSn, true);
									etxsendSn.reValue();
									//return false;	
					    		}
					    	}else{
								if((StringUtil.parseInt(etx_bnum.getValStr()))<StringUtil.parseInt(etx_sendQty.getValStr())){
									if(histlist.size()>0){
							  			for (int i = 0; i < histlist.size(); i++) {
							  				Map<String, Object> itemMap = new HashMap<String, Object>();
							  				itemMap=histlist.get(i);
							  				if(itemMap.get("RFLOTD_SN").equals(etxsendSn.getValStr().toString().trim())){
												showErrorMsg("扫描的["+etxsendSn.getValStr()+"]单件标签已存在!");
												getFocues(etxsendSn, true);
												etxsendSn.reValue();
												goon=false;
												break;
							  				}
							  			} 
									}
									if(goon){
									  histlistRemoveSn();	
									}
								}else{
									showErrorMsg("此托标签已完成单件标签的扫描,单件标签个数不能大于单包量!");
									getFocues(etxsendSn, true);
									etxsendSn.reValue();
									//return false;
								}
					    	}
					    }else{
					    	if(!StringUtil.isEmpty(_scan_qty)){
					    		if(StringUtil.parseInt(etx_bnum.getValStr())>0){
										  histlistRemoveSn();	
					    		}else{
									showErrorMsg("此托标签已无可拆的单件标签!,单件标签个数不能大于散包量!");
									getFocues(etxsendSn, true);
									etxsendSn.reValue();
									//return false;
								}
					    	}else{
					    		if(execute){
					    			histlistRemoveSn();	
					    			execute=false;
					    		}
					    		else if(StringUtil.parseInt(etx_bnum.getValStr())<=StringUtil.parseInt(etx_sendQty.getValStr())){
									  histlistRemoveSn();	
								}else{
									showErrorMsg("此托标签已无可拆的单件标签!,单件标签个数不能大于单包量!");
									getFocues(etxsendSn, true);
									etxsendSn.reValue();
									//return false;
								}
					    	}
					    }

						//return true;
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
		
	}

	Boolean istrue=true;
	@Override
	protected boolean onBlur(int id, View v) {
		istrue = true;
		//扫码光标离开事件相对应的代码
		if(etxCrnrtNbr.getId()==id){
			runClickFun();
			if(null != etxCrnrtNbr.getValStr() && !"".equals( etxCrnrtNbr.getValStr().toString().trim())){
				
//				rflot_SnList.buildData(rList); 
//				gdvAddARemove.buildData(rList); 
//				
//				histlist.clear();
//				removeList.clear();
//				SnACodeList.clear();
				
				checkLapCode();
			}
		}
		//扫码光标离开事件相对应的代码
		if(etxsendSn.getId()==id){
			runClickFun();
		}	
		return istrue ;
	}
	
	
	
	//处理托标签数据
	private void checkLapCode() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Spellbiz.spellCheckBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxCrnrtNbr.getValStr());
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
					    uuid=map.get("RFLOT_UUID")+"";
					    type=map.get("RFLOT_SCAN_STATUS")+"";
					    BigDecimal bd=new BigDecimal(map.get("RFLOT_SEQ")+"");
					    seq = bd.toPlainString(); 
					    if("A".equals(type)){
					    	etx_sendmodel.setText("拼托");
					    }else{
					    	etx_sendmodel.setText("拆托");
					    }
					    if(StringUtil.parseInt(map.get("RFLOT_SCATTER_QTY"))>0){
					        etx_num.setText(map.get("RFLOT_SCATTER_QTY")+"");  //散量
					       _scan_qty=map.get("RFLOT_SCATTER_QTY")+"";
					       etx_bnum.setText(map.get("RFLOT_ITEM_QTY")+"");
					    }else{
					    	etx_num.setText(map.get("RFLOT_MULT_QTY")+"");
					    	etx_bnum.setText(map.get("RFLOT_ITEM_QTY")+"");
					    }
					    if(etx_num.getValStr().equals(etx_bnum.getValStr())){
					    	execute=true;
					    }
					    checkSNAll();
				}else{
					showErrorMsg(wr.getMessages());
					getFocues(etxCrnrtNbr, true);
					istrue = false;
				}
			}
		});
	}

	 boolean _indexf=true;
	//托标签查询出所有关联单件标签
	private void checkSNAll() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Spellbiz.spellCheckSnList(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxCrnrtNbr.getValStr(),type);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					List<Map<String, Object>>list=(List<Map<String, Object>>) wr.getDataToList();
					rflot_SnList.buildData(list);
					etx_bnum.setText(list.size()+"");
					if(list.size()>0){
					  histlist=list;
//					  if(_indexf){
//					     result=histlist.size();
//					    _indexf=false;
//					  }
					}
					
				}
			}
		});
	}
	
	
	//验证单件条码是否存在历史标签
	private void checkSN() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return Spellbiz.spellCheckSn(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxsendSn.getValStr(),type,seq,etx_sendCus.getValStr(),etxCrnrtNbr.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					Map<String, Object> map =  wr.getDataToMap();
					SnACodeList.add(map);    /*打标签*/
					isTrue();
				}else{
					showErrorMsg(wr.getMessages());
					etxsendSn.reValue();
					getFocues(etxsendSn, true);
					istrue = false;
					goon=false;
				}
			}
		});
	}
	
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit,ButtonType.Search};
	}	
	
//	@Override
//	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
//		//return super.OnBtnSaveValidata(btnType, v);
//
//	}
//
//	@Override
//	public Object OnBtnSaveClick(ButtonType btnType, View v) {
//		// TODO Auto-generated method stub
//
//		return  true;//super.OnBtnSaveClick(btnType, v);
//	}
//
//	@Override
//	public void OnBtnSaveCallBack(Object data) {
//		// TODO Auto-generated method stub
//		//super.OnBtnSaveCallBack(data);
//	}

	
//	private OnShowConfirmListen sListenSn=new OnShowConfirmListen() { 
//		@Override
//		public void onConfirmClick() {   //询问框  点确定
//			loadDataBase(new IRunDataBaseListens() {
//				@Override
//				public boolean onValidata() {
//					return true;
//				}
//				@Override
//				public Object onGetData() {
//					  return true;
//				}
//				@Override
//				public void onCallBrack(Object data) {
//
//				   }
//			});
//			
//		}
//		
//		@Override
//		public void onCancelClick() {  //询问框  点取消
//			//取消则回到该栏位位置选中该栏位值
//			getFocues(etxsendSn, true);
//			istrue=false;
//		}
//	};
//	
	public void sListenSn(){
		int index=0;
		
	    if("A".equals(type)){
	    	rflot_SnList.buildData(rList); 
	    	Map<String, Object> itemMap = new HashMap<String, Object>();
	    	if(histlist.size()==0){
	    		itemMap.put("RFLOTD_SN", etxsendSn.getValStr());
	    		histlist.add(itemMap);
	    		rflot_SnList.buildData(histlist);
	    	}else{
	    		
	  			for (int i = 0; i < histlist.size(); i++) {
	  				itemMap=histlist.get(i);
	  				if(itemMap.get("RFLOTD_SN").equals(etxsendSn.getValStr().toString().trim())){
						showErrorMsg("扫描的["+etxsendSn.getValStr()+"]单件标签已存在!");
						getFocues(etxsendSn, true);
						etxsendSn.reValue();
						index=1;
	  				}
	  			} 
	  			if(index==0){
	  			    	Map<String, Object> iMap = new HashMap<String, Object>();
	  			    	iMap.put("RFLOTD_SN", etxsendSn.getValStr());
			    		histlist.add(iMap);
			    		rflot_SnList.buildData(histlist); 
			    		removeList.add(iMap);
	  			}
	    	}
	    	boolean df=false;
			if(histlist.size()!=0&&index==0){
				if(StringUtil.parseInt(etx_bnum.getValStr())==0){
					 result2= StringUtil.parseInt(SnACodeList.size())+StringUtil.parseInt(1);
					 result3=1;

				}else{
					 if(result3==0){
						 if(StringUtil.parseInt(etx_bnum.getValStr())>0){
							 result2= StringUtil.parseInt(etx_bnum.getValStr())+1;
							 result3=1;
						 }
				   }else{
				      result2=StringUtil.parseInt(result2)+StringUtil.parseInt(1);
				   }
				}
				 etx_bnum.setText(result2+"");   
			}
			rflot_SnList.buildData(histlist);
	    }else{
			  Map<String, Object> itemMap = new HashMap<String, Object>();
		  		 if(histlist.size()>0){
		  			for (int i = 0; i < histlist.size(); i++) {
		  				itemMap=histlist.get(i);
		  				if(itemMap.get("RFLOTD_SN").equals(etxsendSn.getValStr().toString().trim())){
		  					removeList.add(histlist.get(i));
		  					histlist.remove(histlist.get(i));
		  					index=1;
		  				}
		  			}
		  			if(index==0){
			  			itemMap.put("RFLOTD_SN", etxsendSn.getValStr().toString().trim());
			  			removeList.add(itemMap);	
		  			}
		  			 etx_bnum.setText(histlist.size()+"");
		  		}else{
		  			itemMap.put("RFLOTD_SN", etxsendSn.getValStr().toString().trim());
		  			removeList.add(itemMap);
		  		}
			 rflot_SnList.buildData(histlist);  /*重新填充*/
			 
			if(SnACodeList.size()!=0){
			    // etx_num.setText(SnACodeList.size()+"");
				 etx_bnum.setText(SnACodeList.size()+"");
			}
		 }
	    
	    etxsendSn.reValue();
	    
	    gdvAddARemove.buildData(SnACodeList);
	    

	}
	boolean goon=true;
	public void  histlistRemoveSn(){
		if(null != etxsendSn.getValStr() && !"".equals( etxsendSn.getValStr().toString().trim())){
			 if(etxsendSn.getValStr().length()<37||etxsendSn.getValStr().length()>37){
					showErrorMsg("扫描的["+etxsendSn.getValStr()+"]单件标签长度有误!");
					getFocues(etxsendSn, true);
					etxsendSn.reValue();
					goon=false;
			 }else{
				 
				 if(SnACodeList.size()>0){
		  			 for (int i = 0; i < SnACodeList.size(); i++) {
		  				Map<String, Object> map=SnACodeList.get(i);
		  				if(map.get("RFLOTD_SN").equals(etxsendSn.getValStr().toString().trim())){
							showErrorMsg("扫描的["+etxsendSn.getValStr()+"]单件标签已存在!");
							getFocues(etxsendSn, true);
							etxsendSn.reValue();
							goon=false;
		  				}
		  			}
		  			if(goon){ 
		  			 checkSN();
		  			}
				}else{
					if(goon){ 
			  			 checkSN();
			  		}
				}
			 }
		 }
			

	}
	
	public void isTrue(){
		if ("A".equals(type)) {
			//  弹出框中处理
			//   showConfirm("此单件标签与此托标签需组成拼托关系吗?", sListenSn); 
			sListenSn();
		} else {
			//  弹出框中处理
			//   showConfirm("此单件标签是否解除与此托标签的关系?", sListenSn); 
					sListenSn();
		}
	}
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		jsonStr="";
		jsonStrremove="";
		jsonStrsnAcoderemove="";
		List<String> cache = new ArrayList<String>();
		for (int i = 0; i < histlist.size(); i++) {
			JSONObject obj = new JSONObject(histlist.get(i)); 
			cache.add(obj.toString());  
		}
		 jsonStr = cache.toString(); 
		 
	    List<String> snlist = new ArrayList<String>();
		for (int i = 0; i < removeList.size(); i++) {
			JSONObject obj = new JSONObject(removeList.get(i)); 
			snlist.add(obj.toString());  
		}
		
	    List<String> snAcodelist = new ArrayList<String>();
		for (int i = 0; i < SnACodeList.size(); i++) {
			JSONObject obj = new JSONObject(SnACodeList.get(i)); 
			snAcodelist.add(obj.toString());  
		}
		 jsonStr = cache.toString(); 
		 jsonStrremove = snlist.toString();
		 jsonStrsnAcoderemove = snAcodelist.toString(); 
		 return Spellbiz.spellSub(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),applicationDB.user.getUserType(),applicationDB.user.getUserId(),uuid,etxCrnrtNbr.getValStr(),jsonStr,jsonStrremove,jsonStrsnAcoderemove,type,etx_bnum.getValStr()); 
	}
	
	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr=(WebResponse)data;
		if(wr.isSuccess()){
			etxCrnrtNbr.reValue();
			etx_sendPart.setText("");  
			etx_sendPartDesc.setText("");
			etx_sendQty.setText("");  
			etx_sendUnit.setText("");
			etx_sendCus.setText("");  
			etx_sendmodel.setText("");
			etx_num.setText("");
			etx_bnum.setText("");
			etxsendSn.reValue();
			rflot_SnList.buildData(rList); 
			gdvAddARemove.buildData(rList); 
			
			histlist.clear();
			removeList.clear();
			SnACodeList.clear();
			showMessage(wr.getMessages());
		}else{
			String msg=wr.getMessages();
			showErrorMsg(msg);
		}
		
	}
	

	@Override
	public boolean OnBtnSerValidata(ButtonType btnType, View v) {
		return istrue;
	}
	
	@Override
	public Object OnBtnSerClick(ButtonType btnType, View v) {
		return Spellbiz.spellCheckSnList(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxCrnrtNbr.getValStr(),type);
	}
	
	@Override
	public void OnBtnSerCallBack(Object data) {
		//rflot_SnList.buildData((List<Map<String, Object>>)data);
		rflot_SnList.buildData(rList);
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			List<Map<String, Object>>list=(List<Map<String, Object>>) wr.getDataToList();
			 rflot_SnList.buildData(list);
			 etx_bnum.setText(list.size()+"");
		}else{
			if(!type.equals("A")){
		    	showErrorMsg(wr.getMessages());
			}
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
