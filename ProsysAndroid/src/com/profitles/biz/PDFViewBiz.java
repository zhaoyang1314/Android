package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;

/**
 * 新版MES完工操作主界面服务层
 * wade
 * 2019/07/01
 * */
public class PDFViewBiz extends BaseBiz {
	
	// 页面初始化时加载加工单数据

	public WebResponse serPartInfo(String domain, String site, String part,String desc,String kh,String khjh) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site)
				.addParam("part", part).addParam("desc", desc).addParam("kh", kh).addParam("khjh", khjh)
				.execRtn("serPartInfo");
		return wrp;
	}
	
	    // 查询图纸文件
		public WebResponse getPartDraw(String domain,String site, String part){
			WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site",site)
					.addParam("part", part)
					.execRtn("seaPartDraw");
			return wrp;
		}
	
	public WebResponse serIQCReportInfo(String domain, String site, String part,String desc,String iqc,String cons) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site)
					.addParam("part", part).addParam("desc", desc).addParam("iqc", iqc).addParam("cons", cons)
					.execRtn("serIQCReportInfo");
			return wrp;
		}
	
	    // 查询图纸文件
			public WebResponse getIQCDraw(String domain,String site, String QUALITY_SHIPPER,String QUALITY_PART,String QUALITY_LINE,String QUALITY_CRE_TIME){
				WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site",site)
						.addParam("QUALITY_SHIPPER", QUALITY_SHIPPER).addParam("QUALITY_PART", QUALITY_PART)
						.addParam("QUALITY_LINE", QUALITY_LINE.toString()).addParam("QUALITY_CRE_TIME", QUALITY_CRE_TIME)
						.execRtn("serIQCReportDraw");
				return wrp;
			}
}
