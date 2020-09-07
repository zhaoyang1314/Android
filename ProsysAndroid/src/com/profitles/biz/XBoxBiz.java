package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class XBoxBiz extends BaseBiz {

	
	public WebResponse xBoxBar(String domain,String site ,String code) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code)
				.execRtn("getXBoxBar");
		return wr;
	}
	
	public WebResponse xBoxSub(String domain,String site ,String code,String lot ,String part,String surplus ,String splitBox,String userId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("code", code).addParam("lot", lot).addParam("part", part)
				.addParam("surplus", surplus).addParam("splitBox", splitBox).addParam("userId", userId)
				.execRtn("getXBoxSub");
		return wr;
	}

}
