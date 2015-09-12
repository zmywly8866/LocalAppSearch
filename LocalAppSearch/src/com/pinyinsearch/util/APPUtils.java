package com.pinyinsearch.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

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
			.setIcon(resolveInfo.loadIcon(packageManager))
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
}
