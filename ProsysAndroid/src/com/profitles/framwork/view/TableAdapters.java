package com.profitles.framwork.view;

import java.util.List;

import com.profitles.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
 
 
/**
 * @author zero
 *
 */
public class TableAdapters extends BaseAdapter {
	
	private List<Goods> list;
	private LayoutInflater inflater;
	
	public TableAdapters(Context context, List<Goods> list){
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
 

	@Override
	public int getCount() {
		int ret = 0;
		if(list!=null){
			ret = list.size();
		}
		return ret;
	}
 
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Goods goods = (Goods) this.getItem(position);
		
		ViewHolder viewHolder;
		
		if(convertView == null){
			
			viewHolder = new ViewHolder();
			
			convertView = inflater.inflate(R.layout.act_self_item, null);
			viewHolder.qat_id = (TextView) convertView.findViewById(R.id.text_id);
			viewHolder.qat_date = (TextView) convertView.findViewById(R.id.text_date);
			viewHolder.qat_date_time = (TextView) convertView.findViewById(R.id.text_date_time);
			viewHolder.qat_shite = (TextView) convertView.findViewById(R.id.text_shift);
			viewHolder.qat_user = (TextView) convertView.findViewById(R.id.text_user);
			viewHolder.qat_part = (TextView) convertView.findViewById(R.id.text_part);
			viewHolder.qat_cust_part = (TextView) convertView.findViewById(R.id.text_cust_part);
			viewHolder.qat_version = (TextView) convertView.findViewById(R.id.text_version);
			viewHolder.qat_part_desc = (TextView) convertView.findViewById(R.id.text_part_desc);
			viewHolder.qat_type = (TextView) convertView.findViewById(R.id.text_type);

			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		LayoutParams layoutParams = viewHolder.qat_id.getLayoutParams();
		layoutParams.height=100;
		layoutParams.width=47;
		viewHolder.qat_id.setLayoutParams(layoutParams);
		viewHolder.qat_id.setText(goods.getId());
		viewHolder.qat_id.setTextSize(13);
		viewHolder.qat_date.setText(goods.getQat_date());
		viewHolder.qat_date.setTextSize(13);
		viewHolder.qat_date_time.setText(goods.getQat_date_time());
		viewHolder.qat_date_time.setTextSize(13);
		viewHolder.qat_shite.setText(goods.getQat_shift());
		viewHolder.qat_shite.setTextSize(13);
		viewHolder.qat_user.setText(goods.getQat_user());
		viewHolder.qat_user.setTextSize(13);
		viewHolder.qat_part.setText(goods.getQat_part());
		viewHolder.qat_part.setTextSize(13);
		viewHolder.qat_cust_part.setText(goods.getQat_cust_part());
		viewHolder.qat_cust_part.setTextSize(13);
		viewHolder.qat_version.setText(goods.getQat_rev());
		viewHolder.qat_version.setTextSize(13);
		viewHolder.qat_part_desc.setText(goods.getQat_part_desc());
		viewHolder.qat_part_desc.setTextSize(13);
		viewHolder.qat_type.setText(goods.getQat_type());
		viewHolder.qat_type.setTextSize(13);
		
		return convertView;
	}
	
	public static class ViewHolder{
		public TextView qat_id;
		public TextView qat_date;
		public TextView qat_date_time;
		public TextView qat_shite;
		public TextView qat_user;
		public TextView qat_part;
		public TextView qat_cust_part;
		public TextView qat_version;
		public TextView qat_part_desc;
		public TextView qat_type;
	}
	
}