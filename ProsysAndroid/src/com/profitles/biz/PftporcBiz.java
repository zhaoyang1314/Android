package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class PftporcBiz extends BaseBiz {
	
	//送货单单号
	public WebResponse pftPorcCnNbr(String domain,String site,String cnNbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("cnNbr", cnNbr)
				.execRtn("checkPftporcCnNbr");
		return wrp;
	}
	
	//送货单单号存在扫描记录 取消删除记录
	public WebResponse pftPorcCnNbrBydele(String domain,String site,String cnNbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("cnNbr", cnNbr)
				.execRtn("checkPorcCnNbrBydele");
		return wrp;
	}
		
	//扫码
	public WebResponse pftPorcCode(String domain,String site,String code,String cnNbr,String vend,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("code", code).addParam("cnNbr", cnNbr).addParam("vend", vend).addParam("userId", userId)
				.execRtn("checkPftporcCode");
		return wrp;
	}

	//校验仓储
	public WebResponse pftPorcLocBin(String domain,String site,String LocBin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("LocBin", LocBin)
				.execRtn("queryPftporcLocBin");
		return wrp;
	}		

	//保存数据
	public WebResponse PftporcSave(String domain, String site, String nbr, String vend, String code, String part,String lot, String unit,String scat, String sQty, 
			String loc,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("vend", vend).addParam("code", code)
				.addParam("part", part).addParam("lot", lot).addParam("unit", unit).addParam("scat", scat).addParam("sQty", sQty).addParam("loc", loc)
				.addParam("userId", userId)
				.execRtn("getPftporcSave");
		return wrp;
	}
	
	//提交数据
	public WebResponse PftporcSub(String domain, String site, String nbr, String vend, String userId,String fnceffdate,String apphUser,String reson) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr).addParam("vend", vend)
				.addParam("userId", userId).addParam("fnceffdate", fnceffdate).addParam("apphUser", apphUser).addParam("reson", reson)
				.execRtn("PftporcSub");
		return wrp;
	}

	
	
	//用户验证
	public WebResponse porcUserAuthentication(String domain, String site,String user,String password){
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain)
				.addParam("site", site)
				.addParam("user", user)
				.addParam("password",password)
				.execRtn("porcUserAuthentication");
		return wrp;
	}
}
