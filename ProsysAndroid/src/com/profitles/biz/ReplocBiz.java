package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;

public class ReplocBiz extends BaseBiz {
	public WebResponse getConsInfo(String domain,String site,String part,String lot,String loc,String bin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part)
				.addParam("lot", lot).addParam("loc", loc).addParam("bin", bin)
				.execRtn("getReplocInfo");
		return wrp;
	}	
	// 扫码进行 获取 库存
	public List<Map<String, Object>> pkCheckNbr(String domain,String site,String scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("scan", scan)
				.execRtn("getReplocInfoScan");
		return  wrp.getDataToList();
	}	
}
