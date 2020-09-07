package com.profitles.framwork.webservice.bean;

import java.util.Date;

public class UserBean {
	
	private String userId;
	private String userName;
	private String userMail;
	private Date loginTime;
	private String mac;
	private int sessionId;
	private String userType; 
	private String userStatus;
	private String useFlag;
	private String userDmain;
	private String userSite;
	private String userFactry;
	private String userLoc;
	private String userDate;
	private String[] domains;
	private String[] sites;
	private String[] locs;
	
	public String getUserDate() {
		return userDate;
	}
	public void setUserDate(String userDate) {
		this.userDate = userDate;
	}
	public String getUserLoc() {
		return userLoc;
	}
	public void setUserLoc(String userLoc) {
		this.userLoc = userLoc;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getMac() {
		return mac;
	}
	public String[] getLocs() {
		return locs;
	}
	public void setLocs(String[] locs) {
		this.locs = locs;
	}
	public String getUserDmain() {
		return userDmain;
	}
	public void setUserDmain(String userDmain) {
		this.userDmain = userDmain;
	}
	public String getUserSite() {
		return userSite;
	}
	public void setUserSite(String userSite) {
		this.userSite = userSite;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}
	public String getUserFactry() {
		return userFactry;
	}
	public void setUserFactry(String userFactry) {
		this.userFactry = userFactry;
	}
	public String[] getDomains() {
		return domains;
	}
	public void setDomains(String[] domains) {
		this.domains = domains;
	}
	public String[] getSites() {
		return sites;
	}
	public void setSites(String[] sites) {
		this.sites = sites;
	}
}
