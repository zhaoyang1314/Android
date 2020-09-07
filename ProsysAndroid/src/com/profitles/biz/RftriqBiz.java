package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;

public class RftriqBiz extends BaseBiz {
	public WebResponse getRftriqInfo(String domain,String site,String part,String vend,String loc,String eta,String userId,String scan, boolean isUser) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part)
				.addParam("vend", vend).addParam("loc", loc).addParam("eta", eta).addParam("userId", userId).addParam("scan", scan)
				.addParam("isUser", isUser ? "1" : "0")
				.execRtn("getRftriqInfo");
		return wrp;
	}	 
	public List<Map<String, Object>> getRftriqInfoScan(String domain,String site,String eta,String userId,String scan, boolean isUser) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("eta", eta).addParam("userId", userId).addParam("scan", scan).addParam("isUser", isUser ? "1" : "0")
				.execRtn("getRftriqInfoScan");
		return wrp.getDataToList();
	}
	
	// 客户图号关系查询功能的查询按钮事件
	
	public WebResponse getSearchPartCust(String domain, String part, String cust, String partdesc, String cust_part){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("part", part).addParam("cust", cust)
				.addParam("partdesc", partdesc).addParam("cust_part", cust_part)
				.execRtn("getSearchPartCust");
		return wrp;
	}
}
