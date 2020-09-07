package com.profitles.framwork.util;

import android.os.Environment;
import android.util.DisplayMetrics;

public class Constants {

	/** 数据库文件 **/
	public final static String DB_Path = Environment.getExternalStorageDirectory().getPath() + "/profitles/file";
	public final static String App_Path = Environment.getExternalStorageDirectory().getPath() + "/profitles/apk";
	/** 应用名称 **/
	public final static String App_Save_Name = "profitles.apk";
	public static String App_Version = "201811071730";

	/**
	 * 根节点17701893963
	 */
	public final static String DB_Root = "Root";
	/**
	 * 通用截取字符串标记
	 */
	public final static String DB_SPLIT_TAG_KEY = "splitTag";
	public final static String DB_SPLIT_TAG_VAL = "|$|";
	
	//项目源文件路径
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	//日期格式
	public final static String DATE_FORMAT = "yyyy-mm-dd";
	//字符分隔符
	public static final String SPLIT_TAG = "|";
	//列表间隔显示颜色
	public static final String CHECK_ROW_COLOR = "#ffe4e1";
	//Bean中关键自动Key
	public static final String KEY_ATT_FILED_NAME = "attKeyNames";
	public static final String KEY_ATT_METHOD_NAME = "setAttKeyNames";
	public static final int Default_Int_Val = -100011;
	public static final String ActivityPackage = "com.profitles.activity";
	public static final String EnptyMenuTagert = "N/A";
	public static final int EditDefHeight = 30; //屏幕大小
	public static final int EditDefHavHeight = 25; //横屏屏幕大小
	public static final int DataGridMX = 20;
	 public static String WebEndPoint = "http://192.168.1.191:8888/Prosys/services/SrmService";
	 //    public static String WebEndPoint = "http://114.55.145.166:8888/ProfitLESGRE/services/SrmService";
//	 public static String WebEndPoint = "http://27.115.34.251:18855/ProfitLESGRE/services/SrmService";
				//http://192.168.1.226:8888/Prosys/services/SrmService
	//"https://demo.profitles.com:8456/Prosys/services/SrmService?wsdl";
	//	public static String WebEndPoint = "http://demo.profitles.com:8856/Prosys/services/SrmService?wsdl";
	public static String WebUrl = WebEndPoint.replace("/services/SrmService", "");
//	17
//	public static final int EditDefHeight = 50;
//	public static String WebEndPoint = "http://192.168.1.17:8888/ProfitLDLES/services/SrmService";
//	客户环境
	//public static final int EditDefHeight = 50;
	//public static String WebEndPoint = "http://192.168.1.3:8888/ProfitALES/services/SrmService";
	
	// SN006 语音提示修改 2019-07-09 By Brenna 
	public static final String ErrorMes = "错误";
	public static final String WaringMes = "警告";
	public static final boolean isReadMes = false;
}
