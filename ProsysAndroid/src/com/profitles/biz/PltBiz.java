package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class PltBiz extends BaseBiz {
	
	//单号
	public WebResponse pltCheckBar(String domain,String site,String code) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.execRtn("checkPltBar");
		return wrp;
	}

	//提交数据
	public WebResponse pxSub(String domain, String site,String userId,String json,String toLot,String toLocBin,String sumQty,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("userId", userId).addParam("json",json)
				.addParam("toLot", toLot).addParam("toLocBin", toLocBin).addParam("sumQty", sumQty).addParam("fnceffdate", fnceffdate)
				.execRtn("getPxSub");
		return wrp;
	}	
	
	/*//查询供方是否存在
	public WebResponse porcVend(String domain,String vend) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("vend", vend)
				.execRtn("queryPorcVend");
		return wrp;
	}	
	
	//查询零件是否存在
	public WebResponse porcPart(String domain,String site,String part,String vend,String nbr,String cnNbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part).addParam("vend", vend)
				.addParam("nbr", nbr).addParam("cnNbr", cnNbr)
				.execRtn("queryPorcPart");
		return wrp;
	}
	
	//验证同批次零件是否收过货
	public WebResponse porcLot(String domain,String site,String nbr,String cnNbr,String part,String lot) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("cnNbr", cnNbr)
				.addParam("part", part).addParam("lot", lot)
				.execRtn("queryPorcLot");
		return wrp;
	}	
	
	//校验仓储
	public WebResponse porcLocBin(String domain,String site,String LocBin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("LocBin", LocBin)
				.execRtn("queryPorcLocBin");
		return wrp;
	}	
		
	//保存提交数据
	public WebResponse porcSave(String domain, String site, String loc, String part, String vend, String qty,String um, String nbr, String appName,
			 String userId, String parentid,String nums, String ntype,String lot,String isQad,String cnNbr,String mult) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("loc", loc).addParam("part", part)
				.addParam("vend", vend).addParam("qty", qty).addParam("um", um).addParam("nbr", nbr).addParam("appName", appName)
				.addParam("userId", userId).addParam("parentid", parentid).addParam("nums", nums).addParam("ntype", ntype)
				.addParam("lot", lot).addParam("isQad", isQad).addParam("cnNbr", cnNbr).addParam("mult", mult)
				.execRtn("getPorcSave");
		return wrp;
	}	
	//解锁
	public WebResponse porcUnLock(String domain,String site ,String nbr,String table,String userId,String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("userId", userId).addParam("macId", macId)
				.execRtn("PorcUnLock");
		return wr;
	}*/	
	
}
