package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class TrBiz extends BaseBiz{
	//仓储
	public WebResponse tr_fmBinonBlur(String locbin,String domain,String site){
		WebResponse wrp = getWcuCP()
				.addParam("lockey",locbin)
				.addParam("domain", domain)
				.addParam("site",site)
				.execRtn("checkloc");
		return wrp;
	}
	 
	//零件
	public WebResponse tr_part(String domain,String site,String part,String locbin){
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain)
				.addParam("site",site)
				.addParam("part",part)
				.addParam("locbin",locbin)
				.execRtn("trPart");
		return wrp;
	}
	
	//批次
	public WebResponse tr_lot(String domain,String site,String part,String lot,String locbin){
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain)
				.addParam("site",site)
				.addParam("part",part)
				.addParam("lot",lot)
				.addParam("locbin",locbin)
				.execRtn("trLot");
		return wrp;
	} 
		
	//扫码
	public WebResponse tr_scan(String domain,String site,String fmbin,String code){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("locbin",fmbin)
				.addParam("code",code)
				.execRtn("trCode");
		return wrp;
	}
	
	//提交
	public WebResponse tr_submit(String domain,String site,String fmbin,String tobin,String part
			,String lot,String scount,String um,String partname,String code,String vend,String userid,String fnceffdate){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("fmbin",fmbin)
				.addParam("tobin",tobin)
				.addParam("part",part)
				.addParam("lot",lot)
				.addParam("scount",scount)
				.addParam("um",um)
				.addParam("partname",partname)
				.addParam("code", code)
				.addParam("vend", vend)
				.addParam("userid", userid)
				.addParam("fnceffdate", fnceffdate)
				.addParam("type", "TrActivity")
				.execRtn("trSub");
		return wrp;	
	}
}
