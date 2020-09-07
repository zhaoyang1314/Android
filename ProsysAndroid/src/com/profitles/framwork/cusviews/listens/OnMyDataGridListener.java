package com.profitles.framwork.cusviews.listens;

import java.util.Map;

import android.view.View;

import com.profitles.framwork.cusviews.listens.base.BaseListener;

public interface OnMyDataGridListener extends BaseListener{

	/**
	 * 单击单元格事件，返回False则不执行 onRowClick
	 * @param view	当前Item对象
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 */
	public void onItemClick(View view, Object val, int rowIndex, int colIndex, Map<String, Object> rowData);
	
	
	/**
	 * 长安单元格事件
	 * @param view
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 */
	public boolean onItemLongClick(View view, Object val, int rowIndex, int colIndex, Map<String, Object> rowData);
}
