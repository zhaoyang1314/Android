package com.profitles.framwork.language;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import com.profitles.framwork.util.Constants;

public class LanagerMgr {
	
	private static Map<String, String> messages;
	
	static{
		messages = new HashMap<String, String>();
		messages.put("ERROR01", "连接服务器失败，请检查网络异常！");
	}

	public static String formatException(Exception e){
		String msg = "";
		if(e instanceof ConnectException){
			msg = messages.get("ERROR01");
		}
		return msg;
	}
}
