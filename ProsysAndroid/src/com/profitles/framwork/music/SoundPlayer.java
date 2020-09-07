package com.profitles.framwork.music;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.profitles.activity.R;

public class SoundPlayer {

	private static SoundPool soundPool;
	private static Map<Integer, Integer> soundMap; // 音效资源id与加载过后的音源id的映射关系表

	public SoundPlayer(Context context) {
		initSound(context);
	}

	// 初始化音效播放器
	public void initSound(Context context) {
		if(soundPool == null){
			soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
			soundMap = new HashMap<Integer, Integer>();
			soundMap.put(R.drawable.sys_success_mbg,soundPool.load(context, R.drawable.sys_success_mbg, 1));
			soundMap.put(R.drawable.sys_warning_mbg,soundPool.load(context, R.drawable.sys_warning_mbg, 2));
			soundMap.put(R.drawable.sys_error_mbg,soundPool.load(context, R.drawable.sys_error_mbg, 3));
		}
	}

	/**
	 * 播放音效
	 * 
	 * @param resId
	 *            音效资源id
	 */
	public void playSound(int resId) {
		Integer soundId = soundMap.get(resId);
		if (soundId != null)
			soundPool.play(soundId, 1, 1, 1, 0, 1);
	}

	/**
	 * 警告
	 */
	public void warning() {
		playSound(R.drawable.sys_warning_mbg);
	}

	/**
	 * 成功
	 */
	public void success() {
		playSound(R.drawable.sys_success_mbg);// SN006 语音提示修改 2019-07-09 By Brenna 
	}

	/**
	 * 错误
	 */
	public void error() {
		playSound(R.drawable.sys_error_mbg);// SN006 语音提示修改 2019-07-09 By Brenna 
	}
}
