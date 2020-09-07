package com.profitles.framwork.read;

import android.app.Activity;

import com.iflytek.cloud.SpeechSynthesizer;


public abstract class ReadSpackingBase {
	// 语音合成对象
	protected SpeechSynthesizer mTts;

	public abstract boolean init(Activity activity);
	public abstract boolean spacking(String text);
	public abstract void destroy();
}
