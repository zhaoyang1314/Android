package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class BlockBiz extends BaseBiz {

	public WebResponse getScanInfo(String domain, String site, String bar,String userid) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("bar", bar)
				.addParam("userid", userid).execRtn("getScanInfob");
		return wrp;
	}
	
	/**标签Block功能方法*/
	public WebResponse updateState(String domain, String site, String bar,String userid) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("bar", bar)
				.addParam("userid", userid).execRtn("updateScanState");
		return wrp;
	}
	
	/**标签Block判定功能方法*/
	public WebResponse getScanPd(String domain, String site,String scan, String userid,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan)
				.addParam("userid", userid).addParam("fnceffdate", fnceffdate).execRtn("getScanPd");
		return wrp;
	}
	
	
}
