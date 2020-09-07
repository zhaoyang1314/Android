package com.profitles.framwork.cusviews.view;

import java.util.Calendar;
import java.util.Date;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.profitles.framwork.cusviews.view.css.MainCss;
import com.profitles.framwork.util.StringUtil;

public class MyDateView extends MyEditText {

	private boolean isDefToday;
	private String attShowTitle;
	
	public MyDateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		MainCss.setcss(this);
		init(context, attrs);
	}

	public MyDateView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.editTextStyle);
	}

	public MyDateView(Context context) {
		this(context, null);
	}
	
	private void init(final Context context, AttributeSet attrs){
		if(getInputType() == InputType.TYPE_NULL);
			setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);

		TypedArray t = context.obtainStyledAttributes(attrs,com.profitles.activity.R.styleable.MyDateView);
		setDefToday(t.getBoolean(com.profitles.activity.R.styleable.MyDateView_isDefToday, false));
		setAttShowTitle(t.getString(com.profitles.activity.R.styleable.MyDateView_attShowTitle));
		t.recycle();
		
		if(isDefToday()){
			setText(StringUtil.parseDate(new Date(), "yyy-MM-dd"));
		}
		
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {  
	                AlertDialog.Builder builder = new AlertDialog.Builder(context);  
	                View view = View.inflate(context, com.profitles.activity.R.layout.sys_date_time_dialog, null);  
	                final DatePicker datePicker = (DatePicker) view.findViewById(com.profitles.activity.R.id.sys_date_picker);  
	                TextView txvTimePicker = (TextView) view.findViewById(com.profitles.activity.R.id.sys_time_picker_txv);
	                final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(com.profitles.activity.R.id.sys_time_picker);    
	                builder.setView(view);
	      
	                
	                Calendar cal = Calendar.getInstance();  
	                cal.setTimeInMillis(System.currentTimeMillis());  
	                datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);  

	                if(getInputType() == InputType.TYPE_DATETIME_VARIATION_TIME){
	                	txvTimePicker.setVisibility(View.VISIBLE);
	                	timePicker.setVisibility(View.VISIBLE);
		                timePicker.setIs24HourView(true);  
		                timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));  
		                timePicker.setCurrentMinute(Calendar.MINUTE);  
	                }
	      
	                if(!StringUtil.isEmpty(getAttShowTitle())){
	                	builder.setTitle(getAttShowTitle());
	                }
                    builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {  
      
                        @Override  
                        public void onClick(DialogInterface dialog, int which) {  
      
                            StringBuffer sb = new StringBuffer();  
                            sb.append(String.format("%d-%02d-%02d",   
                                    datePicker.getYear(),   
                                    datePicker.getMonth() + 1,  
                                    datePicker.getDayOfMonth()));  
                            sb.append("  ");  
							if (getInputType() == InputType.TYPE_DATETIME_VARIATION_TIME) {
								sb.append(timePicker.getCurrentHour()).append(":")
										.append(timePicker.getCurrentMinute());
							}
                            setText(sb);  
                            requestFocus();  
                            dialog.cancel();  
                        }  
                    });  
	                  
	                Dialog dialog = builder.create();  
	                dialog.show();  
                }  
	            return true;  
			}
		});
	}

	public boolean isDefToday() {
		return isDefToday;
	}

	public void setDefToday(boolean isDefToday) {
		this.isDefToday = isDefToday;
	}

	public String getAttShowTitle() {
		return attShowTitle;
	}

	public void setAttShowTitle(String attShowTitle) {
		this.attShowTitle = attShowTitle;
	}
}
