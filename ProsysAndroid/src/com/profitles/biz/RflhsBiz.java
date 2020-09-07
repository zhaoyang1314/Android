package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class RflhsBiz extends BaseBiz{

	public WebResponse getRflInfo(String domain,String site,String scan,String lot,String part,String po,String cons,String vend) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan)
				.addParam("lot", lot).addParam("part", part).addParam("po", po).addParam("cons", cons).addParam("vend", vend)
				.execRtn("getRflhsInfo");
		return wrp;
	}	
}
