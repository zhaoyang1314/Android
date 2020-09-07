package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class TrlBiz extends BaseBiz{
	//仓储
	public WebResponse trl_fmBinonBlur(String locbin,String domain,String site){
		WebResponse wrp = getWcuCP()
				.addParam("lockey",locbin)
				.addParam("domain", domain)
				.addParam("site",site)
				.execRtn("checkloc");
		return wrp;
	}
	
	//扫描的源储位
	public WebResponse trBinSonBlur(String locbin,String domain,String site){
		WebResponse wrp = getWcuCP()
				.addParam("lockey",locbin)
				.addParam("domain", domain)
				.addParam("site",site)
				.execRtn("checklocS");
		return wrp;
	}	
	//验证单件标签是否存在
	public WebResponse trl_CheckSnList(String domain,String site,String code,String type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code).addParam("type", type)
				.execRtn("getSnAll");
		return wrp;
	}
	
	//托标签
	public WebResponse trl_CheckBar(String domain,String site,String code) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.execRtn("checkspellBar");
		return wrp;
	}
	
	//提交
	public WebResponse trl_submit(String domain,String site,String fmbin,String fmloc,String tobin,String part
			,String lot,String scount,String um,String partname,String code,String vend,String userid,String fnceffdate,String ycLoc){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("fmbin",fmbin)
				.addParam("fmloc",fmloc)
				.addParam("tobin",tobin)
				.addParam("part",part)
				.addParam("lot",lot)
				.addParam("scount",scount)
				.addParam("um",um)
				.addParam("partname",partname)
				.addParam("code", code)
				.addParam("vend", vend)
				.addParam("userid", userid)
				.addParam("fnceffdate", fnceffdate)
				.addParam("type", "TrLActivity")
				.addParam("ycLoc", ycLoc)
				.execRtn("trSub");
		return wrp;	
	}
}
