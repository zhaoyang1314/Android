package com.profitles.activity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

import com.google.zxing.common.StringUtils;
import com.profitles.biz.FinshOpScanBiz;
import com.profitles.biz.SelfiViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.listens.OnMyDataGridListener;
import com.profitles.framwork.cusviews.view.MyDataGrid;
import com.profitles.framwork.cusviews.view.MyLinearLayout;
import com.profitles.framwork.cusviews.view.MyRelativeLayout;
import com.profitles.framwork.cusviews.view.MyTabHost;
import com.profitles.framwork.cusviews.view.MyTextView;
import com.profitles.framwork.pdfUtil.FileUtils;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;
import com.profitles.framwork.view.Good;
import com.profitles.framwork.view.Goods;
import com.profitles.framwork.view.TableAdapter;
import com.profitles.framwork.view.TableAdapters;

import java.util.ArrayList;    

import android.annotation.SuppressLint;
import android.app.Activity;    
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;    
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;    
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;    
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;    
import android.widget.LinearLayout.LayoutParams;    
import android.widget.ProgressBar;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
@SuppressWarnings(value = { "all" })
public class SelfActivity extends AppFunActivity {
	
	private SelfiViewBiz selBiz;
	List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
	private String domain, site,userid;
	private MyDataGrid item_data,item_datas;
	private MyTabHost data_name;
	private Map<String, Object> datas;
	private String[] POINT = Constants.WebEndPoint.split("/");
	private String SERVER_POINT = "http://"+POINT[2];
	private Handler linkHander;
	private String report_path = "" , message ="", path = "", QAT_RESULT = "";
	private FinshOpScanBiz biz;
	private ApplicationDB applicationDB;
	private int FileLength, X, result;
    private int DownedFileLength=0;
    private ProgressBar progressBar;
    private TextView tv;
	private ProgressDialog mDialog;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_selfchekreport;
	}
	
	@Override
	protected void pageLoad() {
		selBiz = new SelfiViewBiz();
		biz = new FinshOpScanBiz();
		domain 		= ApplicationDB.user.getUserDmain();
		site 		= ApplicationDB.user.getUserSite();
		userid      = ApplicationDB.user.getUserId();
		data_name= ((MyTabHost) this.findViewById(R.id.tbl_pkAdv));
		data_name.setup();
		item_data = (MyDataGrid) findViewById(R.id.d_list) ;
		item_datas = (MyDataGrid) findViewById(R.id.y_list) ;
	/*	progressBar=(ProgressBar) findViewById(R.id.progressBar);
		tv = (TextView) findViewById(R.id.textView);*/
		getItemsByNull();
		setContentListener();
		setItemListener();
		
	}

	private void getItemsByNull(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return selBiz.getSelfItem(domain, site, userid);
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					itemList = wr.getDataToList();
					if(!StringUtil.isEmpty(itemList)&&itemList.size()>0){
						for (int i = 0; i < itemList.size(); i++) {
							Map<String, Object> mp = (Map<String, Object>) itemList.get(i);
							mp.put("INDEXS", StringUtil.parseInt(mp.get("INDEXS")));
						}
						item_data.buildData(itemList);
					}
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	private void getItemsByNotNull(){
		loadDataBase(new IRunDataBaseListens() {
			@Override
			public boolean onValidata() {
				return true;
			}
			
			@Override
			public Object onGetData() {
				return selBiz.getSelfItems(domain, site, userid);
			}
			
			@Override
			public void onCallBrack(Object data) {
				WebResponse wr=(WebResponse) data;
				if(wr.isSuccess()){
					itemList = wr.getDataToList();
					if(!StringUtil.isEmpty(itemList)&&itemList.size()>0){
						for (int i = 0; i < itemList.size(); i++) {
							Map<String, Object> mp = (Map<String, Object>) itemList.get(i);
							mp.put("INDEXS", StringUtil.parseInt(mp.get("INDEXS")));
						}
						item_datas.buildData(itemList);
						setItemListyener();
					}
				}else{
					showErrorMsg(wr.getMessages());
				}
			}
		});
	}
	
	private void setContentListener() {
		data_name.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if(tabId.equals("y_view")){
					getItemsByNotNull();
				}else if(tabId.equals("d_view")){
					getItemsByNull();
				}
				
			}
			
		});
	}
	private void setItemListener() {
		item_data.setOnMyDataGridListener(new OnMyDataGridListener() {
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowDatas) {
				return false;
			}
			public void onItemClick(View view, Object val,  int rowIndex, int colIndex ,Map<String, Object> rowData) {
				if(rowIndex!=0){
						datas = rowData;
//						view.setBackgroundColor(Color.YELLOW);
						final String PART = rowData.get("QAT_OP_PART")+"" ;
						AlertDialog alert=new AlertDialog.Builder(SelfActivity.this).create();
						alert.setTitle("序号:"+ StringUtil.isEmpty(rowData.get("INDEXS")+"", "")); 
						alert.setMessage("零件:"+ StringUtil.isEmpty(rowData.get("QAT_OP_PART")+"", "")+"\r\n"+"日期:"+ StringUtil.isEmpty(rowData.get("QAT_CSP_DATE")+"", ""));
						alert.setButton(DialogInterface.BUTTON_POSITIVE,"查自检", new DialogInterface.OnClickListener() {     
					          @Override
					          public void onClick(DialogInterface arg0, int arg1) {
					        	 Intent i = new Intent(SelfActivity.this , PsiSelfActivity.class);
								 Bundle bundle = new Bundle();
								 bundle.putString("part", StringUtil.isEmpty(datas.get("QAT_OP_PART")+"", ""));
								 bundle.putString("nbr", StringUtil.isEmpty(datas.get("QAT_NBR")+"", ""));
								 i.putExtras(bundle); 
								 startActivity(i);
					             //finish();
					          } 
					      });
						alert.setButton(DialogInterface.BUTTON_NEUTRAL,"检验结果",new DialogInterface.OnClickListener(){
			
							@Override
							public void onClick(DialogInterface dialog, int which) {
								 Intent i = new Intent(SelfActivity.this , SelfInsertActivity.class);
								 Bundle bundle = new Bundle();
								 bundle.putString("nbr", StringUtil.isEmpty(datas.get("QAT_NBR")+"", ""));
								 bundle.putString("part", StringUtil.isEmpty(datas.get("QAT_OP_PART")+"", ""));
								 bundle.putString("cust", StringUtil.isEmpty(datas.get("QAT_CUST_PART")+"", ""));
								 bundle.putString("dates", StringUtil.isEmpty(datas.get("QAT_CSP_DATE")+"", ""));
								 bundle.putString("desc", StringUtil.isEmpty(datas.get("QAT_OP_PART_DESC")+"", ""));
								 bundle.putString("users", StringUtil.isEmpty(datas.get("QAT_CSP_USER")+"", ""));
								 bundle.putString("scan", StringUtil.isEmpty(datas.get("QAT_SCAN")+"", ""));
								 bundle.putString("sn", StringUtil.isEmpty(datas.get("QAT_SN")+"", ""));
								 i.putExtras(bundle); 
								 startActivity(i);
								 finish();
							}
							
							
						});
						alert.setButton(DialogInterface.BUTTON_NEGATIVE,"零件图纸",new DialogInterface.OnClickListener(){     
					          @Override
					          public void onClick(DialogInterface arg0, int arg1) {loadDataBase(new IRunDataBaseListens() {
									@Override
									public boolean onValidata() {
										return true;
									}
									@Override
									public Object onGetData() {
										return biz.getPartDraw(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(),PART);
									}
									@SuppressWarnings("resource")
									@Override
									public void onCallBrack(Object data) {
										WebResponse wrb = (WebResponse) data;
										if (wrb.isSuccess()) {
											Map<String, Object> dataToMap = wrb.getDataToMap();
											String PART_DRAW = dataToMap.get("PART_DRAW") + "";
											String PATH = dataToMap.get("PATH") + "";   //获取保存的时候的文件地址
											FileUtils fileUtils = new FileUtils();
											String[] split = PART_DRAW.split("/");
											String	PART_DRAW_NM = split[4]; 
											boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/ProfitMES/",PART_DRAW_NM);
											if(!fileIsExists){
													downLoadUrl(SERVER_POINT,PART_DRAW_NM, "/sdcard/profit/ProfitMES/", PART_DRAW);
											}else{
												Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/ProfitMES/"+PART_DRAW_NM);
												startActivity(pdfFileIntent);
											}
										} else {
											showMessage(wrb.getMessages());
										}
									}

								});} 
					      });
						alert.show();
				}
				//Intent i = new Intent(SelfActivity.this , PsiSelfActivity.class);
				//Bundle bundle = new Bundle();
				//bundle.putString("users", StringUtil.isEmpty(rowData.get("QAT_CSP_USER")+"", ""));
				//bundle.putString("part", StringUtil.isEmpty(rowData.get("QAT_OP_PART")+"", ""));
				//bundle.putString("rev", StringUtil.isEmpty(rowData.get("QAT_REV")+"", ""));
				//bundle.putString("desc", StringUtil.isEmpty(rowData.get("QAT_OP_PART_DESC")+"", ""));
				//bundle.putString("date", StringUtil.isEmpty(rowData.get("QAT_CSP_DATE")+"", ""));
				//bundle.putString("index", StringUtil.isEmpty(rowData.get("INDEXS")+"", ""));
				//bundle.putString("time", StringUtil.isEmpty(rowData.get("QAT_CSP_TIME")+"", ""));
				//bundle.putString("cust", StringUtil.isEmpty(rowData.get("QAT_CUST_PART")+"", ""));
				//i.putExtras(bundle);
				//startActivity(i);
			}
		});
		
	}
	
	private void setItemListyener() {
		item_datas.setOnMyDataGridListener(new OnMyDataGridListener() {
			public boolean onItemLongClick(View view, Object val,  int rowIndex, int colIndex,Map<String, Object> rowDatas) {
				datas = rowDatas;
				final String QAT_SN =	datas.get("QAT_SN")+"";
				final String QAT_SCAN = datas.get("QAT_SCAN")+"";
				final String QAT_SECD_RESULT = datas.get("QAT_SECD_RESULT")+"";
				QAT_RESULT = "";
				AlertDialog.Builder builder = new AlertDialog.Builder(SelfActivity.this);
	            builder.setTitle("请填写序号:"+ StringUtil.isEmpty(datas.get("INDEXS")+"", "") +",SN码:"+QAT_SN+"的备注:");
	            // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
	            View viewC = LayoutInflater.from(SelfActivity.this).inflate( R.layout.dialog, null);
	            // 设置我们自己定义的布局文件作为弹出框的Content
	            builder.setView(viewC);
	            final EditText username = (EditText) viewC.findViewById(R.id.edUsername);
	            if(!StringUtil.isEmpty(QAT_SECD_RESULT))username.setText(QAT_SECD_RESULT);

	            builder.setPositiveButton("保存",
	                    new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                        	loadDataBase(new IRunDataBaseListens(){

									@Override
									public boolean onValidata() {
										if(StringUtil.isEmpty(username.getText()+"")){
											showErrorMsg("输入框数据不能为空");
											return false;
										}
										QAT_RESULT = username.getText()+"";
										return true;
									}

									@Override
									public Object onGetData() {
										return selBiz.updateScdResult(domain, site, QAT_SN, QAT_SCAN, QAT_RESULT);
									}

									@Override
									public void onCallBrack(Object data) {
										WebResponse wrs = (WebResponse) data;
										if(wrs.isSuccess()){
											showMessage(wrs.getMessages());
										}else{
											showErrorMsg(wrs.getMessages());
										}
										
									}
	                        		
	                        	});
	                            //确定操作的内容
	                        }
	                    });

	            builder.setNegativeButton("取消",
	                    new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            // TODO Auto-generated method stub
	                            Toast.makeText(SelfActivity.this, "取消输入成功",
	                                    Toast.LENGTH_SHORT).show();
	                        }
	                    });
	            builder.show();
				return true;
			}
			public void onItemClick(View view, Object val,  int rowIndex, int colIndex ,Map<String, Object> rowData) {
				datas = rowData;
//				view.setBackgroundColor(Color.YELLOW);
				AlertDialog alert=new AlertDialog.Builder(SelfActivity.this).create();
				alert.setTitle("序号:"+ StringUtil.isEmpty(rowData.get("INDEXS")+"", "")); 
				alert.setMessage("零件:"+ StringUtil.isEmpty(rowData.get("QAT_OP_PART")+"", "")+"\r\n"+"日期:"+ StringUtil.isEmpty(rowData.get("QAT_CSP_DATE")+"", ""));
				final String REP_PATH = 	datas.get("QAT_REP_PATH")+"";
				final String PART = rowData.get("QAT_OP_PART")+"";
				message = "";
				loadDataBase(new IRunDataBaseListens(){
					
					@Override
					public boolean onValidata() {
						// TODO Auto-generated method stub
						return true;
					}
					
					@Override
					public Object onGetData() {
						
						return selBiz.checkReport(domain, site, REP_PATH);
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
			        	 Intent i = new Intent(SelfActivity.this , PsiSelfActivity.class);
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
			        			  try {
									Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/ProfitMES"+"/"+REP_PATH);
									  startActivity(pdfFileIntent);
								} catch (Exception e) {
									showErrorMsg("文件损坏无法进行打开");
									e.printStackTrace();
								}
			        		  }
			        	  }
			        	  
			          } 
			      });
				alert.setButton(DialogInterface.BUTTON_NEGATIVE,"零件图纸", new DialogInterface.OnClickListener() {     
			          @Override
			          public void onClick(DialogInterface arg0, int arg1) {loadDataBase(new IRunDataBaseListens() {
							@Override
							public boolean onValidata() {
								return true;
							}
							@Override
							public Object onGetData() {
								return biz.getPartDraw(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(),PART);
							}
							@SuppressWarnings("resource")
							@Override
							public void onCallBrack(Object data) {
								WebResponse wrb = (WebResponse) data;
								if (wrb.isSuccess()) {
									Map<String, Object> dataToMap = wrb.getDataToMap();
									String PART_DRAW = dataToMap.get("PART_DRAW") + "";
									String PATH = dataToMap.get("PATH") + "";   //获取保存的时候的文件地址
									FileUtils fileUtils = new FileUtils();
									String[] split = PART_DRAW.split("/");
									String	PART_DRAW_NM = split[4]; 
									boolean fileIsExists = fileUtils.fileIsExists("/sdcard/profit/ProfitMES",PART_DRAW_NM);
									if(!fileIsExists){
											downLoadUrl(SERVER_POINT,PART_DRAW_NM, "/sdcard/profit/ProfitMES", PART_DRAW);
									}else{
										Intent pdfFileIntent = fileUtils.getPdfFileIntent("sdcard/profit/ProfitMES"+"/"+PART_DRAW_NM);
										startActivity(pdfFileIntent);
									}
								} else {
									showMessage(wrb.getMessages());
								}
							}

						});} 
			      });
				alert.show();
			}
		});
		
	}

	
	 /**
     * 从服务器下载文件
     * @param path 下载文件的地址
     * @param FileName 文件名字
     */
    @SuppressLint("HandlerLeak")
	private  void downLoadUrl(final String point, final String name , final String localDirectory , final String serverfilepath) {
	      		mDialog = new ProgressDialog(SelfActivity.this);
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
	     final	FileUtils fileUtils = new FileUtils();	
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
	                 if (con.getResponseCode() == 200) { // 测试连接是否接通
	                     InputStream is = con.getInputStream();//获取输入流
	                     boolean check = fileUtils.check(point+"/URL/fileupload"+ serverfilepath); // 判断服务器上面的文件是否损坏
	                     if(!check){
	                    	 Message _msg3 = new Message();
	                    	 mDialog.cancel();
						     mDialog=null;
	                    	 _msg3.what = -1;
	                    	 linkHander.sendMessage(_msg3);
	                     }else
	                     if (is != null && check) {
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
				            	Toast.makeText(SelfActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
				            	break;
			                case 1:
			                	try {
			                		Intent pdfFileIntent = fileUtils.getPdfFileIntent(localDirectory+ "/" + name);
			                		startActivity(pdfFileIntent);
			                	} catch (Exception e) {
			                		Toast.makeText(SelfActivity.this, "文件下载未完成,无法打开,转为后台下载", Toast.LENGTH_SHORT).show();
			                		e.printStackTrace();
			                	}
			                    break;
			                case -1:
				            	Toast.makeText(SelfActivity.this, "服务器文件损坏,无法进行下载,请确认服务器文件是否有效", Toast.LENGTH_SHORT).show();
				            	break;
			                case 2:
			                    Toast.makeText(SelfActivity.this, "从服务器下载文件异常", Toast.LENGTH_SHORT).show();
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
   

	@Override
	protected void unLockNbr() {
		
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
		return null;
	}
	@Override
	public void OnBtnRtnCallBack(Object data) {
		Intent intent = new Intent(SelfActivity.this, MenuActivity.class);
		startActivity(intent);	
	}

	@Override
	public String getAppVersion() {
		return ApplicationDB.user.getUserDate();
	}
	
}