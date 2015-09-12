package com.pinyinsearch.jpinyin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * 资源文件加载类
 *
 * @version 1.0
 */
public class PinyinResource {
	private static Properties getResource(Context context, String resourceName) {
		InputStream is = null;
		Properties props = null;
		try {
			is = context.getAssets().open(resourceName);
			props = new Properties();
			props.load(is);
		} catch (IOException e) {
			
		} finally {
			try {
				if (null != is) {
					is.close();
				}
			} catch (IOException e) {
				
			}
		}
		return props;
	}

	protected static Properties getPinyinTable(Context context) {
		String resourceName = "pinyin/pinyin.properties";
		return getResource(context, resourceName);
	}

	protected static Properties getMutilPintinTable(Context context) {
		String resourceName = "pinyin/mutil_pinyin.properties";
		return getResource(context, resourceName);
	}

	protected static Properties getChineseTable(Context context) {
		String resourceName = "pinyin/chinese.properties";
		return getResource(context, resourceName);
	}
}
