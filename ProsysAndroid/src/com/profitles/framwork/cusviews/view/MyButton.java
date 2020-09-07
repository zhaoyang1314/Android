package com.profitles.framwork.cusviews.view;

import android.R;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.Button;

import com.profitles.framwork.cusviews.view.css.MainCss;
import com.profitles.framwork.util.Constants;

public class MyButton extends Button {

	public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		MainCss.setcss(this);
		init(context, attrs);
	}

	public MyButton(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.buttonStyle);
		init(context, attrs);
	}

	public MyButton(Context context) {
		this(context, null);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs){
		
		this.setPadding(10, 0, 10, 0);
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
}
