package com.profitles.framwork.cusviews.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.profitles.activity.R;
import com.profitles.framwork.cusviews.view.bean.ValidataBean;
import com.profitles.framwork.cusviews.view.css.MainCss;
import com.profitles.framwork.cusviews.view.interfaces.IMyBaseView;
import com.profitles.framwork.cusviews.view.validata.ViewValidata;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class MySpinner extends Spinner implements IMyBaseView{

	private ValidataBean vdbean;
	private SimpleAdapter sadp;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private String valueId = "Value", textId = "Text", hint; 

	public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public MySpinner(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.spinnerStyle);
	}

	public MySpinner(Context context) {
		this(context, null);
	}

	private void init(Context context, AttributeSet attrs){
		MainCss.setcss(this);

		from = new String[]{ textId, valueId };
		to = new int[]{ R.id.sys_spnItemText, R.id.sys_spnItemKey };

		vdbean = ViewValidata.initValidata(context, attrs);
		
		TypedArray t = context.obtainStyledAttributes(attrs,com.profitles.activity.R.styleable.EditAllView);
		hint = t.getString(com.profitles.activity.R.styleable.EditAllView_hint);
		t.recycle();
		
		if(!StringUtil.isEmpty(hint)){
			addItem(hint, "");
		}
//		this.setPadding(0, 0, 0, 0);
	}
	
	
	
	public void clearItems(){
		data = new ArrayList<Map<String,String>>();
		sadp = new SimpleAdapter(getContext(), data, R.layout.sys_view_spinner_item, from, to);
		setAdapter(sadp);
		

		LinearLayout.LayoutParams spi_parent = new LinearLayout.LayoutParams(
                 this.getWidth() == 0 ? LayoutParams.MATCH_PARENT : this.getWidth() , LayoutParams.WRAP_CONTENT);
		DisplayMetrics dm = new DisplayMetrics();  
        dm = this.getContext().getApplicationContext().getResources().getDisplayMetrics();
        Configuration cf = getContext().getResources().getConfiguration();
		if(getHeight() == 0){
			if(cf.orientation == cf.ORIENTATION_LANDSCAPE){
				//横屏
				spi_parent.height = ((int)(Constants.EditDefHavHeight * dm.density));
			}else if(cf.orientation == cf.ORIENTATION_PORTRAIT){
				//竖屏
				spi_parent.height = ((int)(Constants.EditDefHeight * dm.density));
			}
		}
		this.setLayoutParams(spi_parent);
	}
	
	public void addAndClearItem(String key, String value){
		clearItems();
		addItem(key, value);
	}
	
	public void addItem(String text, String value){
		if(data == null) clearItems();
		data.add(crtItem(text, value));
		sadp.notifyDataSetChanged();
	}
	/*
	 * @使用该方法进行下拉框赋值，不会出现空值的现象
	 * wade 20181204
	 * */
	public void addItemW(String text, String value){
		if(data == null) clearItems();
		data.clear();
		data.add(crtItem(text, value));
		sadp.notifyDataSetChanged();
	}
	public void addAndClearItems(String[] values){
		clearItems();
		addItems(values);
	}
	
	public void addItems(String[] values){
		if(data == null) clearItems();
		for(int i = 0; values != null && i < values.length; i++){
			data.add(crtItem(values[i], values[i]));
		}
		sadp.notifyDataSetChanged();
	}
	
	private Map<String, String> crtItem(String text, String value){
		Map<String, String> item = new HashMap<String, String>();
		item.put(textId, text);
		item.put(valueId, value);
		return item;
	}

	public ValidataBean getVdbean() {
		return vdbean;
	}

	public void setVdbean(ValidataBean vdbean) {
		this.vdbean = vdbean;
	}

	public String getValStr() {
		Object item = getSelectedItem();
		if(item instanceof Map) return ((Map<String, String>)item).get(valueId);
		return null;
	}
	public String getValStrNm() {
		Object item = getSelectedItem();
		if(item instanceof Map) return ((Map<String, String>)item).get(textId);
		return null;
	}
}
