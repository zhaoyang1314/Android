package com.profitles.biz;

import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import com.profitles.framwork.activity.util.WebResponse;

/**
 * 交接项
 * Tyler
 * 2019/07/16
 * */
public class HandoverItemBiz extends BaseBiz {
	
	// 查询交接项
	public WebResponse handoverItem(String domain, String site,String line) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("line",line)
				.execRtn("handoverItem");
		return wrp;
	}
	
	// 新增交接参数
	public WebResponse confirmButton(String domain, String site,String userid,String line,
				String jsonStr,String xswo_ukey){
			    WebResponse wrp = getWcuCP().addParam("domain", domain)
			    .addParam("site", site).addParam("userid", userid).addParam("line",line)
				.addParam("jsonStr",jsonStr).addParam("xswo_ukey",xswo_ukey)
				.execRtn("confirmButton");
			    return wrp;
		}
		//意见项修改
	public WebResponse updateOpin(String domain, String site,String line,String opin){
			    WebResponse wrp = getWcuCP().addParam("domain", domain)
			    .addParam("site", site).addParam("line",line)
				.addParam("opin",opin)
				.execRtn("updateOpin");
			    return wrp;
		}
	
	// 查询交接项
		public WebResponse getItemValue(String domain, String site,String line) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("line",line)
					.execRtn("getItemValue");
			return wrp;
		}
}
