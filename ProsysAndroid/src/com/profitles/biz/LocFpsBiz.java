package com.profitles.biz;

import android.provider.Telephony.Mms.Addr;

import com.profitles.framwork.activity.util.WebResponse;

public class LocFpsBiz extends BaseBiz {
 
	/**扫码操作进行验证*/
	public WebResponse saveLocFpsBar(String domain,String site, String nbr,String userId ,String fnceffdate, String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("fnceffdate", fnceffdate).addParam("macId", macId)
				.execRtn("getLocInfo");
		return wr;
	}
	 
}
