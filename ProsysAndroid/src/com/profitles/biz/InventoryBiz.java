package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class InventoryBiz extends BaseBiz {

	// 单号
	public WebResponse Inventory_nbr(String domain, String site, String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.execRtn("InventoryNbr");
		return wrp;
	}

	// 仓储
	public WebResponse Inventory_locbin(String domain, String site, String nbr,
			String locbin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.addParam("locbin", locbin).execRtn("InventoryLocbin");
		return wrp;
	}

	// 条码
	public WebResponse Inventory_scan(String domain, String site, String nbr,
			String scan, String loc, String bin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.addParam("scan", scan).addParam("loc", loc)
				.addParam("bin", bin).execRtn("InventoryScan");
		return wrp;
	}

	// 验证
	public WebResponse Inventory_vaildAllDo(String domain, String site, String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr).execRtn("InventoryVaildAllDo");
		return wrp;
	}
	
	// 保存 - 验证
	public WebResponse Inventory_save_val(String domain, String site, String nbr,
			String lot, String loc, String bin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.addParam("lot", lot).addParam("loc", loc)
				.addParam("bin", bin).execRtn("InventoryVaildSave");
		return wrp;
	}

	// 保存
	public WebResponse Inventory_save(String domain, String site, String nbr, String part,
			String lot, String loc, String bin, String qty,String scan,String vend) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr).addParam("part", part)
				.addParam("lot", lot).addParam("loc", loc)
				.addParam("bin", bin).addParam("qty", qty)
				.addParam("scan", scan).addParam("vend", vend).execRtn("InventorySave");
		return wrp;
	}

	// 提交
	public WebResponse Inventory_submit(String domain, String site, String nbr,
			String userid,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("nbr", nbr)
				.addParam("userid", userid).addParam("fnceffdate", fnceffdate)
				.execRtn("InventorySubmit");
		return wrp;
	}
}
