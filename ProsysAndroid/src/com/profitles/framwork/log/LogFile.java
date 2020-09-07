package com.profitles.framwork.log;

import java.io.File;
import java.util.Date;

import com.profitles.framwork.fileutil.FileUtilBase;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;

public class LogFile extends FileUtilBase {

	private final File logfile = new File(Constants.DB_Path + "/" + StringUtil.parseDate(new Date(), "yyyy-MM-dd") + ".log");
	private boolean isDebug = false;
	private boolean isError = true;
	private boolean isInfo = true;
	private boolean isWarning = false;
	private Class<?> cls;
	
	public LogFile(Class<?> cls){
		this.cls = cls;
	}
	
	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public boolean isInfo() {
		return isInfo;
	}

	public void setInfo(boolean isInfo) {
		this.isInfo = isInfo;
	}

	public boolean isWarning() {
		return isWarning;
	}

	public void setWarning(boolean isWarning) {
		this.isWarning = isWarning;
	}

	public void debug(String msg){
		if(isDebug())	writeMsg(msg, LogFile_Type.Debug);
	}
	
	public void error(String msg){
		if(isError()) writeMsg(msg, LogFile_Type.Error);
	}
	
	public void error(StackTraceElement stk, String msg){
		if(isError()){
			msg = formatTck(stk) + msg;
			writeMsg(msg, LogFile_Type.Error);
		}
	}
	
	public void error(StackTraceElement[] stk, String msg){
		for (int i = 0; i < stk.length; i++) {
			error(stk[i], msg);
		}
	}
	
	public void info(String msg){
		if(isInfo()) writeMsg(msg, LogFile_Type.Info);
	}
	
	public void warning(String msg){
		if(isWarning()) writeMsg(msg, LogFile_Type.Warning);
	}
	
	private String formatMsg(String msg, LogFile_Type type){
		msg = "[" +StringUtil.parseDate(new Date(), "yyyy-MM-dd hh:mm:ss") + "] " +
					type.toString() + " : " + cls.getName() + " => " + msg;
		return msg;
	}
	
	private void writeMsg(String msg, LogFile_Type type){
		try {
			appWirteInfo(logfile, "utf-8", formatMsg(msg, type));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String formatTck(StackTraceElement ste){
		if(ste == null) return "MyError: STR = NULL";
		StringBuffer sBuffer = new StringBuffer("");
		sBuffer.append(ste.getClassName() + "  ")
		.append(ste.getFileName() + "  ")
		.append(ste.getLineNumber() + "  ")
		.append(ste.getMethodName() + "  ");
		return sBuffer.toString();
	}
	
	public enum LogFile_Type{
		Debug, Error, Info, Warning
	}
}
