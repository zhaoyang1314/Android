package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class SendBiz extends BaseBiz {

	public WebResponse sendByRSN(String domain) {
		WebResponse wr = getWcuCP().addParam("domain", domain)
				.execRtn("getSendByRSN");
		return wr;
	}	
	public WebResponse sendNbr(String domain,String site ,String nbr) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.execRtn("getSendNbr");
		return wr;
	}
	
	public WebResponse sendSave(String domain,String site ,String nbr,String part,String lot,String qty,String spnReason) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("part", part).addParam("lot", lot).addParam("qty", qty).addParam("spnReason", spnReason)
				.execRtn("getsendSave");
		return wr;
	}
	
	//提交前先保存一次
	public WebResponse sendSubmit(String domain,String site,String nbr,String part,String um,String lot,String nuit,String qty,String spnReason,String table,String userId,String macId,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("part",part)
				.addParam("um",um)
				.addParam("lot",lot)
				.addParam("nuit",nuit)
				.addParam("qty",qty)
				.addParam("spnReason",spnReason)
				.addParam("table",table)
				.addParam("userId",userId)
				.addParam("macId",macId)
				.addParam("fnceffdate",fnceffdate)
				.execRtn("getSendSubmit");
		return wrp;		
	}
	
	//直接提交
	public WebResponse sendSubmitByNbr(String domain,String site ,String nbr,String table,String userId,String macId,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("domain",domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("table",table)
				.addParam("userId",userId)
				.addParam("macId",macId)
				.addParam("fnceffdate",fnceffdate)
				.execRtn("getSendSubmitByNbr");
		return wrp;		
	}

}
