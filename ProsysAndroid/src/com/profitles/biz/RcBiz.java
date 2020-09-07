package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class RcBiz extends BaseBiz {

	//加载页面根据用户查找是否存在未收完记录
	public WebResponse RcLoad(String domain,String site,String userId) {
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain)
				.addParam("site", site)
				.addParam("userId", userId)
				.execRtn("RcLoadByUser");
		return wrp;
	}
	
	//扫码
	public WebResponse RcCode(String domain,String site,String code,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("code", code).addParam("userId", userId)
				.execRtn("checkRcCode");
		return wrp;
	}

	//重复扫码删除
	public WebResponse RcCode_Dele(String domain,String site,String code,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("code", code).addParam("userId", userId)
				.execRtn("RcCode_Dele");
		return wrp;
	}	

	//保存数据
	public WebResponse RcSave(String domain, String site, String code, String boxQty,String scat, String sQty, 
			String loc,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.addParam("boxQty", boxQty).addParam("scat", scat).addParam("sQty", sQty).addParam("loc", loc)
				.addParam("userId", userId)
				.execRtn("RcSave");
		return wrp;
	}
	
	//提交数据
	public WebResponse RcSub(String domain, String site, String userId,String fnceffdate) {
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain)
				.addParam("site", site)
				.addParam("userId", userId)
				.addParam("fnceffdate", fnceffdate)
				.execRtn("RcSub");
		return wrp;
	}

}
