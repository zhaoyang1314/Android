package com.profitles.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.profitles.biz.CpdxBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;

public class CpdxActivity extends AppFunActivity{

	private CpdxBiz cpdxBiz;
	private MyEditText  etx_sendPart,etx_sendPartDesc,etx_sendQty,etx_sendUnit, 
						etx_sendCus,etx_num,etx_bnum, 
						etx_SplitNum, etx_ScanType;
	private MyTextView txvSonScan, txvSourceScan, txvSplitNum;
	private MyReadBQ etx_targetScan, etx_oneScan, etx_SourceScan;
	private MyDataGrid rflot_SnList;
	private ApplicationDB applicationDB;
	private String domain, site, userid, date;
	private List<Map<String,Object>> snList = new ArrayList<Map<String,Object>>(); // 目标标签下的子标签信息
	
	
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_cpdx;
	}
	
	@Override
	protected void pageLoad() {
		  cpdxBiz = new CpdxBiz();
		  domain = ApplicationDB.user.getUserDmain();
		  site = ApplicationDB.user.getUserSite();  
		  userid = ApplicationDB.user.getUserId();  
		  date = ApplicationDB.user.getUserDate(); 
		  
		  // 目标标签改变事件
		  etx_targetScan = (MyReadBQ) findViewById(R.id.etx_targetScan);
		  etx_targetScan.addTextChangedListener(new TextWatcher() { 
			  
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!StringUtil.isEmpty(etx_targetScan.getValStr())){
						checkTargetScan();
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				public void afterTextChanged(Editable s) {}
		  });
		  
		  // 单件标签改变事件
		  etx_oneScan = (MyReadBQ) findViewById(R.id.etx_oneScan);
		  etx_oneScan.addTextChangedListener(new TextWatcher() { 
			  
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(!StringUtil.isEmpty(etx_oneScan.getValStr())){
						// 匹配子件标签
						checkSonScan();
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				public void afterTextChanged(Editable s) {}
			  });
		  
		  // 源标签改变事件
		  etx_SourceScan = (MyReadBQ) findViewById(R.id.etx_SourceScan);
		  etx_SourceScan.addTextChangedListener(new TextWatcher() { 
			  
			  public void onTextChanged(CharSequence s, int start, int before, int count) {
				  if(!StringUtil.isEmpty(etx_SourceScan.getValStr())){

					  // 检查源标签
					  checkSourceScan();
				  }
			  }
			  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			  public void afterTextChanged(Editable s) {}
		  });
		  
		  // 界面显示的数据信息
		  etx_sendPart = (MyEditText) findViewById(R.id.etx_sendPart);
		  etx_sendPartDesc = (MyEditText) findViewById(R.id.etx_sendPartDesc);
		  etx_sendQty = (MyEditText) findViewById(R.id.etx_sendQty);
		  etx_sendUnit = (MyEditText) findViewById(R.id.etx_sendUnit);
		  etx_sendCus = (MyEditText) findViewById(R.id.etx_sendCus);
		  etx_num = (MyEditText) findViewById(R.id.etx_num);
		  etx_bnum = (MyEditText) findViewById(R.id.etx_bnum);
		  etx_bnum = (MyEditText) findViewById(R.id.etx_bnum);
		  txvSonScan = (MyTextView) findViewById(R.id.txvSonScan);
		  txvSourceScan = (MyTextView) findViewById(R.id.txvSourceScan);
		  txvSplitNum = (MyTextView) findViewById(R.id.txvSplitNum);
		  
		  // 文本编辑框
		  etx_SplitNum = (MyEditText) findViewById(R.id.etx_SplitNum);// 拆分数量
		  etx_ScanType = (MyEditText) findViewById(R.id.etx_ScanType);// 批控类型
		  
		  rflot_SnList = (MyDataGrid)findViewById(R.id.rflot_SN);
		  
	} 
	
	/**
	 * 检查目标标签是否是有效的标签
	 *
	 * @Description: TODO
	 * 
	 *
	 */
	private void checkTargetScan() {
		loadDataBase(new IRunDataBaseListens() {
			
			@Override
			public boolean onValidata() { return true; }
			
			@Override
			public Object onGetData() {
				return cpdxBiz.checkTargetScan(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), etx_targetScan.getValStr().toString(),applicationDB.user.getUserId());
			}
			
			@Override
			public void onCallBrack(Object data) {
				
				try{
					
					// 获取查询数据
					WebResponse wr = (WebResponse)data;
					if(wr.isSuccess()){
						
						Map<String, Object> map = wr.getDataToMap();
						etx_sendPart.setText(StringUtil.isEmpty(map.get("RFLOT_PART"), ""));
						etx_sendPartDesc.setText(StringUtil.isEmpty(map.get("RFLOT_PART_DESC"), ""));
						etx_sendQty.setText(StringUtil.isEmpty(map.get("RFLOT_BOX_QTY"), ""));
						etx_sendUnit.setText(StringUtil.isEmpty(map.get("RFLOT_UM"), ""));
						etx_sendCus.setText(StringUtil.isEmpty(map.get("RFLOT_CUST_PART"), ""));
						etx_num.setText(StringUtil.isEmpty(map.get("RFLOT_NUM"), "0"));
						etx_bnum.setText(StringUtil.isEmpty(map.get("RFLOT_BNUM"), "0"));
						etx_ScanType.setText(StringUtil.isEmpty(map.get("RFLOT_LBS"), ""));// 将批控类型绑定到该隐藏控件中，便于后续获取该值
						etx_SourceScan.setText("");
						etx_SplitNum.setText("");
						etx_oneScan.setText("");
						
						/**
						 * 判断批控类型
						 * 	L：批号
						 *  B：箱号
						 *  S：序号
						 *  
						 *  根据不同的批控类型隐藏或显示对应的控件
						 */
						// IG6108_修改扫描批控类型为序号的标签时，和进行扫箱号类型的标签一致_20190731_by_Michael_start
						if("L".equals(StringUtil.isEmpty(map.get("RFLOT_LBS"), "")) || "B".equals(StringUtil.isEmpty(map.get("RFLOT_LBS"), ""))){
							
							// 设置子件标签整行不可见
							txvSonScan.setVisibility(View.GONE);
							etx_oneScan.setVisibility(View.GONE);// 不可见，并且不留痕迹，不占位置
							
							// // 设置源标签、拆出数量整行可见
							txvSourceScan.setVisibility(View.VISIBLE);
							etx_SourceScan.setVisibility(View.VISIBLE);
							txvSplitNum.setVisibility(View.VISIBLE);// 设置拆分数量可见
							etx_SplitNum.setVisibility(View.VISIBLE);
						} else if("S".equals(StringUtil.isEmpty(map.get("RFLOT_LBS"), ""))){
							
							// 设置子件标签整行可见
							txvSonScan.setVisibility(View.VISIBLE);
							etx_oneScan.setVisibility(View.VISIBLE);
							
							// 设置源标签、拆出数量整行不可见
							txvSourceScan.setVisibility(View.GONE);
							etx_SourceScan.setVisibility(View.GONE);// 不可见，并且不留痕迹，不占位置
							txvSplitNum.setVisibility(View.GONE);// 设置拆分数量不可见
							etx_SplitNum.setVisibility(View.GONE);// 不可见，并且不留痕迹，不占位置
						}
						// IG6108_修改扫描批控类型为序号的标签时，和进行扫箱号类型的标签一致_20190731_by_Michael_end
						
						showMessage("");
						
						// 查询目标标签下的子标签
						querySnAll();
					} else {
						
						showErrorMsg(wr.getMessages());
						clear();
						getFocues(etx_targetScan, true);
					}
				} catch (Exception e){
					
					showErrorMsg(e.getMessage());
				}
				
			}
		});
	}
	
	/**
	 * 查询目标标签下的子件标签
	 */ 
	private void querySnAll() {
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			@Override
			public Object onGetData() {
				return cpdxBiz.getSnList(domain, site, etx_targetScan.getValStr());
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr = (WebResponse)data;
				
				// 防止第二次为目标标签倒箱时，已经存在的datagrid不会清空的问题
				snList = new ArrayList<Map<String,Object>>();
				rflot_SnList.buildData(snList); 
				if(wr.isSuccess()){
					List<Map<String, Object>> list = (List<Map<String, Object>>) wr.getDataToList();
					snList = list;
					rflot_SnList.buildData(list);
				}
				getFocues(etx_oneScan, true);// 子标签获取焦点
			}
		});
	}
	
	/**
	 * 匹配子件标签
	 *
	 * @Description: TODO
	 * 
	 *
	 */
	private void checkSonScan() {
			loadDataBase(new IRunDataBaseListens() {
			
			@Override
			public boolean onValidata() {
				
				if(StringUtil.isEmpty(etx_targetScan.getValStr())){
					
					showErrorMsg("请扫描目标标签之后再做操作！");
					etx_oneScan.setText("");// 将值置空
					return false;
				}
				return true;
			}
			
			@Override
			public Object onGetData() {
				return cpdxBiz.checkSonScan(applicationDB.user.getUserDmain(), 
						applicationDB.user.getUserSite(), etx_targetScan.getValStr().toString(), 
						etx_oneScan.getValStr().toString(), applicationDB.user.getUserId());
			}
			
			@Override
			public void onCallBrack(Object data) {
				
				// 获取查询数据
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					
					if("autoSubmit".equalsIgnoreCase(wr.getMessages())){
						
						clear();
						showMessage("自动提交成功!请尽快修改纸质标签的数量喔！");
						getFocues(etx_targetScan, true);
					} else {
						
						showMessage("倒箱成功！");
						Map<String, Object> map = wr.getDataToMap();
						etx_num.setText(StringUtil.isEmpty(map.get("RFLOT_NUM"), "0"));
						etx_bnum.setText(StringUtil.isEmpty(map.get("RFLOT_NUM"), "0"));
						
						// 查询目标标签的子件标签信息
						querySnAll();
						etx_oneScan.setText("");
						getFocues(etx_oneScan, true);
					}
				} else {
					
					showErrorMsg(wr.getMessages());
					etx_oneScan.setText("");
					getFocues(etx_oneScan, true);
				}
			}
		});
	}
	
	/**
	 * 检查源标签
	 *
	 * @Description: TODO
	 * 
	 *
	 */
	private void checkSourceScan() {
		loadDataBase(new IRunDataBaseListens() {
			
			@Override
			public boolean onValidata() { 
			
				if(StringUtil.isEmpty(etx_targetScan.getValStr())){
					
					showErrorMsg("请扫描目标标签之后再做操作！");
					etx_SourceScan.setText("");// 将值置空
					return false;
				}
				return true;
			}
			
			@Override
			public Object onGetData() {
				return cpdxBiz.checkSourceScan(applicationDB.user.getUserDmain(), 
						applicationDB.user.getUserSite(), etx_targetScan.getValStr().toString(), 
						etx_SourceScan.getValStr().toString(), applicationDB.user.getUserId(), etx_ScanType.getValStr().toString());
			}
			
			@Override
			public void onCallBrack(Object data) {
				
				// 获取查询数据
				WebResponse wr = (WebResponse)data;
				if(wr.isSuccess()){
					
					// 校验成功，将光标移动到拆出数量栏位
					etx_SplitNum.setText("");
					getFocues(etx_SplitNum, true);
				} else {
					
					showErrorMsg(wr.getMessages());
					etx_SourceScan.setText("");
					getFocues(etx_SourceScan, true);
				}
			}
		});
	}
	
	protected boolean onBlur(int id, View v) {
		
		//扫码光标离开事件相对应的代码
		if(etx_targetScan.getId() == id){
			runClickFun();
		}
		
		//扫码光标离开事件相对应的代码
		if(etx_oneScan.getId() == id){
			runClickFun();
		}	
		
		//扫码光标离开事件相对应的代码
		if(etx_SourceScan.getId() == id){
			runClickFun();
		}	
		return true ;
	}
	
	@Override
	protected void unLockNbr() {}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}
	
	/**
	 * 提交
	 */
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		
		if(StringUtil.isEmpty(etx_targetScan.getValStr())){
			
			showErrorMsg("请扫描目标标签之后再做操作！");
			return false;
		}
		
		return true;
	}
	
	@Override
	public Object OnBtnSubClick(ButtonType btnType, View v) {
		return cpdxBiz.submit(domain, site, etx_targetScan.getValStr(), userid, etx_bnum.getValStr());
	}

	@Override
	public void OnBtnSubCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		try{
			
			if(wr.isSuccess()){
				
				clear();
				showMessage(wr.getMessages());
			} else {
				
				String msg = wr.getMessages();
				showErrorMsg(msg);
			}
		} catch (Exception e){
			
			showErrorMsg("提交失败！" + e.getMessage());
		}
		
	}
	
	//清空
	public void clear(){
		etx_targetScan.reValue();
		etx_oneScan.reValue();
		etx_sendPart.setText("");  
		etx_sendPartDesc.setText("");
		etx_sendQty.setText("");  
		etx_sendUnit.setText("");
		etx_sendCus.setText("");  
		etx_num.setText("");
		etx_bnum.setText("");
		etx_SourceScan.setText("");
		etx_SplitNum.setText("");
		snList = new ArrayList<Map<String,Object>>();
		rflot_SnList.buildData(snList); 
		getFocues(etx_targetScan, true);
	}
	
	
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Save,ButtonType.Submit};
	}	
	
	
	// 保存
	@Override
	public boolean OnBtnSaveValidata(ButtonType btnType, View v) {
		
		/****************优化成品倒箱保存按钮只允许在批号、箱号倒箱场景下可用及一些校验优化_20190724_by_Michael*****************/
		if(StringUtil.isEmpty(etx_targetScan.getValStr())){
			
			showErrorMsg("请扫描目标标签之后再做操作！");
			return false;
		}

		// 只有标签对应的零件的批控类型为批号、箱号的时候才可以使用保存按钮，否则提示不可用
		if("S".equals(etx_ScanType.getValStr()) || "".equals(etx_ScanType.getValStr())){
			
			showErrorMsg("操作失败！该按钮仅限按批号拼箱操作时才可用！");
			return false;
		}
		
		if(StringUtil.isEmpty(etx_SourceScan.getValStr())){
			
			showErrorMsg("请扫描源标签之后再做操作！");
			return false;
		}
		
		if(StringUtil.isEmpty(etx_SplitNum.getValStr())){
			
			showErrorMsg("请输入拆出数量之后再做操作！");
			return false;
		}
		
		return true;
	}

	@Override
	public Object OnBtnSaveClick(ButtonType btnType, View v) {
		return cpdxBiz.save(domain, site, etx_targetScan.getValStr(), userid, etx_SourceScan.getValStr(), etx_SplitNum.getValStr());
	}
	
	@Override
	public void OnBtnSaveCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		try{
			
			if(wr.isSuccess()){
				
				showMessage("倒箱成功！");
				Map<String, Object> map = wr.getDataToMap();
				etx_num.setText(StringUtil.isEmpty(map.get("RFLOT_NUM"), "0")); // 已扫标签的数量
				etx_bnum.setText(StringUtil.isEmpty(map.get("RFLOT_SNUM"), "0")); // 已扫数量
				
				// 查询目标标签的子件标签信息
				querySnAll();
				etx_SourceScan.setText("");
				etx_SplitNum.setText("");
				getFocues(etx_SourceScan, true);
			} else {
				
				String msg = wr.getMessages();
				showErrorMsg(msg);
				etx_SourceScan.setText("");
				etx_SplitNum.setText("");
				getFocues(etx_SourceScan, true);
			}
		} catch (Exception e){
			
			showErrorMsg("保存失败！" + e.getMessage());
		}
	}
	
}
