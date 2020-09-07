package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class RScanSBiz extends BaseBiz {
	
	//获取分拆类型
	public WebResponse rScansType(String domain,String site) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.execRtn("checkrScansType");
		return wrp;
	}
	
	//扫标签码
	public WebResponse rScansScan(String domain,String site,String scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan)
				.execRtn("checkRScansScan");
		return wrp;
	}
	
	//验证子项标签
	public WebResponse rScansScanSI(String domain,String site,String scan,String si) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan).addParam("si", si)
				.execRtn("checkRScansScanSI");
		return wrp;
	}
	
	//根据供应商零件去找Bom
	public WebResponse rScansBom(String domain,String site,String part,String vend,String type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part).addParam("vend", vend).addParam("type", type)
				.execRtn("checkRScansBom");
		return wrp;
	}	
	
	//保存数据
	public WebResponse rScansSave(String domain, String site, String userId,String scan, String part, String iqcNbr, String vend,
			String si, String qty, String rQty,String type, String yxj) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("userId", userId).addParam("scan", scan)
				.addParam("part", part).addParam("iqcNbr", iqcNbr).addParam("vend", vend).addParam("si", si).addParam("qty", qty)
				.addParam("rQty", rQty).addParam("type", type).addParam("yxj", yxj)
				.execRtn("getRScansSave");
		return wrp;
	}
	
	//提交数据
	public WebResponse rScansSub(String domain, String site, String userId,String scan,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("site", site)
									.addParam("userId", userId)
									.addParam("scan", scan)
									.addParam("fnceffdate", fnceffdate)
									.execRtn("getRScansSub");
		return wrp;
	}	
	
}
