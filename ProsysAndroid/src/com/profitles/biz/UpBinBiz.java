package com.profitles.biz;

import java.util.List;
import java.util.Map;

import com.profitles.activity.R.string;
import com.profitles.framwork.activity.util.WebResponse;


public class UpBinBiz extends BaseBiz {
	
	
	

	public Map<String, String> getInitialNb() {
		// TODO Auto-generated method stub
		return null;
	}
	/**扫码是触发的方法*/
	public WebResponse anlBarcode(String barcode, String domain, String site) {
		// TODO Auto-generated method stub
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site)
				.addParam("barcode", barcode)
				.execRtn("anlBarcode");
		return wrp;
	}
	/**验证库位是否存在（源储、至储）*/
	public WebResponse checkloc(String lockey,String domain,String site) {
		// TODO Auto-generated method stub
		WebResponse wrp = getWcuCP().addParam("lockey", lockey)
				.addParam("domain", domain)
				.addParam("site", site)
				.execRtn("checkloc");
		return wrp;
	}
	/**点击保存按钮是保存数据*/
	public WebResponse upBinSave(String domain, String site, String fLoc, String tLoc, String lot, String part, String tBin, 
			String vend, String scan, String qty, String statue, String um, String userId, String boxQ, String desc,String fnceffdate,String tray) {
		// TODO Auto-generated method stub
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site)
				.addParam("fLoc", fLoc)
				.addParam("tLoc", tLoc)
				.addParam("lot", lot)
				.addParam("part", part)
				.addParam("tBin", tBin)
				.addParam("vend", vend)
				.addParam("scan", scan)
				.addParam("sqty", qty)
				.addParam("statue", statue)
				.addParam("um", um)
				.addParam("boxQ", boxQ)
				.addParam("desc", desc)
				.addParam("userId", userId)
				.addParam("fnceffdate ", fnceffdate)
				.addParam("tray ", tray)
				.execRtn("upbinSave");
		return wrp;
	}
	/**验证用户名的存在*/
	public WebResponse checkUser(String user) {
		WebResponse wrp = getWcuCP().addParam("user", user)
				.execRtn("checkUser");
		return wrp;
	}
	/** 验证至储正确性*/
	public Object checkbin(String domain, String site, String tLoc, String tBin, String part, String vend, String lot, String sqty ) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site)
				.addParam("tLoc", tLoc)
				.addParam("lot", lot)
				.addParam("part", part)
				.addParam("vend",vend)
				.addParam("tBin", tBin)
				.addParam("sqty", sqty)
				.execRtn("checkbin");
		return wrp;
	}
	public Object getQtyByLoc(String domain, String site, String part,
			String lot, String vend, String fLoc, String fBin) {
		WebResponse wrp = getWcuCP().addParam("domain", domain)
				.addParam("site", site)
				.addParam("vend", vend)
				.addParam("lot", lot)
				.addParam("part", part)
				.addParam("fLoc", fLoc)
				.addParam("fBin", fBin)
				.execRtn("getQtyByLoc");
		return wrp;
	}
	
	/**负荷率*/
	public WebResponse CapCalculation(String part ,String domain,String site,String tobin,String qty,String kcqty) {
		WebResponse wrp = getWcuCP()
				.addParam("part", part).addParam("domain", domain)
				.addParam("site", site).addParam("tobin", tobin)
				.addParam("qty", qty).addParam("kcqty", kcqty)
				.execRtn("CapCalculation");
		return wrp;
	}
}
