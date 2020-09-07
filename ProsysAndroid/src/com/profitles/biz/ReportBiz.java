package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class ReportBiz extends BaseBiz {
	//保存提交数据
	public WebResponse getConsInfo(String etaDate) {
		WebResponse wrp = getWcuCP().addParam("etaDate", etaDate).execRtn("getConsInfo");
		return wrp;
	}	
}
