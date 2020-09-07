package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class QiBiz extends BaseBiz{
		
	//按零件查询库存
	public WebResponse qip_queryInvByPart(String domain,String site,String part){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("part",part)
				.execRtn("QueryInvByPart");
		return wrp;
	}
	
	//按储位查询库存
	public WebResponse qip_queryInvByBin(String domain,String site,String bin){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("bin",bin)
				.execRtn("QueryInvByBin");
		return wrp;
	}
	
}
