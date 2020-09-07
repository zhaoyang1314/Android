package com.profitles.biz;

import java.util.List;
import java.util.Map;

import android.R.string;

import com.profitles.framwork.activity.util.WebResponse;

public class PsterBiz extends BaseBiz {
	
	// pageload加载
	public WebResponse getPkList(String domain,String site,String userid){
		WebResponse wrp = getWcuCP().addParam("domain",domain)
				.addParam("site",site).addParam("userid",userid)
				.execRtn("PsterList");
		return wrp;
	}
	//验证单号是否存在，是否处于可操作状态
		public WebResponse PsterSearNbr(String domain, String site,String userid,String nbr) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("userid", userid)
					.addParam("nbr", nbr)
					.execRtn("PsterNbr");
			return wrp;
		}
	//扫标签 
	public WebResponse PsterSearScan(String domain,String site,String userid,String scan,String nbr) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("scan", scan).addParam("nbr", nbr)
				.execRtn("PsterSearScan");
		return wrp;
	}
	//  统计
	public WebResponse getPsterStatis(String domain,String site,String userid,String nbr){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("nbr", nbr)
				.execRtn("getPsterStatis");
		return wrp;
	}
	//  提交
	public WebResponse submitPsterStatis(String domain,String site,String userid,
			String nbr,String scan,String status,String part,String type,String seq){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("nbr", nbr).addParam("scan", scan)
				.addParam("status", status).addParam("part", part)
				.addParam("type", type).addParam("seq", seq)
				.execRtn("psterSubmit");
		return wrp;
	}
	// 查询
	public WebResponse selectXrskg(String domain,String site,String userid,
			String nbr,String scan,String status,String part,String type,String seq){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("nbr", nbr).addParam("scan", scan)
				.addParam("status", status).addParam("part", part)
				.addParam("type", type).addParam("seq", seq)
				.execRtn("selectXrskg");
		return wrp;
	}
	
	// 解除箱拖
	public WebResponse removeBox(String domain,String site,String userid,
			String nbr,String scan,String status,String part,String type,String seq) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("nbr", nbr).addParam("scan", scan)
				.addParam("status", status).addParam("part", part)
				.addParam("type", type).addParam("seq", seq)
				.execRtn("psterRemoveBox");
		return wrp;
	}
	
}
