package com.profitles.framwork.cusviews.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.profitles.activity.CaptureActivity;
import com.profitles.activity.R;
import com.profitles.framwork.activity.base.BaseActivity;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.bean.ValidataBean;
import com.profitles.framwork.cusviews.view.interfaces.IMyBaseView;
import com.profitles.framwork.cusviews.view.interfaces.IMyEditText;

public class MyReadBQ extends MyLinearLayout implements IMyBaseView, IMyEditText{
	
	private MyEditText edtBQInfo;
	private ImageButton btnReadBQ;
	
	public MyReadBQ(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyReadBQ(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyReadBQ(Context context) {
		this(context, null);
	}

	protected void init(Context context, AttributeSet attrs){
		super.init(context, attrs);
		setWillNotDraw(false);
		setOrientation(LinearLayout.HORIZONTAL);

		edtBQInfo = new MyEditText(context, attrs);
		btnReadBQ = new ImageButton(context, attrs);
		btnReadBQ.setScaleType(ScaleType.FIT_CENTER);
		btnReadBQ.setImageResource(R.drawable.sys_rdbq_bg2);
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		btnReadBQ.setPadding(0, 0, 0, 0);
		btnReadBQ.setLayoutParams(lParams);
		btnReadBQ.setBackgroundColor(Color.TRANSPARENT);
		btnReadBQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edtBQInfo.requestFocus();
				Activity activity = null;
				if(getContext() instanceof Activity){
					activity = (Activity)getContext();
				}else if(getContext() instanceof BaseActivity){
					activity = (Activity)getContext();
				}else if(getContext() instanceof ContextWrapper){
					activity = (Activity)((ContextWrapper) getContext()).getBaseContext();
				}
				if(activity instanceof BaseActivity)
					((BaseActivity)activity).addReadBQItem(MyReadBQ.this);
				ApplicationDB._qbReqParamTemp = activity;
				Intent intent = new Intent(activity, CaptureActivity.class); 
				activity.startActivityForResult(intent, getId());
			}
		});
		
		addViews(edtBQInfo, btnReadBQ);
	}
	
	public String getValStr() {
		return fmtValStr(edtBQInfo.getValStr());
	}
	
	/**
	 * 去掉特殊字符
	 * @return
	 */
	private String fmtValStr(String tag){
		if(tag != null){
			StringBuffer sbf = new StringBuffer();
			for(int i = 0; i < tag.length(); i++){
				int vl = tag.charAt(i);
				if(!(vl > 0 && vl <= 31)){
					sbf.append(tag.charAt(i));
				}
			}
			tag = sbf.toString();
		}
		return tag;
	}
	
	public void setText(String val) {
		edtBQInfo.setText(val);
	}

	public ValidataBean getVdbean() {
		return edtBQInfo.getVdbean();
	}

	public void setVdbean(ValidataBean vdbean) {
		edtBQInfo.setVdbean(vdbean);
	}
	
	public void setReadOnly(boolean isro){
		edtBQInfo.setReadOnly(isro);
		btnReadBQ.setEnabled(!isro);
	}
	
	public void reValue(){
		edtBQInfo.reValue();
	}
	
	public void setOnFocusChangeListener(OnFocusChangeListener ofcl){
		edtBQInfo.setOnFocusChangeListener(ofcl);
	}

	public void addTextChangedListener(TextWatcher twa){
		edtBQInfo.addTextChangedListener(twa);
	}
	
	@Override
	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		return edtBQInfo.requestFocus();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getMeasuredWidth();
		edtBQInfo.setWidth(width - btnReadBQ.getHeight());
	}
}
