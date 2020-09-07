package com.profitles.biz;

import com.profitles.framwork.activity.util.WebResponse;

/**
 * 新版MES完工操作主界面服务层
 * wade
 * 2019/07/01
 * */
public class FinshOpScanBiz extends BaseBiz {
	
	// 根据加工单以及唯一查询加工单信息
	public WebResponse seaPlanInfo(String domain, String site,String userid, String nbr, String ukey) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("nbr",nbr).addParam("ukey",ukey)
				.execRtn("seaPlanInfo");
		return wrp;
	}
	
	// 查询产线是否免检
		public WebResponse seaCxInfo(String domain, String site, String line) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site)
					.addParam("line",line)
					.execRtn("seaCxInfo");
			return wrp;
		}
	// 扫描标签信息
	public WebResponse seaScanInfo(String domain, String site,String userid, String scan, String op, String line, String ukey, String part) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("userid", userid)
				.addParam("scan", scan).addParam("op", op)
				.addParam("line", line).addParam("ukey", ukey)
				.addParam("part", part).execRtn("seaScanInfo");
		return wrp;
	} 
	
	// 查询图纸文件
	public WebResponse getPartDraw(String domain,String site, String part){
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site",site)
				.addParam("part", part)
				.execRtn("seaPartDraw");
		return wrp;
	}
	
	
	// 进行关单操作
		public WebResponse closeWo(String domain, String site, String ukey, String nbr){
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("ukey", ukey)
					.addParam("nbr", nbr).execRtn("closeWo");
			return wrp;
		}
		// 获取在检数据
		public WebResponse getCountCheckinfo(String domain, String site,String state, String line) {
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("state", state)
					.addParam("line", line).execRtn("getCountCheckinfo");
			return wrp;
		} 
	// 进行加工单下线入库操作
		public WebResponse finshOPMethod(String domain, String site,String userid, String tiaoma,String nbr,String part,String woukey,String time,String qty, String opdesc){
			WebResponse wrp = getWcuCP().addParam("domain", domain)
					.addParam("site", site).addParam("userid", userid)
					.addParam("tiaoma", tiaoma).addParam("nbr", nbr)
					.addParam("part", part).addParam("woukey", woukey)
					.addParam("time", time).addParam("qty", qty)
					.addParam("opdesc", opdesc).execRtn("finshOP");
			return wrp;
		}
		
		
		/**
		    * 查询检验计划图纸CheckPlan文件
		    * Jack
		    * @param domain
		    * @param wonbr
		    * @param ukey
		    * @param lineOP
		    * @return
		    */
			public WebResponse getTechnologyDraw(String domain,String site, String part){
				WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site",site)
						.addParam("part", part)
						.execRtn("getTechnologyDraw");
				return wrp;
			}

			/**
			 * 查询指导书图纸文件
			 * Jack
			 * @param domain
			 * @param wonbr
			 * @param ukey
			 * @param lineOP
			 * @return
			 */
			public WebResponse getGuidanceDraw(String domain,String site, String part){
				WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site",site)
						.addParam("part", part)
						.execRtn("getGuidanceDraw");
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
			
	// 查询选择的工序是否是入库工序
			public WebResponse opIsNotRemark(String domain,String opdesc,String tmp_qclot_wo_ukey){
				WebResponse wrp = getWcuCP().addParam("domain", domain)
						.addParam("opdesc", opdesc).addParam("tmp_qclot_wo_ukey", tmp_qclot_wo_ukey)
						.execRtn("searchOpRound");
				return wrp;
			}
			
}
