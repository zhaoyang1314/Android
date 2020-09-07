package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class PsiViewBiz extends BaseBiz{

	//获取自检数据信息
	public WebResponse getPsiList(String domain, String site,String userid,String part) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("part",part)
				.execRtn("getPsiList");
		return wrp;
	}

	
	//将自检的数据信息保存到自检记录表中去
	public WebResponse savePsiList(String domain, String site,String userid,String explaintxt,String operenv,String part,String wo,String line,String op,String shift,String edition,String custpart,String jsonStr,
			 String tiaoma, String woukey, String time, String num, String operenvnm) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid",userid).addParam("explaintxt", explaintxt)
				.addParam("operenv", operenv).addParam("part",part).addParam("wo",wo).addParam("line",line)
				.addParam("op",op).addParam("shift",shift).addParam("edition", edition).addParam("custpart", custpart)
				.addParam("jsonStr",jsonStr).addParam("tiaoma",tiaoma).addParam("woukey",woukey)
				.addParam("time",time).addParam("num",num).addParam("operenvnm",operenvnm).execRtn("savePsiList");
		return wrp;
	}



}
