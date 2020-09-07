package com.profitles.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.PsiViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyDateView;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyGridView;
import com.profitles.framwork.cusviews.view.MyLinearLayout;
import com.profitles.framwork.cusviews.view.MySpinner;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.util.StringUtil;


public class PsiActivity  extends AppFunActivity{

	private MyLinearLayout webView;
	private ApplicationDB applicationDB;
	private List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> codeList = new ArrayList<Map<String,Object>>();
	private Map<String,List<Map<String, Object>>> mapList = new HashMap<String,List<Map<String, Object>>>();
	Map<String, Object> itemmap = new HashMap<String, Object>();
	List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
	private PsiViewBiz biz;
	private String explaintxt,operenv, OPERENVNM;    //  说明     运行环境
	private String part, wo, line, op;               //零件号   加工单  生产线  工序     零件号默认了7004242  仅提供自己测试用  正式删掉
	private String shift="", edition, custpart, tiaoma, woukey, time, qty,sn;     //班次   版本号    客户件号               班次默认  中班    仅提供测试用   正式请删掉
	private String jsonStr="";
	JSONArray jArray = new JSONArray();
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_psi;
	}

	@Override
	protected void pageLoad() {
		biz = new PsiViewBiz();
		webView = (MyLinearLayout)findViewById(R.id.psiList);
		Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
		Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据  
		part = bundle.getString("PART");//getString()返回指定key的值  
		wo = bundle.getString("XSWO_NBR");
		line = bundle.getString("XSWO_LINE");
		op = bundle.getString("XSWO_OP");
		edition = bundle.getString("EDITION");
		custpart = bundle.getString("CUSTPART");
		tiaoma = bundle.getString("TIAOMA");    //条码
		woukey = bundle.getString("XSWO_UKEY");  
		qty = bundle.getString("QTY");      //数量
		OPERENVNM=bundle.getString("OPERENVNM");     //环境
		shift=bundle.getString("XSWO_SHIFT");
		time = applicationDB.user.getUserDate();
		sn=bundle.getString("SN");
		getLinePage();

	}


	@Override
	protected void unLockNbr() {

	}

	// 获取自检数据信息  赋值到dateList集合
	private void getLinePage(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public Object onGetData() {
				return biz.getPsiList(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), applicationDB.user.getUserId(),part);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					mapList = wrs.getDataToMapList();
					dateList=mapList.get("PsiList");
					codeList=mapList.get("ProCode");
					//获得的数据库交接项信息
					if(!StringUtil.isEmpty(dateList)&&dateList.size()>0){
						onCreate();
					}else{
						showErrorMsg(wrs.getMessages()+"该零件未维护自检参数");
						Intent intent = new Intent(PsiActivity.this,
								FinshOPActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("XSWO_NBR", wo);
						bundle.putString("XSWO_UKEY", woukey);
						bundle.putString("TURNTIAOMA", tiaoma);
						bundle.putString("SPACE", OPERENVNM);
						bundle.putString("TURNQTY", qty);
						intent.putExtras(bundle);
						startActivity(intent);
					}

				}else{
					showErrorMsg(wrs.getMessages());
				}
			}
		});
	}

	//动态布局数据显示
	@SuppressLint("NewApi")
	protected void onCreate() {
		
		boolean funbool=false;
		//创建一个LinearLayout容器  以下向容器中添加数据列
		MyLinearLayout mainLinerLayout = (MyLinearLayout) webView;
		for (int i = 0; i < dateList.size(); i++) {
			final int index=i;
			try {

				if(!funbool){
					funbool=true;
					MyTextView partName = new MyTextView(this);
					partName.setText("工号:"+applicationDB.user.getUserId()+"            零件:"+part+"          序列码:"+tiaoma+"       SN号："+sn);
					mainLinerLayout.addView(partName);

					//创建标题Text  设置text内容
					MyTextView functionEn = new MyTextView(this);
					functionEn.setText("备注:");
					//						functionEn.setTextSize(15);
					final MySpinner function =  new MySpinner(this);
					for (int j = 0; j < codeList.size(); j++) {
								function.addItem(codeList.get(j).get("RJP_NAME")+"",codeList.get(j).get("RJP_ID")+"");
					}
//					//创建下拉框      添加内容
//					function.addItem("正常", "10");                                  
//					function.addItem("首件", "20");
//					function.addItem("末件", "30");
//					function.addItem("调程序", "40");
//					function.addItem("换刀具", "50");
//					function.addItem("其他", "60");
					mainLinerLayout.addView(functionEn);
					mainLinerLayout.addView(function);
					function.setOnItemSelectedListener(new MySpinner.OnItemSelectedListener() {//选择item的选择点击监听事件  
						@SuppressLint("NewApi")
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							operenv=function.getValStr().toString();
							OPERENVNM = function.getValStrNm().toString();
						}
						public void onNothingSelected(AdapterView<?> arg0) {
							operenv=function.getValStr().toString();
							OPERENVNM = function.getValStrNm().toString();
						}
					});
				}
				
				MyTextView qcp_titleType = new MyTextView(this);
				qcp_titleType.setText("自检项:");
				//					qcp_titleType.setTextSize(15);
				//创建文本框    初始化内容   设置字体大小size    设置文本框宽度
				MyEditText qcp_type = new MyEditText(this);
				itemmap.put("item"+index,dateList.get(i).get("QCP_ITEM"));
				qcp_type.setText(dateList.get(i).get("QCP_ITEM")+"");
				//					qcp_type.setTextSize(15);
				qcp_type.setWidth(220);
				qcp_type.setReadOnly(true);


				MyTextView Measuringtool = new MyTextView(this);
				Measuringtool.setText("     量具:");
				//创建文本框    初始化内容   设置字体大小size    设置文本框宽度
				MyEditText tool = new MyEditText(this);
				itemmap.put("tool"+index,dateList.get(i).get("QCP_TOOL"));
				tool.setText(dateList.get(i).get("QCP_TOOL")+"   ");
				tool.setWidth(180);
				tool.setReadOnly(true);


				MyTextView result = new MyTextView(this);
				result.setText("      结果:");

				final MyEditText option =  new MyEditText(this);
				option.setWidth(120);
				option.setText("合格");
				option.setReadOnly(true);
				itemmap.put("option"+index, option.getValStr().toString());
//				option.setDropDownWidth(1);
//				option.setClickable(false);
				option.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						itemmap.put("option"+index, option.getValStr().toString());
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						
					}
					@Override
					public void afterTextChanged(Editable s) {
						
					}
				});

				MyTextView qcp_titleDefault = new MyTextView(this);
				qcp_titleDefault.setText("  测试值:");
				final MyTextView num = new MyTextView(this);
				float max=StringUtil.parseFloat(dateList.get(i).get("QCP_MAX"));
				float min=StringUtil.parseFloat(dateList.get(i).get("QCP_MIN"));
				final MyEditText defaults = new MyEditText(this);
				float ainum=StringUtil.parseFloat(dateList.get(i).get("QCP_DEFAULT")+"");
				final MyTextView maxvalue =new MyTextView(this);
				final MyTextView minvalue =new MyTextView(this);
				final RadioGroup radiogroup= new RadioGroup(this);
				final RadioButton radiobutton=new RadioButton(this);
				final RadioButton radiobutton2=new RadioButton(this);
				maxvalue.setText(dateList.get(i).get("QCP_MAX")+"");
				minvalue.setText(dateList.get(i).get("QCP_MIN")+"");
				
				if(dateList.get(i).get("QCP_DATATYPE").equals("数字")){
//					defaults.setText(dateList.get(i).get("QCP_DEFAULT")+"");
					defaults.setWidth(186);
//					defaults.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
					itemmap.put("defaults"+index, defaults.getValStr().toString());

					num.setText("     范围:{"+dateList.get(i).get("QCP_NUM")+"}"+"");
					num.setTextSize(15);
					num.setWidth(320);

					MyLinearLayout line=new MyLinearLayout(this);
					line.addView(qcp_titleType);
					line.addView(qcp_type);
					line.addView(Measuringtool);
					line.addView(tool);
					line.addView(qcp_titleDefault);
					line.addView(defaults);
					line.addView(result);
					line.addView(option);
					line.addView(num);
					mainLinerLayout.addView(line);
					defaults.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
							//输入内容之前你想做什么
							
						}
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							String result =defaults.getValStr().toString();
							float resultvalue=StringUtil.parseFloat(result);
							String maxresult =maxvalue.getValStr().toString();
							float maxvalue=StringUtil.parseFloat(maxresult);
							String minresult =minvalue.getValStr().toString();
							float minvalue=StringUtil.parseFloat(minresult);
							float def=StringUtil.parseFloat(dateList.get(index).get("QCP_DEFAULT"));
							float endval=def+resultvalue;
							if(endval>maxvalue||endval<minvalue){
								defaults.setTextColor(Color.RED);
								num.setTextColor(Color.RED);
								option.setText("不合格");
							}else{
								defaults.setTextColor(Color.BLACK);
								num.setTextColor(Color.BLACK);
								option.setText("合格");
							}
						}
						@Override
						public void afterTextChanged(Editable s) {
//							String standard="^([0-9][0-9]*)+(.[0-9]{1,4})?$";
							String standard="^[-+]?([0-9][0-9]*)+(.[0-9]{1,4})?$";
							Pattern pattern = Pattern.compile(standard);
							String defStr=defaults.getValStr().toString();
							if(!StringUtil.isEmpty(defStr)){
								if(defStr.length()-defStr.replace(".", "").length()==1 && defStr.charAt(defStr.length()-1)=='.'
										||defStr.length()-defStr.replace("-", "").length()==1 && defStr.charAt(defStr.length()-1)=='-'
										||defStr.length()-defStr.replace("+", "").length()==1 && defStr.charAt(defStr.length()-1)=='+'){
									itemmap.put("defaults"+index, defaults.getValStr().toString());
									return;
								}
								Matcher matcher = pattern.matcher(defStr);
							    if(!matcher.matches()){
							    	showErrorMsg("该自检项的测试值只能输入数字类型,且小数位只能是四位!");
							    	defaults.setText("");
							    	return;
							    }
							}
							itemmap.put("defaults"+index, defaults.getValStr().toString());
						}
					});
//					if(ainum>max || ainum<min){
//						defaults.setTextColor(Color.RED);
//						num.setTextColor(Color.RED);
//						option.setText("不合格");
//					}else{
//						defaults.setTextColor(Color.BLACK);
//						num.setTextColor(Color.BLACK);
//						option.setText("合格");
//					}
				}else if(dateList.get(i).get("QCP_DATATYPE").equals("字符")){
					defaults.setText(dateList.get(i).get("QCP_DEFAULT")+"");
					defaults.setWidth(186);
					itemmap.put("defaults"+index, defaults.getValStr().toString());

					final MySpinner XZoption =  new MySpinner(this);
					//创建下拉框      添加内容
					XZoption.addItem("合格", "合格");
					XZoption.addItem("不合格", "不合格");
					XZoption.setOnItemSelectedListener(new MySpinner.OnItemSelectedListener() {//选择item的选择点击监听事件  
						@SuppressLint("NewApi")
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							itemmap.put("option"+index, XZoption.getValStr().toString());
						}
						public void onNothingSelected(AdapterView<?> arg0) {
							itemmap.put("option"+index, XZoption.getValStr().toString());
						}
					});
					
					MyLinearLayout line=new MyLinearLayout(this);
					line.addView(qcp_titleType);
					line.addView(qcp_type);
					line.addView(Measuringtool);
					line.addView(tool);
					line.addView(qcp_titleDefault);
					line.addView(defaults);
					line.addView(result);
					line.addView(XZoption);
//					line.addView(num);
					mainLinerLayout.addView(line);
					defaults.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
							//输入内容之前你想做什么
						}
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							
						}
						@Override
						public void afterTextChanged(Editable s) {
							itemmap.put("defaults"+index, defaults.getValStr().toString());
						}
					});
				}else if(dateList.get(i).get("QCP_DATATYPE").equals("逻辑")){
					radiobutton.setText("好");
					radiobutton.setId(0);
					radiobutton.setTranslationY(-14);
					radiobutton2.setText("不好");
					radiobutton2.setId(1);
					radiobutton2.setTranslationY(-14);
					radiogroup.setOrientation(LinearLayout.HORIZONTAL);
					radiogroup.addView(radiobutton, 0);
					radiogroup.addView(radiobutton2, 1);
//					radiogroup.check(0);  初始化页面默认选中【好】
					itemmap.put("defaults"+index, "");  //初始值为""

//					num.setText("     范围:{"+dateList.get(i).get("QCP_NUM")+"}"+"");
//					num.setTextSize(15);
//					num.setWidth(320);

					MyLinearLayout line=new MyLinearLayout(this);
					line.addView(qcp_titleType);
					line.addView(qcp_type);
					line.addView(Measuringtool);
					line.addView(tool);
					line.addView(qcp_titleDefault);
					line.addView(radiogroup);
					line.addView(result);
					line.addView(option);
//					line.addView(num);
					mainLinerLayout.addView(line);
					//选择框组合监听
					radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener()  {
						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							if(checkedId==radiobutton.getId()){
//								String count=String.valueOf(checkedId);	
								itemmap.put("defaults"+index, "好");
								option.setText("合格");
							}else{
//								String count=String.valueOf(checkedId);						 						
								itemmap.put("defaults"+index, "不好");	
								option.setText("不合格");
							}
						}
					}); 
				}else{
					final MyDateView date=new MyDateView(this);
//					final DatePicker date=new DatePicker(this);
					date.setWidth(186);
					Date dateText = new Date();
					SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
					date.setText(dateFormat.format(dateText));
					itemmap.put("defaults"+index, date.getValStr().toString());
				    date.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							String dateStr=date.getValStr().toString().trim();
							Pattern pattern = Pattern
					                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
					        Matcher m = pattern.matcher(dateStr);
					        if (!m.matches()) {
					        	showErrorMsg("不符合日期格式!");
					        	itemmap.put("defaults"+index,dateStr);
					            return;
					        }
							itemmap.put("defaults"+index, dateStr);
						}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,
								int after) {
							itemmap.put("defaults"+index, date.getValStr().toString());
							
						}
						@Override
						public void afterTextChanged(Editable s) {
							// TODO Auto-generated method stub
							
						}
					});
//					num.setText("     范围:{"+dateList.get(i).get("QCP_NUM")+"}"+"");
//					num.setTextSize(15);
//					num.setWidth(320);

					MyLinearLayout line=new MyLinearLayout(this);
					line.addView(qcp_titleType);
					line.addView(qcp_type);
					line.addView(Measuringtool);
					line.addView(tool);
					line.addView(qcp_titleDefault);
					line.addView(date);
//					line.addView(result);
//					line.addView(option);
//					line.addView(num);
					mainLinerLayout.addView(line);
					
				}


				//数据列表最后添加说明    判断是不是最后一列
				if((i+1)==dateList.size()){
					MyLinearLayout explan=new MyLinearLayout(this);
					MyTextView explain = new MyTextView(this);
					explain.setText("说明:");

					final MyEditText explainText = new MyEditText(this);
					//						explainText.setTextSize(15);
					explainText.setWidth(1500);
					explan.addView(explain);
					explan.addView(explainText);
					mainLinerLayout.addView(explan);
					//						final String itemName=explainText.getValStr().trim();
					explainText.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {
							//输入内容之前你想做什么
						}
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							//输入的时候你想做什么
						}
						@Override
						public void afterTextChanged(Editable s) {
							explaintxt=explainText.getValStr().toString();
						}
					});
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		itemList.add(itemmap);
		jArray.put(itemList);
	}

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Submit,ButtonType.Return,ButtonType.Help};
	}


	/**
	 * 提交
	 */
	public boolean OnBtnSubValidata(ButtonType btnType, View v) {
		return true;
	}

	public Object OnBtnSubClick(ButtonType btnType, View v) {
		jsonStr="";
		//itemmap参数转json
		List<String> cache = new ArrayList<String>();
		JSONObject obj = new JSONObject(itemList.get(0)); 
		cache.add(obj.toString());
		jsonStr = cache.toString();
		return biz.savePsiList(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),applicationDB.user.getUserId(),explaintxt,operenv,
				part,wo,line,op,shift,edition,custpart,jsonStr, tiaoma, woukey, time, qty, OPERENVNM);
	}

	public void OnBtnSubCallBack(Object data) {
		WebResponse wr = (WebResponse)data;
		if(wr.isSuccess()){
			showSuccessMsg(wr.getMessages());
			Intent intent = new Intent(PsiActivity.this,
					FinshOPActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("XSWO_NBR", wo);
			bundle.putString("XSWO_UKEY", woukey);  
			bundle.putString("SPACE", OPERENVNM);
			intent.putExtras(bundle);
			startActivity(intent);
		}else{
			showErrorMsg(wr.getMessages());
		}
	}


	/**
	 * 返回   To
	 */
	@Override
	public boolean OnBtnRtnValidata(ButtonType btnType, View v) {
		return true;
	}

	@Override
	public Object OnBtnRtnClick(ButtonType btnType, View v) {
		return null;
	}
	@Override
	public void OnBtnRtnCallBack(Object data) {
		Intent intent = new Intent(PsiActivity.this,
				FinshOPActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("XSWO_NBR", wo);
		bundle.putString("XSWO_UKEY", woukey);
		bundle.putString("TURNTIAOMA", tiaoma);
		bundle.putString("SPACE", OPERENVNM);
		bundle.putString("TURNQTY", qty);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	/**
	 * end
	 */



	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}
}