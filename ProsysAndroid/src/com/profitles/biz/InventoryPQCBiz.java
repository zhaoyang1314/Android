package com.profitles.biz;

import java.util.HashMap;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;

public class InventoryPQCBiz extends BaseBiz {

	
	public WebResponse getPQCDate(String domain,String site ,String code) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.execRtn("getPQCDate");
		return wr;
	}
	
	public WebResponse CKDateSub(String domain,String site ,String nbr,String part) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("part", part).execRtn("CheckDate");
		return wr;
	}
	
	public WebResponse PQCDateSub(String domain,String site ,String uid,String nbr,String part,String qty,String ckqty,String loc) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("uid", uid)
				.addParam("part", part).addParam("nbr", nbr)
				.addParam("qty", qty).addParam("ckqty", ckqty).addParam("loc", loc)
				.execRtn("save");
		return wr;
	}

}
