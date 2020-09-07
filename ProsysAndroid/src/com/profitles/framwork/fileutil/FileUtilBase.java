package com.profitles.framwork.fileutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class FileUtilBase {
	
	private String _filterStr = "";
	/**
	 * 删除文件
	 * @param path
	 */
	public void deleteByPath(String path){
		File file = new File(path);
		if(file.exists())
			file.deleteOnExit();
	}
	
	/**
	 * 检查文件是否存在，并传入参数存在是否删除
	 * @param path
	 * @param isDelete
	 * @return
	 */
	public boolean existOrDelete(String path, boolean isDelete){
		File file = new File(path);
		boolean isExists = file.exists();
		if(isExists && isDelete){
			file.deleteOnExit(); return false;
		}
		return isExists;
	}
	
	/**
	 * 根据路径获取对应的所有文件对象
	 * @param path
	 * @return
	 */
	public File[] getFoldersByPath(String path){
		File file = new File(path);
		if(!file.exists())
			return new File[0];
		return file.listFiles();
	}
	
	public File[] getFoldersByPath(String path, String filterStr){
		File file = new File(path);
		_filterStr = filterStr;
		File[] fs = file.listFiles(new FileFilter(){
			public boolean accept(File f) {
				String tmp = f.getName().toLowerCase();
				// 循环过滤文件过滤
				if (tmp.endsWith(_filterStr)) {
					return true;
				}
				return false;
			}
		});
		return fs;
	}
	
	/**
	 * 获取文件读取流对象
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public BufferedReader getFileReStream(String path, String code) throws IOException{
		return new BufferedReader(new InputStreamReader(
			     new FileInputStream(path), code));
	}	
	
	public BufferedReader getFileReStream(File file, String code) throws IOException{
		return new BufferedReader(new InputStreamReader(
			     new FileInputStream(file), code));
	}
	
	/**
	 * 获取一个写入流
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public BufferedWriter getFileWrStream(String path) throws IOException{
		return new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path)));
	}
	
	public BufferedWriter getFileWrStream(File f, String code) throws IOException{
		return new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f), code));
	}
	
	public void copyFile(File f,String dest) throws IOException{
        FileInputStream in=new FileInputStream(f);
        File file=new File(dest);
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file);
        int c;
        byte buffer[]=new byte[1024];
        while((c=in.read(buffer))!=-1){
            for(int i=0;i<c;i++)
                out.write(buffer[i]);        
        }
        out.flush();
        out.close();
        in.close();
    }
	
	public void appWirteInfo(File file, String code, String msg){
		msg = "\n" + msg;
		if(!file.exists()){
			try {
				File tFile = new File(file.getParent());
				tFile.mkdirs();
				FileOutputStream out=new FileOutputStream(file,true);
				out.write(msg.getBytes());
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				RandomAccessFile raf=new RandomAccessFile(file.getPath(),"rw");
				raf.seek(raf.length());
				raf.write(msg.getBytes());
				raf.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
