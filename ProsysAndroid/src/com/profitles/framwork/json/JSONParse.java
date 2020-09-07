package com.profitles.framwork.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class JSONParse {

	/**
	 * 将传入的json字符串转换为元素为map集合的List集合
	 * @param jsonArrStr
	 * @return
	 */
	public static List<Map<String, Object>> jsonObjList(String jsonArrStr) {
		List<Map<String, Object>> jsonObjList = new ArrayList<Map<String, Object>>();
		List<?> jsonList = jsonToList(jsonArrStr);
		Gson gson = new Gson();
		for (Object object : jsonList) {
			String jsonStr = gson.toJson(object);
			Map<String, Object> json = jsonToMap(jsonStr);
			jsonObjList.add((Map<String, Object>) json);
		}
		return jsonObjList;
	}

	/**
	 * 将传入的json字符串解析为List集合
	 * @param jsonStr
	 * @return
	 */
	public static List<?> jsonToList(String jsonStr) {
		List<?> ObjectList = null;
		Gson gson = new Gson();
		java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<?>>() {
		}.getType();
		ObjectList = gson.fromJson(jsonStr, type);
		return ObjectList;
	}

	/**
	 * 将传入的json字符串解析为Map集合
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String jsonStr) {
		Map<String, Object> ObjectMap = null;
		Gson gson = new Gson();
		java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
		}.getType();
		ObjectMap = gson.fromJson(jsonStr, type);
		return ObjectMap;
	}

	/**
	 * 将传入的json字符串解析为ClassBean
	 * @param jsonStr
	 * @param c
	 * @return
	 */
	public static Object jsonToClass(String jsonStr, Class<?> c) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, c);
	}
}
