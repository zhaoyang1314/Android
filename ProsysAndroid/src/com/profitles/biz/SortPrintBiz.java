package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;

public class SortPrintBiz extends BaseBiz {

	//根据销售发货通知单
	public List<Map<String, Object>> getSortList(String domain,String site,String userId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("userId", userId)
				.execRtn("getSortByNbr");
		return wr.getDataToList();
	}
	
	//根据检验是否可打印
	public WebResponse getIsPrint(String domain,String site,String nbr,String userId , String macId) {
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId)
				.execRtn("getIsPrint");
		return wr;
	}
	
	//打印，并由参数控制是否发货
	public WebResponse ptAdConfirm(String domain,String site,String nbr,String userId , 
			String macId,String appName,String appvVer,String isQad,String fnceffdate){
		WebResponse wr = getWcuCP().addParam("domain", domain).addParam("site", site).addParam("nbr", nbr)
				.addParam("userId", userId).addParam("macId", macId).addParam("appName", appName)
				.addParam("appvVer", appvVer).addParam("isQad", isQad).addParam("fnceffdate", fnceffdate)
				.execRtn("ptAdConfirm");
		return wr;
	}
	
	
}
