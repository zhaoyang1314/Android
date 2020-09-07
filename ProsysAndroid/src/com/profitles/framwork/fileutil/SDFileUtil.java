package com.profitles.framwork.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.profitles.framwork.log.LogFile;
import com.profitles.framwork.util.Constants;

/**
 * 数据文件是否存在 此处文件后续将可抽出写入 doa 层中，现在临时写在这里
 * @return
 */
public class SDFileUtil extends FileUtilBase {

	private Context context;
	public static String rootPath = Constants.DB_Path;
	private LogFile logFile = new LogFile(this.getClass());

	public SDFileUtil(){};
	
	public SDFileUtil(Context context){
		setContext(context);
	}
	
	/**
	 * 检查数据文件目录是否已经存在，存在则返回true，不存在则返回false
	 * @return
	 */
	public boolean isExitsDBFile(boolean isCreate){
		File file = new File(rootPath);
		if(file.isDirectory()){
			return true;
		}else if(!file.isDirectory() && isCreate){
			return file.mkdirs();
		}
		return false;
	}
	
	/**
	 * 创建数据库文件目录
	 * @return
	 */
	public boolean createDBFile(){
		try {
			File file = new File(rootPath);
			if(!file.exists()){
				return new File(rootPath).mkdirs();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 检查SD卡是否存在
	 */
	public boolean isExitsSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	
	/**
	 * 获取数据库连接流
	 * @param dbName 资源库名称
	 * @return
	 */
	public InputStream getDataXML(String dbName){
		try {
			File file = new File(rootPath + "/" + dbName);	//获取外部SD资源
			if(file.exists()){
				return new FileInputStream(file);
			}else {
				//如外部资源不存在则获取内部资源
				return getContext().getResources().getAssets().open(dbName); 
			}
		}catch (Exception e) {
			logFile.error(e.getStackTrace(), e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean writeByInputStm(InputStream inpStm, String toPath) {
		FileOutputStream out = null;
		try {
			existOrDelete(toPath, true);		//检查文件是否存在，存在则直接删除
			out = new FileOutputStream(toPath);
			byte[] buffer = new byte[1024];
			int read;
			while ((read = inpStm.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
			out.getFD().sync();
			out.flush();
			inpStm.close();
			inpStm = null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				out.close();
				out = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * 安装Apk
	 * @param path
	 */
	public void installApk(String path){  
	     File apkfile = new File(path);  
	     if (!apkfile.exists()) {  
	         return;  
	     }      
	     Intent i = new Intent(Intent.ACTION_VIEW);
	     i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");   
	     getContext().startActivity(i);  
	}   
	
	/**
	 * 获取版本数据库连接流
	 * @return
	 */
	public InputStream getVersionDB(){
		return null;
	}
	 
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
