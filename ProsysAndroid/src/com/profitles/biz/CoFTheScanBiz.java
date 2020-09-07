package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

public class CoFTheScanBiz extends BaseBiz {

	
	// 条码
	public WebResponse cof_scan(String domain, String site,String userid, String scan,String nbr,
			String desc,String part,String line,String chejian,String cpart,String ukey
			) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("scan", scan).addParam("nbr",nbr)
				.addParam("desc",desc).addParam("part", part)
				.addParam("line", line).addParam("chejian", chejian)
				.addParam("cpart", cpart).addParam("ukey", ukey)
				.execRtn("cofScan");
		return wrp;
	}

	// 提交String domain, String site, String userid,String scan,String nbr,String desc,String type,String num,String part,
	//String shift,String ukey,String wkctr,String line,String time
	
	public WebResponse cof_submit(String domain, String site, String userid,String scan,String nbr,String desc,String type,String num,String part,
			String shift,String ukey,String wkctr,String line,String time,String partdesc
			
		) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("scan", scan).addParam("nbr",nbr)
				.addParam("desc",desc).addParam("type",type)
				.addParam("num", num).addParam("part", part)
				.addParam("shift", shift).addParam("ukey", ukey)
				.addParam("wkctr", wkctr).addParam("line", line)
				.addParam("time", time).addParam("partdesc", partdesc)
				.execRtn("cofSubmit");
		return wrp;
	}
	
	//登录界面获取员工信息
	public WebResponse cof_getInfo(String domain,String userid,String site){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.execRtn("cofGetInfo");
		return wrp;
	}
	
	//获取加工单工序
	public WebResponse cof_getOp(String domain,String site,String userid,String nbr,String part,String tmp_qclot_wo_ukey){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("nbr", nbr).addParam("part", part)
				.addParam("ukey", tmp_qclot_wo_ukey)
				.execRtn("cofGetOp");
		return wrp;
	}
	//查询开工记录
	public WebResponse cofGetRecord(String domain,String site,String userid,String line){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("line", line)
				.execRtn("cofGetRecord");
		return wrp;
	}
	
	//获取生产线
	public WebResponse cofGetLine(String domain,String site,String userid){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
			
				.execRtn("cofGetLine");
		return wrp;
	}
	//查找产品是的管理类型
	public WebResponse cofManageType(String domain,String site,String part){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("part", part)
			
				.execRtn("cofManageType");
		return wrp;
	}
	//进行弹出框确认按钮操作 domain, userId, site, ukey, num, part, scan, nbr
	public WebResponse cofConfirm(String domain,String userId, String site,String ukey,String num,
			String part,String scan,String nbr){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("userId", userId).addParam("site", site)
				.addParam("ukey", ukey).addParam("num", num)
				.addParam("part", part).addParam("scan", scan)
				.addParam("nbr", nbr)
				.execRtn("confirmMenthod");
		return wrp;
	}
	
}
