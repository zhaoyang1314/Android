package com.profitles.biz;


import com.profitles.framwork.activity.util.WebResponse;

public class CpdxBiz extends BaseBiz {
	
	//扫描目标标签
	public WebResponse checkTargetScan(String domain,String site,String scan,String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("scan", scan).addParam("userId", userId)
				.execRtn("checkTargetScan");
		return wrp;
	}
	
	//获取目标标签下的子标签信息
	public WebResponse getSnList(String domain,String site,String scan) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("scan", scan).execRtn("getSnList");
		return wrp;
	}
	
	// 检查子件标签
	public WebResponse checkSonScan(String domain,String site,String tarScan, String sonScan,
			String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("tarScan", tarScan).addParam("sonScan", sonScan).addParam("userId", userId)
				.execRtn("checkSonScan");
		return wrp;
	}
	
	// 检查是否拼满
	public WebResponse checkBoxQty(String domain,String site,String tarScan, String sonScan,
			String userId) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("tarScan", tarScan).addParam("sonScan", sonScan).addParam("userId", userId)
				.execRtn("checkBoxQty");
		return wrp;
	}
	
	// 提交
	public WebResponse submit(String domain, String site, String tarScan, String userId,
			String etx_num) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("tarScan", tarScan).addParam("userId", userId)
				.addParam("etx_num", etx_num)
				.execRtn("cpdxSubmit");
		return wrp;
	}

	
	// 检查源标签
	public WebResponse checkSourceScan(String domain,String site,String tarScan, String sourceScan,
			String userId, String type) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("tarScan", tarScan).addParam("sourceScan", sourceScan).addParam("userId", userId)
				.addParam("type", type).execRtn("checkSourceScan");
		return wrp;
	}
	

	// 保存
	public WebResponse save(String domain, String site, String tarScan, String userId, String etx_SourceScan,
			String etx_SplitNum) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", site)
				.addParam("tarScan", tarScan).addParam("userId", userId)
				.addParam("etx_SourceScan", etx_SourceScan).addParam("etx_SplitNum", etx_SplitNum)
				.execRtn("cpdxSave");
		return wrp;
	}
}
