package com.profitles.framwork.cusviews.view;

import android.R;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.profitles.framwork.cusviews.view.css.MainCss;
import com.profitles.framwork.util.Constants;

public class MyTextView extends TextView{
	
	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		MainCss.setcss(this);
//		init(context, attrs);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.textViewStyle);
//		init(context, attrs);
	}

	public MyTextView(Context context) {
		this(context, null);
//		init(context, null);
	}

	public String getValStr() {
		return getText().toString();
	}

	private void init(Context context, AttributeSet attrs){
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
