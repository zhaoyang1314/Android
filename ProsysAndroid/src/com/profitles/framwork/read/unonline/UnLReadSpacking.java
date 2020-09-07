package com.profitles.framwork.read.unonline;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;
import com.profitles.framwork.read.ReadSpackingBase;
import com.profitles.framwork.read.online.ApkInstaller;

public class UnLReadSpacking extends ReadSpackingBase{

	private ApkInstaller mInstaller ;
	private String msg;
	private Context context;

	public UnLReadSpacking(Activity activity) {
		init(activity);
	}

	@Override
	public boolean init(Activity activity) {
		context = activity;
		if(SpeechUtility.getUtility() == null)
			SpeechUtility.createUtility(activity, SpeechConstant.APPID +"=577f6a14");
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(activity, mTtsInitListener);
		if(msg != null) return false;

		mInstaller = new  ApkInstaller(activity);
		return true;
	}

	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				msg = "初始化失败,错误码："+code;
        	} else {
				// 初始化成功，之后可以调用startSpeaking方法
        		// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
        		// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}
		}
	};

	@Override
	public boolean spacking(String text) {
		setParam();
		int code = mTts.startSpeaking(text, mTtsListener);
		if (code != ErrorCode.SUCCESS) {
			if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
				//未安装则跳转到提示安装页面
				mInstaller.install();
			}else {
				msg = "语音合成失败,错误码: " + code;	
			}
		}
		return false;
	}	

	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		
		@Override
		public void onSpeakResumed() {
		}
		
		@Override
		public void onSpeakProgress(int arg0, int arg1, int arg2) {
		}
		
		@Override
		public void onSpeakPaused() {
		}
		
		@Override
		public void onSpeakBegin() {
		}
		
		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		}
		
		@Override
		public void onCompleted(SpeechError arg0) {
			System.out.println("OK");
		}
		
		@Override
		public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		}
	};
	
	public void setmTtsListener(SynthesizerListener mTtsListener) {
		this.mTtsListener = mTtsListener;
	}
	
	/**
	 * 参数设置
	 * @param param
	 * @return
	 */
	private void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		//设置合成
		//设置使用本地引擎
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		//设置发音人资源路径
		mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
		//设置发音人
		mTts.setParameter(SpeechConstant.VOICE_NAME, UnLReadCfgOnLine.voicerLocal);
		//设置合成语速
		mTts.setParameter(SpeechConstant.SPEED, UnLReadCfgOnLine.speed_preference);
		//设置合成音调
		mTts.setParameter(SpeechConstant.PITCH, UnLReadCfgOnLine.pitch_preference);
		//设置合成音量
		mTts.setParameter(SpeechConstant.VOLUME, UnLReadCfgOnLine.volume_preference);
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, UnLReadCfgOnLine.stream_preference);
		
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
		
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
	}

	//获取发音人资源路径
	private String getResourcePath(){
		StringBuffer tempBuffer = new StringBuffer();
		//合成通用资源
		tempBuffer.append(ResourceUtil.generateResourcePath(context, RESOURCE_TYPE.assets, "tts/common.jet"));
		tempBuffer.append(";");
		//发音人资源
		tempBuffer.append(ResourceUtil.generateResourcePath(context, RESOURCE_TYPE.assets, "tts/"+UnLReadCfgOnLine.voicerLocal+".jet"));
		return tempBuffer.toString();
	}
	
	public void destroy() {
		mTts.stopSpeaking();
		mTts.destroy();
	}
}
