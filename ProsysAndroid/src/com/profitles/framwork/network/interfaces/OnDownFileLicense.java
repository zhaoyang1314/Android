package com.profitles.framwork.network.interfaces;

import com.profitles.framwork.cusviews.listens.base.BaseListener;

public interface OnDownFileLicense extends BaseListener {
	
	public void onDownload();

	public void onDownloading(float progress);
	
	public void onDownloaded(float size, String path);
}
