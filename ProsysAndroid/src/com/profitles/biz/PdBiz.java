package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;


public class PdBiz extends BaseBiz {

	// 扫描仓储
		public WebResponse PdNewSchNbr(String domain, String site, String nbr) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("nbr", nbr).execRtn("PdNbr");
			return wrp;
		}
		
	// 扫描仓储
	public WebResponse PdLocBin(String domain, String site, String locbin,String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("locbin", locbin)
				.addParam("nbr", nbr).execRtn("PdLocBin");
		return wrp;
	}
	// 扫描仓储
		public WebResponse PdLocBin1(String domain, String site, String locbin) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("locbin", locbin)
					.execRtn("PdLocBin");
			return wrp;
		}
	// 扫描标签
	public WebResponse PdScan(String domain, String site,String scan,String loc) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan).addParam("loc", loc)
				.execRtn("PdScan");
		return wrp;
	}
	// 扫描标签
		public WebResponse PdNewScan(String domain, String site,String scan,String loc,String user,String bin,String nbr) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("scan", scan).addParam("loc", loc)
					.addParam("user", user).addParam("bin", bin).addParam("nbr", nbr).execRtn("PdNewScan");
			return wrp;
		}
	
	// 提交
	public WebResponse Pdsubmit(String domain, String site, String userid,String um ,String qty ,String scan, String part,String vend ,String lot,String loc,String bin,String date,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("um", um).addParam("qty", qty)
				.addParam("scan", scan).addParam("part", part)
				.addParam("vend", vend).addParam("lot", lot)
				.addParam("loc", loc).addParam("bin", bin)
				.addParam("date", date).addParam("fnceffdate", fnceffdate)
				.execRtn("Pdsubmit");
		return wrp;
	}
	
	// 扫描标签
	public WebResponse PdNewScanXj(String domain, String site,String scan,String loc,String user,String bin,String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan).addParam("loc", loc)
				.addParam("user", user).addParam("bin", bin).addParam("nbr", nbr).execRtn("PdNewScanXj");
		return wrp;
	}
	
	// 提交
	public WebResponse PdSubmitXj(String domain, String site,String scan,String loc,String user,String bin,String nbr, String qty) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan).addParam("loc", loc)
				.addParam("user", user).addParam("bin", bin).addParam("nbr", nbr)
				.addParam("qty", qty).execRtn("PdNewSubmitXj");
		return wrp;
	}
}
