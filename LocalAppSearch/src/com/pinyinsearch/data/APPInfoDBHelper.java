package com.pinyinsearch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class APPInfoDBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "applist.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "applist";
	public static final String APP_NAME = "app_name";
	public static final String APP_FULL_SPELL = "app_full_spell";
	public static final String APP_FIRST_SPELL = "app_first_spell";
	public static final String APP_PACKAGE_NAME = "app_package_name";
	public static final String APP_CLASS_NAME = "app_class_name";
	public static final String APP_TAGS = "app_tags";
	public static final String APP_ICON = "app_icon";
	public static final String APP_OPEN_CNT = "app_open_cnt";
	
	public APPInfoDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ APP_NAME + " TEXT, "
				+ APP_FULL_SPELL + " TEXT, "
				+ APP_FIRST_SPELL + " TEXT, "
				+ APP_PACKAGE_NAME + " TEXT, "
				+ APP_CLASS_NAME + " TEXT, "
				+ APP_TAGS + " TEXT, "
				+ APP_ICON + " BLOB, "
				+ APP_OPEN_CNT + " INTEGER"
				+ ")";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion != newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

}
