package com.profitles.framwork.params;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class ParamsBaseMgr {

	protected Map<String, Object> data;
	
	public ParamsBaseMgr(Map<String, Object> _data){
		data = _data == null ? new HashMap<String, Object>() : _data;
	}

	public void addParams(String key, Object value){
		data = data == null ? new HashMap<String, Object>() : data;
		data.put(key, value);
	}
	
	public int getInt(String key, int def){
		Object o = data.get(key);
		if(o == null) return def;
		return StringUtil.parseInt(o);
	}
	
	public float getFloat(String key, float def){
		Object o = data.get(key);
		if(o == null) return def;
		return StringUtil.parseFloat(o);
	}
	
	public Date getDate(String key, Date def){
		Object o = data.get(key);
		if(o == null) return def;
		return StringUtil.parseDate(o.toString(), Constants.DATE_FORMAT);
	}
	
	public String getString(String key, String def){
		Object o = data.get(key);
		if(o == null) return def;
		return o.toString();
	}
	
	public boolean getBoolean(String key, boolean def){
		Object o = data.get(key);
		if(o == null) return def;
		return StringUtil.parseBoolean(o);
	}
	
	public Object getObject(String key, Object def){
		Object o = data.get(key);
		if(o == null) return def;
		return o;
	}

	public String[] getArrayStr(String key, String splTag) {
		String str = getString(key, "");
		if(!StringUtil.isEmpty(str)){
			return str.split(splTag);
		}
		return null;
	}
}
