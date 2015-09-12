package com.pinyinsearch.data;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private String appName;
	private String appFullSpell;
	private String appFirstSpell;
	private String packageName;
	private String className;
	private String tags;
	private Drawable icon;
	private int openCnt;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppFullSpell() {
		return appFullSpell;
	}

	public void setAppFullSpell(String appFullSpell) {
		this.appFullSpell = appFullSpell;
	}

	public String getAppFirstSpell() {
		return appFirstSpell;
	}

	public void setAppFirstSpell(String appFirstSpell) {
		this.appFirstSpell = appFirstSpell;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	public int getOpenCnt() {
		return openCnt;
	}

	public void setOpenCnt(int openCnt) {
		this.openCnt = openCnt;
	}

	public static class Builder{
		private String appName;
		private String appFullSpell;
		private String appFirstSpell;
		private String packageName;
		private String className;
		private String tags;
		private Drawable icon;
		private int openCnt;
		
		public Builder setAppName(String appName){
			this.appName = appName;
			return this;
		}
		
		public Builder setFullSpell(String fullSpell){
			this.appFullSpell = fullSpell;
			return this;
		}
		
		public Builder setAppFirstSpell(String appFirstSpell){
			this.appFirstSpell = appFirstSpell;
			return this;
		}
		
		public Builder setPackageName(String packageName){
			this.packageName = packageName;
			return this;
		}
		
		public Builder setClassName(String className){
			this.className = className;
			return this;
		}
		
		public Builder setTags(String tags){
			this.tags = tags;
			return this;
		}
		
		public Builder setIcon(Drawable icon){
			this.icon = icon;
			return this;
		}
		
		public Builder setOpenCnt(int openCnt){
			this.openCnt = openCnt;
			return this;
		}
		
		public AppInfo build(){
			return new AppInfo(this);
		}
	}
	
	private AppInfo(Builder builder){
		this.appName = builder.appName;
		this.appFullSpell = builder.appFullSpell;
		this.appFirstSpell = builder.appFirstSpell;
		this.packageName = builder.packageName;
		this.className = builder.className;
		this.tags = builder.tags;
		this.icon = builder.icon;
		this.openCnt = builder.openCnt;
	}

	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", appFullSpell=" + appFullSpell + ", appFirstSpell=" + appFirstSpell
				+ ", packageName=" + packageName + ", className=" + className + ", tags=" + tags + ", openCnt="
				+ openCnt + "]";
	}
}
