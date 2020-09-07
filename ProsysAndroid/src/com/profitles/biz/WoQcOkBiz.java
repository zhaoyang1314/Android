package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class WoQcOkBiz extends BaseBiz {
	
	//扫码
	public WebResponse woQcOkScan(String domain,String site,String scan,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan).addParam("userId", userId)
				.execRtn("getWoQcOkScan");
		return wrp;
	}

	//提交数据
	public WebResponse woQcOkSub(String domain,String site,String scan,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("site", site)
									.addParam("userId", userId)
									.addParam("scan", scan)
									.execRtn("woQcOkSub");	
		return wrp;
	}
 
}
