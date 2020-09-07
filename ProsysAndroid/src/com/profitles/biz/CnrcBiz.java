package com.profitles.biz;

import java.util.Date;

import android.R.menu;

import com.profitles.framwork.activity.util.WebResponse;

public class CnrcBiz extends BaseBiz {

	//扫码
	public WebResponse cnrcCode(String domain,String site,String code) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("code", code)
				.execRtn("checkCnrcCode");
		return wrp;
	}
	
	//获取送货单送货数量
	public WebResponse cnrcXrconsNbr(String domain,String site,String xrconsNbr,String part) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("xrconsNbr", xrconsNbr).addParam("part", part)
				.execRtn("queryXrconsNbr");
		return wrp;
	}
	
	//校验仓储
	public WebResponse cnrcLocBin(String domain,String site,String LocBin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("LocBin", LocBin)
				.execRtn("queryCnrcLocBin");
		return wrp;
	}	
	//单号
	public WebResponse cnrcNbr(String domain,String site,String nbr,String dataSet,String userId,String macId,String biaoshi1,String biaoshi2) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("dataSet", dataSet).addParam("userId", userId).addParam("macId", macId)
				.addParam("biaoshi1", biaoshi1).addParam("biaoshi2", biaoshi2)
				.execRtn("checkCnrcNbr");
		return wrp;
	}
	
	//查询供应商是否存在
	public WebResponse cnrcVend(String domain,String vend) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("vend", vend)
				.execRtn("queryCnrcVend");
		return wrp;	
	}
	
	//查询零件是否存在
	public WebResponse cnrcPart(String domain,String site,String part,String vend) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("part", part).addParam("vend", vend)
				.execRtn("queryCnrcPart");
		return wrp;
	}
	
	
	//保存提交数据
	public WebResponse cnrcSave(String domain, String site, String loc, String lot, String part, String vend,String scan, String qty,String um, String nbr, String appName,
			 String userId, String parentid, String tag, String nums, String ntype,String lot_Gone,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("loc", loc).addParam("lot", lot).addParam("part", part)
				.addParam("vend", vend).addParam("scan", scan).addParam("qty", qty).addParam("um", um).addParam("nbr", nbr).addParam("appName", appName)
				.addParam("userId", userId).addParam("parentid", parentid).addParam("tag", tag).addParam("nums", nums).addParam("ntype", ntype).addParam("lot_Gone", lot_Gone)
				.addParam("fnceffdate", fnceffdate).execRtn("getCnrcSave");
		return wrp;
	}	
	//解锁
	public WebResponse cnrcUnLock(String domain,String site ,String nbr,String table,String userId,String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("table", table).addParam("userId", userId).addParam("macId", macId)
				.execRtn("cnrcUnLock");
		return wr;
	}	
	
}
