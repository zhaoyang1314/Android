package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class DisBiz extends BaseBiz {
	
	//托标签
	public WebResponse DisCheckBar(String domain,String site,String code) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.execRtn("DisCheckBar");
		return wrp;
	}

	//查询当前托标签下的单件标签
	public WebResponse DisCheckSnList(String domain,String site,String code,String type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code).addParam("type", type)
				.execRtn("DisCheckSnList");
		return wrp;
	}
	
	//拼托或拆托操作
	public WebResponse DisOperation(String userid,String domain,String site,String scan,String snCode,String cust_part,String scanType,String scan_status,String m_seq,String qty,String detqty, String edition) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("scan", scan).addParam("snCode", snCode).addParam("cust_part", cust_part)
				.addParam("scanType", scanType).addParam("scan_status", scan_status)
				.addParam("m_seq", m_seq).addParam("qty", qty).addParam("detqty", detqty)
				.addParam("edition", edition).execRtn("DisOperation");
		return wrp;
	}
	//提交数据
	public WebResponse DisSub(String domain, String site,String usertype,String userid,String uuid,String code,String json,String jsonremove,String jsonsnacoderemove,String type,String bnum) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("usertype", usertype).addParam("userid", userid)
				.addParam("uuid", uuid).addParam("code", code).addParam("json",json).addParam("jsonremove",jsonremove).addParam("jsonsnacoderemove",jsonsnacoderemove).addParam("type",type).addParam("bnum",bnum)
				.execRtn("getspellSub");
		return wrp;
	}	
	
	//提交 __Marin
	public WebResponse DisInforSub(String userid,String domain, String site,String m_seq ,String scan,String mult_qty,String qty,String strscan,String scan_status,String delall,String detqty) {
		WebResponse wrp = getWcuCP().addParam("userid", userid).addParam("domain", domain).addParam("site", site).addParam("m_seq", m_seq)
									.addParam("scan", scan).addParam("mult_qty", mult_qty).addParam("qty", qty)
									.addParam("strscan", strscan).addParam("scan_status", scan_status)
									.addParam("delall", delall).addParam("detqty", detqty).execRtn("DisInforSub");
		return wrp;
	}
	
}
