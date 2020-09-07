package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;


public class LastWgBiz extends BaseBiz {

	// 扫描加工单标签
	public WebResponse LastWgLastBar(String domain, String site, String scan,String userid) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan)
				.addParam("userid", userid).execRtn("LastWgLastBar");
		return wrp;
	}
	
	// 扫描标签
	public WebResponse LastWgScan(String domain, String site,String scan,String part) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan)
				.addParam("part", part).execRtn("LastWgScan");
		return wrp;
	}
	
	// 提交
	public WebResponse LastWgsubmit(String domain, String site, String userid,String ukey,String scanbox) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("ukey", ukey).addParam("scanbox", scanbox)
				.execRtn("LastWgsubmit");
		return wrp;
	}
	
}
