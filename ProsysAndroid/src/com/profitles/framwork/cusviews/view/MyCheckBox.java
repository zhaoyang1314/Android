package com.profitles.framwork.cusviews.view;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.profitles.framwork.cusviews.view.css.MainCss;

public class MyCheckBox extends CheckBox {
	
	private CheckedType ckdType = CheckedType.Check_Type;
	private String valueKey;

	public MyCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		MainCss.setcss(this);
		init(context, attrs);
	}

	public MyCheckBox(Context context, AttributeSet attrs) {
		this(context, null, R.attr.checkboxStyle);
	}
	
	public MyCheckBox(Context context, String text){
		this(context, null, R.attr.checkboxStyle);
		this.setText(text);
	}

	public MyCheckBox(Context context) {
		this(context, "");
	}

	private void init(Context context){
	}
	
	private void init(Context context, AttributeSet attrs){
		init(context);
	}
	
	/**
	 * 返回一个CheckBox组
	 * @param context
	 * @param texts
	 */
	public static MyCheckBox[] getCheckBoxs(Context context, String[] texts, String[] keys, int width, int height){
		MyCheckBox [] mckbs = null;
		if(texts != null){
			mckbs = new MyCheckBox[texts.length];
			for (int i = 0; i < texts.length; i++) {
				mckbs[i] = new MyCheckBox(context, texts[i]);
				mckbs[i].setValueKey(i < keys.length ? keys[i] : null);
				mckbs[i].setLayoutParams(new ViewGroup.LayoutParams(width, height));
			}
		}
		return mckbs;
	}
	
	public CheckedType getCkdType() {
		return ckdType;
	}

	public void setCkdType(CheckedType ckdType) {
		this.ckdType = ckdType;
	}

	public String getValueKey() {
		return valueKey;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	/**
	 * 选择模式，单选或多选
	 * @author james
	 */
	public enum CheckedType{
		Single_Type,
		Check_Type
	}
}
