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
public class TableAdapter extends BaseAdapter {
	
	private List<Good> list;
	private LayoutInflater inflater;
	
	public TableAdapter(Context context, List<Good> list){
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
		
		Good good = (Good) this.getItem(position);
		
		ViewHolder viewHolder;
		
		if(convertView == null){
			
			viewHolder = new ViewHolder();
			
			convertView = inflater.inflate(R.layout.act_self_check_item, null);
			viewHolder.qat_check_id = (TextView) convertView.findViewById(R.id.text_check_id);
			viewHolder.qat_check_date = (TextView) convertView.findViewById(R.id.text_check_date);
			viewHolder.qat_check_date_time = (TextView) convertView.findViewById(R.id.text_date_check_time);
			viewHolder.qat_check_shite = (TextView) convertView.findViewById(R.id.text_check_shift);
			viewHolder.qat_check_user = (TextView) convertView.findViewById(R.id.text_check_user);
			viewHolder.qat_check_part = (TextView) convertView.findViewById(R.id.text_check_part);
			viewHolder.qat_check_cust_part = (TextView) convertView.findViewById(R.id.text_check_cust_part);
			viewHolder.qat_check_version = (TextView) convertView.findViewById(R.id.text_check_version);
			viewHolder.qat_check_part_desc = (TextView) convertView.findViewById(R.id.text_check_part_desc);
			viewHolder.qat_check_type = (TextView) convertView.findViewById(R.id.text_check_type);
			viewHolder.qat_check_out_slt = (TextView) convertView.findViewById(R.id.text_check_out_slt);
			viewHolder.qat_check_size_slt = (TextView) convertView.findViewById(R.id.text_check_size_slt);
			viewHolder.qat_check_jdg_slt = (TextView) convertView.findViewById(R.id.text_check_jdg_slt);
			viewHolder.qat_check_slt = (TextView) convertView.findViewById(R.id.text_check_slt);
			viewHolder.qat_check_chk_time = (TextView) convertView.findViewById(R.id.text_check_chk_time);
			viewHolder.qat_check_csp_user = (TextView) convertView.findViewById(R.id.text_check_csp_user);


			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		LayoutParams layoutParams = viewHolder.qat_check_id.getLayoutParams();
		layoutParams.height=100;
		layoutParams.width=47;
		viewHolder.qat_check_id.setLayoutParams(layoutParams);
		viewHolder.qat_check_id.setText(good.getId());
		viewHolder.qat_check_id.setTextSize(13);
		viewHolder.qat_check_date.setText(good.getQat_date());
		viewHolder.qat_check_date.setTextSize(13);
		viewHolder.qat_check_date_time.setText(good.getQat_date_time());
		viewHolder.qat_check_date_time.setTextSize(13);
		viewHolder.qat_check_shite.setText(good.getQat_shift());
		viewHolder.qat_check_shite.setTextSize(13);
		viewHolder.qat_check_user.setText(good.getQat_user());
		viewHolder.qat_check_user.setTextSize(13);
		viewHolder.qat_check_part.setText(good.getQat_part());
		viewHolder.qat_check_part.setTextSize(13);
		viewHolder.qat_check_cust_part.setText(good.getQat_cust_part());
		viewHolder.qat_check_cust_part.setTextSize(13);
		viewHolder.qat_check_version.setText(good.getQat_rev());
		viewHolder.qat_check_version.setTextSize(13);
		viewHolder.qat_check_part_desc.setText(good.getQat_part_desc());
		viewHolder.qat_check_part_desc.setTextSize(13);
		viewHolder.qat_check_type.setText(good.getQat_type());
		viewHolder.qat_check_type.setTextSize(13);
		viewHolder.qat_check_out_slt.setText(good.getQat_out_slt());
		viewHolder.qat_check_out_slt.setTextSize(13);
		viewHolder.qat_check_size_slt.setText(good.getQat_size_slt());
		viewHolder.qat_check_size_slt.setTextSize(13);
		viewHolder.qat_check_jdg_slt.setText(good.getQat_jdg_slt());
		viewHolder.qat_check_jdg_slt.setTextSize(13);
		viewHolder.qat_check_slt.setText(good.getQat_slt());
		viewHolder.qat_check_slt.setTextSize(13);
		viewHolder.qat_check_chk_time.setText(good.getQat_chk_time());
		viewHolder.qat_check_chk_time.setTextSize(13);
		viewHolder.qat_check_csp_user.setText(good.getQat_csp_user());
		viewHolder.qat_check_csp_user.setTextSize(13);
		
		return convertView;
	}
	
	public static class ViewHolder{
		public TextView qat_check_id;
		public TextView qat_check_date;
		public TextView qat_check_date_time;
		public TextView qat_check_shite;
		public TextView qat_check_user;
		public TextView qat_check_part;
		public TextView qat_check_cust_part;
		public TextView qat_check_version;
		public TextView qat_check_part_desc;
		public TextView qat_check_type;
		public TextView qat_check_out_slt;
		public TextView qat_check_size_slt;
		public TextView qat_check_jdg_slt;
		public TextView qat_check_slt;
		public TextView qat_check_chk_time;
		public TextView qat_check_csp_user;
	}
	
}