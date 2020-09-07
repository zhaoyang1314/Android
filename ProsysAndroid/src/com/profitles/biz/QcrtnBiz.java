package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class QcrtnBiz extends BaseBiz {

	// 单号验证
	public WebResponse qctrnNbr(String domain, String site, String nbr,
			String userId, String macId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId)
				.execRtn("qctrnNbr");
		return wrp;
	}

	// 扫码
	public WebResponse qctrnBar(String domain, String site, String code,
			String nbr, String loc) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("code", code)
				.addParam("nbr", nbr).addParam("loc", loc).execRtn("qctrnBar");
		return wrp;
	}

	// 保存
	public WebResponse qcrtnSave(String domain, String site, String nbr,
			String locbin, String partn, String lotr, String nums, String scan,
			String actpartumString) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.addParam("locbin", locbin).addParam("partn", partn)
				.addParam("lotr", lotr).addParam("nums", nums)
				.addParam("scan", scan)
				.addParam("actpartumString", actpartumString)
				.execRtn("qcrtnSave");
		return wrp;
	}

	// 提交前先保存一次
	public WebResponse qcrtnSubmit(String okQty,String ngQty,String rsvQty,String expQty, String domain, String site, String nbr,
			String locbin, String partn, String lotr, String nums, String scan,
			String actpartumString, String table, String userId, String macId,
			String qad,String SubminOrSubminByNbr,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("okQty", okQty)
				.addParam("ngQty", ngQty)
				.addParam("rsvQty", rsvQty).addParam("expQty", expQty)
				.addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.addParam("locbin", locbin).addParam("partn", partn)
				.addParam("lotr", lotr).addParam("nums", nums)
				.addParam("scan", scan)
				.addParam("actpartumString", actpartumString)
				.addParam("table", table).addParam("userId", userId)
				.addParam("macId", macId).addParam("qad", qad)
				.addParam("SubminOrSubminByNbr", SubminOrSubminByNbr)
				.addParam("fnceffdate", fnceffdate)
				.execRtn("qcrtnSubmit");
		return wrp;
	}

	// 直接提交
	public WebResponse qcrtnSubmitByNbr(String okQty,String ngQty,String rsvQty,String expQty,String domain, String site, String nbr,
			String table, String userId, String macId, String qad) {
		WebResponse wrp = getWcuCP().addParam("okQty", okQty)
				.addParam("ngQty", ngQty)
				.addParam("rsvQty", rsvQty).addParam("expQty", expQty)
				.addParam("domain", domain)
				.addParam("OKQTY", okQty).addParam("NGQTY", ngQty)
				.addParam("RSVQTY", rsvQty).addParam("EXPQTY", expQty)
				.addParam("domain", domain).addParam("site", site)
				.addParam("nbr", nbr).addParam("table", table)
				.addParam("userId", userId).addParam("macId", macId)
				.addParam("qad", qad).execRtn("qcrtnSubmitByNbr");
		return wrp;
	}
}
