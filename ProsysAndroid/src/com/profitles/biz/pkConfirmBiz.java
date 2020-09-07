package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class pkConfirmBiz extends BaseBiz{
		
	//单号
	public WebResponse pkConfirm_nbr(String domain,String site,String nbr){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.execRtn("pkConfirmNbr");
		return wrp;
	}
	//保存
	public WebResponse onItemClickUpQty(String domain,String site,String nbr,String line,String qty){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("line",line)
				.addParam("qty",qty)
				.execRtn("onItemClickUpQty");
		return wrp;
	}
	
	//提交
	public WebResponse pkConfirm_submit(String domain,String site,String nbr,String userid,String fnceffdate){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr", nbr)
				.addParam("userid", userid)
				.addParam("fnceffdate", fnceffdate)
				.execRtn("pkConfirmSub");
		return wrp;	
	}
}
