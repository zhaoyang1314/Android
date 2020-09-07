package com.profitles.framwork.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.R.bool;

public class StringUtil {
	
	public static Date parseDate(String strDate, String pattern) {
		try {
			DateFormat format = new SimpleDateFormat(pattern);
			return format.parse(strDate);
		} catch (ParseException pe) {
			return null;
		}
	}
	
	public static String parseDate(Object date, String pattern) {
		try {
			DateFormat format = new SimpleDateFormat(pattern);
			return format.format((Date)date);
		} catch (Exception pe) {
			return null;
		}
	}
	
	public static int parseInt(Object str){
		try {
			if(str != null && str.toString().indexOf(".") > 0){
				str = str.toString().replaceAll("\\.0*0$", "");
			}
			return Integer.parseInt(str+"");
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public static boolean parseBoolean(Object str){
		return Boolean.parseBoolean(str+"");
	}
	
	public static float parseFloat(Object str){
		try {
			if(str.equals("-") || str.equals(".") || str.equals("-.")) return 0.0f;
			return Float.parseFloat(str+"");
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public static double parseDouble(Object str){
		try {
			return Double.parseDouble(str+"");
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public static double parseDoubleByBount(Object str){
		try {
			if((str+"").indexOf("%") >= 0){
				return Double.parseDouble((str+"").replace("%", ""))*100;
			}else{
				return parseDouble(str);
			}
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public static float formatFloat(float val, String format){
		DecimalFormat df = new DecimalFormat(format);
		df.format(val);
		return parseFloat(df);
	}
	
	public static String getValStr(Object str, String defStr){
		if(str == null || str.toString().trim().equals("")){
			return defStr;
		}
		return str + "";
	}
	
	public static String subString(String val, String tag, String nextTag){
		String v = val.replaceFirst(tag, "");
		return v.substring(0, v.indexOf(nextTag));
	}
	
	/**
	 * 加
	 * @param d
	 * @param d2
	 * @return
	 */
	public static float mathAdd(Object d, Object d2){
		BigDecimal bDecimal  = new BigDecimal(StringUtil.parseDouble(d));
    	BigDecimal bDecima2  = new BigDecimal(StringUtil.parseDouble(d2));
    	return bDecimal.add(bDecima2).floatValue();
	}
	
	/**
	 * 减
	 * @param d
	 * @param d2
	 * @return
	 */
	public static float mathSubtract(Object d, Object d2){
		BigDecimal bDecimal  = new BigDecimal(StringUtil.parseDouble(d));
    	BigDecimal bDecima2  = new BigDecimal(StringUtil.parseDouble(d2));
    	return bDecimal.subtract(bDecima2).floatValue();
	}
	
	/**
	 * 乘
	 * @param d
	 * @param d2
	 * @return
	 */
	public static float mathMultiply(double d, double d2){
		BigDecimal bDecimal  = new BigDecimal(d);
    	BigDecimal bDecima2  = new BigDecimal(d2);
    	return bDecimal.multiply(bDecima2).floatValue();
	}
	
	/**
	 * 乘
	 * @param d
	 * @param d2
	 * @return
	 */
	public static float mathMultiply2(Object d, Object d2){
		BigDecimal bDecimal  = new BigDecimal(StringUtil.parseDouble(d));
    	BigDecimal bDecima2  = new BigDecimal(StringUtil.parseDouble(d2));
    	return bDecimal.multiply(bDecima2).floatValue();
	}
	
	/**
	 * 除
	 * @param d
	 * @param d2
	 * @return
	 */
	public static float mathDivide(double d, double d2){
		BigDecimal bDecimal  = new BigDecimal(d);
    	BigDecimal bDecima2  = new BigDecimal(d2);
    	return bDecimal.divide(bDecima2).floatValue();
	}
	
	public static float mathDivide2(Object d, Object d2){
		BigDecimal bDecimal  = new BigDecimal(StringUtil.parseDouble(d));
    	BigDecimal bDecima2  = new BigDecimal(StringUtil.parseDouble(d2));
    	return bDecimal.divide(bDecima2).floatValue();
	}
	
	/**
	 * 返回最小值
	 * @param val
	 * @return
	 */
	public static Integer findMinValue(Integer[] val){
		for (int i = 0; i < val.length-1; i++) {
			if(val[i] < val[i+1]){
				val[i] = val[i] + val[i+1];
				val[i+1] = val[i] - val[i+1];
				val[i] = val[i] - val[i+1];
			}
		}
		return val[val.length - 1];
	}
	
	/**
	 * 根据传入参数替换提示信息并返回
	 * 
	 * @param 待参量的提示信息
	 * @param 参数数据信息
	 * @return
	 */
	public static String getFormatMsg(String msg, String... parm){
		if(parm != null && msg != null){
			for (int i = 0; i < parm.length; i++) {
				msg = msg.replace("${"+ i +"}", parm[i]);
			}
		}
		return msg;
	}
	
	public static boolean isEmpty(Object obj){
		return obj == null || obj.toString().trim().equals("") || obj.toString().toUpperCase().equals("NULL");
	}
	
	public static String isEmpty(Object obj, String defVal){
		if(isEmpty(obj)) return defVal;
		return obj.toString();
	}

	public static String remLastLine(String _msg) {
		return _msg.replaceAll("\n$", "");
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
