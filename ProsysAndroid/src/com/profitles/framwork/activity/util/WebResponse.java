package com.profitles.framwork.activity.util;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.params.ParamsBaseMgr;
import com.profitles.framwork.util.StringUtil;

public class WebResponse extends ParamsBaseMgr {
	
	private ParamsBaseMgr wdata;
	public static final String Failed_Key = "Failed_Key";

	public WebResponse(Map<String, Object> data){
		super(data);
		if(data != null && data.get(RtnJsonKey.Data.toString()) instanceof Map){
			wdata = new ParamsBaseMgr((Map<String, Object>)data.get(RtnJsonKey.Data.toString()));
		}
	}
	
	public WebResponse(Map<String, Object> data, RtnJsonKey state){
		this(data);
		addParams(RtnJsonKey.Statue.toString(), state);
	}
	
	public boolean isSuccess(){
		return data != null &&
				RtnJsonKey.Success.toString().equals(getString(RtnJsonKey.Statue.toString(), ""));
	}
	
	public String getMessages(){
		String nbr = getErrNbr();
		return (StringUtil.isEmpty(nbr) ? "" : (nbr + ":") ) + getString(RtnJsonKey.Message.toString(), "");
	}
	
	/**
	 * 获取错误编码，没有则返回 Null
	 * @return
	 */
	public String getErrNbr(){
		return getString(Failed_Key, "");
	}
	
	public Object getWData(){
		return getObject(RtnJsonKey.Data.toString(), null);
	}
	
	public List<Map<String, Object>> getDataToList(){
		Object data = getWData();
		return (List<Map<String, Object>>)data;
	}
	public Map<String,List<Map<String, Object>>> getDataToMapList(){
		Object data = getWData();
		return (Map<String,List<Map<String, Object>>>)data;
	}
	
	public Map<String, Object> getDataToMap(){
		Object data = getWData();
		return (Map<String, Object>)data;
	}

	public ParamsBaseMgr getWDParamsMgr() {
		return wdata;
	}

	public enum RtnJsonKey{
		Statue, Success, Faild, Message, Data  
	}
}
