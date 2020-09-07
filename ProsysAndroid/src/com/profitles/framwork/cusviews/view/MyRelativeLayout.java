package com.profitles.framwork.cusviews.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.profitles.framwork.cusviews.view.css.MainCss;

@SuppressLint("NewApi")
public class MyRelativeLayout extends RelativeLayout {
	
	public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public MyRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyRelativeLayout(Context context) {
		this(context, null);
	}
	
	protected void init(Context context, AttributeSet attrs){
		MainCss.setcss(this);
	}

	public MyRelativeLayout addViews(View... views){
		for (int i = 0; i < views.length; i++) {
			this.addView(views[i]);	
		}
		return this;
	}
}
