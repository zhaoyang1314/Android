package com.profitles.biz;

import java.util.Date;

import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.params.ParamsBaseMgr;
import com.profitles.framwork.webservice.bean.UserBean;

public class LoginBiz extends BaseBiz {
	
	public Object initUserInfo(String uid){
		WebResponse wrp = getWcuCP().addParam("userId", uid)
				.execRtn("Login_GetAUser");
		 ApplicationDB.user = initUserBean(wrp);
		 return wrp;
	}

	public String login(String uid, String pwd, String domain, String loc,String datetime) {
		WebResponse wrp = getWcuCP().addParam("userId", uid).addParam("password", pwd)
				.addParam("code", "PROFITLES").addParam("loc", loc).addParam("domain", domain)
				.execRtn("Login_SyncLogin");
		if(ApplicationDB.user != null && wrp.isSuccess()){
			ApplicationDB.user.setLoginTime(new Date());
			ApplicationDB.user.setUserDmain(domain);
			ApplicationDB.user.setUserSite(wrp.getString("SITE", ""));
			ApplicationDB.user.setUserLoc(loc);
			ApplicationDB.user.setUserDate(datetime);
		}
		return wrp.getMessages();
	}
	
	private UserBean initUserBean(WebResponse wrp){
		if(!wrp.isSuccess()) return null;
		ParamsBaseMgr pbMgr = wrp.getWDParamsMgr();
		UserBean uBean = new UserBean();
		uBean.setUserId(pbMgr.getString("USER_ID",""));
		uBean.setUseFlag(pbMgr.getString("USE_FLAG",""));
		uBean.setUserName(pbMgr.getString("USER_NM",""));
		uBean.setUserStatus(pbMgr.getString("USER_STATUS",""));
		uBean.setUserType(pbMgr.getString("USER_TYPE",""));
		uBean.setSessionId(pbMgr.getInt("SESSIONID", 0));
		uBean.setLoginTime(new Date());
		uBean.setDomains(pbMgr.getArrayStr("USER_PRI_DOMAINS", ";"));
		uBean.setSites(pbMgr.getArrayStr("USER_PRI_SITES", ";"));
		uBean.setLocs(pbMgr.getArrayStr("USER_PRI_LOCS", ";"));
		return uBean;
	}

	public void initSysCtrl(String domain, String userSite) {
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", userSite)
				.execRtn("Sys_GetSysCtrl");
		if(!wrp.isSuccess()) return;
		ApplicationDB.Ctrl = wrp.getWDParamsMgr();
	}
	
/*	public void initPoStrategy(String domain, String userSite) {   //获取采购策略
		WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", userSite)
				.execRtn("Sys_GetPoStrategy");
		if(!wrp.isSuccess()) return;
		ApplicationDB.Ps = wrp.getWDParamsMgr();
	}	
*/
	public void initWocCtrl(String domain, String userSite) {   //获取生产管控策略
	WebResponse wrp = getWcuCP().addParam("domain", domain).addParam("site", userSite)
			.execRtn("Sys_GetWocCtrl");
		if(!wrp.isSuccess()) return;
		ApplicationDB.WoC = wrp.getWDParamsMgr();
	}	

	public void initLoclLoop(String userLoc,String domain, String userSite) {
		WebResponse wrp = getWcuCP().addParam("locarea", userLoc).addParam("domain", domain).addParam("site", userSite)
				.addParam("vend", "-").addParam("part", "-")
				.execRtn("Sys_GetLocFlow");
		if(!wrp.isSuccess()) return;
		ApplicationDB.LoclLoop = wrp.getWDParamsMgr();
	}
	
	public WebResponse getinfor(String domain,String locs,String fnceffdate,String istrue){
		WebResponse wrp = getWcuCP()
				.addParam("domain", domain)
				.addParam("locs",locs)
				.addParam("fnceffdate",fnceffdate)
				.addParam("istrue",istrue)
				.execRtn("getinfor");
		return wrp;
	}

	public void initSocCtrl(String doamin, String site) {
		WebResponse wrp = getWcuCP().addParam("domain", doamin).addParam("site", site)
				.execRtn("Sys_GetSocCtrl");
			if(!wrp.isSuccess()) return;
			ApplicationDB.SoC = wrp.getWDParamsMgr(); 
	}
}
