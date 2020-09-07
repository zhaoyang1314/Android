package com.profitles.framwork.read.online;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.profitles.framwork.read.ReadSpackingBase;

public class ReadSpacking extends ReadSpackingBase{
	
	private ApkInstaller mInstaller ;
	private String msg;

	public ReadSpacking(Activity activity) {
		init(activity);
	}

	@Override
	public boolean init(Activity activity) {
		if(SpeechUtility.getUtility() == null)
			SpeechUtility.createUtility(activity, SpeechConstant.APPID +"=577f6a14");
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(activity, mTtsInitListener);
		if(msg != null) return false;
		
		mInstaller = new  ApkInstaller(activity);
		return false;
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

	public String getMsg() {
		return msg;
	}

	@Override
	public boolean spacking(String text) {// 移动数据分析，收集开始合成事件
//		FlowerCollector.onEvent(context, "tts_play");
		
		// 设置参数
		setParam();
		int code = mTts.startSpeaking(text, mTtsListener);
//		/** 
//		 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//		 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//		*/
//		String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//		int code = mTts.synthesizeToUri(text, path, mTtsListener);
		if (code != ErrorCode.SUCCESS) {
			if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
				//未安装则跳转到提示安装页面
				mInstaller.install();
			}else {
				msg = "语音合成失败,错误码: " + code;	
			}
		}
		return true;
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
		// 根据合成引擎设置相应参数
		if(ReadCfgOnLine.mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, ReadCfgOnLine.voicer);
			//设置合成语速
			mTts.setParameter(SpeechConstant.SPEED, ReadCfgOnLine.speed_preference);
			//设置合成音调
			mTts.setParameter(SpeechConstant.PITCH, ReadCfgOnLine.pitch_preference);
			//设置合成音量
			mTts.setParameter(SpeechConstant.VOLUME, ReadCfgOnLine.volume_preference);
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			// 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
			/**
			 * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
			 * 开发者如需自定义参数，请参考在线合成参数设置
			 */
		}
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, ReadCfgOnLine.stream_preference);
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
		
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
	}
	
	public void destroy() {
		mTts.stopSpeaking();
		mTts.destroy();
	}
}
