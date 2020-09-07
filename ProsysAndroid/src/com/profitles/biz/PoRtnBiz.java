package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;


public class PoRtnBiz extends BaseBiz {
	// 扫描拼拆托打印出来的标签
		public WebResponse searchScan(String domain, String site, String scan) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("scan", scan)
					.execRtn("searchRflotScan");
			return wrp;
		}
  //进行采购单号验证String domain, String site, String pono,String part
		public WebResponse searchPoNo(String domain, String site, String ponbr,String part) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("ponbr", ponbr)
					.addParam("part", part)
					.execRtn("searchPoNo");
			return wrp;
		}
	//根据输入的采购单行号进行相关信息查询
		public WebResponse searchPoLineInfo(String domain, String site, String ponbr,String poline) {
			WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("ponbr", ponbr).addParam("poline",poline).execRtn("searchPoLineInfo");
			return wrp;
		}
	//提交处理
		public WebResponse submit(String domain, String site, String ponbr,String poline,String recqty,String orrtnqty,String rtnqty,String scan,String bin,String loc,String part,String lot,String vend,String um,String userid) {
			WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("ponbr", ponbr).addParam("poline",poline)
					.addParam("recqty", recqty).addParam("orrtnqty", orrtnqty).addParam("rtnqty", rtnqty).addParam("scan", scan)
					.addParam("bin", bin).addParam("loc", loc).addParam("part", part).addParam("lot", lot).addParam("vend", vend)
					.addParam("um", um).addParam("userid", userid).execRtn("submit");
			return wrp;
		}
}
