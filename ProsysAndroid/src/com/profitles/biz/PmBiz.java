package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class PmBiz extends BaseBiz {
	
	//托标签
	public WebResponse spellCheckBar(String domain,String site,String code) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.execRtn("checkspellBar");
		return wrp;
	}

	//验证单件标签是否存在
	public WebResponse spellCheckSnList(String domain,String site,String code,String type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code).addParam("type", type)
				.execRtn("getSninfoAll");
		return wrp;
	}
	
	//验证单件标签是否存在
	public WebResponse spellCheckSn(String domain,String site,String code,String type,String seq,String cust_part,String m_scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code).addParam("type", type).addParam("seq", seq)
				.addParam("cust_part", cust_part).addParam("m_scan", m_scan)
				.execRtn("checkspellSn");
		return wrp;
	}

}
