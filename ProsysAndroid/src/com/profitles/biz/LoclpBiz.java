package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class LoclpBiz extends BaseBiz {

	public WebResponse getList(String domain, String site, String part,
			String lot, String loc, String bin, String userid, String macId) {
		WebResponse wr = getWcuCP()
				.addParam("domain", domain).addParam("site", site)
				.addParam("part", part).addParam("lot", lot)
				.addParam("loc", loc).addParam("bin", bin)
				.addParam("userid", userid).addParam("macId", macId)
				.execRtn("getLocPrintList");
		return wr;
	}

	public WebResponse locPrint(String domain, String site, String part,
			String lot, String loc, String bin, String ref, String allqty,
			String multqty, String boxnum, String sannum, String status,
			String vend, String userid, String macId) {
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain).addParam("site", site)
				.addParam("part", part).addParam("lot", lot)
				.addParam("loc", loc).addParam("bin", bin)
				.addParam("ref", ref).addParam("allqty", allqty)
				.addParam("multqty", multqty).addParam("boxnum", boxnum)
				.addParam("sannum", sannum).addParam("status", status)
				.addParam("vend", vend).addParam("userid", userid)
				.addParam("macId", macId).execRtn("saveLocPrintInfo");
		return wrp;
	}

}
