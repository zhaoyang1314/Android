package com.profitles.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;













import android.widget.TextView;
import android.widget.Toast;

import com.profitles.biz.HandoverItemBiz;
import com.profitles.biz.LineWoListViewBiz;
import com.profitles.biz.PDFViewBiz;
import com.profitles.biz.WoListViewBiz;
import com.profitles.framwork.activity.AppFunActivity;
import com.profitles.framwork.activity.AppFunActivity.ButtonType;
import com.profitles.framwork.activity.base.rdb.IRunDataBaseListens;
import com.profitles.framwork.activity.listens.OnShowConfirmListen;
import com.profitles.framwork.activity.util.WebResponse;
import com.profitles.framwork.appdb.ApplicationDB;
import com.profitles.framwork.cusviews.view.MyButton;
import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyViewGroup;
import com.profitles.framwork.pdfUtil.FileUtils;
import com.profitles.framwork.util.Constants;
import com.profitles.framwork.util.StringUtil;


public class PDFViewActivity extends AppFunActivity {
	private PDFViewBiz biz1;
	private LinearLayout mytable;
	private MyEditText myText;
	private MyEditText txv_pDesc;
	private MyEditText kh;
	private MyEditText khjh;
	private Handler linkHander;
	private String[] POINT = Constants.WebEndPoint.split("/");
	private String SERVER_POINT = "http://"+POINT[2];
	private ApplicationDB applicationDB;
	private List<Map<String, Object>> dateList = new ArrayList<Map<String,Object>>();
	private int FileLength, X, result;
    private int DownedFileLength=0;
    private ProgressBar progressBar;
    private TextView tv;
	private ProgressDialog mDialog;
	@Override
	protected int getMainBodyLayout() {
		return R.layout.act_pdfpage;
	}
	@Override
	protected void pageLoad() {
		biz1=new PDFViewBiz();
		mytable=(LinearLayout) findViewById(R.id.MyTable2);
		myText=(MyEditText) findViewById(R.id.ext_pdfPart);
		txv_pDesc=(MyEditText) findViewById(R.id.txv_pDesc);
		kh=(MyEditText) findViewById(R.id.ext_kh);
		khjh=(MyEditText) findViewById(R.id.txv_khjh);
	}
	
    //点击查询
		@Override
		public boolean OnBtnSerValidata(ButtonType btnType, View v) {
			if(!StringUtil.isEmpty(myText.getValStr())&&!StringUtil.isEmpty(kh.getValStr())){
				showErrorMsg("零件号或者零件描述不能和客户或者客户件号同时作为查询条件");
				return false;
			}
			if(!StringUtil.isEmpty(myText.getValStr())&&!StringUtil.isEmpty(khjh.getValStr())){
				showErrorMsg("零件号或者零件描述不能和客户或者客户件号同时作为查询条件");
				return false;
			}
			if(!StringUtil.isEmpty(txv_pDesc.getValStr())&&!StringUtil.isEmpty(kh.getValStr())){
				showErrorMsg("零件号或者零件描述不能和客户或者客户件号同时作为查询条件");
				return false;
			}
			if(!StringUtil.isEmpty(txv_pDesc.getValStr())&&!StringUtil.isEmpty(khjh.getValStr())){
				showErrorMsg("零件号或者零件描述不能和客户或者客户件号同时作为查询条件");
				return false;
			}
			return true;
		}

		@Override
		public Object OnBtnSerClick(ButtonType btnType, View v) {
			return biz1.serPartInfo(applicationDB.user.getUserDmain(), applicationDB.user.getUserSite(), myText.getValStr()+"",txv_pDesc.getValStr()+"",kh.getValStr()+"",khjh.getValStr()+"");
		}
		@Override
		public void OnBtnSerCallBack(Object data) {
		  WebResponse wrs = (WebResponse) data;
			if(wrs.isSuccess()){
				dateList=wrs.getDataToList();
				if(!StringUtil.isEmpty(dateList) && dateList.size()>0){
					
					setValue();
				}else{
					showErrorMsg("未找到相关零件数据");
				}
			}else{
				showErrorMsg(wrs.getMessages());
			}
		}
	
	
	 

	
	@SuppressLint("NewApi")
	public void setValue(){	
		mytable.removeAllViews();
		 LinearLayout mytable2 = (LinearLayout)mytable;
		 mytable2.removeAllViews();
		  DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);
		  int width = dm.widthPixels;
		  int height=dm.heightPixels;
		 
		  
		 //这里创建按钮，每行放置n个按钮  
			  for (int i=0; i<dateList.size();) {  
				  LinearLayout mytable1=new LinearLayout(this);
				  Button but=new Button(this);
				  Button but1=new Button(this);
				  Button but2=new Button(this);
			        //将LIST数据填充到按钮中
			        but.setText(dateList.get(i).get("XSPT_PART")+""+"_"+dateList.get(i).get("XSPT_DESC1")+""); 
			        but.setTextSize(width/100);
			        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width/3-60,height/4-40);	 
			     
				    lp.setMargins(20, 20, 20, 20);
			        mytable1.addView(but,lp);
			        mytable1.setGravity(20);
			        mytable1.setOrientation(LinearLayout.HORIZONTAL);
                     //将按钮放入layout组件
			        //为按钮设置一个.标记，来确认是按下了哪一个按钮
			        String line=but.getText().toString();
			         final String part=line.substring(0, line.indexOf("_")); 
					 final int cut=i;
				     but.setOnClickListener(new Button.OnClickListener() {
								@Override
							   public void onClick(View v) {
									getPdf(part);
							 }
					 });
				     i++;
				     if(i>=dateList.size()){
				    	 mytable2.addView(mytable1);
				    	 break;
				     }
				     if(!StringUtil.isEmpty(dateList.get(i).get("XSPT_PART")+"")){
				    	//将LIST数据填充到按钮中
					        but1.setText(dateList.get(i).get("XSPT_PART")+""+"_"+dateList.get(i).get("XSPT_DESC1")+""); 
					        but1.setTextSize(width/100);
					        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width/3-60,height/4-40);	 
					      
						    lp1.setMargins(20, 20, 20, 20);
					        mytable1.addView(but1,lp1);
					        mytable1.setOrientation(LinearLayout.HORIZONTAL);
					        mytable1.setGravity(20);
		                     //将按钮放入layout组件
					        //为按钮设置一个.标记，来确认是按下了哪一个按钮
					        String line1=but1.getText().toString();
					         final String part1=line1.substring(0, line1.indexOf("_")); 
							 final int cut1=i;
							 but1.setOnClickListener(new Button.OnClickListener() {
										@Override
									   public void onClick(View v) {
											getPdf(part1);
									 }
							 });
						     i++;
						     if(i>=dateList.size()){
						    	 mytable2.addView(mytable1);
						    	 break;
						     }
					  }
				     if(!StringUtil.isEmpty(dateList.get(i).get("XSPT_PART")+"")){
					    	//将LIST数据填充到按钮中
						        but2.setText(dateList.get(i).get("XSPT_PART")+""+"_"+dateList.get(i).get("XSPT_DESC1")+""); 
						        but2.setTextSize(width/100);
						        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(width/3-60,height/4-40);	 
						        
							    lp2.setMargins(20, 20, 20, 20);
						        mytable1.addView(but2,lp2);
						        mytable1.setOrientation(LinearLayout.HORIZONTAL);
						        mytable1.setGravity(20);
			                     //将按钮放入layout组件
						        //为按钮设置一个.标记，来确认是按下了哪一个按钮
						        String line2=but2.getText().toString();
						         final String part2=line2.substring(0, line2.indexOf("_")); 
								 final int cut2=i;
								 but2.setOnClickListener(new Button.OnClickListener() {
											@Override
										   public void onClick(View v) {
												getPdf(part2);
										 }
								 });
							     i++;
							     if(i>=dateList.size()){
							    	 mytable2.addView(mytable1);
							    	 break;
							     }
						  }
					    mytable2.addView(mytable1);
			  }
			  
	  }
	
	     //点击返回
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
			Intent intent = new Intent(PDFViewActivity.this, MenuActivity.class);
			startActivity(intent);	
		}
		
		
		 
		// 获得交接项记录
		 private void  getPdf(final String part){
				loadDataBase(new IRunDataBaseListens() {
					@Override
					public boolean onValidata() {
						return true;
					}
					@Override
					public Object onGetData() {
						return biz1.getPartDraw(applicationDB.user.getUserDmain(),applicationDB.user.getUserSite(),part);
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
				});	 
			}	
		 
		 /**
		     * 从服务器下载文件
		     * @param path 下载文件的地址
		     * @param FileName 文件名字
		     */
			    @SuppressWarnings("deprecation")
				private  void downLoadUrl(final String point, final String name , final String localDirectory , final String serverfilepath) {
					final FileUtils fileUtils = new FileUtils();
			    	mDialog = new ProgressDialog(PDFViewActivity.this);
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
					            	Toast.makeText(PDFViewActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
					            	break;
				                case 1:
				                	try {
				                		Intent pdfFileIntent = fileUtils.getPdfFileIntent(localDirectory+ "/" + name);
				                		startActivity(pdfFileIntent);
				                	} catch (Exception e) {
				                		Toast.makeText(PDFViewActivity.this, "文件下载未完成,无法打开,转为后台下载", Toast.LENGTH_SHORT).show();
				                		e.printStackTrace();
				                	}
				                    break;
				                case -1:
					            	Toast.makeText(PDFViewActivity.this, "服务器文件损坏,无法进行下载,请确认服务器文件是否有效", Toast.LENGTH_SHORT).show();
					            	break;
				                case 2:
				                    Toast.makeText(PDFViewActivity.this, "从服务器下载文件异常", Toast.LENGTH_SHORT).show();
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
	protected ButtonType[] getNeedBtn(){
		return new ButtonType[]{ButtonType.Search,ButtonType.Return, ButtonType.Help};
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
