package com.profitles.framwork.cusviews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.profitles.activity.R;
import com.profitles.framwork.util.StringUtil;

public class MyTabHost extends TabHost {

	private String childId;
	private String childKey;
	private String childName;
	private int currentTab;

	public MyTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public MyTabHost(Context context) {
		this(context, null);
		
	}

	private void init(Context context, AttributeSet attrs){
		TypedArray t = getContext().obtainStyledAttributes(attrs,R.styleable.MyTabHost);
		childId = t.getString(R.styleable.MyTabHost_attChildId);
		childKey = t.getString(R.styleable.MyTabHost_attChildKey);
		childName = t.getString(R.styleable.MyTabHost_attChildName);
		currentTab = t.getInt(R.styleable.MyTabHost_currentTab, 0);
		t.recycle();
	}

	@Override
	public void setup() {
		super.setup();
		String[] childIds = null, childKeys = null, childNames = null;
		if(!StringUtil.isEmpty(childId)) childIds  = childId.split(",");
		if(!StringUtil.isEmpty(childKey)) childKeys  = childKey.split(",");
		if(!StringUtil.isEmpty(childName)) childNames  = childName.split(",");
		
		if(childIds == null || childKeys == null || childNames == null) return;
		for(int i = 0; i < childIds.length; i++){
			int layoutId = getResources().getIdentifier(childIds[i], "id", getContext().getPackageName());
			if(layoutId > 0){
				addTab(newTabSpec(childKeys[i]).setIndicator(childNames[i], null)
						.setContent(layoutId));
			}
		}
		setCurrentTab(currentTab);

		TabWidget tabWidget = getTabWidget();
		int count = tabWidget.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);
			view.getLayoutParams().height = tabWidget.getLayoutParams().height;
			final TextView tv = (TextView) view.findViewById(android.R.id.title);

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv
					.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0); // 取消文字底边对齐
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE); // 设置文字居中对齐
		}   
	}
}
