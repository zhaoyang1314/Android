package com.profitles.framwork.cusviews.view.validata;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.profitles.framwork.BaseObject;
import com.profitles.framwork.cusviews.view.bean.ValidataBean;

public class ViewValidata extends BaseObject {

	public static ValidataBean initValidata(Context context, AttributeSet attrs){
		ValidataBean vdbean = new ValidataBean();
		TypedArray t = context.obtainStyledAttributes(attrs,com.profitles.activity.R.styleable.EditAllView);
		vdbean.setRequired(t.getBoolean(com.profitles.activity.R.styleable.EditAllView_isRequired, false));
		vdbean.setReqAltMsg(t.getString(com.profitles.activity.R.styleable.EditAllView_reqAltMsg));
		vdbean.setRegValidata(t.getString(com.profitles.activity.R.styleable.EditAllView_regValidata));
		vdbean.setRegAltMsg(t.getString(com.profitles.activity.R.styleable.EditAllView_regAltMsg));
		vdbean.setReadOnly(t.getBoolean(com.profitles.activity.R.styleable.EditAllView_isReadOnly, false));
		t.recycle();
		
		TypedArray t2 = context.obtainStyledAttributes(attrs,com.profitles.activity.R.styleable.MyEditText);
		vdbean.setBlurRequired(t2.getBoolean(com.profitles.activity.R.styleable.MyEditText_isBlurRequired, false));
		vdbean.setBlreAltMsg(t2.getString(com.profitles.activity.R.styleable.MyEditText_blreAltMsg));
		t2.recycle();
		
		return vdbean;
	}
}
