package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class JhwckBiz extends BaseBiz{
	
	//原因数据加载
		public WebResponse jhwrk_OnLoadReason(String domain){
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.execRtn("jhwckOnLoadReason");
			return wrp;
		}
	
	//扫码
	public WebResponse jhwrk_scan(String domain,String site,String scan,String loc,String bin){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("scan",scan)
				.addParam("loc",loc)
				.addParam("bin",bin)
				.execRtn("jhwckScan");
		return wrp;
	}
	
	//仓储
	public WebResponse jhwrk_locbin(String locbin,String domain,String site){
		WebResponse wrp = getWcuCP()
				.addParam("locbin",locbin)
				.addParam("domain", domain)
				.addParam("site",site)
				.execRtn("jhwckLocbin");
		return wrp;
	}
	
	//提交
	public WebResponse jhwrk_submit(String domain,String site,String loc,String bin,String part
			,String lot,String scount,String um,String desc,String scan,String vend,String userid,String rsg,
			String fnceffdate){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("loc",loc)
				.addParam("bin",bin)
				.addParam("part",part)
				.addParam("lot",lot)
				.addParam("scount",scount)
				.addParam("um",um)
				.addParam("desc",desc)
				.addParam("scan", scan)
				.addParam("vend", vend)
				.addParam("userid", userid)
				.addParam("rsg", rsg)
				.addParam("fnceffdate", fnceffdate)
				.execRtn("jhwckSub");
		return wrp;	
	}
}
