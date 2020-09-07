package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;

/**
 * 新版MES完工操作主界面服务层
 * wade
 * 2019/07/01
 * */
public class WoListViewBiz extends BaseBiz {
	
	// 页面初始化时加载加工单数据

	public WebResponse seaWoInfo(String domain, String site, String userid, String line) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("line", line)
				.execRtn("seaWoInfo");
		return wrp;
	}
	
	// 判断产线是否存在开工记录
	public WebResponse cofGetLineById(String domain, String site, String userId, String XSWO_UKEY, 
			String XSWO_NBR, String XSWO_PART, String XSWO_REL_DATE, String XSWO_OP, String XSWO_REV,
			String XSWO_PART_NM, String XSWO_QTY_ORD, String XSWO_QTY_COMP,String line){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site)
				.addParam("userId", userId)
				.addParam("XSWO_UKEY", XSWO_UKEY)
				.addParam("XSWO_NBR", XSWO_NBR)
				.addParam("XSWO_PART", XSWO_PART)
				.addParam("XSWO_REL_DATE", XSWO_REL_DATE)
				.addParam("XSWO_OP", XSWO_OP)
				.addParam("XSWO_REV", XSWO_REV)
				.addParam("XSWO_PART_NM", XSWO_PART_NM)
				.addParam("XSWO_QTY_ORD", XSWO_QTY_ORD)
				.addParam("XSWO_QTY_COMP", XSWO_QTY_COMP)
				.addParam("line", line)
				.execRtn("cofGetLineById");
		return wrp;
	}
	
	// 判断某加工单是否开工
	public WebResponse cofGetLineIsBg(String XSWO_NBR,String XSWO_UKEY,String domain,String site,String userid,String line){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("XSWO_NBR", XSWO_NBR)
				.addParam("XSWO_UKEY", XSWO_UKEY)
				.addParam("site", site).addParam("userid", userid)
				.addParam("line", line)
				.execRtn("cofGetLineIsBg");
		return wrp;
	}
	
	// 修改加工单信息和生产线状态
	public WebResponse updateRfqclot(String domain,String userid,String site,String xswo_ukey){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("userid", userid)
				.addParam("site", site)
				.addParam("xswo_ukey", xswo_ukey)
				.execRtn("updateRfqclot");
		return wrp;
	}
	
	// 查询开工记录
	public WebResponse getLineState(String domain, String site,String line) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("line",line)
					.execRtn("getLineState");
			return wrp;
		}
	
}
