package com.pinyinsearch.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
	private int appWH = 0;
	public AppListAdapter(Context context){
		mLayoutInflater = LayoutInflater.from(context);
		appWH = (int)context.getResources().getDimension(R.dimen.app_icon_wh);
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
		mViewHolder.iconImg.setBackgroundDrawable(zoomImage(appInfo.getIcon(),appWH,appWH));
		mViewHolder.appNameTxt.setText(appInfo.getAppName());
		mViewHolder.openCntTxt.setText(appInfo.getOpenCnt()>0?appInfo.getOpenCnt()+"":"");
	}
	
	/***
     * 图片的缩放方法
     * @param bgimage 源图片资源
     * @param newWidth 缩放后宽度
     * @param newHeight 缩放后高度
     */
    private Drawable zoomImage(Drawable drawable, double newWidth, double newHeight) {
    	Bitmap bgimage = drawableToBitmap(drawable);
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return new BitmapDrawable(Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true));
    }
    
    private Bitmap drawableToBitmap(Drawable drawable) {
		if(null == drawable){
			return null;
		}
		
		Bitmap bitmap = Bitmap.createBitmap(
			drawable.getIntrinsicWidth(),
			drawable.getIntrinsicHeight(),
			drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);

		return bitmap;

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
