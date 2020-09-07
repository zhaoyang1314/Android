package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class RecehsBiz extends BaseBiz {
	public WebResponse getConsInfo(String domain,String site,String part,String ventor,String rece,String nbr,String date,String send) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part)
				.addParam("ventor", ventor).addParam("rece", rece).addParam("nbr", nbr).addParam("date",date).addParam("send", send)
				.execRtn("getRecehist");
		return wrp;
	}	
}
