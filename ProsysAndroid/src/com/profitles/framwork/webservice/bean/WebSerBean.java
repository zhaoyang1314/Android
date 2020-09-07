package com.profitles.framwork.webservice.bean;

import org.ksoap2.SoapEnvelope;

import com.google.zxing.common.StringUtils;
import com.profitles.framwork.util.StringUtil;

import android.R.bool;

public class WebSerBean {

	private String nameSpace; 	// 命名空间
	private String methodName; 	// 调用的方法名称
	private String endPoint; 	// EndPoint
	private String soapAction; 	// SOAP Action
	private boolean isDotnet = false;
	private String encoding = "UTF-8";
	private int sepVersion = SoapEnvelope.VER10;
	private int port;
	private String domian, endPath;

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public int getSepVersion() {
		return sepVersion;
	}

	public void setSepVersion(int sepVersion) {
		this.sepVersion = sepVersion;
	}

	public void setEndPoint(String endPoint) {
		if(endPoint != null){
			String tp = endPoint.substring(endPoint.indexOf("//")+2, endPoint.length());
			String[] temp = tp.substring(0, tp.indexOf("/")).split(":");
			this.domian = temp[0];
			this.port = StringUtil.parseInt(temp[1]);
			this.endPath = tp.substring(tp.indexOf("/")).replace("?wsdl", "");
		}
		this.endPoint = endPoint;
	}

	public String getEndPath() {
		return endPath;
	}

	public boolean isHttpsEP(){
		return getEndPoint() != null && getEndPoint().toUpperCase().indexOf("HTTPS") >= 0;
	}
	
	public int getPort() {
		return port;
	}

	public String getDomian() {
		return domian;
	}

	public String getSoapAction() {
		return soapAction;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public boolean isDotnet() {
		return isDotnet;
	}

	public void setDotnet(boolean isDotnet) {
		this.isDotnet = isDotnet;
	}
}
