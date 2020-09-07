package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.framwork.activity.util.WebResponse;


public class UpBinPrsBiz extends BaseBiz {
	
	/**扫描标签*/
	public WebResponse anlBarcodePrs( String domain, String site ,String user,String barcode) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("site", site)
									.addParam("user", user)
									.addParam("barcode", barcode)
									.execRtn("anlBarcodePrs");
		return wrp;
	}
	/**验证扫描的是库位还是储位*/
	public WebResponse checkLocBin(String lockey,String domain,String site) {
		// TODO Auto-generated method stub
		WebResponse wrp = getWcuCP().addParam("lockey", lockey)
				.addParam("domain", domain)
				.addParam("site", site)
				.execRtn("checkloc");
		return wrp;
	}
	/**保存数据*/
	public WebResponse upbinPrsSave(String domain, String site, String jsonStr , String userId,String tLoc,String tBin, String statue,String fnceffdate) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("json",jsonStr)
				.addParam("userId", userId).addParam("tLoc",tLoc).addParam("tBin", tBin)
				.addParam("statue", statue).addParam("fnceffdate", fnceffdate)
				.execRtn("upbinPrsSave");
		return wrp;
	}
	/**获取建议储位*/
	public WebResponse getAdv_bin(String domain, String site, String jsonStr,String rfptv_bin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site).addParam("json",jsonStr)
				.addParam("rfptv_bin", rfptv_bin)
				.execRtn("getAdv_bin");
		return wrp;
	}
	//写入扫描作业表
	public WebResponse createWkfl(String NUM_LBL,String RFLOT_SCAN,String RFLOT_PART,String Lot,String CQTY,String QTY,String fBin,String user,String domain,String site,String vend,String RFLOT_UM,String status,String RFPTV_LBS,String RFPTV_MULT_BOX,String RFLOT_BOX_QTY,String RFLOT_SCATTER_QTY){
		WebResponse wrp = getWcuCP().addParam("RFLOT_SCAN", RFLOT_SCAN)
				.addParam("RFLOT_NUM_LBL", NUM_LBL)
				.addParam("RFLOT_PART", RFLOT_PART)
				.addParam("Lot",Lot).addParam("CQTY", CQTY)
				.addParam("QTY", QTY)
				.addParam("fBin", fBin).addParam("user",user)
				.addParam("domain",domain).addParam("site",site)
				.addParam("vend",vend).addParam("RFLOT_UM",RFLOT_UM)
				.addParam("status",status).addParam("RFPTV_LBS", RFPTV_LBS)
				.addParam("RFPTV_MULT_BOX", RFPTV_MULT_BOX).addParam("RFLOT_BOX_QTY", RFLOT_BOX_QTY)
				.addParam("RFLOT_SCATTER_QTY", RFLOT_SCATTER_QTY)
				.execRtn("createWkfl");
		return wrp;
	}
	//上架查询存在未提交数据
	public WebResponse getScannedInfo(String domain,String user, String Table,String status){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("user", user)
									.addParam("Table", Table)
									.addParam("status", status)
									.execRtn("getScannedInfo");
		return wrp;
	}
	//清除存在未提交数据
	public WebResponse deleteScan(String domain,String user, String Table,String status,String scan){
			WebResponse wrp = getWcuCP().addParam("domain", domain)
										.addParam("user", user)
										.addParam("Table", Table)
										.addParam("status", status)
										.addParam("scan", scan)
										.execRtn("deleteScan");
			return wrp;
	}
	//清除存在未提交数据
	public WebResponse deleteScanned(String domain,String user, String Table,String status){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("user", user)
									.addParam("Table", Table)
									.addParam("status", status)
									.execRtn("deleteScanned");
		return wrp;
	}
	//判断当前的条码是否被他人扫描上架中
	public WebResponse getOtherScanned(String domain ,String user, String RFLOT_SCAN,String Table,String status){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("user", user)
									.addParam("RFLOT_SCAN", RFLOT_SCAN)
									.addParam("Table", Table)
									.addParam("status", status)
									.execRtn("getOtherScanned");
		return wrp;
	}
	
	//判断上架策略
	public WebResponse getStrategy(String domain, String site){
		WebResponse wrp = getWcuCP().addParam("domain", domain)
									.addParam("site", site)
									.execRtn("getStrategy");
		return wrp;
	}
	public Object getQtyByLoc(String domain, String site, String part,String lot, String vend, String fLoc, String fBin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site)
				.addParam("vend", vend)
				.addParam("lot", lot)
				.addParam("part", part)
				.addParam("fLoc", fLoc)
				.addParam("fBin", fBin)
				.execRtn("getQty");
		return wrp;
	}
	//扫描源储获取零件库存
	public Object getStock(String bin,String part,String lot,String domain,String site,String vend,String ref){
		WebResponse wrp = getWcuCP().addParam("bin", bin)
				.addParam("part", part)
				.addParam("lot", lot)
				.addParam("domain", domain)
				.addParam("site", site)
				.addParam("vend", vend)
				.addParam("ref", ref)
				.execRtn("getStock");
		return wrp;
	}
}
