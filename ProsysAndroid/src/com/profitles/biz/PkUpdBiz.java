package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;

public class PkUpdBiz extends BaseBiz {

	public WebResponse pkCheckNbr(String domain,String site, String nbr,String userId , String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId)
				.execRtn("checkPkUpdNbr");
		return wr;
	}
	//单号存在扫描记录是否继续扫(取消会把当前单号所有保存记录清除)
	public WebResponse checkPkNbrTemp(String domain,String site ,String nbr,String userId,String macId ,String isDeleteTemp) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId).addParam("isDeleteTemp", isDeleteTemp)
				.execRtn("checkPkUpdNbrTemp");
		return wr;
	}
	
	public WebResponse pkCheckLocBin(String domain,String site ,String nbr, String LocBin) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("LocBin", LocBin)
				.execRtn("checkPkUpdLocBin");
		return wr;
	}
	
	//异常操作 仓储  记录rfifo_log
	public WebResponse pkExcLocBin(String domain,String site ,String nbr,String LocBin,String rfifoId,String userId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("LocBin", LocBin).addParam("rfifoId", rfifoId).addParam("userId", userId)
				.execRtn("excPkUpdLocBin");
		return wr;
	}
	//条码时调用：的 批次事件
	public WebResponse pkCheckBar(String domain,String site ,String nbr,String LocBin,String code,String biaoshi) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("LocBin", LocBin).addParam("code", code).addParam("biaoshi", biaoshi)
				.execRtn("checkPkUpdScanBar");
		return wr;
	}
	//扫条码时调用：如果是 箱标签，并该条码已存在,是否撤销扫描分拣数据，选择撤销时调用
	public WebResponse pkShowMess(String domain,String site ,String nbr,String LocBin,String code,String biaoshi) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("LocBin", LocBin).addParam("code", code).addParam("biaoshi", "1")
				.execRtn("checkPkUpdScanShowMess");
		return wr;
	}
	//又一个 批次事件
	public WebResponse pkCheckChangeLot(String domain,String site ,String nbr,String LocBin,String part,String lot,String qtyReq,String boxQty,String biaoshi,String vend) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("LocBin", LocBin).addParam("part", part)
				.addParam("lot", lot).addParam("qtyReq", qtyReq).addParam("boxQty", boxQty).addParam("biaoshi", biaoshi).addParam("vend", vend)
				.execRtn("checkPkUpdChangeLot");
		return wr;
	}
	
	public WebResponse pkCheckSave(String domain,String site ,String nbr,String LocBin,String part,String lot,String qtyReq,String boxQty,String biaoshi,String vend) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("LocBin", LocBin).addParam("part", part)
				.addParam("lot", lot).addParam("qtyReq", qtyReq).addParam("boxQty", boxQty).addParam("biaoshi", biaoshi).addParam("vend", vend)
				.execRtn("checkPkUpdChangeLot");
		return wr;
	}
	
	public WebResponse pkSave(String domain,String site ,String nbr,String table,String part,String lot,String sumQty,String userId,String mult,String boxQty,String scat,String bin,String scan,String vend
			,String cust,String endCm,String saveCmBar,String cmBar,String cmPart,String PkReq,String activity,String isQuX,String yLot) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("part", part).addParam("lot", lot)
				.addParam("sumQty", sumQty).addParam("userId", userId).addParam("mult", mult).addParam("boxQty", boxQty).addParam("scat", scat).addParam("bin", bin).addParam("scan", scan).addParam("vend", vend)
				.addParam("cust", cust).addParam("endCm", endCm).addParam("saveCmBar", saveCmBar).addParam("cmBar", cmBar).addParam("cmPart", cmPart).addParam("PkReq",PkReq).addParam("activity",activity)
				.addParam("isQuX",isQuX).addParam("yLot", yLot)
				.execRtn("pkUpdSave");
		return wr;
	}
	
	public WebResponse pkSubmit(String domain,String site ,String nbr,String table,String part,String lot,String sumQty,String userId,String mult,String boxQty,String scat,String bin,String scan,String macId,String vend,String fnceffdate,String guid,String plSet
			,String cust,String endCm,String saveCmBar,String cmBar,String cmPart) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("part", part).addParam("lot", lot)
				.addParam("sumQty", sumQty).addParam("userId", userId).addParam("mult", mult).addParam("boxQty", boxQty).addParam("scat", scat).addParam("bin", bin).addParam("scan", scan).addParam("macId", macId)
				.addParam("vend", vend).addParam("fnceffdate", fnceffdate).addParam("guid", guid).addParam("plSet", plSet)
				.addParam("cust", cust).addParam("endCm", endCm).addParam("saveCmBar", saveCmBar).addParam("cmBar", cmBar).addParam("cmPart", cmPart)
				.execRtn("pkUpdSubmit");
		return wr;
	}
	
	public WebResponse pkSubmitByNbr(String domain,String site ,String nbr,String table,String userId,String macId,String fnceffdate,String guid,String plSet) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("userId", userId)
				.addParam("macId", macId).addParam("fnceffdate", fnceffdate).addParam("guid", guid).addParam("plSet", plSet)
				.execRtn("pkUpdSubmitByNbr");
		return wr;
	}
	
	//解锁
	public WebResponse pkUnLock(String domain,String site ,String nbr,String table,String userId,String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("userId", userId).addParam("macId", macId)
				.execRtn("pkUpdUnLock");
		return wr;
	}

	public List<Map<String, Object>> getPkList(String domain,String site) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site)
				.execRtn("getPkUpdList");
		return wr.getDataToList();
	}

	// return pkupdbiz.pkCheckCmBar(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),etxPkNbr.getValStr().toString(), 
	// 		 etxPkBar.getValStr().toString(),needCheck,endCm);
	
	public WebResponse pkCheckCmBar(String domain, String site,
			String nbr, String bar, String part,String needcheck, String cust,String endcust,String cmbar) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("nbr", nbr).addParam("bar", bar)
				.addParam("part", part).addParam("needcheck", needcheck)
				.addParam("cust", cust).addParam("endcust", endcust).addParam("cmbar", cmbar)
				.execRtn("checkPkUpdCmBar");
		return wr;
	}

	public Object pkDltScan(String domain, String site, String nbr,
			String bar, String userId, String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("nbr", nbr).addParam("bar", bar)
				.addParam("userId", userId)
				.addParam("macId", macId)
				.execRtn("pkUpdDelectScan");
		return wr;
	}
	
}
