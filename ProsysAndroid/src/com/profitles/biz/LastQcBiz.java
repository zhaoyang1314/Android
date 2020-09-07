package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;


public class LastQcBiz extends BaseBiz {

	// 扫描加工单标签
	public WebResponse LastQcLastBar(String domain, String site, String scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan).execRtn("LastQcLastBar");
		return wrp;
	}
	
	// 扫描标签
	public WebResponse LastQcScan(String domain, String site,String scan,String part) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan)
				.addParam("part", part).execRtn("LastQcScan");
		return wrp;
	}
	
	// 提交
	public WebResponse LastQcsubmit(String domain, String site, String userid,String desc,String ukey) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("desc", desc).addParam("ukey", ukey)
				.execRtn("LastQcsubmit");
		return wrp;
	}
	
}
