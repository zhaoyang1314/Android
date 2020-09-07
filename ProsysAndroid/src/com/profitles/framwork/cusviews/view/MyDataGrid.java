package com.profitles.framwork.cusviews.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.profitles.activity.R;
import com.profitles.framwork.activity.base.BaseActivity;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class MyDataGrid extends MyLinearLayout {
	
	private Map<String, Object> head;
	private List<String> widths;
	private List<Map<String, Object>> data;
	private Map<String, Map<String, Object>> fdata;
	private int selRowIndex, selColIndex, rowHeight, btnHeight , currPage=1;
	private final static String HideKey = "HIDE";
	private OnMyDataGridListener onMyDataGridListener;
	private boolean isShowSeq, isBlooean = true;

	public MyDataGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initSub(context, attrs);
	}

	public MyDataGrid(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyDataGrid(Context context) {
		this(context, null);
	}
	
	private void initSub(Context context, AttributeSet attrs){
		setOrientation(LinearLayout.VERTICAL);
		data = new ArrayList<Map<String,Object>>();
		fdata = new HashMap<String, Map<String,Object>>();
		
		TypedArray t = getContext().obtainStyledAttributes(attrs,R.styleable.MyDataGrid);
		String colKey = t.getString(R.styleable.MyDataGrid_attColKeys);
		String colName = t.getString(R.styleable.MyDataGrid_attColNames);
		String colWidth = t.getString(R.styleable.MyDataGrid_attColWidths);
		rowHeight = t.getInt(R.styleable.MyDataGrid_attRowHeight, 60);
		btnHeight = (int)(rowHeight * 0.8);
		isShowSeq = t.getBoolean(R.styleable.MyDataGrid_isShowSeq, true);
		if(!StringUtil.isEmpty(colKey)){
			String[] colKeys = colKey.split(",");
			String[] colNames = colName.split(",");
			for(int i = 0; i < colKeys.length; i++){
				addHeadItem(colKeys[i], colNames.length <= i ? HideKey : colNames[i]);
			}
		}
		if(!StringUtil.isEmpty(colWidth)){
			widths = new ArrayList<String>(Arrays.asList(colWidth.split(",")));
		}
		t.recycle();
	}
	
	public MyDataGrid addHeadItem(String key, String name){
		head = head == null ? new LinkedHashMap<String, Object>() : head;
		head.put(key, name);
		return this;
	}
	
	public void buildData(List<Map<String, Object>> _data){
		currPage = 1;
		data = _data;
//		buildDataPage(_data);
		gotPageSize(currPage, true);
	}
	
	private void buildDataPage(List<Map<String, Object>> _data){
		if(hsv != null) hsv.removeAllViews();
		if(isShowSeq && hsv == null){
			widths.add(0, "80");
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("SEQ", "序");
			map.putAll(head);
			head = map;
		}
		//Build Head
		_data = _data == null ? new ArrayList<Map<String,Object>>() : _data;
		_data.add(0, head);
		//Build Data
		
		drawDGrid(_data);
	}
	
	public void clearData(){
		List<Map<String, Object>> _data = new ArrayList<Map<String,Object>>();
		_data.add(head);
		hsv.removeAllViews();
		System.gc();
		drawDGrid(_data);
	}
	private HorizontalScrollView hsv;
	private ScrollView ssv;
	
	private void drawDGrid(List<Map<String, Object>> _data){
		if(hsv == null){
		}
		hsv = new HorizontalScrollView(getContext());
		hsv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		if(ssv == null){
		}
		ssv = new ScrollView(getContext());
		ssv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));

		MyLinearLayout myOutBdy = new MyLinearLayout(getContext());
		myOutBdy.setLayoutParams(new LayoutParams(getWidth(), LayoutParams.FILL_PARENT));
		myOutBdy.setOrientation(LinearLayout.VERTICAL);
		
		MyLinearLayout head = createRow(_data.get(0), 0);
		MyLinearLayout myllBody = new MyLinearLayout(getContext());
		myllBody.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		myllBody.setOrientation(LinearLayout.VERTICAL);

		for(int i = 1; i < _data.size(); i++){
			try {
				if(isShowSeq && i > 0) _data.get(i).put("SEQ", i + ((currPage-1)*Constants.DataGridMX));
				myllBody.addView(createRow(_data.get(i), i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		myllBody.addView(createEmpryRow(_data.size()));

		if(hsv.getParent() == null){
			ssv.addView(myllBody);
			myOutBdy.addView(head);
			myOutBdy.addView(ssv);
			hsv.addView(myOutBdy);
			if(data != null && data.size() > Constants.DataGridMX && isBlooean) addView(createPS());
			addView(hsv);
		}
	}
	
	private MyLinearLayout createPS(){
		final int maxInt = (int)Math.ceil(data.size()/StringUtil.parseFloat(Constants.DataGridMX));
		MyLinearLayout crtLy = new MyLinearLayout(getContext());
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, btnHeight);
		crtLy.setLayoutParams(lParams);
		crtLy.setOrientation(LinearLayout.HORIZONTAL);

		final MyTextView edtPage = new MyTextView(getContext());
		edtPage.setText(currPage+"");
		edtPage.setHeight(btnHeight);
		edtPage.setPadding(0, 0, 0, 0);
		MyButton btnBack = new MyButton(getContext());
		btnBack.setText("上一页");
		btnBack.setPadding(12, 4, 12, 4);
		btnBack.setHeight(btnHeight);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if(currPage > 1){
					--currPage;
					edtPage.setText(currPage+"");
					gotPageSize(currPage, true);
				}else{
					Toast.makeText(getContext(), "已是第一页！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		MyButton btnNext = new MyButton(getContext());
		btnNext.setText("下一页");
		btnNext.setPadding(12, 4, 12, 4);
		btnNext.setHeight(btnHeight);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if(maxInt > currPage){
					++currPage;
					edtPage.setText(currPage+"");
					gotPageSize(currPage, true);
				}else{
					Toast.makeText(getContext(), "已是最后一页！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		MyTextView txtView = new MyTextView(getContext());
		txtView.setText(" 共: " + maxInt +" 页");
		crtLy.addViews(btnBack, edtPage, btnNext, txtView);
		isBlooean = false;
		return crtLy;
	}
	
	private void gotPageSize(int pageSize, boolean isClear){
//		if(data != null && data.size() > Constants.DataGridMX * (pageSize - 1)){
//			buildData(data.subList(Constants.DataGridMX * (pageSize - 1), Constants.DataGridMX * (pageSize)-1));
		int ps = Constants.DataGridMX * (pageSize);//-1;
		if(data == null){
			buildDataPage(null);
		}else{
			List<Map<String, Object>> _data = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> _data2 = (data.subList(Constants.DataGridMX * (pageSize - 1), 
					data.size() < ps ? data.size() : ps));
			for(int i = 0; i < _data2.size(); i++){
				_data.add(_data2.get(i));
			}
			buildDataPage(_data);
		}
//		}
	}
	
	private MyLinearLayout createRow(Map<String, Object> rowData, final int rowIndex){
		final MyLinearLayout llyRow = new MyLinearLayout(getContext());
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, rowHeight);
		llyRow.setLayoutParams(lParams);
		llyRow.setOrientation(LinearLayout.HORIZONTAL);
		llyRow.setBackgroundColor(rowIndex == 0  ? Color.TRANSPARENT : 
			(rowIndex%2 == 0 ? Color.TRANSPARENT: Color.parseColor(Constants.CHECK_ROW_COLOR)) );
		int i = 0;
		for(Iterator<String> it = head.keySet().iterator(); it.hasNext(); i++){
			String key = it.next();
			llyRow.addView(createCol(rowData.get(key), rowIndex, i, rowData));
		}
		return llyRow;
	}
	
	private MyLinearLayout createEmpryRow(int rowIndex){
		MyLinearLayout llyRow = new MyLinearLayout(getContext());
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, rowHeight*4);
		llyRow.setLayoutParams(lParams);
		llyRow.setOrientation(LinearLayout.HORIZONTAL);
		int i = 0;
		for(Iterator<String> it = head.keySet().iterator(); it.hasNext(); it.next()){
			llyRow.addView(createCol("-", rowIndex, i, new HashMap<String, Object>()));
		}
		return llyRow;
	}
	
	private MyLinearLayout createCol(final Object val, final int rowIndex, final int colIndex, final Map<String, Object> rowData){
		final MyLinearLayout llyCol = new MyLinearLayout(getContext());
		int width = widths.size() > colIndex ? StringUtil.parseInt(widths.get(colIndex)) : 0;
		LayoutParams lParams = new LayoutParams(width, rowHeight);
		if(width == 0) lParams.weight = 1;
		llyCol.setLayoutParams(lParams);
		
		MyTextView txvItemText = new MyTextView(getContext());
		txvItemText.setText(val == null ? "" : val.toString());
		txvItemText.setGravity(Gravity.CENTER);
		txvItemText.setWidth(width);
		
		MyTextView txvItemRowIndex = new MyTextView(getContext());
		txvItemRowIndex.setText(rowIndex+"");
		txvItemRowIndex.setVisibility(View.GONE);
		
		MyTextView txvItemColIndex = new MyTextView(getContext());
		txvItemColIndex.setText(colIndex+"");
		txvItemRowIndex.setVisibility(View.GONE);
		
		llyCol.addViews(txvItemText, txvItemRowIndex, txvItemColIndex);
		llyCol.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onMyDataGridListener != null){
					onMyDataGridListener.onItemClick(llyCol, val, rowIndex, colIndex, rowData);
				}
			}
		});
		llyCol.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(onMyDataGridListener != null){
					return onMyDataGridListener.onItemLongClick(llyCol, val, rowIndex, colIndex, rowData);
				}
				return true;
			}
		});
		setItemDataByRC(rowIndex, colIndex, val);
		return llyCol;
	}
	
	public Object getItemDataByRC(int rowIndex, int colIndex){
		if(!fdata.containsKey(rowIndex+"")) return null;
		return fdata.get(rowIndex+"").get(colIndex+"");
	}
	
	public void setItemDataByRC(int rowIndex, int colIndex, Object val){
		if(!fdata.containsKey(rowIndex+"")){
			fdata.put(rowIndex+"", new HashMap<String, Object>());
		}
		fdata.get(rowIndex+"").put(colIndex+"", val);
	}
	
	public Object getSelValue(){
		return getItemDataByRC(selRowIndex, selColIndex);
	}

	public int getSelRowIndex() {
		return selRowIndex;
	}

	public int getSelColIndex() {
		return selColIndex;
	}

	public void setOnMyDataGridListener(OnMyDataGridListener onMyDataGridListener) {
		this.onMyDataGridListener = onMyDataGridListener;
	}
	
	public Map<String, Object> getRowDataByIndex(int rowIndex){
		return fdata.get(rowIndex+"");
	}
	
	public Map<String, Object> getRowDataByKey(int rowIndex){
		return data.get(rowIndex);
	}
}
