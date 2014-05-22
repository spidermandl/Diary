package com.diary.goal.setting.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.view.QuickView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.ScrollView;

public class PaperOverviewActivity extends SherlockActivity {

	private static final int MAIN_TITLE=0;
	private static final int SUB_TITLE=1;
	private static final int PLAIN_TEXT=2;

	JSONObject diaryText;
	ArrayList<Spanned> textSpans=new ArrayList<Spanned>();
	ArrayList<Integer> mainTitPos=new ArrayList<Integer>();
	ArrayList<Integer> subTitPos=new ArrayList<Integer>();
	ArrayList<Integer> txtPos=new ArrayList<Integer>();
	QuickView textPanel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.paper_overview);
		super.onCreate(savedInstanceState);
		
		ScrollView scrollView=(ScrollView)this.findViewById(R.id.paper_scroll);
		scrollView.setBackgroundDrawable(new BitmapDrawable(BitmapCustomize.customizePicture(this, R.drawable.paper_overview_diary,
				DiaryApplication.getInstance().getScreen_w(), DiaryApplication.getInstance().getScreen_h(), false)));
		
		textPanel=(QuickView)this.findViewById(R.id.diary_review);
		
		final ActionBar ab = getSupportActionBar();
		
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		
		initDiaryText();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			//this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
			break;
		default:
			break;
		}
		return true;
	}
	
	private void initDiaryText(){
		StringBuffer buffer=new StringBuffer();
		int start=0,end=0;
		if(diaryText!=null){
			try {
				JSONArray array=diaryText.getJSONArray(Constant.MAIN_SEQUENCE_ORDER);
				for(int i=0;i<array.length();i++){//大标题循环
					String mainTit=array.getString(i);//大标题
					JSONObject subObj=diaryText.getJSONObject(mainTit);
					mainTitPos.add(start);end=start+mainTit.length();mainTitPos.add(end);start=end;
					buffer.append(mainTit);
					JSONArray subArr=subObj.getJSONArray(Constant.SUB_SEQUENCE_ORDER);
					for(int j=0;j<subArr.length();j++){//小标题循环
						String subTit=subArr.getString(j);//小标题
						buffer.append(subTit);
						subTitPos.add(start);end=start+subTit.length();subTitPos.add(end);start=end;
						String text = subObj.getString(subTit);//正文
						if(text!=null&&text.length()!=0){
							txtPos.add(start);end=start+text.length();subTitPos.add(end);start=end;
							buffer.append(text);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		SpannableString wholeText=new SpannableString(buffer.toString());
		int index=0;
		while (index<mainTitPos.size()) {
			setSpannableString(wholeText, MAIN_TITLE, mainTitPos.get(index), mainTitPos.get(index+1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			index=index+2;
		}
		index=0;
		while (index<subTitPos.size()) {
			setSpannableString(wholeText, SUB_TITLE, subTitPos.get(index), subTitPos.get(index+1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			index=index+2;
		}
		index=0;
		while (index<txtPos.size()) {
			setSpannableString(wholeText, PLAIN_TEXT, subTitPos.get(index), txtPos.get(index+1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			index=index+2;
		}
		
		textPanel.setText(wholeText);
	}
	/**
	 * 设置字体样式
	 * @param ss
	 * @param type
	 * @param start
	 * @param end
	 * @param flags
	 */
	private void setSpannableString(SpannableString ss,int type, int start, int end, int flags){
		switch (type) {
		case MAIN_TITLE:
			ss.setSpan(new AbsoluteSizeSpan(20), start, end, flags);
			ss.setSpan(new ForegroundColorSpan(Color.GRAY), start, end, flags);
			
			break;
			
		case SUB_TITLE:
			ss.setSpan(new AbsoluteSizeSpan(16), start, end, flags);
			ss.setSpan(new ForegroundColorSpan(Color.RED), start, end, flags);
			ss.setSpan(new StyleSpan(Typeface.BOLD) , start, end, flags);
			break;
			
		case PLAIN_TEXT:
			ss.setSpan(new AbsoluteSizeSpan(12), start, end, flags);
			ss.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, flags);
			break;

		default:
			ss.setSpan(new AbsoluteSizeSpan(12), start, end, flags);
			ss.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, flags);
			break;
		}
	}
}
