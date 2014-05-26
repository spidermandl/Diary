package com.diary.goal.setting.activity;

import java.util.ArrayList;
import java.util.Date;

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
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.ScrollView;
/**
 * 日记回顾界面
 * @author Desmond Duan
 *
 */
public class PaperOverviewActivity extends SherlockActivity {

	private static final int MAIN_TITLE=0;
	private static final int SUB_TITLE=1;
	private static final int PLAIN_TEXT=2;
	private static final int STAR_TITLE=3;

	JSONObject diaryText;
	ArrayList<Spanned> textSpans=new ArrayList<Spanned>();
	ArrayList<Integer> mainTitPos=new ArrayList<Integer>();
	ArrayList<Integer> subTitPos=new ArrayList<Integer>();
	ArrayList<Integer> txtPos=new ArrayList<Integer>();
	ArrayList<Integer> starPos=new ArrayList<Integer>();
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
	/**
	 * 初始日记显示内容
	 */
	private void initDiaryText(){
		/**
		 * 取数据库日记
		 */
		Date date=new Date(getIntent().getLongExtra("review_date", (new Date()).getTime()));
		try {
			String rawDiary=DiaryApplication.getInstance().getDbHelper().getDiaryContent(date);
			if(rawDiary!=null)
				diaryText=new JSONObject(rawDiary);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/**
		 * 解析日记
		 */
		StringBuffer buffer=new StringBuffer();
		int start=0,end=0;
		if(diaryText!=null){
			try {
				JSONArray array=diaryText.getJSONArray(Constant.MAIN_SEQUENCE_ORDER);
				for(int i=0;i<array.length();i++){//大标题循环
					String mainTit=array.getString(i);//大标题
					JSONObject subObj=diaryText.getJSONObject(mainTit);
					if(mainTit.length()>0){
						buffer.append(mainTit).append("          ").append('\n');
						mainTitPos.add(start);end=start+mainTit.length()+11;mainTitPos.add(end);
						start=start+mainTit.length();
						float index=Float.valueOf(subObj.getString(Constant.MAIN_STATUS));
						for(int d=0;d<(int)(index+0.5);d++){
							starPos.add(start);starPos.add(start+2);
							start=start+2;
						}
						start=end;
					}
					JSONArray subArr=subObj.getJSONArray(Constant.SUB_SEQUENCE_ORDER);
					for(int j=0;j<subArr.length();j++){//小标题循环
						String subTit=subArr.getString(j);//小标题
						if(subTit.length()>0){
							buffer.append("  ").append('[').append(subTit).append(']');
							subTitPos.add(start);end=start+subTit.length()+4;subTitPos.add(end);
							start=end;
							
						}
						String text = subObj.getString(subTit);//正文
						if(text!=null&&text.length()!=0){//正文不为空
							txtPos.add(start);end=start+text.length();
							if(text.charAt(0)!='\n'){
								buffer.append('\n');
								end++;
							}
							buffer.append(text);
							if(text.charAt(text.length()-1)!='\n'){
								buffer.append('\n');
								end++;
							}
							txtPos.add(end);
							start=end;
						}else{//小标题后加换行
							buffer.append('\n');
							end++;
							start=end;
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		 * 设置字体样式
		 */
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
			setSpannableString(wholeText, PLAIN_TEXT, txtPos.get(index), txtPos.get(index+1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			index=index+2;
		}
		index=0;
		while (index<starPos.size()){
			setSpannableString(wholeText, STAR_TITLE, starPos.get(index), starPos.get(index+1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
			ss.setSpan(new RelativeSizeSpan(1.5f), start, end, flags);
			ss.setSpan(new ForegroundColorSpan(Color.GRAY), start, end, flags);
			
			break;
			
		case SUB_TITLE:
			ss.setSpan(new RelativeSizeSpan(1.2f), start, end, flags);
			ss.setSpan(new ForegroundColorSpan(Color.RED), start, end, flags);
			//ss.setSpan(new StyleSpan(Typeface.BOLD) , start, end, flags);
			break;
			
		case PLAIN_TEXT:
			ss.setSpan(new RelativeSizeSpan(1.0f), start, end, flags);
			ss.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, flags);
			break;
		
		case STAR_TITLE:
			ss.setSpan(new ImageSpan(this,android.R.drawable.star_on), start, end, flags);
		default:
			ss.setSpan(new RelativeSizeSpan(1.0f), start, end, flags);
			ss.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, flags);
			break;
		}
	}
}
