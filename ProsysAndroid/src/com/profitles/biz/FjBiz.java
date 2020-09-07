package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class FjBiz extends BaseBiz{
	//单号
	public WebResponse fj_nbr(String domain,String nbr){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("nbr",nbr)
				.execRtn("fjnbr");
		return wrp;
	}
	
	//仓储
	public WebResponse fj_locbin(String domain,String site,String nbr,String locbin){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("locbin",locbin)
				.execRtn("fjlocbin");
		return wrp;
	}
	
	//零件
	public WebResponse fj_part(String domain,String site,String nbr,String locbin,String part){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("locbin",locbin)
				.addParam("part",part)
				.execRtn("fjpart");
		return wrp;
	}
	
	//批次
	public WebResponse fj_lot(String domain,String site,String nbr,String locbin,String part,String lot){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("locbin",locbin)
				.addParam("part",part)
				.addParam("lot",lot)
				.execRtn("fjlot");
		return wrp;
	}
}
