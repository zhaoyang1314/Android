package com.profitles.framwork.webservice;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsServiceConnectionSE;
import org.ksoap2.transport.HttpsTransportSE;

import com.profitles.framwork.BaseObject;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.activity.util.WebResponse.RtnJsonKey;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.json.JSONParse;
import com.profitles.framwork.language.LanagerMgr;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;
import com.profitles.framwork.webservice.bean.WebSerBean;
import com.profitles.framwork.webservice.interfaces.IWebConnUtil;

public class WebConnUtil extends BaseObject implements IWebConnUtil {

	private WebSerBean wsBean;
	private Map<String, String> params;
	
	public WebConnUtil() {
		wsBean = new WebSerBean();
		wsBean.setEndPoint(Constants.WebEndPoint);
		wsBean.setNameSpace(wsBean.getEndPoint());
	}
	
	public WebResponse execRtn(String methodName){
		return formatRsp(callService(methodName));
	}
	
	public WebResponse execRtn(String methodName, Map<String, String> params){
		if(params != null) addParam(params);
		return formatRsp(callService(methodName));
	}
	
	private WebResponse formatRsp(String mapJson){
		WebResponse wrp = null;
		try {
			wrp = new WebResponse(JSONParse.jsonToMap(mapJson));
		} catch (Exception e) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(RtnJsonKey.Message.toString(), mapJson);
			wrp = new WebResponse(params, RtnJsonKey.Faild);
		}
		return wrp;
	}
	
	public WebConnUtil clearParams(){
		if(params != null) params.clear();
		return this;
	}

//	public String execRtnStr(String methodName){
//		return callService(methodName);
//	}
//
//	public Map<String, Object> execRtnItem(String methodName){
//		return execRtnItem(methodName, null);
//	}
//	
//	public Map<String, Object> execRtnItem(String methodName, Map<String, String> params){
//		if(params != null) addParam(params);
//		return JSONParse.jsonToMap(callService(methodName));
//	}
//	
	
//	public List<Map<String, Object>> execRtnList(String methodName){
//		return execRtnList(methodName, null);
//	}
//	
//	public List<Map<String, Object>> execRtnList(String methodName, Map<String, String> params){
//		if(params != null) addParam(params);
//		return JSONParse.jsonObjList(callService(methodName));
//	}
	
	public WebConnUtil addParam(String key, String value){
		params = params == null ? new HashMap<String, String>() : params;
		params.put(key, value);
		return this;
	}
	
	public WebConnUtil addParam(Map<String, String> _params){
		params = params == null ? new HashMap<String, String>() : params;
		params.putAll(_params);
		return this;
	}

	private String callService(String methodName) {
		if(!wsBean.isHttpsEP()) return callService2(methodName);
		wsBean.setMethodName(methodName);
		String soapAction = StringUtil.isEmpty(wsBean.getSoapAction(),"") + wsBean.getMethodName();

		// 指定WebService的命名空间和调用的方法名
		SoapObject request = new SoapObject(wsBean.getNameSpace(), methodName);

		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		if(params != null){
			for(String key : params.keySet()){
				request.addProperty(key, params.get(key));
			}    
		}
		
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(wsBean.getSepVersion());
		envelope.bodyOut = request;
		envelope.dotNet = wsBean.isDotnet();
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = wsBean.getEncoding();
		HttpsTransportSE transport = new HttpsTransportSE(wsBean.getDomian(), wsBean.getPort(), wsBean.getEndPath(), 60000 );
		try {	
			((HttpsServiceConnectionSE) transport.getServiceConnection()).setSSLSocketFactory(ApplicationDB.socketFactory);  
			// 调用WebService
			transport.call(soapAction, envelope);
			// 获取返回的数据
			return envelope.getResponse().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return LanagerMgr.formatException(e);
		}
		// 获取返回的结果
		//return null;
	}
	private String callService2(String methodName) {
		wsBean.setMethodName(methodName);
		String soapAction = StringUtil.isEmpty(wsBean.getSoapAction(),"") + wsBean.getMethodName();

		// 指定WebService的命名空间和调用的方法名
		SoapObject request = new SoapObject(wsBean.getNameSpace(), methodName);

		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		if(params != null){
			for(String key : params.keySet()){
				request.addProperty(key, params.get(key));
			}    
		}
		
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(wsBean.getSepVersion());
		envelope.bodyOut = request;
		envelope.dotNet = wsBean.isDotnet();
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = wsBean.getEncoding();
		HttpTransportSE transport = new HttpTransportSE(wsBean.getEndPoint(),60000 );
		try {	
			// 调用WebService
			transport.call(soapAction, envelope);
			// 获取返回的数据
			return envelope.getResponse().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return LanagerMgr.formatException(e);
		}
		// 获取返回的结果
		//return null;
	}
}
