package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class BarLdBiz extends BaseBiz {

	public WebResponse getBarLdInfo(String domain, String site, String bar,String userid) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("bar", bar)
				.addParam("userid", userid).execRtn("getScanInfo");
		return wrp;
	}
	/**标签Block功能方法*/
	public WebResponse blockScan(String domain, String site, String scan, String lot, String loc, String bin, String vend, 
			String qty, String part, String starus, String um, String userid, String scanstatus) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan)
				.addParam("lot", lot).addParam("loc", loc)
				.addParam("bin", bin).addParam("vend", vend)
				.addParam("qty", qty).addParam("part", part)
				.addParam("starus", starus).addParam("um", um)
				.addParam("userid", userid).addParam("scanstatus", scanstatus)
				.execRtn("blockScan");
		return wrp;
	}
	/*public WebResponse getScanBin(String domain, String site, String  bin,
			String part, String lot, String scan,
			String vend, String userid) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("bin", bin)
				.addParam("part", part).addParam("lot", lot)
				.addParam("scan", scan)
				.addParam("vend", vend)
				.addParam("userid", userid)
				.execRtn("getScanBin");
		return wrp;
	}*/
}
