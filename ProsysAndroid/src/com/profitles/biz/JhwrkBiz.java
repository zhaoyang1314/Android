package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class JhwrkBiz extends BaseBiz{
	
	//原因数据加载
		public WebResponse jhwrk_OnLoadReason(String domain){
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.execRtn("jhwrkOnLoadReason");
			return wrp;
		}
 	
	//扫码
	public WebResponse jhwrk_scan(String domain,String site,String scan){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("scan",scan)
				.execRtn("jhwrkScan");
		return wrp;
	}
	
	//仓储
	public WebResponse jhwrk_locbin(String locbin,String domain,String site){
		WebResponse wrp = getWcuCP()
				.addParam("locbin",locbin)
				.addParam("domain", domain)
				.addParam("site",site)
				.execRtn("jhwrkLocbin");
		return wrp;
	}
	
	//验证供应商是否存在
	public WebResponse jhwrk_vend(String domain,String userid,String vend){
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain)
				.addParam("userid",userid)
				.addParam("vend",vend)
				.execRtn("jhwrkVend");
		return wrp;
	}
	
	//零件
	public WebResponse jhwrk_part(String domain,String site,String vend,String part){
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain)
				.addParam("site",site)
				.addParam("vend",vend)
				.addParam("part",part)
				.execRtn("jhwrkPart");
		return wrp;
	}
	
	//提交
	public WebResponse jhwrk_submit(String domain,String site,String loc,String bin,String part
			,String lot,String scount,String um,String desc,String scan,String vend,String userid
			,String rsg,String rfptv_rcvloc_stu,String scat,String unit,String box,String fnceffdate){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site",site)
				.addParam("loc",loc)
				.addParam("bin",bin)
				.addParam("part",part)
				.addParam("lot",lot)
				.addParam("scount",scount)
				.addParam("um",um)
				.addParam("desc",desc)
				.addParam("scan", scan)
				.addParam("vend", vend)
				.addParam("userid", userid)
				.addParam("rsg", rsg)
				.addParam("rfptv_rcvloc_stu", rfptv_rcvloc_stu)
				.addParam("scat", scat)
				.addParam("unit", unit)
				.addParam("box", box)
				.addParam("fnceffdate", fnceffdate)
				.execRtn("jhwrkSub");
		return wrp;	
	}
}
