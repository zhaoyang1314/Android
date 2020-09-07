package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class fpsBiz extends BaseBiz {

	// 单号
	public WebResponse fps_nbr(String domain, String site, String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.execRtn("fpsNbr");
		return wrp;
	}
	
	// 条码
	public WebResponse fps_scan(String domain, String site, String scan,String nbr,String rfc_is_nbr_fps) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("scan", scan)
				.addParam("nbr", nbr).addParam("rfc_is_nbr_fps", rfc_is_nbr_fps).execRtn("fpsScan");
		return wrp;
	}

	// 提交
	public WebResponse fps_submit(String domain, String site, String userid,
			String locbin, String tiaoma,String vend,String lbs,String qty,String nbr,String cons_nbr,
			String fnceffdate,String rfc_is_nbr_fps) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("locbin", locbin).addParam("tiaoma", tiaoma)
				.addParam("vend", vend).addParam("lbs", lbs)
				.addParam("qty", qty).addParam("nbr", nbr)
				.addParam("cons_nbr", cons_nbr).addParam("fnceffdate", fnceffdate)
				.addParam("rfc_is_nbr_fps", rfc_is_nbr_fps)
				.execRtn("fpsSubmit");
		return wrp;
	}
}
