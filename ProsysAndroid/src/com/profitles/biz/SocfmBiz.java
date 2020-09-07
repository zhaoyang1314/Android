package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class SocfmBiz extends BaseBiz {

	public WebResponse getSoShipByNbr(String domain,String site, String nbr,String userId , String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId)
				.execRtn("getSoShipByNbr");
		return wr;
	}
	
	public WebResponse unLockSoShip(String domain,String site ,String nbr,String userId,String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId)
				.execRtn("unLockSoShip");
		return wr;
	}
	
	public WebResponse updateSoShipSt(String domain,String site,String nbr,String userId,String appName,String appvVer, String isQad,String macId,String fnceffdate){
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("userId", userId)
				.addParam("appName", appName).addParam("appvVer", appvVer).addParam("isQad", isQad).addParam("macId", macId).addParam("fnceffdate", fnceffdate)
				.execRtn("updateSoShipSt");
		return wr;
	} 
	
	//保存
	public WebResponse updateSoShipSave(String domain,String site,String nbr,String line,String qty){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("line",line)
				.addParam("qty",qty)
				.execRtn("updateSoShipSave");
		return wrp;
	}
}
