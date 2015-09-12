package com.pinyinsearch.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uperone.pinyinsearch.R;

/**
 * T9拼音键盘
 * 
 * */
public class T9View extends RelativeLayout implements OnClickListener{
	private String mKeyWords = "";
	private TextView mKeyWordsTxt = null;
	private OnKeyClickListener mOnKeyClickListener = null;
	private Context mContext = null;
	public T9View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initT9View(context);
	}

	public T9View(Context context, AttributeSet attrs) {
		super(context, attrs);
		initT9View(context);
	}

	public T9View(Context context) {
		super(context);
		initT9View(context);
	}
	
	@Override
	public void onClick(View v) {
		if(null == mOnKeyClickListener){
			return;
		}
		switch(v.getId()){
		case R.id.key1BtnId: {
			mKeyWords += "1";
			mOnKeyClickListener.onClickNumber("1");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key2BtnId: {
			mKeyWords += "2";
			mOnKeyClickListener.onClickNumber("2");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key3BtnId: {
			mKeyWords += "3";
			mOnKeyClickListener.onClickNumber("3");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key4BtnId: {
			mKeyWords += "4";
			mOnKeyClickListener.onClickNumber("4");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key5BtnId: {
			mKeyWords += "5";
			mOnKeyClickListener.onClickNumber("5");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key6BtnId: {
			mKeyWords += "6";
			mOnKeyClickListener.onClickNumber("6");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key7BtnId: {
			mKeyWords += "7";
			mOnKeyClickListener.onClickNumber("7");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key8BtnId: {
			mKeyWords += "8";
			mOnKeyClickListener.onClickNumber("8");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key9BtnId: {
			mKeyWords += "9";
			mOnKeyClickListener.onClickNumber("9");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key10BtnId: {
			mOnKeyClickListener.onClickBack();
		}
			break;
		case R.id.key11BtnId: {
			mKeyWords += "0";
			mOnKeyClickListener.onClickNumber("0");
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		case R.id.key12BtnId: {
			mOnKeyClickListener.onClickDelete();
			if(!TextUtils.isEmpty(mKeyWords)){
				mKeyWords = mKeyWords.substring(0, mKeyWords.length()-1);
			}
			mOnKeyClickListener.onSearchText(mKeyWords);
		}
			break;
		default: {

		}
			break;
		}
		
		mKeyWordsTxt.setText(TextUtils.isEmpty(mKeyWords)?mContext.getResources().getString(R.string.long_click_start_voice):mKeyWords);
	}
	
	public void setOnKeyClickListener(OnKeyClickListener onKeyClickListener){
		mOnKeyClickListener = onKeyClickListener;
	}
	
	private void initT9View(Context context){
		mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.view_9keyboard_layout, null);
		view.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
	
		mKeyWordsTxt = (TextView)view.findViewById(R.id.keyWordsTxtId);
		mKeyWordsTxt.setText(mContext.getResources().getString(R.string.long_click_start_voice));
		mKeyWordsTxt.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(null != mOnKeyClickListener){
					mOnKeyClickListener.onClickVoice();
				}
				return false;
			}
		});
		
		Button key1Btn = (Button)view.findViewById(R.id.key1BtnId);
		key1Btn.setText(getAlphaFormatString("1"));
		key1Btn.setOnClickListener(this);
		
		Button key2Btn = (Button)view.findViewById(R.id.key2BtnId);
		key2Btn.setText(getFormatString("2 ABC"));
		key2Btn.setOnClickListener(this);
		
		Button key3Btn = (Button)view.findViewById(R.id.key3BtnId);
		key3Btn.setText(getFormatString("3 DEF"));
		key3Btn.setOnClickListener(this);
		
		Button key4Btn = (Button)view.findViewById(R.id.key4BtnId);
		key4Btn.setText(getFormatString("4 GHI"));
		key4Btn.setOnClickListener(this);
		
		Button key5Btn = (Button)view.findViewById(R.id.key5BtnId);
		key5Btn.setText(getFormatString("5 JKL"));
		key5Btn.setOnClickListener(this);
		
		Button key6Btn = (Button)view.findViewById(R.id.key6BtnId);
		key6Btn.setText(getFormatString("6 MNO"));
		key6Btn.setOnClickListener(this);
		
		Button key7Btn = (Button)view.findViewById(R.id.key7BtnId);
		key7Btn.setText(getFormatString("7 PQRS"));
		key7Btn.setOnClickListener(this);
		
		Button key8Btn = (Button)view.findViewById(R.id.key8BtnId);
		key8Btn.setText(getFormatString("8 TUV"));
		key8Btn.setOnClickListener(this);
		
		Button key9Btn = (Button)view.findViewById(R.id.key9BtnId);
		key9Btn.setText(getFormatString("9 WXYZ"));
		key9Btn.setOnClickListener(this);
		
		Button key10Btn = (Button)view.findViewById(R.id.key10BtnId);
		key10Btn.setOnClickListener(this);
		
		Button key11Btn = (Button)view.findViewById(R.id.key11BtnId);
		key11Btn.setText(getAlphaFormatString("0"));
		key11Btn.setOnClickListener(this);
		
		Button key12Btn = (Button)view.findViewById(R.id.key12BtnId);
		key12Btn.setOnClickListener(this);
		
		addView(view);
	}
	
	private SpannableString getFormatString(String srcString){
		SpannableString srcSpannableString = new SpannableString(srcString);
		srcSpannableString.setSpan(new ForegroundColorSpan(0x7fffffff), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		srcSpannableString.setSpan(new AbsoluteSizeSpan((int)mContext.getResources().getDimension(R.dimen.toast_text_size)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
		
		return srcSpannableString;
	}
	
	private SpannableString getAlphaFormatString(String srcString){
		SpannableString srcSpannableString = new SpannableString(srcString);
		srcSpannableString.setSpan(new ForegroundColorSpan(0x7fffffff), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		return srcSpannableString;
	}
	
	public String getStringsNumber(String string){
		StringBuilder numbers = new StringBuilder();
		char[] strings = string.toCharArray();
		for(char charater : strings){
			numbers.append(getStringNumber(charater+""));
		}
		
		return numbers.toString();
	}
	
	private int getStringNumber(String charater){
		charater = charater.toLowerCase();
		if(charater.equals("a")||charater.equals("b")||charater.equals("c")){
			return 2;
		}else if(charater.equals("d")||charater.equals("e")||charater.equals("f")){
			return 3;
		}else if(charater.equals("g")||charater.equals("h")||charater.equals("i")){
			return 4;
		}else if(charater.equals("j")||charater.equals("k")||charater.equals("l")){
			return 5;
		}else if(charater.equals("m")||charater.equals("n")||charater.equals("o")){
			return 6;
		}else if(charater.equals("p")||charater.equals("q")||charater.equals("r")||charater.equals("s")){
			return 7;
		}else if(charater.equals("t")||charater.equals("u")||charater.equals("v")){
			return 8;
		}else if(charater.equals("w")||charater.equals("x")||charater.equals("y")||charater.equals("z")){
			return 9;
		}else if(isNumeric(charater)){
			// 如果是数字，是几就是几
			return Integer.parseInt(charater);
		}
		
		return 0;
	}
	
	public boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   }
		   
		   return true; 
		}
	
	public interface OnKeyClickListener{
		public void onClickNumber(String number);
		public void onSearchText(String keyWords);
		public void onClickBack();
		public void onClickDelete();
		public void onClickVoice();
	}
}
