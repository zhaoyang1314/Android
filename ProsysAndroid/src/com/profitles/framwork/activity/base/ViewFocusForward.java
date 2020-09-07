package com.profitles.framwork.activity.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.bool;
import android.provider.SyncStateContract.Constants;
import android.view.View;

import com.profitles.framwork.cusviews.view.MyEditText;
import com.profitles.framwork.cusviews.view.MyReadBQ;
import com.profitles.framwork.util.SysConfig;

public class ViewFocusForward {

	private List<List<View>> viewList;
	private Map<View, Integer> mpIndex;
	private int actIndex = 0, unActIndex = SysConfig.Default_Int_Value;
	private boolean isPause = false;
	
	public ViewFocusForward(){
		viewList = new ArrayList<List<View>>();
		mpIndex = new HashMap<View, Integer>();
	}
	
	public ViewFocusForward addItemView(View... v){
		for(int i = 0; i < v.length; i++){
			viewList.add(crtItem(v[i]));
		}
		return this;
	}
	
	public ViewFocusForward addItemGroupView(View... v){
		viewList.add(crtItem(v));
		return this;
	}
	
	private List<View> crtItem(View... v){
		for (int i = 0; i < v.length; i++) {
			mpIndex.put(v[i], mpIndex.size()+i);
		}
		return Arrays.asList(v);
	}
	
	public View actNext(){
		return actByIndex(actIndex+1);
	}
	
	public View actByIndex(int _index){
		if(!isPause){
			actIndex = chgActView(_index, false) ? _index : actIndex;
		}
		return getViewByIndex(_index);
	}
	
	private View getViewByIndex(int _index) {
		return _index < viewList.size() ? viewList.get(_index).get(0) : null;
	}

	public boolean unActNext(){
		return unActByIndex(actIndex-1);
	}
	
	public boolean unActByIndex(int _index){
		boolean isOK = false;
		if(!isPause){
			for(; actIndex > _index; actIndex--){
				isOK = chgActView(actIndex, true);
			}
			unActIndex = SysConfig.Default_Int_Value;
		}else{
			unActIndex = _index;
		}
		return isOK;
	}
	
	public View getActView(){
		return getViewByIndex(actIndex);
	}
	
	public List<View> getActViews(){
		return viewList.get(actIndex);
	}
	
	public int getIndexByView(View v){
		return mpIndex.containsKey(v) ? mpIndex.get(v) : SysConfig.Default_Int_Value;
	}
	
	public boolean isLesActIndex(View v){
		return getIndexByView(v) != SysConfig.Default_Int_Value && getIndexByView(v) < actIndex;
	}
	
	public void setup(int startIndex){
		actIndex = startIndex;
		for (int i = startIndex+1; i < viewList.size(); i++) {
			chgActView(i, true);
		}
		firstGetFocus();
	}
	
	public void setup(){
		setup(0);
	}
	
	private boolean chgActView(int _index, boolean isRdo){
		if(_index < viewList.size() && !isPause){
			List<View> ls = viewList.get(_index);
			for(int i = 0; i < ls.size(); i++){
				if(ls.get(i) instanceof MyEditText){
					((MyEditText)ls.get(i)).setReadOnly(isRdo);
				}else if(ls.get(i) instanceof MyReadBQ){
					((MyReadBQ)ls.get(i)).setReadOnly(isRdo);
				}
			}
			return true;
		}
		return false;
	}
	
	private void firstGetFocus(){
		if(viewList.size() > 0)
		viewList.get(0).get(0).requestFocus();
	}

	public void pause() {
		isPause = true;
	}

	public View unPause() {
		isPause = false;
		unActUnPause();
		return getActView();
	}

	public void goon() {
		isPause = false;
		actNext();
		unActUnPause();
	}
	
	private void unActUnPause(){
		if(unActIndex != SysConfig.Default_Int_Value)
			unActByIndex(unActIndex);
	}
}
