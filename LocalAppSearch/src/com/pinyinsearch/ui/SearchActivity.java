package com.pinyinsearch.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.pinyinsearch.data.APPManager;
import com.pinyinsearch.data.AppInfo;
import com.pinyinsearch.ui.T9View.OnKeyClickListener;
import com.pinyinsearch.util.APPUtils;
import com.uperone.pinyinsearch.R;

public class SearchActivity extends Activity{
	private ListView mAppListView = null;
	private TextView mToastTxtView = null;
	private AppListAdapter mAppListAdapter = null;
	private T9View mDialpadView = null;
	private APPManager mAppManager = null;
	private LoadAppInfoTask mLoadAppInfoTask = null;
	private UpdateAppInfoTask mUpdateAppInfoTask = null;
	private List<AppInfo> mAppInfoList = new ArrayList<AppInfo>();
	
	// 语音听写对象
	private SpeechRecognizer mSpeechRecognizer = null;
	// 语音听写UI
	private RecognizerDialog mRecognizerDialog = null;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_search);
		mAppListView = (ListView)findViewById(R.id.appListViewId);
		mToastTxtView = (TextView)findViewById(R.id.toastTxtId);
		mAppListAdapter = new AppListAdapter(this);
		mDialpadView = (T9View)findViewById(R.id.appDialerId);
		mDialpadView.setOnKeyClickListener(new OnKeyClickListener() {
			@Override
			public void onSearchText(String keyWords) {
				System.out.println("====== onSearchText keyWords == " + keyWords);
				updateList(keyWords);
			}
			
			@Override
			public void onClickNumber(String number) {
				System.out.println("====== onClickNumber number == " + number);
			}
			
			@Override
			public void onClickDelete() {
				System.out.println("====== onClickDelete");
			}
			
			@Override
			public void onClickBack() {
				System.out.println("====== onClickBack");
				finish();
			}

			@Override
			public void onClickVoice() {
				startRecong();
			}
		});
		mAppListView.setAdapter(mAppListAdapter);
		mAppListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppInfo appInfo = mAppListAdapter.getAppInfoList().get(position);
				appInfo.setOpenCnt(appInfo.getOpenCnt()+1);
				System.out.println("====== onAddDialCharacter packageName == " + appInfo.getPackageName() + " className == " + appInfo.getClassName());
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(appInfo.getPackageName(), appInfo.getClassName()));
				startActivity(intent);
				
				// 更新打开次数
				List<AppInfo> updateAppInfoList = new ArrayList<AppInfo>();
				updateAppInfoList.add(appInfo);
				mAppManager.addAppInfoList(updateAppInfoList);
				
				mAppListAdapter.notifyDataSetChanged();
			}
		});
		
		mAppManager = new APPManager(this);
		getAppInfo();
		initiFlytek();
	}
	
	public void getAppInfo(){
		mLoadAppInfoTask = new LoadAppInfoTask();
		mLoadAppInfoTask.execute();
	}
	
	private void updateList(String keyWords){
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		if(TextUtils.isEmpty(keyWords)){
			mAppListAdapter.setAppInfoList(appInfoList);
			setToastTxt(getResources().getString(R.string.dial_or_long_click_search_app));
			return;
		}
		
		String stringNumber = null;
		for(AppInfo appInfo : mAppInfoList){
			stringNumber = mDialpadView.getStringsNumber(appInfo.getAppFirstSpell());
			System.out.println("====== firstSpell == " + appInfo.getAppFirstSpell() + " stringNumber == " + stringNumber + " keyWords == " + keyWords);
			if(stringNumber.startsWith(keyWords)){
				appInfoList.add(appInfo);
			}
		}
		
		showResult(appInfoList);
	}
	
	private void updateList4Voice(String keyWords){
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		if(TextUtils.isEmpty(keyWords)){
			mAppListAdapter.setAppInfoList(appInfoList);
			setToastTxt(getResources().getString(R.string.dial_or_long_click_search_app));
			return;
		}
		
		String stringNumber = null;
		for(AppInfo appInfo : mAppInfoList){
			System.out.println("====== firstSpell == " + appInfo.getAppFirstSpell() + " stringNumber == " + stringNumber + " keyWords == " + keyWords);
			if(appInfo.getAppName().contains(keyWords)){
				appInfoList.add(appInfo);
			}
		}
		
		showResult(appInfoList);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null != mAppInfoList){
			mAppInfoList.clear();
		}
		
		if(null != mLoadAppInfoTask){
			mLoadAppInfoTask.cancel(true);
			mLoadAppInfoTask = null;
		}
		
		if(null != mUpdateAppInfoTask){
			mUpdateAppInfoTask.cancel(true);
			mUpdateAppInfoTask = null;
		}
		
		if(null != mAppManager){
			mAppManager.closeDB();
		}
		
		uninitiFlytek();
	}
	
	class LoadAppInfoTask extends AsyncTask<Void, Void, List<AppInfo>>{
		@Override
		protected List<AppInfo> doInBackground(Void... params) {
			List<AppInfo> appInfoList = mAppManager.getAppInfoList();
			if(null == appInfoList || appInfoList.size() == 0){
				setToastTxt(SearchActivity.this.getResources().getString(R.string.first_in_init_app_list));
				appInfoList = APPUtils.getAppInfoList(SearchActivity.this);
				mAppManager.addAppInfoList(appInfoList);
			}
			
			return appInfoList;
		}

		@Override
		protected void onPostExecute(List<AppInfo> result) {
			super.onPostExecute(result);
			mAppInfoList = result;
			List<AppInfo> appInfoList = getOpenedAppList(mAppInfoList);
			showResult(appInfoList);
			mUpdateAppInfoTask = new UpdateAppInfoTask();
			mUpdateAppInfoTask.execute();
		}

	}
	
	class UpdateAppInfoTask extends AsyncTask<Void, Void, List<AppInfo>>{
		@Override
		protected List<AppInfo> doInBackground(Void... params) {
			List<AppInfo> localAppInfoList = APPUtils.getAppInfoList(SearchActivity.this);
			List<AppInfo> savedAppInfoList = mAppManager.getAppInfoList();
			List<AppInfo> deleteAppInfoList = getDeleteAppInfoList(savedAppInfoList, localAppInfoList);
			if(!isListEmpty(deleteAppInfoList)){
				mAppManager.deleteAppInfoList(deleteAppInfoList);
			}
			List<AppInfo> updateAppInfoList = getUpdateAppInfoList(savedAppInfoList, localAppInfoList);
			if(!isListEmpty(updateAppInfoList)){
				mAppManager.addAppInfoList(updateAppInfoList);
			}
			
			return mAppManager.getAppInfoList();
		}

		@Override
		protected void onPostExecute(List<AppInfo> result) {
			super.onPostExecute(result);
			mAppInfoList = result;
		}
	}
	
	private boolean isListEmpty(List<AppInfo> appInfoList){
		return null==appInfoList||appInfoList.size()==0;
	}
	
	/**
	 * 得到新增或者有更新的APP列表
	 * 
	 * */
	private List<AppInfo> getUpdateAppInfoList(List<AppInfo> savedAppInfoList, List<AppInfo> localAppInfoList){
		boolean exist = false;
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		for(AppInfo localAppInfo : localAppInfoList){
			exist = false;
			for(AppInfo savedAppInfo : savedAppInfoList){
				if(localAppInfo.getAppName().equals(savedAppInfo.getAppName()) || localAppInfo.getPackageName().equals(savedAppInfo.getPackageName())){
					exist = true;
					if(!localAppInfo.getAppName().equals(savedAppInfo.getAppName())
							|| !localAppInfo.getPackageName().equals(savedAppInfo.getPackageName())
							|| !localAppInfo.getClassName().equals(savedAppInfo.getClassName())){
						// 如果包名、APP名称、入口名有变化就需要加进来
						appInfoList.add(localAppInfo);
					}
					break;
				}
			}
			
			// 如果没有找到匹配的，说明是新增的，需要加进来
			if(!exist){
				appInfoList.add(localAppInfo);
			}
		}
		
		return appInfoList;
	}
	
	/**
	 * 得到已经卸载的APP列表
	 * 
	 * */
	private List<AppInfo> getDeleteAppInfoList(List<AppInfo> savedAppInfoList, List<AppInfo> localAppInfoList){
		boolean delete = true;
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		for(AppInfo savedAppInfo : savedAppInfoList){
			delete = true;
			for(AppInfo localAppInfo : localAppInfoList){
				if(localAppInfo.getAppName().equals(savedAppInfo.getAppName())){
					delete = false;
					break;
				}
			}
			
			if(delete){
				appInfoList.add(savedAppInfo);
			}
		}
		
		return appInfoList;
	}
	
	/**
	 * 得到打开过的应用列表，并根据打开频次排序
	 * 
	 * */
	private List<AppInfo> getOpenedAppList(List<AppInfo> existAppInfoList){
		List<AppInfo> appInfoList = new ArrayList<AppInfo>();
		for(AppInfo appInfo : existAppInfoList){
			if(appInfo.getOpenCnt()>0){
				appInfoList.add(appInfo);
			}
		}
		
		return appInfoList;
	}
	
	/**
	 * 初始化语音识别对象
	 */
	private void initiFlytek() {
		// 初始化识别对象
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + getString(R.string.app_id));
		mSpeechRecognizer = SpeechRecognizer.createRecognizer(this, miFlytekInitListener);
	}

	/**
	 * 退出时释放连接
	 */
	private void uninitiFlytek() {
		// 退出时释放连接
		if (null != mSpeechRecognizer) {
			mSpeechRecognizer.cancel();
			mSpeechRecognizer.destroy();
		}
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener miFlytekInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败,错误码：" + code);
			}
		}
	};

	private void showTip(final String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 开始录音
	 * 
	 */
	private void startRecong() {
		setiFlytekParam();
		if (null == mRecognizerDialog) {
			// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
			mRecognizerDialog = new RecognizerDialog(this, miFlytekInitListener);
		}

		// 显示听写对话框
		mRecognizerDialog.setListener(mRecognizerDialogListener);
		mRecognizerDialog.setOnDismissListener(mOnDismissListener);
		mRecognizerDialog.show();
	}

	private OnDismissListener mOnDismissListener = new OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface dialog) {
			
		}
	};

	/**
	 * 听写UI监听器
	 * 
	 */
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			String result = JsonParser.parseIatResult(results.getResultString());
			if (!TextUtils.isEmpty(result)) {
				if (result.contains("。")) {
					result = result.substring(0, (result.length() - 1));
				}
				
				if(!TextUtils.isEmpty(result)){
					updateList4Voice(result);
				}
			}
		}

		/**
		 * 识别回调错误
		 * 
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	/**
	 * 参数设置
	 * 
	 */
	private void setiFlytekParam() {
		// 设置语言
		mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置语言区域
		mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");
		// 设置语音前端点
		mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, "4000");
		// 设置语音后端点
		mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
		// 设置标点符号
		mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");
		// 设置音频保存路径
		mSpeechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory().toString() + "/iflytek/wavaudio.pcm");
	}
	
	private void setToastTxt(final String toast){
		SearchActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAppListView.setVisibility(View.GONE);
				mToastTxtView.setVisibility(View.VISIBLE);
				mToastTxtView.setText(toast);
			}
		});
	}
	
	private void showListView(){
		SearchActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAppListView.setVisibility(View.VISIBLE);
				mToastTxtView.setVisibility(View.GONE);
			}
		});
	}
	
	private void showResult(List<AppInfo> appInfoList) {
		if(null == appInfoList || appInfoList.size() == 0){
			setToastTxt(SearchActivity.this.getResources().getString(R.string.dial_or_long_click_search_app));
		}else{
			showListView();
			mAppListAdapter.setAppInfoList(appInfoList);
		}
	}
}
