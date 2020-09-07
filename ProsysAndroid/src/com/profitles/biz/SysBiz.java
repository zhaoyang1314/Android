package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class SysBiz extends BaseBiz {

	public WebResponse getUpdNewVer() {
		WebResponse wr = getWcuCP().addParam("key", "'PFT_ALES_VERSION','PFT_ALES_URL','PFT_ALES_NAME'").execRtn("getUpdNewVer");
		return wr;
	}
}
