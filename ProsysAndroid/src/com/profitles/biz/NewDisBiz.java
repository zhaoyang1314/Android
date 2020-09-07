package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;
/**
 * 系统新的拼拆箱的方法
 * */
public class NewDisBiz extends BaseBiz {
	
	// 父标签扫描
	public WebResponse DisCheckBar(String domain,String site,String parentCode) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("parentCode", parentCode)
				.execRtn("NewDisCheckBar");
		return wrp;
	}
	
	// 字标签扫描
	public WebResponse checkSonBar(String domain,String site,String sonCode){
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("sonCode", sonCode)
				.execRtn("NewDisCheckSonBar");
		return wrp;
	}
	
	// 提交方法
	public WebResponse submit(String domain,String site,String json, String parentCode){
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("json", json).addParam("parentCode",parentCode)
				.execRtn("submitDis");
		return wrp;
	}
}
