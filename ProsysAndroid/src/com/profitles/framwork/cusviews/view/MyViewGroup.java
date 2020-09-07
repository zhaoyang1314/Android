package com.profitles.framwork.cusviews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {
	public MyViewGroup(Context context) {
		 super(context);
		 }

	public MyViewGroup(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
	    super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    measureChildren(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	    int witth = getWidth();
	    int low = 0;
	    int diswith = 18;//定义行间距
	    for (int i = 0; i < getChildCount(); i++) {
	        View child = getChildAt(i);
	        int widt = child.getMeasuredWidth();
	        int height = child.getMeasuredHeight();
	        if (diswith + widt > witth) {
	            low++;
	            diswith = 18;
	        }
	        child.layout(diswith+20, low * height+50, diswith + widt, height * (low + 1));
	        diswith += widt;
	    }
	}


}