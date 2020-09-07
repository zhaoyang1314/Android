package com.profitles.biz;

import android.R.string;

import com.profitles.framwork.activity.util.WebResponse;

public class PrBiz extends BaseBiz {
	
	//托标签
	public WebResponse PrCheckBar(String domain,String site,String nbr,String scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.addParam("scan", scan)
				.execRtn("PrScan");
		return wrp;
	}
	
	//验证单件标签是否存在
	public WebResponse PrCheckSub(String domain,String site,String json,String userid,String date,String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("json", json)
				.addParam("userid", userid).addParam("date", date)
				.addParam("nbr", nbr)
				.execRtn("returnSalseSub");
		return wrp;
	}
	//验证退货单是否存在，是否处于可操作状态
	public WebResponse PrSearNbr(String domain, String site,String userid,String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("nbr", nbr)
				.execRtn("PrCode");
		return wrp;
	}
	
}
