package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class PpvBiz extends BaseBiz{
	//扫码
	public WebResponse ppv_scancode(String domain,String site,String scan){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("scan",scan)
				.execRtn("ppvScanCode");
		return wrp;
	}
	
	//至容
	public WebResponse ppv_zContainer(String domain,String part,String vend,
			String zContainer,String yBox, String yUnit, String yScat,String bcount){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("part",part)
				.addParam("vend",vend)
				.addParam("zContainer",zContainer)
				.addParam("yBox",yBox)
				.addParam("yUnit",yUnit)
				.addParam("yScat",yScat)
				.addParam("bcount",bcount)
				.execRtn("ppvzContainer");
		return wrp;
	}
	
	//提交
	public WebResponse ppv_Commit(String domain,String site,String userId,String yContainer,
			String zContainer,String zBox, String zUnit, String zScat,String Scan,String yCount,String fnceffdate){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site)
				.addParam("userId", userId)
				.addParam("yContainer", yContainer)
				.addParam("zContainer", zContainer)
				.addParam("zBox", zBox)
				.addParam("zUnit", zUnit)
				.addParam("zScat", zScat)
				.addParam("Scan", Scan)
				.addParam("yCount", yCount)
				.addParam("fnceffdate", fnceffdate)
				.execRtn("ppvCommit");
		return wrp; 
	}
}
