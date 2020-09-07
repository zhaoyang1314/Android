package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class SelfiViewBiz extends BaseBiz {

	public WebResponse getSelfItem(String domain, String site,String userid) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("userid", userid)
				.execRtn("getSelfItem");
		return wrp;
	}
	
	
	
	public WebResponse getSelfItems(String domain, String site,String userid) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("userid", userid)
				.execRtn("getSelfItems");
		return wrp;
	}
	
	
	public WebResponse getPsiSelfItem(String domain, String site,String userid,String part,String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("part",part).addParam("nbr",nbr)
				.execRtn("getPsiSelfItem");
		return wrp;
	}



	public WebResponse getSelfScan(String domain, String site, String userid,
			String scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("scan",scan)
				.execRtn("getSelfScan");
		return wrp;
	}



	public WebResponse getSelfSave(String domain, String site, String userid,
			String date,
			String scan, String part, String users, String dates, String cust,
			String desc, String outslt, String sizeslt, String slt, String result,String nbr,String fist, String chktype, String sn, String checkbox) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("date", date)
				.addParam("scan",scan).addParam("part",part)
				.addParam("users",users).addParam("dates",dates)
				.addParam("cust",cust).addParam("desc",desc)
				.addParam("outslt",outslt).addParam("sizeslt",sizeslt)
				.addParam("slt",slt).addParam("result",result)
				.addParam("nbr",nbr).addParam("fist",fist)
				.addParam("chktype",chktype).addParam("sn",sn)
				.addParam("checkbox",checkbox).execRtn("getSelfSave");
		return wrp;
	}

	// 获取检验的拆分类型
	public WebResponse getRscanType(String domain, String type){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("type", type).execRtn("getRscanType");
		return wrp;
	}
	
	// 获取检测机型
		public WebResponse getCheckType(String domain){
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					  .execRtn("getCheckType");
			return wrp;
		}
		// 查询三坐标报告
		public WebResponse checkReport(String domain, String site,String path_nm){
		    WebResponse wrp = getWcuCP().addParam("domain", domain)
		    .addParam("site", site).addParam("path_nm", path_nm)
			.execRtn("checkReport");
		    return wrp;
		}
		
	// 修改送检记录的二次检验结果
		
		public WebResponse updateScdResult(String domain, String site,String sn, String scan, String result){
		    WebResponse wrp = getWcuCP().addParam("domain", domain)
		    .addParam("site", site).addParam("sn", sn)
			.addParam("scan", scan).addParam("result", result)
			.execRtn("updateScdResult");
		    return wrp;
		}
}
