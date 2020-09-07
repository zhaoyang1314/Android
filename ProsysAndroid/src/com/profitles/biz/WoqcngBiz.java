package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class WoqcngBiz extends BaseBiz {
	
	//获取分拆类型
	public WebResponse WoqcngType(String domain,String site) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.execRtn("getWoqcngSType");
		return wrp;
	}
	
	//扫标签码
	public WebResponse WoqcngScan(String domain,String site,String scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan)
				.execRtn("getWoqcngScan");
		return wrp;
	}
	
	//验证子项标签
	public WebResponse WoqcngScanSI(String domain,String site,String scan,String si) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan).addParam("si", si)
				.execRtn("getWoqcngScanSI");
		return wrp;
	}
	
	//根据供应商零件去找Bom
	public WebResponse WoqcngBom(String domain,String site,String part,String vend,String type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part).addParam("vend", vend).addParam("type", type)
				.execRtn("getWoqcngBom");
		return wrp;
	}
	
	//验证供应商是否存在，与零件是否是关系
	public WebResponse WoqcngVend(String domain,String site,String part,String vend,String type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part).addParam("vend", vend).addParam("type", type)
				.execRtn("checkWoqcngVend");
		return wrp;
	}	
	//保存数据
	public WebResponse WoqcngSave(String domain, String site, String userId,String scan, String part, String vend,
			String si, String qty, String rQty,String type, String yxj) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("userId", userId).addParam("scan", scan)
				.addParam("part", part).addParam("vend", vend).addParam("si", si).addParam("qty", qty)
				.addParam("rQty", rQty).addParam("type", type).addParam("yxj", yxj)
				.execRtn("WoqcngSave");
		return wrp;
	}
	
	//提交数据
	public WebResponse WoqcngSub(String domain, String site, String userId,String scan,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("site", site)
									.addParam("userId", userId)
									.addParam("scan", scan)
									.addParam("fnceffdate", fnceffdate)
									.execRtn("WoqcngSub");
		return wrp;
	}	
	
}
