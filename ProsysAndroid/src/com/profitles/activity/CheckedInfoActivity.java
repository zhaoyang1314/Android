package com.profitles.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jcifs.smb.SmbFile;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.profitles.biz.CheckInfoBiz;
import com.profitles.biz.SelfiViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.pdfUtil.FileUtils;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;
@SuppressLint("NewApi")
public class CheckedInfoActivity extends AppFunActivity {
	private ApplicationDB applicationDB;
	private List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
	private CheckInfoBiz biz;
	private String nbr, WO_NBR, UKEY, LINE;
	private MyDataGrid dtg;
	private Map<String, Object> datas;
	private String qty,tiaoma,OPERENVNM;    //数量   条码   环境
	Map<String, Object> itemmap = new HashMap<String, Object>();
	List<Map<String, Object>> itemList1 = new ArrayList<Map<String,Object>>();
	String state="1"; //状态=已检
	private String XSWC_MJCJ="", PATH_NM, qat_nbr, DETECTION_TYPE_IP, SERVER_DOWNLOADS, SERVER_POINT;//当前产线车间
	private View backRow;
	private int checkRowIndex;
	private Handler linkHander;
	private SelfiViewBiz selBiz;
	private String report_path = "" , message ="", path = "";
	private int FileLength, X, result;
    private int DownedFileLength=0;
    private ProgressBar progressBar;
    private TextView tv;
	private ProgressDialog mDialog;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_checked_view;
	}

	@Override
	protected void pageLoad() {	
		dtg = (MyDataGrid) findViewById(R.id.mdtg_chk);
		biz = new CheckInfoBiz();
	    selBiz = new  SelfiViewBiz() ;
		Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
		Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
		WO_NBR=bundle.getString("XSWO_NBR");//getString()返回指定key的值  
		UKEY=bundle.getString("XSWO_UKEY");//getString()返回指定key的值  
		tiaoma = bundle.getString("TIAOMA");    //条码
		qty = bundle.getString("QTY");      //数量
		LINE = bundle.getString("txvWkLine");
		OPERENVNM=bundle.getString("OPERENVNM");     //环境
		DETECTION_TYPE_IP = bundle.getString("DETECTION_TYPE_IP"); // 三坐标报告
		SERVER_DOWNLOADS = bundle.getString("SERVER_DOWNLOADS"); // 是否服务器下载
		SERVER_POINT = bundle.getString("SERVER_POINT"); // ip
		if(!StringUtil.isEmpty(bundle.getString("XSWC_MJCJ"))){
			XSWC_MJCJ=bundle.getString("XSWC_MJCJ");
		}
		getLinePage();	
		/*dtg.setOnMyDataGridListener(new OnMyDataGridListener() {
			@Override
			public void onItemClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
				  qat_nbr = (String) rowData.get("QAT_NBR");
				if(rowIndex == 0) return;
				if(backRow != null){
					// backRow.setBackgroundColor(Color.TRANSPARENT);//清空上次点击行颜色
					backRow.setBackgroundColor(checkRowIndex == 0  ? Color.TRANSPARENT : 
						(checkRowIndex%2 == 0 ? Color.TRANSPARENT: Color.parseColor(Constants.CHECK_ROW_COLOR)) );//清空上次点击行颜色
				}
				backRow  = (View) view.getParent();		//保存获取到行View
				checkRowIndex = rowIndex;
				View vv = (View) view.getParent();		//获取到行View
				vv.setBackgroundColor(Color.YELLOW);	//更改选中行的背景色
//				checkThreeCoord();
				
			}

			@Override
			public boolean onItemLongClick(View view, Object val, int rowIndex,
					int colIndex, Map<String, Object> rowData) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});*/
	}
	
	// 获得未检信息
	private void getLinePage(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public Object onGetData() {
				return biz.getCheckInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),state, LINE);
			}
			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;		
				if(wrs.isSuccess()){
					//获得的数据库交接项信息
					itemList = wrs.getDataToList();
					
						dtg.buildData(itemList);
				
						setItemListyener();
					onCreate();
				}else{
					showErrorMsg(wrs.getMessages());
				}
			}		
		});	 
	}
	
	private void checkThreeCoord(){
		loadDataBase(new IRunDataBaseListens() {

			@Override
			public boolean onValidata() {
				if(itemList.size() > 0){
					for (Map<String, Object> str : itemList) {
						if(qat_nbr.equals(str.get("QAT_NBR")+"")){
							PATH_NM = str.get("QAT_REP_PATH")+"";
						}
					}
				}
				return true;
			}

			@Override
			public Object onGetData() {
				// TODO Auto-generated method stub
				return biz.checkReport(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), PATH_NM);
			}

			@Override
			public void onCallBrack(Object data) {
				WebResponse wrs = (WebResponse) data;
				if(wrs.isSuccess()){
					List<Map<String, Object>> dataToList = wrs.getDataToList();
					if(dataToList == null ||dataToList.size() == 0){
						showMessage("没有对应的三坐标报告,请到系统界面进行上传");
					}else if(dataToList.size() > 1){
						showMessage("系统上传的三坐标报告数据出现重复异常");
					}else{
						String report_path = dataToList.get(0).get("CHECK_REPORT_PATH")+""; // 报告名
						String path = dataToList.get(0).get("CHECK_SYS_PATH")+"";
						FileUtils fileUtils = new FileUtils();
						/*String[] split = report_path.split("/");
						String	REPORT_DRAW_NM = split[4]; */
						boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/",PATH_NM);
						if(!fileIsExists){
							if("NO".equals(SERVER_DOWNLOADS)){
								downLoad(PATH_NM , "DETECTION", DETECTION_TYPE_IP);
							}else{
								downLoadUrl(SERVER_POINT,PATH_NM, "/sdcard/profit/ProfitMES", report_path);
							}
						}else{
							Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/ProfitMES"+"/"+PATH_NM);
							startActivity(pdfFileIntent);
						}
						
						/*Intent intent = new Intent(CheckedInfoActivity.this,
								DrawPdfActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("PART_DRAW", report_path);
						bundle.putString("PATH", path);
						intent.putExtras(bundle);
						startActivity(intent);*/
					}
					
				}else{
					showMessage(wrs.getMessages());
				}
			}
			
		});
	}
	
	
	private void setItemListyener() {
		dtg.setOnMyDataGridListener(new OnMyDataGridListener() {
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowDatas) {
				return false;
			}
			public void onItemClick(View view, Object val,  int rowIndex, int colIndex ,Map<String, Object> rowData) {
				datas = rowData;
				//view.setBackgroundColor(Color.YELLOW);
				AlertDialog alert=new AlertDialog.Builder(CheckedInfoActivity.this).create();
				alert.setTitle("SN码:"+ StringUtil.isEmpty(rowData.get("QAT_SN")+"", "")); 
				alert.setMessage("零件:"+ StringUtil.isEmpty(rowData.get("QAT_OP_PART")+"", "")+"\r\n"+"日期:"+ StringUtil.isEmpty(rowData.get("QAT_CSP_DATE")+"", ""));
				final String REP_PATH = 	datas.get("QAT_REP_PATH")+"";
				message = "";
				loadDataBase(new IRunDataBaseListens(){
					
					@Override
					public boolean onValidata() {
						// TODO Auto-generated method stub
						return true;
					}
					
					@Override
					public Object onGetData() {
						
						return selBiz.checkReport(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), REP_PATH);
					}
					
					@Override
					public void onCallBrack(Object data) {
						WebResponse wrs = (WebResponse) data;
						if(wrs.isSuccess()){
							List<Map<String, Object>> dataToList = wrs.getDataToList();
							if(dataToList == null ||dataToList.size() == 0){
							 message = "没有对应的三坐标报告,请到系统界面进行上传";
							}else if(dataToList.size() > 1){
							 message =	"系统上传的三坐标报告数据出现重复异常";
							}else{
								 report_path = dataToList.get(0).get("CHECK_REPORT_PATH")+""; // 报告名
								 path = dataToList.get(0).get("CHECK_SYS_PATH")+"";
								FileUtils fileUtils = new FileUtils();
								boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/ProfitMES",REP_PATH);
								if(!fileIsExists){
									fileUtils.downLoadUrl(SERVER_POINT,REP_PATH, "/sdcard/profit/ProfitMES", report_path);
								}
							}
						}else{
							showMessage(wrs.getMessages());
						}
					}
				});
				alert.setButton(DialogInterface.BUTTON_POSITIVE,"查自检", new DialogInterface.OnClickListener() {     
			          @Override
			          public void onClick(DialogInterface arg0, int arg1) {
			        	 Intent i = new Intent(CheckedInfoActivity.this , PsiSelfActivity.class);
						 Bundle bundle = new Bundle();
						 bundle.putString("part", StringUtil.isEmpty(datas.get("QAT_OP_PART")+"", ""));
						 bundle.putString("nbr", StringUtil.isEmpty(datas.get("QAT_NBR")+"", ""));
						 i.putExtras(bundle); 
						 startActivity(i); 
			          } 
			      });
				
				alert.setButton(DialogInterface.BUTTON_NEUTRAL,"三坐标报告", new DialogInterface.OnClickListener() {     
			          @Override
			          public void onClick(DialogInterface arg0, int arg1) {
			        	  if(!StringUtil.isEmpty(message)){
			        		  showErrorMsg(message);
			        	  }else{
			        		  FileUtils fileUtils = new FileUtils();
			        		  boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/ProfitMES",REP_PATH);
			        		  if(!fileIsExists){
			        			  downLoadUrl(SERVER_POINT,REP_PATH, "/sdcard/profit/ProfitMES", report_path);
			        		  }else{
			        			  Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/ProfitMES"+"/"+REP_PATH);
			        			  startActivity(pdfFileIntent);
			        		  }
			        	  }
			          } 
			      });
				
				alert.show();
			}
		});
		
	}
	
	 /**
     * 从局域网的共享服务器上面下载文件到本地
     * 
     * */
	
  final int SUCCESS=1;
  final int FAIL=0;
	private void downLoad(final String name, final String department, final String addrss) {
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
							msg.what = 0;
							linkHander.sendMessage(msg);
						}
					    FileUtils fileUtils = new FileUtils();
					   boolean copyRemoteFile = fileUtils.copyRemoteFile(remoteFile, "/sdcard/profit/"+applicationDB.user.getUserId()+"/"+department+"/");
					   if(!copyRemoteFile){
						  msg.what = 2;
						linkHander.sendMessage(msg);
					   }else{
							msg.what = 1;
							linkHander.sendMessage(msg);
						}
					    
					}  
					catch (Exception e) {  
						msg.what = 0;
						linkHander.sendMessage(msg);
					}  
					finally {  
					    try {  
					        if(in != null) in.close();  
					    }  
					    catch (Exception e) {
					    	
					    }  
					}
             }
           }).start();
    	   linkHander =new Handler(){
    	        @Override
    	        public void handleMessage(Message msg) {
    	            super.handleMessage(msg);
    	            switch (msg.what) {
    	                case SUCCESS:
    	                	FileUtils fileUtils = new FileUtils();
    	                	Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/"+applicationDB.user.getUserId()+"/"+department+"/"+ name);
							startActivity(pdfFileIntent);
    	                    break;
    	                case FAIL:
    	                    Toast.makeText(CheckedInfoActivity.this, "连接共享服务器失败", Toast.LENGTH_SHORT).show();
    	                    break;
    	                case 2:
    	                    Toast.makeText(CheckedInfoActivity.this, "拷贝远程文件到本地目录失败:文件不存在", Toast.LENGTH_SHORT).show();
    	                    break;
    	                default:
    	                    super.handleMessage(msg);
    	            }
    	        }
    	    };
		
	}

	
	 /**
     * 从服务器下载文件
     * @param path 下载文件的地址
     * @param FileName 文件名字
     */
	    @SuppressWarnings("deprecation")
		private  void downLoadUrl(final String point, final String name , final String localDirectory , final String serverfilepath) {
	    	final FileUtils fileUtils = new FileUtils();
	    	mDialog = new ProgressDialog(CheckedInfoActivity.this);
	  		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置风格为圆形进度条
	  		mDialog.setMax(100);
	  		mDialog.setMessage("文件下载中....");
	  		mDialog.setIndeterminate(false);//设置进度条是否为不明确
	  		mDialog.setCancelable(false);//设置进度条是否可以按退回键取消
	  		mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
	  			@Override
	  			public void onDismiss(DialogInterface dialog) {
	  				// TODO Auto-generated method stub
	  				mDialog=null;
	  			}
	  		});
	  		mDialog.setButton("终止下载", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						if(mDialog!=null){
					    	mDialog.dismiss();
					    	mDialog=null;
					    	}
					}
	  		});
	  		
	 new Thread(new Runnable() {
	     @Override
	     public void run() {
			FileOutputStream fileOutputStream = null;//文件输出流
			HttpURLConnection con = null;
			 Message msg = new Message();
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
	              File path1 = new File(localDirectory);
	              FileLength = con.getContentLength();
	              DownedFileLength=0;
	             int f = 0;
	             if (con.getResponseCode() == 200) {
	                 InputStream is = con.getInputStream();//获取输入流
	                 boolean check = fileUtils.check(point+"/URL/fileupload"+ serverfilepath);
	                 if(!check){
	                	 Message _msg3 = new Message();
	                	 mDialog.cancel();
					     mDialog=null;
					     _msg3.what = -1;
                		 linkHander.sendMessage(_msg3);
	                 }else if (is != null && check) {
	                	 File createTempFile = File.createTempFile(name, ".pft",path1);
	                	 fileOutputStream =  new FileOutputStream(createTempFile);
	                		 byte[] buf = new byte[1024];
	                		 int ch;
	                		 while ((ch = is.read(buf)) != -1) {
	                			 fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
	                			 DownedFileLength += ch;
	                			 Message _msg1 = new Message();
	                			 _msg1.what = 3;
	                			 linkHander.sendMessage(_msg1);
	                			 if(mDialog != null)	{
	                				 mDialog.setProgress(result);
	                			 }
	                		 }
	                		 File newFile = new File("/"+localDirectory+"/"+ name);
	                		 boolean renameTo = createTempFile.renameTo(newFile);
	                 }
	                 if(mDialog != null){
	                	 mDialog.cancel();
	                	 msg.what = 1;
	                	 linkHander.sendMessage(msg);
	                 }
	                 if (fileOutputStream != null) {
	                     fileOutputStream.flush();
	                     fileOutputStream.close();
	                 }
	             }else{
	            	 mDialog.cancel();
	            	 msg.what = 0;
	                 linkHander.sendMessage(msg);
	             }
			}catch(Exception e){
				mDialog.cancel();
				e.getMessage();
				 msg.what = 2;
	             linkHander.sendMessage(msg);
			}
	     }
	     }).start();
	 mDialog.show();
	        linkHander =new Handler(){
		        @Override
		        public void handleMessage(Message msg) {
		            super.handleMessage(msg);
		            switch (msg.what) {
			            case 0:
			            	Toast.makeText(CheckedInfoActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();	
			            	break;
			            case -1:
			            	Toast.makeText(CheckedInfoActivity.this, "服务器文件损坏,无法进行下载,请确认服务器文件是否有效", Toast.LENGTH_SHORT).show();
			            	break;
		                case 1:
		                	try {
		                		Intent pdfFileIntent = fileUtils.getPdfFileIntent(localDirectory+ "/" + name);
		                		startActivity(pdfFileIntent);
		                	} catch (Exception e) {
		                		Toast.makeText(CheckedInfoActivity.this, "文件下载未完成,无法打开,转为后台下载", Toast.LENGTH_SHORT).show();
		                		e.printStackTrace();
		                	}
		                    break;
		                case 2:
		                    Toast.makeText(CheckedInfoActivity.this, "从服务器下载文件异常", Toast.LENGTH_SHORT).show();
		                    break;
		                case 3:
		                     result=DownedFileLength*100/FileLength;
		                    break;
		                default:
		                    super.handleMessage(msg);
		            }
		        }
		    };
	    }
	
	
	
	protected void onCreate() {
		//序号存入map
		for (int i = 0; i < itemList.size(); i++) {
			itemmap.put(String.valueOf(i), itemList.get(i).get("QAT_NBR").toString());
		}
		itemList1.add(itemmap);
	}

	@Override
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Return, ButtonType.Help};
	}	
	
	@Override
	public boolean OnBtnRtnValidata(ButtonType btnType, View v) {
		return true;
	}

	@Override
	public Object OnBtnRtnClick(ButtonType btnType, View v) {
		//序号map转json转string
		nbr="";
		List<String> cache = new ArrayList<String>();
		JSONObject obj = new JSONObject(itemList1.get(0)); 
		cache.add(obj.toString());  
		nbr = cache.toString(); 
		return biz.updateCheckState(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(),nbr,state);
	}
	@Override
	public void OnBtnRtnCallBack(Object data) {
		//刷新主页面
		Intent intent = new Intent(CheckedInfoActivity.this, FinshOPActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("XSWO_NBR", WO_NBR);  
		bundle.putString("XSWO_UKEY", UKEY);  
		bundle.putString("TURNTIAOMA", tiaoma);
		bundle.putString("SPACE", OPERENVNM);
		bundle.putString("TURNQTY", qty);
		bundle.putString("XSWC_MJCJ", XSWC_MJCJ);
		intent.putExtras(bundle);
		startActivity(intent);	
	}
	@Override
	public String getAppVersion() {
		// TODO Auto-generated method stub
		return applicationDB.user.getUserDate();
	}
	@Override
	protected void unLockNbr() {
		// TODO Auto-generated method stub
	}

	
}
