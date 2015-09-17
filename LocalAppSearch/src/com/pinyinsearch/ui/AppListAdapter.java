package com.pinyinsearch.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinyinsearch.data.AppInfo;
import com.uperone.pinyinsearch.R;

public class AppListAdapter extends BaseAdapter {
	private List<AppInfo> mAppInfoList = null;
	private LayoutInflater mLayoutInflater = null;
	private ViewHolder mViewHolder = null;
	private int itemWH = 0;
	public AppListAdapter(Context context){
		mLayoutInflater = LayoutInflater.from(context);
		itemWH = (int)context.getResources().getDimension(R.dimen.app_icon_wh);
	}
	
	@Override
	public int getCount() {
		return isListEmpty()?0:mAppInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return isListEmpty()?null:mAppInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return isListEmpty()?0:position;
	}
	
	public void setAppInfoList(List<AppInfo> appInfoList){
		mAppInfoList = appInfoList;
		notifyDataSetChanged();
	}
	
	public List<AppInfo> getAppInfoList(){
		return mAppInfoList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(isListEmpty()){
			return convertView;
		}
		
		if(null == convertView){
			mViewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.adapter_app_item, null);
			mViewHolder.appNameTxt = (TextView)convertView.findViewById(R.id.appNameTxtId);
			mViewHolder.iconImg = (ImageView)convertView.findViewById(R.id.iconImgId);
			mViewHolder.openCntTxt = (TextView)convertView.findViewById(R.id.openCntTxtId);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder)convertView.getTag();
		}
		
		showItem(mAppInfoList.get(position));
		
		return convertView;
	}
	
	private void showItem(AppInfo appInfo){
		mViewHolder.iconImg.setBackgroundDrawable(new BitmapDrawable(decodeBitmap(appInfo,itemWH, itemWH)));
		mViewHolder.appNameTxt.setText(appInfo.getAppName());
		mViewHolder.openCntTxt.setText(appInfo.getOpenCnt()>0?appInfo.getOpenCnt()+"":"");
	}
	
    private Bitmap decodeBitmap(AppInfo appInfo, int reqWidth, int reqHeight){
    	final BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	BitmapFactory.decodeByteArray(appInfo.getIconBytes(), 0, appInfo.getIconBytes().length, options);
    	options.inSampleSize=calculateInSampleSize(options, reqWidth, reqHeight);
    	options.inJustDecodeBounds = false;
    	
    	return BitmapFactory.decodeByteArray(appInfo.getIconBytes(), 0, appInfo.getIconBytes().length, options);
    }
    
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
    	final int width = options.outWidth;
    	final int height = options.outHeight;
    	int inSampleSize = 1;
    	
    	if(height > reqHeight || width > reqWidth){
    		final int halfWidth = width/2;
    		final int halfHeight = height/2;
    		
    		while((halfHeight/inSampleSize)>=reqHeight&&(halfWidth/inSampleSize)>=reqWidth){
    			inSampleSize *= 2;
    		}
    	}
    	
    	return inSampleSize;
    }

	
	class ViewHolder{
		TextView appNameTxt = null;
		ImageView iconImg = null;
		TextView openCntTxt = null;
	}

	private boolean isListEmpty(){
		return null==mAppInfoList||mAppInfoList.size()==0;
	}
}
