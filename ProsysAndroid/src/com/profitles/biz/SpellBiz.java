package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class SpellBiz extends BaseBiz {
	
	//托标签
	public WebResponse spellCheckBar(String domain,String site,String code) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.execRtn("checkspellBar");
		return wrp;
	}

	//验证单件标签是否存在
	public WebResponse spellCheckSnList(String domain,String site,String code,String type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code).addParam("type", type)
				.execRtn("getSnAll");
		return wrp;
	}
	
	//验证单件标签是否存在
	public WebResponse spellCheckSn(String domain,String site,String code,String type,String seq,String cust_part,String m_scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code).addParam("type", type).addParam("seq", seq)
				.addParam("cust_part", cust_part).addParam("m_scan", m_scan)
				.execRtn("checkspellSn");
		return wrp;
	}
	//提交数据
	public WebResponse spellSub(String domain, String site,String usertype,String userid,String uuid,String code,String json,String jsonremove,String jsonsnacoderemove,String type,String bnum) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("usertype", usertype).addParam("userid", userid)
				.addParam("uuid", uuid).addParam("code", code).addParam("json",json).addParam("jsonremove",jsonremove).addParam("jsonsnacoderemove",jsonsnacoderemove).addParam("type",type).addParam("bnum",bnum)
				.execRtn("getspellSub");
		return wrp;
	}	
	

	
}
