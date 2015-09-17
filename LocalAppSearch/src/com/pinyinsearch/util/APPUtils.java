package com.pinyinsearch.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.pinyinsearch.data.AppInfo;
import com.pinyinsearch.jpinyin.PinyinHelper;

public class APPUtils {
	private APPUtils(){
		
	}
	
	public static List<AppInfo> getAppInfoList(Context context){
		String mySelfPackageName = context.getApplicationInfo().packageName;
		PackageManager packageManager = context.getPackageManager();
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		List<ResolveInfo> resolveInfoList = getResolveInfoList(packageManager);
		String appName = null;
		for(ResolveInfo resolveInfo : resolveInfoList){
			appName = resolveInfo.loadLabel(packageManager).toString();
			AppInfo appInfo = new AppInfo.Builder()
			.setAppName(appName)
			.setFullSpell(PinyinHelper.convertToPinyinString(context, appName, " ").toLowerCase())
			.setAppFirstSpell(PinyinHelper.getShortPinyin(context, appName).toLowerCase())
			.setClassName(resolveInfo.activityInfo.name)
			.setPackageName(resolveInfo.activityInfo.packageName)
			.setIconBytes(drawable2Bytes(resolveInfo.loadIcon(packageManager)))
			.setOpenCnt(0)
			.build();
			
			System.out.println("====== " + appInfo.toString());
			if(!appInfo.getPackageName().equals(mySelfPackageName)){
				// 排除自己
				appInfoList.add(appInfo);
			}
		}
	
		return appInfoList;
	}

	private static List<ResolveInfo> getResolveInfoList(PackageManager packageManager) {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        return packageManager.queryIntentActivities(mainIntent, 0);
	}
	
	private static byte[] drawable2Bytes(Drawable d) {  
        Bitmap bitmap = drawable2Bitmap(d);  
        return bitmap2Bytes(bitmap);  
    }
	
	public static Bitmap drawable2Bitmap(Drawable drawable) {  
        Bitmap bitmap = Bitmap  
                .createBitmap(  
                        drawable.getIntrinsicWidth(),  
                        drawable.getIntrinsicHeight(),  
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                : Bitmap.Config.RGB_565);  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
                drawable.getIntrinsicHeight());  
        drawable.draw(canvas);  
        return bitmap;  
    }
	
	public static byte[] bitmap2Bytes(Bitmap bm) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
        return baos.toByteArray();  
    } 
}
