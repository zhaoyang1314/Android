package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class WostQcBiz extends BaseBiz {
	
	//扫码
	public WebResponse wostQcScan(String domain,String site,String scan,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan).addParam("userId", userId)
				.execRtn("checkWostQcScan");
		return wrp;
	}

	//扫码
	public WebResponse wostQcSn(String domain,String site,String part,String sn) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part).addParam("sn", sn)
				.execRtn("checkWostQcSn");
		return wrp;
	}

	//提交数据
	public WebResponse wostQcSub(String domain, String site, String userId, String scan, 
					String woNbr,String shift,String part,String partName,String lot,String wkctr,
					String line,String cPart,String uKey,String uDate ,String sn) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("site", site)
									.addParam("userId", userId)
									.addParam("scan", scan)
									.addParam("woNbr", woNbr)
									.addParam("shift", shift) 
									.addParam("part", part)
									.addParam("partName", partName)
									.addParam("lot", lot)
 									.addParam("wkctr", wkctr)
									.addParam("line", line)
									.addParam("cPart", cPart)
									.addParam("uKey", uKey)
									.addParam("uDate", uDate)
									.addParam("sn", sn)
									.execRtn("wostQcSub");	
		return wrp;
	}
 
}
