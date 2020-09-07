package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;

public class PkBiz extends BaseBiz {

	public WebResponse pkCheckNbr(String domain,String site, String nbr,String userId , String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId)
				.execRtn("checkPkNbr");
		return wr;
	}
	
	public WebResponse checkPkNbrTemp(String domain,String site ,String nbr,String userId,String macId ,String isDeleteTemp) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId).addParam("isDeleteTemp", isDeleteTemp)
				.execRtn("checkPkNbrTemp");
		return wr;
	}
	
	public WebResponse pkCheckLocBin(String domain,String site ,String nbr, String LocBin) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("LocBin", LocBin)
				.execRtn("CheckLocBin");
		return wr;
	}
	
	//异常操作 仓储  记录rfifo_log
	public WebResponse pkExcLocBin(String domain,String site ,String nbr,String LocBin,String rfifoId,String userId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("LocBin", LocBin).addParam("rfifoId", rfifoId).addParam("userId", userId)
				.execRtn("ExcLocBin");
		return wr;
	}
	
	public WebResponse pkCheckBar(String domain,String site ,String nbr,String code,String biaoshi) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("code", code).addParam("biaoshi", biaoshi)
				.execRtn("checkPkScanBar");
		return wr;
	}
	/*
	 * public WebResponse pkCheckBar(String domain,String site ,String nbr,String LocBin,String code,String biaoshi) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("LocBin", LocBin).addParam("code", code).addParam("biaoshi", biaoshi)
				.execRtn("checkPkScanBar");
		return wr;
	}
	 */
	
	public WebResponse pkCheckChangeLot(String domain,String site ,String nbr,String LocBin,String part,String lot,String qtyReq,String boxQty,String biaoshi,String vend) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("LocBin", LocBin).addParam("part", part)
				.addParam("lot", lot).addParam("qtyReq", qtyReq).addParam("boxQty", boxQty).addParam("biaoshi", biaoshi).addParam("vend", vend)
				.execRtn("checkPkChangeLot");
		return wr;
	}
	
	public WebResponse pkCheckSave(String domain,String site ,String nbr,String LocBin,String part,String lot,String qtyReq,String boxQty,String biaoshi,String vend) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("LocBin", LocBin).addParam("part", part)
				.addParam("lot", lot).addParam("qtyReq", qtyReq).addParam("boxQty", boxQty).addParam("biaoshi", biaoshi).addParam("vend", vend)
				.execRtn("checkPkChangeLot");
		return wr;
	}
	
	public WebResponse pkSave(String domain,String site ,String nbr,String table,String part,String lot,String sumQty,String userId,String mult,String boxQty,String scat,String bin,String scan,String vend) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("part", part).addParam("lot", lot)
				.addParam("sumQty", sumQty).addParam("userId", userId).addParam("mult", mult).addParam("boxQty", boxQty).addParam("scat", scat).addParam("bin", bin).addParam("scan", scan).addParam("vend", vend)
				.execRtn("pkSave");
		return wr;
	}
	
	public WebResponse pkSubmit(String domain,String site ,String nbr,String table,String part,String lot,String sumQty,String userId,String mult,String boxQty,String scat,String bin,String scan,String macId,String vend,String fnceffdate,String guid,String plSet) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("part", part).addParam("lot", lot)
				.addParam("sumQty", sumQty).addParam("userId", userId).addParam("mult", mult).addParam("boxQty", boxQty).addParam("scat", scat).addParam("bin", bin).addParam("scan", scan).addParam("macId", macId)
				.addParam("vend", vend).addParam("fnceffdate", fnceffdate).addParam("guid", guid).addParam("plSet", plSet)
				.execRtn("pkSubmit");
		return wr;
	}
	
	public WebResponse pkSubmitByNbr(String domain,String site ,String nbr,String table,String userId,String macId,String fnceffdate,String guid,String plSet) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("userId", userId)
				.addParam("macId", macId).addParam("fnceffdate", fnceffdate).addParam("guid", guid).addParam("plSet", plSet)
				.execRtn("pkSubmitByNbr");
		return wr;
	}
	
	//解锁
	public WebResponse pkUnLock(String domain,String site ,String nbr,String table,String userId,String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("userId", userId).addParam("macId", macId)
				.execRtn("pkUnLock");
		return wr;
	}

	public List<Map<String, Object>> getPkList(String domain,String site) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site)
				.execRtn("getPkList");
		return wr.getDataToList();
	}
	
}
