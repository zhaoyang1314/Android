package com.profitles.framwork.pdfUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.profitles.framwork.appdb.ApplicationDB;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;

public class FileUtils{
	private ApplicationDB applicationDB;
	//android获取一个用于打开HTML文件的intent  
    public static Intent getHtmlFileIntent(String Path)  
    {  
        File file = new File(Path);  
        Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.setDataAndType(uri, "text/html");  
        return intent;  
    }  
    //android获取一个用于打开图片文件的intent  
    public static Intent getImageFileIntent(String Path)  
    {  
        File file = new File(Path);  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(file);  
        intent.setDataAndType(uri, "image/*");  
        return intent;  
    }  
    //android获取一个用于打开PDF文件的intent  
    public static Intent getPdfFileIntent(String Path)  
    {  
    	 File file = new File(Path);
         Intent intent = new Intent("android.intent.action.VIEW");
         intent.addCategory("android.intent.category.DEFAULT");
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
         return intent;
    }  
    //android获取一个用于打开文本文件的intent  
    public static Intent getTextFileIntent(String Path)  
    {  
        File file = new File(Path);  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(file);  
        intent.setDataAndType(uri, "text/plain");  
        return intent;  
    }  

    //android获取一个用于打开音频文件的intent  
    public static Intent getAudioFileIntent(String Path)  
    {  
        File file = new File(Path);  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        intent.putExtra("oneshot", 0);  
        intent.putExtra("configchange", 0);  
        Uri uri = Uri.fromFile(file);  
        intent.setDataAndType(uri, "audio/*");  
        return intent;  
    }  
    //android获取一个用于打开视频文件的intent  
    public static Intent getVideoFileIntent(String Path)  
    {  
        File file = new File(Path);  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        intent.putExtra("oneshot", 0);  
        intent.putExtra("configchange", 0);  
        Uri uri = Uri.fromFile(file);  
        intent.setDataAndType(uri, "video/*");  
        return intent;  
    }  


    //android获取一个用于打开CHM文件的intent  
    public static Intent getChmFileIntent(String Path)  
    {  
        File file = new File(Path);  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(file);  
        intent.setDataAndType(uri, "application/x-chm");  
        return intent;  
    }  


    //android获取一个用于打开Word文件的intent  
    public static Intent getWordFileIntent(String Path)  
    {  
        File file = new File(Path);  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(file);  
        intent.setDataAndType(uri, "application/msword");  
        return intent;  
    }  
    //android获取一个用于打开Excel文件的intent  
    public static Intent getExcelFileIntent(String Path)  
    {  
        File file = new File(Path);  
        Intent intent = new Intent("android.intent.action.VIEW");  
        intent.addCategory("android.intent.category.DEFAULT");  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        Uri uri = Uri.fromFile(file);  
        intent.setDataAndType(uri, "application/vnd.ms-excel");  
        return intent;  
    }  
    
  private String path = Environment.getExternalStorageDirectory().toString() + "/profit";
    
    public FileUtils() {
        File file = new File(path);
        /**
         *如果文件夹不存在就创建
         */
        if (!file.exists()) {
            file.mkdirs();
        }
    }
 
    /**
     * 创建一个文件
     * @param FileName 文件名
     * @return
     */
    public File createFile(String FileName) {
        return new File(path, FileName);
    }
    
  
    
    public void downLoad(final String name, final String department, final String addrss) {
		new Thread(new Runnable() {
            @Override
            public void run() {
			    	InputStream in = null ; 
			    	 Message msg = new Message();
					try {  
					    //创建远程文件对象  smb://用户名:密码@ip地址/共享的路径/...
					    String remotePhotoUrl = "smb://"+ addrss + name;  
					    SmbFile remoteFile = new SmbFile(remotePhotoUrl);  
					    try {
							remoteFile.connect(); //尝试连接  
						} catch (Error e) {
							e.printStackTrace();
						}
					copyRemoteFile(remoteFile, "/sdcard/profit/"+applicationDB.user.getUserId()+"/"+department+"/");
					}  
					catch (Exception e) {  
						e.printStackTrace();
					}  
					finally {  
					    try {  
					        if(in != null) in.close();  
					    }  
					    catch (Exception e) {
					    	e.printStackTrace();
					    }  
					}
             }
           }).start();
	}
    
    
    /**
     * 从服务器下载文件
     * @param path 服务器tomcat地址
     * @param name 文件名字
     * localDirectory 本地存储地址
     */
    /**
     * 此配置放到tomcat中的server.xml的Host标签里面
     * <Context docBase="C:\" path="/URL" reloadable="true"></Context> 
     * 
     * */
    public  void downLoadUrl(final String point, final String name , final String localDirectory , final String serverfilepath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
            	FileOutputStream fileOutputStream = null;//文件输出流
            	HttpURLConnection con = null;
                try {
                	URL url = new URL(point+"/URL/fileupload"+ serverfilepath);
                	con = (HttpURLConnection) url.openConnection();
                	con.setReadTimeout(5000);
                	con.setConnectTimeout(5000);
                	con.setRequestProperty("Charset", "UTF-8");
                	con.setRequestMethod("GET");
                	File[] localFiles = new File(localDirectory).listFiles();
                     if (null == localFiles) {
                         // 目录不存在的话,就创建目录
                          new File(localDirectory).mkdirs();
                     }
                     File path = new File(localDirectory);
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        if (is != null) {
//                            fileOutputStream =  new FileOutputStream(localDirectory+"/"+ name);
                        	File createTempFile = File.createTempFile(name, ".pft",path);
                            fileOutputStream =  new FileOutputStream(createTempFile);
                            long length = createTempFile.length();
                            if(length > 0){ // 查询服务器文件是否是0大小文件
                            	byte[] buf = new byte[1024];
                            	int ch;
                            	while ((ch = is.read(buf)) != -1) {
                            		fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                            	}
                            	File newFile = new File("/"+localDirectory+"/"+ name);
                            	boolean renameTo = createTempFile.renameTo(newFile);
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        con.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    
    /**
     * 拷贝远程文件到本地目录
     * @param smbFile 远程SmbFile
     * @param localDirectory 本地存储目录,本地目录不存在时会自动创建,本地目录存在时可自行选择是否清空该目录下的文件,默认为不清空
     * @return boolean 是否拷贝成功
     */
    public static boolean copyRemoteFileuURL(SmbFile smbFile, String localDirectory){
        SmbFileInputStream in = null;
        FileOutputStream out = null;
        try {
            File[] localFiles = new File(localDirectory).listFiles();
            if (null == localFiles) {
                // 目录不存在的话,就创建目录
                new File(localDirectory).mkdirs();
            }
            in = new SmbFileInputStream(smbFile);
            out = new FileOutputStream(localDirectory + smbFile.getName());
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
        	e.getStackTrace();
            System.out.println("拷贝远程文件到本地目录失败");
            return false;
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    
    
    /**
     * 拷贝远程文件到本地目录
     * @param smbFile 远程SmbFile
     * @param localDirectory 本地存储目录,本地目录不存在时会自动创建,本地目录存在时可自行选择是否清空该目录下的文件,默认为不清空
     * @return boolean 是否拷贝成功
     */
    public static boolean copyRemoteFile(SmbFile smbFile, String localDirectory){
        SmbFileInputStream in = null;
        FileOutputStream out = null;
        try {
            File[] localFiles = new File(localDirectory).listFiles();
            if (null == localFiles) {
                // 目录不存在的话,就创建目录
                new File(localDirectory).mkdirs();
            }
            in = new SmbFileInputStream(smbFile);
            out = new FileOutputStream(localDirectory + smbFile.getName());
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
        	e.getStackTrace();
            System.out.println("拷贝远程文件到本地目录失败");
            return false;
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean fileIsExists(String path, String name){
		try {
			File f = new File(path+"/"+name);
			if (!f.exists()) {
				
//				deleteDirectory(path);
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
    
    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String filePath) {
    File file = new File(filePath);
        if (file.isFile() && file.exists()) {
        return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String filePath) {
    boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
            //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
            //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param filePath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String filePath) {
    File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
            // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
            // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }
    
    
    /**
     * 获取目录下所有文件(按时间排序)
     * 
     * @param path
     * @return
     * @throws ParseException 
     */
    public static List<File> listFileSortByModifyTime(String path) throws ParseException {
        List<File> list = getFiles(path, new ArrayList<File>());
        List<File> removeList = new ArrayList<File>();
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return -1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String nowtime = df.format(date);
            Date d1 = df.parse(nowtime);
            for (File file : list) {
            	String fileName = file.getName();
            	String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            	long lastModified = file.lastModified();
            	long time = d1.getTime();
            	long l = (long) (lastModified - (time - 60*60*1000*24*30L));
            	if(lastModified < (time - 60*60*1000*24*30L) || "pft".equals(suffix)){
            		removeList.add(file);
            	}
			}
        }
        return removeList;
    }

    /**
     * 
     * 获取目录下所有文件
     * 
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }
    
   
    @SuppressWarnings("unused")
	public static boolean check(String path) {
		boolean flag1 = false;
		int n = 0;
		try {
			Document document = new Document(new PdfReader(path).getPageSize(1));
			document.open();
			PdfReader reader = new PdfReader(path);
			n = reader.getNumberOfPages();
			if (n != 0)
				flag1 = true;
			document.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return flag1;

	}


}
