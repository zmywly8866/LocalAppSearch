package com.pinyinsearch.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class APPManager {
	private APPInfoDBHelper mAPPInfoDBHelper = null;
	private SQLiteDatabase mSqliteDatabase = null;
	
	public APPManager(Context context){
		mAPPInfoDBHelper = new APPInfoDBHelper(context, APPInfoDBHelper.DB_NAME, null, APPInfoDBHelper.DB_VERSION);
		mSqliteDatabase = mAPPInfoDBHelper.getWritableDatabase();
	}
	
	public List<AppInfo> getAppInfoList(){
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		Cursor cursor = mSqliteDatabase.rawQuery("select * from " + APPInfoDBHelper.TABLE_NAME + " order by " + APPInfoDBHelper.APP_OPEN_CNT + " DESC", null);
		if(null != cursor){
			while(cursor.moveToNext()){
				AppInfo appInfo = new AppInfo.Builder()
				.setAppName(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_NAME)))
				.setAppFirstSpell(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_FIRST_SPELL)))
				.setClassName(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_CLASS_NAME)))
				.setFullSpell(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_FULL_SPELL)))
				.setPackageName(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_PACKAGE_NAME)))
				.setTags(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_TAGS)))
				.setIconBytes(cursor.getBlob(cursor.getColumnIndex(APPInfoDBHelper.APP_ICON)))
				.setOpenCnt(cursor.getInt(cursor.getColumnIndex(APPInfoDBHelper.APP_OPEN_CNT)))
				.build();
				
				appInfoList.add(appInfo);
			}
			
			cursor.close();
		}
		
		return appInfoList;
	}
	
	private AppInfo getAppInfo(String appName){
		AppInfo appInfo = null;
		Cursor cursor = mSqliteDatabase.rawQuery("select * from " + APPInfoDBHelper.TABLE_NAME
				+ " where "
				+ APPInfoDBHelper.APP_NAME + " =\"" + appName + "\"", null);
		if(null != cursor){
			while(cursor.moveToNext()){
				appInfo = new AppInfo.Builder()
				.setAppName(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_NAME)))
				.setAppFirstSpell(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_FIRST_SPELL)))
				.setClassName(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_CLASS_NAME)))
				.setFullSpell(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_FULL_SPELL)))
				.setPackageName(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_PACKAGE_NAME)))
				.setTags(cursor.getString(cursor.getColumnIndex(APPInfoDBHelper.APP_TAGS)))
				.setIconBytes(cursor.getBlob(cursor.getColumnIndex(APPInfoDBHelper.APP_ICON)))
				.setOpenCnt(cursor.getInt(cursor.getColumnIndex(APPInfoDBHelper.APP_OPEN_CNT)))
				.build();
			}
			
			cursor.close();
		}
		
		return appInfo;
	}
	
	public void addAppInfoList(List<AppInfo> appInfoList){
		for(AppInfo appInfo : appInfoList){
			AppInfo existAppInfo = getAppInfo(appInfo.getAppName());
			if(null == existAppInfo){
				ContentValues contentValues = new ContentValues();
				contentValues.put(APPInfoDBHelper.APP_NAME, appInfo.getAppName());
				contentValues.put(APPInfoDBHelper.APP_FIRST_SPELL, appInfo.getAppFirstSpell());
				contentValues.put(APPInfoDBHelper.APP_CLASS_NAME, appInfo.getClassName());
				contentValues.put(APPInfoDBHelper.APP_FULL_SPELL, appInfo.getAppFullSpell());
				contentValues.put(APPInfoDBHelper.APP_PACKAGE_NAME, appInfo.getPackageName());
				contentValues.put(APPInfoDBHelper.APP_TAGS, appInfo.getTags());
				contentValues.put(APPInfoDBHelper.APP_ICON, appInfo.getIconBytes());
				contentValues.put(APPInfoDBHelper.APP_OPEN_CNT, appInfo.getOpenCnt());
				mSqliteDatabase.insert(APPInfoDBHelper.TABLE_NAME, null, contentValues);
			}else{
				if(!appInfo.getPackageName().equals(existAppInfo.getPackageName())|| !appInfo.getClassName().equals(existAppInfo.getClassName()) || appInfo.getOpenCnt()!=existAppInfo.getOpenCnt()){
					// 包名或者类名变了或者是更新点击次数时，需要更新
					ContentValues contentValues = new ContentValues();
					contentValues.put(APPInfoDBHelper.APP_NAME, appInfo.getAppName());
					contentValues.put(APPInfoDBHelper.APP_FIRST_SPELL, appInfo.getAppFirstSpell());
					contentValues.put(APPInfoDBHelper.APP_CLASS_NAME, appInfo.getClassName());
					contentValues.put(APPInfoDBHelper.APP_FULL_SPELL, appInfo.getAppFullSpell());
					contentValues.put(APPInfoDBHelper.APP_PACKAGE_NAME, appInfo.getPackageName());
					contentValues.put(APPInfoDBHelper.APP_TAGS, appInfo.getTags());
					contentValues.put(APPInfoDBHelper.APP_ICON, appInfo.getIconBytes());
					contentValues.put(APPInfoDBHelper.APP_OPEN_CNT, appInfo.getOpenCnt());
					System.out.println("====== appName == " + appInfo.getAppName() + " openCnt == " + appInfo.getOpenCnt());
					mSqliteDatabase.update(APPInfoDBHelper.TABLE_NAME, contentValues, APPInfoDBHelper.APP_NAME + "=?", new String[]{appInfo.getAppName()});
				}
			}
		}
	}
	
	public void deleteAppInfoList(List<AppInfo> appInfoList){
		for(AppInfo appInfo : appInfoList){
			mSqliteDatabase.delete(APPInfoDBHelper.TABLE_NAME, APPInfoDBHelper.APP_NAME + "=?", new String[]{appInfo.getAppName()});
		}
	}
	
	public void closeDB(){
		if(null != mSqliteDatabase){
			mSqliteDatabase.close();
		}
		
		if(null != mAPPInfoDBHelper){
			mAPPInfoDBHelper.close();
		}
	}
}
