package com.profitles.framwork.webservice.interfaces;

import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.webservice.WebConnUtil;

public interface IWebConnUtil {
	
	public WebResponse execRtn(String methodName);
	
	public WebResponse execRtn(String methodName, Map<String, String> params);
	
//	public String execRtnStr(String methodName);
//
//	public abstract Map<String, Object> execRtnItem(String methodName);
//
//	public abstract Map<String, Object> execRtnItem(String methodName,Map<String, String> params);
//
//	public abstract List<Map<String, Object>> execRtnList(String methodName);
//
//	public abstract List<Map<String, Object>> execRtnList(String methodName,
//			Map<String, String> params);
	
	public WebConnUtil addParam(String key, String value);
	
	public WebConnUtil addParam(Map<String, String> _params);
	
	public WebConnUtil clearParams();
}