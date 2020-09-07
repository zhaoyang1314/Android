package com.profitles.framwork.activity.forword;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class ActForwordMgr {

	private static List<Activity> lsact;
	
	private void _forword(Activity from, Class<? extends Activity> to) {
		Intent intent = new Intent();
		intent.setClass(from, to);
		from.startActivity(intent);
		addActive(from);
	}
	
	public void forword(Activity from, Class<? extends Activity> to) {
		_forword(from, to);
		addActive(from);
	}
	
	public void forwordFinish(Activity from, Class<? extends Activity> to) {
		_forword(from, to);
		from.finish();
	}
	
	private void addActive(Activity act){
		lsact = lsact == null ? new ArrayList<Activity>() : lsact;
		lsact.add(act);
	}
	
	public void exit(){
		while (lsact != null && lsact.size() > 0){
			lsact.get(0).finish();
			lsact.remove(0);
		}
	}
}
