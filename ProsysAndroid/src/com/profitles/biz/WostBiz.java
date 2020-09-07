package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class WostBiz extends BaseBiz {
	
	//扫码
	public WebResponse wostScan(String domain,String site,String scan,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan).addParam("userId", userId)
				.execRtn("checkWostScan");
		return wrp;
	}

	//提交数据
	public WebResponse wostSub(String domain, String site, String userId, String scan, 
					String woNbr,String shift,String part,String lot,String wkctr,String line,
					String cPart,String uKey,String uDate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("site", site)
									.addParam("userId", userId)
									.addParam("scan", scan)
									.addParam("woNbr", woNbr)
									.addParam("shift", shift)
									.addParam("part", part)
									.addParam("lot", lot)
									.addParam("wkctr", wkctr)
									.addParam("line", line)
									.addParam("cPart", cPart)
									.addParam("uKey", uKey)
									.addParam("uDate", uDate)
									.execRtn("wostSub");
		return wrp;
	}
 
}
