package com.profitles.framwork.cusviews.view.bean;

import com.profitles.framwork.BaseObject;
import com.profitles.framwork.util.StringUtil;

public class ValidataBean extends BaseObject {
	private boolean isRequired = false;
	private String reqAltMsg;
	private String regValidata;
	private String regAltMsg;
	private boolean isBlurRequired = false;
	private String blreAltMsg;
	private boolean isReadOnly;
	
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public String getReqAltMsg() {
		return StringUtil.isEmpty(reqAltMsg, blreAltMsg);
	}
	public void setReqAltMsg(String reqAltMsg) {
		this.reqAltMsg = reqAltMsg;
	}
	public String getRegValidata() {
		return regValidata;
	}
	public void setRegValidata(String regValidata) {
		this.regValidata = regValidata;
	}
	public String getRegAltMsg() {
		return regAltMsg;
	}
	public void setRegAltMsg(String regAltMsg) {
		this.regAltMsg = regAltMsg;
	}
	public boolean isBlurRequired() {
		return isBlurRequired;
	}
	public void setBlurRequired(boolean isBlurRequired) {
		this.isBlurRequired = isBlurRequired;
	}
	public String getBlreAltMsg() {
		return blreAltMsg;
	}
	public void setBlreAltMsg(String blreAltMsg) {
		this.blreAltMsg = blreAltMsg;
	}
	public boolean isReadOnly() {
		return isReadOnly;
	}
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
}
