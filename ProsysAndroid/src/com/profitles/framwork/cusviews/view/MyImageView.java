package com.profitles.framwork.cusviews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyImageView(Context context) {
		this(context, null);
	}

}
