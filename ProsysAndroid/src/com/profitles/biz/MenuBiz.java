package com.profitles.biz;

import java.util.List;
import java.util.Map;

public class MenuBiz extends BaseBiz {

	public List<Map<String, Object>> getMenuByUid(String menuId, String userId,String domain) {
		return getWcuCP().addParam("menuId", menuId).addParam("userId", userId).addParam("domain", domain)
				.execRtn("getMenuByUserForAd").getDataToList();
	}
}
