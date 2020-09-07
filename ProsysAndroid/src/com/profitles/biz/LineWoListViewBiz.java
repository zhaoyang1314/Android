package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

/**
 * 生产线界面
 * Tyler
 * 2019/07/09
 * */
public class LineWoListViewBiz extends BaseBiz {
	
	// 查询生产线
	public WebResponse LineButton(String domain, String site,String userid) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.execRtn("LineButton");
		return wrp;
	}
	//查询交接项记录
	public WebResponse getCount(String domain, String site,String line) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("line", line)
				.execRtn("getCount");
		return wrp;
	}

}
