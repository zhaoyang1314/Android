package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class QcpickBiz extends BaseBiz {
	//单号
	public WebResponse qcpick_nbr(String domain,String site,String nbrpd,String userId,String macId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbrpd)
				.addParam("userId",userId)
				.addParam("macId",macId)
				.execRtn("qcpickNbr");
		return wrp;		
	}
	//扫码
	public WebResponse qcpick_scan(String domain,String site,String code,String nbr,String loc){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("code",code)
				.addParam("nbr", nbr)
				.addParam("loc",loc)
				.execRtn("qcpickCode");
		return wrp;
	}
	//保存
	public WebResponse qcpick_save(String domain,String site,String nbr,String locbin,String partn,String lotr,String nums,String scan,String actpartumString) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("locbin",locbin)
				.addParam("partn",partn)
				.addParam("lotr",lotr)
				.addParam("nums",nums)
				.addParam("scan",scan)
				.addParam("actpartumString",actpartumString)
				.execRtn("qcpickSave");
		return wrp;		
	}
	//提交
	public WebResponse qcpick_submit(String domain,String site,String nbr,String locbin,String partn
			,String lotr,String nums,String userid,String uuid,String actpartumString,String code,String fnceffdate){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("locbin",locbin)
				.addParam("partn",partn)
				.addParam("lotr",lotr)
				.addParam("nums",nums)
				.addParam("userid",userid)
				.addParam("uuid", uuid)
				.addParam("actpartumString", actpartumString)
				.addParam("code", code)
				.addParam("fnceffdate", fnceffdate)
				.execRtn("qcpickSub");
		return wrp;	
	}
	//返回
	public WebResponse qcpick_return(String domain , String site , String nbr ,
			String userId , String macId){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("domain",domain)
				.addParam("site",site)
				.addParam("nbr",nbr)
				.addParam("userid",userId)
				.addParam("uuid",macId)
				.execRtn("qcpickret");
		return wrp;	
	}
}
