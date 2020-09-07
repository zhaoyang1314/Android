package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class LapBiz extends BaseBiz {

	
	public WebResponse lapBar(String domain,String site ,String code) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.execRtn("getLapBar");
		return wr;
	}
	
	public WebResponse lapSubByBar(String domain,String site ,String code,String userId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code).addParam("userId", userId)
				.execRtn("getLapSubByBar");
		return wr;
	}
	
	public WebResponse lapSub(String domain,String site ,String part,String lot,String unit,String vend,String po,String poLine,String userId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part).addParam("lot", lot)
				.addParam("unit", unit).addParam("vend", vend).addParam("po", po).addParam("poLine", poLine).addParam("userId", userId)
				.execRtn("getLapSub");
		return wr;
	}

}
