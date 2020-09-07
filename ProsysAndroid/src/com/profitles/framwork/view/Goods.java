package com.profitles.framwork.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class Goods {
	private String id;
	private String qat_date;
	private String qat_date_time;
	private String qat_shift;
	private String qat_user;
	private String qat_part;
	private String qat_cust_part;
	private String qat_rev;
	private String qat_part_desc;
	private String qat_type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQat_date() {
		return qat_date;
	}
	public void setQat_date(String qat_date) {
		this.qat_date = qat_date;
	}
	public String getQat_date_time() {
		return qat_date_time;
	}
	public void setQat_date_time(String qat_date_time) {
		this.qat_date_time = qat_date_time;
	}
	public String getQat_shift() {
		return qat_shift;
	}
	public void setQat_shift(String qat_shift) {
		this.qat_shift = qat_shift;
	}
	public String getQat_user() {
		return qat_user;
	}
	public void setQat_user(String qat_user) {
		this.qat_user = qat_user;
	}
	public String getQat_part() {
		return qat_part;
	}
	public void setQat_part(String qat_part) {
		this.qat_part = qat_part;
	}
	public String getQat_cust_part() {
		return qat_cust_part;
	}
	public void setQat_cust_part(String qat_cust_part) {
		this.qat_cust_part = qat_cust_part;
	}
	public String getQat_rev() {
		return qat_rev;
	}
	public void setQat_rev(String qat_rev) {
		this.qat_rev = qat_rev;
	}
	public String getQat_part_desc() {
		return qat_part_desc;
	}
	public void setQat_part_desc(String qat_part_desc) {
		this.qat_part_desc = qat_part_desc;
	}
	public String getQat_type() {
		return qat_type;
	}
	public void setQat_type(String qat_type) {
		this.qat_type = qat_type;
	}
	public Goods(String id, String qat_date, String qat_date_time,
			String qat_shift, String qat_user, String qat_part,
			String qat_cust_part, String qat_rev, String qat_part_desc,
			String qat_type) {
		super();
		this.id = id;
		this.qat_date = qat_date;
		this.qat_date_time = qat_date_time;
		this.qat_shift = qat_shift;
		this.qat_user = qat_user;
		this.qat_part = qat_part;
		this.qat_cust_part = qat_cust_part;
		this.qat_rev = qat_rev;
		this.qat_part_desc = qat_part_desc;
		this.qat_type = qat_type;
	}
	public Goods() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}