package com.profitles.biz;


import java.util.Map;
import org.json.JSONArray;
import com.profitles.framwork.activity.util.WebResponse;

/**
 * 在检
 * Tyler
 * 2019/07/18
 * */
public class CheckInfoBiz extends BaseBiz {
	
	// 查询已检参数
	public WebResponse getCheckInfo(String domain, String site,String state, String line) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("state", state)
				.addParam("line", line).execRtn("getCheckInfo");
		return wrp;
	}
	// 修改状态
	public WebResponse updateCheckState(String domain, String site,String nbr,String state){
			    WebResponse wrp = getWcuCP().addParam("domain", domain)
			    .addParam("site", site).addParam("nbr", nbr).addParam("state", state)
				.execRtn("updateCheckState");
			    return wrp;
		}
	
	// 查询三坐标报告
	public WebResponse checkReport(String domain, String site,String path_nm){
	    WebResponse wrp = getWcuCP().addParam("domain", domain)
	    .addParam("site", site).addParam("path_nm", path_nm)
		.execRtn("checkReport");
	    return wrp;
}
}
