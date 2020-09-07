package com.profitles.biz;

import android.R.string;

import com.profitles.framwork.activity.util.WebResponse;

public class UpStatusBiz extends BaseBiz {

	/*检验条码*/

	public WebResponse upStatus(String domain, String site, String scan,String type,String user,String cangcu,String isPLcz,String userId) {
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain).addParam("site", site)
				.addParam("scan", scan).addParam("type",type) .addParam("user", user).addParam("cangcu", cangcu).addParam("isPLcz", isPLcz)
				.addParam("userId", userId)
				.execRtn("upStatus");
		return wrp;
	}
	/*验证条码是否存在，如果存在则自动带出仓储*/
	
	public WebResponse isCzBar(String domain, String scan) {
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain).addParam("scan", scan)
				.execRtn("isCzBar");
		return wrp;
	}

}
