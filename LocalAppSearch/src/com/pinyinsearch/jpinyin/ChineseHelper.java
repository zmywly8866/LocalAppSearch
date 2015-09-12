package com.pinyinsearch.jpinyin;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;


import android.content.Context;

/**
 * 汉字简繁体转换类
 *
 * @version 1.0
 */
public class ChineseHelper {
	private static Properties CHINESE_TABLE = null;

	private ChineseHelper() {

	}

	private static Properties getChineseTable(Context context) {
		if (null == CHINESE_TABLE) {
			CHINESE_TABLE = PinyinResource.getChineseTable(context);
		}

		return CHINESE_TABLE;
	}

	/**
	 * 将单个繁体字转换为简体字
	 * 
	 * @param c
	 *            需要转换的繁体字
	 * @return 转换后的简体字
	 */
	public static char convertToSimplifiedChinese(Context context, char c) {
		if (isTraditionalChinese(context, c)) {
			return getChineseTable(context).getProperty(String.valueOf(c)).charAt(0);
		}
		return c;
	}

	/**
	 * 将单个简体字转换为繁体字
	 * 
	 * @param c
	 *            需要转换的简体字
	 * @return 转换后的繁字体
	 */
	public static char convertToTraditionalChinese(Context context, char c) {
		String hanzi = String.valueOf(c);
		if (getChineseTable(context).containsValue(hanzi)) {
			Iterator<Entry<Object, Object>> itr = getChineseTable(context).entrySet().iterator();
			while (itr.hasNext()) {
				Entry<Object, Object> e = itr.next();
				if (e.getValue().toString().equals(hanzi)) {
					return e.getKey().toString().charAt(0);
				}
			}
		}
		return c;
	}

	/**
	 * 将繁体字转换为简体字
	 * 
	 * @param str
	 *            需要转换的繁体字
	 * @return 转换后的简体体
	 */
	public static String convertToSimplifiedChinese(Context context, String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);
			sb.append(convertToSimplifiedChinese(context, c));
		}
		return sb.toString();
	}

	/**
	 * 将简体字转换为繁体字
	 * 
	 * @param str
	 *            需要转换的简体字
	 * @return 转换后的繁字体
	 */
	public static String convertToTraditionalChinese(Context context, String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);
			sb.append(convertToTraditionalChinese(context, c));
		}
		return sb.toString();
	}

	/**
	 * 判断某个字符是否为繁体字
	 * 
	 * @param c
	 *            需要判断的字符
	 * @return 是繁体字返回true，否则返回false
	 */
	public static boolean isTraditionalChinese(Context context, char c) {
		return getChineseTable(context).containsKey(String.valueOf(c));
	}

	/**
	 * 判断某个字符是否为汉字
	 * 
	 * @param c
	 *            需要判断的字符
	 * @return 是汉字返回true，否则返回false
	 */
	public static boolean isChinese(char c) {
		String regex = "[\\u4e00-\\u9fa5]";
		return String.valueOf(c).matches(regex);
	}
}
