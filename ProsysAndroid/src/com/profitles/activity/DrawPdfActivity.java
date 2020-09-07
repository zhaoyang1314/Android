package com.profitles.activity;

import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.profitles.biz.FinshOpScanBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.pdfUtil.BASE64Encoder;
import com.profitles.framwork.util.Constants;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class DrawPdfActivity extends AppFunActivity {


	private WebView webView ;
	private ApplicationDB applicationDB;
	private FinshOpScanBiz biz;
	private String PART_DRAW;   //获取文件路径
	private String PATH;    //获取保存地址
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_drawpdf;
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	protected void pageLoad() {
		biz = new FinshOpScanBiz();
		webView = (WebView)findViewById(R.id.pdfview);
		Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
		Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据  
		PART_DRAW = bundle.getString("PART_DRAW");//getString()返回指定key的值  
		PATH = bundle.getString("PATH");//getString()返回指定key的值  

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginState(WebSettings.PluginState.ON);
		webSettings.setAllowContentAccess(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAllowUniversalAccessFromFileURLs(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		webHtml();
	}

	private void webHtml() {
		try {
			/**
			 * Constants.WebUrl获取 服务器IP+项目名
			 * PDFPrint.jsp  调用的pdf显示的jsp
			 * path="+PATH+PART_DRAW   将地址传递到PDFPrint.jsp中获取显示
			 */
			WebViewLoadPDF(webView, Constants.WebUrl+"/PDFPrint.jsp?path="+PATH+PART_DRAW);
			//			WebViewLoadPDF(webView, "http://192.168.1.156:8888/Prosys/PDFPrint.jsp?path=C:/fileupload"+PART_DRAW);
			//			WebViewLoadPDF(webView, "http://www.anweitong.com/upload/document/standard/national_standards/138793918364316200.pdf");
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
	}

	public static void WebViewLoadPDF(WebView webView, String docPath) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//api >= 19
			webView.loadUrl("file:///android_asset/index.html?" + docPath);
		} else {
			if (!TextUtils.isEmpty(docPath)) {
				byte[] bytes = null;
				try {// 获取以字符编码为utf-8的字符
					bytes = docPath.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (bytes != null) {
					docPath = new BASE64Encoder().encode(bytes);// BASE64转码
				}
			}
			webView.loadUrl("file:///android_asset/index.html?" + docPath);
		}
	}

	@Override
	protected void unLockNbr() {

	}
	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Return, ButtonType.Help};
	}  

	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}

}