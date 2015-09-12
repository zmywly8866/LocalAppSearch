package com.pinyinsearch.ui;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonParser {
	private JsonParser(){
		
	}
	
	public static String parseIatResult(String json) {
		StringBuilder ret = new StringBuilder();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 转写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
			}
		} catch (Exception e) {
			
		}
		
		return ret.toString();
	}

	public static String parseGrammarResult(String json) {
		StringBuilder ret = new StringBuilder();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				for (int j = 0; j < items.length(); j++) {
					JSONObject obj = items.getJSONObject(j);
					if (obj.getString("w").contains("nomatch")) {
						ret.append("没有匹配结果.");
						return ret.toString();
					}
					ret.append("【结果】").append(obj.getString("w"));
					ret.append("【置信度】").append(obj.getInt("sc"));
					ret.append("\n");
				}
			}
		} catch (Exception e) {
			ret.append("没有匹配结果.");
		}
		
		return ret.toString();
	}
}
