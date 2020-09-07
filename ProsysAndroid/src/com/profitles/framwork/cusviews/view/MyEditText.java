package com.profitles.framwork.cusviews.view;

import android.R;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.profitles.framwork.cusviews.view.bean.ValidataBean;
import com.profitles.framwork.cusviews.view.css.MainCss;
import com.profitles.framwork.cusviews.view.interfaces.IMyBaseView;
import com.profitles.framwork.cusviews.view.interfaces.IMyEditText;
import com.profitles.framwork.cusviews.view.validata.ViewValidata;
import com.profitles.framwork.util.Constants;

public class MyEditText extends EditText implements IMyBaseView, IMyEditText{
	
	private ValidataBean vdbean;
	private KeyListener kListener;
	private Drawable bgdb;
	private String defValue;

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		MainCss.setcss(this);
		init(context, attrs);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.editTextStyle);
	}

	public MyEditText(Context context) {
		this(context, null);
	}

	public String getValStr() {
		return getText().toString();
	}
	
	private void init(Context context, AttributeSet attrs){
		vdbean = ViewValidata.initValidata(context, attrs);
		if(vdbean.isReadOnly()) setReadOnly(true);
		setSingleLine(true);
		defValue = getValStr();
		
		DisplayMetrics dm = new DisplayMetrics();  
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
		if(getHeight() == 0){
			if(cf.orientation == cf.ORIENTATION_LANDSCAPE){
				//横屏
				setHeight((int)(Constants.EditDefHavHeight * dm.density));
			}else if(cf.orientation == cf.ORIENTATION_PORTRAIT){
				//竖屏
				setHeight((int)(Constants.EditDefHeight * dm.density));
			}
		}
	}

	public ValidataBean getVdbean() {
		return vdbean;
	}

	public void setVdbean(ValidataBean vdbean) {
		this.vdbean = vdbean;
	}
	
	public void setReadOnly(boolean isro){
		if(isro) {
			kListener = kListener == null ? getKeyListener() : kListener;
			bgdb = getBackground();
			setKeyListener(null);
			setBackgroundColor(MainCss.ReadOnlyBgColor);
		}else if(!isro && kListener != null && bgdb != null){
			setKeyListener(kListener);
			setBackgroundDrawable(bgdb);
		}
		this.setFocusable(!isro);
	}
	
	public void reValue(){
		setText(defValue);
	}

	@Override
	public void setLayoutParams(android.view.ViewGroup.LayoutParams params) {
		if(params instanceof LayoutParams && ((LayoutParams)params).weight > 0){
			((LayoutParams)params).width = 0;
		}
		super.setLayoutParams(params);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.setFocusable(enabled);
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		this.setFocusable(visibility == View.VISIBLE);
	}
	
	

}
