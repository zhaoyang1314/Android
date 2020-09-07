package com.profitles.framwork.cusviews.view;

import com.profitles.framwork.cusviews.view.css.MainCss;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class MyLinearLayout extends LinearLayout {
	
	public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyLinearLayout(Context context) {
		this(context, null);
	}
	
	protected void init(Context context, AttributeSet attrs){
		MainCss.setcss(this);
	}

	public MyLinearLayout addViews(View... views){
		for (int i = 0; i < views.length; i++) {
			this.addView(views[i]);	
		}
		return this;
	}
}
