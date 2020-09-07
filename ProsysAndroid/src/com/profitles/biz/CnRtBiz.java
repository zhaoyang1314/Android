package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class CnRtBiz extends BaseBiz {
	
	//查询单号是否存在
	public WebResponse getConsNbr(String domain,String site,String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.execRtn("getNbrByList");
		return wrp;
	}
	
	//提交
	public WebResponse getCnrcSubmit(String domain,String site,String nbr,String userId,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("userId", userId)
				.addParam("fnceffdate", fnceffdate).execRtn("cnrcSubmit");
		return wrp;
	}	
	
}
