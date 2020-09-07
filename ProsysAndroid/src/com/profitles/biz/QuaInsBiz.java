package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;


public class QuaInsBiz extends BaseBiz {

	// 扫描标签
		public WebResponse QuaInsScan(String domain, String site,String scan,String marking) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("scan", scan)
					.addParam("marking",marking).execRtn("QuaInsScan");
			return wrp;
		}
		
		
		
		public WebResponse QuaInsDel(String domain, String site,String scan,String oqc) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("scan", scan)
					.addParam("oqc", oqc)
					.execRtn("QuaInsDel");
			return wrp;
		}
	
	// 提交
	public WebResponse QuaInsSubmit(String domain, String site, String userid,String um ,String qty ,String scan, String part,String vend ,String lot,String loc,String bin,String date,String fnceffdate,String rflot_type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("um", um).addParam("qty", qty)
				.addParam("scan", scan).addParam("part", part)
				.addParam("vend", vend).addParam("lot", lot)
				.addParam("loc", loc).addParam("bin", bin)
				.addParam("date", date).addParam("fnceffdate", fnceffdate)
				.addParam("rflot_type", rflot_type).execRtn("QuaInsSubmit");
		return wrp;
	}
	
}
