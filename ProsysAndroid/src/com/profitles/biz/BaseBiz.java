package com.profitles.biz;

import com.profitles.biz.interfaces.IBaseBiz;
import com.profitles.framwork.webservice.WebConnUtil;
import com.profitles.framwork.webservice.interfaces.IWebConnUtil;

public class BaseBiz implements IBaseBiz{

	private IWebConnUtil wcu;

	private IWebConnUtil getWcu() {
		wcu = wcu == null ? new WebConnUtil() : wcu;
		return wcu;
	}
	
	public IWebConnUtil getWcuCP() {
		return getWcu().clearParams();
	}
}
