package com.profitles.framwork.cusviews.view.interfaces;

import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;

import com.profitles.framwork.cusviews.view.bean.ValidataBean;

public interface IMyEditText {
	
	public int getId();
	
	public String getValStr();

	public ValidataBean getVdbean();

	public void setVdbean(ValidataBean vdbean);

	public void setReadOnly(boolean isro);

	public void reValue();
	
	public void setOnFocusChangeListener(OnFocusChangeListener ofcl);
	
	public void addTextChangedListener(TextWatcher twa);
	
	public void setOnClickListener(OnClickListener ocl);
}
